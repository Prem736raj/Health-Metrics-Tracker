// File: com/health/calculator/bmi/tracker/data/model/BMRResultData.kt
package com.health.calculator.bmi.tracker.data.model

import com.health.calculator.bmi.tracker.data.export.ExportDisclosurePolicy

data class BMRResultData(
    val primaryBMR: Float = 0f,
    val selectedFormula: BMRFormula = BMRFormula.MIFFLIN_ST_JEOR,
    val allFormulaResults: Map<BMRFormula, Float> = emptyMap(),
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val age: Int = 0,
    val isMale: Boolean = true,
    val bodyFatPercentage: Float = 0f,
    val isUnitKg: Boolean = true,
    val isUnitCm: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) {
    val bmrPerHour: Float get() = primaryBMR / 24f
    val bmrInKJ: Float get() = primaryBMR * 4.184f
    val bmrPerHourKJ: Float get() = bmrInKJ / 24f

    val lowestBMR: Float get() = allFormulaResults.values.minOrNull() ?: primaryBMR
    val highestBMR: Float get() = allFormulaResults.values.maxOrNull() ?: primaryBMR
    val averageBMR: Float get() = if (allFormulaResults.isNotEmpty())
        allFormulaResults.values.average().toFloat() else primaryBMR
    val bmrRange: Float get() = highestBMR - lowestBMR

    fun getInterpretation(): String {
        return "The ${selectedFormula.displayName} equation estimates resting energy use " +
                "at approximately ${primaryBMR.toInt()} kcal/day for the inputs you entered. " +
                "Actual energy needs vary with activity, health, body composition and " +
                "measurement conditions; this is not a minimum intake or a prescription."
    }

    fun getBMRLevel(): BMRLevel {
        // General reference ranges (vary by age/gender but give rough guidance)
        return if (isMale) {
            when {
                primaryBMR < 1200 -> BMRLevel.LOW
                primaryBMR < 1600 -> BMRLevel.BELOW_AVERAGE
                primaryBMR < 2000 -> BMRLevel.AVERAGE
                primaryBMR < 2400 -> BMRLevel.ABOVE_AVERAGE
                else -> BMRLevel.HIGH
            }
        } else {
            when {
                primaryBMR < 1000 -> BMRLevel.LOW
                primaryBMR < 1300 -> BMRLevel.BELOW_AVERAGE
                primaryBMR < 1600 -> BMRLevel.AVERAGE
                primaryBMR < 1900 -> BMRLevel.ABOVE_AVERAGE
                else -> BMRLevel.HIGH
            }
        }
    }

    fun toShareText(): String {
        return buildString {
            append("My Basal Metabolic Rate (BMR)\n")
            append("━━━━━━━━━━━━━━━━━━━━━━━━\n")
            append("BMR: ${primaryBMR.toInt()} kcal/day")
            append(" (${bmrInKJ.toInt()} kJ/day)\n")
            append("Formula: ${selectedFormula.displayName}\n")
            append("BMR per hour: ${String.format("%.1f", bmrPerHour)} kcal\n\n")
            append("All formula comparison:\n")
            allFormulaResults.forEach { (formula, value) ->
                val marker = if (formula == selectedFormula) " (selected)" else ""
                append("  • ${formula.displayName}: ${value.toInt()} kcal$marker\n")
            }
            append("\n")
            append(ExportDisclosurePolicy.shareFooter())
        }
    }

    fun toHistoryJson(): String {
        return buildString {
            append("{")
            append("\"bmr\":${primaryBMR},")
            append("\"formula\":\"${selectedFormula.name}\",")
            append("\"weightKg\":${weightKg},")
            append("\"heightCm\":${heightCm},")
            append("\"age\":${age},")
            append("\"gender\":\"${if (isMale) "Male" else "Female"}\",")
            append("\"bodyFat\":${bodyFatPercentage},")
            append("\"bmrKJ\":${bmrInKJ},")
            append("\"bmrPerHour\":${bmrPerHour}")
            append("}")
        }
    }
}

enum class BMRLevel(
    val label: String,
    val emoji: String,
    val description: String
) {
    LOW(
        "Lower estimate band",
        "🔵",
        "This estimate falls in a lower broad reference band used for context by the app. It does not indicate a health problem or a slow metabolism by itself."
    ),
    BELOW_AVERAGE(
        "Lower-middle estimate band",
        "🟢",
        "This estimate falls below the middle of the broad reference bands used for context. Individual variation is expected."
    ),
    AVERAGE(
        "Middle estimate band",
        "🟢",
        "This estimate falls near the middle of the broad reference bands used for context, not a clinical normal range."
    ),
    ABOVE_AVERAGE(
        "Upper-middle estimate band",
        "🟡",
        "This estimate falls above the middle of the broad reference bands used for context. It does not measure muscle mass or metabolism directly."
    ),
    HIGH(
        "Higher estimate band",
        "🟠",
        "This estimate falls in a higher broad reference band used for context. It is not a diagnosis and should not be used to set a target on its own."
    )
}
