package com.codit.interview.aptitude.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/** Low-level data access for `assets/databases/interview.db` (tips, formulas, GK info). */
class InterviewDatabase(context: Context) {

    private val helper = AssetDatabaseHelper(
        context = context,
        assetName = ASSET_NAME,
        version = VERSION,
    )

    private val db: SQLiteDatabase get() = helper.writableDatabase

    fun readTips(table: String): List<TipRow> = db.query(
        table,
        arrayOf("tipno", "title", "tip", "fav"),
        null, null, null, null,
        "tipno ASC",
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) add(cursor.toTipRow())
        }
    }

    fun readTip(table: String, number: Int): TipRow? = db.query(
        table,
        arrayOf("tipno", "title", "tip", "fav"),
        "tipno = ?",
        arrayOf(number.toString()),
        null, null, null,
    ).use { cursor -> if (cursor.moveToFirst()) cursor.toTipRow() else null }

    fun readTipByTitle(table: String, title: String): TipRow? = db.query(
        table,
        arrayOf("tipno", "title", "tip", "fav"),
        "title = ?",
        arrayOf(title),
        null, null, null,
    ).use { cursor -> if (cursor.moveToFirst()) cursor.toTipRow() else null }

    fun lastTipNumber(table: String): Int =
        db.rawQuery("SELECT MAX(tipno) FROM \"$table\"", null).use { cursor ->
            if (cursor.moveToFirst() && !cursor.isNull(0)) cursor.getInt(0) else 0
        }

    /** Copies a tip into [favouriteTable] and flags the source row. */
    fun copyToFavourites(table: String, tipNumber: Int, favouriteTable: String): Boolean {
        val source = readTip(table, tipNumber) ?: return false
        if (source.favourite) return false

        return db.runInTransaction {
            val values = ContentValues().apply {
                put("tip", source.body)
                put("title", source.title)
            }
            if (db.insert(favouriteTable, null, values) == -1L) {
                false
            } else {
                db.update(
                    table,
                    ContentValues().apply { put("fav", "true") },
                    "tipno = ?",
                    arrayOf(tipNumber.toString()),
                ) == 1
            }
        }
    }

    private fun Cursor.string(column: String): String? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getString(index)
    }

    private fun Cursor.toTipRow() = TipRow(
        number = getInt(getColumnIndexOrThrow("tipno")),
        title = string("title").orEmpty(),
        body = string("tip").orEmpty(),
        favourite = string("fav")?.trim() == "true",
    )

    private companion object {
        const val ASSET_NAME = "interview.db"
        const val VERSION = 1
    }
}

/** Raw tip row as stored in SQLite. */
data class TipRow(
    val number: Int,
    val title: String,
    val body: String,
    val favourite: Boolean,
)
