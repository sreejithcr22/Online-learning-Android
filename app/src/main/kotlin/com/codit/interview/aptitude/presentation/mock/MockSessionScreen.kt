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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Q. ${state.currentNumber} of ${state.totalQuestions}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            state.question?.options?.forEachIndexed { index, option ->
                MockOptionRow(
                    index = index,
                    text = option,
                    isSelected = state.highlightedOptionIndex == index,
                    isSubmitted = state.isRevealed,
                    isCorrectOption = index == state.question?.correctOptionIndex,
                    onClick = { viewModel.selectOption(index) },
                )
            }
        }

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
private fun MockOptionRow(
    index: Int,
    text: String,
    isSelected: Boolean,
    isSubmitted: Boolean,
    isCorrectOption: Boolean,
    onClick: () -> Unit,
) {
    val status = MaterialTheme.statusColors
    val background = when {
        isSubmitted && isCorrectOption -> status.correct.copy(alpha = 0.16f)
        isSubmitted && isSelected -> status.wrong.copy(alpha = 0.16f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val borderColor = when {
        isSubmitted && isCorrectOption -> status.correct
        isSubmitted && isSelected -> status.wrong
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .border(1.dp, borderColor, MaterialTheme.shapes.small)
            .clickable(enabled = !isSubmitted, onClick = onClick)
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = ('A' + index).toString(),
            style = MaterialTheme.typography.titleMedium,
            color = borderColor,
            modifier = Modifier.width(28.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        if (isSubmitted && (isCorrectOption || isSelected)) {
            Icon(
                imageVector = if (isCorrectOption) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                tint = borderColor,
                modifier = Modifier.size(20.dp),
            )
        }
    }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(320.dp),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalArrangement = Arrangement.spacedBy(Spacing.small),
            ) {
                items(navigator.total) { index ->
                    val number = index + 1
                    val tint = when (navigator.statuses.getOrNull(index) ?: AttemptStatus.NOT_ATTEMPTED) {
                        AttemptStatus.CORRECT -> status.correct
                        AttemptStatus.WRONG -> status.wrong
                        AttemptStatus.NOT_ATTEMPTED -> status.notAttempted
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(
                                if (number == currentNumber) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    tint.copy(alpha = 0.25f)
                                }
                            )
                            .clickable { onSelect(number) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "$number",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (number == currentNumber) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("CLOSE") } },
    )
}
