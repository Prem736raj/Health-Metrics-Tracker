package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.data.model.*
import org.junit.Assert.*
import org.junit.Test

class BloodPressureGuidanceTest {
    @Test fun isolatedSevereValuesGetImmediateSymptomGuidance() {
        for ((systolic, diastolic) in listOf(180 to 75, 110 to 120, 185 to 125)) {
            val category = BloodPressureCalculator.categorize(systolic, diastolic)
            assertEquals(BloodPressureReference.SEVERE_READING_GUIDANCE, category.description)
            val guidance = BpRecommendationsProvider.getGuidance(category)
            assertEquals(BloodPressureReference.SEVERE_READING_GUIDANCE, guidance.doctorAdvice)
            assertTrue(guidance.doctorAdvice!!.contains("OR"))
            assertTrue(guidance.doctorAdvice!!.contains("Do not wait for another reading"))
            assertTrue(guidance.doctorAdvice!!.contains("Without these symptoms"))
        }
    }

    @Test fun belowSevereBoundaryDoesNotShowTheSevereAlert() {
        val guidance = BpRecommendationsProvider.getGuidance(BloodPressureCalculator.categorize(179, 119))
        assertNull(guidance.urgencyNote)
        assertNotEquals(BloodPressureReference.SEVERE_READING_GUIDANCE, guidance.doctorAdvice)
    }
}
