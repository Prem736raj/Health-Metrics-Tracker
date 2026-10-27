package com.health.calculator.bmi.tracker.ui.screens.metabolicsyndrome

import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.calculator.Ethnicity
import com.health.calculator.bmi.tracker.calculator.MetabolicCriterion
import com.health.calculator.bmi.tracker.calculator.MetabolicRiskLevel
import com.health.calculator.bmi.tracker.calculator.MetabolicSyndromeResult
import com.health.calculator.bmi.tracker.calculator.MultiStandardComparison
import com.health.calculator.bmi.tracker.ui.theme.*

@Composable
fun MetabolicSyndromeResultScreen(
    result: MetabolicSyndromeResult,
    isSaved: Boolean,
    standardsComparison: MultiStandardComparison?,
    onSave: () -> Unit,
    onRecalculate: () -> Unit,
    onShare: () -> Unit,
    onEthnicityChange: (Ethnicity) -> Unit,
    onNavigateToCalculator: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(result) {
        animationStarted = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    // Animated values
    val riskColor = animateRiskColor(criteriaMet = result.criteriaMet)
    val gaugeProgress = animateGaugeProgress(targetProgress = result.criteriaMet / 5f)
    val animatedCount = animateCountIncrement(targetCount = result.criteriaMet)
    val criterionVisibilities = rememberCriterionAnimStates(
        count = result.criteria.size,
        trigger = animationStarted
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // === Animated Risk Gauge ===
        RiskGauge(
            criteriaMet = animatedCount,
            animationProgress = gaugeProgress,
            riskColor = riskColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        // === Screening Summary ===
        ScreeningSummaryCard(result = result, animationStarted = animationStarted)

        Spacer(modifier = Modifier.height(16.dp))

        // === Criteria Cards ===
        Text(
            text = stringResource(R.string.txt_criteria_assessment),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        result.criteria.forEachIndexed { index, criterion ->
            AnimatedVisibility(
                visible = criterionVisibilities.getOrElse(index) { false },
                enter = slideInHorizontally(
                    initialOffsetX = { if (index % 2 == 0) -it else it },
                    animationSpec = tween(400)
                ) + fadeIn(tween(400))
            ) {
                CriterionCard(criterion = criterion)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Standards Comparison ===
        if (standardsComparison != null) {
            StandardsComparisonSection(
                comparison = standardsComparison,
                onEthnicityChange = onEthnicityChange
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // === Risk Message ===
        RiskMessageCard(riskLevel = result.riskLevel, criteriaMet = result.criteriaMet)

        Spacer(modifier = Modifier.height(16.dp))

        // === Detailed Recommendations (expandable) ===
        var showRecommendations by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = DetailColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showRecommendations = !showRecommendations }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Analytics, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(stringResource(R.string.txt_detailed_analysis_recommendati), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text("Tap to ${if (showRecommendations) "collapse" else "view health insights"}", style = MaterialTheme.typography.bodySmall, color = DetailColors.text(MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }
                Icon(if (showRecommendations) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }

        AnimatedVisibility(
            visible = showRecommendations,
            enter = expandVertically(tween(400)) + fadeIn(tween(400)),
            exit = shrinkVertically(tween(300)) + fadeOut(tween(200))
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                DetailedRecommendationsSection(result = result)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Cross Links ===
        CrossLinksSection(onNavigate = onNavigateToCalculator)

        Spacer(modifier = Modifier.height(24.dp))

        // === Action Buttons ===
        ActionButtons(isSaved = isSaved, onSave = onSave, onRecalculate = onRecalculate, onShare = onShare)

        Spacer(modifier = Modifier.height(16.dp))

        // === Disclaimer ===
        Card(
            colors = CardDefaults.cardColors(containerColor = DetailColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_this_assessment_is_for_educati),
                    style = MaterialTheme.typography.bodySmall,
                    color = DetailColors.text(MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun RiskGauge(
    criteriaMet: Int,
    animationProgress: Float,
    riskColor: Color
) {
    val trackColor = DetailColors.Outline
    Surface(color = DetailColors.SurfaceAlt, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("$criteriaMet/5", style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold, color = DetailColors.Text)
                Text(stringResource(R.string.txt_criteria_met_1), style = MaterialTheme.typography.bodySmall,
                    color = DetailColors.Secondary, modifier = Modifier.weight(1f).padding(bottom = 6.dp))
            }
            // Five nodes display the existing criteria count; all criterion details remain below.
            Canvas(Modifier.fillMaxWidth().height(36.dp)) {
                val y = size.height / 2
                drawLine(trackColor, Offset(size.width * .08f, y), Offset(size.width * .92f, y), 2.dp.toPx())
                drawLine(riskColor, Offset(size.width * .08f, y),
                    Offset(size.width * (.08f + .84f * animationProgress), y), 3.dp.toPx())
                repeat(5) { index ->
                    val center = Offset(size.width * (.08f + index * .21f), y)
                    drawCircle(trackColor, 9.dp.toPx(), center, style = Stroke(2.dp.toPx()))
                    if (index < criteriaMet) drawCircle(riskColor, 6.dp.toPx(), center)
                }
            }
        }
    }
}

@Composable
private fun ScreeningSummaryCard(result: MetabolicSyndromeResult, animationStarted: Boolean) {
    val screeningColor = if (result.isSyndromePresent) HealthColors.Caution else HealthColors.Healthy
    val screeningIcon = if (result.isSyndromePresent) Icons.Outlined.Info else Icons.Outlined.CheckCircle

    AnimatedVisibility(
        visible = animationStarted,
        enter = scaleIn(tween(500, delayMillis = 600)) + fadeIn(tween(500, delayMillis = 600))
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DetailColors.surface(screeningColor.copy(alpha = 0.1f))),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(screeningColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(screeningIcon, contentDescription = null, tint = screeningColor, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Screening reference: ${result.criteriaMet} of 5 markers",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DetailColors.text(screeningColor)
                    )
                    Text(
                        text = "Selected ${result.selectedEthnicity.displayName} waist reference; not a diagnosis",
                        style = MaterialTheme.typography.bodySmall,
                        color = DetailColors.text(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun CriterionCard(criterion: MetabolicCriterion) {
    val statusColor = if (criterion.isMet) HealthColors.Caution else HealthColors.Healthy
    val statusIcon = if (criterion.isMet) Icons.Outlined.Warning else Icons.Outlined.CheckCircle

    val bgAlpha by animateFloatAsState(
        targetValue = 0.06f,
        animationSpec = tween(600),
        label = "criterion_bg"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = DetailColors.surface(statusColor.copy(alpha = bgAlpha))),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(statusColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(criterion.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    if (criterion.isOnMedication) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                            Text(stringResource(R.string.txt_medicated), style = MaterialTheme.typography.labelSmall, color = DetailColors.text(MaterialTheme.colorScheme.tertiary), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("Your value: ${criterion.userValue}", style = MaterialTheme.typography.bodyMedium, color = DetailColors.text(MaterialTheme.colorScheme.onSurfaceVariant))
                Text("Threshold: ${criterion.threshold}", style = MaterialTheme.typography.bodySmall, color = DetailColors.secondaryText(DetailColors.Secondary))
            }
            Surface(color = statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = if (criterion.isMet) "Marker met" else "Marker not met",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = DetailColors.text(statusColor),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun RiskMessageCard(riskLevel: MetabolicRiskLevel, criteriaMet: Int) {
    val (icon, message, color) = when (riskLevel) {
        MetabolicRiskLevel.NONE -> Triple(Icons.Outlined.CheckCircle, "No screening markers met in the values entered. Keep tracking consistently.", HealthColors.Healthy)
        MetabolicRiskLevel.LOW -> Triple(Icons.Outlined.Info, "One screening marker met. Review the value and its context rather than drawing a diagnosis from one screen.", HealthColors.Good)
        MetabolicRiskLevel.MODERATE -> Triple(Icons.Outlined.Info, "Two screening markers met. Consider discussing the pattern at a routine visit.", HealthColors.Warning)
        MetabolicRiskLevel.HIGH -> Triple(Icons.Outlined.Warning, "Three screening markers met. This is not a diagnosis; ask a healthcare professional for context.", HealthColors.Caution)
        MetabolicRiskLevel.VERY_HIGH -> Triple(Icons.Outlined.Warning, "$criteriaMet of 5 screening markers met. This is not a diagnosis; arrange professional review.", HealthColors.Danger)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = DetailColors.surface(MaterialTheme.colorScheme.surfaceVariant)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(riskLevel.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = DetailColors.text(color))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium, color = DetailColors.text(MaterialTheme.colorScheme.onSurfaceVariant), lineHeight = 20.sp)
        }
    }
}

@Composable
private fun ActionButtons(isSaved: Boolean, onSave: () -> Unit, onRecalculate: () -> Unit, onShare: () -> Unit) {
    val haptic = LocalHapticFeedback.current

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSave() },
            enabled = !isSaved,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(if (isSaved) Icons.Filled.Check else Icons.Outlined.Save, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isSaved) "Saved" else "Save")
        }
        OutlinedButton(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onRecalculate() },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.txt_recalculate))
        }
        OutlinedButton(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onShare() },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.txt_share))
        }
    }
}
