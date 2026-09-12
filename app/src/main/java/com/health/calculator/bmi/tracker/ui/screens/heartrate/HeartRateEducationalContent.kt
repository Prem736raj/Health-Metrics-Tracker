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
                title = "Normal Resting Heart Rate by Age",
                emoji = "📋"
            ) {
                val ageRanges = listOf(
                    Triple("Newborn (0-1 mo)", "70-190 BPM", HealthColors.Danger),
                    Triple("Infant (1-12 mo)", "80-160 BPM", HealthColors.Caution),
                    Triple("Toddler (1-3 yr)", "80-130 BPM", HealthColors.Warning),
                    Triple("Child (3-5 yr)", "80-120 BPM", HealthColors.Warning),
                    Triple("Child (6-12 yr)", "70-110 BPM", HealthColors.Healthy),
                    Triple("Teen (13-17 yr)", "60-100 BPM", HealthColors.Healthy),
                    Triple("Adult (18-64 yr)", "60-100 BPM", HealthColors.Good),
                    Triple("Senior (65+ yr)", "60-100 BPM", FeatureColors.StepsStart),
                    Triple("Athlete", "40-60 BPM", FeatureColors.StepsDeep)
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
        title = "Heart Rate Training Explained",
        subtitle = "Why training in zones matters and the science behind it",
        color = HealthColors.Healthy,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Why zones matter
            ContentBlock(
                title = "Why Training in Zones Matters",
                emoji = "🎯",
                content = "Training in specific heart rate zones allows you to target different physiological " +
                        "adaptations. Without zones, most people either train too hard (risking burnout and injury) " +
                        "or too easy (limiting progress). Zones give you a roadmap for effective, sustainable training."
            )

            // Fuel sources by zone
            ContentBlock(
                title = "How Your Body Fuels Each Zone",
                emoji = "⛽"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FuelSourceCard(
                        zone = "Zone 1-2",
                        zoneLabel = "Recovery / Fat Burn",
                        fatPercent = 85,
                        glycogenPercent = 15,
                        description = "Your body primarily burns fat as fuel. This is sustainable for long periods. " +
                                "Fat is an almost unlimited energy source — even lean people have tens of thousands of calories stored as fat.",
                        fatColor = HealthColors.Warning,
                        glycogenColor = FeatureColors.StepsStart
                    )
                    FuelSourceCard(
                        zone = "Zone 3",
                        zoneLabel = "Aerobic",
                        fatPercent = 50,
                        glycogenPercent = 50,
                        description = "An even mix of fat and carbohydrates (glycogen). This is the sweet spot " +
                                "for building cardiovascular endurance without depleting your energy stores too quickly.",
                        fatColor = HealthColors.Warning,
                        glycogenColor = FeatureColors.StepsStart
                    )
                    FuelSourceCard(
                        zone = "Zone 4",
                        zoneLabel = "Anaerobic",
                        fatPercent = 15,
                        glycogenPercent = 85,
                        description = "Mostly glycogen (stored carbs). Your body can't deliver oxygen fast enough, " +
                                "so it switches to anaerobic metabolism. Lactic acid builds up — this is why it burns.",
                        fatColor = HealthColors.Warning,
                        glycogenColor = FeatureColors.StepsStart
                    )
                    FuelSourceCard(
                        zone = "Zone 5",
                        zoneLabel = "VO₂ Max",
                        fatPercent = 5,
                        glycogenPercent = 95,
                        description = "Almost entirely glycogen and phosphocreatine. Sustainable for only 1-3 minutes. " +
                                "This zone pushes your absolute physiological limits.",
                        fatColor = HealthColors.Warning,
                        glycogenColor = FeatureColors.StepsStart
                    )
                }
            }

            // Why not always Zone 5
            ContentBlock(
                title = "Why You Shouldn't Always Train in Zone 5",
                emoji = "⚠️"
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = HealthColors.Caution.copy(alpha = 0.06f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val reasons = listOf(
                            "Overtraining — Your body may not recover from daily Zone 5 sessions, leading to fatigue, sleep disruption, or decreased performance",
                            "Injury risk — High intensity can increase the risk of muscle, tendon, and joint injuries",
                            "Diminishing returns — More Zone 5 does not always mean more fitness; an easier aerobic base still matters",
                            "Burnout — Constantly pushing your limits can be mentally exhausting and unsustainable long-term",
                            "Heart stress — Excessive high-intensity training may place additional stress on the heart",
                            "Training balance — Many endurance programs use mostly easy sessions with a smaller amount of hard work; individual plans vary"
                        )
                        reasons.forEach { reason ->
                            Text(
                                text = reason,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // The 80/20 rule
            ContentBlock(
                title = "The 80/20 Rule of Training",
                emoji = "📊",
                content = "A commonly used training distribution is:\n\n" +
                        "• 80% of training in Zone 1-3 (easy to moderate)\n" +
                        "• 20% of training in Zone 4-5 (hard to maximum)\n\n" +
                        "This \"polarized\" approach can build an aerobic base while retaining some high-intensity work. " +
                        "It is a general example, not a prescription; adjust intensity to your experience and health."
            )
        }
    }
}

@Composable
private fun FuelSourceCard(
    zone: String,
    zoneLabel: String,
    fatPercent: Int,
    glycogenPercent: Int,
    description: String,
    fatColor: Color,
    glycogenColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$zone — $zoneLabel",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Fuel bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .weight(fatPercent.toFloat().coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(fatColor.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (fatPercent >= 20) {
                        Text(
                            text = "${fatPercent}% fat",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(glycogenPercent.toFloat().coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(glycogenColor.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (glycogenPercent >= 20) {
                        Text(
                            text = "${glycogenPercent}% carbs",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Legend
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(fatColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.txt_fat), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(glycogenColor)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.txt_glycogen), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                lineHeight = 15.sp
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
        subtitle = "Compare different methods and find what works for you",
        color = FeatureColors.StepsStart,
        isExpanded = isExpanded,
        onToggle = { isExpanded = !isExpanded }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val methods = listOf(
                MonitorMethod(
                    emoji = "✋",
                    name = "Manual Pulse Check",
                    accuracy = "Low-Medium",
                    accuracyStars = 2,
                    cost = "Free",
                    pros = listOf(
                        "No equipment needed",
                        "Available anytime, anywhere",
                        "Good for resting HR checks"
                    ),
                    cons = listOf(
                        "Difficult during exercise",
                        "Counting errors common",
                        "No continuous monitoring",
                        "Can't check zones in real-time"
                    ),
                    tip = "Best for: Quick resting HR checks, especially first thing in the morning",
                    color = MaterialTheme.colorScheme.outline
                ),
                MonitorMethod(
                    emoji = "📟",
                    name = "Chest Strap Monitor",
                    accuracy = "Very High",
                    accuracyStars = 5,
                    cost = "$$",
                    pros = listOf(
                        "Often a practical consumer option; accuracy varies",
                        "Real-time continuous monitoring",
                        "Works during all activities",
                        "Reliable in all conditions",
                        "Connects to most fitness apps"
                    ),
                    cons = listOf(
                        "Can be uncomfortable initially",
                        "Needs to be wet for good contact",
                        "Extra piece of equipment",
                        "Battery replacement needed"
                    ),
                    tip = "Best for: Serious training, racing, and anyone wanting precise zone data",
                    color = HealthColors.Healthy
                ),
                MonitorMethod(
                    emoji = "⌚",
                    name = "Wrist-Based (Smartwatch)",
                    accuracy = "Medium-High",
                    accuracyStars = 3,
                    cost = "$$$",
                    pros = listOf(
                        "Convenient — already on your wrist",
                        "Continuous 24/7 monitoring",
                        "Tracks trends, sleep, and recovery",
                        "GPS and other fitness features",
                        "No extra gear needed"
                    ),
                    cons = listOf(
                        "Less accurate during intense exercise",
                        "Affected by movement and sweat",
                        "Lag in detecting rapid HR changes",
                        "Tattoos may affect accuracy",
                        "Needs charging"
                    ),
                    tip = "Best for: Daily monitoring, general fitness tracking, and casual training",
                    color = FeatureColors.StepsStart
                ),
                MonitorMethod(
                    emoji = "🎧",
                    name = "HR-Monitoring Earbuds",
                    accuracy = "Medium-High",
                    accuracyStars = 3,
                    cost = "$$$",
                    pros = listOf(
                        "Dual-purpose: music + HR monitoring",
                        "Good accuracy from in-ear optical sensor",
                        "Comfortable for most activities",
                        "No wrist needed"
                    ),
                    cons = listOf(
                        "Limited device compatibility",
                        "Falls out during intense exercise",
                        "Battery life concerns",
                        "Fewer options available"
                    ),
                    tip = "Best for: Runners and gym-goers who always use earbuds anyway",
                    color = HealthColors.Severe
                ),
                MonitorMethod(
                    emoji = "💍",
                    name = "Smart Ring",
                    accuracy = "Medium",
                    accuracyStars = 3,
                    cost = "$$$",
                    pros = listOf(
                        "Very discreet and comfortable",
                        "Good for 24/7 resting HR tracking",
                        "Excellent sleep tracking",
                        "Long battery life"
                    ),
                    cons = listOf(
                        "Not great during exercise",
                        "Limited real-time display",
                        "Sizing can be tricky",
                        "Fewer features than smartwatches"
                    ),
                    tip = "Best for: Resting HR tracking, sleep analysis, and daily monitoring",
                    color = HealthColors.Caution
                )
            )

            methods.forEach { method ->
                MonitorMethodCard(method = method)
            }

            // Accuracy comparison summary
            ContentBlock(
                title = "Quick Accuracy Comparison",
                emoji = "📊"
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = stringResource(R.string.txt_from_most_to_least_accurate),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        val ranking = listOf(
                            "1. Chest strap — Often a high-accuracy consumer option; fit and device quality matter",
                            "2. Smartwatch — Useful for trends, with accuracy varying by device and activity",
                            "3. HR earbuds — Optical readings vary by fit, device, and movement",
                            "4. Smart ring — Often better suited to resting trends than live exercise readings",
                            "5. Manual — Depends on timing and counting technique"
                        )
                        ranking.forEach { item ->
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MonitorMethod(
    val emoji: String,
    val name: String,
    val accuracy: String,
    val accuracyStars: Int,
    val cost: String,
    val pros: List<String>,
    val cons: List<String>,
    val tip: String,
    val color: Color
)

@Composable
private fun MonitorMethodCard(method: MonitorMethod) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDetails = !showDetails },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = method.color.copy(alpha = 0.04f)
        ),
        border = if (showDetails) androidx.compose.foundation.BorderStroke(
            1.dp, method.color.copy(alpha = 0.2f)
        ) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = heartRateEducationIcon(method.emoji),
                    contentDescription = null,
                    tint = method.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = method.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Stars
                        Text(
                            text = stringResource(R.string.txt_text_placeholder_22).repeat(method.accuracyStars) + "☆".repeat(5 - method.accuracyStars),
                            fontSize = 10.sp
                        )
                        Text(
                            text = method.cost,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                Icon(
                    imageVector = if (showDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = showDetails,
                enter = expandVertically(spring(dampingRatio = 0.8f, stiffness = 300f)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = method.color.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pros
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.txt_pros),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = HealthColors.Healthy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            method.pros.forEach { pro ->
                                Text(
                                    text = "• $pro",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        // Cons
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.txt_cons),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = HealthColors.Danger
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            method.cons.forEach { con ->
                                Text(
                                    text = "• $con",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = method.color.copy(alpha = 0.08f)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lightbulb,
                                contentDescription = null,
                                tint = method.color,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = method.tip,
                                style = MaterialTheme.typography.labelSmall,
                                color = method.color,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
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
                verdict = "Partially True",
                verdictColor = HealthColors.Caution,
                explanation = "Zone 2 does burn a higher PERCENTAGE of calories from fat (~60% vs ~35% in Zone 4). " +
                        "However, higher-intensity zones burn MORE TOTAL CALORIES per minute. " +
                        "For weight loss, total calorie burn matters most.\n\n" +
                        "The best approach? A mix of both: Zone 2 for longer sessions (more total fat burned) " +
                        "and Zone 4-5 HIIT for short, high-calorie-burn sessions (plus EPOC — excess post-exercise oxygen consumption " +
                        "keeps burning calories for hours after)."
            )

            MythCard(
                mythNumber = 2,
                myth = "\"Higher heart rate always means a better workout\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "A \"better\" workout depends on your goal. If your goal is endurance, " +
                        "a long Zone 2 session is far more effective than a short Zone 5 burst.\n\n" +
                        "Training too hard too often leads to overtraining, injury, and burnout. " +
                        "Elite athletes spend 80% of their time in Zone 2 — not Zone 5. " +
                        "A good workout is one that matches your training goal for that day."
            )

            MythCard(
                mythNumber = 3,
                myth = "\"220 minus age gives your exact max heart rate\"",
                verdict = "Approximation Only",
                verdictColor = HealthColors.Caution,
                explanation = "The 220-age formula was never meant to be precise for individuals. " +
                        "It's a population average with a standard deviation of ±10-12 BPM.\n\n" +
                        "That means a 30-year-old might have a true MHR anywhere from 178 to 202, not exactly 190. " +
                        "Newer formulas like Tanaka (208 - 0.7 × age) are slightly more accurate, " +
                        "but the only way to know your true MHR is through a supervised max-effort test.\n\n" +
                        "For most people, the estimate is close enough for zone-based training."
            )

            MythCard(
                mythNumber = 4,
                myth = "\"If my heart rate is low, I'm not working hard enough\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "Fit individuals have lower heart rates at the same effort level because their hearts " +
                        "are more efficient (pumping more blood per beat). A well-trained runner may run at 130 BPM " +
                        "while an untrained person walks at 130 BPM.\n\n" +
                        "Focus on YOUR zones relative to YOUR max heart rate, not someone else's numbers."
            )

            MythCard(
                mythNumber = 5,
                myth = "\"You should always exercise at the same heart rate\"",
                verdict = "False",
                verdictColor = HealthColors.Danger,
                explanation = "Varying intensity is key to fitness gains. Your body adapts to repetitive stimuli. " +
                        "Training only in Zone 3 will lead to a plateau.\n\n" +
                        "A well-designed program includes easy days (Zone 1-2), moderate days (Zone 3), " +
                        "and hard days (Zone 4-5) for maximum adaptation and progress."
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
