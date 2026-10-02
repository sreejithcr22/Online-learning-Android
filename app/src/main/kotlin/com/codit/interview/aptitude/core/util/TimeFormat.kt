package com.codit.interview.aptitude.core.util

/** Formatting helpers for the durations shown across timers, analytics and reports. */
object TimeFormat {

    /** `mm:ss`, used for question and mock-test countdowns. */
    fun clock(totalSeconds: Int): String {
        val safe = totalSeconds.coerceAtLeast(0)
        return "%02d:%02d".format(safe / 60, safe % 60)
    }

    /** Countdown that keeps counting below zero, e.g. `-00:12`. */
    fun countdown(totalSeconds: Int): String =
        if (totalSeconds < 0) "-%02d:%02d".format(-totalSeconds / 60, -totalSeconds % 60)
        else clock(totalSeconds)

    /** `mm:ss` or `hh:mm:ss` for the aggregate "total time" analytics card. */
    fun longClock(totalSeconds: Int): String {
        val safe = totalSeconds.coerceAtLeast(0)
        val hours = safe / 3600
        val minutes = (safe % 3600) / 60
        val seconds = safe % 60
        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

    /** Compact human form used for totals: `2h 15m`. */
    fun hoursAndMinutes(totalSeconds: Int): String {
        val safe = totalSeconds.coerceAtLeast(0)
        val hours = safe / 3600
        val minutes = (safe % 3600) / 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}
