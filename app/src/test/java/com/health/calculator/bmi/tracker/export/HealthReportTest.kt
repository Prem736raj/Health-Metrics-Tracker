package com.health.calculator.bmi.tracker.export

import com.health.calculator.bmi.tracker.data.export.HealthReport
import com.health.calculator.bmi.tracker.data.export.ReportPeriod
import com.health.calculator.bmi.tracker.data.export.ReportRecord
import com.health.calculator.bmi.tracker.data.export.ReportTool
import com.health.calculator.bmi.tracker.data.export.ReportWindow
import com.health.calculator.bmi.tracker.data.model.CalculatorType
import com.health.calculator.bmi.tracker.data.records.MedicalRecordKind
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

class HealthReportTest {
    private val now = Instant.parse("2026-11-22T05:00:00Z")
    private val window = ReportWindow(ReportPeriod.WEEK, now, ZoneId.of("Asia/Kolkata"))

    @Test
    fun everyMedicalRecordHasASectionAndPrivateSectionsAreOptIn() {
        assertEquals(MedicalRecordKind.entries.toSet(), ReportTool.entries.mapNotNull { it.medicalRecordKind }.toSet())
        val report = HealthReport(window, mapOf(
            ReportTool.GLUCOSE to listOf(ReportRecord(window.end, "98 mg/dL")),
            ReportTool.MEDICATIONS to listOf(ReportRecord(window.end, "Sample medication")),
            ReportTool.VISITS to listOf(ReportRecord(window.end, "Follow-up"))
        ))
        assertEquals(listOf(ReportTool.GLUCOSE), report.snapshot(ReportTool.defaultSelection).sections.map { it.tool })
        assertEquals(3, report.snapshot(ReportTool.defaultSelection + ReportTool.MEDICATIONS + ReportTool.VISITS).recordCount)
    }

    @Test
    fun everyRecordedCalculatorHasExactlyOneReportSection() {
        val mappedCalculators = ReportTool.entries.mapNotNull { it.calculator }
        assertEquals(CalculatorType.entries.toSet(), mappedCalculators.toSet())
        assertEquals(mappedCalculators.size, mappedCalculators.toSet().size)
        assertFalse(ReportTool.BLOOD_PRESSURE.fromCalculationHistory)
        assertFalse(ReportTool.WATER_ESTIMATE.fromCalculationHistory)
    }

    @Test
    fun previewFiltersLocalDatesOrdersRecordsAndDoesNotMergeEqualResults() {
        val report = HealthReport(window, mapOf(
            ReportTool.WEIGHT to listOf(
                ReportRecord(window.start, "72 kg"),
                ReportRecord(window.start - 1, "71 kg"),
                ReportRecord(window.end, "72 kg"),
                ReportRecord(window.end + 1, "73 kg")
            ),
            ReportTool.BMI to listOf(ReportRecord(window.end, "23 kg/m²"))
        ))

        assertEquals(Instant.parse("2026-11-15T18:30:00Z").toEpochMilli(), window.start)
        assertEquals(listOf(ReportTool.WEIGHT, ReportTool.BMI), report.sections.map { it.tool })
        val selected = report.snapshot(setOf(ReportTool.WEIGHT))
        assertEquals(2, selected.recordCount)
        assertEquals(listOf(window.end, window.start), selected.sections.single().records.map { it.timestamp })
        assertTrue(report.snapshot(emptySet()).sections.isEmpty())
    }

    @Test
    fun toolsWithOnlyBlankOrOutOfRangeResultsAreOmitted() {
        val report = HealthReport(window, mapOf(
            ReportTool.BMI to listOf(ReportRecord(window.end, "")),
            ReportTool.BMR to listOf(ReportRecord(window.start - 1, "1,600 kcal/day"))
        ))
        assertTrue(report.sections.isEmpty())
    }

    @Test
    fun pdfPacksSmallSectionsAndRetainsEveryRowAcrossPages() {
        val pressure = List(35) { index -> ReportRecord(window.end - index, "120/80 mmHg") }
        val report = HealthReport(window, mapOf(
            ReportTool.BLOOD_PRESSURE to pressure,
            ReportTool.WEIGHT to listOf(ReportRecord(window.end, "72 kg")),
            ReportTool.BMI to listOf(ReportRecord(window.end, "23 kg/m²"))
        ))
        val pages = report.snapshot(ReportTool.entries.toSet()).tablePages()
        assertEquals(2, pages.size)
        assertEquals(27, pages.first().single().rows.size)
        assertTrue(pages.last().first().continued)
        assertEquals(pressure, pages.flatMap { it }.filter { it.section.tool == ReportTool.BLOOD_PRESSURE }.flatMap { it.rows })
        assertEquals(3, pages.last().size)
    }
}
