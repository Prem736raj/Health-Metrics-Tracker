package com.health.calculator.bmi.tracker.core

import com.health.calculator.bmi.tracker.core.utils.DateUtils
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class DateUtilsTest {
    @Test fun formattingUsesCurrentLocaleAndTimezone() {
        val originalLocale = Locale.getDefault()
        val originalZone = TimeZone.getDefault()
        try {
            val timestamp = Instant.parse("2026-01-01T01:00:00Z").toEpochMilli()
            Locale.setDefault(Locale.US)
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
            assertEquals("January 1, 2026", DateUtils.formatFullDate(timestamp))
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"))
            assertEquals("December 31, 2025", DateUtils.formatFullDate(timestamp))
            Locale.setDefault(Locale.FRANCE)
            assertTrue(DateUtils.formatFullDate(timestamp).startsWith("décembre"))
        } finally {
            Locale.setDefault(originalLocale)
            TimeZone.setDefault(originalZone)
        }
    }

    @Test fun concurrentFormattingDoesNotShareMutableFormatterState() {
        val pool = Executors.newFixedThreadPool(4)
        try {
            val timestamps = (0L..199L).map { it * 86_400_000L }
            val expected = timestamps.map(DateUtils::formatDateTime)
            val actual = pool.invokeAll(timestamps.map { timestamp -> Callable { DateUtils.formatDateTime(timestamp) } })
                .map { it.get() }
            assertEquals(expected, actual)
        } finally {
            pool.shutdownNow()
        }
    }

    @Test fun futureDateIsNotDescribedAsJustNow() {
        val future = System.currentTimeMillis() + 86_400_000L
        assertEquals(DateUtils.formatShortDate(future), DateUtils.getRelativeTimeString(future))
    }
}
