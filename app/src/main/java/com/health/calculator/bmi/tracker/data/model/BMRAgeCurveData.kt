// File: com/health/calculator/bmi/tracker/data/model/BMRAgeCurveData.kt
package com.health.calculator.bmi.tracker.data.model

object BMRAgeCurveData {

    // Average BMR values by age group (kcal/day)
    // Based on population averages from medical literature
    data class AgeBMRPoint(
        val age: Int,
        val maleBMR: Float,
        val femaleBMR: Float
    )

    val ageCurvePoints = listOf(
        AgeBMRPoint(15, 1820f, 1460f),
        AgeBMRPoint(20, 1850f, 1450f),
        AgeBMRPoint(25, 1830f, 1430f),
        AgeBMRPoint(30, 1800f, 1400f),
        AgeBMRPoint(35, 1770f, 1380f),
        AgeBMRPoint(40, 1740f, 1350f),
        AgeBMRPoint(45, 1710f, 1325f),
        AgeBMRPoint(50, 1675f, 1300f),
        AgeBMRPoint(55, 1640f, 1275f),
        AgeBMRPoint(60, 1600f, 1250f),
        AgeBMRPoint(65, 1565f, 1225f),
        AgeBMRPoint(70, 1530f, 1200f),
        AgeBMRPoint(75, 1490f, 1175f),
        AgeBMRPoint(80, 1450f, 1150f)
    )

    fun getAverageBMRForAge(age: Int, isMale: Boolean): Float {
        val clampedAge = age.coerceIn(15, 80)
        val points = ageCurvePoints

        // Find surrounding points for interpolation
        val lower = points.lastOrNull { it.age <= clampedAge } ?: points.first()
        val upper = points.firstOrNull { it.age >= clampedAge } ?: points.last()

        if (lower.age == upper.age) {
            return if (isMale) lower.maleBMR else lower.femaleBMR
        }

        val fraction = (clampedAge - lower.age).toFloat() / (upper.age - lower.age).toFloat()
        val lowerBMR = if (isMale) lower.maleBMR else lower.femaleBMR
        val upperBMR = if (isMale) upper.maleBMR else upper.femaleBMR

        return lowerBMR + (upperBMR - lowerBMR) * fraction
    }

    fun getComparisonText(userBMR: Float, age: Int, isMale: Boolean): String {
        val average = getAverageBMRForAge(age, isMale)
        val diff = userBMR - average
        val percentage = (diff / average * 100f)
        val genderText = if (isMale) "men" else "women"

        return when {
            percentage > 15f -> "Your BMR estimate is higher than the reference average for $genderText your age " +
                    "(+${percentage.toInt()}%). This comparison reflects the inputs and equation; it cannot identify muscle mass or health."
            percentage > 5f -> "Your BMR estimate is above the reference average for $genderText your age " +
                    "(+${percentage.toInt()}%). A comparison like this is informational and does not measure fitness or health."
            percentage > -5f -> "Your BMR estimate is close to the reference average for $genderText your age. " +
                    "Expected variation is common; this is not a health assessment."
            percentage > -15f -> "Your BMR estimate is below the reference average for $genderText your age " +
                    "(${percentage.toInt()}%). Differences can reflect body-size inputs or model assumptions and are not diagnostic."
            else -> "Your BMR estimate is well below the reference average for $genderText your age " +
                    "(${percentage.toInt()}%). Verify the entries and discuss a persistent unexpected result with a qualified professional."
        }
    }

    fun getDecadeDeclineText(age: Int): String {
        return when {
            age < 25 -> "Age is one input in many BMR equations. This population reference is a comparison, not a forecast of your future metabolism."
            age < 35 -> "For adults in this age range, BMR estimates can vary with body size, body composition and the equation selected."
            age < 45 -> "Use consistent entries and the same equation when comparing estimates over time; a chart cannot explain the cause of a change."
            age < 55 -> "A BMR estimate is sensitive to its inputs. Compare like-for-like readings rather than treating the curve as a personal target."
            age < 65 -> "Population averages describe groups, not individuals. Consider professional context for a persistent unexpected change."
            else -> "This reference remains informational. It does not diagnose metabolic health or predict how your energy needs will change."
        }
    }
}
