package com.codit.interview.aptitude.presentation.practice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.automirrored.rounded.NavigateBefore
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.automirrored.rounded.StickyNote2
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.automirrored.rounded.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.presentation.calculator.CalculatorDialog
import com.codit.interview.aptitude.presentation.navigation.ReportArgs
import com.codit.interview.aptitude.presentation.components.LoadingState
import com.codit.interview.aptitude.presentation.components.NotesDialog
import com.codit.interview.aptitude.presentation.notes.NoteFormatter
import com.codit.interview.aptitude.presentation.theme.Spacing
import com.codit.interview.aptitude.presentation.theme.statusColors

@Composable
fun PracticeRoute(
    onBack: () -> Unit,
    onShowReport: (ReportArgs) -> Unit,
    viewModel: PracticeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showNotes by remember { mutableStateOf(false) }
    var showCalculator by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }
    var showNavigator by remember { mutableStateOf(false) }
    var showTimeUpDialog by remember { mutableStateOf(false) }
    var showLeaveDialog by remember { mutableStateOf(false) }

    // Time-up cues mirror the legacy QuestionActivity: a toast at 30s/1m, then a
    // vibration and a modal at zero.
    LaunchedEffect(event) {
        val current = event
        when (current) {
            is PracticeEvent.Toast -> {
                android.widget.Toast.makeText(context, current.message, android.widget.Toast.LENGTH_SHORT)
                    .show()
                viewModel.consumeEvent()
            }

            PracticeEvent.TimeAlmostUp -> {
                android.widget.Toast.makeText(context, "Time almost up!", android.widget.Toast.LENGTH_SHORT)
                    .show()
                viewModel.consumeEvent()
            }

            PracticeEvent.TimeUp -> {
                showTimeUpDialog = true
                viewModel.consumeEvent()
            }

            null -> Unit
        }
    }

    if (state.isLoading) {
        LoadingState()
        return
    }

    if (state.hasNoQuestions) {
        PracticeEmptyState(onBack = onBack)
        return
    }

    PracticeScreen(
        state = state,
        onBack = { showLeaveDialog = true },
        onSelectOption = viewModel::selectOption,
        onSubmit = viewModel::submit,
        onNext = viewModel::goToNext,
        onPrevious = viewModel::goToPrevious,
        onOpenNavigator = { showNavigator = true },
        onToggleFavourite = viewModel::toggleFavourite,
        onOpenNotes = { showNotes = true },
        onOpenCalculator = { showCalculator = true },
        onOpenExplanation = { showExplanation = true },
    )


    if (showNotes) {
        NotesDialog(
            initialText = state.question?.note.orEmpty(),
            onDismiss = { showNotes = false },
            onSave = { text ->
                viewModel.saveNote(text)
                showNotes = false
            },
        )
    }

    if (showCalculator) {
        CalculatorDialog(
            onDismiss = { showCalculator = false },
            onCopyToNotes = { history ->
                val current = state.question?.note.orEmpty()
                viewModel.saveNote(NoteFormatter.appendCalculatorHistory(current, history))
                showCalculator = false
            },
        )
    }

    if (showExplanation) {
        ExplanationDialog(
            optionLabel = state.question?.let { optionLabel(it, it.correctOptionIndex) }.orEmpty(),
            explanation = state.explanation.orEmpty(),
            onDismiss = { showExplanation = false },
        )
    }

    if (showNavigator) {
        QuestionNavigatorDialog(
            navigator = state.navigator,
            currentNumber = state.currentNumber,
            onSelect = viewModel::goTo,
            onDismiss = { showNavigator = false },
        )
    }

    if (showTimeUpDialog) {
        AlertDialog(
            onDismissRequest = { showTimeUpDialog = false },
            title = { Text("Time's up") },
            text = { Text("You have exceeded the time for this question.") },
            confirmButton = {
                TextButton(onClick = { showTimeUpDialog = false }) { Text("IGNORE") }
            },
        )
    }

    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Leave this session?") },
            text = { Text("Your answers are saved as you go. You can resume from here later.") },
            confirmButton = {
                TextButton(onClick = {
                    showLeaveDialog = false
                    onBack()
                }) { Text("EXIT") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        showLeaveDialog = false
                        viewModel.persistSessionProgress()
                        val report = viewModel.buildReport()
                        if (report != null) onShowReport(report) else onBack()
                    }) { Text("SHOW REPORT") }
                    TextButton(onClick = { showLeaveDialog = false }) { Text("STAY") }
                }
            },
        )
    }
}

@Composable
fun PracticeScreen(
    state: PracticeUiState,
    onBack: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onOpenNavigator: () -> Unit,
    onToggleFavourite: () -> Unit,
    onOpenNotes: () -> Unit,
    onOpenCalculator: () -> Unit,
    onOpenExplanation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val question = state.question

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.large),
    ) {
        PracticeHeader(
            state = state,
            onBack = onBack,
            onOpenNavigator = onOpenNavigator,
            onToggleFavourite = onToggleFavourite,
            onOpenNotes = onOpenNotes,
            onOpenCalculator = onOpenCalculator,
        )

        Text(
            text = question?.text.orEmpty(),
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
            question?.options?.forEachIndexed { index, option ->
                OptionRow(
                    index = index,
                    text = option,
                    isSelected = state.highlightedOptionIndex == index,
                    revealed = state.hasAnswered || state.isSubmitted,
                    isCorrectOption = index == question.correctOptionIndex,
                    onClick = { onSelectOption(index) },
                )
            }
        }

        AnimatedVisibility(visible = state.canShowExplanation) {
            TextButton(onClick = onOpenExplanation) {
                Icon(Icons.Rounded.Lightbulb, contentDescription = null)
                Text("  Explanation")
            }
        }

        if (state.isSubmitted || state.hasAnswered) {
            Text(
                text = if (state.isCorrect || (state.hasAnswered && question?.attemptStatus == AttemptStatus.CORRECT)) {
                    "Correct answer!"
                } else {
                    "Correct answer: ${question?.let { optionLabel(it, it.correctOptionIndex) }.orEmpty()}"
                },
                style = MaterialTheme.typography.titleMedium,
                color = if (state.isCorrect) MaterialTheme.statusColors.correct else MaterialTheme.statusColors.wrong,
                modifier = Modifier.padding(vertical = Spacing.small),
            )
        }

        if (!state.isSubmitted && !state.hasAnswered) {
            SubmitButton(enabled = state.selectedOptionIndex != null, onClick = onSubmit)
        } else {
            NavigationRow(
                canGoPrevious = state.canGoPrevious,
                canGoNext = state.canGoNext,
                onPrevious = onPrevious,
                onNext = onNext,
            )
        }

        Spacer(Modifier.height(Spacing.medium))
    }
}

@Composable
private fun PracticeHeader(
    state: PracticeUiState,
    onBack: () -> Unit,
    onOpenNavigator: () -> Unit,
    onToggleFavourite: () -> Unit,
    onOpenNotes: () -> Unit,
    onOpenCalculator: () -> Unit,
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
        TimerBadge(state.timer)
        IconButton(onClick = onOpenNavigator) {
            Icon(Icons.AutoMirrored.Rounded.ShowChart, contentDescription = "Jump to question")
        }
        if (state.showsNotesAndCalculator) {
            IconButton(onClick = onOpenCalculator) {
                Icon(Icons.Rounded.Calculate, contentDescription = "Calculator")
            }
            IconButton(onClick = onOpenNotes) {
                Icon(Icons.AutoMirrored.Rounded.StickyNote2, contentDescription = "Notes")
            }
        }
        if (state.showsFavouriteAction) {
            IconButton(onClick = onToggleFavourite, enabled = !state.isFavourite) {
                Icon(
                    imageVector = if (state.isFavourite) {
                        Icons.Rounded.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    },
                    contentDescription = "Add to favourites",
                    tint = if (state.isFavourite) {
                        MaterialTheme.statusColors.wrong
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
        IconButton(onClick = onBack) {
            Icon(Icons.Rounded.Close, contentDescription = "Close")
        }
    }
}

@Composable
private fun TimerBadge(timer: TimerUiState) {
    val status = MaterialTheme.statusColors
    val (label, tint) = when (timer) {
        is TimerUiState.Running ->
            timer.display to if (timer.remainingSeconds <= 10) status.wrong else status.accent

        is TimerUiState.Expired -> "00:00" to status.wrong
        is TimerUiState.Stopped -> timer.display to status.onSubtleSurface
        TimerUiState.Idle -> "--:--" to status.onSubtleSurface
    }
    Surface(
        color = tint.copy(alpha = 0.14f),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = tint,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = Spacing.medium, vertical = Spacing.small),
        )
    }
}

@Composable
private fun OptionRow(
    index: Int,
    text: String,
    isSelected: Boolean,
    revealed: Boolean,
    isCorrectOption: Boolean,
    onClick: () -> Unit,
) {
    val status = MaterialTheme.statusColors
    val background = when {
        revealed && isCorrectOption -> status.correct.copy(alpha = 0.16f)
        revealed && isSelected -> status.wrong.copy(alpha = 0.16f)
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val borderColor = when {
        revealed && isCorrectOption -> status.correct
        revealed && isSelected -> status.wrong
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .border(1.dp, borderColor, MaterialTheme.shapes.small)
            .clickable(enabled = !revealed, onClick = onClick)
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
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (revealed && (isCorrectOption || isSelected)) {
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
private fun SubmitButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.statusColors.accent,
        ),
    ) {
        Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = null)
        Text("  SUBMIT")
    }
}

@Composable
private fun NavigationRow(
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedAction(
            label = "Previous",
            icon = { Icon(Icons.AutoMirrored.Rounded.NavigateBefore, contentDescription = null) },
            enabled = canGoPrevious,
            onClick = onPrevious,
        )
        OutlinedAction(
            label = "Next",
            icon = { Icon(Icons.AutoMirrored.Rounded.NavigateNext, contentDescription = null) },
            enabled = canGoNext,
            onClick = onNext,
        )
    }
}

@Composable
private fun OutlinedAction(
    label: String,
    icon: @Composable () -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.statusColors.accent,
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.statusColors.accent,
        ),
    ) {
        icon()
        Text("  $label")
    }
}

@Composable
private fun QuestionNavigatorDialog(
    navigator: com.codit.interview.aptitude.domain.model.QuestionNavigator,
    currentNumber: Int,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val status = MaterialTheme.statusColors
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Jump to question  •  ${navigator.correctCount} correct, " +
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
                items(navigator.total.coerceAtLeast(0)) { index ->
                    val number = index + 1
                    val attemptStatus = navigator.statuses.getOrNull(index)
                        ?: AttemptStatus.NOT_ATTEMPTED
                    val tint = when (attemptStatus) {
                        AttemptStatus.CORRECT -> status.correct
                        AttemptStatus.WRONG -> status.wrong
                        AttemptStatus.NOT_ATTEMPTED -> status.notAttempted
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
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

@Composable
private fun ExplanationDialog(optionLabel: String, explanation: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Answer - $optionLabel") },
        text = {
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState()),
            )
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("GOT IT") } },
    )
}

@Composable
private fun PracticeEmptyState(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.extraLarge),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No records found!", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onBack) { Text("BACK") }
        }
    }
}

/** "option A".."option D", matching the wording of the legacy explanation dialog. */
internal fun optionLabel(question: Question, index: Int): String =
    ('A' + index.coerceIn(0, 3)).let { "option $it" }
