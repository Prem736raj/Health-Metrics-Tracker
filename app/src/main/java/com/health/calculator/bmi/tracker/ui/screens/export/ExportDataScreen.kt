package com.health.calculator.bmi.tracker.ui.screens.export

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.data.export.ExportDisclosurePolicy
import com.health.calculator.bmi.tracker.ui.components.SupportingLeafMark
import com.health.calculator.bmi.tracker.ui.components.detail.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportDataScreen(
    onNavigateBack: () -> Unit,
    onOpenHistoryExport: () -> Unit,
    onOpenHealthReport: () -> Unit,
    onOpenDataManagement: () -> Unit
) {
    DetailScreenTheme(DetailFeature.EXPORT) {
        Scaffold(topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { Text(stringResource(R.string.export_backup_title)) }, navigationIcon = {
                IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
            })
        }) { padding ->
            LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SupportingLeafMark()
                        Text(stringResource(R.string.export_backup_subtitle), style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                    }
                }
                item {
                    OutlinedCard(onClick = onOpenHealthReport, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Health report", style = MaterialTheme.typography.titleLarge)
                            Text("Choose calculators and trackers, preview your records, and share a PDF.")
                        }
                    }
                }
                item {
                    OutlinedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Icon(Icons.Outlined.IosShare, null, tint = DetailColors.Accent, modifier = Modifier.size(32.dp))
                            Text(stringResource(R.string.history_export_button), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                            HorizontalDivider(color = DetailColors.Outline)
                            Text("${ExportDisclosurePolicy.INFORMATIONAL_LABEL} only. Choose the sections and format you want before sharing.", style = MaterialTheme.typography.bodyMedium, color = DetailColors.Secondary)
                            Button(onClick = onOpenHistoryExport, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                                Text(stringResource(R.string.history_export_button))
                            }
                        }
                    }
                }
                item {
                    OutlinedCard(onClick = onOpenDataManagement, modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Icon(Icons.Outlined.FolderOpen, null, tint = DetailColors.Accent)
                            Text(stringResource(R.string.data_management_button), modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
