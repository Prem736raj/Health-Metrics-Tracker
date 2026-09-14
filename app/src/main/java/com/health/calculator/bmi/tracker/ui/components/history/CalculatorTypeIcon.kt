package com.health.calculator.bmi.tracker.ui.components.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.health.calculator.bmi.tracker.data.model.CalculatorType

/**
 * One icon vocabulary for history filters and history entries.
 *
 * CalculatorType still exposes a legacy emoji for exports and backwards
 * compatibility, but interactive UI should use platform-independent vectors.
 */
internal fun calculatorTypeIcon(type: CalculatorType): ImageVector = when (type) {
    CalculatorType.BMI -> Icons.Outlined.Calculate
    CalculatorType.BMR -> Icons.Outlined.LocalFireDepartment
    CalculatorType.BLOOD_PRESSURE -> Icons.Outlined.MonitorHeart
    CalculatorType.WHR -> Icons.Outlined.Straighten
    CalculatorType.WATER_INTAKE -> Icons.Outlined.WaterDrop
    CalculatorType.METABOLIC_SYNDROME -> Icons.Outlined.HealthAndSafety
    CalculatorType.BSA -> Icons.Outlined.Person
    CalculatorType.IBW -> Icons.Outlined.MonitorWeight
    CalculatorType.CALORIE -> Icons.Outlined.Restaurant
    CalculatorType.HEART_RATE -> Icons.Outlined.FavoriteBorder
}
