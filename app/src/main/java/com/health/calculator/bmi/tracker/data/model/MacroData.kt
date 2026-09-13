// File: com/health/calculator/bmi/tracker/data/model/MacroData.kt
package com.health.calculator.bmi.tracker.data.model

import androidx.compose.ui.graphics.Color

data class MacroBreakdown(
    val totalCalories: Float = 0f,
    val proteinPercentage: Float = 30f,
    val carbsPercentage: Float = 40f,
    val fatPercentage: Float = 30f,
    val mealsPerDay: Int = 3,
    val dietApproach: DietApproach = DietApproach.BALANCED
) {
    // Calories per macro
    val proteinCalories: Float get() = totalCalories * proteinPercentage / 100f
    val carbsCalories: Float get() = totalCalories * carbsPercentage / 100f
    val fatCalories: Float get() = totalCalories * fatPercentage / 100f

    // Grams per macro (protein=4cal/g, carbs=4cal/g, fat=9cal/g)
    val proteinGrams: Float get() = proteinCalories / 4f
    val carbsGrams: Float get() = carbsCalories / 4f
    val fatGrams: Float get() = fatCalories / 9f

    // Total grams
    val totalGrams: Float get() = proteinGrams + carbsGrams + fatGrams

    // Per meal
    val proteinPerMeal: Float get() = proteinGrams / mealsPerDay
    val carbsPerMeal: Float get() = carbsGrams / mealsPerDay
    val fatPerMeal: Float get() = fatGrams / mealsPerDay
    val caloriesPerMeal: Float get() = totalCalories / mealsPerDay

    fun toShareText(): String {
        return buildString {
            append("Macronutrient Breakdown\n")
            append("━━━━━━━━━━━━━━━━━━━━\n")
            append("Diet: ${dietApproach.displayName}\n")
            append("Total: ${totalCalories.toInt()} kcal/day\n\n")
            append("Protein: ${proteinGrams.toInt()}g (${proteinCalories.toInt()} kcal) — ${proteinPercentage.toInt()}%\n")
            append("Carbs: ${carbsGrams.toInt()}g (${carbsCalories.toInt()} kcal) — ${carbsPercentage.toInt()}%\n")
            append("Fat: ${fatGrams.toInt()}g (${fatCalories.toInt()} kcal) — ${fatPercentage.toInt()}%\n\n")
            append("Per Meal ($mealsPerDay meals/day):\n")
            append("  Protein: ${proteinPerMeal.toInt()}g | Carbs: ${carbsPerMeal.toInt()}g | Fat: ${fatPerMeal.toInt()}g\n")
            append("  Calories: ${caloriesPerMeal.toInt()} kcal per meal")
        }
    }
}

enum class DietApproach(
    val displayName: String,
    val emoji: String,
    val carbsPercent: Float,
    val proteinPercent: Float,
    val fatPercent: Float,
    val description: String,
    val bestFor: String
) {
    BALANCED(
        displayName = "Balanced",
        emoji = "⚖️",
        carbsPercent = 40f,
        proteinPercent = 30f,
        fatPercent = 30f,
        description = "A general-purpose macro split for meal-planning context",
        bestFor = "Everyday planning"
    ),
    LOW_CARB(
        displayName = "Low Carb",
        emoji = "🥩",
        carbsPercent = 20f,
        proteinPercent = 40f,
        fatPercent = 40f,
        description = "A lower-carbohydrate pattern with higher protein and fat",
        bestFor = "Personal preference or planning"
    ),
    HIGH_CARB(
        displayName = "High Carb",
        emoji = "🍚",
        carbsPercent = 55f,
        proteinPercent = 25f,
        fatPercent = 20f,
        description = "A carbohydrate-focused pattern for higher activity demands",
        bestFor = "Activity-focused planning"
    ),
    KETOGENIC(
        displayName = "Ketogenic",
        emoji = "🥑",
        carbsPercent = 5f,
        proteinPercent = 25f,
        fatPercent = 70f,
        description = "A very-low-carbohydrate, higher-fat pattern; suitability varies",
        bestFor = "Specific goals with professional context"
    ),
    HIGH_PROTEIN(
        displayName = "High Protein",
        emoji = "💪",
        carbsPercent = 30f,
        proteinPercent = 40f,
        fatPercent = 30f,
        description = "A higher-protein pattern for users who prefer that split",
        bestFor = "Protein-focused planning"
    ),
    CUSTOM(
        displayName = "Custom",
        emoji = "🎛️",
        carbsPercent = 40f,
        proteinPercent = 30f,
        fatPercent = 30f,
        description = "Set your own macro ratios",
        bestFor = "Personalized targets"
    )
}

// Color constants for macros
object MacroColors {
    val Protein = Color(0xFF2196F3) // Blue
    val Carbs = Color(0xFFFFC107)   // Gold/Yellow
    val Fat = Color(0xFFFF7043)     // Orange/Red

    val ProteinLight = Color(0xFFBBDEFB)
    val CarbsLight = Color(0xFFFFF9C4)
    val FatLight = Color(0xFFFFCCBC)
}
