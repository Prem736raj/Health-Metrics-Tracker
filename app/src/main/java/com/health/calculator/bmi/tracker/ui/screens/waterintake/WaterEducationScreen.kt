// ui/screens/waterintake/WaterEducationScreen.kt
package com.health.calculator.bmi.tracker.ui.screens.waterintake

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterEducationScreen(
    onNavigateBack: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            tint = FeatureColors.WaterStart,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(stringResource(R.string.txt_hydration_guide), fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
        ) {
            // Header
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -30 }
                ) {
                    EducationHeaderCard()
                }
            }

            // Section 1: Why Hydration Matters
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 100)) + slideInVertically(tween(500, 100)) { 40 }
                ) {
                    WhyHydrationMattersSection()
                }
            }

            // Section 2: How Much Water
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 200)) + slideInVertically(tween(500, 200)) { 40 }
                ) {
                    HowMuchWaterSection()
                }
            }

            // Section 3: Overhydration Warning
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 300)) + slideInVertically(tween(500, 300)) { 40 }
                ) {
                    OverhydrationSection()
                }
            }

            // Section 4: Exercise & Hydration
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 400)) + slideInVertically(tween(500, 400)) { 40 }
                ) {
                    ExerciseHydrationSection()
                }
            }

            // Section 5: Special Needs
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 500)) + slideInVertically(tween(500, 500)) { 40 }
                ) {
                    SpecialNeedsSection()
                }
            }

            // Medical Disclaimer
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, 600))
                ) {
                    MedicalDisclaimerCard()
                }
            }
        }
    }
}

// ─── Header ──────────────────────────────────────────────────────────────────

@Composable
private fun EducationHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(FeatureColors.WaterStart, FeatureColors.WaterDeep)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.WaterDrop,
                    contentDescription = null,
                    tint = FeatureColors.OnWater,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    "Everything You Need to\nKnow About Hydration",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
                Text(
                    stringResource(R.string.txt_evidence_based_information_to__1),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ─── Section 1: Why Hydration Matters ────────────────────────────────────────

@Composable
private fun WhyHydrationMattersSection() {
    val benefits = listOf(
        BenefitItem("⚡", "Energy & Performance",
            "Water is essential for energy production in every cell. Even mild dehydration (1-2%) can impair physical performance and cause fatigue. Staying hydrated helps maintain energy levels throughout the day."
        ),
        BenefitItem("🧠", "Brain Function & Mood",
            "Your brain is about 75% water. Dehydration can impair concentration, memory, and mood. Studies show that even 1-2% dehydration can affect cognitive performance and increase anxiety."
        ),
        BenefitItem("✨", "Skin Health",
            "Fluid balance is part of normal body function. Drinking extra water does not guarantee fewer wrinkles or treat a skin condition. Follow appropriate care advice for persistent skin concerns."
        ),
        BenefitItem("🫀", "Heart & Circulation",
            "Water helps maintain blood volume and supports healthy circulation. When dehydrated, your heart has to work harder to pump blood, which can increase heart rate and lower blood pressure."
        ),
        BenefitItem("🫁", "Digestion & Metabolism",
            "Water is crucial for proper digestion. It helps break down food, absorb nutrients, and prevent constipation. Adequate hydration also supports your body's metabolic processes."
        ),
        BenefitItem("🦴", "Joint Lubrication",
            "The cartilage in joints and spinal discs is about 80% water. Water supports normal tissue function; drinking more water is not a treatment for joint pain."
        ),
        BenefitItem("🌡️", "Temperature Regulation",
            "Your body uses water (through sweat) to regulate temperature. Without adequate hydration, your body can't cool itself effectively, increasing the risk of heat-related illnesses."
        ),
        BenefitItem("🫘", "Kidney Function",
            "Kidneys need water to filter waste from the blood and produce urine. Fluid needs depend on kidney function and your care plan. Urine color can change with foods, medicines and illness; it is not a diagnostic test."
        ),
        BenefitItem("🛡️", "Immune Support",
            "Water helps carry oxygen to cells, including immune cells, and supports the lymphatic system. Proper hydration helps your body fight off illness more effectively."
        )
    )

    ExpandableEducationCard(
        title = "Why Hydration Matters",
        icon = "💪",
        gradientColors = listOf(FeatureColors.WaterStart, FeatureColors.WaterDeep),
        badge = "9 Benefits"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            // Quick stat
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickStatItem("60%", "of your body\nis water")
                    QuickStatItem("75%", "of your brain\nis water")
                    QuickStatItem("83%", "of your lungs\nis water")
                }
            }

            Spacer(Modifier.height(8.dp))

            benefits.forEach { benefit ->
                BenefitRow(benefit)
            }
        }
    }
}

@Composable
private fun QuickStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = FeatureColors.WaterStart
        )
        Text(
            label,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 14.sp
        )
    }
}

data class BenefitItem(val icon: String, val title: String, val description: String)

@Composable
private fun BenefitRow(benefit: BenefitItem) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded }
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (expanded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    else Color.Transparent,
                    RoundedCornerShape(10.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = educationIcon(benefit.icon),
                    contentDescription = null,
                    tint = FeatureColors.WaterDeep,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                benefit.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(tween(200)) + fadeIn(tween(200)),
            exit = shrinkVertically(tween(150)) + fadeOut(tween(150))
        ) {
            Text(
                text = benefit.description,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 52.dp, end = 12.dp, bottom = 8.dp, top = 4.dp)
            )
        }
    }
}

// ─── Section 2: How Much Water ───────────────────────────────────────────────

@Composable
private fun HowMuchWaterSection() {
    ExpandableEducationCard(
        title = "How Much Water Do You Really Need?",
        icon = "🤔",
        gradientColors = listOf(HealthColors.Healthy, FeatureColors.StepsDeep),
        badge = "Myth Busted"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Myth busting
            MythBustCard(
                myth = "\"Everyone needs exactly 8 glasses (2L) of water per day\"",
                reality = "This is one of the most common health myths! The \"8×8 rule\" (eight 8-ounce glasses) has no scientific basis. " +
                        "Your actual water needs depend on your body weight, activity level, climate, health status, and diet. " +
                        "The original recommendation may have included water from all sources, including food."
            )

            // Actual guidelines
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = HealthColors.HealthyLight.copy(alpha = 0.75f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(stringResource(R.string.txt_general_guidelines), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FeatureColors.StepsDeep)
                    HorizontalDivider(color = FeatureColors.StepsDeep.copy(alpha = 0.16f))

                    GuidelineRow("📚", "National Academies reference", "Adequate intake includes food and beverages; healthy adults vary by sex, climate, activity, and diet")
                    GuidelineRow("⚖️", "Use the app as a starting point", "There is no universal weight-based prescription. Adjust for thirst and professional advice")
                    GuidelineRow("🚽", "Urine Test", "If your urine is pale yellow, you're likely well hydrated")
                    GuidelineRow("🥵", "Thirst Mechanism", "For most healthy adults, drinking when thirsty is reasonable")
                }
            }

            // Factors that increase needs
            Text(stringResource(R.string.txt_factors_that_increase_your_nee), fontWeight = FontWeight.Bold, fontSize = 14.sp)

            val factors = listOf(
                "🏃" to "Physical activity — you lose water through sweat",
                "☀️" to "Hot or humid weather — increases perspiration",
                "🏔️" to "High altitude — faster breathing and increased urination",
                "🤒" to "Illness — fever, vomiting, or diarrhea",
                "🤰" to "Pregnancy and breastfeeding",
                "☕" to "High caffeine or alcohol intake (mild diuretic effect)",
                "🧂" to "High sodium diet — your body needs more water to process salt",
                "✈️" to "Air travel — cabin air is very dry"
            )

            factors.forEach { (icon, text) ->
                Row(
                    modifier = Modifier.padding(start = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = educationIcon(icon),
                        contentDescription = null,
                        tint = FeatureColors.WaterDeep,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(cleanLegacyMarker(text), fontSize = 13.sp, lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Pro tip
            InfoTipCard(
                "💡 Pro Tip",
                "The best indicator of your hydration status is the color of your urine. " +
                        "Aim for pale straw color. If it's dark yellow or amber, drink more water. " +
                        "If it's completely clear, you might be overhydrating."
            )
        }
    }
}

@Composable
private fun MythBustCard(myth: String, reality: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = HealthColors.Caution,
                    modifier = Modifier.size(20.dp)
                )
                Text(stringResource(R.string.txt_myth), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = HealthColors.Caution)
            }
            Text(
                myth,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = HealthColors.Caution,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            HorizontalDivider(color = HealthColors.Caution.copy(alpha = 0.24f))

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = HealthColors.Healthy,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(stringResource(R.string.txt_reality), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = HealthColors.Healthy)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        reality,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun GuidelineRow(icon: String, title: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = educationIcon(icon),
            contentDescription = null,
            tint = FeatureColors.WaterDeep,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(detail, fontSize = 12.sp, lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─── Section 3: Overhydration ────────────────────────────────────────────────

@Composable
private fun OverhydrationSection() {
    ExpandableEducationCard(
        title = "Signs of Overhydration",
        icon = "⚠️",
        gradientColors = listOf(HealthColors.Caution, HealthColors.Danger),
        badge = "Important"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Key message
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = HealthColors.CautionLight.copy(alpha = 0.75f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = HealthColors.Caution,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        stringResource(R.string.txt_yes_you_can_drink_too_much_wat) +
                                "It's most common in endurance athletes and people who drink excessive amounts quickly.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = HealthColors.Caution
                    )
                }
            }

            // Hyponatremia
            Text(stringResource(R.string.txt_what_is_hyponatremia), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(
                stringResource(R.string.txt_hyponatremia_occurs_when_sodiu) +
                        "water too quickly. Sodium is essential for nerve and muscle function. When diluted, cells can swell, " +
                        "which is particularly dangerous for brain cells.",
                fontSize = 13.sp, lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            // Symptoms
            Text(stringResource(R.string.txt_warning_signs), fontWeight = FontWeight.Bold, fontSize = 14.sp)

            val symptoms = listOf(
                "🤢" to "Nausea and vomiting" to "Can occur with fluid or electrolyte imbalance",
                "🤕" to "Persistent headache" to "May have many causes; consider the wider context",
                "😵💫" to "Confusion or disorientation" to "Concerning symptom — seek urgent medical care",
                "💪" to "Muscle cramps or weakness" to "Can occur with fluid or electrolyte imbalance",
                "🫧" to "Bloating and swelling" to "Especially in hands and feet",
                "😴" to "Extreme fatigue" to "Beyond normal tiredness",
                "🚨" to "Seizures (severe cases)" to "Urgent medical care is needed"
            )

            symptoms.forEach { (iconLabel, description) ->
                val (icon, name) = iconLabel
                Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                            HealthColors.WarningLight.copy(alpha = 0.45f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = educationIcon(icon),
                        contentDescription = null,
                        tint = HealthColors.Caution,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text(description, fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }

            // When to be careful
            Text(stringResource(R.string.txt_when_to_be_careful), fontWeight = FontWeight.Bold, fontSize = 14.sp)

            val cautions = listOf(
                "🏃" to "During marathon/endurance events — don't force excessive drinking",
                "⏰" to "Avoid rapidly drinking large volumes; a fixed hourly amount is not a safety guarantee",
                "🧂" to "For prolonged heavy sweating, discuss a suitable fluid and electrolyte plan",
                "💊" to "Certain medications can affect water balance — consult your doctor",
                "📏" to "There is no universal safe daily fluid limit; follow any prescribed restriction"
            )

            cautions.forEach { (icon, text) ->
                Row(
                    modifier = Modifier.padding(start = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = educationIcon(icon),
                        contentDescription = null,
                        tint = HealthColors.Caution,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(cleanLegacyMarker(text), fontSize = 13.sp, lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }

            InfoTipCard(
                "💡 Balance Is Key",
                "Listen to your body. Drink when thirsty, sip throughout the day rather than gulping large amounts, " +
                        "and pay attention to your urine color. Pale yellow is the sweet spot."
            )
        }
    }
}

// ─── Section 4: Exercise & Hydration ─────────────────────────────────────────

@Composable
private fun ExerciseHydrationSection() {
    ExpandableEducationCard(
        title = "Hydration and Exercise",
        icon = "🏋️",
        gradientColors = listOf(HealthColors.Healthy, FeatureColors.WeightDeep),
        badge = "Active Guide"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Timeline visual
            Text(stringResource(R.string.txt_exercise_hydration_timeline), fontWeight = FontWeight.Bold, fontSize = 15.sp)

            // Before
            ExercisePhaseCard(
                phase = "BEFORE",
                marker = "🔜",
                timeframe = "2-3 hours before exercise",
                color = FeatureColors.WaterDeep,
                recommendations = listOf(
                    "💧 Start comfortably hydrated; needs vary with activity and conditions",
                    "⏰ Sip if needed rather than forcing a fixed amount",
                    "🚽 Allow time for bathroom visit before starting",
                    "⚡ Follow any medical fluid restriction or individual exercise plan"
                )
            )

            // During
            ExercisePhaseCard(
                phase = "DURING",
                marker = "🏃",
                timeframe = "During exercise",
                color = HealthColors.Healthy,
                recommendations = listOf(
                    "💧 Take regular opportunities to drink according to your needs",
                    "⏱️ For workouts over 60 min: consider sports drinks",
                    "🧂 For intense/long sessions: add electrolytes",
                    "📏 Avoid forcing fluid to meet an app goal; thirst and an individual plan provide context",
                    "🌡️ Heat can increase fluid needs; a fixed percentage is not suitable for everyone"
                )
            )

            // After
            ExercisePhaseCard(
                phase = "AFTER",
                marker = "🏁",
                timeframe = "Post-exercise recovery",
                color = HealthColors.Caution,
                recommendations = listOf(
                    "⚖️ Body-weight changes can help a qualified professional assess sweat losses",
                    "⏰ Rehydrate gradually over 2-4 hours",
                    "🧂 Fluid and electrolyte needs vary; follow your care plan if restrictions apply",
                    "🥛 Milk and chocolate milk are excellent post-workout beverages",
                    "🍌 Eat water-rich foods to support recovery"
                )
            )

            // Electrolytes section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = HealthColors.HealthyLight.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(stringResource(R.string.txt_electrolytes_exercise), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = FeatureColors.WeightDeep)
                    HorizontalDivider(color = FeatureColors.WeightDeep.copy(alpha = 0.16f))

                    Text(
                        stringResource(R.string.txt_for_workouts_lasting_over_60_m) +
                                "You need to replace electrolytes lost through sweat:",
                        fontSize = 13.sp, lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    val electrolytes = listOf(
                        "🧂" to "Sodium" to "Most important — lost in highest quantities through sweat",
                        "🍌" to "Potassium" to "Supports muscle function and prevents cramps",
                        "💚" to "Magnesium" to "Important for muscle relaxation and energy",
                        "🦴" to "Calcium" to "Supports muscle contraction"
                    )

                    electrolytes.forEach { (iconLabel, desc) ->
                        val (icon, name) = iconLabel
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = educationIcon(icon),
                                contentDescription = null,
                                tint = HealthColors.Healthy,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(desc, fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }

            // Sweat rate
            InfoTipCard(
                "💡 Know Your Sweat Rate",
                "Weigh yourself before and after exercise (without clothes). Each kg lost ≈ 1L of fluid loss. " +
                        "This helps you personalize your hydration strategy. Average sweat rate is 0.5-1.5L/hour."
            )
        }
    }
}

@Composable
private fun ExercisePhaseCard(
    phase: String,
    marker: String,
    timeframe: String,
    color: Color,
    recommendations: List<String>
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = educationIcon(marker),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            phase,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }
                Text(
                    timeframe,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            recommendations.forEach { rec ->
                Text(
                    cleanLegacyMarker(rec),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

// ─── Section 5: Special Needs ────────────────────────────────────────────────

@Composable
private fun SpecialNeedsSection() {
    ExpandableEducationCard(
        title = "Special Hydration Needs",
        icon = "🩺",
        gradientColors = listOf(HealthColors.Severe, FeatureColors.BpDeep),
        badge = "5 Categories"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                stringResource(R.string.txt_certain_conditions_and_situati) +
                        "Always consult your healthcare provider for personalized advice.",
                fontSize = 13.sp, lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Pregnancy
            SpecialNeedCard(
                marker = "🤰",
                title = "Pregnancy",
                color = HealthColors.Severe,
                additionalMl = "Individual guidance",
                details = listOf(
                    "This app does not add a fixed pregnancy fluid allowance",
                    "Water helps form amniotic fluid",
                    "Supports increased blood volume (up to 50% more)",
                    "Fluid needs during pregnancy should be discussed with your maternity team",
                    "Morning sickness? Try small, frequent sips",
                    "Discuss hydration, nausea or swelling with your maternity team"
                )
            )

            // Breastfeeding
            SpecialNeedCard(
                marker = "🤱",
                title = "Breastfeeding",
                color = FeatureColors.BpDeep,
                additionalMl = "Individual guidance",
                details = listOf(
                    "This app does not add a fixed breastfeeding fluid allowance",
                    "Breast milk is about 87% water",
                    "Drink a glass of water each time you nurse",
                    "Watch for signs of dehydration in yourself",
                    "Caffeine should be limited — it passes to breast milk"
                )
            )

            // Illness/Fever
            SpecialNeedCard(
                marker = "🤒",
                title = "Illness & Fever",
                color = HealthColors.Danger,
                additionalMl = "No fixed target",
                details = listOf(
                    "Fever, vomiting and diarrhea can change fluid needs",
                    "Use an appropriate oral rehydration product only as directed",
                    "Small, frequent sips may be easier to tolerate",
                    "Ask a clinician or pharmacist for tailored advice",
                    "Seek prompt care if you cannot keep fluids down or symptoms are severe"
                )
            )

            // High Altitude
            SpecialNeedCard(
                marker = "🏔️",
                title = "High Altitude",
                color = FeatureColors.WaterDeep,
                additionalMl = "Context varies",
                details = listOf(
                    "Altitude, humidity and activity can change fluid needs",
                    "Lower humidity and faster breathing increase water loss",
                    "Increased urination is common at altitude",
                    "Hydration does not prevent altitude illness; follow travel guidance",
                    "Avoid alcohol which worsens dehydration at altitude"
                )
            )

            // Hot Climate
            SpecialNeedCard(
                marker = "🌡️",
                title = "Hot & Humid Climate",
                color = HealthColors.Caution,
                additionalMl = "Context varies",
                details = listOf(
                    "Heat and sweat can change fluid needs substantially",
                    "Drink regularly and use thirst, breaks and local heat guidance",
                    "Take breaks in shade and monitor how you feel",
                    "If you feel unwell in heat, stop activity and seek help",
                    "Electrolytes may be useful for prolonged heavy sweating; avoid over-drinking"
                )
            )

            // Medical disclaimer
            InfoTipCard(
                "⚕️ Medical Note",
                "Some medical conditions (such as heart failure, kidney disease, or those on fluid restrictions) " +
                        "may require limited fluid intake. Always follow your healthcare provider's specific recommendations."
            )
        }
    }
}

@Composable
private fun SpecialNeedCard(
    marker: String,
    title: String,
    color: Color,
    additionalMl: String,
    details: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(color.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = educationIcon(marker),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(
                        additionalMl,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = color
                    )
                }
                val rotation by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    label = "expand_$title"
                )
                Icon(
                    Icons.Default.ExpandMore,
                    null,
                    modifier = Modifier.rotate(rotation),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(200)) + fadeIn(tween(200)),
                exit = shrinkVertically(tween(150)) + fadeOut(tween(150))
            ) {
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HorizontalDivider(color = color.copy(alpha = 0.15f))
                    Spacer(Modifier.height(4.dp))
                    details.forEach { detail ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                detail, fontSize = 13.sp, lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Reusable Components ─────────────────────────────────────────────────────

@Composable
private fun ExpandableEducationCard(
    title: String,
    icon: String,
    gradientColors: List<Color>,
    badge: String?,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header - always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        brush = Brush.linearGradient(gradientColors),
                        shape = RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp,
                            bottomStart = if (expanded) 0.dp else 16.dp,
                            bottomEnd = if (expanded) 0.dp else 16.dp
                        )
                    )
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        expanded = !expanded
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = educationIcon(icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    if (badge != null) {
                        Text(
                            badge,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                }
                val rotation by animateFloatAsState(
                    targetValue = if (expanded) 180f else 0f,
                    label = "expand_arrow"
                )
                Icon(
                    Icons.Default.ExpandMore,
                    null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.rotate(rotation)
                )
            }

            // Content - expandable
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
private fun InfoTipCard(title: String, text: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = FeatureColors.WaterStart.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = educationIcon(title),
                contentDescription = null,
                tint = FeatureColors.WaterDeep,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    cleanLegacyMarker(title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = FeatureColors.WaterDeep
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    cleanLegacyMarker(text),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MedicalDisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.Info,
                null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    stringResource(R.string.txt_medical_disclaimer),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    stringResource(R.string.txt_this_information_is_for_educat_3) +
                            "diagnosis, or treatment. Always consult your healthcare provider with questions about hydration " +
                            "and your specific health needs. Individual water requirements can vary significantly based on " +
                            "health conditions and medications.",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Maps legacy persisted/content markers to the app's stable icon language.
 * Marker strings remain in the content model so older saved data stays
 * compatible, but they are never rendered as platform-dependent emoji.
 */
private fun educationIcon(marker: String): ImageVector = when {
    marker.contains("💧") || marker.contains("🫧") -> Icons.Outlined.WaterDrop
    marker.contains("⚡") -> Icons.Outlined.Bolt
    marker.contains("✨") -> Icons.Outlined.AutoAwesome
    marker.contains("🫀") -> Icons.Outlined.MonitorHeart
    marker.contains("🧠") -> Icons.Outlined.Lightbulb
    marker.contains("🫁") || marker.contains("🛡️") -> Icons.Outlined.HealthAndSafety
    marker.contains("🦴") -> Icons.Outlined.Accessibility
    marker.contains("🌡") -> Icons.Outlined.Thermostat
    marker.contains("🫘") || marker.contains("🍌") || marker.contains("🥛") || marker.contains("🍉") || marker.contains("🍊") || marker.contains("🥗") || marker.contains("🍲") || marker.contains("☕") || marker.contains("🍺") -> Icons.Outlined.Restaurant
    marker.contains("🏃") || marker.contains("🏋️") || marker.contains("💪") -> Icons.Outlined.FitnessCenter
    marker.contains("☀️") || marker.contains("🌅") -> Icons.Outlined.WbSunny
    marker.contains("🏔") || marker.contains("✈️") -> Icons.Outlined.DirectionsWalk
    marker.contains("🤒") || marker.contains("🤢") || marker.contains("🤰") || marker.contains("🤱") || marker.contains("🩺") -> Icons.Outlined.HealthAndSafety
    marker.contains("⏰") || marker.contains("⏱️") || marker.contains("😴") -> Icons.Outlined.Schedule
    marker.contains("💡") -> Icons.Outlined.Lightbulb
    marker.contains("⚠️") || marker.contains("🚨") || marker.contains("🛑") -> Icons.Outlined.Warning
    marker.contains("🔬") -> Icons.Outlined.Science
    marker.contains("🔜") || marker.contains("🏁") || marker.contains("📏") || marker.contains("⚖️") -> Icons.Outlined.Flag
    else -> Icons.Outlined.Info
}

private val LeadingEducationMarker = Regex("^[\\p{So}\\p{Sk}\\p{M}\\p{Cf}\\s]+")

private fun cleanLegacyMarker(value: String): String =
    value.replaceFirst(LeadingEducationMarker, "").trim()
