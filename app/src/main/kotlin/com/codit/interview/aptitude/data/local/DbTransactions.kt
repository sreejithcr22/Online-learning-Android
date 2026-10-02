package com.codit.interview.aptitude.data.local

import android.database.sqlite.SQLiteDatabase

/**
 * Runs [body] inside a transaction, committing only if it completes normally.
 *
 * `androidx.core` ships an equivalent as `transaction(...)`, but it is still marked
 * experimental; this 10-line version keeps the data layer free of opt-in annotations.
 */
internal inline fun <T> SQLiteDatabase.runInTransaction(body: () -> T): T {
    beginTransaction()
    return try {
        val result = body()
        setTransactionSuccessful()
        result
    } finally {
        endTransaction()
    }
}
