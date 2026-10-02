package com.codit.interview.aptitude.core.util

/**
 * The pre-seeded SQLite content stores most of its numeric-ish data as TEXT and in
 * several different shapes ("2:34", " 12 ", "null"). These helpers are the single
 * place where that mess is normalised.
 */
object SqliteText {

    const val NULL_LITERAL = "null"

    fun isNull(raw: String?): Boolean {
        val value = raw?.trim() ?: return true
        return value.isEmpty() || value == NULL_LITERAL
    }

    /** Parses "option1".."option4" into a 0-based index, or `null` when not attempted. */
    fun optionIndex(raw: String?): Int? {
        if (isNull(raw)) return null
        val digit = raw!!.trim().removePrefix("option").toIntOrNull() ?: return null
        return if (digit in 1..4) digit - 1 else null
    }

    fun optionColumn(index: Int): String = "option${index + 1}"

    /** Parses "mm:ss" into seconds. Returns `null` for missing/garbage input. */
    fun durationSeconds(raw: String?): Int? {
        if (isNull(raw)) return null
        val parts = raw!!.trim().split(":")
        if (parts.size != 2) return null
        val minutes = parts[0].trim().toIntOrNull() ?: return null
        val seconds = parts[1].trim().toIntOrNull() ?: return null
        return minutes * 60 + seconds
    }

    fun durationText(totalSeconds: Int): String =
        "%02d:%02d".format(totalSeconds / 60, totalSeconds % 60)

    fun bool(value: Boolean): String = if (value) "true" else "false"

    fun toBoolean(raw: String?): Boolean = raw?.trim() == "true"
}
