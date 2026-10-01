package com.health.calculator.bmi.tracker.data.management

import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class StorageAnalyzerTest {
    private lateinit var root: File
    private lateinit var scopedContext: Context
    private lateinit var analyzer: StorageAnalyzer

    @Before fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        root = File(context.cacheDir, "storage-test-${UUID.randomUUID()}").apply { mkdirs() }
        scopedContext = object : ContextWrapper(context) {
            override fun getCacheDir() = File(root, "cache").apply { mkdirs() }
            override fun getFilesDir() = File(root, "files").apply { mkdirs() }
            override fun getDatabasePath(name: String) = File(root, "databases/$name").also { it.parentFile!!.mkdirs() }
        }
        analyzer = StorageAnalyzer(scopedContext)
    }

    @After fun tearDown() { root.deleteRecursively() }

    @Test fun storageCountsExportsOnceAndIncludesDatabaseSidecars() = runBlocking {
        File(scopedContext.cacheDir, "ordinary").writeBytes(ByteArray(10))
        File(scopedContext.cacheDir, "exports").mkdirs()
        File(scopedContext.cacheDir, "exports/report.csv").writeBytes(ByteArray(20))
        val database = scopedContext.getDatabasePath("health_calculator_database")
        database.writeBytes(ByteArray(30))
        File(database.path + "-wal").writeBytes(ByteArray(40))
        val info = analyzer.analyzeStorage()
        assertEquals(100L, info.totalBytes)
        assertEquals(70L, info.historyBytes)
        assertEquals(10L, info.cacheBytes)
        assertEquals(20L, info.exportsBytes)
    }

    @Test fun ordinaryCacheCleanupPreservesExportsUntilExplicitlyCleared() = runBlocking {
        File(scopedContext.cacheDir, "ordinary").writeBytes(ByteArray(10))
        File(scopedContext.cacheDir, "exports").mkdirs()
        val report = File(scopedContext.cacheDir, "exports/report.csv").apply { writeBytes(ByteArray(20)) }
        assertEquals(10L, analyzer.clearCache())
        assertTrue(report.exists())
        assertEquals(20L, analyzer.clearExports())
        assertFalse(report.exists())
    }
}
