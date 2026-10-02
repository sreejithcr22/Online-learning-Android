package com.codit.interview.aptitude.presentation.questions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.usecase.ObserveTopicProgressUseCase
import com.codit.interview.aptitude.domain.usecase.ResolveTopicTimersUseCase
import com.codit.interview.aptitude.domain.usecase.SetTopicTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** A topic row in the sub-category list. */
data class TopicRow(
    val topic: Topic,
    val percent: Int,
    val timerSeconds: Int,
) {
    val isInfoOnly: Boolean get() = topic.isInfoOnly
    val timerText: String get() = TimeFormat.clock(timerSeconds)
}

data class TopicListUiState(
    val isLoading: Boolean = true,
    val section: ContentSection,
    val rows: List<TopicRow> = emptyList(),
    val topicBeingTimed: Topic? = null,
)

/**
 * Sub-category list for one section.
 *
 * Replaces `SubCategoryActivity` + `SubCategoryFragment` + `SubCategoryAdapter` and the
 * parallel `GKSubActivity` + `GKSubFragment` + `GKSubAdapter` trio, which between them
 * contained three copies of the same 40-branch topic-to-table mapping.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class TopicListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    resolveTimers: ResolveTopicTimersUseCase,
    private val setTopicTimer: SetTopicTimerUseCase,
    private val observeTopicProgress: ObserveTopicProgressUseCase,
) : ViewModel() {

    val section: ContentSection =
        ContentSection.valueOf(checkNotNull(savedStateHandle.get<String>(ARG_SECTION)))

    private val topicBeingTimed = MutableStateFlow<Topic?>(null)

    private val timers = resolveTimers(section)

    /** Completion percentage for every topic in the section, as one lookup map. */
    private val progressByTopic: Flow<Map<Topic, Int>> = timers.flatMapLatest { rows ->
        if (rows.isEmpty()) {
            flowOf(emptyMap())
        } else {
            val flows = rows.map { observeTopicProgress(it.topic) }
            combine(flows) { values ->
                rows.mapIndexed { index, row -> row.topic to values[index] }.toMap()
            }
        }
    }

    val uiState: StateFlow<TopicListUiState> = combine(
        timers,
        progressByTopic,
        topicBeingTimed,
    ) { resolved, progress, timingTopic ->
        TopicListUiState(
            isLoading = false,
            section = section,
            rows = resolved.map { item ->
                TopicRow(
                    topic = item.topic,
                    percent = progress[item.topic] ?: 0,
                    timerSeconds = item.seconds,
                )
            },
            topicBeingTimed = timingTopic,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = TopicListUiState(section = section),
    )

    fun openTimerDialog(topic: Topic) {
        topicBeingTimed.value = topic
    }

    fun dismissTimerDialog() {
        topicBeingTimed.value = null
    }

    fun saveTimer(topic: Topic, seconds: Int) {
        topicBeingTimed.value = null
        viewModelScope.launch { setTopicTimer(topic, seconds) }
    }

    companion object {
        const val ARG_SECTION = "section"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
