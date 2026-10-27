package com.health.calculator.bmi.tracker.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * ─── VIBRANT WELLNESS PALETTE ──────────────────────────────────────────
 *
 * Every feature area has its own colour personality so the dashboard
 * reads as a curated, editorial surface — not a template.
 *
 *   Water  → ocean blues         Calories → warm sunset coral
 *   Weight → fresh lime-green    Heart    → living rose
 *   Steps  → sky cyan            BP       → deep magenta
 *   BMI    → rich violet         Sleep    → twilight indigo
 *
 * Rule: never use a one-off Color() literal in a feature screen.
 * Add it here first, give it a name, and explain its job.
 * ────────────────────────────────────────────────────────────────────────
 */

// ─── LIGHT SCHEME ──────────────────────────────────────────────────────

val PrimaryLight = Color(0xFF2E623A)        // Rich forest sage green — calm, grounded, high contrast
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD9E8D7) // Soft fresh sage tint
val OnPrimaryContainerLight = Color(0xFF0E2914) // Deep forest green

val SecondaryLight = Color(0xFF2E6B5E)       // Botanical mint — fresh & approachable
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFD4F0E8)
val OnSecondaryContainerLight = Color(0xFF052A23)

val TertiaryLight = Color(0xFF9E4A2E)        // Warm terracotta coral
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFFFDE5DC)
val OnTertiaryContainerLight = Color(0xFF3A1408)

val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val BackgroundLight = Color(0xFFF6F8F5)      // Linen / fresh porcelain sage — calm, not glaring white
val OnBackgroundLight = Color(0xFF162018)    // Deep forest charcoal
val SurfaceLight = Color(0xFFFFFFFF)         // Clean elevated white
val OnSurfaceLight = Color(0xFF162018)
val SurfaceVariantLight = Color(0xFFEBF1EA)  // Soft sage tint
val OnSurfaceVariantLight = Color(0xFF475649)// Muted sage slate
val SurfaceTintLight = PrimaryLight
val InverseSurfaceLight = Color(0xFF2B332C)
val InverseOnSurfaceLight = Color(0xFFEFF3EE)
val InversePrimaryLight = Color(0xFFA5C896)

// ─── DARK SCHEME ───────────────────────────────────────────────────────

val PrimaryDark = Color(0xFFA5C896)          // Calming sage green — glows softly on dark surfaces
val OnPrimaryDark = Color(0xFF121B13)        // Deep dark forest charcoal
val PrimaryContainerDark = Color(0xFF223526) // Muted deep moss container
val OnPrimaryContainerDark = Color(0xFFEDEFEA)

val SecondaryDark = Color(0xFF81C784)        // Fresh botanical light green
val OnSecondaryDark = Color(0xFF101B11)
val SecondaryContainerDark = Color(0xFF1C2F20)
val OnSecondaryContainerDark = Color(0xFFEDEFEA)

val TertiaryDark = Color(0xFFFFB74D)         // Warm gold/amber accent (from Understand card)
val OnTertiaryDark = Color(0xFF2A1604)
val TertiaryContainerDark = Color(0xFF382715)
val OnTertiaryContainerDark = Color(0xFFFFE0B2)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

val BackgroundDark = Color(0xFF0D130E)       // Deep forest charcoal — calm, rich organic black
val OnBackgroundDark = Color(0xFFEDEFEA)     // Clean soft cream-white
val SurfaceDark = Color(0xFF141D16)          // Dark moss surface
val OnSurfaceDark = Color(0xFFEDEFEA)
val SurfaceVariantDark = Color(0xFF1A241C)   // Subtle elevated dark moss
val OnSurfaceVariantDark = Color(0xFFA6B4A6) // Muted sage grey
val SurfaceTintDark = PrimaryDark
val InverseSurfaceDark = Color(0xFFEDEFEA)
val InverseOnSurfaceDark = Color(0xFF141D16)
val InversePrimaryDark = PrimaryLight

val OutlineLight = Color(0xFF748376)
val OutlineVariantLight = Color(0xFFD7E2D5)
val OutlineDark = Color(0xFF667E6B)          // Functional outline
val OutlineVariantDark = Color(0xFF243326)   // Subtle decorative outline
val ScrimLight = Color(0xFF000000)
val ScrimDark = Color(0xFF000000)

// ─── FEATURE GRADIENTS ─────────────────────────────────────────────────

/**
 * Each tracker/feature pair includes a start colour, an end colour, and a
 * text-on-gradient colour so every card can paint its own identity while
 * staying legible.
 */
object FeatureColors {
    // Water — deep ocean → bright sky
    val WaterStart = Color(0xFF0EA5E9)
    val WaterEnd = Color(0xFF38BDF8)
    val WaterDeep = Color(0xFF0284C7)
    val OnWater = Color(0xFFFFFFFF)

    // Weight — fresh green → lime
    val WeightStart = Color(0xFF10B981)
    val WeightEnd = Color(0xFF34D399)
    val WeightDeep = Color(0xFF059669)
    val OnWeight = Color(0xFFFFFFFF)

    // Calories — sunset orange → warm coral
    val CalorieStart = Color(0xFFF97316)
    val CalorieEnd = Color(0xFFFB923C)
    val CalorieDeep = Color(0xFFEA580C)
    val OnCalorie = Color(0xFFFFFFFF)

    // Steps — cyan → turquoise
    val StepsStart = Color(0xFF06B6D4)
    val StepsEnd = Color(0xFF22D3EE)
    val StepsDeep = Color(0xFF0891B2)
    val OnSteps = Color(0xFFFFFFFF)

    // Heart Rate — rose → pink
    val HeartStart = Color(0xFFF43F5E)
    val HeartEnd = Color(0xFFFB7185)
    val HeartDeep = Color(0xFFE11D48)
    val OnHeart = Color(0xFFFFFFFF)

    // Blood Pressure — deep magenta → fuchsia
    val BpStart = Color(0xFFD946EF)
    val BpEnd = Color(0xFFE879F9)
    val BpDeep = Color(0xFFC026D3)
    val OnBp = Color(0xFFFFFFFF)

    // BMI — violet → purple
    val BmiStart = Color(0xFF8B5CF6)
    val BmiEnd = Color(0xFFA78BFA)
    val BmiDeep = Color(0xFF7C3AED)
    val OnBmi = Color(0xFFFFFFFF)

    // Sleep/Wellness — indigo → twilight
    val SleepStart = Color(0xFF6366F1)
    val SleepEnd = Color(0xFF818CF8)
    val SleepDeep = Color(0xFF4F46E5)
    val OnSleep = Color(0xFFFFFFFF)

    // AI Coach — vibrant violet → luminous fuchsia
    val AiStart = Color(0xFF8B5CF6)
    val AiEnd = Color(0xFFEC4899)
    val AiDeep = Color(0xFF7C3AED)
    val OnAi = Color(0xFFFFFFFF)
}

/** Explicit roles used by the redesigned surfaces. */
object WellnessPalette {
    val ActionLight = PrimaryLight
    val ActionDark = PrimaryDark
    val OnTrackLight = Color(0xFFD8E9D6)
    val OnTrackDark = Color(0xFF1B3821)
    val WarmAccentLight = Color(0xFF9E4A2E)
    val WarmAccentDark = Color(0xFFFFB74D)
    val QuietBorderLight = Color(0xFFD7E2D5)
    val QuietBorderDark = Color(0xFF243326)

    // The single hero surface has a calm, organic wellness gradient.
    val HeroStart = Color(0xFF2A5734) // Deep Forest Green
    val HeroEnd = Color(0xFF3A7548)   // Fresh Botanical Green
    val OnHero = Color(0xFFFFFFFF)

    val MetricSurfaceLight = Color(0xFFF0F5EE)
    val MetricSurfaceDark = Color(0xFF1A241C)
    val QuietSurfaceLight = Color(0xFFFFFFFF)
    val QuietSurfaceDark = Color(0xFF141D16)
}

/** Semantic colors for interpretation, never as the only signal of meaning. */
object HealthColors {
    val Healthy = Color(0xFF10B981)
    val HealthyLight = Color(0xFFD1FAE5)
    val HealthyDark = Color(0xFF34D399)

    val Good = Color(0xFF0EA5E9)
    val GoodLight = Color(0xFFE0F2FE)
    val GoodDark = Color(0xFF38BDF8)

    val Warning = Color(0xFFF59E0B)
    val WarningLight = Color(0xFFFEF3C7)
    val WarningDark = Color(0xFFFBBF24)

    val Caution = Color(0xFFF97316)
    val CautionLight = Color(0xFFFFF7ED)
    val CautionDark = Color(0xFFFB923C)

    val Danger = Color(0xFFEF4444)
    val DangerLight = Color(0xFFFEE2E2)
    val DangerDark = Color(0xFFF87171)

    val Severe = Color(0xFFA855F7)
    val SevereLight = Color(0xFFF3E8FF)
    val SevereDark = Color(0xFFC084FC)

    val BelowNormal = Color(0xFF06B6D4)
    val BelowNormalLight = Color(0xFFCFFAFE)
    val BelowNormalDark = Color(0xFF22D3EE)

    val Info = Color(0xFF6366F1)
    val InfoLight = Color(0xFFEEF2FF)
    val InfoDark = Color(0xFF818CF8)
}

/** A vibrant chart palette that pops in both themes. */
object ChartColors {
    val Primary = Color(0xFF2E623A)
    val Secondary = Color(0xFFF43F5E)
    val Tertiary = Color(0xFFF97316)
    val Accent1 = Color(0xFF10B981)
    val Accent2 = Color(0xFF0EA5E9)
    val Accent3 = Color(0xFFA855F7)

    val GridLine = Color(0xFFD7E2D5)
    val GridLineDark = Color(0xFF243326)
    val GradientStart = Primary
    val GradientEnd = Color(0x332E623A)
    val GradientStartDark = Color(0xFFA5C896)
    val GradientEndDark = Color(0x33A5C896)
}

/** Stable accents for calculator discovery cards — each feels unique. */
object CalculatorColors {
    val BMI = Color(0xFF8B5CF6)
    val BMR = Color(0xFFF59E0B)
    val BloodPressure = Color(0xFFF43F5E)
    val WaistToHip = Color(0xFFA855F7)
    val WaterIntake = Color(0xFF0EA5E9)
    val MetabolicSyndrome = Color(0xFFF97316)
    val BSA = Color(0xFF06B6D4)
    val IdealWeight = Color(0xFF10B981)
    val DailyCalorie = Color(0xFFEA580C)
    val HeartRateZone = Color(0xFFE11D48)
}

/** Natural supporting tones for the optional hydration plant illustration. */
object WellnessIllustrationColors {
    val Soil = Color(0xFF6D4C41)
    val Pot = Color(0xFFBCAAA4)
    val PotShade = Color(0xFF8D6E63)
}

// Compatibility aliases used by older feature screens.
val HealthGreen = HealthColors.Healthy
val HealthYellow = HealthColors.Warning
val HealthOrange = HealthColors.Caution
val HealthRed = HealthColors.Danger
val HealthBlue = HealthColors.Info
val HealthTeal = HealthColors.BelowNormal

/** Dark-only extended roles. Existing light expressions are passed through by AppColors. */
object DarkRoles {
    val Level1 = Color(0xFF141D16) // Base surface
    val Level2 = Color(0xFF1A241C) // Elevated surface
    val Level3 = Color(0xFF223024) // Card / interactive surface
    val Text = Color(0xFFEDEFEA)
    val SecondaryText = Color(0xFFA6B4A6)
    val DisabledText = Color(0xFF8A9A8C)
    val SubtleOutline = Color(0xFF243326)
    // Functional boundaries need 3:1; subtle outlines are decorative only.
    val FunctionalOutline = Color(0xFF667E6B)
    val Accent = Color(0xFFA5C896)
    val WarningOutline = Color(0xFFFBBF24)
}
