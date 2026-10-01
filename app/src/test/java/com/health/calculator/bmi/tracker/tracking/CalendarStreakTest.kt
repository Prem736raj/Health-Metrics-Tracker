package com.health.calculator.bmi.tracker.tracking

import com.health.calculator.bmi.tracker.core.utils.CalendarStreak
import java.time.LocalDate
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarStreakTest {
    @Test fun countsAcrossBothDaylightSavingTransitions() {
        val zone = ZoneId.of("America/New_York")
        listOf(LocalDate.of(2026, 3, 9), LocalDate.of(2026, 11, 2)).forEach { today ->
            val logs = (0L..3L).map { today.minusDays(it).atStartOfDay(zone).toInstant().toEpochMilli() }
            assertEquals(4, CalendarStreak.count(logs, today, zone))
        }
    }

    @Test fun duplicateAndFutureLogsDoNotInflateStreak() {
        val zone = ZoneId.of("Asia/Kolkata")
        val today = LocalDate.of(2024, 3, 1)
        val logs = listOf(today, today, today.minusDays(1), today.minusDays(2), today.plusDays(1))
            .map { it.atStartOfDay(zone).toInstant().toEpochMilli() }
        assertEquals(3, CalendarStreak.count(logs, today, zone))
    }

    @Test fun yesterdayCanContinueButMissingYesterdayBreaksStreak() {
        val zone = ZoneId.of("UTC")
        val today = LocalDate.of(2026, 9, 19)
        val yesterday = today.minusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        assertEquals(1, CalendarStreak.count(listOf(yesterday), today, zone))
        assertEquals(0, CalendarStreak.count(listOf(yesterday - 86400000), today, zone))
        assertEquals(0, CalendarStreak.count(emptyList(), today, zone))
    }
}
