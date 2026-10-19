package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.ui.components.HeartRateFormula
import com.health.calculator.bmi.tracker.util.HeartRateZoneCalculator
import com.health.calculator.bmi.tracker.util.HeartRateZoneRecommendationEngine
import com.health.calculator.bmi.tracker.ui.components.FitnessLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HeartRateGuidanceSafetyTest {

    @Test fun planningGuidanceDoesNotPromiseFatLossOrPrescribeAWorkoutSchedule() {
        val zones = HeartRateZoneCalculator.calculateZones(age = 35, formula = HeartRateFormula.TANAKA).zones
        for (level in FitnessLevel.entries) {
            val advice = HeartRateZoneRecommendationEngine.generateRecommendations(level, zones)
            assertTrue(advice.isNotEmpty())
            val text = advice.joinToString(" ") { it.goalDescription + it.keyAdvice + it.tips.joinToString(" ") }
            assertFalse(text.contains("times/week"))
            assertFalse(text.contains("fasted workouts"))
            assertFalse(text.contains("optimal fat burning"))
            assertTrue(text.contains("cannot determine"))
            assertTrue(text.contains("not clearance") || text.contains("do not establish"))
        }
    }

    @Test
    fun zonesExposeTalkTestGuidanceWithoutFixedDurationPrescription() {
        val result = HeartRateZoneCalculator.calculateZones(
            age = 35,
            formula = HeartRateFormula.TANAKA
        )

        assertEquals(5, result.zones.size)
        assertTrue(result.zones.all { it.talkTest.isNotBlank() })
        assertTrue(result.zones.all { it.percentLow < it.percentHigh })
        assertTrue(result.zones.all { it.bpmLow <= it.bpmHigh })
        assertFalse(result.zones.any { zone ->
            listOf("minutes", "minute", "hours", "hour").any { unit ->
                zone.purpose.contains(unit, ignoreCase = true) ||
                    zone.effortDescription.contains(unit, ignoreCase = true)
            }
        })
    }
}
