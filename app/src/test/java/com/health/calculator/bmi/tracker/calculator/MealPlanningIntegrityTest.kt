package com.health.calculator.bmi.tracker.calculator

import com.health.calculator.bmi.tracker.domain.usecase.MealPlanningUseCase
import org.junit.Assert.*
import org.junit.Test

class MealPlanningIntegrityTest {
    private val planner = MealPlanningUseCase()

    @Test fun everyMealCountPreservesTheDailyTotals() {
        for (count in 2..6) {
            val plan = planner.createMealPlan(2000.0, 150.0, 200.0, 600.0 / 9.0, count)
            assertEquals(count, plan.meals.size)
            assertEquals(2000.0, plan.meals.sumOf { it.calories }, 0.001)
            assertEquals(150.0, plan.meals.sumOf { it.proteinGrams }, 0.001)
            assertEquals(200.0, plan.meals.sumOf { it.carbGrams }, 0.001)
        }
    }

    @Test fun invalidCustomSharesCannotCreateMissingOrExtraFoodEnergy() {
        val invalid = listOf(listOf(30f, 30f, 30f), listOf(-10f, 60f, 50f), listOf(Float.NaN, 50f, 50f), listOf(50f, 50f))
        for (shares in invalid) {
            assertThrows(IllegalArgumentException::class.java) {
                planner.createMealPlan(2000.0, 150.0, 200.0, 600.0 / 9.0, 3, shares)
            }
        }
    }

    @Test fun workoutPortionCaloriesAgreeWithTheirMacroEnergy() {
        val result = planner.createWorkoutNutrition(2000.0, 150.0, 200.0, 600.0 / 9.0, "Morning", true)
        for (meal in listOfNotNull(result.preWorkoutMeal, result.postWorkoutMeal)) {
            assertEquals(meal.calories, meal.proteinGrams * 4 + meal.carbGrams * 4 + meal.fatGrams * 9, 0.001)
        }
    }

    @Test fun invalidInputsAndUnsupportedWindowsAreRejected() {
        for (calories in listOf(0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY)) {
            assertThrows(IllegalArgumentException::class.java) { planner.createMealPlan(calories, 100.0, 100.0, 50.0, 3) }
        }
        val plan = planner.createMealPlan(2000.0, 150.0, 200.0, 600.0 / 9.0, 3)
        assertThrows(IllegalArgumentException::class.java) { planner.createIFPlan(plan, "unknown", 12) }
        assertThrows(IllegalArgumentException::class.java) { planner.createIFPlan(plan, "16:8", 24) }
    }

    @Test fun windowAcrossMidnightWrapsToAValidHour() {
        val plan = planner.createMealPlan(2000.0, 150.0, 200.0, 600.0 / 9.0, 3)
        val window = planner.createIFPlan(plan, "16:8", 20)
        assertEquals(4, window.windowEndHour)
        assertEquals(3, window.mealsInWindow.size)
    }
}
