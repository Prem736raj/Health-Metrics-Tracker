package com.health.calculator.bmi.tracker.ui.screens.history

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryDisplayPolicyTest {
    @Test fun calendarsIncludeEveryDayAndAlignWeekdays() {
        for (year in 2024..2027) for (month in 1..12) {
            val first = LocalDate.of(year, month, 1)
            val weeks = calendarWeekCells(first)
            assertTrue(weeks.all { it.size == 7 })
            assertEquals((1..first.lengthOfMonth()).toList(), weeks.flatten().filterNotNull())
            assertEquals(first.dayOfWeek.value - 1, weeks.flatten().indexOf(1))
        }
    }
    @Test fun octoberTwentySixIsVisibleInCalendar() {
        val weeks = calendarWeekCells(LocalDate.of(2026, 10, 1))
        assertEquals(26, weeks[4][0])
        assertEquals(31, weeks.flatten().filterNotNull().size)
    }
    @Test fun numericArrowsFollowSignIncludingSevenPointTwoPercentIncrease() {
        assertEquals(1, numericChangeDirection(7.2f))
        assertEquals(-1, numericChangeDirection(-7.2f))
        assertEquals(0, numericChangeDirection(0f))
        assertEquals(0, numericChangeDirection(Float.NaN))
    }
    @Test fun dayCountUsesSingularOnlyForOne() {
        assertEquals("1 Day", streakDaysLabel(1))
        assertEquals("0 Days", streakDaysLabel(0))
        assertEquals("2 Days", streakDaysLabel(2))
    }
}
