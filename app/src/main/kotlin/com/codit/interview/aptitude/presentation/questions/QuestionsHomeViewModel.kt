package com.codit.interview.aptitude.presentation.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.ProgressSnapshot
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.usecase.ObserveProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** A tappable category card on the questions home screen. */
data class SectionCard(
    val section: ContentSection,
    val title: String,
    val percent: Int,
    val topicCount: Int,
    val questionCount: Int,
)

data class QuestionsHomeUiState(
    val isLoading: Boolean = true,
    val cards: List<SectionCard> = emptyList(),
    val overallPercent: Int = 0,
    val isGkTabSelected: Boolean = false,
)

/**
 * Home screen for the "Questions" tab.
 *
 * Replaces `ParentCategory` + `ParentCategoryHelper` + `ParentCategoryFragment` +
 * `GKFragment`, which duplicated the same three-card layout twice and drove the bars
 * with a `Thread` + `Handler` animation loop.
 */
@HiltViewModel
class QuestionsHomeViewModel @Inject constructor(
    observeProgress: ObserveProgressUseCase,
) : ViewModel() {

    private val isGkTabSelected = MutableStateFlow(false)

    val uiState: StateFlow<QuestionsHomeUiState> = combine(
        observeProgress(),
        isGkTabSelected,
    ) { progress, gkSelected ->
        QuestionsHomeUiState(
            isLoading = false,
            cards = buildCards(progress, gkSelected),
            overallPercent = progress.overallPercent(),
            isGkTabSelected = gkSelected,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = QuestionsHomeUiState(),
    )

    fun onTabSelected(index: Int) {
        isGkTabSelected.value = index == GK_TAB_INDEX
    }

    private fun buildCards(progress: ProgressSnapshot, gkSelected: Boolean): List<SectionCard> =
        if (gkSelected) {
            listOf(progress.cardFor(ContentSection.GENERAL_KNOWLEDGE, "General Knowledge"))
        } else {
            listOf(
                progress.cardFor(ContentSection.QUANTITATIVE, "Quantitative Aptitude"),
                progress.cardFor(ContentSection.LOGICAL, "Logical Reasoning"),
                progress.cardFor(ContentSection.VERBAL, "Verbal Ability"),
            )
        }

    private fun ProgressSnapshot.cardFor(section: ContentSection, title: String) = SectionCard(
        section = section,
        title = title,
        percent = sectionPercent(section),
        topicCount = Topic.entries.count { it.section == section },
        questionCount = sectionTotal(section),
    )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val GK_TAB_INDEX = 1
    }
}
