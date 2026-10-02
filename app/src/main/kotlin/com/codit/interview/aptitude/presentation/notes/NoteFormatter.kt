package com.codit.interview.aptitude.presentation.notes

/** Helpers for the per-question note text, which is stored as free text in SQLite. */
object NoteFormatter {

    /**
     * Appends calculator history to an existing note.
     *
     * The legacy code did this inline in two different activities, with slightly
     * different copy ("Copied from Calculator :-" vs "copied from calculator") and both
     * prepending a literal `"null"` when the note column was unset.
     */
    fun appendCalculatorHistory(existing: String, history: String): String {
        val base = existing.trim().takeUnless { it == "null" }.orEmpty()
        val body = history.trim()
        if (body.isEmpty()) return base
        return if (base.isEmpty()) {
            "$HEADER\n$body"
        } else {
            "$base\n$HEADER\n$body"
        }
    }

    private const val HEADER = "Copied from Calculator :-"
}
