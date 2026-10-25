package com.health.calculator.bmi.tracker.ui.screens.calculators.bmi.components

import org.junit.Assert.assertEquals
import org.junit.Test

class BMIInputMemoryDisplayTest {
    @Test fun relativeTimeDisplaysValuesRatherThanTemplates() {
        val now = 1800000000000L
        val minute = 60000L
        assertEquals("just now", getTimeAgoText(now, now))
        assertEquals("2m ago", getTimeAgoText(now - 2 * minute, now))
        assertEquals("2h ago", getTimeAgoText(now - 120 * minute, now))
        assertEquals("2d ago", getTimeAgoText(now - 2 * 1440 * minute, now))
        assertEquals("2w ago", getTimeAgoText(now - 14 * 1440 * minute, now))
        assertEquals("2mo ago", getTimeAgoText(now - 60 * 1440 * minute, now))
    }
    @Test fun missingOrFutureTimeDoesNotLeakInvalidValues() {
        assertEquals("", getTimeAgoText(0, 1000))
        assertEquals("just now", getTimeAgoText(2000, 1000))
    }
}
