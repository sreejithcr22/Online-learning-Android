package com.codit.interview.aptitude.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The shipped SQLite content stores everything as TEXT and uses the literal string
 * "null" for missing values, so these parsers sit on the critical path of every read.
 */
class SqliteTextTest {

    @Test
    fun `null literal detection`() {
        assertTrue(SqliteText.isNull(null))
        assertTrue(SqliteText.isNull(""))
        assertTrue(SqliteText.isNull("   "))
        assertTrue(SqliteText.isNull("null"))
        assertTrue(SqliteText.isNull(" null "))
        assertFalse(SqliteText.isNull("option1"))
        assertFalse(SqliteText.isNull("0"))
    }

    @Test
    fun `option columns become zero based indices`() {
        assertEquals(0, SqliteText.optionIndex("option1"))
        assertEquals(1, SqliteText.optionIndex("option2"))
        assertEquals(2, SqliteText.optionIndex("option3"))
        assertEquals(3, SqliteText.optionIndex("option4"))
        assertEquals(0, SqliteText.optionIndex(" option1 "))
    }

    @Test
    fun `unparseable option columns become null`() {
        assertNull(SqliteText.optionIndex(null))
        assertNull(SqliteText.optionIndex("null"))
        assertNull(SqliteText.optionIndex("option0"))
        assertNull(SqliteText.optionIndex("option5"))
        assertNull(SqliteText.optionIndex("nonsense"))
    }

    @Test
    fun `option index round trips`() {
        (0..3).forEach { assertEquals(it, SqliteText.optionIndex(SqliteText.optionColumn(it))) }
    }

    @Test
    fun `durations parse from mm colon ss`() {
        assertEquals(0, SqliteText.durationSeconds("0:00"))
        assertEquals(36, SqliteText.durationSeconds("0:36"))
        assertEquals(154, SqliteText.durationSeconds("2:34"))
        assertEquals(2100, SqliteText.durationSeconds("35:00"))
        assertEquals(154, SqliteText.durationSeconds(" 2:34 "))
    }

    @Test
    fun `unparseable durations become null`() {
        assertNull(SqliteText.durationSeconds(null))
        assertNull(SqliteText.durationSeconds("null"))
        assertNull(SqliteText.durationSeconds("2"))
        assertNull(SqliteText.durationSeconds("a:b"))
        assertNull(SqliteText.durationSeconds("2:3:4"))
    }

    @Test
    fun `durations format with padding`() {
        assertEquals("00:00", SqliteText.durationText(0))
        assertEquals("00:36", SqliteText.durationText(36))
        assertEquals("02:34", SqliteText.durationText(154))
        assertEquals("35:00", SqliteText.durationText(2100))
    }

    @Test
    fun `durations round trip`() {
        listOf(0, 36, 154, 2100, 3599).forEach {
            assertEquals(it, SqliteText.durationSeconds(SqliteText.durationText(it)))
        }
    }
}