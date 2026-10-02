package com.codit.interview.aptitude.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Global progress counters and per-topic completion percentages.
 *
 * Replaces the free-form `SharedPreferences` writes scattered across the legacy
 * activities: every key is now a constant on this class, and changes are exposed as
 * a [Flow] so the UI recomposes instead of being poked by hand.
 */
@Singleton
class ProgressPreferences @Inject constructor(
    private val preferences: SharedPreferences,
) {

    /**
     * Emits once on subscription and again on every preference write.
     *
     * Deliberately *not* wrapped in `distinctUntilChanged()`: this flow carries a `Unit`
     * signal, and `Unit == Unit`, so deduplicating would suppress every emission after
     * the first and freeze all progress counters at their initial value. Deduplication,
     * where it is actually wanted, is applied to the mapped values instead.
     */
    private val changes: Flow<Unit> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(Unit)
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(Unit)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun observe(): Flow<Unit> = changes

    fun currentValues(): Values = Values(
        quantitativeAttempted = preferences.getInt(KEY_QUANTITATIVE, 0),
        logicalAttempted = preferences.getInt(KEY_LOGICAL, 0),
        verbalAttempted = preferences.getInt(KEY_VERBAL, 0),
        generalKnowledgeAttempted = preferences.getInt(KEY_GENERAL_KNOWLEDGE, 0),
        correct = preferences.getInt(KEY_CORRECT, 0),
        wrong = preferences.getInt(KEY_WRONG, 0),
        totalTimeSeconds = preferences.getInt(KEY_TOTAL_TIME, 0),
        highestQuestionTimeSeconds = preferences.getInt(KEY_HIGHEST_TIME, 0),
        visitCount = preferences.getInt(KEY_VISIT_COUNT, 0),
        mockTestsCompleted = preferences.getInt(KEY_MOCK_COMPLETED, 0),
    )

    fun topicProgress(topic: Topic): Int =
        preferences.getFloat(keyForTopic(topic), 0f).toInt()

    /** Completion percentage for one topic; re-emits whenever it actually changes. */
    fun observeTopicProgress(topic: Topic): Flow<Int> =
        changes.map { topicProgress(topic) }.distinctUntilChanged()

    /** Increments the section's attempted counter; only called on a first attempt. */
    fun incrementSectionAttempted(section: ContentSection) {
        val key = when (section) {
            ContentSection.QUANTITATIVE -> KEY_QUANTITATIVE
            ContentSection.LOGICAL -> KEY_LOGICAL
            ContentSection.VERBAL -> KEY_VERBAL
            ContentSection.GENERAL_KNOWLEDGE -> KEY_GENERAL_KNOWLEDGE
        }
        preferences.edit { putInt(key, preferences.getInt(key, 0) + 1) }
    }

    fun incrementCorrect() = preferences.edit {
        putInt(KEY_CORRECT, preferences.getInt(KEY_CORRECT, 0) + 1)
    }

    fun incrementWrong() = preferences.edit {
        putInt(KEY_WRONG, preferences.getInt(KEY_WRONG, 0) + 1)
    }

    fun addTimeSpent(seconds: Int) = preferences.edit {
        putInt(KEY_TOTAL_TIME, preferences.getInt(KEY_TOTAL_TIME, 0) + seconds)
        val highest = preferences.getInt(KEY_HIGHEST_TIME, 0)
        if (highest < seconds) putInt(KEY_HIGHEST_TIME, seconds)
    }

    fun setTopicProgress(topic: Topic, percent: Int) {
        preferences.edit { putFloat(keyForTopic(topic), percent.coerceIn(0, 100).toFloat()) }
    }

    fun incrementVisitCount() = preferences.edit {
        putInt(KEY_VISIT_COUNT, preferences.getInt(KEY_VISIT_COUNT, 0) + 1)
    }

    fun hasRequestedReview(): Boolean = preferences.getBoolean(KEY_REVIEWED, false)

    fun markReviewRequested() = preferences.edit { putBoolean(KEY_REVIEWED, true) }

    private fun keyForTopic(topic: Topic) = "$KEY_TOPIC_PROGRESS_PREFIX${topic.name}"

    data class Values(
        val quantitativeAttempted: Int,
        val logicalAttempted: Int,
        val verbalAttempted: Int,
        val generalKnowledgeAttempted: Int,
        val correct: Int,
        val wrong: Int,
        val totalTimeSeconds: Int,
        val highestQuestionTimeSeconds: Int,
        val visitCount: Int,
        val mockTestsCompleted: Int,
    ) {
        val aptitudeAttempted: Int get() = quantitativeAttempted + logicalAttempted + verbalAttempted
    }

    private companion object {
        const val KEY_QUANTITATIVE = "QUANTI_ATTEMPTED"
        const val KEY_LOGICAL = "LOGIC_ATTEMPTED"
        const val KEY_VERBAL = "VERBAL_ATTEMPTED"
        const val KEY_GENERAL_KNOWLEDGE = "GK_ATTEMPTED"
        const val KEY_CORRECT = "CORRECT"
        const val KEY_WRONG = "WRONG"
        const val KEY_TOTAL_TIME = "TIME"
        const val KEY_HIGHEST_TIME = "max_time"
        const val KEY_VISIT_COUNT = "visitCount"
        const val KEY_MOCK_COMPLETED = "MOCK_COMPLETED"
        const val KEY_REVIEWED = "reviewed"
        const val KEY_TOPIC_PROGRESS_PREFIX = "topic_progress:"
    }
}
