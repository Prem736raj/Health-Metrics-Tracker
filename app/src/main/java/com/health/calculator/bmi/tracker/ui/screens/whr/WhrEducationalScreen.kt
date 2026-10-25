package com.health.calculator.bmi.tracker.ui.screens.whr

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.data.model.WhrGuideData
import com.health.calculator.bmi.tracker.ui.theme.CalculatorColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhrEducationalScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.txt_learn_about_whr), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            EducationalHeader()

            // Section 1: What is WHR
            WhatIsWhrSection()

            // Section 2: WHR vs BMI
            WhrVsBmiSection()

            // Section 3: Central Obesity & Disease Risk
            CentralObesityRiskSection()

            // Section 4: How to Reduce Waist Circumference
            ReduceWaistSection()

            // Section 5: Correct Measurement Technique
            MeasurementTechniqueSection()

            // Medical Disclaimer
            MedicalDisclaimerCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EducationalHeader() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.txt_whr_knowledge_center),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.txt_everything_you_need_to_know_ab_1),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// SECTION 1: What is WHR
// ═══════════════════════════════════════════════════════════

@Composable
private fun WhatIsWhrSection() {
    ExpandableEducationalSection(
        icon = Icons.Outlined.HelpOutline,
        title = "What is Waist-to-Hip Ratio?",
        subtitle = "Understanding the basics of WHR",
        accentColor = MaterialTheme.colorScheme.primary,
        initialExpanded = true
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoParagraph(
                "Waist-to-Hip Ratio (WHR) is a simple measurement that compares the circumference of your waist to the circumference of your hips. It's calculated by dividing your waist measurement by your hip measurement."
            )

            FormulaCard(
                formula = "WHR = Waist Circumference ÷ Hip Circumference",
                example = "Example: 80 cm waist ÷ 100 cm hip = 0.80 WHR"
            )

            SectionSubheading("Why does WHR matter?")

            InfoParagraph(
                "WHR describes body proportions and can add context alongside other measurements. It does not measure visceral fat or diagnose a health condition."
            )

            KeyPointCard(
                icon = Icons.Outlined.Science,
                title = "Research Finding",
                text = "Research finds that waist and hip measures can add population-level context beyond BMI. Associations do not predict an individual's outcome, and the measures should not be used as a diagnosis."
            )

            SectionSubheading("Reference classifications")

            WhrReferenceTable()

            InfoParagraph(
                "A WHR below a reference point does not prove that abdominal fat is absent. People with similar measurements can have different health contexts."
            )
        }
    }
}

@Composable
private fun WhrReferenceTable() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    "Reference context",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.txt_male),
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    stringResource(R.string.txt_female),
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            TableRow("Below reference", "< 0.90", "< 0.80", HealthColors.Healthy)
            TableRow("Intermediate", "0.90 – 0.99", "0.80 – 0.84", HealthColors.Warning)
            TableRow("At or above reference", "≥ 1.00", "≥ 0.85", HealthColors.Danger)
        }
    }
}

@Composable
private fun TableRow(
    label: String,
    maleValue: String,
    femaleValue: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = AppColors.text(color)
            )
        }
        Text(
            maleValue,
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Text(
            femaleValue,
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

// ═══════════════════════════════════════════════════════════
// SECTION 2: WHR vs BMI
// ═══════════════════════════════════════════════════════════

@Composable
private fun WhrVsBmiSection() {
    ExpandableEducationalSection(
        icon = Icons.Outlined.CompareArrows,
        title = "WHR vs BMI",
        subtitle = "How two simple measures add different context",
        accentColor = HealthColors.Good
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoParagraph(
                "BMI and WHR describe different aspects of body size. BMI uses height and weight, while WHR describes waist-to-hip proportions. Looking at both may add context, but neither measure is a diagnosis or a complete health assessment."
            )

            // Comparison cards side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ComparisonMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "BMI",
                    icon = Icons.Outlined.Calculate,
                    color = HealthColors.Good,
                    points = listOf(
                        "Overall body fat estimate",
                        "Based on height & weight",
                        "Simple and widely used",
                        "Cannot distinguish fat vs muscle",
                        "Does not describe fat distribution"
                    )
                )
                ComparisonMetricCard(
                    modifier = Modifier.weight(1f),
                    title = "WHR",
                    icon = Icons.Outlined.Straighten,
                    color = CalculatorColors.WaistToHip,
                    points = listOf(
                        "Fat distribution pattern",
                        "Based on waist & hip",
                        "Adds body-proportion context",
                        "Does not identify visceral fat",
                        "Complements, but does not replace, other data"
                    )
                )
            }

            SectionSubheading("When WHR is more informative")

            BulletList(
                items = listOf(
                    Pair("Normal BMI but higher WHR", "A different WHR reference category may prompt a broader conversation about measurements and goals; it does not establish a condition."),
                    Pair("Athletes and muscular individuals", "BMI can be less representative for some muscular people. WHR adds proportion context but cannot determine whether weight is healthy."),
                    Pair("Older adults", "Body composition can change with age even when BMI changes little. WHR is one optional measure to track alongside strength and wellbeing."),
                    Pair("Post-menopausal women", "Body proportions may change over time. Consistent measurements can help describe trends without assigning a diagnosis.")
                )
            )

            KeyPointCard(
                icon = Icons.Outlined.Lightbulb,
                title = "Best Practice",
                text = "BMI and WHR describe different aspects of body size and proportion. Use both as conversation starters, not as standalone risk predictions."
            )

            SectionSubheading("A four-quadrant context view")

            QuadrantGrid()
        }
    }
}

@Composable
private fun ComparisonMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    color: Color,
    points: List<String>
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.06f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(color)
                )
            }
            points.forEach { point ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(stringResource(R.string.txt_text_placeholder_3), fontSize = 12.sp, color = AppColors.text(color))
                    Text(
                        point,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun QuadrantGrid() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Combined BMI + WHR context",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuadrantCell(
                    Modifier.weight(1f),
                    "Normal BMI\nNormal WHR",
                    "Lower reference context",
                    HealthColors.Healthy
                )
                QuadrantCell(
                    Modifier.weight(1f),
                    "Normal BMI\nHigh WHR",
                    "More context needed",
                    HealthColors.Warning
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuadrantCell(
                    Modifier.weight(1f),
                    "High BMI\nNormal WHR",
                    "More context needed",
                    HealthColors.Warning
                )
                QuadrantCell(
                    Modifier.weight(1f),
                    "High BMI\nHigh WHR",
                    "Higher reference context",
                    HealthColors.Danger
                )
            }
        }
    }
}

@Composable
private fun QuadrantCell(
    modifier: Modifier,
    label: String,
    interpretation: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.08f))
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                interpretation,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.text(color),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
// SECTION 3: Central Obesity & Disease Risk
// ═══════════════════════════════════════════════════════════

@Composable
private fun CentralObesityRiskSection() {
    ExpandableEducationalSection(
        icon = Icons.Outlined.MonitorHeart,
        title = "Central fat distribution and health context",
        subtitle = "What population research can and cannot tell you",
        accentColor = HealthColors.Danger
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoParagraph(
                "Central fat distribution has been associated with cardiometabolic outcomes in population studies. WHR alone cannot identify visceral fat, diagnose disease, or estimate an individual's personal risk."
            )

            SectionSubheading("Cardiovascular context")

            DiseaseRiskCard(
                icon = Icons.Outlined.FavoriteBorder,
                disease = "Heart Disease & Stroke",
                riskInfo = "Higher WHR has been associated with cardiovascular outcomes in population studies; it cannot predict an individual's heart-attack or stroke risk.",
                source = "INTERHEART Study (population research; association only)",
                color = HealthColors.Danger,
                details = listOf(
                    "Central fat distribution can occur alongside changes in lipids and blood pressure; an individual assessment needs more than WHR.",
                    "Research associations do not establish that WHR causes arterial inflammation.",
                    "Population estimates vary by study and do not translate to a personal percentage.",
                    "People with similar body-size measures can have different health contexts."
                )
            )

            SectionSubheading("Metabolic context")

            DiseaseRiskCard(
                icon = Icons.Outlined.Info,
                disease = "Type 2 Diabetes",
                riskInfo = "Higher central adiposity has been associated with type 2 diabetes in population studies; WHR is not a diagnostic test.",
                source = "WHO Global Report on Diabetes (contextual background)",
                color = HealthColors.Warning,
                details = listOf(
                    "Waist circumference can add context alongside BMI and other measurements.",
                    "WHR cannot show how insulin, glucose, or other clinical markers are changing.",
                    "Population associations vary with age, sex, ethnicity, and study design.",
                    "Discuss concerning measurements or symptoms with a qualified clinician."
                )
            )

            DiseaseRiskCard(
                icon = Icons.Outlined.Analytics,
                disease = "Metabolic Syndrome",
                riskInfo = "A larger waist is one criterion used in some metabolic-syndrome definitions; only a clinician can assess the full set of criteria.",
                source = "International Diabetes Federation (definition context)",
                color = CalculatorColors.MetabolicSyndrome,
                details = listOf(
                    "Definitions combine waist, blood pressure, glucose, triglycerides, and HDL; thresholds vary by guideline.",
                    "A calculator cannot diagnose metabolic syndrome or estimate future disease.",
                    "Population prevalence and risk estimates vary across countries and study designs.",
                    "If you are concerned, discuss the full set of measurements with a clinician."
                )
            )

            SectionSubheading("Other areas researchers study")

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                MiniRiskItem(Icons.Outlined.Info, "Cognition", "Some studies examine body-size measures in midlife and later cognitive outcomes; this does not predict an individual's dementia risk.")
                MiniRiskItem(Icons.Outlined.Info, "Sleep and breathing", "Body-size measures may be relevant context; they cannot screen for sleep apnea.")
                MiniRiskItem(Icons.Outlined.Info, "Long-term health", "Associations with long-term outcomes do not determine an individual's cancer risk.")
                MiniRiskItem(Icons.Outlined.FavoriteBorder, "Blood pressure", "Central adiposity and blood pressure can be associated; WHR cannot show why a reading is high.")
                MiniRiskItem(Icons.Outlined.Info, "Joint comfort", "Extra body mass can increase mechanical load on some joints; effects vary between people.")
                MiniRiskItem(Icons.Outlined.Info, "Mood", "Research reports associations between body size and mood; this is not a causal conclusion.")
            }

            KeyPointCard(
                icon = Icons.Outlined.Info,
                title = "Key Statistic",
                text = "Large studies have found associations between body-size measures and cardiovascular outcomes. Percentages from a population study should not be interpreted as an individual's preventable-risk score."
            )
        }
    }
}

@Composable
private fun DiseaseRiskCard(
    icon: ImageVector,
    disease: String,
    riskInfo: String,
    source: String,
    color: Color,
    details: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.06f))
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        disease,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                riskInfo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )

            Text(
                "Source: $source",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    HorizontalDivider(color = color.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(4.dp))
                    details.forEach { detail ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(stringResource(R.string.txt_text_placeholder_3), color = AppColors.text(color), fontSize = 12.sp)
                            Text(
                                detail,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniRiskItem(icon: ImageVector, title: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                RoundedCornerShape(8.dp)
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                detail,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
// SECTION 4: How to Reduce Waist
// ═══════════════════════════════════════════════════════════

@Composable
private fun ReduceWaistSection() {
    ExpandableEducationalSection(
        icon = Icons.Outlined.FitnessCenter,
        title = "Ways to support a healthier waistline",
        subtitle = "Practical, sustainable habits",
        accentColor = HealthColors.Healthy
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoParagraph(
                "Waist circumference may change as part of overall habits; no routine can target visceral fat in one area. Consider activity, nutrition, sleep, and stress support that fit your needs."
            )

            SectionSubheading("Exercise strategies")

            NumberedStrategy(
                number = 1,
                title = "Aerobic Exercise (Most Effective)",
                description = "Brisk walking, jogging, cycling or swimming can support fitness. Build toward activity that feels manageable and fits your abilities.",
                keyFact = "Use a weekly activity target as a flexible guide, not a guaranteed waist change",
                color = HealthColors.Healthy
            )

            NumberedStrategy(
                number = 2,
                title = "High-Intensity Interval Training (HIIT)",
                description = "Alternating between brief harder efforts and recovery periods can be an option for people who enjoy it and can exercise safely. It is not required for progress.",
                keyFact = "Short intervals are optional; response and tolerance vary",
                color = HealthColors.Healthy
            )

            NumberedStrategy(
                number = 3,
                title = "Resistance Training",
                description = "Strength exercises can support muscle function and everyday activity. Start gradually and choose movements that match your experience and abilities.",
                keyFact = "Combining different activity types may support overall fitness; there is no guaranteed waist change",
                color = HealthColors.Healthy
            )

            SectionSubheading("Dietary approaches")

            NumberedStrategy(
                number = 4,
                title = "Increase Soluble Fiber",
                description = "Oats, beans, lentils, fruit and vegetables can add fiber to a varied eating pattern. Increase gradually and consider your tolerance.",
                keyFact = "Fiber supports overall nutrition; it does not guarantee a change in waist or visceral fat",
                color = HealthColors.Warning
            )

            NumberedStrategy(
                number = 5,
                title = "Reduce Added Sugars & Refined Carbs",
                description = "If desired, reduce sugary drinks and highly refined snacks in a way that still supports adequate nutrition.",
                keyFact = "Results vary; avoid promises tied to one food change",
                color = HealthColors.Warning
            )

            NumberedStrategy(
                number = 6,
                title = "Eat More Protein",
                description = "Include protein from varied foods. A percentage target is optional planning context, not a prescription.",
                keyFact = "Individual needs and responses vary",
                color = HealthColors.Warning
            )

            NumberedStrategy(
                number = 7,
                title = "Mediterranean Diet Pattern",
                description = "A Mediterranean-style pattern can be a flexible way to include plants, whole grains, fish or alternatives and unsaturated fats.",
                keyFact = "Choose a sustainable pattern that fits your culture, budget and health needs",
                color = HealthColors.Warning
            )

            SectionSubheading("Lifestyle changes")

            NumberedStrategy(
                number = 8,
                title = "Prioritize Sleep (7-9 Hours)",
                description = "Consistent, restorative sleep supports wellbeing and daily activity; sleep needs vary.",
                keyFact = "Sleep needs vary; aim for a routine that leaves you rested when possible",
                color = HealthColors.Good
            )

            NumberedStrategy(
                number = 9,
                title = "Manage Stress",
                description = "Stress support such as breathing exercises, social connection or enjoyable relaxation can help wellbeing.",
                keyFact = "There is no guaranteed waist change from one stress-management method",
                color = HealthColors.Good
            )

            NumberedStrategy(
                number = 10,
                title = "Limit Alcohol Consumption",
                description = "If you drink alcohol, consider lower-risk guidance and alcohol-free days. Your local recommendations and personal health context matter.",
                keyFact = "Reducing alcohol may support some people's goals; individual responses vary",
                color = HealthColors.Good
            )

            SectionSubheading("What to expect over time")

            TimelineCard()

            KeyPointCard(
                icon = Icons.Outlined.Star,
                title = "Most Important",
                text = "Consistency beats intensity. A sustainable routine of moderate exercise, balanced diet, good sleep, and stress management will produce lasting results. Crash diets and extreme exercise often backfire, as the body protects visceral fat during extreme restriction."
            )
        }
    }
}

@Composable
private fun NumberedStrategy(
    number: Int,
    title: String,
    description: String,
    keyFact: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.05f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.15f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "$number",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.text(color)
                        )
                    }
                }
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.08f)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(stringResource(R.string.txt_text_placeholder_9), fontSize = 12.sp)
                    Text(
                        keyFact,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 14.sp,
                        color = AppColors.text(color)
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                stringResource(R.string.txt_expected_timeline_for_waist_re),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            TimelineItem(
                period = "Weeks 1-2",
                description = "You may be building a routine; energy, comfort, and consistency can change before measurements do.",
                icon = Icons.Outlined.Schedule
            )
            TimelineItem(
                period = "Weeks 3-4",
                description = "If you measure consistently, you may notice a trend; day-to-day values naturally fluctuate.",
                icon = Icons.Outlined.Straighten
            )
            TimelineItem(
                period = "Months 2-3",
                description = "Sustainable changes can become easier to maintain; the size and pace of change vary.",
                icon = Icons.Outlined.Timeline
            )
            TimelineItem(
                period = "Months 4-6",
                description = "Continue reviewing trends rather than single readings; plateaus are common.",
                icon = Icons.Outlined.ShowChart
            )
            TimelineItem(
                period = "6-12 Months",
                description = "Use measurements alongside how you feel and other goals; seek guidance if changes are unexpected.",
                icon = Icons.Outlined.CalendarMonth
            )

            Text(
                stringResource(R.string.txt_note_results_vary_based_on_sta),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun TimelineItem(period: String, description: String, icon: ImageVector) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            )
        }
        Column {
            Text(
                period,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
// SECTION 5: Correct Measurement Technique
// ═══════════════════════════════════════════════════════════

@Composable
private fun MeasurementTechniqueSection() {
    ExpandableEducationalSection(
        icon = Icons.Outlined.Straighten,
        title = "Correct Measurement Technique",
        subtitle = "Step-by-step guide with do's and don'ts",
        accentColor = CalculatorColors.WaistToHip
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoParagraph(
                "Accurate and consistent measurements make trends easier to interpret. Small differences in technique can change a ratio, so use the same landmark, tape position, and breathing pattern each time."
            )

            SectionSubheading("Step-by-step guide")

            MeasurementStep(
                step = 1,
                title = "Prepare",
                instructions = listOf(
                    "Use a flexible, non-stretchy measuring tape (cloth or fiberglass)",
                    "Measure in light clothing or directly on skin",
                    "Remove any belt or tight waistband",
                    "Stand in front of a mirror if possible"
                )
            )

            MeasurementStep(
                step = 2,
                title = "Position Yourself",
                instructions = listOf(
                    "Stand upright with feet hip-width apart",
                    "Arms relaxed at your sides",
                    "Distribute weight evenly on both feet",
                    "Don't lean to one side"
                )
            )

            MeasurementStep(
                step = 3,
                title = "Find Waist Landmark",
                instructions = listOf(
                    "Find the lowest palpable rib and the top of your hip bone (iliac crest)",
                    WhrGuideData.waistLandmark,
                    "Keep the tape horizontal and snug without compressing the skin",
                    "Take the reading after a normal exhale"
                )
            )

            MeasurementStep(
                step = 4,
                title = "Measure Waist",
                instructions = listOf(
                    "Wrap the tape around your waist at the identified point",
                    "Ensure the tape is snug but not compressing the skin",
                    "The tape must be level and parallel to the floor all the way around",
                    "Breathe normally and take the reading after a normal exhale",
                    "Do not hold your breath or pull your stomach in"
                )
            )

            MeasurementStep(
                step = 5,
                title = "Find Hip Landmark",
                instructions = listOf(
                    WhrGuideData.hipLandmark,
                    "Stand sideways in front of a mirror to identify the widest point",
                    "This is typically at the level of the greater trochanter (top of the thigh bone)"
                )
            )

            MeasurementStep(
                step = 6,
                title = "Measure Hips",
                instructions = listOf(
                    "Wrap the tape around the widest part of the buttocks",
                    "Keep the tape parallel to the floor",
                    "The tape should be snug without indenting the skin",
                    "Stand naturally — don't clench or tighten your glutes"
                )
            )

            MeasurementStep(
                step = 7,
                title = "Record & Repeat",
                instructions = listOf(
                    "Read to the nearest 0.5 cm or 0.25 inch",
                    "Take 2-3 measurements for each (waist and hip)",
                    "Use the average of the readings",
                    "If readings vary by >1 cm, remeasure until consistent"
                )
            )

            SectionSubheading("Best time to measure")

            BestTimingCard()

            SectionSubheading("Do's and don'ts")

            DosAndDontsCard()

            SectionSubheading("Common mistakes")

            CommonMistakesList()

            SectionSubheading("Consistency tips")

            ConsistencyTipsCard()
        }
    }
}

@Composable
private fun MeasurementStep(
    step: Int,
    title: String,
    instructions: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "$step",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            instructions.forEach { instruction ->
                Row(
                    modifier = Modifier.padding(start = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(stringResource(R.string.txt_text_placeholder_3), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    Text(
                        instruction,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}

@Composable
private fun BestTimingCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(HealthColors.Good.copy(alpha = 0.06f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = HealthColors.Good,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    stringResource(R.string.txt_best_first_thing_in_the_mornin),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "• After using the bathroom\n• Before eating or drinking\n• Before exercising\n• In minimal clothing\n• At the same time each measurement day",
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            )
            Text(
                stringResource(R.string.txt_waist_measurements_can_vary_by),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun DosAndDontsCard() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(HealthColors.Healthy.copy(alpha = 0.06f))
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    stringResource(R.string.txt_do),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(HealthColors.Healthy)
                )
                DoItem("Measure at same time")
                DoItem("Use same tape measure")
                DoItem("Stand relaxed & straight")
                DoItem("Take multiple readings")
                DoItem("Measure on bare skin")
                DoItem("Keep tape level")
                DoItem("Breathe normally")
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(HealthColors.Danger.copy(alpha = 0.06f))
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    stringResource(R.string.txt_don_t),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(HealthColors.Danger)
                )
                DontItem("Suck in your stomach")
                DontItem("Hold your breath")
                DontItem("Measure over thick clothes")
                DontItem("Pull tape too tight")
                DontItem("Measure after big meal")
                DontItem("Let tape twist or tilt")
                DontItem("Rush the measurement")
            }
        }
    }
}

@Composable
private fun DoItem(text: String) {
    Text(
        "• $text",
        style = MaterialTheme.typography.bodySmall,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    )
}

@Composable
private fun DontItem(text: String) {
    Text(
        "• $text",
        style = MaterialTheme.typography.bodySmall,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    )
}

@Composable
private fun CommonMistakesList() {
    val mistakes = listOf(
        Pair("Measuring at the wrong waist point", "Use the same anatomical landmark each time: midway between the lowest palpable rib and the top of the hip bone (iliac crest)."),
        Pair("Uneven tape placement", "If the tape is higher in the back than the front (or vice versa), your reading will be inaccurate. Always check in a mirror or ask someone to help."),
        Pair("Pulling the tape too tight", "Compressing the skin gives a falsely low reading. The tape should touch the skin all around without indenting it."),
        Pair("Measuring after eating", "Meals can temporarily change waist circumference. When possible, measure before eating and use the same routine each time."),
        Pair("Tensing muscles while measuring", "Flexing your abs or glutes during measurement gives inaccurate results. Stay completely relaxed."),
        Pair("Using a stretched-out tape", "Old fabric tapes can stretch over time. Replace your tape annually or use a fiberglass tape.")
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        mistakes.forEachIndexed { index, (title, description) ->
            var expanded by remember { mutableStateOf(false) }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.surface(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.08f))
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { expanded = !expanded }
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            description,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                            modifier = Modifier.padding(top = 6.dp, start = 22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsistencyTipsCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val tips = listOf(
                Icons.Outlined.CalendarMonth to "Measure on the same day each week (e.g., every Monday morning)",
                Icons.Outlined.Schedule to "Always measure at the same time of day",
                Icons.Outlined.Straighten to "Use the same tape measure every time",
                Icons.Outlined.Info to "Stand in the same spot (use a bathroom mirror for reference)",
                Icons.Outlined.EditNote to "Record immediately — don't rely on memory",
                Icons.Outlined.Timeline to "Take 2-3 readings and use the average",
                Icons.Outlined.Timeline to "Track in this app for automatic trend analysis",
                Icons.Outlined.Info to "Optional: take progress photos monthly from the same angle"
            )

            tips.forEach { (icon, tip) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        tip,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// REUSABLE COMPONENTS
// ═══════════════════════════════════════════════════════════

@Composable
private fun ExpandableEducationalSection(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    initialExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initialExpanded) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 2.dp else 0.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        fontSize = 12.sp
                    )
                }

                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = accentColor
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun InfoParagraph(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall,
        lineHeight = 20.sp,
        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
    )
}

@Composable
private fun SectionSubheading(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun FormulaCard(formula: String, example: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                formula,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                example,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun KeyPointCard(icon: ImageVector, title: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(MaterialTheme.colorScheme.tertiary)
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun BulletList(items: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { (title, description) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Column {
                    Text(
                        title,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = AppColors.text(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    )
                }
            }
        }
    }
}

@Composable
private fun MedicalDisclaimerCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Outlined.MedicalServices,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    stringResource(R.string.txt_medical_disclaimer),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(MaterialTheme.colorScheme.error)
                )
                Text(
                    stringResource(R.string.txt_the_information_provided_here_),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}
