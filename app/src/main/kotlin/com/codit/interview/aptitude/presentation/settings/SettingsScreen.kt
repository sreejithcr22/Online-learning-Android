package com.codit.interview.aptitude.presentation.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import com.codit.interview.aptitude.core.util.TimeFormat
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.presentation.components.AptitudeCard
import com.codit.interview.aptitude.presentation.components.TimePickerDialog
import com.codit.interview.aptitude.presentation.theme.Spacing

@Composable
fun SettingsRoute(
    onOpenAbout: () -> Unit,
    onShareApp: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenFeedback: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAutoSaveNotes = viewModel::setAutoSaveNotes,
        onCopyCalculatorHistory = viewModel::setCopyCalculatorHistory,
        onTimerAlerts = viewModel::setTimerAlerts,
        onVibrateOnTimeUp = viewModel::setVibrateOnTimeUp,
        onEditDefaultTimer = viewModel::openDefaultTimerEditor,
        onEditMockDuration = viewModel::openMockDurationEditor,
        onDismissEditors = viewModel::dismissEditors,
        onSaveDefaultTimer = viewModel::saveDefaultTimer,
        onSaveMockDuration = viewModel::saveMockDuration,
        onThemeChanged = viewModel::setTheme,
        onOpenAbout = onOpenAbout,
        onShareApp = onShareApp,
        onOpenStore = onOpenStore,
        onOpenFeedback = onOpenFeedback,
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onAutoSaveNotes: (Boolean) -> Unit,
    onCopyCalculatorHistory: (Boolean) -> Unit,
    onTimerAlerts: (Boolean) -> Unit,
    onVibrateOnTimeUp: (Boolean) -> Unit,
    onEditDefaultTimer: () -> Unit,
    onEditMockDuration: () -> Unit,
    onDismissEditors: () -> Unit,
    onSaveDefaultTimer: (Int) -> Unit,
    onSaveMockDuration: (Int) -> Unit,
    onThemeChanged: (AppTheme) -> Unit,
    onOpenAbout: () -> Unit,
    onShareApp: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        SettingsGroup("Notes") {
            SwitchRow(
                title = "Auto save",
                subtitle = "Save notes automatically when the dialog closes",
                checked = state.settings.autoSaveNotes,
                onCheckedChange = onAutoSaveNotes,
            )
        }

        SettingsGroup("Calculator") {
            SwitchRow(
                title = "Copy history to notes",
                subtitle = "Append calculator history to the question note",
                checked = state.settings.copyCalculatorHistoryToNotes,
                onCheckedChange = onCopyCalculatorHistory,
            )
        }

        SettingsGroup("Timer") {
            SwitchRow(
                title = "Timer alerts",
                subtitle = "Warn at one minute, 30 seconds and time up",
                checked = state.settings.timerAlerts,
                onCheckedChange = onTimerAlerts,
            )
            if (state.settings.timerAlerts) {
                SwitchRow(
                    title = "Vibrate on time up",
                    checked = state.settings.vibrateOnTimeUp,
                    onCheckedChange = onVibrateOnTimeUp,
                )
            }
            ValueRow(
                title = "Default time",
                value = TimeFormat.clock(state.settings.defaultQuestionSeconds),
                onClick = onEditDefaultTimer,
            )
        }

        SettingsGroup("Mock Tests") {
            ValueRow(
                title = "Test duration",
                value = TimeFormat.clock(state.settings.mockTestSeconds),
                onClick = onEditMockDuration,
            )
        }

        SettingsGroup("Appearance") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.Icon(
                    imageVector = if (state.theme == AppTheme.DARK) {
                        Icons.Rounded.DarkMode
                    } else {
                        Icons.Rounded.LightMode
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = if (state.theme == AppTheme.DARK) "Night mode" else "Default theme",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = Spacing.medium),
                )
                TextButton(
                    onClick = {
                        onThemeChanged(
                            if (state.theme == AppTheme.DARK) AppTheme.LIGHT else AppTheme.DARK
                        )
                    }
                ) { Text("Toggle") }
            }
        }

        SettingsGroup("More") {
            TextButton(onClick = onOpenAbout) { Text("About us") }
            TextButton(onClick = onShareApp) { Text("Share app") }
            TextButton(onClick = onOpenStore) { Text("Rate us") }
            TextButton(onClick = onOpenFeedback) { Text("Send feedback") }
        }

        Spacer(Modifier.height(Spacing.large))
    }

    if (state.editingDefaultTimer) {
        TimePickerDialog(
            title = "Default question timer",
            initialSeconds = state.settings.defaultQuestionSeconds,
            maxMinutes = 15,
            onDismiss = onDismissEditors,
            onConfirm = onSaveDefaultTimer,
        )
    }

    if (state.editingMockDuration) {
        TimePickerDialog(
            title = "Mock test duration",
            initialSeconds = state.settings.mockTestSeconds,
            maxMinutes = 60,
            onDismiss = onDismissEditors,
            onConfirm = onSaveMockDuration,
        )
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    AptitudeCard {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(Spacing.small))
        HorizontalDivider()
        Spacer(Modifier.height(Spacing.small))
        content()
    }
}

@Composable
private fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.tiny),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun ValueRow(title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.tiny),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        TextButton(onClick = onClick) { Text(value) }
    }
}
