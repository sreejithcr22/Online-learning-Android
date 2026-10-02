package com.codit.interview.aptitude.domain.repository

import com.codit.interview.aptitude.domain.model.AppSettings
import com.codit.interview.aptitude.domain.model.AppTheme
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.MockTest
import com.codit.interview.aptitude.domain.model.ProgressSnapshot
import com.codit.interview.aptitude.domain.model.Question
import com.codit.interview.aptitude.domain.model.QuestionNavigator
import com.codit.interview.aptitude.domain.model.QuestionSource
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.domain.model.Topic
import com.codit.interview.aptitude.domain.model.TopicTimer
import kotlinx.coroutines.flow.Flow

/**
 * Read/write access to the practice question bank.
 *
 * Implementations live in `data.repository` and are injected through Hilt.
 */
interface QuestionRepository {

    /** Highest question number in [source]; `0` when the table is missing. */
    suspend fun questionCount(source: QuestionSource): Int

    suspend fun questionCountBySection(section: ContentSection): Int

    suspend fun getQuestion(source: QuestionSource, number: Int): Question?

    /** Answer states for every question in [source], in order. */
    fun observeNavigator(source: QuestionSource): Flow<QuestionNavigator>

    /** Streams one question of [source]; emits `null` when the row is missing. */
    fun observeQuestion(source: QuestionSource, number: Int): Flow<Question?>

    /** The question number the user last reached in [source], for session resume. */
    fun observeLastPosition(source: QuestionSource): Flow<Int>

    /** Persists the question number the user is on, for session resume. */
    suspend fun rememberPosition(source: QuestionSource, number: Int)

    suspend fun recordAnswer(source: QuestionSource, number: Int, optionIndex: Int)

    suspend fun recordTimeTaken(source: QuestionSource, number: Int, seconds: Int)

    suspend fun saveNote(source: QuestionSource, number: Int, note: String)

    /** Copies the question into the favourites table; `false` if already present. */
    suspend fun addToFavourites(source: QuestionSource, number: Int): Boolean

    /** Favourited questions of a section, used by the Favourites screen. */
    fun observeFavourites(section: ContentSection): Flow<List<Question>>

    suspend fun favouriteCount(section: ContentSection): Int
}

/** Mock-test state (scores, lock/finish flags). */
interface MockTestRepository {
    fun observeMockTests(): Flow<List<MockTest>>
    suspend fun recordScore(title: String, score: Int)
    suspend fun questionCount(mockTest: MockTest): Int
}

/** Interview tips and formulas — both share the same `interview.db` shape. */
interface TipRepository {
    fun observeTips(table: String): Flow<List<Tip>>
    suspend fun getTip(table: String, number: Int): Tip?
    suspend fun getTipByTitle(table: String, title: String): Tip?
    suspend fun addToFavourites(table: String, tipNumber: Int, favouritesTable: String): Boolean
    suspend fun lastTipNumber(table: String): Int
}

/** Cross-cutting progress counters and per-topic completion percentages. */
interface ProgressRepository {
    fun observeProgress(): Flow<ProgressSnapshot>

    /**
     * Records a newly answered question.
     *
     * @param wasFirstAttempt false when the question had already been answered before,
     *   in which case the global counters are left untouched.
     */
    suspend fun registerAnswer(section: ContentSection, wasFirstAttempt: Boolean, isCorrect: Boolean)

    suspend fun registerTimeSpent(seconds: Int)

    suspend fun setTopicProgress(topic: Topic, percent: Int)

    fun observeTopicProgress(topic: Topic): Flow<Int>

    suspend fun incrementVisitCount()
}

/** Per-topic timer durations. */
interface TopicTimerRepository {
    fun observeTimers(section: ContentSection): Flow<List<TopicTimer>>
    suspend fun setTimer(topic: Topic, seconds: Int)
    /** Rewrites every topic still holding [oldSeconds] to [newSeconds]. */
    suspend fun replaceDefaultTime(oldSeconds: Int, newSeconds: Int)
}

/** App settings and theme. */
interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun updateSettings(settings: AppSettings)
    fun observeTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}

/** One-off counters such as "first launch" and "already asked for a review". */
interface AppStateRepository {
    fun isFirstVisit(): Boolean
    suspend fun markVisited()
    fun hasRequestedReview(): Boolean
    suspend fun markReviewRequested()
}
