package com.codit.interview.aptitude.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.codit.interview.aptitude.domain.model.Topic

/**
 * Writable, app-created database holding the two pieces of state the content databases
 * do not own: mock-test scores and the per-topic timer durations.
 *
 * The legacy version seeded itself from string resources and needed a manual
 * `onUpgrade` insert every time a topic was added. Here the seed is derived from the
 * [com.codit.interview.aptitude.domain.model.Topic] enum, and the v2 → v3 migration
 * carries existing per-topic timers across, so upgrading users keep their settings.
 */
class UserStateDatabase(context: Context) {

    private val helper = OpenHelper(context.applicationContext)

    private val db: SQLiteDatabase get() = helper.writableDatabase

    fun readMockTests(): List<MockTestRow> = db.query(
        TABLE_MOCK_STATE, null, null, null, null, null, "rowid ASC",
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(
                    MockTestRow(
                        title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        score = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE)),
                        isFinished =
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IS_FINISHED)) == "true",
                        isLocked = cursor.getString(cursor.getColumnIndexOrThrow(COL_IS_LOCKED)) == "true",
                    )
                )
            }
        }
    }

    fun recordScore(title: String, score: Int) {
        val values = ContentValues().apply {
            put(COL_SCORE, score)
            put(COL_IS_FINISHED, "true")
        }
        db.update(TABLE_MOCK_STATE, values, "$COL_TITLE = ?", arrayOf(title))
    }

    /** Display name → `"mm:ss"` for every topic of [parent]. */
    fun readTopicTimers(parent: String): Map<String, String> = db.query(
        TABLE_TOPIC_TIMERS, null, "$COL_PARENT = ?", arrayOf(parent), null, null, "rowid ASC",
    ).use { cursor ->
        buildMap {
            while (cursor.moveToNext()) {
                put(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TOPIC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)),
                )
            }
        }
    }

    fun setTopicTimer(topic: String, time: String, parent: String) {
        val updated = db.update(
            TABLE_TOPIC_TIMERS,
            ContentValues().apply { put(COL_TIME, time) },
            "$COL_TOPIC = ?",
            arrayOf(topic),
        )
        if (updated == 0) {
            db.insert(TABLE_TOPIC_TIMERS, null, ContentValues().apply {
                put(COL_TOPIC, topic)
                put(COL_TIME, time)
                put(COL_PARENT, parent)
            })
        }
    }

    /** Bulk-migrates every topic still on [oldTime] to [newTime]. */
    fun replaceTimeForAll(oldTime: String, newTime: String) {
        db.update(
            TABLE_TOPIC_TIMERS,
            ContentValues().apply { put(COL_TIME, newTime) },
            "$COL_TIME = ?",
            arrayOf(oldTime),
        )
    }

    private class OpenHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

        override fun onConfigure(db: SQLiteDatabase) {
            super.onConfigure(db)
            db.setForeignKeyConstraintsEnabled(true)
        }

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $TABLE_MOCK_STATE (
                    $COL_TITLE TEXT PRIMARY KEY,
                    $COL_SCORE INTEGER NOT NULL DEFAULT 0,
                    $COL_IS_FINISHED TEXT NOT NULL DEFAULT 'false',
                    $COL_IS_LOCKED TEXT NOT NULL DEFAULT 'true'
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE $TABLE_TOPIC_TIMERS (
                    $COL_TOPIC TEXT PRIMARY KEY,
                    $COL_TIME TEXT NOT NULL,
                    $COL_PARENT TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.runInTransaction {
                repeat(MOCK_TEST_COUNT) { index ->
                    db.insert(
                        TABLE_MOCK_STATE, null,
                        ContentValues().apply {
                            put(COL_TITLE, mockTitle(index))
                            put(COL_SCORE, 0)
                            put(COL_IS_FINISHED, "false")
                            put(COL_IS_LOCKED, "true")
                        }
                    )
                }
                Topic.entries.forEach { topic ->
                    db.insert(
                        TABLE_TOPIC_TIMERS, null,
                        ContentValues().apply {
                            put(COL_TOPIC, topic.displayName)
                            put(COL_TIME, DEFAULT_TIME)
                            put(COL_PARENT, topic.section.name)
                        }
                    )
                }
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            if (oldVersion < 3) {
                // v2 stored timers in `apti_categs(category, time, parent)` with a
                // hand-written row per topic. Carry those over, then add any topic
                // that has appeared since.
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS $TABLE_TOPIC_TIMERS (
                        $COL_TOPIC TEXT PRIMARY KEY,
                        $COL_TIME TEXT NOT NULL,
                        $COL_PARENT TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                runCatching {
                    db.execSQL(
                        "INSERT OR REPLACE INTO $TABLE_TOPIC_TIMERS($COL_TOPIC, $COL_TIME, $COL_PARENT) " +
                            "SELECT category, time, parent FROM $LEGACY_TABLE_TOPIC_TIMERS"
                    )
                }
                Topic.entries.forEach { topic ->
                    db.insertWithOnConflict(
                        TABLE_TOPIC_TIMERS, null,
                        ContentValues().apply {
                            put(COL_TOPIC, topic.displayName)
                            put(COL_TIME, DEFAULT_TIME)
                            put(COL_PARENT, topic.section.name)
                        },
                        SQLiteDatabase.CONFLICT_IGNORE,
                    )
                }
                runCatching { db.execSQL("DROP TABLE IF EXISTS $LEGACY_TABLE_TOPIC_TIMERS") }
            }
        }
    }

    private companion object {
        const val DATABASE_NAME = "userstate.db"
        const val VERSION = 3

        const val TABLE_MOCK_STATE = "mockstate"
        const val COL_TITLE = "title"
        const val COL_SCORE = "score"
        const val COL_IS_FINISHED = "isfinished"
        const val COL_IS_LOCKED = "islocked"

        const val TABLE_TOPIC_TIMERS = "topic_timers"
        const val COL_TOPIC = "topic"
        const val COL_TIME = "time"
        const val COL_PARENT = "parent"

        const val LEGACY_TABLE_TOPIC_TIMERS = "apti_categs"

        const val MOCK_TEST_COUNT = 10
        const val DEFAULT_TIME = "02:00"

        fun mockTitle(index: Int) = "Mock Test ${index + 1}"
    }
}

/** Raw mock-test row. */
data class MockTestRow(
    val title: String,
    val score: Int,
    val isFinished: Boolean,
    val isLocked: Boolean,
)
