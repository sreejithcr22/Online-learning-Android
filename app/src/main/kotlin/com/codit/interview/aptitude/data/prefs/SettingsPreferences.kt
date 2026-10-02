package com.codit.interview.aptitude.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.codit.interview.aptitude.domain.model.AppSettings
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.domain.model.QuestionSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/** Default (non-user-tunable) preferences file. */
@Singleton
class SettingsPreferences @Inject constructor(
    private val preferences: SharedPreferences,
) {

    private val changes: Flow<Unit> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> trySend(Unit) }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(Unit)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun observeSettings(): Flow<AppSettings> = changes.map { readSettings() }

    fun readSettings(): AppSettings = AppSettings(
        autoSaveNotes = preferences.getBoolean(KEY_AUTO_SAVE_NOTES, true),
        copyCalculatorHistoryToNotes = preferences.getBoolean(KEY_CALC_HISTORY, true),
        timerAlerts = preferences.getBoolean(KEY_TIMER_ALERTS, true),
        vibrateOnTimeUp = preferences.getBoolean(KEY_VIBRATE, false),
        defaultQuestionSeconds = readDuration(KEY_DEFAULT_TIMER, DEFAULT_QUESTION_SECONDS),
        mockTestSeconds = readDuration(KEY_MOCK_TIMER, DEFAULT_MOCK_SECONDS),
    )

    fun writeSettings(settings: AppSettings) {
        preferences.edit {
            putBoolean(KEY_AUTO_SAVE_NOTES, settings.autoSaveNotes)
            putBoolean(KEY_CALC_HISTORY, settings.copyCalculatorHistoryToNotes)
            putBoolean(KEY_TIMER_ALERTS, settings.timerAlerts)
            putBoolean(KEY_VIBRATE, settings.vibrateOnTimeUp)
            putString(KEY_DEFAULT_TIMER, formatDuration(settings.defaultQuestionSeconds))
            putString(KEY_MOCK_TIMER, formatDuration(settings.mockTestSeconds))
        }
    }

    fun observeTheme(): Flow<AppTheme> = changes.map { readTheme() }

    fun readTheme(): AppTheme =
        if (preferences.getInt(KEY_THEME, THEME_LIGHT) == THEME_DARK) AppTheme.DARK else AppTheme.LIGHT

    fun writeTheme(theme: AppTheme) {
        preferences.edit {
            putInt(KEY_THEME, if (theme == AppTheme.DARK) THEME_DARK else THEME_LIGHT)
        }
    }

    /** The question number the user last reached, for session resume. */
    fun lastPosition(source: QuestionSource): Int =
        preferences.getInt(lastPositionKey(source), 1).coerceAtLeast(1)

    fun setLastPosition(source: QuestionSource, number: Int) {
        preferences.edit { putInt(lastPositionKey(source), number.coerceAtLeast(1)) }
    }

    private fun readDuration(key: String, fallbackSeconds: Int): Int {
        val raw = preferences.getString(key, null) ?: return fallbackSeconds
        return parseDuration(raw) ?: fallbackSeconds
    }

    private fun parseDuration(raw: String): Int? {
        val parts = raw.split(":")
        if (parts.size != 2) return null
        val minutes = parts[0].trim().toIntOrNull() ?: return null
        val seconds = parts[1].trim().toIntOrNull() ?: return null
        return minutes * 60 + seconds
    }

    private fun formatDuration(totalSeconds: Int): String =
        "%02d:%02d".format(totalSeconds / 60, totalSeconds % 60)

    private fun lastPositionKey(source: QuestionSource) = "last_position:${source.key}"

    private companion object {
        const val KEY_AUTO_SAVE_NOTES = "note_save"
        const val KEY_CALC_HISTORY = "calc_save"
        const val KEY_TIMER_ALERTS = "timer_alert"
        const val KEY_VIBRATE = "timeup_vibrate"
        const val KEY_DEFAULT_TIMER = "timer_time"
        const val KEY_MOCK_TIMER = "mock_time"
        const val KEY_THEME = "app_theme"

        const val DEFAULT_QUESTION_SECONDS = 150 // 02:30
        const val DEFAULT_MOCK_SECONDS = 35 * 60 // 35:00

        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
    }
}
