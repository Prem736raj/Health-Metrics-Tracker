package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.data.calculator.SleepCalculation
import com.health.calculator.bmi.tracker.data.calculator.calculateSleep
import com.health.calculator.bmi.tracker.data.calculator.formatSleepDuration
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class SleepDurationTest {
    private val kolkata = ZoneId.of("Asia/Kolkata")
    private val now = Instant.parse("2026-11-22T18:00:00Z")

    @Test
    fun overnightSessionUsesPreviousDateAndSubtractsBothAwakePeriods() {
        val result = calculation("22:30", "06:30", settling = "15", awake = "20") as SleepCalculation.Valid
        assertEquals(Instant.parse("2026-11-21T17:00:00Z"), result.estimate.start)
        assertEquals(Instant.parse("2026-11-22T01:00:00Z"), result.estimate.wake)
        assertEquals(480L, result.estimate.intervalMinutes)
        assertEquals(445L, result.estimate.sleepMinutes)
        assertEquals("7 h 25 min", formatSleepDuration(result.estimate.sleepMinutes))
    }

    @Test
    fun daytimeNapAndZeroSleepCanBeRecorded() {
        val nap = calculation("14:00", "14:35") as SleepCalculation.Valid
        assertEquals(35L, nap.estimate.sleepMinutes)
        val awake = calculation("14:00", "14:35", settling = "35") as SleepCalculation.Valid
        assertEquals(0L, awake.estimate.sleepMinutes)
    }

    @Test
    fun rejectsInvalidTimesAndAwakeDurations() {
        assertEquals(SleepCalculation.Error.INVALID_TIMES, calculation("24:00", "6:30"))
        assertEquals(SleepCalculation.Error.SAME_TIME, calculation("6:30", "06:30"))
        assertEquals(SleepCalculation.Error.INVALID_MINUTES, calculation("22:30", "6:30", settling = "-1"))
        assertEquals(SleepCalculation.Error.TOO_MANY_AWAKE_MINUTES, calculation("14:00", "14:35", awake = "36"))
        assertEquals(SleepCalculation.Error.FUTURE_WAKE, calculation("22:30", "23:45"))
    }

    @Test
    fun elapsedTimeAccountsForAClockChangeAndRejectsSkippedOrRepeatedTimes() {
        val zone = ZoneId.of("America/New_York")
        val spring = LocalDate.of(2026, 3, 8)
        val fall = LocalDate.of(2026, 11, 1)
        val springNight = calculation("23:00", "07:00", date = spring, zone = zone) as SleepCalculation.Valid
        val fallNight = calculation("23:00", "07:00", date = fall, zone = zone) as SleepCalculation.Valid
        assertEquals(420L, springNight.estimate.intervalMinutes)
        assertEquals(540L, fallNight.estimate.intervalMinutes)
        assertEquals(SleepCalculation.Error.CLOCK_CHANGE, calculation("23:00", "02:30", date = spring, zone = zone))
        assertEquals(SleepCalculation.Error.CLOCK_CHANGE, calculation("23:00", "01:30", date = fall, zone = zone))
    }

    private fun calculation(start: String, wake: String, settling: String = "", awake: String = "",
        date: LocalDate = LocalDate.of(2026, 11, 22), zone: ZoneId = kolkata
    ) = calculateSleep(start, wake, date, settling, awake, zone, now)
}
