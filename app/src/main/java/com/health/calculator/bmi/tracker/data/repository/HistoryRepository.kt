package com.health.calculator.bmi.tracker.data.repository

import javax.inject.Inject

import com.health.calculator.bmi.tracker.data.local.HistoryDao
import com.health.calculator.bmi.tracker.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Repository layer for calculation history data access.
 */
class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao
) {
    /** All history entries, ordered newest first */
    fun getAllEntries(): Flow<List<HistoryEntry>> {
        return historyDao.getAllEntries()
    }

    suspend fun getAllEntriesSync(limit: Int = Int.MAX_VALUE): List<HistoryEntry> {
        return historyDao.getAllEntriesSync(limit)
    }

    /** Total count of history entries */
    val entryCount: Flow<Int> = historyDao.getEntryCount()

    /** Get entries filtered by calculator type */
    fun getEntriesByType(type: CalculatorType): Flow<List<HistoryEntry>> {
        return historyDao.getEntriesByType(type.key)
    }

    /** Get the most recent entry for a specific calculator */
    fun getLatestEntry(type: CalculatorType): Flow<HistoryEntry?> {
        return historyDao.getLatestEntryByType(type.key)
    }

    fun getLatestByType(calculatorKey: String): Flow<HistoryEntry?> {
        return historyDao.getLatestEntryByType(calculatorKey)
    }

    suspend fun getLatestByTypeSync(calculatorKey: String): HistoryEntry? {
        return historyDao.getLatestEntryByTypeSync(calculatorKey)
    }

    suspend fun getConsecutiveDaysWithEntryType(typeKey: String): Int {
        val zone = java.time.ZoneId.systemDefault()
        val today = java.time.LocalDate.now(zone)
        // Bound by calendar dates, not 30 rows (one day can contain many logs).
        val entries = historyDao.getEntriesByTypeInRange(
            typeKey,
            today.minusDays(29).atStartOfDay(zone).toInstant().toEpochMilli(),
            today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        )
        return com.health.calculator.bmi.tracker.core.utils.CalendarStreak.count(
            entries.map { it.timestamp }, today, zone
        )
    }

    suspend fun getLastEntryDate(): Long? {
        return historyDao.getAllEntriesSync(1).firstOrNull()?.timestamp
    }

    /** Insert a new history entry, returns the generated ID */
    suspend fun addEntry(entry: HistoryEntry): Long {
        return historyDao.insertEntry(entry)
    }

    /** Delete entry by ID */
    suspend fun deleteEntry(id: Long) {
        historyDao.deleteEntryById(id)
    }

    suspend fun deleteEntries(ids: List<Long>): Int = historyDao.deleteEntriesAtomically(ids)

    /** Delete all history entries */
    suspend fun deleteAllEntries() {
        historyDao.deleteAllEntries()
    }

    suspend fun updateNote(id: Long, note: String) {
        historyDao.updateNote(id, note)
    }

    suspend fun clearAllHistory() {
        historyDao.deleteAllEntries()
    }

    suspend fun insertParsedEntry(entry: ParsedHistoryEntry) {
        val historyEntry = HistoryEntry(
            calculatorKey = entry.calculatorKey,
            resultValue = entry.primaryValue.toString(),
            resultLabel = entry.primaryLabel,
            category = entry.category,
            timestamp = entry.timestamp,
            detailsJson = entry.details.entries.joinToString("|") { "${it.key}:${it.value}" },
            note = entry.note
        )
        historyDao.insertEntry(historyEntry)
    }

    suspend fun getEntryById(id: Long): Map<String, String>? {
        val entry = historyDao.getById(id) ?: return null
        return mapOf(
            "id" to entry.id.toString(),
            "calculator_key" to entry.calculatorKey,
            "result_value" to entry.resultValue,
            "result_label" to (entry.resultLabel ?: ""),
            "category" to (entry.category ?: ""),
            "timestamp" to entry.timestamp.toString(),
            "note" to (entry.note ?: ""),
            "details_json" to (entry.detailsJson ?: "")
        )
    }

    suspend fun restoreEntryFromMap(data: Map<String, String>): Long {
        val entry = HistoryEntry(
            calculatorKey = data["calculator_key"] ?: "",
            resultValue = data["result_value"] ?: "",
            resultLabel = data["result_label"] ?: "",
            category = data["category"]?.takeIf { it.isNotBlank() },
            timestamp = data["timestamp"]?.toLongOrNull() ?: System.currentTimeMillis(),
            note = data["note"]?.takeIf { it.isNotBlank() },
            detailsJson = data["details_json"]?.takeIf { it.isNotBlank() }
        )
        return historyDao.insertEntry(entry)
    }

    suspend fun getEntriesCountOlderThan(cutoffTimestamp: Long): Int {
        return historyDao.countOlderThan(cutoffTimestamp)
    }

    suspend fun getEntriesCountByType(calculatorKey: String): Int {
        return historyDao.countByType(calculatorKey)
    }

    /**
     * Returns a map of calculator keys to their most recent calculation timestamps.
     */
    suspend fun getLastCalculatedTimes(): Map<String, Long> {
        return historyDao.getLastCalculatedTimes().associate { 
            it.calculatorKey to it.lastCalcTime 
        }
    }

    suspend fun getTotalCalculationCount(): Int {
        return historyDao.getEntryCount().first()
    }

    suspend fun getDistinctCalculatorTypes(): Set<String> {
        return historyDao.getDistinctCalculatorKeys().toSet()
    }

    suspend fun getFirstEntryDate(): Long? {
        return historyDao.getFirstEntryTimestamp()
    }

    suspend fun getMostUsedCalculator(): String? {
        return historyDao.getMostUsedCalculator()
    }

    suspend fun getEntriesByTypeInRange(typeKey: String, startTime: Long, endTime: Long): List<ParsedHistoryEntry> {
        return historyDao.getEntriesByTypeInRange(typeKey, startTime, endTime).map { it.toParsedEntry() }
    }

    suspend fun getCalculatorUsageCounts(): List<Pair<String, Int>> {
        return historyDao.getUsageCounts().map { it.key to it.count }
    }
}
