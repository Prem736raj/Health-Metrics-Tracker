package com.health.calculator.bmi.tracker.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.health.calculator.bmi.tracker.data.model.HistoryEntry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryCrudTest {
    private lateinit var database: AppDatabase
    private val dao get() = database.historyDao()

    @Before fun openDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java
        ).build()
    }

    @After fun closeDatabase() = database.close()

    @Test fun createReadUpdateDeleteAndLowercaseLookup() = runBlocking {
        val id = dao.insertEntry(HistoryEntry(calculatorKey = "bmi", resultValue = "24", resultLabel = "BMI"))
        assertEquals(id, dao.getLatestEntryByTypeSync("bmi")?.id)
        dao.updateNote(id, "Updated note")
        assertEquals("Updated note", dao.getById(id)?.note)
        assertEquals(1, dao.deleteEntriesAtomically(listOf(id, id, -1)))
        assertNull(dao.getById(id))
        assertEquals(0, dao.deleteEntriesAtomically(emptyList()))
    }

    @Test fun largeCleanupRollsBackAllChunksOnFailure() = runBlocking {
        database.withTransaction {
            (1L..1200L).forEach { id ->
                dao.insertEntry(HistoryEntry(id = id, calculatorKey = "bmi", resultValue = "24", resultLabel = "BMI"))
            }
        }
        database.openHelper.writableDatabase.execSQL(
            "CREATE TRIGGER reject_test_delete BEFORE DELETE ON history_entries " +
                "WHEN OLD.id = 1001 BEGIN SELECT RAISE(ABORT, 'Simulated storage failure'); END"
        )
        try {
            dao.deleteEntriesAtomically((1L..1200L).toList())
            fail("Expected the second chunk to fail")
        } catch (_: android.database.sqlite.SQLiteException) {
            assertEquals(1200, dao.getAllEntriesSync(2000).size)
        }
        database.openHelper.writableDatabase.execSQL("DROP TRIGGER reject_test_delete")
        assertEquals(1200, dao.deleteEntriesAtomically((1L..1200L).toList()))
        assertTrue(dao.getAllEntriesSync(2000).isEmpty())
    }
}
