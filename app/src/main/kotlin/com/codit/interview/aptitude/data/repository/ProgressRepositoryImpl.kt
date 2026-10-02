package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.data.local.MasterDatabase
import com.codit.interview.aptitude.data.prefs.ProgressPreferences
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.model.ProgressSnapshot
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.MockTestRepository
import com.codit.interview.aptitude.domain.repository.ProgressRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Builds the analytics snapshot.
 *
 * Question totals are read from SQLite rather than hardcoded: the legacy app carried
 * constants (372/300/228) that no longer matched the shipped content, which made the
 * overall progress percentage meaningless.
 */
@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressPreferences: ProgressPreferences,
    private val master: MasterDatabase,
    private val mockTests: MockTestRepository,
    private val dispatchers: DispatcherProvider,
) : ProgressRepository {

    override fun observeProgress(): Flow<ProgressSnapshot> = combine(
        progressPreferences.observe(),
        mockTests.observeMockTests(),
    ) { _, mockTestList -> buildSnapshot(mockTestList) }
        .flowOn(dispatchers.io)

    override suspend fun registerAnswer(
        section: ContentSection,
        wasFirstAttempt: Boolean,
        isCorrect: Boolean,
    ) = withContext(dispatchers.io) {
        if (!wasFirstAttempt) return@withContext
        progressPreferences.incrementSectionAttempted(section)
        if (isCorrect) progressPreferences.incrementCorrect() else progressPreferences.incrementWrong()
    }

    override suspend fun registerTimeSpent(seconds: Int) = withContext(dispatchers.io) {
        progressPreferences.addTimeSpent(seconds)
    }

    override suspend fun setTopicProgress(topic: Topic, percent: Int) =
        withContext(dispatchers.io) {
            progressPreferences.setTopicProgress(topic, percent)
        }

    override fun observeTopicProgress(topic: Topic): Flow<Int> =
        progressPreferences.observeTopicProgress(topic)

    override suspend fun incrementVisitCount() = withContext(dispatchers.io) {
        progressPreferences.incrementVisitCount()
    }

    private fun buildSnapshot(mockTestList: List<MockTest>): ProgressSnapshot {
        val values = progressPreferences.currentValues()
        val completed = mockTestList.filter { it.isFinished }
        val totalScore = completed.sumOf { it.score }

        return ProgressSnapshot(
            quantitativeAttempted = values.quantitativeAttempted,
            logicalAttempted = values.logicalAttempted,
            verbalAttempted = values.verbalAttempted,
            generalKnowledgeAttempted = values.generalKnowledgeAttempted,
            correct = values.correct,
            wrong = values.wrong,
            totalTimeSeconds = values.totalTimeSeconds,
            highestQuestionTimeSeconds = values.highestQuestionTimeSeconds,
            quantitativeTotal = countFor(ContentSection.QUANTITATIVE),
            logicalTotal = countFor(ContentSection.LOGICAL),
            verbalTotal = countFor(ContentSection.VERBAL),
            generalKnowledgeTotal = countFor(ContentSection.GENERAL_KNOWLEDGE),
            mockTestsCompleted = completed.size,
            mockTestCount = mockTestList.size,
            mockAverageScore = if (completed.isEmpty()) 0 else totalScore / completed.size,
            visitCount = values.visitCount,
            hasRequestedReview = progressPreferences.hasRequestedReview(),
            topicProgress = Topic.entries.associateWith { progressPreferences.topicProgress(it) },
        )
    }

    /**
     * Total questions in a section, read from SQLite.
     *
     * Info-only GK topics (books-and-authors, days-and-dates) live in `interview.db`
     * rather than `master.db`, so they are excluded.
     */
    private fun countFor(section: ContentSection): Int = when (section) {
        ContentSection.QUANTITATIVE -> Topic.quantitative
        ContentSection.LOGICAL -> Topic.logical
        ContentSection.VERBAL -> Topic.verbal
        ContentSection.GENERAL_KNOWLEDGE -> Topic.generalKnowledge.filterNot { it.isInfoOnly }
    }.sumOf { master.lastQuestionNumber(it.table) }
}
