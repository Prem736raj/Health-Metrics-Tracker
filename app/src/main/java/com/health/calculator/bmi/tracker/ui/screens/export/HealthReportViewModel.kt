package com.health.calculator.bmi.tracker.ui.screens.export

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.health.calculator.bmi.tracker.HealthCalculatorApp
import com.health.calculator.bmi.tracker.data.export.DataExportManager
import com.health.calculator.bmi.tracker.data.export.ExportFormat
import com.health.calculator.bmi.tracker.data.export.HealthReport
import com.health.calculator.bmi.tracker.data.export.HealthReportPdf
import com.health.calculator.bmi.tracker.data.export.HealthReportRepository
import com.health.calculator.bmi.tracker.data.export.HealthReportSnapshot
import com.health.calculator.bmi.tracker.data.export.ReportPeriod
import com.health.calculator.bmi.tracker.data.export.ReportTool
import com.health.calculator.bmi.tracker.data.export.ReportWindow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal data class HealthReportState(
    val period: ReportPeriod = ReportPeriod.MONTH,
    val report: HealthReport? = null,
    val selected: Set<ReportTool> = ReportTool.entries.toSet(),
    val loading: Boolean = true,
    val sharing: Boolean = false,
    val loadFailed: Boolean = false,
    val shareFailed: Boolean = false
)

@HiltViewModel
internal class HealthReportViewModel @Inject constructor(
    application: Application,
    private val savedState: SavedStateHandle
) : AndroidViewModel(application) {
    private val app = application as HealthCalculatorApp
    private val repository = HealthReportRepository(app.database, app.foodLogRepository)
    private val pdf = HealthReportPdf(application)
    private val exporter = DataExportManager.getInstance(application)
    private val initialSelection = savedState.get<ArrayList<String>>("reportTools")
        ?.map(ReportTool::valueOf)?.toSet() ?: ReportTool.entries.toSet()
    private val _state = MutableStateFlow(HealthReportState(selected = initialSelection))
    val state = _state.asStateFlow()
    private var loadingJob: Job? = null

    init {
        load(savedState.get<String>("reportPeriod")?.let(ReportPeriod::valueOf) ?: ReportPeriod.MONTH)
    }

    fun load(period: ReportPeriod) {
        if (_state.value.sharing) return
        loadingJob?.cancel()
        savedState["reportPeriod"] = period.name
        _state.update { it.copy(period = period, loading = true, report = null, loadFailed = false, shareFailed = false) }
        loadingJob = viewModelScope.launch {
            try {
                val report = repository.load(ReportWindow(period))
                _state.update { it.copy(report = report, loading = false) }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.update { it.copy(loading = false, loadFailed = true) }
            }
        }
    }

    fun select(tools: Set<ReportTool>) {
        savedState["reportTools"] = ArrayList(tools.map { it.name })
        _state.update { it.copy(selected = tools, shareFailed = false) }
    }

    fun share(snapshot: HealthReportSnapshot) {
        if (snapshot.sections.isEmpty() || _state.value.sharing) return
        _state.update { it.copy(sharing = true, shareFailed = false) }
        viewModelScope.launch {
            try {
                val uri = withContext(Dispatchers.IO) { pdf.generate(snapshot) }
                exporter.shareFile(uri, ExportFormat.PDF)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _state.update { it.copy(shareFailed = true) }
            } finally {
                _state.update { it.copy(sharing = false) }
            }
        }
    }
}
