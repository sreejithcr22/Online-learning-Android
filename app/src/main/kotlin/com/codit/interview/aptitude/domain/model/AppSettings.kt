package com.codit.interview.aptitude.domain.model

/** User-tunable app settings (replaces the `PreferenceFragment` screen). */
data class AppSettings(
    val autoSaveNotes: Boolean = true,
    val copyCalculatorHistoryToNotes: Boolean = true,
    val timerAlerts: Boolean = true,
    val vibrateOnTimeUp: Boolean = true,
    /** Default per-question timer, in seconds. */
    val defaultQuestionSeconds: Int = 150,
    /** Mock-test duration, in seconds. */
    val mockTestSeconds: Int = 35 * 60,
)

/** App-wide visual theme. */
enum class AppTheme { LIGHT, DARK }
