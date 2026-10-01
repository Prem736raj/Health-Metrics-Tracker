package com.health.calculator.bmi.tracker.data.management

import dagger.hilt.android.qualifiers.ApplicationContext

import android.content.Context
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StorageAnalyzer internal constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        @Volatile
        private var instance: StorageAnalyzer? = null

        fun getInstance(@ApplicationContext context: Context): StorageAnalyzer {
            return instance ?: synchronized(this) {
                instance ?: StorageAnalyzer(context.applicationContext).also { instance = it }
            }
        }
    }

    suspend fun analyzeStorage(): StorageInfo = withContext(Dispatchers.IO) {
        // Use the correct database name from AppDatabase
        val dbFile = context.getDatabasePath("health_calculator_database")
        val historyBytes = listOf(dbFile, File(dbFile.path + "-wal"), File(dbFile.path + "-shm"))
            .filter { it.isFile }.sumOf { it.length() }

        val cacheBytes = getDirSize(context.cacheDir)
        val exportsDir = File(context.cacheDir, "exports")
        val exportsBytes = getDirSize(exportsDir)
        
        val backupsDir = File(context.filesDir, "backups")
        val backupsBytes = getDirSize(backupsDir)

        val prefsDir = File(context.filesDir.parent ?: "", "shared_prefs")
        val settingsBytes = getDirSize(prefsDir)

        // Also check datastore files
        val datastoreDir = File(context.filesDir, "datastore")
        val datastoreBytes = getDirSize(datastoreDir)

        val totalSettings = settingsBytes + datastoreBytes

        // Exports already belong to cacheBytes; do not count them twice.
        val totalBytes = historyBytes + cacheBytes + totalSettings + backupsBytes

        StorageInfo(
            totalBytes = totalBytes,
            historyBytes = historyBytes,
            cacheBytes = (cacheBytes - exportsBytes).coerceAtLeast(0L), // Cache minus exports subfolder
            settingsBytes = totalSettings,
            exportsBytes = exportsBytes,
            backupsBytes = backupsBytes
        )
    }

    suspend fun clearCache(): Long = withContext(Dispatchers.IO) {
        val before = getDirSize(context.cacheDir)
        // Export removal has its own explicit action. Ordinary cache cleanup
        // must not invalidate a report the user still intends to share.
        context.cacheDir.listFiles()?.filterNot { it.name == "exports" }?.forEach { file ->
            if (file.isDirectory) clearDir(file)
            file.delete()
        }
        val after = getDirSize(context.cacheDir)
        (before - after).coerceAtLeast(0L)
    }

    suspend fun clearExports(): Long = withContext(Dispatchers.IO) {
        val dir = File(context.cacheDir, "exports")
        val size = getDirSize(dir)
        clearDir(dir)
        (size - getDirSize(dir)).coerceAtLeast(0L)
    }

    suspend fun clearBackups(): Long = withContext(Dispatchers.IO) {
        val dir = File(context.filesDir, "backups")
        val size = getDirSize(dir)
        clearDir(dir)
        (size - getDirSize(dir)).coerceAtLeast(0L)
    }

    private fun getDirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        var size = 0L
        dir.walkTopDown().forEach { file ->
            if (file.isFile) size += file.length()
        }
        return size
    }

    private fun clearDir(dir: File) {
        if (!dir.exists()) return
        dir.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                clearDir(file)
                file.delete()
            } else {
                file.delete()
            }
        }
    }
}
