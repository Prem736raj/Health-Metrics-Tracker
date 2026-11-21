package com.health.calculator.bmi.tracker.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.health.calculator.bmi.tracker.data.export.HealthReportRepository
import com.health.calculator.bmi.tracker.data.export.ReportPeriod
import com.health.calculator.bmi.tracker.data.export.ReportTool
import com.health.calculator.bmi.tracker.data.export.ReportWindow
import com.health.calculator.bmi.tracker.data.records.MedicalRecord
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import com.health.calculator.bmi.tracker.data.records.normalize
import com.health.calculator.bmi.tracker.data.repository.FoodLogRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class MedicalRecordsIntegrationTest {
    @Test
    fun medicalRecordsSurviveEditsAndReportSelectionKeepsPrivateDetailsOut() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        try {
            val dao = database.medicalRecordDao()
            val now = Instant.now()
            val timestamp = now.minusSeconds(30).toEpochMilli()
            val glucose = record(MedicalRecordKind.GLUCOSE, timestamp, "5,40", "mmol/L", "Before meal")
            val samples = listOf(
                glucose,
                record(MedicalRecordKind.TEMPERATURE, timestamp, "98.60", "°F", "Oral"),
                record(MedicalRecordKind.OXYGEN, timestamp, "97", "%", detail = "72"),
                record(MedicalRecordKind.MEDICATION, timestamp, "SampleMedicine", "", "Current", "PrivateInstructions"),
                record(MedicalRecordKind.VISIT, timestamp, "SampleVisit", "", detail = "PrivateClinic")
            )
            samples.forEach { assertTrue(dao.insert(it) >= 0L) }
            assertEquals(-1L, dao.insert(glucose))
            assertEquals(5, dao.observe().first().size)
            assertEquals(1, dao.update(glucose.copy(context = "After meal")))

            val report = HealthReportRepository(database, FoodLogRepository(context))
                .load(ReportWindow(ReportPeriod.MONTH, now))
            val normal = report.snapshot(ReportTool.defaultSelection)
            assertEquals(listOf(ReportTool.GLUCOSE, ReportTool.TEMPERATURE, ReportTool.OXYGEN),
                normal.sections.map { it.tool }.filter { it.medicalRecordKind != null })
            val edited = normal.sections.single { it.tool == ReportTool.GLUCOSE }.records.single()
            assertEquals("5.40 mmol/L", edited.result)
            assertEquals("After meal", edited.context)
            val privateOnly = report.snapshot(setOf(ReportTool.MEDICATIONS, ReportTool.VISITS))
            assertEquals(listOf("SampleMedicine", "SampleVisit"), privateOnly.sections.map { it.records.single().result })
            assertFalse(privateOnly.sections.flatMap { it.records }.any { "Private" in it.result || "Private" in it.context })

            dao.delete(glucose.id)
            assertEquals(4, dao.observe().first().size)
            assertFalse(dao.observe().first().any { it.id == glucose.id })
        } finally {
            database.close()
        }
    }

    private fun record(kind: MedicalRecordKind, timestamp: Long, value: String, unit: String, tag: String = "",
        detail: String = ""
    ): MedicalRecord = requireNotNull(kind.normalize(
        MedicalRecord(kind.name, kind.name, timestamp, value, unit, tag, detail, "PrivateNote"), timestamp
    ))
}
