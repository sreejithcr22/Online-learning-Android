package com.codit.interview.aptitude.presentation.practice

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.core.coroutines.ApplicationScope
import com.codit.interview.aptitude.core.coroutines.tickerFlow
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.model.QuestionSource
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import com.codit.interview.aptitude.domain.usecase.AddQuestionToFavouritesUseCase
import com.codit.interview.aptitude.domain.usecase.GetFormulaForTopicUseCase
import com.codit.interview.aptitude.domain.usecase.ObserveSettingsUseCase
import com.codit.interview.aptitude.domain.usecase.RecordTimeTakenUseCase
import com.codit.interview.aptitude.domain.usecase.SaveQuestionNoteUseCase
import com.codit.interview.aptitude.domain.usecase.SubmitAnswerUseCase
import com.codit.interview.aptitude.domain.usecase.UpdateTopicProgressUseCase
import com.codit.interview.aptitude.presentation.navigation.PracticeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

/** One-shot UI events: toasts plus the vibrate/dialog cues when the clock runs down. */
sealed interface PracticeEvent {
    data class Toast(val message: String) : PracticeEvent
    data object TimeAlmostUp : PracticeEvent
    data object TimeUp : PracticeEvent
}

@Immutable
data class PracticeUiState(
    val isLoading: Boolean = true,
    val hasNoQuestions: Boolean = false,
    val title: String = "",
    val question: Question? = null,
    val currentNumber: Int = 1,
    val totalQuestions: Int = 0,
    val navigator: QuestionNavigator = QuestionNavigator(0, emptyList()),
    /** The option the user has tapped but not yet submitted. */
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val timer: TimerUiState = TimerUiState.Idle,
    val canGoPrevious: Boolean = false,
    val canGoNext: Boolean = false,
    val showsNotesAndCalculator: Boolean = false,
    val showsFavouriteAction: Boolean = true,
    val formula: Tip? = null,
) {
    val hasAnswered: Boolean get() = question?.isAnswered == true

    /** Explanations are hidden until the question has actually been attempted. */
    val canShowExplanation: Boolean get() = hasAnswered || isSubmitted

    val explanation: String?
        get() = question?.takeIf { canShowExplanation }?.explanationOrDefault()

    val isFavourite: Boolean get() = question?.isFavourite == true

    /**
     * Whether the answer currently shown is the correct one.
     *
     * Uses this session's submission when there is one, and otherwise falls back to the
     * answer stored in the database, so a resumed question is coloured the same way as
     * a freshly answered one.
     */
    val isAnswerCorrect: Boolean
        get() = if (isSubmitted) isCorrect else question?.attemptStatus == AttemptStatus.CORRECT

    val isAnswerRevealed: Boolean get() = hasAnswered || isSubmitted

    /**
     * The option to highlight: the user's current pick, or — once the app is showing a
     * question answered in a previous run — the answer stored in the database.
     *
     * Falling back to the stored answer is what stops a resumed question from looking
     * unanswered, while `selectedOptionIndex` alone would leave the row unpainted when
     * the user taps an option before submitting.
     */
    val highlightedOptionIndex: Int?
        get() = selectedOptionIndex ?: question?.answeredOptionIndex
}

/**
 * Drives one question-at-a-time practice session.
 *
 * Handles the timed per-question countdown, option selection and submission, the
 * question navigator, notes, favourites and the end-of-session report. The legacy
 * equivalent was a 2 100-line `Fragment` plus a 300-line `Activity` that shared state
 * through five callback interfaces; all of that is now this one class plus [uiState].
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class PracticeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questions: QuestionRepository,
    private val submitAnswer: SubmitAnswerUseCase,
    private val recordTimeTaken: RecordTimeTakenUseCase,
    private val saveNote: SaveQuestionNoteUseCase,
    private val addToFavourites: AddQuestionToFavouritesUseCase,
    private val updateTopicProgress: UpdateTopicProgressUseCase,
    private val getFormula: GetFormulaForTopicUseCase,
    private val observeSettings: ObserveSettingsUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private val source: QuestionSource = readSource(savedStateHandle)
    private val mode: PracticeMode =
        savedStateHandle.get<String>(ARG_MODE)?.let(PracticeMode::valueOf) ?: PracticeMode.PRACTICE

    private val budgetSeconds: Int = savedStateHandle.get<String>(ARG_SECONDS)?.toIntOrNull() ?: 0

    /** GK, mock and favourites sessions run without a per-question clock. */
    private val isTimed = mode == PracticeMode.PRACTICE

    private val isFavouritesList = source is QuestionSource.OfFavourites

    private val currentNumber = MutableStateFlow(1)
    private val selection = MutableStateFlow<Selection?>(null)
    private val timerState = MutableStateFlow<TimerUiState>(TimerUiState.Idle)
    private val formulaState = MutableStateFlow<Tip?>(null)

    /** Elapsed seconds recorded for each question answered in this session. */
    private val sessionTimes = MutableStateFlow<List<Int>>(emptyList())

    private val _events = MutableStateFlow<PracticeEvent?>(null)
    val events: StateFlow<PracticeEvent?> = _events.asStateFlow()

    private var tickerJob: Job? = null
    private var lastHandledNumber = NO_QUESTION
    private var lastAlertedAt = Int.MIN_VALUE

    /** Re-subscribes whenever the selected question number changes. */
    private val questionFlow = currentNumber
        .flatMapLatest { number -> questions.observeQuestion(source, number).map { number to it } }

    private val timerAlertsEnabled = MutableStateFlow(true)

    val uiState: StateFlow<PracticeUiState> = combine(
        questionFlow,
        questions.observeNavigator(source),
        selection,
        timerState,
        formulaState,
    ) { (number, question), navigator, selectionState, timer, formula ->
        PracticeUiState(
            isLoading = false,
            hasNoQuestions = navigator.total == 0,
            title = source.displayTitle,
            question = question,
            currentNumber = number,
            totalQuestions = navigator.total,
            navigator = navigator,
            selectedOptionIndex = selectionState?.selected,
            isSubmitted = selectionState?.submitted == true,
            isCorrect = selectionState != null && selectionState.submitted &&
                selectionState.selected == question?.correctOptionIndex,
            timer = timer,
            canGoPrevious = number > 1,
            canGoNext = number < navigator.total,
            showsNotesAndCalculator = isTimed,
            showsFavouriteAction = !isFavouritesList,
            formula = formula,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = PracticeUiState(isLoading = true, title = source.displayTitle),
    )

    init {
        // Only react to a *new* question being selected. Re-emissions caused by our own
        // writes (answering, timing) must not reset the running clock.
        questionFlow
            .onEach { (number, question) ->
                if (number != lastHandledNumber) {
                    lastHandledNumber = number
                    onQuestionSelected(question)
                }
            }
            .launchIn(viewModelScope)

        observeSettings()
            .onEach { timerAlertsEnabled.value = it.timerAlerts }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            currentNumber.value = resumePosition()
            if (source is QuestionSource.OfTopic) {
                formulaState.value = getFormula(
                    Topic.formulaTable(source.section), source.topic.displayName
                )
            }
        }
    }

    // ---------------------------------------------------------------- navigation

    fun goToNext() = goTo(currentNumber.value + 1)

    fun goToPrevious() = goTo(currentNumber.value - 1)

    fun goTo(number: Int) {
        val total = uiState.value.totalQuestions
        val clamped = number.coerceIn(1, maxOf(total, 1))
        if (clamped == currentNumber.value) return
        viewModelScope.launch { questions.rememberPosition(source, clamped) }
        currentNumber.value = clamped
    }

    // ------------------------------------------------------------------- actions

    fun selectOption(index: Int) {
        val state = uiState.value
        // Options lock once submitted, or once a previously stored answer is revealed.
        if (state.isSubmitted || state.hasAnswered) return
        selection.value = Selection(selected = index, submitted = false)
    }

    fun submit() {
        val state = uiState.value
        val chosen = selection.value?.selected ?: return
        if (state.isSubmitted) return
        val question = state.question ?: return

        val wasFirstAttempt = !question.isAnswered
        val elapsed = currentElapsedSeconds()

        tickerJob?.cancel()
        tickerJob = null
        timerState.value = TimerUiState.Stopped(elapsed)

        selection.value = Selection(selected = chosen, submitted = true)

        viewModelScope.launch {
            if (isTimed && elapsed > 0) {
                recordTimeTaken(source, state.currentNumber, elapsed)
                sessionTimes.value = sessionTimes.value + elapsed
            }
            submitAnswer(
                source = source,
                number = state.currentNumber,
                optionIndex = chosen,
                wasFirstAttempt = wasFirstAttempt,
                trackGlobally = isTimed,
            )
        }
    }

    fun toggleFavourite() {
        val number = uiState.value.currentNumber
        viewModelScope.launch {
            val added = addToFavourites(source, number)
            _events.value = PracticeEvent.Toast(
                if (added) "Added to favourites!" else "Already in favourites"
            )
        }
    }

    fun saveNote(note: String) {
        val number = uiState.value.currentNumber
        viewModelScope.launch { saveNote(source, number, note) }
    }

    fun consumeEvent() {
        _events.value = null
    }

    /** Snapshot of this session, for the report shown on exit. */
    fun buildReport(): com.codit.interview.aptitude.presentation.navigation.ReportArgs? {
        val state = uiState.value
        if (state.navigator.total == 0) return null
        return com.codit.interview.aptitude.presentation.navigation.ReportArgs(
            title = state.title,
            correct = state.navigator.correctCount,
            wrong = state.navigator.wrongCount,
            notAttempted = state.navigator.notAttemptedCount,
            total = state.navigator.total,
            showTime = isTimed,
            timePerQuestion = sessionTimes.value,
        )
    }

    /**
     * Records the topic's completion percentage so the sub-category list stays current.
     *
     * Also runs from [onCleared], because a session can end without ever calling this:
     * the system back gesture and the app-bar arrow both pop the destination directly.
     */
    fun persistSessionProgress() {
        val state = uiState.value
        val topic = (source as? QuestionSource.OfTopic)?.topic ?: return
        if (state.navigator.total == 0) return
        val attempted = state.navigator.correctCount + state.navigator.wrongCount
        viewModelScope.launch {
            updateTopicProgress(topic, attempted * 100 / state.navigator.total)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // `viewModelScope` is already cancelled here, so the write runs on the
        // application scope and still lands when the user swipes back or taps the
        // app-bar arrow rather than the in-app close button.
        val topic = (source as? QuestionSource.OfTopic)?.topic ?: return
        val state = uiState.value
        if (state.navigator.total == 0) return
        val attempted = state.navigator.correctCount + state.navigator.wrongCount
        applicationScope.launch {
            updateTopicProgress(topic, attempted * 100 / state.navigator.total)
        }
    }

    // ------------------------------------------------------------------- ticking

    private fun onQuestionSelected(question: Question?) {
        selection.value = null
        lastAlertedAt = Int.MIN_VALUE

        if (question == null || !isTimed) {
            tickerJob?.cancel()
            timerState.value = TimerUiState.Idle
            return
        }

        if (question.isAnswered) {
            // Already answered: show the recorded time, do not run the clock.
            tickerJob?.cancel()
            timerState.value = TimerUiState.Stopped(question.timeTakenSeconds ?: 0)
            return
        }

        startTicker(budgetSeconds)
    }

    private fun startTicker(seconds: Int) {
        tickerJob?.cancel()
        if (seconds <= 0) {
            timerState.value = TimerUiState.Idle
            return
        }
        timerState.value = TimerUiState.Running(remainingSeconds = seconds, elapsedSeconds = 0)
        tickerJob = tickerFlow()
            .onEach {
                val current = timerState.value
                if (current !is TimerUiState.Running) return@onEach
                val elapsed = current.elapsedSeconds + 1
                val remaining = current.remainingSeconds - 1
                timerState.value = if (remaining < 0) {
                    TimerUiState.Expired(elapsed)
                } else {
                    TimerUiState.Running(remaining, elapsed)
                }
                announceThresholds(remaining)
            }
            .launchIn(viewModelScope)
    }

    private fun announceThresholds(remainingSeconds: Int) {
        if (!timerAlertsEnabled.value) return
        if (remainingSeconds in ALERT_THRESHOLDS && lastAlertedAt != remainingSeconds) {
            lastAlertedAt = remainingSeconds
            _events.value = if (remainingSeconds == ALERT_THRESHOLDS.first()) {
                PracticeEvent.TimeUp
            } else {
                PracticeEvent.TimeAlmostUp
            }
        }
    }

    private fun currentElapsedSeconds(): Int = when (val current = timerState.value) {
        is TimerUiState.Running -> current.elapsedSeconds
        is TimerUiState.Expired -> current.elapsedSeconds
        is TimerUiState.Stopped -> current.elapsedSeconds
        TimerUiState.Idle -> 0
    }

    private suspend fun resumePosition(): Int {
        val total = questions.questionCount(source)
        if (isFavouritesList) return 1
        val last = questions.observeLastPosition(source).first()
        return last.coerceIn(1, maxOf(total, 1))
    }

    /** The selected option plus whether it has been submitted. */
    private data class Selection(val selected: Int, val submitted: Boolean)

    companion object {
        const val ARG_TOPIC = "topic"
        const val ARG_SECTION = "section"
        const val ARG_MODE = "mode"
        const val ARG_SECONDS = "seconds"
        const val ARG_MOCK_TITLE = "mockTitle"
        const val ARG_MOCK_TABLE = "mockTable"

        private const val STOP_TIMEOUT_MILLIS = 5_000L
        private const val NO_QUESTION = -1
        private val ALERT_THRESHOLDS = listOf(0, 30, 60)
    }
}

/**
 * Rebuilds the [QuestionSource] from the route arguments.
 *
 * Replaces the `APPSTATE.CURRENT_TABLE` / `currentFragment` string globals: a
 * practice session now carries everything it needs in its own navigation route.
 */
private fun readSource(handle: SavedStateHandle): QuestionSource {
    val mockTable = handle.get<String>(PracticeViewModel.ARG_MOCK_TABLE)
    if (mockTable != null) {
        return QuestionSource.OfMockTest(
            MockTest(
                index = mockTable.removePrefix("mock").toIntOrNull() ?: 1,
                title = handle.get<String>(PracticeViewModel.ARG_MOCK_TITLE).orEmpty(),
                score = 0,
                isFinished = false,
                isLocked = false,
                questionCount = 0,
            )
        )
    }

    val section = ContentSection.valueOf(checkNotNull(handle.get<String>(PracticeViewModel.ARG_SECTION)))
    val mode = handle.get<String>(PracticeViewModel.ARG_MODE)?.let(PracticeMode::valueOf)
        ?: PracticeMode.PRACTICE

    return if (mode == PracticeMode.FAVOURITES) {
        QuestionSource.OfFavourites(section)
    } else {
        val topicName = checkNotNull(handle.get<String>(PracticeViewModel.ARG_TOPIC))
        QuestionSource.OfTopic(Topic.valueOf(topicName))
    }
}
