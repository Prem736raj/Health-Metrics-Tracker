// ui/screens/bloodpressure/BpExportScreen.kt
package com.health.calculator.bmi.tracker.ui.screens.bloodpressure

import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.DetailScreenTheme
import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.pluralStringResource
import com.health.calculator.bmi.tracker.R


import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.health.calculator.bmi.tracker.ui.theme.HealthColors
import com.health.calculator.bmi.tracker.data.export.BpVisitPeriod
import com.health.calculator.bmi.tracker.data.export.BpVisitReport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BpExportScreen(
    onNavigateBack: () -> Unit,
    viewModel: BpExportViewModel = hiltViewModel()
) = DetailScreenTheme(DetailFeature.BLOOD_PRESSURE) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val visitReport by viewModel.visitReport.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.txt_export_share), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (DetailColors.isDark) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            visitReport?.let { report ->
                BpVisitReportCard(
                    report = report,
                    isGenerating = uiState.isGeneratingVisitReport,
                    showError = uiState.visitReportFailed,
                    onSelectPeriod = viewModel::selectVisitPeriod,
                    onShare = { viewModel.shareVisitReport(report) }
                )
            }

            // Stats summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DetailColors.surface(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ExportStatItem(
                        value = "${uiState.totalReadings}",
                        label = "Total Readings",
                        icon = Icons.Outlined.MonitorHeart
                    )
                    ExportStatItem(
                        value = uiState.dateRange,
                        label = "Date Range",
                        icon = Icons.Outlined.DateRange
                    )
                }
            }

            // Export options
            Text(
                stringResource(R.string.txt_full_report),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // PDF Report
            ExportOptionCard(
                icon = Icons.Outlined.PictureAsPdf,
                iconColor = HealthColors.Danger,
                title = "PDF Report",
                description = "Complete report with all readings, averages, and distribution. Perfect for personal records.",
                buttonText = "Generate PDF",
                isLoading = uiState.isGeneratingPdf,
                isEnabled = uiState.totalReadings > 0,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onExportPdf()
                }
            )

            // CSV Export
            ExportOptionCard(
                icon = Icons.Outlined.TableChart,
                iconColor = HealthColors.Healthy,
                title = "CSV Spreadsheet",
                description = "Raw data in spreadsheet format. Import into Excel, Google Sheets, or other tools.",
                buttonText = "Export CSV",
                isLoading = uiState.isGeneratingCsv,
                isEnabled = uiState.totalReadings > 0,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.onExportCsv()
                }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Share Last Reading
            Text(
                stringResource(R.string.txt_share_reading),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (uiState.hasLatestReading) {
                // Share as text
                ExportOptionCard(
                    icon = Icons.Outlined.TextSnippet,
                    iconColor = HealthColors.Severe,
                    title = "Share as Text",
                    description = "Share your latest reading (${uiState.latestReadingText}) as formatted text message.",
                    buttonText = "Share Text",
                    isLoading = false,
                    isEnabled = true,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.onShareLatestAsText()
                    }
                )

                // Share as image
                ExportOptionCard(
                    icon = Icons.Outlined.Image,
                    iconColor = HealthColors.Caution,
                    title = "Share as Image",
                    description = "Share your latest reading as a beautifully styled card image.",
                    buttonText = "Share Image",
                    isLoading = uiState.isGeneratingImage,
                    isEnabled = true,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.onShareLatestAsImage()
                    }
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = DetailColors.surface(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stringResource(R.string.txt_no_readings_to_share_yet_take_),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DetailColors.text(DetailColors.Secondary),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Success snackbar
    if (uiState.showSuccess) {
        val context = LocalContext.current
        LaunchedEffect(uiState.showSuccess) {
            viewModel.dismissSuccess()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BpVisitReportCard(
    report: BpVisitReport,
    isGenerating: Boolean,
    showError: Boolean,
    onSelectPeriod: (BpVisitPeriod) -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(R.string.bp_visit_report), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.bp_visit_contents), style = MaterialTheme.typography.bodyMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BpVisitPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = report.period == period,
                        onClick = { onSelectPeriod(period) },
                        enabled = !isGenerating,
                        label = { Text(stringResource(R.string.bp_visit_last_days, period.days)) }
                    )
                }
            }
            Text(report.dateRange, style = MaterialTheme.typography.bodyMedium)
            if (report.readings.isEmpty()) {
                Text(stringResource(R.string.bp_visit_empty), style = MaterialTheme.typography.bodyMedium)
            } else {
                val count = report.readings.size
                Text(pluralStringResource(R.plurals.bp_log_reading_count, count, count))
                Text(
                    stringResource(R.string.bp_visit_days_covered, report.daysWithReadings, report.period.days),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (showError) {
                Text(stringResource(R.string.bp_visit_share_error), color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = onShare,
                modifier = Modifier.fillMaxWidth(),
                enabled = report.readings.isNotEmpty() && !isGenerating
            ) {
                Text(stringResource(if (isGenerating) R.string.txt_generating else R.string.bp_visit_share_pdf))
            }
        }
    }
}

@Composable
private fun ExportStatItem(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = DetailColors.text(DetailColors.Secondary)
        )
    }
}

@Composable
private fun ExportOptionCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    buttonText: String,
    isLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = DetailColors.text(DetailColors.Secondary)
                    )
                }
            }

            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = isEnabled && !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = iconColor
                ),
                border = BorderStroke(1.dp, iconColor.copy(alpha = 0.3f))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = iconColor,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.txt_generating), fontWeight = FontWeight.Medium)
                } else {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(buttonText, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
