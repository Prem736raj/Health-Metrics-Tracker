package com.health.calculator.bmi.tracker.ui.components.home

import androidx.compose.ui.graphics.Color
import com.health.calculator.bmi.tracker.ui.theme.CalculatorColors

/**
 * Data class to hold all calculator card states
 */
data class CalculatorCardsState(
    // BMI
    val lastBMI: Float? = null,
    val lastBMICategory: String? = null,
    
    // BMR
    val lastBMR: Int? = null,
    val lastTDEE: Int? = null,
    
    // Blood Pressure
    val lastBPSystolic: Int? = null,
    val lastBPDiastolic: Int? = null,
    val lastBPCategory: String? = null,
    
    // WHR
    val lastWHR: Float? = null,
    val lastWHRCategory: String? = null,
    
    // Water
    val waterIntakeToday: Int = 0,
    val waterGoalToday: Int = 0,
    
    // Metabolic Syndrome
    val metabolicCriteriaMet: Int? = null,
    
    // BSA
    val lastBSA: Float? = null,
    
    // IBW
    val idealBodyWeight: Float? = null,
    val currentWeight: Float? = null,
    
    // Calories
    val caloriesConsumedToday: Int = 0,
    val calorieTargetToday: Int = 0,
    
    // Heart Rate
    val maxHeartRate: Int? = null,
    val restingHeartRate: Int? = null
)

/**
 * Calculator info for static display
 */
data class CalculatorInfo(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val route: String,
    val accentColor: Color
)

val calculatorInfoList = listOf(
    CalculatorInfo(
        id = "bmi",
        emoji = "📊",
        title = "BMI Calculator",
        description = "Calculate your Body Mass Index",
        route = "bmi_calculator",
        accentColor = CalculatorColors.BMI
    ),
    CalculatorInfo(
        id = "bmr",
        emoji = "🔥",
        title = "BMR Calculator",
        description = "Calculate your Basal Metabolic Rate",
        route = "bmr_calculator",
        accentColor = CalculatorColors.BMR
    ),
    CalculatorInfo(
        id = "bp",
        emoji = "💓",
        title = "Blood Pressure",
        description = "Check your blood pressure category",
        route = "blood_pressure_checker",
        accentColor = CalculatorColors.BloodPressure
    ),
    CalculatorInfo(
        id = "whr",
        emoji = "📏",
        title = "Waist-to-Hip Ratio",
        description = "Assess your body fat distribution",
        route = "whr_calculator",
        accentColor = CalculatorColors.WaistToHip
    ),
    CalculatorInfo(
        id = "water",
        emoji = "💧",
        title = "Water Intake",
        description = "Track your daily hydration",
        route = "water_intake_calculator",
        accentColor = CalculatorColors.WaterIntake
    ),
    CalculatorInfo(
        id = "metabolic",
        emoji = "🏥",
        title = "Metabolic Syndrome",
        description = "Assess your metabolic health risk",
        route = "metabolic_syndrome_checker",
        accentColor = CalculatorColors.MetabolicSyndrome
    ),
    CalculatorInfo(
        id = "bsa",
        emoji = "📐",
        title = "Body Surface Area",
        description = "Calculate your body surface area",
        route = "bsa_calculator",
        accentColor = CalculatorColors.BSA
    ),
    CalculatorInfo(
        id = "ibw",
        emoji = "⚖️",
        title = "Ideal Body Weight",
        description = "Find your ideal weight range",
        route = "ibw_calculator",
        accentColor = CalculatorColors.IdealWeight
    ),
    CalculatorInfo(
        id = "calorie",
        emoji = "🔥",
        title = "Daily Calories",
        description = "Track your daily calorie intake",
        route = "calorie_calculator",
        accentColor = CalculatorColors.DailyCalorie
    ),
    CalculatorInfo(
        id = "heartrate",
        emoji = "❤️",
        title = "Heart Rate Zones",
        description = "Optimize your training intensity",
        route = "heart_rate_zone_calculator",
        accentColor = CalculatorColors.HeartRateZone
    )
)
