// File: com/health/calculator/bmi/tracker/data/model/BMRTrendData.kt
package com.health.calculator.bmi.tracker.data.model

data class BMRHistoryPoint(
    val bmr: Float,
    val tdee: Float,
    val weightKg: Float,
    val formulaName: String,
    val activityLevel: String,
    val timestamp: Long,
    val dateLabel: String
)

data class BMRTrendStats(
    val currentBMR: Float = 0f,
    val averageBMR: Float = 0f,
    val highestBMR: Float = 0f,
    val lowestBMR: Float = 0f,
    val firstBMR: Float = 0f,
    val changeFromFirst: Float = 0f,
    val changePercentFromFirst: Float = 0f,
    val currentTDEE: Float = 0f,
    val averageTDEE: Float = 0f,
    val totalReadings: Int = 0,
    val previousBMR: Float = 0f,
    val changeFromPrevious: Float = 0f,
    val previousWeight: Float = 0f,
    val currentWeight: Float = 0f,
    val weightChange: Float = 0f
) {
    val hasMultipleReadings: Boolean get() = totalReadings > 1
    val hasPreviousReading: Boolean get() = totalReadings > 1
    val bmrIncreased: Boolean get() = changeFromPrevious > 0
    val bmrDecreased: Boolean get() = changeFromPrevious < 0

    fun getChangeInsight(): BMRChangeInsight {
        if (!hasMultipleReadings) return BMRChangeInsight.NO_DATA

        if (previousBMR <= 0f) return BMRChangeInsight.STABLE

        val absPercent = kotlin.math.abs(changeFromPrevious / previousBMR * 100f)

        return when {
            absPercent < 1f -> BMRChangeInsight.STABLE
            changeFromPrevious > 0 && weightChange > 0 -> BMRChangeInsight.INCREASED_WEIGHT_GAIN
            changeFromPrevious > 0 && weightChange <= 0 -> BMRChangeInsight.INCREASED_MUSCLE_GAIN
            changeFromPrevious < 0 && weightChange < 0 -> BMRChangeInsight.DECREASED_WEIGHT_LOSS
            changeFromPrevious < 0 && weightChange >= 0 -> BMRChangeInsight.DECREASED_CONCERNING
            else -> BMRChangeInsight.STABLE
        }
    }
}

enum class BMRChangeInsight(
    val emoji: String,
    val title: String,
    val message: String,
    val isPositive: Boolean
) {
    NO_DATA(
        emoji = "📊",
        title = "Start Tracking",
        message = "Track your BMR regularly to see trends and insights. We recommend checking monthly or whenever your weight changes significantly.",
        isPositive = true
    ),
    STABLE(
        emoji = "✅",
        title = "BMR Stable",
        message = "Your saved BMR estimates are similar across readings. This pattern does not measure body composition or metabolic health.",
        isPositive = true
    ),
    INCREASED_WEIGHT_GAIN(
        emoji = "📈",
        title = "BMR Increased",
        message = "Your BMR estimate increased alongside a higher recorded weight. Changes can reflect the inputs, equation variation or physiology; this is not a muscle measurement.",
        isPositive = true
    ),
    INCREASED_MUSCLE_GAIN(
        emoji = "💪",
        title = "BMR Estimate Increased",
        message = "Your BMR estimate increased while recorded weight was stable or lower. Do not infer muscle gain from this estimate alone.",
        isPositive = true
    ),
    DECREASED_WEIGHT_LOSS(
        emoji = "📉",
        title = "BMR Estimate Decreased",
        message = "Your BMR estimate decreased alongside lower recorded weight. This direction can occur as inputs change, but it is not a personal prediction of future weight change.",
        isPositive = false
    ),
    DECREASED_CONCERNING(
        emoji = "⚠️",
        title = "Review This Estimate",
        message = "Your BMR estimate decreased without recorded weight loss. Check the entries and equation used; discuss a persistent unexpected change with a qualified professional.",
        isPositive = false
    )
}
