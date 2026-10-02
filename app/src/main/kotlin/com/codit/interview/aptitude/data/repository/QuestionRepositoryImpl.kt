package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.core.util.SqliteText
import com.codit.interview.aptitude.data.local.MasterDatabase
import com.codit.interview.aptitude.data.local.QuestionRow
import com.codit.interview.aptitude.data.mapper.toDomain
import com.codit.interview.aptitude.data.prefs.ProgressPreferences
import com.codit.interview.aptitude.data.prefs.SettingsPreferences
import com.codit.interview.aptitude.domain.model.AttemptStatus
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.domain.model.QuestionSource
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.repository.QuestionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Reads and writes the practice question bank.
 *
 * SQLite gives us no change notifications, so writes bump a monotonically increasing
 * [revision]. Every observable combines that revision with the preference-change
 * stream, which makes the UI refresh itself after any write — the manual
 * "notifyDataSetChanged / re-read on resume" plumbing of the legacy fragments is gone.
 */
@Singleton
class QuestionRepositoryImpl @Inject constructor(
    private val database: MasterDatabase,
    private val progressPreferences: ProgressPreferences,
    private val settingsPreferences: SettingsPreferences,
    private val dispatchers: DispatcherProvider,
) : QuestionRepository {

    private val revision = MutableStateFlow(0L)
    private val writeLock = Mutex()

    /** Re-emits whenever questions, notes, favourites or per-question timers change. */
    private fun changes(): Flow<Long> = combine(
        revision,
        settingsPreferences.observeSettings(),
    ) { value, _ -> value }.onStart { emit(revision.value) }

    private fun signalChange() {
        revision.value = revision.value + 1
    }

    override suspend fun questionCount(source: QuestionSource): Int =
        withContext(dispatchers.io) { database.lastQuestionNumber(source.table) }

    override suspend fun questionCountBySection(section: ContentSection): Int =
        withContext(dispatchers.io) {
            section.topicsWithQuestions().sumOf { database.lastQuestionNumber(it.table) }
        }

    override suspend fun getQuestion(source: QuestionSource, number: Int): Question? =
        withContext(dispatchers.io) {
            database.readQuestion(source.table, number)?.toDomain()
        }

    override fun observeNavigator(source: QuestionSource): Flow<QuestionNavigator> =
        changes().map { readNavigator(source.table) }.flowOn(dispatchers.io)

    override fun observeQuestion(source: QuestionSource, number: Int): Flow<Question?> =
        changes().map { database.readQuestion(source.table, number)?.toDomain() }
            .flowOn(dispatchers.io)

    override fun observeLastPosition(source: QuestionSource): Flow<Int> =
        changes().map { settingsPreferences.lastPosition(source) }.distinctUntilChanged()

    override suspend fun rememberPosition(source: QuestionSource, number: Int) =
        withContext(dispatchers.io) { settingsPreferences.setLastPosition(source, number) }

    override suspend fun recordAnswer(source: QuestionSource, number: Int, optionIndex: Int) {
        updateColumn(source, number, "attempted", SqliteText.optionColumn(optionIndex))
    }

    override suspend fun recordTimeTaken(source: QuestionSource, number: Int, seconds: Int) {
        updateColumn(source, number, "time", SqliteText.durationText(seconds))
    }

    override suspend fun saveNote(source: QuestionSource, number: Int, note: String) {
        updateColumn(source, number, "notes", note)
    }

    override suspend fun addToFavourites(source: QuestionSource, number: Int): Boolean =
        withContext(dispatchers.io) {
            val added = writeLock.withLock {
                database.copyToFavourites(source.table, number, source.favouriteTable)
            }
            signalChange()
            added
        }

    override fun observeFavourites(section: ContentSection): Flow<List<Question>> =
        changes()
            .map { database.readQuestions(QuestionSource.OfFavourites(section).table) }
            .map { rows -> rows.map(QuestionRow::toDomain) }
            .flowOn(dispatchers.io)

    override suspend fun favouriteCount(section: ContentSection): Int =
        withContext(dispatchers.io) {
            database.countRows(QuestionSource.OfFavourites(section).table)
        }

    private suspend fun updateColumn(
        source: QuestionSource,
        number: Int,
        column: String,
        value: String,
    ) = withContext(dispatchers.io) {
        writeLock.withLock { database.updateColumn(source.table, number, column, value) }
        signalChange()
    }

    private fun readNavigator(table: String): QuestionNavigator {
        val rows = database.readQuestions(table)
        val statuses = rows.map { row ->
            AttemptStatus.of(
                answeredOptionIndex = SqliteText.optionIndex(row.attemptedOptionColumn),
                correctOptionIndex = SqliteText.optionIndex(row.correctOptionColumn) ?: 0,
            )
        }
        return QuestionNavigator(total = database.lastQuestionNumber(table), statuses = statuses)
    }

    private companion object {
        fun ContentSection.topicsWithQuestions(): List<Topic> = when (this) {
            ContentSection.QUANTITATIVE -> Topic.quantitative
            ContentSection.LOGICAL -> Topic.logical
            ContentSection.VERBAL -> Topic.verbal
            ContentSection.GENERAL_KNOWLEDGE -> Topic.generalKnowledge.filterNot { it.isInfoOnly }
        }
    }
}
