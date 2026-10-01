package com.health.calculator.bmi.tracker.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class HealthCategoryPresentationTest {
    @Test fun specificCategoriesWinOverSubstrings() {
        assertEquals(HealthCategoryColor.MODERATE, HealthCategoryPresentation.colorFor("High normal"))
        assertEquals(HealthCategoryColor.MODERATE, HealthCategoryPresentation.colorFor("High-normal"))
        assertEquals(HealthCategoryColor.DANGER, HealthCategoryPresentation.colorFor("Hypertension Stage 2"))
        assertEquals(HealthCategoryColor.WARNING, HealthCategoryPresentation.colorFor("Hypertension Stage 1"))
    }

    @Test fun unknownLabelsAreNotPresentedAsNormal() {
        assertEquals(HealthCategoryColor.NEUTRAL, HealthCategoryPresentation.colorFor("Abnormal"))
        assertEquals(HealthCategoryColor.NEUTRAL, HealthCategoryPresentation.colorFor("Unhealthy"))
        assertEquals(HealthCategoryColor.NEUTRAL, HealthCategoryPresentation.colorFor(null))
        assertEquals(HealthCategoryColor.EXCELLENT, HealthCategoryPresentation.colorFor("Normal weight"))
    }
}
