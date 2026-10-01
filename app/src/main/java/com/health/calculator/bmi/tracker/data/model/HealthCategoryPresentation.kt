package com.health.calculator.bmi.tracker.data.model

import java.util.Locale

/** Presentation of existing category labels, not a medical classification rule. */
internal object HealthCategoryPresentation {
    private val normalLabel = Regex("\\b(normal|optimal|healthy)\\b")

    fun colorFor(category: String?): HealthCategoryColor {
        val label = category?.lowercase(Locale.ROOT) ?: return HealthCategoryColor.NEUTRAL
        // Specific labels must precede broader matches: stage 2 hypertension is
        // not stage 1, and high normal is not normal.
        return when {
            listOf("emergency", "crisis", "stage 2", "markedly").any(label::contains) -> HealthCategoryColor.DANGER
            listOf("obese", "hypertension", "high risk", "stage 1", "several indicators").any(label::contains) -> HealthCategoryColor.WARNING
            listOf("overweight", "high normal", "high-normal", "moderate", "above action").any(label::contains) -> HealthCategoryColor.MODERATE
            normalLabel.containsMatchIn(label) || label.contains("below action") -> HealthCategoryColor.EXCELLENT
            else -> HealthCategoryColor.NEUTRAL
        }
    }
}
