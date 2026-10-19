package com.health.calculator.bmi.tracker.insights

import com.health.calculator.bmi.tracker.util.SmartRecommendationEngine
import com.health.calculator.bmi.tracker.util.UserHealthContext
import org.junit.Assert.*
import org.junit.Test

class RecommendationConsistencyTest {
    @Test fun aNearGoalEntryDoesNotProduceContradictoryGoalPrompts() {
        val results = SmartRecommendationEngine.generateRecommendations(UserHealthContext(currentWeight = 70.3f, goalWeight = 70f))
        assertTrue(results.any { it.id == "goal_achieved" })
        assertFalse(results.any { it.id == "goal_close" })
        assertEquals("weight_tracking", results.single { it.id == "goal_achieved" }.actionRoute)
    }

    @Test fun waterProgressIsARecordingPromptNotAnInstructionToDrink() {
        val results = SmartRecommendationEngine.generateRecommendations(UserHealthContext(waterLoggedToday = true, waterProgress = 0.8f))
        assertTrue(results.single { it.id == "water_almost" }.message.contains("do not need to drink more"))
    }
}
