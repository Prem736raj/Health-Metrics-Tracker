package com.health.calculator.bmi.tracker.ui.screens.calculators.bmi

import com.health.calculator.bmi.tracker.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.CalculatorColors
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

// ─── Colors ───────────────────────────────────────────────────────────────────

private val EducationAccent = CalculatorColors.BMI
private val EducationAccentLight = FeatureColors.BmiEnd

// ─── Section Data ─────────────────────────────────────────────────────────────

private data class EducationSection(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val iconColor: Color
)

private val educationSections = listOf(
    EducationSection(
        id = "what_is_bmi",
        title = "What is BMI?",
        icon = Icons.Filled.MenuBook,
        iconColor = HealthColors.Good
    ),
    EducationSection(
        id = "how_calculated",
        title = "How is BMI Calculated?",
        icon = Icons.Outlined.Calculate,
        iconColor = HealthColors.BelowNormal
    ),
    EducationSection(
        id = "categories",
        title = "BMI Categories",
        icon = Icons.Outlined.Category,
        iconColor = CalculatorColors.WaistToHip
    ),
    EducationSection(
        id = "limitations",
        title = "Limitations of BMI",
        icon = Icons.Outlined.ReportProblem,
        iconColor = HealthColors.Warning
    ),
    EducationSection(
        id = "tips",
        title = "Tips for Healthy Weight",
        icon = Icons.Outlined.Lightbulb,
        iconColor = HealthColors.Healthy
    ),
    EducationSection(
        id = "when_doctor",
        title = "When to See a Doctor",
        icon = Icons.Filled.LocalHospital,
        iconColor = HealthColors.Danger
    )
)

// ─── Main Education Content ───────────────────────────────────────────────────

/**
 * Complete BMI educational content section with 6 expandable/collapsible topics.
 * Provides accessible, friendly health information alongside the calculator.
 */
@Composable
fun BmiEducationContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        EducationHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // ── Expandable Sections ───────────────────────────────────────────
        educationSections.forEach { section ->
            ExpandableEducationCard(section = section)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Footer Disclaimer ─────────────────────────────────────────────
        EducationDisclaimer()
    }
}

// ─── Education Header ─────────────────────────────────────────────────────────

@Composable
private fun EducationHeader() {
    val darkGradientSurface = com.health.calculator.bmi.tracker.ui.theme.AppColors.isDark

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(EducationAccent.copy(alpha = 0.05f))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = if (darkGradientSurface) listOf(com.health.calculator.bmi.tracker.ui.theme.DarkRoles.Level2, com.health.calculator.bmi.tracker.ui.theme.DarkRoles.Level2) else listOf(
                                EducationAccent.copy(alpha = 0.15f),
                                EducationAccentLight.copy(alpha = 0.08f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MenuBook,
                    contentDescription = null,
                    tint = AppColors.icon(EducationAccent),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = stringResource(R.string.txt_learn_about_bmi),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.txt_understand_your_results_and_wh),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ─── Expandable Education Card ────────────────────────────────────────────────

@Composable
private fun ExpandableEducationCard(
    section: EducationSection
) {
    var isExpanded by rememberSaveable(section.id) { mutableStateOf(false) }

    val expandRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "expand_${section.id}"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(400, easing = EaseOutCubic)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // ── Clickable Header ──────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isExpanded = !isExpanded }
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(section.iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = null,
                        tint = AppColors.icon(section.iconColor),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = AppColors.secondaryText(section.iconColor.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(expandRotation)
                )
            }

            // ── Expandable Content ────────────────────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(300)) + expandVertically(
                    animationSpec = tween(400, easing = EaseOutCubic)
                ),
                exit = fadeOut(tween(200)) + shrinkVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    HorizontalDivider(
                        color = section.iconColor.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    when (section.id) {
                        "what_is_bmi" -> WhatIsBmiContent()
                        "how_calculated" -> HowCalculatedContent()
                        "categories" -> CategoriesContent()
                        "limitations" -> LimitationsContent()
                        "tips" -> TipsContent()
                        "when_doctor" -> WhenDoctorContent()
                    }
                }
            }
        }
    }
}

// ─── 1. What is BMI? ──────────────────────────────────────────────────────────

@Composable
private fun WhatIsBmiContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentText(
            stringResource(R.string.txt_body_mass_index_bmi_is_a_simpl)
        )

        HighlightBox(
            icon = Icons.Outlined.Info,
            text = "BMI gives you a quick snapshot of where your weight falls on a scale from underweight to obese, helping you and your doctor identify potential health risks."
        )

        ContentText(
            stringResource(R.string.txt_while_bmi_is_widely_used_becau)
        )

        KeyPoint(
            label = "Used by",
            value = "World Health Organization, doctors, and health professionals worldwide"
        )

        KeyPoint(
            label = "Measures",
            value = "The relationship between your weight and height"
        )

        KeyPoint(
            label = "Purpose",
            value = "Screen for potential weight-related health risks"
        )
    }
}

// ─── 2. How is BMI Calculated? ────────────────────────────────────────────────

@Composable
private fun HowCalculatedContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentText(
            stringResource(R.string.txt_bmi_uses_a_straightforward_mat)
        )

        // Formula card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(HealthColors.BelowNormal.copy(alpha = 0.06f))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.txt_bmi_formula),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = HealthColors.BelowNormal
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Metric formula
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.txt_metric),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                append("BMI = ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = HealthColors.BelowNormal)) {
                                    append("weight (kg)")
                                }
                                append(" ÷ ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = HealthColors.BelowNormal)) {
                                    append("height (m)²")
                                }
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Imperial formula
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.txt_imperial),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                append("BMI = ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = HealthColors.BelowNormal)) {
                                    append("weight (lbs)")
                                }
                                append(" ÷ ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = HealthColors.BelowNormal)) {
                                    append("height (in)²")
                                }
                                append(" × 703")
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Example
        HighlightBox(
            icon = Icons.Outlined.Calculate,
            text = "Example: A person weighing 70 kg and 1.75 m tall:\nBMI = 70 ÷ (1.75 × 1.75) = 70 ÷ 3.0625 = 22.9"
        )

        ContentText(
            stringResource(R.string.txt_the_result_is_expressed_in_kg_)
        )
    }
}

// ─── 3. BMI Categories ────────────────────────────────────────────────────────

@Composable
private fun CategoriesContent() {
    data class CategoryRow(
        val name: String,
        val range: String,
        val color: Color,
        val risk: String
    )

    val categories = listOf(
        CategoryRow("Severe Thinness", "< 16.0", HealthColors.DangerDark, "Very High"),
        CategoryRow("Moderate Thinness", "16.0 – 16.9", HealthColors.Danger, "High"),
        CategoryRow("Mild Thinness", "17.0 – 18.4", HealthColors.Warning, "Moderate"),
        CategoryRow("Normal Weight", "18.5 – 24.9", HealthColors.Healthy, "Low"),
        CategoryRow("Overweight", "25.0 – 29.9", HealthColors.WarningDark, "Increased"),
        CategoryRow("Obese Class I", "30.0 – 34.9", HealthColors.Caution, "High"),
        CategoryRow("Obese Class II", "35.0 – 39.9", HealthColors.Danger, "Very High"),
        CategoryRow("Obese Class III", "≥ 40.0", HealthColors.DangerDark, "Extremely High")
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentText(
            stringResource(R.string.txt_the_world_health_organization_)
        )

        // Category table
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Table header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_category),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.weight(1.1f)
                    )
                    Text(
                        text = stringResource(R.string.txt_bmi_range),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.weight(0.7f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.txt_risk),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.weight(0.7f),
                        textAlign = TextAlign.End
                    )
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                categories.forEach { cat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .let {
                                if (cat.name == "Normal Weight")
                                    it.background(cat.color.copy(alpha = 0.06f))
                                        .border(0.5.dp, cat.color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                else it
                            }
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Color dot + name
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(cat.color)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (cat.name == "Normal Weight") FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (cat.name == "Normal Weight")
                                cat.color
                            else
                                MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        // BMI range
                        Text(
                            text = cat.range,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(0.7f),
                            textAlign = TextAlign.Center
                        )

                        // Risk level
                        Text(
                            text = cat.risk,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = cat.color,
                            modifier = Modifier.weight(0.7f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }

        HighlightBox(
            icon = Icons.Outlined.HealthAndSafety,
            text = "The \"Normal Weight\" range (18.5–24.9) is associated with the lowest risk of weight-related health problems for most adults."
        )
    }
}

// ─── 4. Limitations of BMI ────────────────────────────────────────────────────

@Composable
private fun LimitationsContent() {
    data class LimitationItem(
        val icon: ImageVector,
        val title: String,
        val description: String
    )

    val limitations = listOf(
        LimitationItem(
            icon = Icons.Outlined.HealthAndSafety,
            title = "Doesn't distinguish muscle from fat",
            description = "Athletes and muscular individuals may have a high BMI despite having low body fat. Muscle weighs more than fat per unit volume."
        ),
        LimitationItem(
            icon = Icons.Outlined.Category,
            title = "Ignores bone density",
            description = "People with denser bones will weigh more, potentially skewing their BMI higher without being overweight."
        ),
        LimitationItem(
            icon = Icons.Outlined.Info,
            title = "Doesn't account for age",
            description = "Older adults tend to have more body fat than younger adults at the same BMI. The standard ranges may not be ideal for all age groups."
        ),
        LimitationItem(
            icon = Icons.Outlined.HealthAndSafety,
            title = "Same cutoffs for both sexes",
            description = "Women naturally carry more body fat than men. Using the same BMI thresholds for both may not reflect individual health accurately."
        ),
        LimitationItem(
            icon = Icons.Outlined.Category,
            title = "Ethnic variations",
            description = "Different ethnic groups may have different body compositions and health risk levels at the same BMI. Asian populations, for example, may face higher risks at lower BMI values."
        ),
        LimitationItem(
            icon = Icons.Outlined.ReportProblem,
            title = "No fat distribution info",
            description = "BMI doesn't tell you where your body stores fat. Belly fat (visceral fat) is more dangerous than fat stored in hips and thighs, but BMI can't differentiate."
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentText(
            stringResource(R.string.txt_while_bmi_is_a_useful_starting)
        )

        limitations.forEach { item ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        HighlightBox(
            icon = Icons.Outlined.Info,
            text = "For a more complete picture of your health, combine BMI with other measurements like waist circumference, body fat percentage, and waist-to-hip ratio."
        )
    }
}

// ─── 5. Tips for Healthy Weight ───────────────────────────────────────────────

@Composable
private fun TipsContent() {
    data class TipItem(
        val number: Int,
        val icon: ImageVector,
        val title: String,
        val description: String,
        val color: Color
    )

    val tips = listOf(
        TipItem(
            number = 1,
            icon = Icons.Outlined.HealthAndSafety,
            title = "Eat a balanced diet",
            description = "Focus on whole grains, fruits, vegetables, lean proteins, and healthy fats. Avoid highly processed foods and excessive sugar.",
            color = HealthColors.Healthy
        ),
        TipItem(
            number = 2,
            icon = Icons.Outlined.HealthAndSafety,
            title = "Stay physically active",
            description = "Aim for at least 150 minutes of moderate exercise per week. Find activities you enjoy — walking, swimming, cycling, or dancing.",
            color = HealthColors.Good
        ),
        TipItem(
            number = 3,
            icon = Icons.Outlined.Info,
            title = "Stay well hydrated",
            description = "Drink plenty of water throughout the day. Sometimes thirst is mistaken for hunger. Carry a water bottle as a reminder.",
            color = FeatureColors.WaterDeep
        ),
        TipItem(
            number = 4,
            icon = Icons.Outlined.Info,
            title = "Get quality sleep",
            description = "Aim for 7–9 hours of sleep per night. Poor sleep disrupts hunger hormones and can lead to weight gain over time.",
            color = HealthColors.Info
        ),
        TipItem(
            number = 5,
            icon = Icons.Outlined.HealthAndSafety,
            title = "Manage stress",
            description = "Chronic stress increases cortisol, which can promote fat storage. Try mindfulness, meditation, deep breathing, or yoga.",
            color = CalculatorColors.WaistToHip
        ),
        TipItem(
            number = 6,
            icon = Icons.Outlined.Calculate,
            title = "Track your progress",
            description = "Monitor your weight and health metrics regularly — but not obsessively. Focus on long-term trends, not daily fluctuations.",
            color = HealthColors.Caution
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ContentText(
            stringResource(R.string.txt_maintaining_a_healthy_weight_i)
        )

        tips.forEach { tip ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = tip.color.copy(alpha = 0.04f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Number circle
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(tip.color.copy(alpha = 0.1f))
                            .border(1.dp, tip.color.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tip.number.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = tip.color
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tip.icon,
                                contentDescription = null,
                                tint = tip.color,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tip.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tip.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        HighlightBox(
            icon = Icons.Filled.Favorite,
            text = "Remember: Health is more than a number on a scale. Focus on how you feel, your energy levels, and your overall wellbeing."
        )
    }
}

// ─── 6. When to See a Doctor ──────────────────────────────────────────────────

@Composable
private fun WhenDoctorContent() {
    data class DoctorTrigger(
        val icon: ImageVector,
        val text: String
    )

    val triggers = listOf(
        DoctorTrigger(Icons.Outlined.ReportProblem, "Your BMI is below 16 (Severe Thinness) — this may indicate malnutrition or an underlying medical condition"),
        DoctorTrigger(Icons.Outlined.ReportProblem, "Your BMI is 30 or above (Obese) — your doctor can help create a safe, effective plan"),
        DoctorTrigger(Icons.Outlined.Info, "You've experienced unexplained rapid weight loss or gain"),
        DoctorTrigger(Icons.Outlined.HealthAndSafety, "You're taking medications that affect your weight"),
        DoctorTrigger(Icons.Outlined.Info, "You have concerns about your eating habits or relationship with food"),
        DoctorTrigger(Icons.Filled.Favorite, "You have existing health conditions like diabetes, heart disease, or high blood pressure"),
        DoctorTrigger(Icons.Outlined.HealthAndSafety, "You're pregnant or planning to become pregnant"),
        DoctorTrigger(Icons.Outlined.Info, "You're assessing a child's weight — pediatric BMI uses different charts")
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContentText(
            stringResource(R.string.txt_while_this_calculator_provides)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                triggers.forEach { trigger ->
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = trigger.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 1.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = trigger.text,
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Emergency note
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
            border = androidx.compose.foundation.BorderStroke(
                0.5.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(R.string.txt_your_health_matters),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.txt_a_healthcare_professional_can_),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// ─── Reusable Components ──────────────────────────────────────────────────────

@Composable
private fun ContentText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)),
        lineHeight = 22.sp
    )
}

@Composable
private fun HighlightBox(
    icon: ImageVector,
    text: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = AppColors.surface(EducationAccent.copy(alpha = 0.04f)),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp, EducationAccent.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppColors.icon(EducationAccent),
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 1.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun KeyPoint(
    label: String,
    value: String
) {
    Row(modifier = Modifier.padding(start = 4.dp)) {
        Text(
            text = stringResource(R.string.txt_text_placeholder_3),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = AppColors.text(EducationAccent),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("$label: ")
                }
                append(value)
            },
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)),
            lineHeight = 18.sp
        )
    }
}

// ─── Education Disclaimer ─────────────────────────────────────────────────────

@Composable
private fun EducationDisclaimer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.surface(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)))
            .then(if (AppColors.isDark) Modifier.border(1.dp, com.health.calculator.bmi.tracker.ui.theme.DarkRoles.WarningOutline, RoundedCornerShape(12.dp)) else Modifier)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = AppColors.icon(MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
            modifier = Modifier
                .size(14.dp)
                .offset { IntOffset(0, 4) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.txt_this_educational_content_is_fo_2),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = AppColors.secondaryText(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)),
            lineHeight = 16.sp
        )
    }
}
