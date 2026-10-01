package com.health.calculator.bmi.tracker.ui.screens.calorie

internal object FoodInputPolicy {
    fun parseMacro(input: String): Double? = if (input.isBlank()) 0.0 else
        input.trim().toDoubleOrNull()?.takeIf { it.isFinite() && it >= 0.0 }
}
