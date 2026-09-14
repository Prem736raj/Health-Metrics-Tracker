package com.health.calculator.bmi.tracker.data.model

data class WeightGoalProgress(
    val currentWeight: Double,
    val goalWeight: Double,
    val startingWeight: Double,
    val totalToLoseOrGain: Double,
    val remainingToGoal: Double,
    val percentageComplete: Float,
    val isGainingGoal: Boolean,
    val estimatedDaysRemaining: Int?,
    val estimatedCompletionDate: Long?,
    val isGoalReached: Boolean,
    val averageWeeklyChange: Double?
) {
    /** Progress is presentation data; clamp malformed/restored values before drawing it. */
    val safePercentageComplete: Float
        get() = if (isGoalReached) {
            1f
        } else {
            percentageComplete.takeIf { it.isFinite() }?.coerceIn(0f, 1f) ?: 0f
        }

    val directionLabel: String
        get() = when {
            totalToLoseOrGain <= 0.0 -> "maintain"
            isGainingGoal -> "gain"
            else -> "lose"
        }
}
