package com.health.calculator.bmi.tracker.ui.components.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.data.model.ThemeMode
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DetailDesignSystemTest(private val mode: ThemeMode) {
    companion object {
        @JvmStatic @Parameterized.Parameters(name = "{0}")
        fun modes() = listOf(arrayOf(ThemeMode.LIGHT), arrayOf(ThemeMode.DARK))
    }
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun featureThemeFollowsAppBrightnessAndRestoresParent() {
        var before: Color? = null
        var inside: Color? = null
        var after: Color? = null
        var scoped = false
        var restoredActive = true
        var dark: Boolean? = null
        var cardContent: Pair<Color, Color>? = null
        compose.setContent {
            HealthCalculatorTheme(mode) {
                val original = MaterialTheme.colorScheme.background
                DetailScreenTheme(DetailFeature.BMR) {
                    val background = MaterialTheme.colorScheme.background
                    val active = DetailColors.active
                    val isDark = DetailColors.isDark
                    val resolvedCard = contentColorFor(MaterialTheme.colorScheme.surface)
                    val expectedCard = DetailColors.Text
                    SideEffect { cardContent = resolvedCard to expectedCard }
                    SideEffect { inside = background; scoped = active; dark = isDark }
                }
                val restored = MaterialTheme.colorScheme.background
                val active = DetailColors.active
                SideEffect { before = original; after = restored; restoredActive = active }
            }
        }
        compose.runOnIdle {
            assertTrue(scoped)
            assertFalse(restoredActive)
            assertEquals(before, after)
            assertEquals(mode == ThemeMode.DARK, dark)
            assertEquals(detailPalette(DetailFeature.BMR, mode == ThemeMode.DARK).background, inside)
            assertEquals(cardContent!!.second, cardContent!!.first)
        }
    }

    @Test fun unscopedSharedHelpersKeepLegacyColors() {
        var values: List<Pair<Color, Color>> = emptyList()
        compose.setContent {
            HealthCalculatorTheme(mode) {
                val marker = Color(0xFF23795A)
                val pairs = listOf(
                    DetailColors.text(marker) to AppColors.text(marker),
                    DetailColors.secondaryText(marker) to AppColors.secondaryText(marker),
                    DetailColors.accent(marker) to AppColors.accent(marker),
                    DetailColors.icon(marker) to AppColors.icon(marker),
                    DetailColors.outline(marker) to AppColors.outline(marker),
                    DetailColors.surface(marker, 2) to AppColors.surface(marker, 2)
                )
                SideEffect { values = pairs }
            }
        }
        compose.runOnIdle { values.forEach { assertEquals(it.first, it.second) }; assertEquals(6, values.size) }
    }

    @Test fun allFeatureHeadersWrapOnNarrowScreenWithLargerText() {
        compose.setContent {
            HealthCalculatorTheme(mode) {
                val original = LocalDensity.current
                CompositionLocalProvider(LocalDensity provides Density(original.density, 1.3f)) {
                    Column(Modifier.width(320.dp).verticalScroll(rememberScrollState())) {
                        DetailFeature.entries.forEach { feature ->
                            DetailScreenTheme(feature) {
                                DetailHero(feature.name.lowercase().replace('_', ' '),
                                    "Measurements, context and saved observations", feature = feature)
                            }
                        }
                    }
                }
            }
        }
        DetailFeature.entries.forEach { feature ->
            compose.onNodeWithTag("detail-hero-${feature.name.lowercase()}").performScrollTo()
            val bounds = compose.onRoot().fetchSemanticsNode().boundsInRoot
            compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.GetTextLayoutResult), true)
                .fetchSemanticsNodes().filter { it.boundsInRoot.overlaps(bounds) }.forEach { node ->
                    val layouts = mutableListOf<TextLayoutResult>()
                    node.config[SemanticsActions.GetTextLayoutResult].action?.invoke(layouts)
                    layouts.forEach { layout ->
                        assertFalse("Clipped $feature: ${layout.layoutInput.text}", layout.multiParagraph.didExceedMaxLines)
                        assertTrue("Height clipped $feature", layout.multiParagraph.height <= layout.size.height + 1f)
                        assertTrue("Small text $feature", layout.layoutInput.style.fontSize.value >= 12f)
                    }
                }
        }
    }
}
