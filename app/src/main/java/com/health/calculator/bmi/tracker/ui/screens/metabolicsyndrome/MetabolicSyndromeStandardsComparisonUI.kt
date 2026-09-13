// File: app/src/main/java/com/health/calculator/bmi/tracker/ui/screens/metabolicsyndrome/MetabolicSyndromeStandardsComparisonUI.kt
package com.health.calculator.bmi.tracker.ui.screens.metabolicsyndrome

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
import com.health.calculator.bmi.tracker.calculator.*
import com.health.calculator.bmi.tracker.ui.theme.*

private data class StandardContext(
    val icon: ImageVector,
    val region: String,
    val standard: String,
    val note: String
)

@Composable
fun StandardsComparisonSection(
    comparison: MultiStandardComparison,
    onEthnicityChange: (Ethnicity) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Toggle card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    expanded = !expanded
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CompareArrows,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.txt_compare_medical_standards),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.txt_atp_iii_vs_idf_vs_who_criteria),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(tween(400)) + fadeIn(tween(400)),
            exit = shrinkVertically(tween(300)) + fadeOut(tween(200))
        ) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                // Quick Result Summary
                QuickResultBanner(comparison = comparison)

                Spacer(modifier = Modifier.height(14.dp))

                // Ethnicity Selector (for IDF)
                EthnicitySelector(
                    selectedEthnicity = comparison.selectedEthnicity,
                    onSelect = onEthnicityChange
                )

                Spacer(modifier = Modifier.height(14.dp))

                // ATP III Detail
                StandardDetailCard(result = comparison.atpResult, accentColor = HealthBlue)

                Spacer(modifier = Modifier.height(10.dp))

                // IDF Detail
                StandardDetailCard(result = comparison.idfResult, accentColor = HealthTeal)

                Spacer(modifier = Modifier.height(10.dp))

                // WHO Detail
                StandardDetailCard(result = comparison.whoResult, accentColor = HealthOrange)

                // Disagreement note
                if (!comparison.allAgree && comparison.disagreementNote != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    DisagreementNoteCard(note = comparison.disagreementNote)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Ethnicity waist cutoff reference
                EthnicityWaistCutoffTable()

                Spacer(modifier = Modifier.height(14.dp))

                // Which standard note
                WhichStandardNote()
            }
        }
    }
}

@Composable
private fun QuickResultBanner(comparison: MultiStandardComparison) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.txt_results_by_standard),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StandardQuickBadge(
                    name = "ATP III",
                    isMet = comparison.atpResult.isMet,
                    count = "${comparison.atpResult.criteriaMetCount}/5",
                    color = HealthBlue,
                    isScored = comparison.atpResult.criteriaDetails.isNotEmpty()
                )
                StandardQuickBadge(
                    name = "IDF",
                    isMet = comparison.idfResult.isMet,
                    count = "${comparison.idfResult.criteriaMetCount}/5",
                    color = HealthTeal,
                    isScored = comparison.idfResult.criteriaDetails.isNotEmpty()
                )
                StandardQuickBadge(
                    name = "WHO",
                    isMet = comparison.whoResult.isMet,
                    count = if (comparison.whoResult.criteriaDetails.isEmpty()) "—" else "${comparison.whoResult.criteriaMetCount}/5",
                    color = HealthOrange,
                    isScored = comparison.whoResult.criteriaDetails.isNotEmpty()
                )
            }

            if (comparison.allAgree) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = HealthGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = HealthGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.txt_all_three_standards_agree_on_t),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = HealthGreen
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = HealthOrange.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = HealthOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.txt_standards_disagree_see_details),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = HealthOrange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardQuickBadge(
    name: String,
    isMet: Boolean,
    count: String,
    color: Color,
    isScored: Boolean
) {
    val statusColor = when {
        !isScored -> HealthColors.Info
        isMet -> HealthColors.Caution
        else -> HealthColors.Healthy
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = statusColor.copy(alpha = 0.12f),
            shape = CircleShape,
            modifier = Modifier.size(52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = when {
                        !isScored -> Icons.Outlined.Info
                        isMet -> Icons.Outlined.Warning
                        else -> Icons.Outlined.CheckCircle
                    },
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = count,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = when {
                !isScored -> "Not scored"
                isMet -> "Reference met"
                else -> "Reference not met"
            },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = statusColor
        )
    }
}

@Composable
private fun EthnicitySelector(
    selectedEthnicity: Ethnicity,
    onSelect: (Ethnicity) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var showDropdown by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = HealthTeal.copy(alpha = 0.06f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Public,
                    contentDescription = null,
                    tint = HealthTeal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_ethnicity_specific_idf_cutoffs),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = HealthTeal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.txt_idf_uses_different_waist_thres),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box {
                OutlinedCard(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            showDropdown = true
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = selectedEthnicity.displayName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "M > ${selectedEthnicity.maleWaistCm.toInt()} cm | F > ${selectedEthnicity.femaleWaistCm.toInt()} cm",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = "Select ethnicity",
                            tint = HealthTeal
                        )
                    }
                }

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false }
                ) {
                    Ethnicity.getAll().forEach { ethnicity ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = ethnicity.displayName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (ethnicity == selectedEthnicity) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = "M > ${ethnicity.maleWaistCm.toInt()} cm | F > ${ethnicity.femaleWaistCm.toInt()} cm",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSelect(ethnicity)
                                showDropdown = false
                            },
                            trailingIcon = {
                                if (ethnicity == selectedEthnicity) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = HealthTeal
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardDetailCard(
    result: StandardResult,
    accentColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val isScored = result.criteriaDetails.isNotEmpty()
    val statusColor = when {
        !isScored -> HealthColors.Info
        result.isMet -> HealthColors.Caution
        else -> HealthColors.Healthy
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.04f)
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = !expanded
            }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            !isScored -> Icons.Outlined.Info
                            result.isMet -> Icons.Outlined.Warning
                            else -> Icons.Outlined.CheckCircle
                        },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.standardName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result.requiredForDiagnosis,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when {
                            !isScored -> "Not scored"
                            result.isMet -> "Reference met"
                            else -> "Reference not met"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expanded details
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(250)) + fadeOut(tween(200))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Criteria breakdown
                    result.criteriaDetails.forEach { criterion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Status icon
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (criterion.isMet) HealthColors.Caution.copy(alpha = 0.12f)
                                        else HealthColors.Healthy.copy(alpha = 0.12f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (criterion.isMet) Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = if (criterion.isMet) HealthColors.Caution else HealthColors.Healthy,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = criterion.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (criterion.isRequired) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            color = accentColor.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(3.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.txt_req),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = accentColor,
                                                fontSize = 8.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${criterion.userValue} (${criterion.threshold})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notes
                    result.notes.forEach { note ->
                        val isWarning = note.startsWith("⚠️")
                        val cleanNote = note
                            .removePrefix("⚠️")
                            .removePrefix("ℹ️")
                            .trimStart()
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = if (isWarning) Icons.Outlined.Warning else Icons.Outlined.Info,
                                contentDescription = null,
                                tint = if (isWarning) HealthColors.Caution else accentColor,
                                modifier = Modifier
                                    .size(15.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(
                                text = cleanNote,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
private fun DisagreementNoteCard(note: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = HealthOrange.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = HealthOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_why_standards_disagree),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = HealthOrange
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun EthnicityWaistCutoffTable() {
    var expanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val cutoffs = remember { MetabolicSyndromeStandards.getEthnicityWaistCutoffs() }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                expanded = !expanded
            }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Straighten,
                    contentDescription = null,
                    tint = HealthColors.BelowNormal,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_idf_ethnicity_specific_waist_c),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(250)) + fadeOut(tween(200))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(10.dp))

                    // Table header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.txt_ethnic_group),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = stringResource(R.string.txt_male),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.5f)
                        )
                        Text(
                            text = stringResource(R.string.txt_female),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.5f)
                        )
                    }

                    cutoffs.forEach { cutoff ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.2f)) {
                                Text(
                                    text = cutoff.groupName,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = cutoff.regions,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 9.sp,
                                    lineHeight = 11.sp
                                )
                            }
                            Text(
                                text = "> ${cutoff.maleWaistCm.toInt()} cm",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = "> ${cutoff.femaleWaistCm.toInt()} cm",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WhichStandardNote() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.txt_which_standard_should_i_use),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val standards = listOf(
                StandardContext(
                    icon = Icons.Outlined.Assessment,
                    region = "United States",
                    standard = "ATP III / NCEP",
                    note = "Commonly used screening reference in the United States"
                ),
                StandardContext(
                    icon = Icons.Outlined.Public,
                    region = "Europe & International",
                    standard = "IDF",
                    note = "Includes ethnicity-specific waist references"
                ),
                StandardContext(
                    icon = Icons.Outlined.Science,
                    region = "Research / Historical",
                    standard = "WHO",
                    note = "Requires laboratory and clinical information not collected here"
                ),
                StandardContext(
                    icon = Icons.Outlined.LocalHospital,
                    region = "Your healthcare professional",
                    standard = "Varies",
                    note = "They can choose the reference most appropriate for your situation"
                )
            )

            standards.forEach { context ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = context.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = context.region,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = context.standard,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = context.note,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_this_app_uses_atp_iii_as_the_p),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}
