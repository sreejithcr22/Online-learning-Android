package com.codit.interview.aptitude.domain.model

/** Progress state of a timed practice session, persisted per topic. */
data class TopicTimer(
    val topic: Topic,
    val seconds: Int,
)

/** A mock test and the user's score for it. */
data class MockTest(
    val index: Int,
    val title: String,
    val score: Int,
    val isFinished: Boolean,
    val isLocked: Boolean,
    val questionCount: Int,
) {
    val table: String get() = "mock$index"
}

/** Aggregate, denormalised progress snapshot backing the analytics screen. */
data class ProgressSnapshot(
    val quantitativeAttempted: Int = 0,
    val logicalAttempted: Int = 0,
    val verbalAttempted: Int = 0,
    val generalKnowledgeAttempted: Int = 0,
    val correct: Int = 0,
    val wrong: Int = 0,
    val totalTimeSeconds: Int = 0,
    val highestQuestionTimeSeconds: Int = 0,
    val quantitativeTotal: Int = 0,
    val logicalTotal: Int = 0,
    val verbalTotal: Int = 0,
    val generalKnowledgeTotal: Int = 0,
    val mockTestsCompleted: Int = 0,
    val mockTestCount: Int = 0,
    val mockAverageScore: Int = 0,
    /** How many times the app has been opened. */
    val visitCount: Int = 0,
    /** Whether the Play Store review prompt has already been accepted once. */
    val hasRequestedReview: Boolean = false,
    /** Per-topic completion percentage, keyed by [Topic]. */
    val topicProgress: Map<Topic, Int> = emptyMap(),
) {
    val aptitudeAttempted: Int get() = quantitativeAttempted + logicalAttempted + verbalAttempted
    val aptitudeTotal: Int get() = quantitativeTotal + logicalTotal + verbalTotal
    val practiceAttempted: Int get() = aptitudeAttempted + generalKnowledgeAttempted
    val practiceTotal: Int get() = aptitudeTotal + generalKnowledgeTotal

    val correctAndWrong: Int get() = correct + wrong

    val accuracyPercent: Int
        get() = if (correctAndWrong == 0) 0 else correct * 100 / correctAndWrong

    val averageTimeSeconds: Int
        get() = if (aptitudeAttempted == 0) 0 else totalTimeSeconds / aptitudeAttempted

    fun sectionAttempted(section: ContentSection): Int = when (section) {
        ContentSection.QUANTITATIVE -> quantitativeAttempted
        ContentSection.LOGICAL -> logicalAttempted
        ContentSection.VERBAL -> verbalAttempted
        ContentSection.GENERAL_KNOWLEDGE -> generalKnowledgeAttempted
    }

    fun sectionTotal(section: ContentSection): Int = when (section) {
        ContentSection.QUANTITATIVE -> quantitativeTotal
        ContentSection.LOGICAL -> logicalTotal
        ContentSection.VERBAL -> verbalTotal
        ContentSection.GENERAL_KNOWLEDGE -> generalKnowledgeTotal
    }

    fun sectionPercent(section: ContentSection): Int {
        val total = sectionTotal(section)
        return if (total == 0) 0 else sectionAttempted(section) * 100 / total
    }

    fun overallPercent(): Int {
        val total = practiceTotal
        return if (total == 0) 0 else practiceAttempted * 100 / total
    }
}

/** Outcome of a finished practice or mock-test session, shown on the report screen. */
data class SessionReport(
    val title: String,
    val correct: Int,
    val wrong: Int,
    val notAttempted: Int,
    val total: Int,
    /** Per-question elapsed seconds; empty for mock tests, which do not track per-question time. */
    val timePerQuestionSeconds: List<Int> = emptyList(),
) {
    val attempted: Int get() = correct + wrong
    val showTimeAnalysis: Boolean get() = timePerQuestionSeconds.isNotEmpty()

    val accuracyPercent: Int
        get() = if (attempted == 0) 0 else correct * 100 / attempted

    val totalTimeSeconds: Int get() = timePerQuestionSeconds.sum()
    val averageTimeSeconds: Int
        get() = if (timePerQuestionSeconds.isEmpty()) 0 else totalTimeSeconds / timePerQuestionSeconds.size
    val highestTimeSeconds: Int get() = timePerQuestionSeconds.maxOrNull() ?: 0
    val lowestTimeSeconds: Int get() = timePerQuestionSeconds.minOrNull() ?: 0
}

/** The set of answer states for every question in a table — used to build the navigator. */
data class QuestionNavigator(
    val total: Int,
    val statuses: List<AttemptStatus>,
) {
    val correctCount: Int get() = statuses.count { it == AttemptStatus.CORRECT }
    val wrongCount: Int get() = statuses.count { it == AttemptStatus.WRONG }
    val notAttemptedCount: Int get() = statuses.count { it == AttemptStatus.NOT_ATTEMPTED }
}
