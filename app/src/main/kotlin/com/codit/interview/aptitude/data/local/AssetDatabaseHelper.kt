package com.codit.interview.aptitude.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Opens a SQLite database that ships inside `assets/databases`.
 *
 * Android cannot open a database from the APK directly, so the asset is copied to
 * the app's private files directory on first use and opened from there. This replaces
 * the third-party `sqliteassethelper` artifact with ~40 lines we control, and keeps
 * the pre-seeded 1.7 MB content working with no data migration.
 *
 * @param upgradeAssetPath optional `;`-separated SQL script applied by [onUpgrade]
 */
class AssetDatabaseHelper(
    context: Context,
    private val assetName: String,
    private val version: Int,
    private val upgradeAssetPath: String? = null,
) : SQLiteOpenHelper(context.applicationContext, assetName, null, version) {

    private val appContext = context.applicationContext

    private fun databaseFile(): File = appContext.getDatabasePath(assetName)

    override fun onCreate(db: SQLiteDatabase) = Unit // content is copied, never created

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val script = upgradeAssetPath ?: return
        Log.i(TAG, "upgrading $assetName from $oldVersion to $newVersion using $script")
        val sql = runCatching { appContext.assets.open(script).bufferedReader().use { it.readText() } }
            .getOrNull() ?: run {
            Log.w(TAG, "missing upgrade script: $script")
            return
        }
        db.beginTransaction()
        try {
            splitStatements(sql).forEach { statement ->
                db.execSQL(statement)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e(TAG, "failed to apply $script", e)
        } finally {
            db.endTransaction()
        }
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        copyAssetIfNeeded()
        return super.getWritableDatabase()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        copyAssetIfNeeded()
        return super.getReadableDatabase()
    }

    /**
     * Copies the packaged database next to the app's databases, unless a usable copy
     * is already there.
     */
    private fun copyAssetIfNeeded() {
        val target = databaseFile()
        if (target.exists() && target.length() > 0L) return

        target.parentFile?.mkdirs()
        val temporary = File(target.parentFile, "$assetName.copying")
        try {
            appContext.assets.open(ASSET_DIR + "/" + assetName).use { input ->
                FileOutputStream(temporary).use { output -> input.copyTo(output) }
            }
            // Rename is atomic within the same directory, so a crash mid-copy can
            // never leave a truncated database behind.
            if (!temporary.renameTo(target)) {
                throw IOException("could not move $temporary to $target")
            }
            Log.i(TAG, "seeded $assetName (${target.length()} bytes)")
        } catch (e: Exception) {
            temporary.delete()
            Log.e(TAG, "failed to seed $assetName from assets", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "AssetDatabase"
        const val ASSET_DIR = "databases"

        /**
         * Splits an upgrade script on `;` while ignoring separators inside string
         * literals and comments.
         */
        fun splitStatements(script: String): List<String> {
            val statements = mutableListOf<String>()
            val current = StringBuilder()
            var inString = false
            var index = 0
            while (index < script.length) {
                val char = script[index]
                when {
                    char == '\'' -> {
                        inString = !inString
                        current.append(char)
                    }

                    !inString && char == '-' && index + 1 < script.length && script[index + 1] == '-' -> {
                        // Skip a line comment entirely.
                        while (index < script.length && script[index] != '\n') index++
                        continue
                    }

                    !inString && char == ';' -> {
                        current.toString().trim().takeIf { it.isNotEmpty() }?.let(statements::add)
                        current.setLength(0)
                    }

                    else -> current.append(char)
                }
                index++
            }
            current.toString().trim().takeIf { it.isNotEmpty() }?.let(statements::add)
            return statements
        }
    }
}
