package com.health.calculator.bmi.tracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.health.calculator.bmi.tracker.ui.screens.bsa.BSAMedicalApplicationsSection
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BsaEducationScopeTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun expandedClinicalContextContainsEducationWithoutPersonalizedDoses() {
        composeRule.setContent {
            HealthCalculatorTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) { BSAMedicalApplicationsSection() }
            }
        }
        composeRule.onNodeWithText("BSA in Clinical Context").performClick()
        composeRule.onNodeWithText("Medication protocols").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Kidney-function reports").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Your Dose").assertDoesNotExist()
        composeRule.onNodeWithText("Example Drug A").assertDoesNotExist()
    }
}
