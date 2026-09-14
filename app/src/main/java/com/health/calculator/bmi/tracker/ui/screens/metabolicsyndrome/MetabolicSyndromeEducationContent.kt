package com.health.calculator.bmi.tracker.ui.screens.metabolicsyndrome

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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

@Composable
fun MetabolicSyndromeEducationScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.txt_learn_about_metabolic_syndrome),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = stringResource(R.string.txt_evidence_based_information_to_),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Section 1: What is Metabolic Syndrome
        EducationSection(
            icon = Icons.Outlined.MonitorHeart,
            title = "What is Metabolic Syndrome?",
            accentColor = HealthRed
        ) {
            WhatIsMetabolicSyndromeContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 2: The 5 Risk Factors
        EducationSection(
            icon = Icons.Outlined.Science,
            title = "The 5 Risk Factors Explained",
            accentColor = HealthOrange
        ) {
            FiveRiskFactorsContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 3: Who is at Risk
        EducationSection(
            icon = Icons.Outlined.Groups,
            title = "Who is at Risk?",
            accentColor = HealthYellow
        ) {
            WhoIsAtRiskContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 4: Prevention and Treatment
        EducationSection(
            icon = Icons.Outlined.DirectionsRun,
            title = "Prevention and Treatment",
            accentColor = HealthGreen
        ) {
            PreventionTreatmentContent()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Section 5: Understanding Blood Work
        EducationSection(
            icon = Icons.Outlined.Assignment,
            title = "Understanding Your Blood Work",
            accentColor = HealthBlue
        ) {
            BloodWorkGuideContent()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Medical Disclaimer
        MedicalDisclaimerCard()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun EducationSection(
    icon: ImageVector,
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
            // Header
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
                        imageVector = icon,
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
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = accentColor
                )
            }

            // Expandable content
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

// ─── Section 1: What is Metabolic Syndrome ───────────────────

@Composable
private fun WhatIsMetabolicSyndromeContent() {
    Column {
        InfoParagraph(
            text = "Metabolic syndrome is a clinical term for a cluster of measurements that can occur together. Definitions differ, and a professional must interpret repeat readings, medicines and laboratory context."
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatHighlightCard(
            icon = Icons.Outlined.Assessment,
            stat = "Common",
            description = "Prevalence estimates vary by population and definition. A professional uses repeat measurements and local guidance to interpret the pattern."
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoParagraph(
            text = "Also known by other names:"
        )

        Spacer(modifier = Modifier.height(6.dp))

        AlsoKnownAsChips(
            names = listOf(
                "Syndrome X",
                "Insulin Resistance Syndrome",
                "Dysmetabolic Syndrome",
                "Reaven's Syndrome"
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        KeyPointCard(
            title = "The Core Problem: Insulin Resistance",
            content = "Insulin sensitivity, body fat distribution, blood pressure and blood lipids can influence one another, but this app cannot measure or explain the cause of an individual's result."
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoParagraph(
            text = "Many clinical definitions use at least 3 of 5 markers, but a clinician must interpret repeat measurements, medication status, and laboratory context. This app shows an educational screening reference only."
        )

        Spacer(modifier = Modifier.height(12.dp))

        StatRow(
            stats = listOf(
                StatItem("5", "Screening markers"),
                StatItem("1", "Reading at a time"),
                StatItem("—", "No personal risk score")
            )
        )
    }
}

// ─── Section 2: The 5 Risk Factors ───────────────────────────

@Composable
private fun FiveRiskFactorsContent() {
    Column {
        InfoParagraph(
            text = "Each of the five criteria describes a different measurement. The count is a screening reference, not a diagnosis or an individual's event-risk calculation."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Factor 1: Central Obesity
        RiskFactorDetailCard(
            number = "1",
            name = "Central Obesity (Large Waistline)",
            icon = Icons.Outlined.Straighten,
            threshold = "Example ATP III: men > 102 cm (40 in), women > 88 cm (35 in). IDF uses ethnicity-specific references (South Asian: 90/80 cm).",
            color = HealthRed,
            explanation = "Waist circumference is a practical body-size measure used in some screening definitions; it cannot show visceral fat.",
            whyItMatters = "Use the measurement consistently and discuss a persistent pattern with a professional."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Factor 2: High Triglycerides
        RiskFactorDetailCard(
            number = "2",
            name = "Elevated Triglycerides",
            icon = Icons.Outlined.Science,
            threshold = "≥ 150 mg/dL (1.7 mmol/L)",
            color = HealthOrange,
            explanation = "Triglycerides are a common type of fat in the blood. Higher values can be associated with cardiovascular risk, but fasting status, medicines and the wider lipid panel matter.",
            whyItMatters = "Triglycerides are one laboratory marker; fasting status, medicines and the wider lipid panel matter."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Factor 3: Low HDL
        RiskFactorDetailCard(
            number = "3",
            name = "Reduced HDL Cholesterol",
            icon = Icons.Outlined.FavoriteBorder,
            threshold = "Men: < 40 mg/dL | Women: < 50 mg/dL",
            color = HealthYellow,
            explanation = "HDL (high-density lipoprotein) is one part of the cholesterol panel. Lower values can be associated with higher cardiovascular risk, but HDL alone does not predict an individual's outcome.",
            whyItMatters = "Interpret HDL with LDL, triglycerides, blood pressure, family history and other context. A healthcare professional can explain what the complete panel means for you."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Factor 4: High Blood Pressure
        RiskFactorDetailCard(
            number = "4",
            name = "Elevated Blood Pressure",
            icon = Icons.Outlined.MonitorHeart,
            threshold = "Systolic ≥ 130 OR Diastolic ≥ 85 mmHg",
            color = HealthRed,
            explanation = "Blood pressure is a measurement that varies with technique, timing and context. Repeated readings are more useful than one value.",
            whyItMatters = "Use the blood-pressure tracker for a consistent log and ask a professional to interpret persistent elevation."
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Factor 5: High Fasting Glucose
        RiskFactorDetailCard(
            number = "5",
            name = "Elevated Fasting Glucose",
            icon = Icons.Outlined.Assessment,
            threshold = "≥ 100 mg/dL (5.6 mmol/L)",
            color = HealthOrange,
            explanation = "Fasting blood glucose is a laboratory measurement whose interpretation depends on the lab, fasting status, medicines and repeat testing.",
            whyItMatters = "This app cannot diagnose diabetes or insulin resistance from a value; discuss results with a qualified professional."
        )

        Spacer(modifier = Modifier.height(14.dp))

        KeyPointCard(
            title = "How They're Interconnected",
            content = "These 5 factors don't exist in isolation — they share a common root in insulin resistance. When cells resist insulin:\n\n• The pancreas produces more insulin → promotes fat storage\n• The liver produces more triglycerides → raises blood fats\n• Kidneys retain more sodium → raises blood pressure\n• Cells can't absorb glucose efficiently → raises blood sugar\n• Fat accumulates centrally → worsens insulin resistance\n\nThis creates a vicious cycle where each factor worsens the others."
        )
    }
}

// ─── Section 3: Who is at Risk ───────────────────────────────

@Composable
private fun WhoIsAtRiskContent() {
    Column {
        InfoParagraph(
            text = "Many factors can influence these measurements. Population associations do not determine an individual's diagnosis or future outcome."
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(R.string.txt_non_modifiable_risk_factors),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = HealthRed,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.txt_these_factors_you_cannot_chang),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        RiskFactorItem(
            icon = Icons.Outlined.Cake,
            title = "Age",
            description = "Age can be associated with different measurement patterns, but people of any adult age can have varied results."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Groups,
            title = "Family History",
            description = "Family history can be useful context to share with a professional; it does not predict an individual's result."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Public,
            title = "Ethnicity",
            description = "South Asian, Hispanic, African American, and Native American populations have higher rates. Different ethnic groups may need lower waist cutoffs."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Person,
            title = "Gender-Specific Risks",
            description = "Life stage and conditions such as PCOS can be relevant context; only a professional can assess personal risk."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.txt_modifiable_risk_factors),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = HealthGreen,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.txt_these_factors_you_can_change_a),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        RiskFactorItem(
            icon = Icons.Outlined.MonitorWeight,
            title = "Obesity (especially central obesity)",
            description = "Body-size measures are one part of metabolic health. Sustainable habits can support wellbeing without promising a specific risk change."
        )
        RiskFactorItem(
            icon = Icons.Outlined.EventSeat,
            title = "Sedentary Lifestyle",
            description = "Lower activity is associated with several metabolic markers. Breaking up long periods of sitting where practical can support overall wellbeing."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Restaurant,
            title = "Unhealthy Diet",
            description = "Food patterns vary by culture and access. A varied, adequately nourishing pattern is a reasonable general goal."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Schedule,
            title = "Poor Sleep & Sleep Apnea",
            description = "Sleep quality and duration can affect wellbeing; persistent sleep concerns deserve professional discussion."
        )
        RiskFactorItem(
            icon = Icons.Outlined.FavoriteBorder,
            title = "Chronic Stress",
            description = "Stress can affect sleep, appetite and how measurements vary. Support and professional care may help when stress feels persistent or hard to manage."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Warning,
            title = "Smoking",
            description = "Smoking is associated with less favorable cardiometabolic markers. Quitting supports overall health, and a professional can help you choose support."
        )
        RiskFactorItem(
            icon = Icons.Outlined.Warning,
            title = "Excessive Alcohol",
            description = "Alcohol can affect triglycerides, blood pressure, sleep and weight. Lower intake is safer for many people; ask a professional if you want personalised guidance."
        )

        Spacer(modifier = Modifier.height(14.dp))

        KeyPointCard(
            title = "Conditions Linked to Higher Risk",
            content = "• Polycystic Ovary Syndrome (PCOS) — 2-3x higher risk\n• Non-alcoholic Fatty Liver Disease (NAFLD)\n• History of gestational diabetes\n• Hypothyroidism\n• Cushing's syndrome\n• Certain medications (antipsychotics, steroids, some HIV medications)"
        )
    }
}

// ─── Section 4: Prevention and Treatment ─────────────────────

@Composable
private fun PreventionTreatmentContent() {
    Column {
        InfoParagraph(
            text = "Lifestyle habits can support wellbeing, but these measurements can have many causes. Use the app to prepare questions and discuss persistent changes with a professional."
        )

        Spacer(modifier = Modifier.height(14.dp))

        StatHighlightCard(
            icon = Icons.Outlined.Lightbulb,
            stat = "5-10%",
            description = "A modest weight change may improve some metabolic measurements for some people. Responses vary, and weight is only one part of metabolic health."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Weight Loss
        TreatmentCard(
            icon = Icons.Outlined.MonitorWeight,
            title = "Weight Management",
            color = HealthGreen,
            points = listOf(
                "Aim for gradual, sustainable weight loss of 0.5-1 kg per week",
                "A 5-7% change may improve some markers for some people; discuss a safe target with a professional",
                "Focus on sustainable habits rather than trying to target a specific type of body fat",
                "Avoid crash diets — they can be difficult to sustain and may affect wellbeing",
                "Set realistic goals and celebrate small victories"
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Exercise
        TreatmentCard(
            icon = Icons.Outlined.DirectionsRun,
            title = "Physical Activity",
            color = HealthBlue,
            points = listOf(
                "Aim for at least 150 minutes of moderate aerobic activity per week",
                "Include resistance/strength training 2-3 times per week",
                "Short walking sessions after meals may support glucose management for some people",
                "Reduce prolonged sitting — stand or walk every 30-60 minutes",
                "Any movement is better than none — start small and build gradually",
                "Activities like brisk walking, cycling, swimming, and dancing are excellent"
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Diet
        TreatmentCard(
            icon = Icons.Outlined.Restaurant,
            title = "Dietary Changes",
            color = HealthTeal,
            points = listOf(
                "A Mediterranean-style pattern can be a flexible option: plants, whole grains, varied protein and unsaturated fats",
                "DASH diet: Designed for blood pressure, also improves other markers",
                "Consider moderating refined carbohydrates and added sugars within your overall eating pattern",
                "Increase fiber intake to 25-30g daily from vegetables, legumes, and whole grains",
                "Eat fatty fish 2-3x per week for omega-3 fatty acids",
                "Notice sodium in food labels; an appropriate target depends on personal context",
                "Choose whole, unprocessed foods over packaged alternatives"
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Lifestyle
        TreatmentCard(
            icon = Icons.Outlined.FavoriteBorder,
            title = "Lifestyle Modifications",
            color = HealthYellow,
            points = listOf(
                "If you smoke, quitting supports overall health; a professional can help with a plan",
                "Manage stress through meditation, deep breathing, yoga, or therapy",
                "Prioritize 7-9 hours of quality sleep per night",
                "Get screened for sleep apnea if you snore or feel chronically tired",
                "If you drink alcohol, lower intake is safer for many people; individual guidance may differ",
                "Build a support system — friends, family, or health communities"
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Medication
        TreatmentCard(
            icon = Icons.Outlined.Medication,
            title = "Discuss treatment with a professional",
            color = HealthOrange,
            points = listOf(
                "Treatment depends on confirmed measurements, history and clinician assessment",
                "A professional may discuss lifestyle, monitoring or medicines when appropriate",
                "Do not start, stop or change a medicine based on this app",
                "Bring your logs and questions to an appointment",
                "Never self-medicate — follow the instructions of your healthcare team"
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        KeyPointCard(
            title = "Your Action Plan",
            content = "1. Start with one manageable change at a time\n2. Choose movement you can do consistently\n3. Build a varied, adequately nourishing eating pattern\n4. Keep repeat measurements and questions for your clinician\n5. Track trends without treating them as a diagnosis\n6. Give changes time and seek support when needed"
        )
    }
}

// ─── Section 5: Understanding Blood Work ─────────────────────

@Composable
private fun BloodWorkGuideContent() {
    Column {
        InfoParagraph(
            text = "Understanding blood test results can help you have meaningful conversations with a healthcare professional. Ranges and fasting requirements vary by lab and personal context, so use this as general education only."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Preparation
        KeyPointCard(
            title = "Before Your Blood Test",
            content = "• Follow the lab's instructions; not every test requires fasting\n• Ask whether water, medicines or supplements should be changed\n• Tell the ordering professional about medicines and relevant conditions\n• Use the same lab when practical if you are comparing trends\n• Ask how and when a result should be repeated"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Blood values guide
        Text(
            text = stringResource(R.string.txt_key_lab_values_explained),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        BloodValueCard(
            name = "Fasting Blood Glucose",
            normalRange = "70-99 mg/dL (3.9-5.5 mmol/L)",
            preDiabetic = "100-125 mg/dL (5.6-6.9 mmol/L)",
            diabetic = "≥ 126 mg/dL (≥ 7.0 mmol/L; requires confirmation)",
            whatItMeasures = "The amount of sugar in your blood after fasting. Reflects how well your body manages blood sugar overnight.",
            icon = Icons.Outlined.Assessment
        )

        Spacer(modifier = Modifier.height(8.dp))

        BloodValueCard(
            name = "Triglycerides",
            normalRange = "< 150 mg/dL (< 1.7 mmol/L)",
            preDiabetic = "150-199 mg/dL (borderline high)",
            diabetic = "≥ 200 mg/dL (higher reference) | ≥ 500 mg/dL (very high)",
            whatItMeasures = "The most common type of fat in your blood. Affected by diet, exercise, and alcohol. Can vary significantly day to day.",
            icon = Icons.Outlined.Science
        )

        Spacer(modifier = Modifier.height(8.dp))

        BloodValueCard(
            name = "HDL Cholesterol",
            normalRange = "Men: ≥ 40 mg/dL | Women: ≥ 50 mg/dL",
            preDiabetic = "Optimal: ≥ 60 mg/dL (protective)",
            diabetic = "Men: < 40 mg/dL | Women: < 50 mg/dL (lower reference)",
            whatItMeasures = "\"Good\" cholesterol that removes harmful cholesterol from arteries. Higher is better — one of the few values where MORE is good.",
            icon = Icons.Outlined.FavoriteBorder
        )

        Spacer(modifier = Modifier.height(8.dp))

        BloodValueCard(
            name = "HbA1c (Hemoglobin A1c)",
            normalRange = "< 5.7%",
            preDiabetic = "5.7-6.4% (pre-diabetes)",
            diabetic = "≥ 6.5% (requires confirmation)",
            whatItMeasures = "Reflects your average blood sugar over the past 2-3 months. More reliable than a single fasting glucose test. Ask your doctor about this test.",
            icon = Icons.Outlined.Assessment
        )

        Spacer(modifier = Modifier.height(14.dp))

        // When to retest
        Text(
            text = stringResource(R.string.txt_when_to_retest),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        RetestScheduleCard(
            items = listOf(
                RetestItem(
                    condition = "No follow-up advised",
                    frequency = "Follow your lab/clinician plan",
                    icon = Icons.Outlined.CheckCircle
                ),
                RetestItem(
                    condition = "A result needs follow-up",
                    frequency = "Timing depends on the result",
                    icon = Icons.Outlined.Warning
                ),
                RetestItem(
                    condition = "Several screening markers",
                    frequency = "Ask a professional about timing",
                    icon = Icons.Outlined.Warning
                ),
                RetestItem(
                    condition = "After a treatment change",
                    frequency = "Use the prescriber's plan",
                    icon = Icons.Outlined.Medication
                ),
                RetestItem(
                    condition = "After a lifestyle change",
                    frequency = "Review timing with a professional",
                    icon = Icons.Outlined.DirectionsRun
                )
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        KeyPointCard(
            title = "Tips for Talking to Your Doctor",
            content = "• Bring your tracked results from this app\n• Ask about each abnormal value specifically\n• Ask: \"What can I do about this?\"\n• Ask about target values for YOUR situation\n• Don't be afraid to ask for explanations in simple terms\n• Request copies of all your lab results\n• Ask when you should retest"
        )
    }
}

// ─── Reusable Content Components ─────────────────────────────

@Composable
private fun InfoParagraph(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 22.sp
    )
}

@Composable
private fun StatHighlightCard(icon: ImageVector, stat: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stat,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
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
}

@Composable
private fun AlsoKnownAsChips(names: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        names.forEach { name ->
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun KeyPointCard(title: String, content: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(6.dp))
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
private fun StatRow(stats: List<StatItem>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        stats.forEach { stat ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = HealthRed
                )
                Text(
                    text = stat.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class StatItem(val value: String, val label: String)

@Composable
private fun RiskFactorDetailCard(
    number: String,
    name: String,
    icon: ImageVector,
    threshold: String,
    color: Color,
    explanation: String,
    whyItMatters: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = color.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = number,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = color
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = threshold,
                        style = MaterialTheme.typography.labelSmall,
                        color = color
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = color.copy(alpha = 0.08f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(R.string.txt_why_it_matters),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = whyItMatters,
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
private fun RiskFactorItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(30.dp)
                .size(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
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
private fun TreatmentCard(
    icon: ImageVector,
    title: String,
    color: Color,
    points: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.6f))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BloodValueCard(
    name: String,
    normalRange: String,
    preDiabetic: String,
    diabetic: String,
    whatItMeasures: String,
    icon: ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = whatItMeasures,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Value ranges
            ValueRangeRow(label = "Reference", value = normalRange, color = HealthGreen)
            ValueRangeRow(label = "Context", value = preDiabetic, color = HealthOrange)
            ValueRangeRow(label = "Discuss", value = diabetic, color = HealthRed)
        }
    }
}

@Composable
private fun ValueRangeRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class RetestItem(
    val condition: String,
    val frequency: String,
    val icon: ImageVector
)

@Composable
private fun RetestScheduleCard(items: List<RetestItem>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .width(24.dp)
                            .size(16.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.condition,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.frequency,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                if (item != items.last()) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 2.dp)
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
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Outlined.MedicalServices,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.txt_medical_disclaimer),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.txt_this_educational_content_is_fo_4),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
