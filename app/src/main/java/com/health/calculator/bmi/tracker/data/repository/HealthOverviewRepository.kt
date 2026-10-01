package com.health.calculator.bmi.tracker.data.repository

import javax.inject.Inject
import com.health.calculator.bmi.tracker.core.navigation.Screen

import com.health.calculator.bmi.tracker.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class HealthOverviewRepository @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val waterGamificationRepository: WaterGamificationRepository,
    private val foodLogRepository: FoodLogRepository
) {

    fun getHealthOverview(): Flow<HealthOverview> {
        return combine(
            historyRepository.getLatestByType(CalculatorType.BMI.key),
            historyRepository.getLatestByType(CalculatorType.BLOOD_PRESSURE.key),
            historyRepository.getLatestByType(CalculatorType.WHR.key),
            historyRepository.getLatestByType(CalculatorType.BMR.key),
            historyRepository.getLatestByType(CalculatorType.METABOLIC_SYNDROME.key),
            waterGamificationRepository.observeStreakData(),
            foodLogRepository.getWeeklyAdherence()
        ) { args ->
            val bmi = args[0] as HistoryEntry?
            val bp = args[1] as HistoryEntry?
            val whr = args[2] as HistoryEntry?
            val bmr = args[3] as HistoryEntry?
            val metabolic = args[4] as HistoryEntry?
            val waterStreakData = args[5] as? WaterStreakData // Use safe cast just in case
            val calorieAdherence = args[6] as Float

            val waterStreak = waterStreakData?.currentStreak ?: 0
            val wellnessScore = calculateWellnessScore(bmi, bp, whr, waterStreak, calorieAdherence)

            HealthOverview(
                latestBmi = bmi?.toHealthMetricSummary("BMI", Screen.BmiCalculator.route),
                latestBp = bp?.toHealthMetricSummary("Blood Pressure", Screen.BloodPressureCalculator.route),
                latestWhr = whr?.toHealthMetricSummary("Waist-to-Hip", Screen.WaistToHipCalculator.route),
                latestBmr = bmr?.toHealthMetricSummary("BMR", Screen.BmrCalculator.route),
                waterStreak = waterStreak,
                calorieAdherence = calorieAdherence,
                metabolicSyndromeStatus = metabolic?.toHealthMetricSummary("Metabolic Risk", Screen.MetabolicSyndromeCalculator.route),
                healthScore = wellnessScore
            )
        }
    }

    private fun HistoryEntry.toHealthMetricSummary(label: String, route: String): HealthMetricSummary {
        return HealthMetricSummary(
            label = label,
            value = resultValue + (resultLabel?.let { " $it" } ?: ""),
            category = category ?: "Unknown",
            categoryColor = HealthCategoryPresentation.colorFor(category),
            lastUpdated = timestamp,
            navigateRoute = route
        )
    }

    /**
     * Build the legacy integer field from data availability and logging only.
     * Measurement values are deliberately never graded as good or bad.
     */
    private fun calculateWellnessScore(
        bmi: HistoryEntry?,
        bp: HistoryEntry?,
        whr: HistoryEntry?,
        waterStreak: Int,
        calorieAdherence: Float
    ): Int {
        val components = listOf(
            bmi != null,
            bp != null,
            whr != null,
            waterStreak > 0,
            calorieAdherence > 0f
        )
        val available = components.count { it }
        return if (available == 0) -1 else (available * 100) / components.size
    }
}
