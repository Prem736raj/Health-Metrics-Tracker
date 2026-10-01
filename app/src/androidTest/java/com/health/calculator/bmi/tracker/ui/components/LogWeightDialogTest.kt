package com.health.calculator.bmi.tracker.ui.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LogWeightDialogTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun decimalCommaCanBeSavedWithLargeTextInDarkTheme() {
        var saved = false
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                HealthCalculatorTheme(themeMode = ThemeMode.DARK) {
                    LogWeightDialog(
                        weightInput = "70,53", noteInput = "", dateMillis = System.currentTimeMillis(),
                        useMetric = true, isSaving = false, onWeightChange = {}, onNoteChange = {},
                        onDateClick = {}, onSave = { saved = true }, onDismiss = {}
                    )
                }
            }
        }
        composeRule.onNodeWithText("Save").assertIsDisplayed().assertIsEnabled().performClick()
        composeRule.runOnIdle { assertTrue(saved) }
    }
}
