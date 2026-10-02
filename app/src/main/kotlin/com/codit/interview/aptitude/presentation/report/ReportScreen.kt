package com.codit.interview.aptitude.presentation.report

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.SessionReport
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.DonutChart
import com.codit.interview.aptitude.presentation.components.DonutSlice
import com.codit.interview.aptitude.presentation.components.MetricRow
import com.codit.interview.aptitude.presentation.components.TimePerQuestionChart
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

/**
 * End-of-session report.
 *
 * Replaces `ProgressActivity`, which built an `achartengine` pie chart and line chart
 * as raw `View`s inside a `LinearLayout`. Both charts are now Compose `Canvas`
 * components and the numbers come from a single immutable [SessionReport].
 */
@Composable
fun ReportScreen(
    report: SessionReport,
    modifier: Modifier = Modifier,
) {
    val status = MaterialTheme.statusColors

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        AptitudeCard {
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(Spacing.large))
            DonutChart(
                slices = listOf(
                    DonutSlice("Correct", report.correct, status.correct),
                    DonutSlice("Wrong", report.wrong, status.wrong),
                    DonutSlice("Not Attempted", report.notAttempted, status.notAttempted),
                ),
                centerLabel = "${report.accuracyPercent}%",
                centerCaption = "accuracy",
            )
        }

        AptitudeCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                MetricRow("Total", report.total.toString(), Modifier.weight(1f))
                MetricRow("Correct", report.correct.toString(), Modifier.weight(1f))
                MetricRow("Wrong", report.wrong.toString(), Modifier.weight(1f))
                MetricRow("Skipped", report.notAttempted.toString(), Modifier.weight(1f))
            }
        }

        if (report.showTimeAnalysis) {
            AptitudeCard {
                Text(
                    text = "Time analysis",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(Spacing.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    MetricRow("Total", TimeFormat.clock(report.totalTimeSeconds), Modifier.weight(1f))
                    MetricRow("Average", TimeFormat.clock(report.averageTimeSeconds), Modifier.weight(1f))
                    MetricRow("Lowest", TimeFormat.clock(report.lowestTimeSeconds), Modifier.weight(1f))
                    MetricRow("Highest", TimeFormat.clock(report.highestTimeSeconds), Modifier.weight(1f))
                }
                Spacer(Modifier.height(Spacing.large))
                TimePerQuestionChart(
                    values = report.timePerQuestionSeconds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                )
            }
        }
    }
}
