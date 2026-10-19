package com.health.calculator.bmi.tracker.util

import com.health.calculator.bmi.tracker.ui.components.FitnessLevel

data class WorkoutRecommendation(
    val goalName: String,
    val goalDescription: String,
    val keyAdvice: String,
    val tips: List<String>
)

/** Planning considerations, without prescribed schedules or zone-derived calorie claims. */
object HeartRateZoneRecommendationEngine {
    fun generateRecommendations(fitnessLevel: FitnessLevel, zones: List<HeartRateZone>): List<WorkoutRecommendation> {
        val easyTalkTest = zones.firstOrNull()?.talkTest ?: "Comfortable conversation"
        val startingAdvice = if (fitnessLevel == FitnessLevel.BEGINNER) {
            "Start with manageable activity and build gradually."
        } else {
            "Review effort and recovery alongside your current routine."
        }
        return listOf(
            WorkoutRecommendation(
                goalName = "Comfortable activity",
                goalDescription = "Use estimates alongside how you feel.",
                keyAdvice = "$startingAdvice The estimated zones do not establish a safe exercise limit.",
                tips = listOf("Talk-test context: $easyTalkTest", "Medicines, heat, illness and measurement quality can change heart-rate response.", "A lower or higher reading is not automatically a better workout.")
            ),
            WorkoutRecommendation(
                goalName = "A sustainable routine",
                goalDescription = "Choose activity that fits your abilities, goals and recovery.",
                keyAdvice = "There is no universal duration, frequency or split between zones for every person. You do not need to reach every zone.",
                tips = listOf("Heart rate alone cannot determine fuel-burning percentages or calories used.", "No zone guarantees body-fat loss or a particular fitness outcome.", "A qualified professional can help adapt an exercise plan when needed.")
            ),
            WorkoutRecommendation(
                goalName = "Symptoms and context",
                goalDescription = "Pay attention to symptoms even when a reading is within an estimated zone.",
                keyAdvice = "Stop activity if you develop concerning symptoms. For a possible medical emergency, contact local emergency services immediately.",
                tips = listOf("Do not perform a maximum-effort test on your own to confirm an app estimate.", "Follow any individual activity limits from your care team.", "Discuss unexpected heart-rate patterns with a healthcare professional.")
            )
        )
    }
}
