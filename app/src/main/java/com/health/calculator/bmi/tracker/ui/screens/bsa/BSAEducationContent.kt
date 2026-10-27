package com.health.calculator.bmi.tracker.ui.screens.bsa

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.*

/**
 * Keeps legacy educational data readable while presenting one consistent
 * vector icon language in the UI. The old emoji values are not rendered.
 */
private fun bsaEducationIcon(legacyEmoji: String): ImageVector = when (legacyEmoji) {
    "📐", "📏" -> Icons.Outlined.Straighten
    "🧮", "⚡" -> Icons.Outlined.Calculate
    "🎯", "🏆" -> Icons.Outlined.Flag
    "⚖️" -> Icons.Outlined.CompareArrows
    "👶", "🧒", "🧑", "👤" -> Icons.Outlined.Person
    "🏥" -> Icons.Outlined.LocalHospital
    "🔬" -> Icons.Outlined.Science
    "💡" -> Icons.Outlined.Lightbulb
    "🤔", "🤷" -> Icons.Outlined.HelpOutline
    else -> Icons.Outlined.Info
}

@Composable
fun BSAEducationScreen(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.txt_learn_about_bsa),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = stringResource(R.string.txt_everything_you_need_to_know_ab),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Section 1
        BSAEducationSection(
            icon = "📐",
            title = "What is Body Surface Area?",
            accentColor = HealthBlue
        ) {
            WhatIsBSAContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 2
        BSAEducationSection(
            icon = "🧮",
            title = "Why So Many Formulas?",
            accentColor = HealthOrange
        ) {
            WhySoManyFormulasContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 3
        BSAEducationSection(
            icon = "🎯",
            title = "Which Formula Should I Use?",
            accentColor = HealthGreen
        ) {
            WhichFormulaContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 4
        BSAEducationSection(
            icon = "⚖️",
            title = "BSA vs BMI",
            accentColor = HealthTeal
        ) {
            BSAvsBMIContent()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Disclaimer
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_this_educational_content_is_fo_1),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BSAEducationSection(
    icon: String,
    title: String,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = !expanded
            }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = bsaEducationIcon(icon),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = accentColor
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(350)) + fadeIn(tween(350)),
                exit = shrinkVertically(tween(250)) + fadeOut(tween(200))
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    HorizontalDivider(
                        color = accentColor.copy(alpha = 0.15f),
                        modifier = Modifier.padding(bottom = 14.dp)
                    )
                    content()
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// Section 1: What is BSA?
// ─────────────────────────────────────────

@Composable
private fun WhatIsBSAContent() {
    Column {
        // Relatable analogy
        AnalygyCard(
            emoji = "🛏️",
            text = "Imagine wrapping your entire body in a thin sheet — from the top of your head to the tips of your toes, including your arms, legs, and everything in between. The total area of that sheet is your Body Surface Area (BSA)."
        )

        Spacer(modifier = Modifier.height(14.dp))

        Paragraph(
            text = "Body Surface Area is the total area of the external surface of the human body, measured in square meters (m²). While it might seem like a simple concept, accurately measuring or estimating this area is surprisingly complex because of the irregular shape of the human body."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Key facts
        Text(
            text = stringResource(R.string.txt_key_facts_about_bsa),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = AppColors.text(HealthBlue),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FactItem(
            emoji = "👤",
            title = "Average Adult BSA",
            description = "An average adult male has a BSA of about 1.9 m², while an average adult female is about 1.6 m². A newborn baby has a BSA of only about 0.25 m²."
        )
        FactItem(
            emoji = "📏",
            title = "Based on Weight & Height",
            description = "Since directly measuring body surface is impractical, BSA is calculated from your weight and height using mathematical formulas."
        )
        FactItem(
            emoji = "🏥",
            title = "Used in clinical context",
            description = "Clinicians may use BSA as one input in specialized decisions. This app provides an informational estimate and never supplies medication or treatment doses."
        )
        FactItem(
            emoji = "🔬",
            title = "Adds context beyond weight",
            description = "BSA combines height and weight into a body-size estimate; different equations can vary and none measures health on its own."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Visual comparison
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(HealthBlue.copy(alpha = 0.06f))
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.txt_to_put_it_in_perspective),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(HealthBlue)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PerspectiveItem(emoji = "👶", label = "Baby", value = "~0.25 m²", description = "About the size\nof a pillow")
                    PerspectiveItem(emoji = "🧒", label = "Child (10yr)", value = "~1.1 m²", description = "About the size\nof a desk")
                    PerspectiveItem(emoji = "🧑", label = "Adult", value = "~1.7 m²", description = "About the size\nof a door")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Paragraph(
            text = "Fun fact: Your skin is actually your largest organ! It performs vital functions like temperature regulation, protection from infection, and sensation. Understanding BSA helps medical professionals work with this important organ system."
        )
    }
}

@Composable
private fun PerspectiveItem(emoji: String, label: String, value: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(95.dp)
    ) {
        Icon(
            imageVector = bsaEducationIcon(emoji),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.ExtraBold,
            color = AppColors.text(HealthBlue)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            fontSize = 12.sp
        )
    }
}

// ─────────────────────────────────────────
// Section 2: Why So Many Formulas?
// ─────────────────────────────────────────

@Composable
private fun WhySoManyFormulasContent() {
    Column {
        Paragraph(
            text = "Over the past century, scientists have developed numerous formulas to estimate BSA. Each was created in a different era, using different populations and measurement techniques. Here's the fascinating story:"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Timeline
        Text(
            text = stringResource(R.string.txt_a_century_of_bsa_research),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = AppColors.text(HealthOrange),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TimelineItem(
            year = "1916",
            formula = "Du Bois & Du Bois",
            highlight = "Historical reference",
            description = "Two brothers used a coating method on a small group to create an early practical BSA formula. It remains widely cited, but the original sample size and population are important limitations.",
            color = HealthBlue
        )

        TimelineItem(
            year = "1925",
            formula = "Takahira",
            highlight = "Asian Perspective",
            description = "Developed using Japanese population data and an early recognition that body proportions vary between populations. Population fit does not guarantee individual accuracy.",
            color = HealthTeal
        )

        TimelineItem(
            year = "1935",
            formula = "Boyd",
            highlight = "Expanded Data",
            description = "Used a larger dataset than Du Bois and introduced a more complex mathematical approach. Validation still depends on the population and setting.",
            color = HealthYellow
        )

        TimelineItem(
            year = "1968",
            formula = "Fujimoto",
            highlight = "Japanese Population",
            description = "Derived from Japanese population measurements. It is included for comparison; a population-specific equation does not guarantee better accuracy for every person.",
            color = HealthTeal
        )

        TimelineItem(
            year = "1970",
            formula = "Gehan & George",
            highlight = "Large Dataset",
            description = "Based on 401 subjects, a large dataset for its time. It provides another reference equation rather than a universally best method.",
            color = HealthGreen
        )

        TimelineItem(
            year = "1978",
            formula = "Haycock",
            highlight = "Pediatric Focus",
            description = "Developed from pediatric measurements; pediatric interpretation still needs clinical context.",
            color = HealthOrange
        )

        TimelineItem(
            year = "1987",
            formula = "Mosteller",
            highlight = "Simplicity",
            description = "Created an easy-to-compute square-root formula. It can be useful for a quick estimate, while different equations may produce different results.",
            color = HealthGreen
        )

        TimelineItem(
            year = "2000",
            formula = "Shuter & Aslani",
            highlight = "CT-Based",
            description = "Derived from body-surface measurements. It is included as a later reference equation; the method requested by a qualified professional should take priority in care.",
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Why no single formula is perfect
        HighlightCard(
            emoji = "🤔",
            title = "Why Can't We Have Just One Perfect Formula?",
            color = HealthOrange,
            content = "No single formula is guaranteed to fit every person because:\n\n• Body proportions vary between individuals\n• Populations have different average body shapes\n• Children have different proportions than adults\n• People at the extremes of body size may differ from the source sample\n• Each formula was derived from a specific population and method\n• Measurement and validation approaches have changed over time\n\nCompare the method as well as the number, and follow the equation requested by a qualified professional when BSA is used in care."
        )

        Spacer(modifier = Modifier.height(10.dp))

        QuickStatRow(
            stats = listOf(
                Pair("Several", "reference equations"),
                Pair("Different", "source populations"),
                Pair("Record", "the method used")
            )
        )
    }
}

@Composable
private fun TimelineItem(
    year: String,
    formula: String,
    highlight: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Timeline connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(44.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(60.dp)
                    .background(color.copy(alpha = 0.2f))
            )
        }

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = color.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = year,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppColors.text(color),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formula,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = highlight,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

// ─────────────────────────────────────────
// Section 3: Which Formula Should I Use?
// ─────────────────────────────────────────

@Composable
private fun WhichFormulaContent() {
    Column {
        Paragraph(
            text = "Choosing the right formula depends on who you are and what the calculation is for. Here's a simple guide:"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Decision guide cards
        FormulaGuideCard(
            emoji = "🏥",
            scenario = "General Adult Use",
            recommendation = "Du Bois & Du Bois",
            reason = "A commonly cited historical equation. A professional may use a different method for a specific context; treat this app result as an estimate.",
            color = HealthBlue,
            tag = "COMMON OPTION"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormulaGuideCard(
            emoji = "⚡",
            scenario = "Quick Calculation",
            recommendation = "Mosteller",
            reason = "A simple formula — multiply weight by height, divide by 3600, and take the square root. It is convenient for a quick estimate; different equations can produce different results.",
            color = HealthGreen,
            tag = "EASIEST"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormulaGuideCard(
            emoji = "👶",
            scenario = "Children & Infants",
            recommendation = "Haycock",
            reason = "Developed from pediatric measurements; use with a professional when the result affects care.",
            color = HealthOrange,
            tag = "PEDIATRIC"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormulaGuideCard(
            emoji = "🌏",
            scenario = "Japanese / East Asian",
            recommendation = "Fujimoto or Takahira",
            reason = "These equations were derived from Asian population data and are included for comparison; population fit does not guarantee individual accuracy.",
            color = HealthTeal,
            tag = "REGIONAL"
        )

        Spacer(modifier = Modifier.height(8.dp))

        FormulaGuideCard(
            emoji = "🔬",
            scenario = "Research / Maximum Accuracy",
            recommendation = "Shuter & Aslani or Gehan & George",
            reason = "These are later reference equations with different source data. Use the equation specified by the study or qualified professional rather than assuming one is universally most accurate.",
            color = MaterialTheme.colorScheme.tertiary,
            tag = "RESEARCH"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Simple decision tree
        HighlightCard(
            emoji = "🤷",
            title = "Still not sure? Use this simple rule:",
            color = HealthGreen,
            content = "• For a general adult estimate → Du Bois or Mosteller\n• For a child → Ask a clinician which method they use\n• If your care team requested a formula → Use that one\n• For research or comparison → Record the formula with the result\n\nNo BSA equation is universally correct for every person. Treat the result as an estimate and follow the method requested by a qualified professional when BSA is being used in clinical care."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Agreement note
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_good_news_for_most_adults_all_),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun FormulaGuideCard(
    emoji: String,
    scenario: String,
    recommendation: String,
    reason: String,
    color: Color,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.05f))
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bsaEducationIcon(emoji),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scenario,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    color = color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppColors.text(color),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Use: $recommendation",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.text(color)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = reason,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

// ─────────────────────────────────────────
// Section 4: BSA vs BMI
// ─────────────────────────────────────────

@Composable
private fun BSAvsBMIContent() {
    Column {
        Paragraph(
            text = "BSA and BMI are both calculated from weight and height, but they describe different things. Understanding the distinction helps you decide when each estimate is useful and when professional context is needed."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Side-by-side comparison
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.txt_head_to_head_comparison),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Header
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(0.8f))
                    Text(
                        text = stringResource(R.string.txt_bsa),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppColors.text(HealthBlue),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = stringResource(R.string.txt_bmi),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = AppColors.text(HealthGreen),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                ComparisonRow("Stands for", "Body Surface Area", "Body Mass Index")
                ComparisonRow("Measures", "External body surface", "Weight relative to height")
                ComparisonRow("Unit", "m² (square meters)", "kg/m² (no unit name)")
                ComparisonRow("Formula type", "Empirical power equations", "Simple division")
                ComparisonRow("Main use", "Clinical context", "Population screening context")
                ComparisonRow("WHO categories", "No categories", "Underweight to Obese")
                ComparisonRow("Typical adult", "1.6 – 2.0 m²", "18.5 – 24.9")
                ComparisonRow("Used by", "Doctors & pharmacists", "Everyone & public health")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // When to use each
        Text(
            text = stringResource(R.string.txt_when_each_measurement_matters),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        UseCaseCard(
            emoji = "📐",
            title = "Use BSA when...",
            color = HealthBlue,
            useCases = listOf(
                "Supporting clinician calculations (never a self-dosing tool)",
                "Assessing burn injury extent",
                "Evaluating kidney function (GFR normalization)",
                "Measuring cardiac output (Cardiac Index)",
                "Providing context alongside clinical measurements",
                "Research involving metabolic rate"
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        UseCaseCard(
            emoji = "⚖️",
            title = "Use BMI when...",
            color = HealthGreen,
            useCases = listOf(
                "Screening for weight-related health risks",
                "Setting weight management goals",
                "Public health population studies",
                "Insurance and general health assessment",
                "Tracking your weight status over time",
                "Quick general health check"
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Key insight
        HighlightCard(
            emoji = "💡",
            title = "The Key Difference",
            color = HealthTeal,
            content = "Think of it this way:\n\n• BMI provides a population reference category based on height and weight; it is not a complete health assessment.\n\n• BSA describes estimated body surface area and is mainly used as clinical context. It is not a self-dosing tool.\n\nBoth use weight and height, but they answer different questions. Record the method and discuss any result that affects care with a qualified professional."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Common misconceptions
        Text(
            text = stringResource(R.string.txt_common_misconceptions),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        MythBusterItem(
            myth = "BSA and BMI are interchangeable",
            truth = "They measure completely different things. A person with a high BMI could have an average BSA, and vice versa."
        )
        MythBusterItem(
            myth = "BSA tells you if you're overweight",
            truth = "BSA has no \"healthy\" or \"unhealthy\" ranges — it's simply a body size measurement, not a health indicator."
        )
        MythBusterItem(
            myth = "You need to know your BSA for daily health tracking",
            truth = "BSA is mainly a clinical context measure. For personal wellness tracking, weight and other trends may be more practical."
        )
        MythBusterItem(
            myth = "A higher BSA means you're less healthy",
            truth = "BSA simply reflects body size. Taller and heavier people naturally have higher BSA regardless of health status."
        )
    }
}

@Composable
private fun ComparisonRow(label: String, bsaValue: String, bmiValue: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = bsaValue,
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.text(HealthBlue),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            lineHeight = 14.sp
        )
        Text(
            text = bmiValue,
            style = MaterialTheme.typography.labelSmall,
            color = AppColors.text(HealthGreen),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun UseCaseCard(
    emoji: String,
    title: String,
    color: Color,
    useCases: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.05f))
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bsaEducationIcon(emoji),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(color)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            useCases.forEach { useCase ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.5f))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = useCase,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MythBusterItem(myth: String, truth: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Myth",
                    tint = HealthRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Myth: \"$myth\"",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.text(HealthRed)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Clarification",
                    tint = HealthGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Truth: $truth",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// Shared Components
// ─────────────────────────────────────────

@Composable
private fun Paragraph(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 22.sp
    )
}

@Composable
private fun AnalygyCard(emoji: String, text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = bsaEducationIcon(emoji),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FactItem(emoji: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = bsaEducationIcon(emoji),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp).width(30.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun HighlightCard(emoji: String, title: String, color: Color, content: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(color.copy(alpha = 0.06f))
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bsaEducationIcon(emoji),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.text(color)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun QuickStatRow(stats: List<Pair<String, String>>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { (value, label) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AppColors.text(HealthOrange)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
