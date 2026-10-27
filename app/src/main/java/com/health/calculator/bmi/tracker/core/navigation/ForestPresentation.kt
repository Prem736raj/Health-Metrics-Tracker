package com.health.calculator.bmi.tracker.core.navigation

/** These reference screens use the forest appearance in both day and night mode. */
internal fun usesForestPresentation(route: String?): Boolean = when (route) {
    Screen.Track.route, Screen.Calculators.route, Screen.Insights.route, Screen.Profile.route -> true
    else -> false
}

/** True means dark system icons, suitable for the app's ordinary light surfaces. */
internal fun usesLightSystemBars(darkAppTheme: Boolean): Boolean = !darkAppTheme
