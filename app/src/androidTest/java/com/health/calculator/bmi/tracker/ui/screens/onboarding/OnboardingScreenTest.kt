package com.health.calculator.bmi.tracker.ui.screens.onboarding

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun introPage_exposesCoreCopyAndSkipAction() {
        var completed = false
        val welcomeTitle = resourceString(R.string.onboarding_welcome_title)
        val skipLabel = resourceString(R.string.txt_skip)
        val firstPageProgress = resourceString(R.string.onboarding_page_progress, 1, 3)

        setOnboardingContent(onComplete = { completed = true })

        composeRule.onNodeWithText(welcomeTitle).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(firstPageProgress).assertIsDisplayed()
        composeRule.onNodeWithText(skipLabel).assertIsDisplayed().performClick()
        composeRule.runOnIdle { assertTrue(completed) }
    }

    @Test
    fun selectedQuickAction_isDeliveredFromFinalPage() {
        var selectedAction: OnboardingStartAction? = null
        val nextLabel = resourceString(R.string.txt_next)
        val waterLabel = resourceString(R.string.onboarding_action_water)
        val waterDescription = resourceString(R.string.onboarding_action_water_description)
        val startWithWater = resourceString(R.string.onboarding_start_with_action, waterLabel)
        val finalPageProgress = resourceString(R.string.onboarding_page_progress, 3, 3)

        setOnboardingContent(onStartAction = { selectedAction = it })

        composeRule.onNodeWithText(nextLabel).performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText(nextLabel).performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithContentDescription(finalPageProgress).assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("$waterLabel. $waterDescription")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
            .assertIsSelected()
        composeRule.onNodeWithText(startWithWater).assertIsDisplayed().performClick()

        composeRule.runOnIdle {
            assertEquals(OnboardingStartAction.WATER, selectedAction)
        }
    }

    private fun setOnboardingContent(
        onComplete: () -> Unit = {},
        onStartAction: (OnboardingStartAction) -> Unit = {},
        fontScale: Float = 1f,
        themeMode: ThemeMode = ThemeMode.SYSTEM
    ) {
        composeRule.setContent {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, fontScale)) {
            HealthCalculatorTheme(themeMode = themeMode) {
                OnboardingScreen(
                    onComplete = onComplete,
                    onSetUpProfile = {},
                    onStartAction = onStartAction
                )
            }
            }
        }
    }

    @Test fun largeTextDarkThemeCanSelectLastActionAndContinue() {
        var selected: OnboardingStartAction? = null
        setOnboardingContent(onStartAction = { selected = it }, fontScale = 2f, themeMode = ThemeMode.DARK)
        repeat(2) {
            composeRule.onNodeWithText(resourceString(R.string.txt_next)).performClick()
            composeRule.waitForIdle()
        }
        val label = resourceString(R.string.onboarding_action_bmi)
        val description = resourceString(R.string.onboarding_action_bmi_description)
        composeRule.onNodeWithContentDescription("$label. $description")
            .performScrollTo().assertIsDisplayed().performClick().assertIsSelected()
        composeRule.onNodeWithText(resourceString(R.string.onboarding_start_with_action, label))
            .assertIsDisplayed().performClick()
        composeRule.runOnIdle { assertEquals(OnboardingStartAction.CALCULATORS, selected) }
    }

    private fun resourceString(id: Int, vararg formatArgs: Any): String =
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(id, *formatArgs)
}
