package com.codit.interview.aptitude.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.ProgressSnapshot
import com.codit.interview.aptitude.domain.repository.AppStateRepository
import com.codit.interview.aptitude.domain.usecase.ObserveProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** One metric card on the analytics dashboard. */
data class AnalyticsCard(
    val title: String,
    val percent: Int,
    val centreValue: String,
    val metrics: List<Pair<String, String>>,
)

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val cards: List<AnalyticsCard> = emptyList(),
    val averageTime: String = "00:00",
    val totalTime: String = "0m",
    val highestTime: String = "00:00",
    /** The legacy app asked for a Play Store review on every fifth visit. */
    val showReviewPrompt: Boolean = false,
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    observeProgress: ObserveProgressUseCase,
    private val appState: AppStateRepository,
) : ViewModel() {

    private val reviewPromptDismissed = MutableStateFlow(false)

    val uiState: StateFlow<AnalyticsUiState> = observeProgress()
        .map { snapshot -> snapshot.toUiState(reviewPromptDismissed.value) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = AnalyticsUiState(),
        )

    fun consumeReviewPrompt() {
        reviewPromptDismissed.value = true
    }

    fun onReviewAccepted() {
        reviewPromptDismissed.value = true
        viewModelScope.launch { appState.markReviewRequested() }
    }

    private fun ProgressSnapshot.toUiState(promptDismissed: Boolean) = AnalyticsUiState(
        isLoading = false,
        cards = toCards(),
        averageTime = TimeFormat.clock(averageTimeSeconds),
        totalTime = TimeFormat.hoursAndMinutes(totalTimeSeconds),
        highestTime = TimeFormat.clock(highestQuestionTimeSeconds),
        showReviewPrompt = !promptDismissed && shouldAskForReview(),
    )

    /** Every fifth visit, once the user has actually been shown the prompt. */
    private fun ProgressSnapshot.shouldAskForReview(): Boolean =
        !hasRequestedReview && visitCount > 0 && visitCount % REVIEW_INTERVAL == 0

    private fun ProgressSnapshot.toCards(): List<AnalyticsCard> {
        val aptitudePercent = if (aptitudeTotal == 0) {
            0
        } else {
            aptitudeAttempted * 100 / aptitudeTotal
        }
        val gkPercent = sectionPercent(ContentSection.GENERAL_KNOWLEDGE)
        val mockPercent = if (mockTestCount == 0) 0 else mockTestsCompleted * 100 / mockTestCount

        return listOf(
            AnalyticsCard(
                title = "Overall Progress",
                percent = overallPercent(),
                centreValue = "${overallPercent()}%",
                metrics = listOf(
                    "Questions" to practiceTotal.toString(),
                    "Attempted" to practiceAttempted.toString(),
                ),
            ),
            AnalyticsCard(
                title = "Accuracy",
                percent = accuracyPercent,
                centreValue = "$accuracyPercent%",
                metrics = listOf(
                    "Correct" to correct.toString(),
                    "Wrong" to wrong.toString(),
                ),
            ),
            AnalyticsCard(
                title = "Aptitude",
                percent = aptitudePercent,
                centreValue = "$aptitudePercent%",
                metrics = listOf(
                    "Questions" to aptitudeTotal.toString(),
                    "Attempted" to aptitudeAttempted.toString(),
                ),
            ),
            AnalyticsCard(
                title = "GK",
                percent = gkPercent,
                centreValue = "$gkPercent%",
                metrics = listOf(
                    "Questions" to generalKnowledgeTotal.toString(),
                    "Attempted" to generalKnowledgeAttempted.toString(),
                ),
            ),
            AnalyticsCard(
                title = "Mock Tests",
                percent = mockPercent,
                centreValue = mockTestsCompleted.toString(),
                metrics = listOf(
                    "Tests" to mockTestCount.toString(),
                    "Avg Score" to mockAverageScore.toString(),
                ),
            ),
        )
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val REVIEW_INTERVAL = 5
    }
}
