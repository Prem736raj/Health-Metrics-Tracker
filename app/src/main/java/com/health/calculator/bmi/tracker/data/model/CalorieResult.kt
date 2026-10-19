package com.health.calculator.bmi.tracker.data.model

data class CalorieResult(
    val bmrMifflin: Double,
    val bmrKatchMcArdle: Double?,
    val usedBmr: Double,
    val bmrFormulaUsed: String,
    val activityMultiplier: Double,
    val activityLevelName: String,
    val tdee: Double,
    val tef: Double,
    val goalCalories: Double,
    val goalName: String,
    val goalAdjustment: Int,
    val weeklyChangeKg: Double,
    val weightKg: Double,
    val heightCm: Double,
    val age: Int,
    val gender: String,
    val bodyFatPercent: Double?,
    val isBelowMinimum: Boolean,
    val minimumCalories: Double,
    val safeGoalCalories: Double,
    /** Activity multipliers already approximate total daily expenditure, so TEF is informational and not added twice. */
    val tefIncludedInTdee: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) {
    val bmrCalories: Double get() = usedBmr
    val activityCalories: Double get() = (tdee - usedBmr).coerceAtLeast(0.0)
    val bmrPercent: Float get() = ((usedBmr / tdee) * 100).toFloat()
    val activityPercent: Float get() = ((activityCalories / tdee) * 100).toFloat()
    val tefPercent: Float get() = ((tef / tdee) * 100).toFloat()

    /** Derived from the displayed intake after the app planning minimum is applied. */
    val effectiveGoalAdjustment: Double get() = safeGoalCalories - tdee
    val dailyDeficitOrSurplus: Int get() = kotlin.math.round(effectiveGoalAdjustment).toInt()
    val isDeficit: Boolean get() = effectiveGoalAdjustment < -0.5
    val isSurplus: Boolean get() = effectiveGoalAdjustment > 0.5
    val isMaintenance: Boolean get() = !isDeficit && !isSurplus

    // A fixed energy-balance illustration, not a physiological prediction.
    val weeklyChangeDisplay: Double get() = kotlin.math.abs(effectiveGoalAdjustment * 7.0 / 7700.0)
    val monthlyChangeKg: Double get() = weeklyChangeDisplay * 4.33

    val caloriesPerHour: Double get() = safeGoalCalories / 24.0
    val caloriesPerMeal3: Double get() = safeGoalCalories / 3.0
    val caloriesPerMeal4: Double get() = safeGoalCalories / 4.0
    val caloriesPerMeal5: Double get() = safeGoalCalories / 5.0
}
