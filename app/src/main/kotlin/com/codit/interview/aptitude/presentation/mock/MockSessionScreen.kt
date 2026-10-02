package com.codit.interview.aptitude.presentation.mock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.presentation.components.NavigatorLegend
import com.codit.interview.aptitude.presentation.components.OptionList
import com.codit.interview.aptitude.presentation.components.QuestionNavigatorGrid
import com.codit.interview.aptitude.presentation.navigation.ReportArgs
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

/**
 * Hosts a running mock test and hands the finished report to [onFinished].
 *
 * Explanations are deliberately absent: the legacy app hid them until a mock test was
 * submitted, and that is worth keeping.
 */
@Composable
fun MockSessionHost(
    onFinished: (ReportArgs) -> Unit,
    viewModel: MockSessionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()
    var showSubmitDialog by remember { mutableStateOf(false) }
    var showNavigator by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    // Emit the report exactly once, when the test ends.
    LaunchedEffect(state.isFinished) {
        if (state.isFinished && !submitted) {
            submitted = true
            onFinished(
                ReportArgs(
                    title = state.title,
                    correct = state.correctCount,
                    wrong = state.navigator.wrongCount,
                    notAttempted = state.navigator.notAttemptedCount,
                    total = state.totalQuestions,
                    showTime = false,
                )
            )
        }
    }

    LaunchedEffect(event) {
        if (event is MockSessionEvent.TimeUp) {
            showSubmitDialog = true
            viewModel.consumeEvent()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.large),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // The test name is already in the top app bar.
            Text(
                text = "Question ${state.currentNumber} of ${state.totalQuestions}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Surface(
                color = if (state.isTimeUp) {
                    MaterialTheme.statusColors.wrong.copy(alpha = 0.16f)
                } else {
                    MaterialTheme.statusColors.accent.copy(alpha = 0.14f)
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = state.displayTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isTimeUp) {
                        MaterialTheme.statusColors.wrong
                    } else {
                        MaterialTheme.statusColors.accent
                    },
                    modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.small),
                )
            }
        }

        Text(
            text = state.question?.text.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.medium)
                .heightIn(min = 96.dp),
        )

        OptionList(
            options = state.question?.options.orEmpty(),
            selectedIndex = state.highlightedOptionIndex,
            revealed = state.isRevealed,
            correctOptionIndex = state.question?.correctOptionIndex ?: 0,
            enabled = true,
            onSelect = viewModel::selectOption,
            modifier = Modifier.weight(1f),
        )

        if (state.isRevealed) {
            Text(
                text = if (state.isAnswerCorrect) {
                    "Correct answer!"
                } else {
                    "Correct answer: ${optionLabel(state)}"
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (state.isAnswerCorrect) {
                    MaterialTheme.statusColors.correct
                } else {
                    MaterialTheme.statusColors.wrong
                },
                modifier = Modifier.padding(vertical = Spacing.small),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = viewModel::goToPrevious,
                    enabled = state.canGoPrevious,
                ) {
                    Icon(Icons.AutoMirrored.Rounded.NavigateBefore, contentDescription = null)
                    Text("  Previous")
                }
                TextButton(onClick = { showNavigator = true }) { Text("All questions") }
                TextButton(onClick = viewModel::goToNext, enabled = state.canGoNext) {
                    Text("  Next")
                    Icon(Icons.AutoMirrored.Rounded.NavigateNext, contentDescription = null)
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                Button(
                    onClick = viewModel::submit,
                    enabled = state.selectedOptionIndex != null,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.statusColors.accent,
                    ),
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = null)
                    Text("  SUBMIT")
                }
                Button(
                    onClick = { showSubmitDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                ) { Text("FINISH") }
            }
        }

        Spacer(Modifier.height(Spacing.medium))
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Submit test?") },
            text = {
                val skipped = state.navigator.notAttemptedCount
                if (skipped > 0) {
                    Text("$skipped questions not attempted")
                } else {
                    Text("Do you really want to submit?")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showSubmitDialog = false
                    viewModel.submitTest()
                }) { Text("SUBMIT") }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) { Text("CANCEL") }
            },
        )
    }

    if (showNavigator) {
        MockNavigatorDialog(
            navigator = state.navigator,
            currentNumber = state.currentNumber,
            onSelect = {
                showNavigator = false
                viewModel.goTo(it)
            },
            onDismiss = { showNavigator = false },
        )
    }
}

/** "option A".."option D", matching the wording used on the practice screen. */
private fun optionLabel(state: MockSessionUiState): String {
    val index = state.question?.correctOptionIndex ?: return ""
    return "option ${'A' + index.coerceIn(0, 3)}"
}

@Composable
private fun MockNavigatorDialog(
    navigator: QuestionNavigator,
    currentNumber: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val status = MaterialTheme.statusColors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "All questions  •  ${navigator.correctCount} correct, " +
                    "${navigator.wrongCount} wrong",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                NavigatorLegend()
                QuestionNavigatorGrid(
                    navigator = navigator,
                    currentNumber = currentNumber,
                    onSelect = onSelect,
                    modifier = Modifier.height(320.dp),
                )
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("CLOSE") } },
    )
}
