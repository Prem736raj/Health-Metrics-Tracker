// File: com/health/calculator/bmi/tracker/data/model/TEFData.kt
package com.health.calculator.bmi.tracker.data.model

data class TEFData(
    val bmr: Float = 0f,
    val activityCalories: Float = 0f,
    val tdee: Float = 0f,
    val proteinCalories: Float = 0f,
    val carbsCalories: Float = 0f,
    val fatCalories: Float = 0f,
    val proteinPercentage: Float = 30f,
    val carbsPercentage: Float = 40f,
    val fatPercentage: Float = 30f
) {
    // TEF per macro (using mid-range estimates)
    val proteinTEF: Float get() = proteinCalories * PROTEIN_TEF_MID
    val carbsTEF: Float get() = carbsCalories * CARBS_TEF_MID
    val fatTEF: Float get() = fatCalories * FAT_TEF_MID

    // TEF ranges per macro
    val proteinTEFLow: Float get() = proteinCalories * PROTEIN_TEF_LOW
    val proteinTEFHigh: Float get() = proteinCalories * PROTEIN_TEF_HIGH
    val carbsTEFLow: Float get() = carbsCalories * CARBS_TEF_LOW
    val carbsTEFHigh: Float get() = carbsCalories * CARBS_TEF_HIGH
    val fatTEFLow: Float get() = fatCalories * FAT_TEF_LOW
    val fatTEFHigh: Float get() = fatCalories * FAT_TEF_HIGH

    // Total personalized TEF
    val totalTEF: Float get() = proteinTEF + carbsTEF + fatTEF
    val totalTEFLow: Float get() = proteinTEFLow + carbsTEFLow + fatTEFLow
    val totalTEFHigh: Float get() = proteinTEFHigh + carbsTEFHigh + fatTEFHigh

    // Generic TEF estimate (10% of the activity-adjusted estimate).
    val genericTEF: Float get() = tdee * 0.10f

    /**
     * Activity multipliers already estimate total daily expenditure. TEF is
     * shown as an illustrative component and must not be added a second time.
     * This legacy property therefore remains an alias for the original TDEE.
     */
    val adjustedTDEE: Float get() = tdee

    /** A non-overlapping activity slice for the explanatory chart only. */
    val activityCaloriesForBreakdown: Float
        get() = (tdee - bmr - totalTEF).coerceAtLeast(0f)

    val bmrPercentOfTotal: Float get() = if (tdee > 0) (bmr / tdee) * 100f else 0f
    val activityPercentOfTotal: Float get() = if (tdee > 0) (activityCaloriesForBreakdown / tdee) * 100f else 0f
    val tefPercentOfTotal: Float get() = if (tdee > 0) (totalTEF / tdee) * 100f else 0f

    // TEF as a percentage of the macro-calorie intake used by this estimate.
    val macroIntakeCalories: Float
        get() = (proteinCalories + carbsCalories + fatCalories).coerceAtLeast(0f)
    val tefPercentOfIntake: Float
        get() = if (macroIntakeCalories > 0f) (totalTEF / macroIntakeCalories) * 100f else 0f

    fun getInterpretation(): String {
        return "This model estimates about ${totalTEF.toInt()} calories per day for the " +
                "Thermic Effect of Food (TEF), the energy used to digest and process food. " +
                "It is not a direct measurement, and the result varies with the macro mix " +
                "and the reference ranges used."
    }

    fun getPersonalizedInsight(): String {
        val proteinNote = if (proteinPercentage >= 35f) {
            "This estimate uses a higher protein share (${proteinPercentage.toInt()}%). " +
                    "Protein has a higher published thermic range than carbohydrate or fat, " +
                    "but this result does not predict weight change or prescribe an intake."
        } else if (proteinPercentage >= 25f) {
            "This estimate uses a moderate protein share (${proteinPercentage.toInt()}%). " +
                    "Macro-specific thermic ranges overlap and vary between people; use this " +
                    "as context rather than a target."
        } else {
            "This estimate uses a lower protein share (${proteinPercentage.toInt()}%). " +
                    "Do not change your diet solely to alter TEF; discuss personal nutrition " +
                    "goals with a qualified professional when needed."
        }
        return proteinNote
    }

    companion object {
        const val PROTEIN_TEF_LOW = 0.20f
        const val PROTEIN_TEF_MID = 0.275f // Average of 20-35%
        const val PROTEIN_TEF_HIGH = 0.35f

        const val CARBS_TEF_LOW = 0.05f
        const val CARBS_TEF_MID = 0.10f // Average of 5-15%
        const val CARBS_TEF_HIGH = 0.15f

        const val FAT_TEF_LOW = 0.00f
        const val FAT_TEF_MID = 0.025f // Average of 0-5%
        const val FAT_TEF_HIGH = 0.05f

        fun calculate(
            bmr: Float,
            activityCalories: Float,
            tdee: Float,
            proteinCalories: Float,
            carbsCalories: Float,
            fatCalories: Float,
            proteinPct: Float,
            carbsPct: Float,
            fatPct: Float
        ): TEFData {
            return TEFData(
                bmr = bmr,
                activityCalories = activityCalories,
                tdee = tdee,
                proteinCalories = proteinCalories,
                carbsCalories = carbsCalories,
                fatCalories = fatCalories,
                proteinPercentage = proteinPct,
                carbsPercentage = carbsPct,
                fatPercentage = fatPct
            )
        }
    }
}

data class EnergyComponent(
    val label: String,
    val emoji: String,
    val calories: Float,
    val percentage: Float,
    val description: String,
    val color: androidx.compose.ui.graphics.Color
)
