package com.codit.interview.aptitude.presentation.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codit.interview.aptitude.presentation.theme.Spacing

/**
 * Calculator dialog.
 *
 * Replaces `CalcFragment` (1 005 lines): a `DialogFragment` that inflated
 * `layout_calc.xml`, held the same arithmetic across a dozen `Button` fields, and
 * mutated `TextView`s from listeners. The arithmetic now lives in
 * [CalculatorEngine]; this file is only layout.
 */
@Composable
fun CalculatorDialog(
    onDismiss: () -> Unit,
    onCopyToNotes: (String) -> Unit,
) {
    var state by remember { mutableStateOf(CalculatorState()) }
    var showHistory by remember { mutableStateOf(false) }

    fun dispatch(action: CalculatorAction) {
        state = CalculatorEngine.reduce(state, action)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (showHistory) "History" else "Calculator",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { showHistory = !showHistory }) {
                    Icon(
                        imageVector = if (showHistory) Icons.Rounded.Close else Icons.Rounded.History,
                        contentDescription = if (showHistory) "Back to calculator" else "Show history",
                    )
                }
                IconButton(
                    onClick = {
                        if (state.hasHistory) onCopyToNotes(state.historyEntries.joinToString("\n"))
                    },
                    enabled = state.hasHistory,
                ) {
                    Icon(Icons.Rounded.ContentCopy, contentDescription = "Copy history to notes")
                }
            }
        },
        text = {
            if (showHistory) {
                HistoryPane(
                    entries = state.historyEntries,
                    onClear = { dispatch(CalculatorAction.ClearHistory) },
                )
            } else {
                CalculatorKeypad(
                    state = state,
                    onAction = ::dispatch,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("CLOSE") }
        },
    )
}

@Composable
private fun HistoryPane(entries: List<String>, onClear: () -> Unit) {
    if (entries.isEmpty()) {
        Text(
            text = "Do some calculations and they will appear here!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            textAlign = TextAlign.Center,
        )
        return
    }
    Column {
        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
            items(entries.size) { index -> entries[index] }
        }
        TextButton(onClick = onClear, modifier = Modifier.align(Alignment.End)) { Text("CLEAR") }
    }
}

@Composable
private fun CalculatorKeypad(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit,
) {
    Column {
        Text(
            text = state.output,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
        Text(
            text = state.input,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (state.isInvalid) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            textAlign = TextAlign.End,
        )

        val rows = listOf(
            listOf(Key("C", span = 1), Key("±"), Key("⌫"), Key("÷")),
            listOf(Key("7"), Key("8"), Key("9"), Key("x")),
            listOf(Key("4"), Key("5"), Key("6"), Key("-")),
            listOf(Key("1"), Key("2"), Key("3"), Key("+")),
            listOf(Key("0", span = 2), Key("."), Key("=")),
        )
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            rows.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    row.forEach { key ->
                        KeypadKey(
                            label = key.label,
                            emphasised = key.label in OPERATORS || key.label == "=" ||
                                key.label == "C",
                            modifier = Modifier.weight(key.span.toFloat()),
                            onClick = { key.label.toAction()?.let(onAction) },
                        )
                    }
                }
            }
        }
    }
}

private val OPERATORS = setOf("÷", "x", "-", "+", "=")
private val KEY_HEIGHT = 52.dp

/** One keypad button; [span] lets `0` occupy two columns on the last row. */
private data class Key(val label: String, val span: Int = 1)

@Composable
private fun KeypadKey(
    label: String,
    emphasised: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(KEY_HEIGHT)
            .background(
                color = if (emphasised) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        when (label) {
            "⌫" -> Icon(
                imageVector = Icons.AutoMirrored.Rounded.Backspace,
                contentDescription = "Backspace",
                tint = keyColor(emphasised),
            )

            else -> Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = keyColor(emphasised),
            )
        }
    }
}

@Composable
private fun keyColor(emphasised: Boolean) = if (emphasised) {
    MaterialTheme.colorScheme.onPrimary
} else {
    MaterialTheme.colorScheme.onSurface
}

/**
 * Maps a keypad label to an action.
 *
 * Total by construction: an unknown label is ignored rather than falling through to
 * [CalculatorAction.Operator], which would try to look the label up in a map of the
 * four operators and throw.
 */
private fun String.toAction(): CalculatorAction? = when {
    this == "C" -> CalculatorAction.Clear
    this == "⌫" -> CalculatorAction.Backspace
    this == "±" -> CalculatorAction.Negate
    this == "." -> CalculatorAction.Decimal
    this == "=" -> CalculatorAction.Equals
    this in OPERATORS -> CalculatorAction.Operator(this[0])
    length == 1 && this[0] in '0'..'9' -> CalculatorAction.Digit(this[0])
    else -> null
}
