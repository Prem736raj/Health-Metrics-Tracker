package com.health.calculator.bmi.tracker.tracking

import com.health.calculator.bmi.tracker.data.repository.FoodLogDayPolicy
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class FoodLogDayPolicyTest {
    @Test
    fun onlyAChangedCalendarDateResetsTheDailyLog() {
        assertFalse(FoodLogDayPolicy.needsReset("2026-09-08", "2026-09-08"))
        assertTrue(FoodLogDayPolicy.needsReset("2026-09-07", "2026-09-08"))
        assertTrue(FoodLogDayPolicy.needsReset("", "2026-09-08"))
    }

    @Test
    fun nextMidnightDelayUsesTheLocalCalendarBoundary() {
        val now = ZonedDateTime.of(
            2026, 3, 8, 0, 0, 0, 0,
            ZoneId.of("America/New_York")
        )

        // The spring-forward day is 23 elapsed hours, not a fixed 24-hour
        // interval. The repository must follow the local wall-clock date.
        assertEquals(
            23L * 60L * 60L * 1_000L,
            FoodLogDayPolicy.nextMidnightDelayMillis(now)
        )
    }
}
