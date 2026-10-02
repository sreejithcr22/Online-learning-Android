package com.codit.interview.aptitude.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import com.codit.interview.aptitude.presentation.theme.Spacing
import androidx.compose.runtime.Composable

/**
 * Duration picker built on the standard Android time-entry control.
 *
 * The hour and minute fields are read as minutes and seconds, which is how every duration
 * in the app is written ("02:30" means a two-and-a-half-minute question timer).
 * [TimeInput] is used rather than a dial so the value can be typed instead of hunted for
 * on a clock face — picking 02:30 on a dial is needlessly fiddly.
 *
 * A standard time picker caps its hour field at 23, so the longest duration expressible
 * here is 23:59. When [initialSeconds] is longer than that, the stored value is left
 * alone unless the user actually edits the field, so an existing setting such as a
 * 35-minute mock test is not silently rewritten just by opening the dialog.
 *
 * @param maxMinutes hard ceiling for the confirmed value.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String,
    initialSeconds: Int,
    maxMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    onUseDefault: (() -> Unit)? = null,
) {
    val pickerHours = (initialSeconds / 60).coerceIn(0, 23)
    val pickerMinutes = (initialSeconds % 60).coerceIn(0, 59)
    val outOfRange = initialSeconds / 60 > 23

    val state = rememberTimePickerState(
        initialHour = pickerHours,
        initialMinute = pickerMinutes,
        is24Hour = true,
    )
    // TimeInput has no onValueChange in this Material3 version, so "did the user touch
    // it?" is derived by comparing the state against the values it was seeded with.
    val edited = state.hour != pickerHours || state.minute != pickerMinutes
    val confirmedSeconds = if (edited) state.hour * 60 + state.minute else initialSeconds

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.titleMedium) },
        text = {
            if (outOfRange && !edited) {
                Text(
                    text = "Currently ${initialSeconds / 60} minutes, longer than this " +
                        "picker can display. Press SET to keep it, or type a new value.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            TimeInput(state = state)
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(confirmedSeconds.coerceIn(0, maxMinutes * 60)) }) {
                Text("SET")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.small)) {
                onUseDefault?.let { TextButton(onClick = it) { Text("DEFAULT") } }
                TextButton(onClick = onDismiss) { Text("CANCEL") }
            }
        },
    )
}