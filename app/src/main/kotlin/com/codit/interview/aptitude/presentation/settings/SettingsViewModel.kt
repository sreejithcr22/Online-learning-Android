package com.codit.interview.aptitude.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codit.interview.aptitude.domain.model.AppSettings
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.domain.repository.SettingsRepository
import com.codit.interview.aptitude.domain.usecase.ChangeDefaultTimerUseCase
import com.codit.interview.aptitude.domain.usecase.ObserveSettingsUseCase
import com.codit.interview.aptitude.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLoading: Boolean = true,
    val settings: AppSettings = AppSettings(),
    val theme: AppTheme = AppTheme.LIGHT,
    val editingDefaultTimer: Boolean = false,
    val editingMockDuration: Boolean = false,
)

/**
 * Settings screen state.
 *
 * Replaces `SettingsActivity` + its `PreferenceFragment`, which needed a
 * `res/xml/settings.xml` resource, four copy-pasted `setSummary("Enabled")` blocks and
 * two near-identical `NumberPicker` dialogs.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeSettings: ObserveSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase,
    private val changeDefaultTimer: ChangeDefaultTimerUseCase,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val editingDefaultTimer = MutableStateFlow(false)
    private val editingMockDuration = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> = combine(
        observeSettings(),
        settingsRepository.observeTheme(),
        editingDefaultTimer,
        editingMockDuration,
    ) { settings, theme, editingDefault, editingMock ->
        SettingsUiState(
            isLoading = false,
            settings = settings,
            theme = theme,
            editingDefaultTimer = editingDefault,
            editingMockDuration = editingMock,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = SettingsUiState(),
    )

    fun setAutoSaveNotes(value: Boolean) = update { it.copy(autoSaveNotes = value) }

    fun setCopyCalculatorHistory(value: Boolean) =
        update { it.copy(copyCalculatorHistoryToNotes = value) }

    fun setTimerAlerts(value: Boolean) = update { it.copy(timerAlerts = value) }

    fun setVibrateOnTimeUp(value: Boolean) = update { it.copy(vibrateOnTimeUp = value) }

    fun openDefaultTimerEditor() {
        editingDefaultTimer.value = true
    }

    fun openMockDurationEditor() {
        editingMockDuration.value = true
    }

    fun dismissEditors() {
        editingDefaultTimer.value = false
        editingMockDuration.value = false
    }

    fun saveDefaultTimer(seconds: Int) {
        editingDefaultTimer.value = false
        val previous = uiState.value.settings.defaultQuestionSeconds
        viewModelScope.launch { changeDefaultTimer(previous, seconds) }
    }

    fun saveMockDuration(seconds: Int) {
        editingMockDuration.value = false
        update { it.copy(mockTestSeconds = seconds) }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { settingsRepository.setTheme(theme) }
    }

    private fun update(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch { updateSettings(transform(uiState.value.settings)) }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
