package com.health.calculator.bmi.tracker.data.model

data class MacroResult(
    val totalCalories: Double,
    val proteinGrams: Double,
    val proteinCalories: Double,
    val proteinPercent: Float,
    val proteinPerKg: Double,
    val fatGrams: Double,
    val fatCalories: Double,
    val fatPercent: Float,
    val saturatedFatGrams: Double,
    val unsaturatedFatGrams: Double,
    val carbGrams: Double,
    val carbCalories: Double,
    val carbPercent: Float,
    val fiberRecommendation: Double,
    val dietPresetName: String,
    val numberOfMeals: Int = 3
) {
    private val safeMealCount: Int get() = numberOfMeals.coerceAtLeast(1)

    // Per meal calculations
    val proteinPerMeal: Double get() = proteinGrams / safeMealCount
    val fatPerMeal: Double get() = fatGrams / safeMealCount
    val carbPerMeal: Double get() = carbGrams / safeMealCount
    val caloriesPerMeal: Double get() = totalCalories / safeMealCount

    // Validation
    val isBalanced: Boolean get() = proteinPercent + fatPercent + carbPercent in 99f..101f
    val hasSufficientProtein: Boolean get() = proteinPerKg >= 0.8
    val hasSufficientFat: Boolean get() = fatPercent >= 20f
}

data class DietPreset(
    val id: String,
    val name: String,
    val description: String,
    val carbPercent: Int,
    val proteinPercent: Int,
    val fatPercent: Int,
    val emoji: String,
    val color: Long
)
