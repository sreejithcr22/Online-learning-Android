package com.codit.interview.aptitude.presentation.mock

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.core.coroutines.tickerFlow
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.domain.model.QuestionSource
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import com.codit.interview.aptitude.domain.usecase.RecordMockScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Immutable
data class MockSessionUiState(
    val isLoading: Boolean = true,
    val title: String = "",
    val question: Question? = null,
    val currentNumber: Int = 1,
    val totalQuestions: Int = 0,
    val navigator: QuestionNavigator = QuestionNavigator(0, emptyList()),
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val remainingSeconds: Int = 0,
    val isTimeUp: Boolean = false,
    val isFinished: Boolean = false,
    val correctCount: Int = 0,
    val canGoPrevious: Boolean = false,
    val canGoNext: Boolean = false,
) {
    val displayTime: String get() = TimeFormat.countdown(remainingSeconds)
    val attemptedCount: Int get() = correctCount + navigator.wrongCount

    /** True once the answer is shown — either submitted now, or stored from a past run. */
    val isRevealed: Boolean get() = isSubmitted || question?.isAnswered == true

    /**
     * The option to highlight.
     *
     * Falls back to the answer stored in the database so a resumed question marks the
     * user's previous choice, not just the correct one.
     */
    val highlightedOptionIndex: Int?
        get() = if (isSubmitted) selectedOptionIndex else question?.answeredOptionIndex

    val isAnswerCorrect: Boolean
        get() = if (isSubmitted) isCorrect else question?.attemptStatus == AttemptStatus.CORRECT
}

sealed interface MockSessionEvent {
    data object TimeUp : MockSessionEvent
    data class Toast(val message: String) : MockSessionEvent
}

/**
 * A running mock test.
 *
 * Unlike a practice session this has one countdown for the whole test (not per
 * question), and explanations stay locked until the test is submitted. The legacy
 * implementation spread this across `MockQueActivity` and `QuestionFragBase` with the
 * `APPSTATE.MOCK_TEST_ON` flag deciding which behaviour applied.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class MockSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questions: QuestionRepository,
    private val recordScore: RecordMockScoreUseCase,
) : ViewModel() {

    private val title: String = checkNotNull(savedStateHandle.get<String>(ARG_TITLE))
    private val table: String = checkNotNull(savedStateHandle.get<String>(ARG_TABLE))
    private val durationSeconds: Int = savedStateHandle.get<String>(ARG_SECONDS)?.toIntOrNull() ?: 0

    private val source = QuestionSource.OfMockTest(
        MockTest(
            index = table.removePrefix("mock").toIntOrNull() ?: 1,
            title = title,
            score = 0,
            isFinished = false,
            isLocked = false,
            questionCount = 0,
        )
    )

    private val currentNumber = MutableStateFlow(1)
    private val selection = MutableStateFlow<Pair<Int, Boolean>?>(null)
    private val remaining = MutableStateFlow(durationSeconds)
    private val isFinished = MutableStateFlow(false)

    private val _events = MutableStateFlow<MockSessionEvent?>(null)
    val events: StateFlow<MockSessionEvent?> = _events.asStateFlow()

    private var tickerJob: Job? = null
    private var lastHandledNumber = NO_QUESTION

    private val questionFlow = currentNumber
        .flatMapLatest { number -> questions.observeQuestion(source, number).map { number to it } }

    val uiState: StateFlow<MockSessionUiState> = combine(
        questionFlow,
        questions.observeNavigator(source),
        selection,
        remaining,
        isFinished,
    ) { (number, question), navigator, selectionState, remainingSeconds, finished ->
        MockSessionUiState(
            isLoading = false,
            title = title,
            question = question,
            currentNumber = number,
            totalQuestions = navigator.total,
            navigator = navigator,
            selectedOptionIndex = selectionState?.first,
            isSubmitted = selectionState?.second == true,
            isCorrect = selectionState != null && selectionState.second &&
                selectionState.first == question?.correctOptionIndex,
            remainingSeconds = remainingSeconds,
            isTimeUp = remainingSeconds <= 0,
            isFinished = finished,
            correctCount = navigator.correctCount,
            canGoPrevious = number > 1,
            canGoNext = number < navigator.total,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = MockSessionUiState(isLoading = true, title = title),
    )

    init {
        // Reset the answer selection whenever a *different* question becomes current.
        // Re-emissions caused by our own writes must not clear it.
        questionFlow
            .onEach { (number, _) ->
                if (number != lastHandledNumber) {
                    lastHandledNumber = number
                    selection.value = null
                }
            }
            .launchIn(viewModelScope)

        tickerJob = tickerFlow()
            .onEach {
                val next = remaining.value - 1
                remaining.value = next
                if (next == 0) {
                    tickerJob?.cancel()
                    submitTest()
                }
            }
            .launchIn(viewModelScope)
    }

    fun selectOption(index: Int) {
        val state = uiState.value
        // A question that was answered in a previous run reveals its stored answer
        // straight away, so it cannot be re-selected.
        if (state.isSubmitted || state.hasStoredAnswer) return
        selection.value = index to false
    }

    fun submit() {
        val state = uiState.value
        val chosen = selection.value?.first ?: return
        if (state.isSubmitted) return
        selection.value = chosen to true
        viewModelScope.launch {
            questions.recordAnswer(source, state.currentNumber, chosen)
        }
    }

    fun goToNext() = goTo(currentNumber.value + 1)

    fun goToPrevious() = goTo(currentNumber.value - 1)

    fun goTo(number: Int) {
        val total = uiState.value.totalQuestions
        currentNumber.value = number.coerceIn(1, maxOf(total, 1))
    }

    /** Ends the test, records the score and exposes the report. */
    fun submitTest() {
        if (isFinished.value) return
        isFinished.value = true
        tickerJob?.cancel()
        viewModelScope.launch { recordScore(title, uiState.value.correctCount) }
    }

    fun consumeEvent() {
        _events.value = null
    }

    companion object {
        const val ARG_TITLE = "title"
        const val ARG_TABLE = "table"
        const val ARG_SECONDS = "seconds"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
        private const val NO_QUESTION = -1
    }
}

private val MockSessionUiState.hasStoredAnswer: Boolean
    get() = question?.attemptStatus != AttemptStatus.NOT_ATTEMPTED
