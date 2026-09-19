package com.health.calculator.bmi.tracker.ui.screens.onboarding

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
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
        onStartAction: (OnboardingStartAction) -> Unit = {}
    ) {
        composeRule.setContent {
            HealthCalculatorTheme {
                OnboardingScreen(
                    onComplete = onComplete,
                    onSetUpProfile = {},
                    onStartAction = onStartAction
                )
            }
        }
    }

    private fun resourceString(id: Int, vararg formatArgs: Any): String =
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .getString(id, *formatArgs)
}
