package com.health.calculator.bmi.tracker.ui.screens.export

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.health.calculator.bmi.tracker.data.export.ReportPeriod
import com.health.calculator.bmi.tracker.data.export.ReportSection
import com.health.calculator.bmi.tracker.data.export.ReportWindow
import com.health.calculator.bmi.tracker.ui.components.detail.DetailFeature
import com.health.calculator.bmi.tracker.ui.components.detail.DetailScreenTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun HealthReportScreen(onBack: () -> Unit, viewModel: HealthReportViewModel = hiltViewModel()) =
    DetailScreenTheme(DetailFeature.EXPORT) {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val report = state.report
        val snapshot = remember(report, state.selected) { report?.snapshot(state.selected) }
        val sections = report?.sections.orEmpty()
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Health report") }, navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                })
            },
            bottomBar = {
                Surface(tonalElevation = 3.dp) {
                    Column(Modifier.navigationBarsPadding().fillMaxWidth().padding(16.dp)) {
                        if (state.shareFailed) Text("Couldn’t share the PDF. Try again.", color = MaterialTheme.colorScheme.error)
                        Button(
                            onClick = { snapshot?.let(viewModel::share) },
                            enabled = !state.sharing && snapshot?.sections?.isNotEmpty() == true,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                        ) {
                            Icon(Icons.Outlined.PictureAsPdf, null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (state.sharing) "Preparing PDF…" else "Share health report PDF")
                        }
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ElevatedCard(Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Build your report", style = MaterialTheme.typography.titleLarge)
                            Text("Choose a period and the records you want to share.")
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ReportPeriod.entries.forEach { period ->
                                    FilterChip(selected = state.period == period, onClick = { viewModel.load(period) },
                                        enabled = !state.sharing, label = { Text("Last ${period.days} days") })
                                }
                            }
                            report?.let { Text(it.window.title, style = MaterialTheme.typography.labelLarge) }
                        }
                    }
                }
                when {
                    state.loading -> item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
                    state.loadFailed -> item {
                        Column {
                            Text("Couldn’t load your records.")
                            TextButton(onClick = { viewModel.load(state.period) }) { Text("Retry") }
                        }
                    }
                    sections.isEmpty() -> item { Text("No saved results or logs in this period.") }
                    else -> {
                        val window = requireNotNull(report).window
                        item {
                            val allSelected = sections.all { it.tool in state.selected }
                            val preview = requireNotNull(snapshot)
                            Column {
                                Text("${preview.recordCountLabel} from ${preview.sectionCountLabel}",
                                    style = MaterialTheme.typography.titleMedium)
                                Text("The PDF leaves out names, personal notes and medication details.",
                                    style = MaterialTheme.typography.bodySmall)
                                TextButton(enabled = !state.sharing, onClick = {
                                    val visible = sections.map { it.tool }.toSet()
                                    viewModel.select(if (allSelected) state.selected - visible else state.selected + visible)
                                }) { Text(if (allSelected) "Clear selection" else "Select all") }
                            }
                        }
                        items(sections, key = { it.tool }) { section ->
                            ReportSectionCard(section, window, section.tool in state.selected,
                                enabled = !state.sharing, onSelect = { checked ->
                                    viewModel.select(if (checked) state.selected + section.tool else state.selected - section.tool)
                                })
                        }
                    }
                }
            }
        }
    }

@Composable
private fun ReportSectionCard(section: ReportSection, window: ReportWindow, checked: Boolean,
    enabled: Boolean, onSelect: (Boolean) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked, onSelect, enabled = enabled, modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .semantics { contentDescription = "Include ${section.tool.title}" })
                Row(Modifier.weight(1f).clickable { expanded = !expanded }.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(section.tool.title, style = MaterialTheme.typography.titleMedium)
                        val count = section.records.size
                        Text(if (count == 1) "1 record" else "$count records", style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        if (expanded) "Collapse preview" else "Expand preview")
                }
            }
            if (expanded) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    section.records.take(3).forEach { record ->
                        Column {
                            Text(record.result, style = MaterialTheme.typography.titleSmall)
                            Text(window.date(record.timestamp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (section.records.size > 3) Text("The PDF contains the full selection.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
