package com.health.calculator.bmi.tracker.export

import com.health.calculator.bmi.tracker.data.export.BpVisitPeriod
import com.health.calculator.bmi.tracker.data.export.BpVisitReport
import com.health.calculator.bmi.tracker.data.local.entity.BloodPressureEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class BpVisitReportTest {
    private val zone = ZoneId.of("Asia/Kolkata")
    private val now = Instant.parse("2026-11-22T05:00:00Z")

    @Test
    fun usesLocalCalendarDaysAndRejectsReadingsAfterPreview() {
        val firstIncluded = Instant.parse("2026-11-15T18:30:00Z")
        val report = BpVisitReport(
            BpVisitPeriod.LAST_7_DAYS, now, zone,
            listOf(
                reading(1, firstIncluded.minusMillis(1)),
                reading(2, firstIncluded),
                reading(3, now),
                reading(4, now.plusMillis(1))
            )
        )

        assertEquals(LocalDate.of(2026, 11, 16), report.startDate)
        assertEquals(LocalDate.of(2026, 11, 22), report.endDate)
        assertEquals(listOf(3L, 2L), report.readings.map { it.id })
        assertEquals(2, report.daysWithReadings)
    }

    @Test
    fun includesEveryEligibleEntryAndCountsDaysOnce() {
        val sameDay = (1L..31L).map { id -> reading(id, now) }
        val earlierDay = reading(32, Instant.parse("2026-11-02T05:00:00Z"))
        val groupMember = reading(33, now).copy(isPartOfAverage = true)
        val groupAverage = reading(34, now).copy(isAveragedResult = true, readingsInAverage = 2)
        val report = BpVisitReport(
            BpVisitPeriod.LAST_30_DAYS, now, zone, sameDay + earlierDay + groupMember + groupAverage
        )

        assertEquals(33, report.readings.size)
        assertEquals(2, report.daysWithReadings)
        assertEquals(groupAverage.id, report.readings.first().id)
        assertTrue(report.readings.first().isAveragedResult)
        assertEquals(2, report.readings.first().readingsInAverage)
        assertTrue(report.readings.none { it.isPartOfAverage })
    }

    @Test
    fun stripsPrivateFieldsFromTheExportSnapshotWithoutChangingTheSource() {
        val source = reading(1, now).copy(note = "Private note", medicationName = "Private medication", onMedication = true)
        val report = BpVisitReport(BpVisitPeriod.LAST_7_DAYS, now, zone, listOf(source))
        val exported = report.readings.single()

        assertEquals("", exported.note)
        assertEquals("", exported.medicationName)
        assertFalse(exported.onMedication)
        assertEquals(120, exported.systolic)
        assertEquals("Private note", source.note)
    }

    @Test
    fun emptyPeriodHasNoReadingsOrCoveredDays() {
        val report = BpVisitReport(BpVisitPeriod.LAST_7_DAYS, now, zone, listOf(reading(1, Instant.EPOCH)))
        assertTrue(report.readings.isEmpty())
        assertEquals(0, report.daysWithReadings)
    }

    private fun reading(id: Long, time: Instant) = BloodPressureEntity(
        id = id,
        systolic = 120,
        diastolic = 80,
        category = "NORMAL",
        riskLevel = "LOW",
        measurementTimestamp = time.toEpochMilli(),
        pulsePressure = 40,
        meanArterialPressure = 93.3
    )
}
