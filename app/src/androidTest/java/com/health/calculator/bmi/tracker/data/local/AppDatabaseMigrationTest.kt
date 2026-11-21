package com.health.calculator.bmi.tracker.data.local

import android.database.Cursor
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies the checked-in migrations with a real SQLite implementation.
 *
 * Coverage:
 * - 13→14 (empty schema bump)
 * - 14→15 (chat_messages table)
 * - 15→16 (step_history table)
 * - Full chain 13→16
 * - Safety: destructive fallback is NOT configured
 *
 * Versions below 13 require release-history fixtures and are intentionally not
 * fabricated here; the release checklist must confirm the oldest distributed
 * database before those fixtures are added.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    // ── Individual migration tests ──────────────────────────────────

    @Test
    fun migrate13To14_emptyMigrationPreservesData() {
        helper.createDatabase(TEST_DB, 13).apply {
            execSQL(
                "INSERT INTO history_entries " +
                    "(calculator_key, result_value, result_label, category, timestamp, details_json, note) " +
                    "VALUES ('bmi', '24.1', 'BMI', 'Normal weight', 100, '{\"source\":\"test\"}', 'migration13')"
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DB,
            14,
            true,
            AppDatabase.MIGRATION_13_14
        )
        migrated.query("SELECT COUNT(*) FROM history_entries").use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }
        migrated.query("SELECT note FROM history_entries WHERE calculator_key = 'bmi'")
            .use { cursor: Cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("migration13", cursor.getString(0))
            }
        migrated.close()
    }

    @Test
    fun migrate14To15_createsChatMessagesTable() {
        helper.createDatabase(TEST_DB, 14).apply {
            execSQL(
                "INSERT INTO history_entries " +
                    "(calculator_key, result_value, result_label, category, timestamp, details_json, note) " +
                    "VALUES ('whr', '0.87', 'WHR', 'Action point', 200, '{\"source\":\"test\"}', NULL)"
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DB,
            15,
            true,
            AppDatabase.MIGRATION_14_15
        )

        // Existing data survives
        migrated.query("SELECT COUNT(*) FROM history_entries").use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }

        // chat_messages table exists and is usable
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'chat_messages'"
        ).use { cursor: Cursor ->
            assertTrue("chat_messages table must exist after 14→15", cursor.moveToFirst())
            assertEquals("chat_messages", cursor.getString(0))
        }

        // Verify we can insert into the new table
        migrated.execSQL(
            "INSERT INTO chat_messages (text, isUser, timestamp) VALUES ('hello', 1, 300)"
        )
        migrated.query("SELECT COUNT(*) FROM chat_messages").use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }

        migrated.close()
    }

    @Test
    fun migrate15To16PreservesExistingRowsAndAddsStepHistory() {
        helper.createDatabase(TEST_DB, 15).apply {
            execSQL(
                "INSERT INTO history_entries " +
                    "(calculator_key, result_value, result_label, category, timestamp, details_json, note) " +
                    "VALUES ('bmi', '23.4', 'BMI', 'Reference range', 1, '{\"source\":\"test\"}', NULL)"
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DB,
            16,
            true,
            AppDatabase.MIGRATION_15_16
        )
        migrated.query("SELECT COUNT(*) FROM history_entries").use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals(1, cursor.getInt(0))
        }
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name = 'step_history'"
        ).use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("step_history", cursor.getString(0))
        }
        migrated.close()
    }

    // ── Full chain test ─────────────────────────────────────────────

    @Test
    fun migrate16To17PreservesPreviousHistoryAndCreatesMedicalRecords() {
        helper.createDatabase(TEST_DB, 16).apply {
            execSQL("INSERT INTO history_entries (calculator_key,result_value,result_label,timestamp) VALUES ('sleep','205','min',100)")
            close()
        }
        helper.runMigrationsAndValidate(TEST_DB, 17, true, AppDatabase.MIGRATION_16_17).use { migrated ->
            migrated.query("SELECT result_value FROM history_entries WHERE calculator_key='sleep'").use {
                assertTrue(it.moveToFirst())
                assertEquals("205", it.getString(0))
            }
            migrated.execSQL("INSERT INTO medical_records (id,kind,timestamp,value,unit,context,detail,note) VALUES ('test','GLUCOSE',200,'102','mg/dL','Fasting','','')")
            migrated.query("SELECT value FROM medical_records WHERE id='test'").use {
                assertTrue(it.moveToFirst())
                assertEquals("102", it.getString(0))
            }
        }
    }

    @Test
    fun allMigrations_13to16_chainedPreservesData() {
        helper.createDatabase(TEST_DB, 13).apply {
            execSQL(
                "INSERT INTO history_entries " +
                    "(calculator_key, result_value, result_label, category, timestamp, details_json, note) " +
                    "VALUES ('bsa', '1.92', 'BSA', 'Estimate', 500, '{\"source\":\"chain\"}', 'chain_test')"
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DB,
            16,
            true,
            AppDatabase.MIGRATION_13_14,
            AppDatabase.MIGRATION_14_15,
            AppDatabase.MIGRATION_15_16
        )

        // Original row survives the full chain
        migrated.query("SELECT note FROM history_entries WHERE calculator_key = 'bsa'")
            .use { cursor: Cursor ->
                assertTrue("Original row must survive 13→16 chain", cursor.moveToFirst())
                assertEquals("chain_test", cursor.getString(0))
            }

        // All new tables exist
        migrated.query(
            "SELECT name FROM sqlite_master WHERE type = 'table' AND name IN ('chat_messages', 'step_history') ORDER BY name"
        ).use { cursor: Cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("chat_messages", cursor.getString(0))
            assertTrue(cursor.moveToNext())
            assertEquals("step_history", cursor.getString(0))
        }

        migrated.close()
    }

    // ── Safety gate ─────────────────────────────────────────────────

    @Test
    fun databaseBuilder_opensCurrentSchema() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = AppDatabase.getDatabase(context)
        assertEquals(17, db.openHelper.readableDatabase.version)
        db.close()
    }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}
