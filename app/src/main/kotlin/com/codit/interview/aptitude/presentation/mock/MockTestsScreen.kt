package com.codit.interview.aptitude.presentation.mock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun MockTestsRoute(
    onStartTest: (MockTest, Int) -> Unit,
    viewModel: MockTestsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingState()
        return
    }

    MockTestsScreen(state = state, onStartTest = onStartTest)
}

@Composable
fun MockTestsScreen(
    state: MockTestsUiState,
    onStartTest: (MockTest, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        items(state.tests, key = { it.index }) { test ->
            MockTestRow(test = test, durationSeconds = state.durationSeconds, onStart = onStartTest)
        }
    }
}

@Composable
private fun MockTestRow(
    test: MockTest,
    durationSeconds: Int,
    onStart: (MockTest, Int) -> Unit,
) {
    val accent = if (test.isFinished) {
        MaterialTheme.statusColors.accent
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    AptitudeCard(onClick = { onStart(test, durationSeconds) }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.padding(end = Spacing.large),
                verticalArrangement = Arrangement.spacedBy(Spacing.tiny),
            ) {
                Text(
                    text = test.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${test.questionCount} questions  •  " +
                        TimeFormat.clock(durationSeconds),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = if (test.isFinished) "Attempted" else "Not Attempted",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "${test.score}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "of ${test.questionCount}",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
