package com.health.calculator.bmi.tracker.data.management

import com.health.calculator.bmi.tracker.data.model.CalculatorType
import com.health.calculator.bmi.tracker.data.model.CategoryColor
import com.health.calculator.bmi.tracker.data.model.HistoryDisplayEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DataIntegrityCheckerTest {
    private val checker = DataIntegrityChecker.getInstance()
    private val first = HistoryDisplayEntry(1, CalculatorType.BMI, "23.4", "kg/m²",
        "Reference", CategoryColor.GREEN, 10000, mapOf("Weight" to "70 kg"), "Morning")

    @Test fun equalResultsWithDifferentInputsAreNotDuplicates() {
        assertTrue(checker.findDuplicateIds(listOf(first,
            first.copy(id = 2, timestamp = 10500, details = mapOf("Weight" to "80 kg")))).isEmpty())
    }

    @Test fun notesAndUnitsArePreservedAsDistinctMeasurements() {
        assertTrue(checker.findDuplicateIds(listOf(first,
            first.copy(id = 2, timestamp = 10500, note = "Evening"))).isEmpty())
        assertTrue(checker.findDuplicateIds(listOf(first,
            first.copy(id = 2, timestamp = 10500, primaryLabel = "other"))).isEmpty())
    }

    @Test fun identicalContentIsOnlyFlaggedForReview() {
        assertEquals(listOf(2L), checker.findDuplicateIds(listOf(first,
            first.copy(id = 2, timestamp = 10500))))
        assertTrue(checker.checkIntegrity(listOf(first, first.copy(id = 2, timestamp = 10500)))
            .statusMessage.contains("No records were changed"))
    }
}
