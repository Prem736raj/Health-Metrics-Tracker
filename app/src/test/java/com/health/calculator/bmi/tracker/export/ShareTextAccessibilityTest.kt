package com.health.calculator.bmi.tracker.export

import com.health.calculator.bmi.tracker.calculator.Ethnicity
import com.health.calculator.bmi.tracker.data.model.ActivityLevel
import com.health.calculator.bmi.tracker.data.model.BMRResultData
import com.health.calculator.bmi.tracker.data.model.BloodPressureReading
import com.health.calculator.bmi.tracker.data.model.BpCategory
import com.health.calculator.bmi.tracker.data.model.Gender
import com.health.calculator.bmi.tracker.data.model.MacroBreakdown
import com.health.calculator.bmi.tracker.data.model.WhrCalculator
import com.health.calculator.bmi.tracker.data.export.ExportDisclosurePolicy
import com.health.calculator.bmi.tracker.ui.screens.bloodpressure.buildBloodPressureShareText
import com.health.calculator.bmi.tracker.ui.screens.bmr.BMRShareFormatter
import com.health.calculator.bmi.tracker.ui.screens.whr.WhrShareUtils
import com.health.calculator.bmi.tracker.util.WaterShareHelper
import java.time.LocalDateTime
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareTextAccessibilityTest {

    @Test
    fun calculator_and_tracker_share_text_uses_plain_labels() {
        val bmrText = BMRShareFormatter.formatQuickResult(1_500f, 1_900f, "Mifflin-St Jeor", "Light")
        val whrText = WhrShareUtils.buildDetailedShareText(
            WhrCalculator.calculate(90f, 100f, Gender.MALE, 30, 180f, Ethnicity.SOUTH_ASIAN)
        )
        val bpText = buildBloodPressureShareText(
            BloodPressureReading(systolic = 120, diastolic = 78, pulse = 65, category = BpCategory.NORMAL)
        )
        val waterText = WaterShareHelper.getQuickShareText(1_800, 2_000, 2)

        listOf(bmrText, whrText, bpText, waterText).forEach(::assertContainsDisclosureAndNoEmoji)
        assertTrue(bmrText.contains("My BMR:"))
        assertTrue(whrText.contains("Waist-to-Hip Ratio"))
        assertTrue(bpText.contains("Blood Pressure Reading"))
        assertTrue(waterText.contains("Hydration progress:"))
    }

    @Test
    fun bmr_formatter_never_divides_by_zero_for_invalid_meal_count() {
        val text = BMRShareFormatter.formatCompleteResult(
            resultData = BMRResultData(primaryBMR = 1_500f),
            activityLevel = ActivityLevel.SEDENTARY,
            macroBreakdown = MacroBreakdown(totalCalories = 1_900f),
            tefData = null,
            mealCount = 0
        )

        assertFalse(text.contains("Infinity"))
        assertTrue(text.contains("Per Meal (1 meals/day)"))
    }

    private fun assertContainsDisclosureAndNoEmoji(text: String) {
        assertTrue(text.contains(ExportDisclosurePolicy.APP_NAME))
        assertTrue(text.contains(ExportDisclosurePolicy.DIAGNOSIS_NOTICE))
        assertFalse(text.any { it.code in 0x1F300..0x1FAFF })
    }
}
