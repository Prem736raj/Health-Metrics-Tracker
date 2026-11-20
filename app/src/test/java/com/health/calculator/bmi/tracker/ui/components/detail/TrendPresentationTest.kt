package com.health.calculator.bmi.tracker.ui.components.detail

import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class TrendPresentationTest {
    private val utc = ZoneId.of("UTC")
    private fun labels(vararg dates: String) = trendAxisLabels(dates.map { Instant.parse(it).toEpochMilli() }, utc, Locale.US)

    @Test fun sameDayUsesTimesAndSameMinuteUsesSeconds() {
        assertEquals(listOf("09:10", "15:20"), labels("2026-10-26T09:10:00Z", "2026-10-26T15:20:00Z"))
        assertEquals(listOf("09:10:01", "09:10:30"), labels("2026-10-26T09:10:01Z", "2026-10-26T09:10:30Z"))
    }
    @Test fun mixedDatesKeepDateContextAndYears() {
        assertEquals(listOf("10/26 09:10", "10/26 15:20", "10/27 08:00"),
            labels("2026-10-26T09:10:00Z", "2026-10-26T15:20:00Z", "2026-10-27T08:00:00Z"))
        assertEquals(listOf("10/26/25", "10/26/26"), labels("2025-10-26T09:00:00Z", "2026-10-26T09:00:00Z"))
    }
    @Test fun dateGroupingUsesLocalCalendarAndHandlesEmpty() {
        val times = listOf("2026-10-26T23:00:00Z", "2026-10-27T01:00:00Z").map { Instant.parse(it).toEpochMilli() }
        assertEquals(listOf("04:30", "06:30"), trendAxisLabels(times, ZoneId.of("Asia/Kolkata"), Locale.US))
        assertTrue(trendAxisLabels(emptyList()).isEmpty())
    }
    @Test fun ticksIncludeEndpointsForSparseAndLongSeries() {
        assertEquals(emptyList<Int>(), trendTickIndices(0))
        assertEquals(listOf(0), trendTickIndices(1))
        assertEquals(listOf(0, 1), trendTickIndices(2))
        assertEquals(listOf(0, 14, 29), trendTickIndices(30))
        assertEquals(listOf(0, 29), trendTickIndices(30, 1))
    }
}
