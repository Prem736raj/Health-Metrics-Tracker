// ui/screens/bloodpressure/BpRecommendationsScreen.kt
package com.health.calculator.bmi.tracker.ui.screens.bloodpressure

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.data.model.*
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/** Convert persisted recommendation markers to stable Material icons. */
private fun bpRecommendationIcon(legacyLabel: String): ImageVector = when {
    legacyLabel.contains("✅") -> Icons.Outlined.CheckCircle
    legacyLabel.contains("⚠") -> Icons.Outlined.Warning
    legacyLabel.contains("📈") || legacyLabel.contains("📊") -> Icons.Outlined.ShowChart
    legacyLabel.contains("📋") || legacyLabel.contains("📝") -> Icons.Outlined.Assignment
    legacyLabel.contains("🩺") || legacyLabel.contains("🏥") -> Icons.Outlined.LocalHospital
    legacyLabel.contains("❤️") || legacyLabel.contains("💙") -> Icons.Outlined.FavoriteBorder
    legacyLabel.contains("📏") -> Icons.Outlined.Straighten
    legacyLabel.contains("🔁") -> Icons.Outlined.Timeline
    legacyLabel.contains("🚫") -> Icons.Outlined.Block
    legacyLabel.contains("🥗") || legacyLabel.contains("🧂") -> Icons.Outlined.Restaurant
    legacyLabel.contains("🚶") -> Icons.Outlined.DirectionsWalk
    legacyLabel.contains("😴") -> Icons.Outlined.Schedule
    legacyLabel.contains("💡") -> Icons.Outlined.Lightbulb
    legacyLabel.contains("ℹ") -> Icons.Outlined.Info
    else -> Icons.Outlined.Info
}

// ─── Main Recommendations Section (for embedding in result screen) ─────────────

@Composable
fun BpRecommendationsSection(
    category: BpCategory
) {
    val guidance = remember(category) { BpRecommendationsProvider.getGuidance(category) }
    val categoryColor = getBpCategoryColor(category)
    val haptic = LocalHapticFeedback.current

    val enterAnim = remember { MutableTransitionState(false).apply { targetState = true } }

    AnimatedVisibility(
        visibleState = enterAnim,
        enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn(
            animationSpec = tween(600, delayMillis = 200)
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // Header Message Card
            RecommendationHeaderCard(
                legacyIcon = guidance.headerEmoji,
                message = guidance.headerMessage,
                tone = guidance.headerTone,
                categoryColor = categoryColor,
                monitoringFrequency = guidance.monitoringFrequency
            )

            // Urgency Note (if exists)
            guidance.urgencyNote?.let { note ->
                UrgencyNoteCard(note = note, tone = guidance.headerTone)
            }

            // Lifestyle Recommendations
            if (guidance.recommendations.isNotEmpty()) {
                ExpandableRecommendationCard(
                    title = "Lifestyle Recommendations",
                    icon = bpRecommendationIcon("💡"),
                    recommendations = guidance.recommendations,
                    categoryColor = categoryColor,
                    initiallyExpanded = true
                )
            }

            // Diet Tips
            if (guidance.dietTips.isNotEmpty()) {
                ExpandableRecommendationCard(
                    title = "Diet & Nutrition",
                    icon = bpRecommendationIcon("🥗"),
                    recommendations = guidance.dietTips,
                    categoryColor = categoryColor,
                    initiallyExpanded = false
                )
            }

            // Exercise Tips
            if (guidance.exerciseTips.isNotEmpty()) {
                ExpandableRecommendationCard(
                    title = "Exercise & Activity",
                    icon = bpRecommendationIcon("🚶"),
                    recommendations = guidance.exerciseTips,
                    categoryColor = categoryColor,
                    initiallyExpanded = false
                )
            }

            // Health Risks
            if (guidance.healthRisks.isNotEmpty()) {
                HealthRisksCard(
                    risks = guidance.healthRisks,
                    tone = guidance.headerTone
                )
            }

            // Doctor Advice
            guidance.doctorAdvice?.let { advice ->
                DoctorAdviceCard(advice = advice, tone = guidance.headerTone)
            }

            // White Coat Syndrome (always available)
            WhiteCoatSyndromeCard()

            // Medical Disclaimer
            MedicalDisclaimerCard()
        }
    }
}

// ─── Header Card ───────────────────────────────────────────────────────────────

@Composable
private fun RecommendationHeaderCard(
    legacyIcon: String,
    message: String,
    tone: BpGuidanceTone,
    categoryColor: Color,
    monitoringFrequency: String
) {
    val bgColor = when (tone) {
        BpGuidanceTone.POSITIVE -> HealthColors.Healthy.copy(alpha = 0.08f)
        BpGuidanceTone.GENTLE_AWARENESS -> HealthColors.Healthy.copy(alpha = 0.08f)
        BpGuidanceTone.CAUTIOUS -> HealthColors.Warning.copy(alpha = 0.08f)
        BpGuidanceTone.CONCERNED -> HealthColors.Caution.copy(alpha = 0.08f)
        BpGuidanceTone.URGENT -> HealthColors.Danger.copy(alpha = 0.08f)
        BpGuidanceTone.EMERGENCY -> HealthColors.Danger.copy(alpha = 0.08f)
        BpGuidanceTone.INFORMATIONAL -> HealthColors.Good.copy(alpha = 0.08f)
    }

    val borderColor = categoryColor.copy(alpha = 0.25f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(bgColor)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = bpRecommendationIcon(legacyIcon),
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(28.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.txt_personalized_guidance),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.text(categoryColor)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
                    )
                }
            }

            // Monitoring frequency badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(categoryColor.copy(alpha = 0.1f))
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = categoryColor
                    )
                    Text(
                        "Recommended monitoring: $monitoringFrequency",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.text(categoryColor)
                    )
                }
            }
        }
    }
}

// ─── Urgency Note Card ─────────────────────────────────────────────────────────

@Composable
private fun UrgencyNoteCard(note: String, tone: BpGuidanceTone) {
    val isEmergency = tone == BpGuidanceTone.EMERGENCY || tone == BpGuidanceTone.URGENT

    val infiniteTransition = rememberInfiniteTransition(label = "urgency_pulse")
    val pulseAlpha by if (isEmergency) {
        infiniteTransition.animateFloat(
            initialValue = 0.85f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
            label = "pulse"
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }

    val urgencyColor = if (isEmergency) HealthColors.Danger else HealthColors.Caution
    val bgColor = urgencyColor.copy(alpha = 0.08f * pulseAlpha)
    val textColor = urgencyColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.surface(bgColor)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            urgencyColor.copy(alpha = 0.3f * pulseAlpha)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                if (isEmergency) Icons.Filled.Error else Icons.Filled.Warning,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(22.dp)
            )
            Text(
                note,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isEmergency) FontWeight.Bold else FontWeight.Medium,
                color = AppColors.text(textColor)
            )
        }
    }
}

// ─── Expandable Recommendation Card ────────────────────────────────────────────

@Composable
private fun ExpandableRecommendationCard(
    title: String,
    icon: ImageVector,
    recommendations: List<BpRecommendation>,
    categoryColor: Color,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isExpanded = !isExpanded
                    }
                    .heightIn(min = 48.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.surface(categoryColor.copy(alpha = 0.1f))
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            "${recommendations.size}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.text(categoryColor)
                        )
                    }
                }

                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = tween(300),
                    label = "chevron"
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.graphicsLayer { rotationZ = rotation },
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )

                    recommendations.forEachIndexed { index, rec ->
                        val itemEnter = remember {
                            MutableTransitionState(false).apply { targetState = true }
                        }

                        AnimatedVisibility(
                            visibleState = itemEnter,
                            enter = slideInVertically(
                                initialOffsetY = { 30 },
                                animationSpec = tween(300, delayMillis = index * 80)
                            ) + fadeIn(animationSpec = tween(300, delayMillis = index * 80))
                        ) {
                            RecommendationItem(recommendation = rec)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationItem(recommendation: BpRecommendation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = bpRecommendationIcon(recommendation.icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                recommendation.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                recommendation.description,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
            )
        }
    }
}

// ─── Health Risks Card ─────────────────────────────────────────────────────────

@Composable
private fun HealthRisksCard(risks: List<String>, tone: BpGuidanceTone) {
    var isExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val riskColor = when (tone) {
        BpGuidanceTone.CAUTIOUS -> HealthColors.Warning
        BpGuidanceTone.CONCERNED -> HealthColors.Caution
        BpGuidanceTone.URGENT, BpGuidanceTone.EMERGENCY -> HealthColors.Danger
        BpGuidanceTone.INFORMATIONAL -> HealthColors.Good
        else -> HealthColors.Warning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isExpanded = !isExpanded
                    }
                    .heightIn(min = 48.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.HealthAndSafety,
                        contentDescription = null,
                        tint = riskColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        stringResource(R.string.txt_health_risks_to_be_aware_of),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = tween(300),
                    label = "risk_chevron"
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer { rotationZ = rotation },
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )

                    risks.forEach { risk ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(riskColor.copy(alpha = 0.05f))
                                .padding(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(riskColor)
                                    .align(Alignment.Top)
                                    .offset(y = 6.dp)
                            )
                            Text(
                                risk,
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Doctor Advice Card ────────────────────────────────────────────────────────

@Composable
private fun DoctorAdviceCard(advice: String, tone: BpGuidanceTone) {
    val isUrgent = tone == BpGuidanceTone.URGENT || tone == BpGuidanceTone.EMERGENCY || tone == BpGuidanceTone.CONCERNED

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(if (isUrgent) HealthColors.Good.copy(alpha = 0.1f)
            else HealthColors.Severe.copy(alpha = 0.08f))
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isUrgent) BorderStroke(1.dp, HealthColors.Good.copy(alpha = 0.25f)) else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUrgent) HealthColors.Good.copy(alpha = 0.14f)
                        else HealthColors.Severe.copy(alpha = 0.14f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.LocalHospital,
                    contentDescription = null,
                    tint = if (isUrgent) HealthColors.Good else HealthColors.Severe,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.txt_medical_advice),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(if (isUrgent) HealthColors.Good else HealthColors.Severe)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    advice,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                )
            }
        }
    }
}

// ─── White Coat Syndrome Card ──────────────────────────────────────────────────

@Composable
private fun WhiteCoatSyndromeCard() {
    var isExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val whiteCoatInfo = remember { BpRecommendationsProvider.getWhiteCoatSyndromeInfo() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isExpanded = !isExpanded
                    }
                    .heightIn(min = 48.dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalHospital,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            stringResource(R.string.txt_white_coat_syndrome),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(R.string.txt_why_readings_may_differ_at_the),
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                        )
                    }
                }

                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = tween(300),
                    label = "wc_chevron"
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer { rotationZ = rotation },
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )

                    whiteCoatInfo.forEach { info ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(HealthColors.Info.copy(alpha = 0.08f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = bpRecommendationIcon(info.icon),
                                contentDescription = null,
                                tint = HealthColors.Info,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(20.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    info.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    info.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Medical Disclaimer Card ───────────────────────────────────────────────────

@Composable
private fun MedicalDisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp).padding(top = 2.dp)
            )
            Column {
                Text(
                    stringResource(R.string.txt_medical_disclaimer),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    stringResource(R.string.txt_this_information_is_for_educat),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
                )
            }
        }
    }
}
