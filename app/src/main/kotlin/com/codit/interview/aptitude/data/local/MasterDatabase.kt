package com.codit.interview.aptitude.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Low-level data access for the question bank stored in `assets/databases/master.db`.
 *
 * Every question topic lives in its own table with an identical schema, so all reads
 * and writes go through this one class with the table name as a parameter — the same
 * shape the legacy code used, minus the per-activity duplication.
 */
class MasterDatabase(context: Context) {

    private val helper = AssetDatabaseHelper(
        context = context,
        assetName = ASSET_NAME,
        version = VERSION,
        upgradeAssetPath = "databases/master.db_upgrade_1-2.sql",
    )

    private val db: SQLiteDatabase get() = helper.writableDatabase

    /** Highest `qno` in [table]; `0` for a blank or missing table. */
    fun lastQuestionNumber(table: String): Int {
        if (table.isBlank()) return 0
        return scalarInt("SELECT MAX(qno) FROM \"$table\"") ?: 0
    }

    fun questionCount(table: String): Int {
        if (table.isBlank()) return 0
        return scalarInt("SELECT COUNT(*) FROM \"$table\"") ?: 0
    }

    /**
     * Streams every question of [table] in `qno` order. The cursor is fully materialised
     * and closed before returning so nothing leaks across threads.
     */
    fun readQuestions(table: String): List<QuestionRow> {
        if (table.isBlank()) return emptyList()
        return db.rawQuery(
            "SELECT qno, que, option1, option2, option3, option4, answer, explanation, " +
                "attempted, notes, time, fav FROM \"$table\" ORDER BY qno ASC",
            null,
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) add(cursor.toQuestionRow())
            }
        }
    }

    fun readQuestion(table: String, number: Int): QuestionRow? {
        if (table.isBlank()) return null
        return db.query(
            table,
            arrayOf(
                "qno", "que", "option1", "option2", "option3", "option4", "answer",
                "explanation", "attempted", "notes", "time", "fav",
            ),
            "qno = ?",
            arrayOf(number.toString()),
            null, null, null,
        ).use { cursor ->
            if (cursor.moveToFirst()) cursor.toQuestionRow() else null
        }
    }

    /** Number of rows in a table, or `0` when the table does not exist. */
    fun countRows(table: String): Int = runCatching { questionCount(table) }.getOrDefault(0)

    fun updateColumn(table: String, number: Int, column: String, value: String): Int {
        val values = ContentValues().apply { put(column, value) }
        return db.update(table, values, "qno = ?", arrayOf(number.toString()))
    }

    /**
     * Copies a question into [favouriteTable] and flags the source row.
     *
     * @return false when the row was already favourited.
     */
    fun copyToFavourites(
        sourceTable: String,
        number: Int,
        favouriteTable: String,
    ): Boolean {
        val source = readQuestion(sourceTable, number) ?: return false
        if (source.favourite) return false

        return db.runInTransaction {
            val values = ContentValues().apply {
                put("que", source.text)
                put("option1", source.options.getOrElse(0) { "" })
                put("option2", source.options.getOrElse(1) { "" })
                put("option3", source.options.getOrElse(2) { "" })
                put("option4", source.options.getOrElse(3) { "" })
                put("answer", source.correctOptionColumn)
                put("explanation", source.explanation)
                put("notes", source.note)
            }
            val inserted = db.insert(favouriteTable, null, values)
            if (inserted == -1L) {
                false
            } else {
                val updated = db.update(
                    sourceTable,
                    ContentValues().apply { put("fav", "true") },
                    "qno = ?",
                    arrayOf(number.toString()),
                )
                updated == 1
            }
        }
    }

    private fun scalarInt(sql: String): Int? = db.rawQuery(sql, null).use { cursor ->
        if (cursor.moveToFirst() && !cursor.isNull(0)) cursor.getInt(0) else null
    }

    private fun Cursor.string(column: String): String? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getString(index)
    }

    private fun Cursor.toQuestionRow() = QuestionRow(
        number = getInt(getColumnIndexOrThrow("qno")),
        text = string("que").orEmpty(),
        options = listOf(
            string("option1").orEmpty(),
            string("option2").orEmpty(),
            string("option3").orEmpty(),
            string("option4").orEmpty(),
        ),
        correctOptionColumn = string("answer").orEmpty(),
        explanation = string("explanation"),
        attemptedOptionColumn = string("attempted"),
        note = string("notes").orEmpty(),
        timeText = string("time"),
        favourite = string("fav")?.trim() == "true",
    )

    private companion object {
        const val ASSET_NAME = "master.db"
        const val VERSION = 2
    }
}

/** Raw, un-normalised question as stored in SQLite. Mapping happens in the data layer. */
data class QuestionRow(
    val number: Int,
    val text: String,
    val options: List<String>,
    val correctOptionColumn: String,
    val explanation: String?,
    val attemptedOptionColumn: String?,
    val note: String,
    val timeText: String?,
    val favourite: Boolean,
)
