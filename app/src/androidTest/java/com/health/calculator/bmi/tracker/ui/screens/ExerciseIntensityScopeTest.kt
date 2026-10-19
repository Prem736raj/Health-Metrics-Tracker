package com.health.calculator.bmi.tracker.ui.screens

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.health.calculator.bmi.tracker.ui.components.FitnessLevel
import com.health.calculator.bmi.tracker.ui.components.HeartRateFormula
import com.health.calculator.bmi.tracker.ui.screens.heartrate.ExerciseLogDialog
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import com.health.calculator.bmi.tracker.util.HeartRateZoneCalculator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseIntensityScopeTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun selectedZoneDoesNotAutomaticallyEstablishExerciseIntensity() {
        val result = HeartRateZoneCalculator.calculateZones(
            age = 30, formula = HeartRateFormula.TANAKA, restingHR = 60,
            gender = "Male", fitnessLevel = FitnessLevel.BEGINNER
        )
        composeRule.setContent {
            HealthCalculatorTheme {
                ExerciseLogDialog(result.zones, onDismiss = {}, onLog = {})
            }
        }
        composeRule.onNodeWithText("Log Session").assertIsNotEnabled()
        composeRule.onNodeWithText("Moderate", substring = false).performScrollTo().performClick()
        composeRule.onNodeWithText("Log Session").assertIsEnabled()
    }
}
