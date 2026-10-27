package com.health.calculator.bmi.tracker.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.domain.insights.WellnessInsight
import com.health.calculator.bmi.tracker.ui.components.CalculatorQualitySheet
import com.health.calculator.bmi.tracker.ui.components.forest.*
import kotlinx.coroutines.launch

/** Native botanical reference layouts with separate day and night visual roles. */
@Composable
internal fun ForestTrackHubScreen(
    onOpenWeight: () -> Unit, onOpenWater: () -> Unit,
    onOpenBloodPressure: () -> Unit, onOpenFood: () -> Unit,
    onOpenHealthConnections: () -> Unit, onOpenHistory: () -> Unit,
    onOpenReminders: () -> Unit
) {
    val light = BotanicalPalette.isLight
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val entries = listOf(
        ForestAction("Weight", "Add a weigh-in and\nsee your trend", ForestIcons.Weight, onOpenWeight),
        ForestAction("Water", "Record glasses\nor millilitres", Icons.Filled.WaterDrop, onOpenWater, BotanicalPalette.Water),
        ForestAction("Blood pressure", "Keep a careful\nhome reading log", ForestIcons.HeartPulse, onOpenBloodPressure, if (light) BotanicalPalette.Coral else BotanicalPalette.Primary),
        ForestAction("Food and calories", "Optional meal\nand calorie notes", ForestIcons.Dining, onOpenFood, BotanicalPalette.Amber),
        ForestAction("Steps and\nconnected data", "Choose whether\nto connect Health Connect", Icons.AutoMirrored.Filled.DirectionsWalk, onOpenHealthConnections),
        ForestAction("History", "Review, edit or\nremove previous entries", Icons.Outlined.Description, onOpenHistory, if (light) BotanicalPalette.Purple else BotanicalPalette.Primary)
    )
    ForestBackdrop(botanicalArtwork(R.drawable.bg_forest_track, R.drawable.bg_botanical_day_track)) {
        LazyColumn(Modifier.fillMaxSize().statusBarsPadding(), state = listState, contentPadding = PaddingValues(20.dp, 12.dp, 20.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)) {
            item {
                Box(Modifier.fillMaxWidth().height(98.dp)) {
                    ForestGlass(Modifier.align(Alignment.TopEnd).width(140.dp), wave = false, shape = CircleShape) {
                        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.FavoriteBorder, null, Modifier.size(21.dp), tint = BotanicalPalette.Primary)
                            Spacer(Modifier.width(8.dp))
                            ForestText("Better habits\na brighter you", 12f, if (light) BotanicalPalette.Primary else BotanicalPalette.Secondary)
                        }
                    }
                    Column(Modifier.align(Alignment.BottomStart).padding(bottom = 16.dp)) {
                        ForestText("Track", if (light) 40f else 38f, bold = true, serif = true)
                        ForestText("Small check-ins that make trends useful", 13f, BotanicalPalette.Secondary, bold = true)
                    }
                }
            }
            item {
                ForestGlass(Modifier.fillMaxWidth(), wave = light,
                    onClick = if (light) ({ scope.launch { listState.animateScrollToItem(2) }; Unit }) else null) {
                    if (light) Image(painterResource(R.drawable.botanical_day_card_leaves), null,
                        Modifier.align(Alignment.BottomEnd).width(42.dp).height(48.dp), contentScale = ContentScale.Fit)
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        ForestBadge(Icons.Outlined.CalendarMonth, modifier = Modifier.size(42.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            ForestText("Daily check-ins", 14f, bold = true)
                            ForestText("Log what matters to you. Nothing here is required, and missing a day does not erase your progress.", 12f, BotanicalPalette.Secondary)
                        }
                        if (light) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp), tint = BotanicalPalette.Secondary)
                    }
                }
            }
            items(entries.chunked(2)) { pair ->
                Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                    pair.forEach { entry ->
                        val accent = if (entry.accent == Color.Unspecified) BotanicalPalette.Primary else entry.accent
                        ForestGlass(Modifier.weight(1f).fillMaxHeight(), accent = accent, onClick = entry.onClick) {
                            if (light) Image(painterResource(R.drawable.botanical_day_card_leaves), null,
                                Modifier.align(Alignment.BottomEnd).width(35.dp).height(52.dp), contentScale = ContentScale.Fit)
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                                Modifier.align(Alignment.TopEnd).padding(top = 18.dp, end = 9.dp).size(22.dp), tint = BotanicalPalette.Secondary)
                            Column(Modifier.fillMaxWidth().heightIn(min = 104.dp).padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                ForestBadge(entry.icon, accent, Modifier.size(38.dp))
                                Spacer(Modifier.height(1.dp))
                                ForestText(entry.title, 13.5f, bold = true)
                                ForestText(entry.description, 12f, BotanicalPalette.Secondary)
                            }
                        }
                    }
                }
            }
            item {
                ForestActionRow(ForestAction("Reminders", "Choose helpful prompts only\nwhen you want them", Icons.Outlined.Notifications, onOpenReminders), leaves = true)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForestCalculatorsHubScreen(onOpen: (CalculatorDestination) -> Unit) {
    var selectedInfo by remember { mutableStateOf<CalculatorQualityInfo?>(null) }
    ForestBackdrop(botanicalArtwork(R.drawable.bg_forest_calculators, R.drawable.bg_botanical_day_calculators)) {
        LazyColumn(Modifier.fillMaxSize().statusBarsPadding(), contentPadding = PaddingValues(18.dp, 12.dp, 18.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            item { ForestHubHeader("Calculators", "Estimates with context, limits and sources", 82) }
            item {
                ForestSectionTitle("Pick a starting point", "These tools are informational estimates. Open a result to see the method, reference range and limitations.", Modifier.padding(bottom = 2.dp))
            }
            items(CalculatorQualityCatalog.all, key = { it.id }) { info ->
                val accent = forestCalculatorAccent(info.id)
                ForestGlass(Modifier.fillMaxWidth(), onClick = { onOpen(info.id) }) {
                    Image(painterResource(botanicalArtwork(R.drawable.forest_card_leaves, R.drawable.botanical_day_card_leaves)), null,
                        Modifier.align(Alignment.BottomStart).width(37.dp).height(48.dp), contentScale = ContentScale.Fit, alpha = if (BotanicalPalette.isLight) .85f else .45f)
                    Row(Modifier.fillMaxWidth().heightIn(min = 60.dp).padding(start = 18.dp, top = 7.dp, bottom = 7.dp, end = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        ForestBadge(forestCalculatorIcon(info.id), if (info.id == CalculatorDestination.BMI) BotanicalPalette.Primary else accent, Modifier.size(43.dp))
                        Spacer(Modifier.width(18.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            ForestText(info.title, 14f, bold = true)
                            ForestText(info.description, 12f, BotanicalPalette.Secondary)
                        }
                        IconButton(onClick = { selectedInfo = info }, modifier = Modifier.size(48.dp)) {
                            Icon(Icons.Outlined.Info, "Method and limits for ${info.title}", Modifier.size(23.dp), tint = accent)
                        }
                    }
                }
            }
        }
    }
    selectedInfo?.let { CalculatorQualitySheet(it, onDismiss = { selectedInfo = null }) }
}

@Composable
internal fun ForestInsightsHubScreen(
    onOpenWeeklyReport: () -> Unit, onOpenTrends: () -> Unit, onOpenAssistant: () -> Unit,
    onOpenAchievements: () -> Unit, onOpenArticles: () -> Unit, onOpenHistory: () -> Unit,
    insights: List<WellnessInsight>, onOpenInsight: (String) -> Unit
) {
    ForestBackdrop(botanicalArtwork(R.drawable.bg_forest_insights, R.drawable.bg_botanical_day_insights)) {
        LazyColumn(Modifier.fillMaxSize().statusBarsPadding(), contentPadding = PaddingValues(26.dp, 12.dp, 26.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { ForestHubHeader("Insights", "Patterns from the information you choose to record", 90, BotanicalPalette.Primary) }
            item { ForestSectionTitle("Your next useful view", "Insights describe logged patterns. They do not explain causes or diagnose conditions.") }
            if (insights.isNotEmpty()) {
                item { ForestSectionTitle("From your recent logs", modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)) }
                items(insights.take(5), key = { it.id }) { insight ->
                    ForestInsightCard(insight) { onOpenInsight(insight.actionRoute) }
                }
            }
            item { ForestActionRow(ForestAction("Weekly wellness summary", "Compare your check-ins with the previous week", Icons.Outlined.CalendarMonth, onOpenWeeklyReport)) }
            item { ForestActionRow(ForestAction("Trends", "Review your weight trend; use Track for water and blood pressure logs", ForestIcons.Trend, onOpenTrends)) }
            item { ForestActionRow(ForestAction("AI Wellness Assistant", "Ask general wellness questions with optional context", Icons.Filled.AutoAwesome, onOpenAssistant)) }
            item { ForestActionRow(ForestAction("Milestones", "Celebrate consistent, non-competitive progress", Icons.Filled.EmojiEvents, onOpenAchievements)) }
            item { ForestActionRow(ForestAction("Learn", "Read practical, evidence-informed explainers", Icons.AutoMirrored.Filled.MenuBook, onOpenArticles)) }
            item { ForestActionRow(ForestAction("All history", "Open the detailed history and export tools", Icons.Outlined.History, onOpenHistory)) }
        }
    }
}

@Composable
private fun ForestHubHeader(title: String, subtitle: String, height: Int, subtitleColor: Color = BotanicalPalette.Secondary) {
    Column(Modifier.fillMaxWidth().heightIn(min = height.dp).padding(start = 2.dp, top = 15.dp, bottom = 10.dp), verticalArrangement = Arrangement.Bottom) {
        ForestText(title, if (BotanicalPalette.isLight) 40f else 38f, bold = true, serif = true)
        ForestText(subtitle, 13f, subtitleColor)
    }
}

private data class ForestAction(val title: String, val description: String, val icon: ImageVector, val onClick: () -> Unit, val accent: Color = Color.Unspecified)

@Composable
private fun ForestActionRow(entry: ForestAction, leaves: Boolean = false) {
    val accent = if (entry.accent == Color.Unspecified) BotanicalPalette.Primary else entry.accent
    ForestGlass(Modifier.fillMaxWidth(), wave = leaves, onClick = entry.onClick) {
        if (leaves) Image(painterResource(botanicalArtwork(R.drawable.forest_card_leaves, R.drawable.botanical_day_card_leaves)), null,
            Modifier.align(Alignment.BottomEnd).width(88.dp).height(60.dp), contentScale = ContentScale.Fit, alpha = if (BotanicalPalette.isLight) 1f else .38f)
        Row(Modifier.fillMaxWidth().heightIn(min = 51.dp).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            ForestBadge(entry.icon, accent, Modifier.size(36.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                ForestText(entry.title, 13.5f, bold = true)
                ForestText(entry.description, 12f, BotanicalPalette.Secondary)
            }
            Spacer(Modifier.width(8.dp))
            Box(Modifier.size(27.dp).background(BotanicalPalette.Primary.copy(alpha = .035f), CircleShape), Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(22.dp), tint = BotanicalPalette.Secondary)
            }
        }
    }
}

@Composable
private fun ForestInsightCard(insight: WellnessInsight, onOpen: () -> Unit) {
    ForestGlass(Modifier.fillMaxWidth()) {
        Image(painterResource(botanicalArtwork(R.drawable.forest_insight_card_art, R.drawable.botanical_day_insight_art)), null,
            Modifier.align(Alignment.BottomEnd).width(108.dp).height(145.dp), contentScale = ContentScale.Fit, alpha = if (BotanicalPalette.isLight) 1f else .85f)
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ForestBadge(ForestIcons.Trend, modifier = Modifier.size(34.dp))
                Spacer(Modifier.width(12.dp))
                ForestText(insight.title, 13.5f, modifier = Modifier.weight(1f), bold = true)
            }
            Spacer(Modifier.height(4.dp))
            ForestText(insight.message, 12f, BotanicalPalette.Secondary, Modifier.fillMaxWidth(.72f))
            Spacer(Modifier.height(3.dp))
            ForestText(insight.evidence, 12f, BotanicalPalette.Secondary, Modifier.fillMaxWidth(.72f))
            FilledTonalButton(onClick = onOpen, modifier = Modifier.heightIn(min = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = BotanicalPalette.Button, contentColor = BotanicalPalette.Text),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 5.dp)) {
                ForestText("View detail", 13f, bold = true)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun forestCalculatorAccent(id: CalculatorDestination): Color = when (id) {
    CalculatorDestination.BMI, CalculatorDestination.WAIST_HIP -> BotanicalPalette.Purple
    CalculatorDestination.BMR, CalculatorDestination.CALORIES -> BotanicalPalette.Amber
    CalculatorDestination.BLOOD_PRESSURE, CalculatorDestination.HEART_RATE -> BotanicalPalette.Coral
    CalculatorDestination.WATER -> BotanicalPalette.Blue
    else -> BotanicalPalette.Primary
}

private fun forestCalculatorIcon(id: CalculatorDestination): ImageVector = when (id) {
    CalculatorDestination.BMI -> ForestIcons.Clipboard
    CalculatorDestination.BMR -> Icons.Filled.LocalFireDepartment
    CalculatorDestination.BLOOD_PRESSURE -> ForestIcons.HeartPulse
    CalculatorDestination.WATER -> Icons.Filled.WaterDrop
    CalculatorDestination.CALORIES -> ForestIcons.Dining
    CalculatorDestination.WAIST_HIP -> ForestIcons.Tape
    CalculatorDestination.HEART_RATE -> Icons.AutoMirrored.Filled.DirectionsWalk
    CalculatorDestination.IDEAL_WEIGHT -> ForestIcons.Weight
    CalculatorDestination.BSA -> Icons.Outlined.AccessibilityNew
    CalculatorDestination.METABOLIC -> Icons.Outlined.Science
}
