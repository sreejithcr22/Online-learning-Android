package com.codit.interview.aptitude.presentation.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.CircularProgressIndicator
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.components.MetricRow
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

/**
 * Analytics dashboard.
 *
 * Replaces `MainActivity`, which built six cards on a background thread inside an
 * `AsyncTask` and animated five `CircleProgressBar` views with a hand-written
 * `while` loop posting to a `Handler`.
 */
@Composable
fun AnalyticsRoute(
    onOpenStore: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnalyticsScreen(
        state = state,
        onReviewAccepted = {
            viewModel.onReviewAccepted()
            onOpenStore()
        },
        onReviewDismissed = viewModel::consumeReviewPrompt,
    )
}

@Composable
fun AnalyticsScreen(
    state: AnalyticsUiState,
    onReviewAccepted: () -> Unit,
    onReviewDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        LoadingState(modifier = modifier)
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.large, vertical = Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        state.cards.forEach { card -> AnalyticsCardView(card) }

        AptitudeCard {
            Column {
                Text(
                    text = "Time",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(Spacing.small))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MetricRow("Average", state.averageTime, Modifier.weight(1f))
                    MetricRow("Total", state.totalTime, Modifier.weight(1f))
                    MetricRow("Highest", state.highestTime, Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(Spacing.large))
    }

    if (state.showReviewPrompt) {
        AlertDialog(
            onDismissRequest = onReviewDismissed,
            title = { Text("Love the app?") },
            text = { Text("Please spend a moment to review the app on the Play Store.") },
            confirmButton = { TextButton(onClick = onReviewAccepted) { Text("Rate us") } },
            dismissButton = { TextButton(onClick = onReviewDismissed) { Text("Not now") } },
        )
    }
}

@Composable
private fun AnalyticsCardView(card: AnalyticsCard) {
    AptitudeCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                percent = card.percent,
                size = 78.dp,
                strokeWidth = 8.dp,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                progressColor = MaterialTheme.statusColors.accent,
                label = card.centreValue,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.tiny),
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    card.metrics.forEach { (caption, value) ->
                        MetricRow(caption, value, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
