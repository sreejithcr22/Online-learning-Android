package com.codit.interview.aptitude.domain.model

/** How a question stood the last time it was answered. Drives the navigator colours. */
enum class AttemptStatus { NOT_ATTEMPTED, CORRECT, WRONG;

/** Recomputed from the stored answer so the navigator can never drift from the data. */
    companion object {
        fun of(answeredOptionIndex: Int?, correctOptionIndex: Int): AttemptStatus = when {
            answeredOptionIndex == null -> NOT_ATTEMPTED
            answeredOptionIndex == correctOptionIndex -> CORRECT
            else -> WRONG
        }
    }
}

/**
 * A single multiple-choice question.
 *
 * The legacy `Question` model exposed raw column strings ("option2", "null", "1:42").
 * Everything is normalised here at the data boundary so the rest of the app works
 * with types instead of sentinel strings.
 */
data class Question(
    val number: Int,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String?,
    val answeredOptionIndex: Int?,
    val note: String,
    val timeTakenSeconds: Int?,
    val isFavourite: Boolean,
) {
    val attemptStatus: AttemptStatus get() = AttemptStatus.of(answeredOptionIndex, correctOptionIndex)

    val isAnswered: Boolean get() = answeredOptionIndex != null

    /** Falls back to the bare answer when no explanation was authored for the question. */
    fun explanationOrDefault(): String =
        explanation?.takeIf { it.isNotBlank() }
            ?: "Correct answer is ${options.getOrElse(correctOptionIndex) { "unknown" }}"
}
