package com.health.calculator.bmi.tracker.ui.screens.calculators.bmi

import androidx.compose.ui.graphics.Color
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/**
 * Semantic colors used by BMI presentation surfaces.
 *
 * The calculator models retain their legacy colorHex values for persistence
 * and compatibility, but UI code should use these roles so the palette stays
 * consistent with the rest of the wellness app.
 */
internal fun bmiCategoryUiColor(category: BmiCategory): Color = when (category) {
    BmiCategory.SEVERE_THINNESS,
    BmiCategory.OBESE_CLASS_III -> HealthColors.Severe
    BmiCategory.MODERATE_THINNESS,
    BmiCategory.OBESE_CLASS_II -> HealthColors.Danger
    BmiCategory.MILD_THINNESS,
    BmiCategory.OVERWEIGHT -> HealthColors.Warning
    BmiCategory.NORMAL -> HealthColors.Healthy
    BmiCategory.OBESE_CLASS_I -> HealthColors.Caution
}

internal fun bmiValueUiColor(bmi: Float): Color = when {
    !bmi.isFinite() || bmi <= 0f -> HealthColors.Info
    else -> bmiCategoryUiColor(BmiCategory.fromBmi(bmi.toDouble()))
}

internal fun bmiPrimeUiColor(status: BmiPrimeStatus): Color = when (status) {
    BmiPrimeStatus.UNDER -> HealthColors.Healthy
    BmiPrimeStatus.AT_LIMIT -> HealthColors.Warning
    BmiPrimeStatus.OVER -> HealthColors.Danger
}

internal fun ponderalIndexUiColor(status: PonderalStatus): Color = when (status) {
    PonderalStatus.LOW -> HealthColors.Warning
    PonderalStatus.NORMAL -> HealthColors.Healthy
    PonderalStatus.HIGH -> HealthColors.Danger
}

internal fun asianBmiUiColor(category: AsianBmiCategory): Color = when (category) {
    AsianBmiCategory.UNDERWEIGHT -> HealthColors.Caution
    AsianBmiCategory.NORMAL -> HealthColors.Healthy
    AsianBmiCategory.OVERWEIGHT -> HealthColors.Warning
    AsianBmiCategory.OBESE_CLASS_I -> HealthColors.Caution
    AsianBmiCategory.OBESE_CLASS_II -> HealthColors.Danger
}
