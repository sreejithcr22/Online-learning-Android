package com.codit.interview.aptitude.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Covers the derived statistics shown on the analytics and report screens. */
class ProgressModelTest {

    private val snapshot = ProgressSnapshot(
        quantitativeAttempted = 10,
        logicalAttempted = 5,
        verbalAttempted = 2,
        generalKnowledgeAttempted = 8,
        correct = 12,
        wrong = 4,
        totalTimeSeconds = 600,
        highestQuestionTimeSeconds = 90,
        quantitativeTotal = 100,
        logicalTotal = 50,
        verbalTotal = 25,
        generalKnowledgeTotal = 40,
        mockTestsCompleted = 3,
        mockTestCount = 10,
        mockAverageScore = 17,
    )

    @Test
    fun `totals aggregate per section`() {
        assertEquals(17, snapshot.aptitudeAttempted)
        assertEquals(175, snapshot.aptitudeTotal)
        assertEquals(25, snapshot.practiceAttempted)
        assertEquals(215, snapshot.practiceTotal)
    }

    @Test
    fun `accuracy is correct over attempted, not over the whole bank`() {
        assertEquals(75, snapshot.accuracyPercent)
    }

    @Test
    fun `accuracy is zero when nothing has been attempted`() {
        assertEquals(0, snapshot.copy(correct = 0, wrong = 0).accuracyPercent)
    }

    @Test
    fun `average time divides by aptitude attempts only`() {
        // GK questions are untimed, so they must not drag the average down.
        assertEquals(35, snapshot.averageTimeSeconds)
    }

    @Test
    fun `overall percent uses the real shipped totals`() {
        assertEquals(11, snapshot.overallPercent())
    }

    @Test
    fun `section percents`() {
        assertEquals(10, snapshot.sectionPercent(ContentSection.QUANTITATIVE))
        assertEquals(10, snapshot.sectionPercent(ContentSection.LOGICAL))
        assertEquals(8, snapshot.sectionPercent(ContentSection.VERBAL))
        assertEquals(20, snapshot.sectionPercent(ContentSection.GENERAL_KNOWLEDGE))
    }

    @Test
    fun `empty sections do not divide by zero`() {
        assertEquals(0, snapshot.copy(generalKnowledgeTotal = 0).sectionPercent(ContentSection.GENERAL_KNOWLEDGE))
        assertEquals(0, ProgressSnapshot().overallPercent())
    }
}

class AttemptStatusTest {

    @Test
    fun `status is derived from the stored answer, never stored itself`() {
        assertEquals(AttemptStatus.NOT_ATTEMPTED, AttemptStatus.of(null, correctOptionIndex = 1))
        assertEquals(AttemptStatus.CORRECT, AttemptStatus.of(1, correctOptionIndex = 1))
        assertEquals(AttemptStatus.WRONG, AttemptStatus.of(2, correctOptionIndex = 1))
    }
}

class QuestionTest {

    private fun question(
        explanation: String? = null,
        answered: Int? = null,
        correct: Int = 0,
        options: List<String> = listOf("a", "b", "c", "d"),
    ) = Question(
        number = 1,
        text = "q",
        options = options,
        correctOptionIndex = correct,
        explanation = explanation,
        answeredOptionIndex = answered,
        note = "",
        timeTakenSeconds = null,
        isFavourite = false,
    )

    @Test
    fun `a question with no explanation falls back to naming the answer`() {
        assertEquals("Correct answer is a", question().explanationOrDefault())
    }

    @Test
    fun `a blank explanation is treated as missing`() {
        assertEquals("Correct answer is a", question(explanation = "   ").explanationOrDefault())
    }

    @Test
    fun `a real explanation is used verbatim`() {
        assertEquals("because", question(explanation = "because").explanationOrDefault())
    }

    @Test
    fun `answered state follows the stored answer`() {
        assertTrue(!question().isAnswered)
        assertTrue(question(answered = 1).isAnswered)
    }

    @Test
    fun `attempt status is computed from the answer`() {
        assertEquals(AttemptStatus.CORRECT, question(answered = 0).attemptStatus)
        assertEquals(AttemptStatus.WRONG, question(answered = 2).attemptStatus)
    }
}

class SessionReportTest {

    @Test
    fun `time statistics are derived from the per question times`() {
        val report = SessionReport(
            title = "Time And Work",
            correct = 3,
            wrong = 1,
            notAttempted = 1,
            total = 5,
            timePerQuestionSeconds = listOf(30, 60, 45, 15),
        )
        assertEquals(150, report.totalTimeSeconds)
        assertEquals(37, report.averageTimeSeconds)
        assertEquals(15, report.lowestTimeSeconds)
        assertEquals(60, report.highestTimeSeconds)
        assertEquals(75, report.accuracyPercent)
        assertTrue(report.showTimeAnalysis)
    }

    @Test
    fun `a mock test has no time analysis`() {
        val report = SessionReport("Mock Test 1", correct = 5, wrong = 2, notAttempted = 18, total = 25)
        assertTrue(!report.showTimeAnalysis)
        assertEquals(0, report.totalTimeSeconds)
        assertEquals(71, report.accuracyPercent)
    }
}

class TopicTest {

    @Test
    fun `every practice topic maps to a real table`() {
        Topic.entries.forEach { topic ->
            if (topic.isInfoOnly) return@forEach
            assertTrue("${topic.name} has no table", topic.table.isNotBlank())
        }
    }

    @Test
    fun `only GK topics are info only`() {
        assertTrue(Topic.entries.filter { it.isInfoOnly }.all { it.isGeneralKnowledge })
    }

    @Test
    fun `sections contain the expected topics`() {
        assertEquals(16, Topic.quantitative.size)
        assertEquals(14, Topic.logical.size)
        assertEquals(10, Topic.verbal.size)
    }

    @Test
    fun `lookup by display name round trips`() {
        Topic.entries.forEach { assertEquals(it, Topic.byDisplayName(it.displayName)) }
        assertNull(Topic.byDisplayName("Not A Topic"))
    }

    @Test
    fun `GK favourites go to their own table`() {
        assertEquals("gk_fav_table", Topic.INDIAN_POLITICS.favouriteTable)
        assertEquals("fav_table", Topic.TIME_AND_WORK.favouriteTable)
    }

    @Test
    fun `favourites source reads the right table per section`() {
        assertEquals("fav_table", QuestionSource.OfFavourites(ContentSection.QUANTITATIVE).table)
        assertEquals("gk_fav_table", QuestionSource.OfFavourites(ContentSection.GENERAL_KNOWLEDGE).table)
    }

    @Test
    fun `mock tests map onto their own table`() {
        val mock = MockTest(
            index = 7,
            title = "Mock Test 7",
            score = 0,
            isFinished = false,
            isLocked = false,
            questionCount = 25,
        )
        val source = QuestionSource.OfMockTest(mock)
        assertEquals("mock7", source.table)
        assertEquals("Mock Test 7", source.displayTitle)
    }
}