package com.health.calculator.bmi.tracker.ui.screens.heartrate

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/**
 * Legacy content models keep their emoji labels for backwards compatibility.
 * The rendered guide uses stable vector icons instead so the visual language
 * is consistent across themes, fonts, and accessibility settings.
 */
private fun heartRateEducationIcon(label: String): ImageVector = when {
    label.contains("❤️") || label.contains("💓") || label.contains("🫀") || label.contains("🔴") -> Icons.Outlined.MonitorHeart
    label.contains("📊") || label.contains("📋") -> Icons.Outlined.Assessment
    label.contains("💤") || label.contains("😴") -> Icons.Outlined.Schedule
    label.contains("🏃") || label.contains("🏋️") || label.contains("💪") -> Icons.Outlined.FitnessCenter
    label.contains("🔬") -> Icons.Outlined.Science
    label.contains("📐") -> Icons.Outlined.Straighten
    label.contains("🎯") || label.contains("🏆") -> Icons.Outlined.Flag
    label.contains("⛽") || label.contains("⚡") -> Icons.Outlined.Bolt
    label.contains("⚠") || label.contains("🚨") -> Icons.Outlined.Warning
    label.contains("😮") || label.contains("😵") || label.contains("🤢") || label.contains("🥶") || label.contains("🤕") || label.contains("😫") -> Icons.Outlined.Warning
    label.contains("⌚") || label.contains("📟") || label.contains("🎧") || label.contains("💍") -> Icons.Outlined.AccessTime
    label.contains("✋") -> Icons.Outlined.FrontHand
    label.contains("💡") -> Icons.Outlined.Lightbulb
    else -> Icons.Outlined.Info
}

private val LeadingLegacyMarker = Regex("^[\\p{So}\\p{Sk}\\p{M}\\p{Cf}\\s]+")
private fun cleanLegacyMarker(value: String): String = value.replaceFirst(LeadingLegacyMarker, "").trim()

// ============================================================
// MAIN EDUCATIONAL SECTION
// ============================================================

@Composable
fun HeartRateEducationalSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.MonitorHeart,
                contentDescription = null,
                tint = FeatureColors.HeartStart,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.txt_learn_about_heart_rate),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.txt_educational_content_to_help_yo),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        // Warning Signs Card (always visible — safety first)
        WarningSignsCard()

        // Educational sections
        UnderstandingHeartRateSection()
        HeartRateTrainingSection()
        MonitoringMethodsSection()
        HeartRateMythsSection()

        // Medical disclaimer
        MedicalDisclaimerCard()
    }
}

// ============================================================
// 1. UNDERSTANDING HEART RATE
// ============================================================

@Composable
private fun UnderstandingHeartRateSection() {
    var isExpanded by remember { mutableStateOf(false) }

    EducationalCard(
        emoji = "❤️",
        title = "Understanding Heart Rate",
        subtitle = "The basics of how your heart works during rest and activity",
        color = FeatureColors.HeartStart,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // What is heart rate
            ContentBlock(
                title = "What is Heart Rate?",
                emoji = "💓",
                content = "Heart rate is the number of times your heart beats per minute (BPM). " +
                        "It can provide useful context about cardiovascular fitness when considered alongside activity, sleep, stress, illness, medicines, and how you feel. " +
                        "Heart rate varies from person to person, so a single reading does not diagnose a condition."
            )

            // Types of heart rate
            ContentBlock(
                title = "Types of Heart Rate",
                emoji = "📊"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeartRateTypeCard(
                        emoji = "💤",
                        name = "Resting Heart Rate (RHR)",
                        range = "60-100 BPM (normal adult)",
                        description = "Your heart rate when completely at rest, ideally measured first thing in the morning. " +
                                "Some well-trained athletes have lower readings, but a low value is not automatically better. Trends and symptoms matter.",
                        color = HealthColors.Good
                    )
                    HeartRateTypeCard(
                        emoji = "🏃",
                        name = "Active Heart Rate",
                        range = "Varies by intensity",
                        description = "Your heart rate during physical activity. It increases with exercise intensity " +
                                "to pump more oxygen-rich blood to working muscles.",
                        color = HealthColors.Healthy
                    )
                    HeartRateTypeCard(
                        emoji = "🔴",
                        name = "Maximum Heart Rate (MHR)",
                        range = "~220 minus age",
                        description = "An estimate of peak heart rate during maximal effort. It is not a personal safety limit, and training zones are estimates.",
                        color = FeatureColors.HeartStart
                    )
                    HeartRateTypeCard(
                        emoji = "📐",
                        name = "Heart Rate Reserve (HRR)",
                        range = "MHR minus RHR",
                        description = "The difference between your max and resting heart rate. " +
                                "Used in the Karvonen formula for more personalized zone calculations.",
                        color = FeatureColors.StepsStart
                    )
                }
            }

            // Why RHR matters
            ContentBlock(
                title = "Why Resting Heart Rate Matters",
                emoji = "🔬",
                content = "Your resting heart rate can add useful context to a broader picture of fitness and wellbeing:"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val points = listOf(
                        "A lower RHR can occur when the heart pumps more blood per beat, but readings vary by person",
                        "Regular aerobic activity may lower a person’s usual resting heart rate over time",
                        "There is no universal week-by-week target; look for gradual trends rather than one reading",
                        "A sustained change from your usual pattern can reflect stress, illness, recovery, or other factors",
                        "Higher resting values have been associated with health risk in population studies, but one reading is not a diagnosis"
                    )
                    points.forEach { point ->
                    BulletPoint(text = point, color = FeatureColors.HeartStart)
                    }
                }
            }

            // RHR by age table
            ContentBlock(
                title = "Adult Resting Heart Rate Context",
                emoji = "📋"
            ) {
                val ageRanges = listOf(
                    Triple("Most adults at rest", "60–100 BPM", HealthColors.Info),
                    Triple("Some trained adults", "May be lower", HealthColors.Info)
                )

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        ageRanges.forEach { (ageGroup, range, color) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ageGroup,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = range,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// 2. HEART RATE TRAINING EXPLAINED
// ============================================================

@Composable
private fun HeartRateTrainingSection() {
    var isExpanded by remember { mutableStateOf(false) }
    EducationalCard(
        emoji = "🏋️",
        title = "Using Estimated Training Zones",
        subtitle = "Combine estimates with how the activity feels",
        color = HealthColors.Healthy,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ContentBlock(
                title = "A guide to effort",
                emoji = "🎯",
                content = "Age-based heart-rate equations describe population averages. Your actual maximum and response to activity can differ. Medicines, illness, heat and training affect readings. These zones are not personal safety limits."
            )
            ContentBlock(
                title = "Use the talk test too",
                emoji = "💬",
                content = "During moderate activity you can usually talk but not sing; vigorous activity makes sustained conversation harder. Build gradually at an effort that fits your abilities and follow any individual exercise advice."
            )
            ContentBlock(
                title = "Fuel use varies",
                emoji = "⛽",
                content = "The body uses a changing mix of fuels during exercise. Heart rate alone cannot determine a percentage of fat or carbohydrate burned, or predict body-fat loss. This app does not measure fuel use or oxygen consumption."
            )
            ContentBlock(
                title = "Choose a manageable routine",
                emoji = "📋",
                content = "There is no universal split of easy and hard training for every person. Recovery, experience, goals and health context matter. Ask a qualified professional about suitable activity if you have symptoms or a condition affecting exercise."
            )
        }
    }
}

// ============================================================
// 3. MONITORING METHODS
// ============================================================

@Composable
private fun MonitoringMethodsSection() {
    var isExpanded by remember { mutableStateOf(false) }
    EducationalCard(
        emoji = "⌚",
        title = "How to Monitor Heart Rate",
        subtitle = "The app uses values you enter; it does not measure your pulse",
        color = FeatureColors.StepsStart,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ContentBlock(
                title = "Manual pulse check",
                emoji = "✋",
                content = "When rested, use two fingers at the thumb side of your wrist and count beats over a full minute. Timing and counting affect the result. A manual check does not provide continuous monitoring."
            )
            ContentBlock(
                title = "Wearable readings",
                emoji = "⌚",
                content = "Chest straps and optical wearables can provide exercise or resting trends. Quality varies by device, fit, movement and activity. Follow the device instructions; this app does not certify or rank their accuracy."
            )
            ContentBlock(
                title = "Keep the context",
                emoji = "📋",
                content = "Compare readings taken in similar conditions. A consumer reading cannot diagnose a heart condition. Unexpected readings or symptoms need professional advice, regardless of an estimated training zone."
            )
        }
    }
}

// ============================================================
// 4. WARNING SIGNS (Safety First — always visible)
// ============================================================

@Composable
private fun WarningSignsCard() {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = HealthColors.Danger.copy(alpha = 0.06f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, HealthColors.Danger.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = HealthColors.Danger,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.txt_warning_signs_during_exercise),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = HealthColors.Danger
                        )
                        Text(
                            text = stringResource(R.string.txt_know_when_to_stop_your_safety_),
                            style = MaterialTheme.typography.labelSmall,
                            color = HealthColors.Danger.copy(alpha = 0.7f)
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = HealthColors.Danger.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Always visible key message
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = HealthColors.Danger.copy(alpha = 0.08f)
                )
            ) {
                Text(
                    text = stringResource(R.string.txt_stop_exercising_immediately_if),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = HealthColors.Danger,
                    modifier = Modifier.padding(10.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(spring(dampingRatio = 0.8f, stiffness = 300f)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Stop signs
                    val stopSigns = listOf(
                        "🫀 Chest pain, tightness, or pressure" to "Could indicate a cardiac event. Seek immediate help.",
                        "😮💨 Extreme shortness of breath" to "Beyond normal exercise breathing — gasping, unable to catch breath.",
                        "😵 Dizziness or lightheadedness" to "Feeling faint, room spinning, or vision going dark.",
                        "💓 Irregular heartbeat" to "Heart fluttering, skipping beats, or racing uncontrollably.",
                        "🤢 Nausea or vomiting" to "Especially if combined with other symptoms.",
                        "🥶 Cold sweats" to "Breaking into a cold, clammy sweat unrelated to exercise heat.",
                        "💪 Unusual pain" to "Sharp pain in arms, jaw, neck, or back during exercise."
                    )

                    stopSigns.forEach { (sign, explanation) ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = HealthColors.Danger,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = cleanLegacyMarker(sign),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = HealthColors.Danger
                                )
                                Text(
                                    text = explanation,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Consult doctor section
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HealthColors.Caution.copy(alpha = 0.08f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = stringResource(R.string.txt_consult_a_doctor_before_starti),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = HealthColors.Caution
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val consultReasons = listOf(
                                "You are over 40 and have been sedentary",
                                "You have a known heart condition",
                                "You have high blood pressure (uncontrolled)",
                                "You have diabetes",
                                "You have a family history of heart disease before age 55",
                                "You experience chest pain or shortness of breath at rest",
                                "You are pregnant",
                                "You have joint or bone conditions"
                            )
                            consultReasons.forEach { reason ->
                                BulletPoint(text = reason, color = HealthColors.Caution)
                            }
                        }
                    }
                }
            }

            if (!isExpanded) {
                Text(
                    text = stringResource(R.string.txt_tap_to_read_all_warning_signs),
                    style = MaterialTheme.typography.labelSmall,
                    color = HealthColors.Danger.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ============================================================
// 5. HEART RATE MYTHS
// ============================================================

@Composable
private fun HeartRateMythsSection() {
    var isExpanded by remember { mutableStateOf(false) }

    EducationalCard(
        emoji = "🔍",
        title = "Heart Rate Myths — Debunked",
        subtitle = "Common misconceptions about heart rate and training",
        color = HealthColors.Severe,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MythCard(
                mythNumber = 1,
                myth = "\"The fat burning zone is the best zone for weight loss\"",
                verdict = "No universal best zone",
                verdictColor = HealthColors.Caution,
                explanation = "Heart rate alone does not show how much fat or carbohydrate you burn. Fuel use and energy expenditure vary with the person, activity and conditions. No estimated zone guarantees body-fat loss; choose manageable activity that fits your goals and health."
            )

            MythCard(
                mythNumber = 2,
                myth = "\"Higher heart rate always means a better workout\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "A higher reading is not automatically a better workout. Appropriate effort depends on your experience, goals, recovery and health. A manageable routine can include easier activity; there is no universal training split for everyone."
            )

            MythCard(
                mythNumber = 3,
                myth = "\"220 minus age gives your exact max heart rate\"",
                verdict = "Approximation Only",
                verdictColor = HealthColors.Caution,
                explanation = "Age equations estimate population averages, not an individual maximum. Different equations can give different values, and none guarantees a safe exercise limit. Do not perform a maximum-effort test on your own to confirm an app estimate."
            )

            MythCard(
                mythNumber = 4,
                myth = "\"If my heart rate is low, I'm not working hard enough\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "Heart-rate response differs between people and can be affected by medicines, conditions and training. Use perceived effort and professional advice alongside estimates rather than comparing your reading with someone else."
            )

            MythCard(
                mythNumber = 5,
                myth = "\"You should always exercise at the same heart rate\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "A suitable routine depends on your experience, recovery and health. You do not need to reach every zone or add high-intensity work to meet an app target. Review an exercise plan with a qualified professional when needed."
            )
        }
    }
}

@Composable
private fun MythCard(
    mythNumber: Int,
    myth: String,
    verdict: String,
    verdictColor: Color,
    explanation: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = HealthColors.Severe.copy(alpha = 0.12f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "#$mythNumber",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = HealthColors.Severe,
                            fontSize = 10.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = myth,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = verdictColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Verdict: $verdict",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = verdictColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )
        }
    }
}

// ============================================================
// MEDICAL DISCLAIMER
// ============================================================

@Composable
private fun MedicalDisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.txt_medical_disclaimer_this_educat) +
                        "and does not constitute medical advice. Heart rate calculations and fitness " +
                        "assessments are estimates based on general population data. Individual results " +
                        "may vary. Always consult a qualified healthcare professional before starting " +
                        "or modifying an exercise program, especially if you have any pre-existing " +
                        "medical conditions.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                lineHeight = 15.sp
            )
        }
    }
}

// ============================================================
// REUSABLE COMPONENTS
// ============================================================

@Composable
private fun EducationalCard(
    emoji: String,
    title: String,
    subtitle: String,
    color: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isExpanded) 3.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = heartRateEducationIcon(emoji),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = color.copy(alpha = 0.6f)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f)
                ) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    HorizontalDivider(
                        color = color.copy(alpha = 0.1f),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                    content()
                }
            }

            if (!isExpanded) {
                Text(
                    text = stringResource(R.string.txt_tap_to_learn_more),
                    style = MaterialTheme.typography.labelSmall,
                    color = color.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ContentBlock(
    title: String,
    emoji: String,
    content: String? = null,
    additionalContent: @Composable (() -> Unit)? = null
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = heartRateEducationIcon(emoji),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        content?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp,
                modifier = Modifier.padding(start = 24.dp)
            )
        }

        additionalContent?.let {
            Box(modifier = Modifier.padding(start = 24.dp, top = if (content != null) 8.dp else 0.dp)) {
                it()
            }
        }
    }
}

@Composable
private fun HeartRateTypeCard(
    emoji: String,
    name: String,
    range: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.05f))
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = heartRateEducationIcon(emoji),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = range,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun BulletPoint(text: String, color: Color) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.txt_text_placeholder_3),
            color = color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp, top = 1.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 16.sp
        )
    }
}
