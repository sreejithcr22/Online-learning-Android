package com.codit.interview.aptitude.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.FlowRow
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.presentation.theme.Spacing

/**
 * Minute/second picker.
 *
 * Replaces the `NumberPicker` pair that was inflated from `timer_body.xml` in three
 * separate activities, each with its own copy of the setup code.
 */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun TimePickerDialog(
    title: String,
    initialSeconds: Int,
    maxMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    onUseDefault: (() -> Unit)? = null,
) {
    var minutes by remember { mutableIntStateOf((initialSeconds / 60).coerceIn(0, maxMinutes)) }
    var seconds by remember { mutableIntStateOf((initialSeconds % 60).coerceIn(0, 59)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.medium)) {
                Text(
                    text = "Selected: ${TimeFormat.clock(minutes * 60 + seconds)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text("Minutes", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    (0..maxMinutes).forEach { value ->
                        FilterChip(
                            selected = value == minutes,
                            onClick = { minutes = value },
                            label = { Text("%02d".format(value)) },
                        )
                    }
                }
                Text("Seconds", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    SECOND_CHOICES.forEach { value ->
                        FilterChip(
                            selected = value == seconds,
                            onClick = { seconds = value },
                            label = { Text("%02d".format(value)) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(minutes * 60 + seconds) }) { Text("SET") }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                onUseDefault?.let {
                    TextButton(onClick = it) { Text("DEFAULT") }
                }
                TextButton(onClick = onDismiss) { Text("CANCEL") }
            }
        },
    )
}

private val SECOND_CHOICES = listOf(0, 15, 30, 45, 59)
