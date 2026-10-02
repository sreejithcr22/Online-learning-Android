package com.codit.interview.aptitude.data.prefs

import android.content.SharedPreferences
import com.codit.interview.aptitude.domain.model.ContentSection
import com.codit.interview.aptitude.domain.model.Topic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regression tests for the progress change signal.
 *
 * The bug: `changes` was wrapped in `distinctUntilChanged()` even though it carries a
 * `Unit` signal. Because `Unit == Unit`, that suppressed every emission after the first,
 * so the questions home screen and the analytics dashboard stayed frozen at their initial
 * values no matter how many questions were answered.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProgressPreferencesTest {

    @Test
    fun `change signal keeps emitting after the first change`() = runTest {
        val prefs = FakeSharedPreferences()
        val store = ProgressPreferences(prefs)

        val seen = mutableListOf<Unit>()
        val collector = launch { store.observe().collect { seen.add(it) } }
        testScheduler.runCurrent()

        // The signal fires once on subscription.
        assertEquals("expected an initial emission", 1, seen.size)

        prefs.putInt("QUANTI_ATTEMPTED", 1)
        prefs.putInt("QUANTI_ATTEMPTED", 2)
        prefs.putInt("QUANTI_ATTEMPTED", 3)
        testScheduler.runCurrent()

        assertEquals(
            "every write must produce an emission; Unit-signal dedup would collapse these",
            4,
            seen.size,
        )

        collector.cancel()
    }

    @Test
    fun `currentValues reflects writes`() {
        val prefs = FakeSharedPreferences()
        val store = ProgressPreferences(prefs)

        assertEquals(0, store.currentValues().quantitativeAttempted)

        store.incrementSectionAttempted(ContentSection.QUANTITATIVE)
        store.incrementSectionAttempted(ContentSection.QUANTITATIVE)
        store.incrementCorrect()
        store.incrementWrong()
        store.incrementWrong()
        store.addTimeSpent(90)
        store.addTimeSpent(30)

        val values = store.currentValues()
        assertEquals(2, values.quantitativeAttempted)
        assertEquals(1, values.correct)
        assertEquals(2, values.wrong)
        assertEquals(120, values.totalTimeSeconds)
        assertEquals("highest single question time", 90, values.highestQuestionTimeSeconds)
    }

    @Test
    fun `topic progress re-emits when the percentage changes`() = runTest {
        val prefs = FakeSharedPreferences()
        val store = ProgressPreferences(prefs)
        val topic = Topic.TIME_AND_WORK

        val emissions = mutableListOf<Int>()
        val collector = launch { store.observeTopicProgress(topic).collect { emissions.add(it) } }
        testScheduler.runCurrent()

        assertEquals(listOf(0), emissions)

        store.setTopicProgress(topic, 25)
        testScheduler.runCurrent()
        assertEquals(listOf(0, 25), emissions)

        store.setTopicProgress(topic, 50)
        testScheduler.runCurrent()
        assertEquals(listOf(0, 25, 50), emissions)

        collector.cancel()
    }

    @Test
    fun `topic progress is deduplicated when the value does not change`() = runTest {
        val prefs = FakeSharedPreferences()
        val store = ProgressPreferences(prefs)
        val topic = Topic.AVERAGE

        val emissions = mutableListOf<Int>()
        val collector = launch { store.observeTopicProgress(topic).collect { emissions.add(it) } }
        testScheduler.runCurrent()

        store.setTopicProgress(topic, 40)
        testScheduler.runCurrent()
        // A write to a different topic must not re-emit this topic's value.
        store.setTopicProgress(TobotPlaceholder, 40)
        testScheduler.runCurrent()

        assertEquals(listOf(0, 40), emissions)
        collector.cancel()
    }

    @Test
    fun `progress is clamped to a sane range`() {
        val store = ProgressPreferences(FakeSharedPreferences())
        store.setTopicProgress(Topic.SERIES, 150)
        assertEquals(100, store.topicProgress(Topic.SERIES))
        store.setTopicProgress(Topic.SERIES, -20)
        assertEquals(0, store.topicProgress(Topic.SERIES))
    }

    @Test
    fun `change signal stops after collection is cancelled`() = runTest {
        val prefs = FakeSharedPreferences()
        val store = ProgressPreferences(prefs)

        val seen = mutableListOf<Unit>()
        val collector = launch { store.observe().collect { seen.add(it) } }
        testScheduler.runCurrent()
        collector.cancel()
        testScheduler.runCurrent()

        val afterCancel = seen.size
        prefs.putInt("CORRECT", 5)
        testScheduler.runCurrent()

        assertEquals("listener must be unregistered on close", afterCancel, seen.size)
    }

    private companion object {
        val TobotPlaceholder = Topic.TIME_AND_WORK
    }
}

/** In-memory [SharedPreferences] that notifies listeners, like the real one. */
private class FakeSharedPreferences : SharedPreferences {

    private val values = mutableMapOf<String, Any?>()
    private val listeners = mutableSetOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    fun putInt(key: String, value: Int) {
        values[key] = value
        listeners.toList().forEach { it.onSharedPreferenceChanged(this, key) }
    }

    override fun getAll(): MutableMap<String, *> = values.toMutableMap()

    override fun getString(key: String, defValue: String?): String? = values[key] as? String ?: defValue

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? =
        values[key] as? MutableSet<String> ?: defValues

    override fun getInt(key: String, defValue: Int): Int = values[key] as? Int ?: defValue

    override fun getLong(key: String, defValue: Long): Long = values[key] as? Long ?: defValue

    override fun getFloat(key: String, defValue: Float): Float = values[key] as? Float ?: defValue

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        values[key] as? Boolean ?: defValue

    override fun contains(key: String): Boolean = values.containsKey(key)

    override fun edit(): SharedPreferences.Editor = FakeEditor()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) {
        listener?.let { listeners.add(it) }
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) {
        listener?.let { listeners.remove(it) }
    }

    private inner class FakeEditor : SharedPreferences.Editor {
        private val pending = mutableMapOf<String, Any?>()
        private val removals = mutableSetOf<String>()
        private var clearAll = false

        override fun putString(key: String, value: String?) = apply { pending[key] = value }

        override fun putStringSet(key: String, values: MutableSet<String>?) =
            apply { pending[key] = values }

        override fun putInt(key: String, value: Int) = apply { pending[key] = value }

        override fun putLong(key: String, value: Long) = apply { pending[key] = value }

        override fun putFloat(key: String, value: Float) = apply { pending[key] = value }

        override fun putBoolean(key: String, value: Boolean) = apply { pending[key] = value }

        override fun remove(key: String) = apply { removals.add(key) }

        override fun clear() = apply { clearAll = true }

        override fun commit(): Boolean {
            if (clearAll) values.clear()
            removals.forEach { values.remove(it) }
            pending.forEach { (k, v) -> values[k] = v }
            listeners.toList().forEach { listener ->
                (if (clearAll) values.keys.toList() else pending.keys.toList())
                    .forEach { listener.onSharedPreferenceChanged(this@FakeSharedPreferences, it) }
            }
            return true
        }

        override fun apply() {
            commit()
        }
    }
}
