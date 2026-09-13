// File: com/health/calculator/bmi/tracker/data/model/BMREducationalContent.kt
package com.health.calculator.bmi.tracker.data.model

data class EducationalSection(
    val title: String,
    val emoji: String,
    val content: List<EducationalParagraph>
)

data class EducationalParagraph(
    val text: String,
    val isBullet: Boolean = false,
    val isHighlight: Boolean = false,
    val isTip: Boolean = false,
    val isWarning: Boolean = false
)

object BMREducationalContent {

    val sections = listOf(
        EducationalSection(
            title = "What is BMR?",
            emoji = "🔥",
            content = listOf(
                EducationalParagraph(
                    text = "Basal Metabolic Rate (BMR) is the number of calories your body needs to perform its most basic life-sustaining functions — like breathing, blood circulation, cell production, and nutrient processing."
                ),
                EducationalParagraph(
                    text = "Think of it as the energy cost of simply being alive, even if you stayed in bed all day and didn't move at all."
                ),
                EducationalParagraph(
                    text = "BMR is often the largest part of daily energy use, but the share varies with body size, activity, food intake and measurement conditions.",
                    isHighlight = true
                ),
                EducationalParagraph(
                    text = "Your BMR is measured under very strict conditions: complete rest, a thermally neutral environment, and after a 12-hour fast."
                )
            )
        ),
        EducationalSection(
            title = "BMR vs TDEE",
            emoji = "⚡",
            content = listOf(
                EducationalParagraph(
                    text = "BMR and TDEE are related but different:"
                ),
                EducationalParagraph(
                    text = "BMR (Basal Metabolic Rate) — An estimate of energy used at rest under controlled conditions; it is not a safe personal calorie floor.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "TDEE (Total Daily Energy Expenditure) — An estimate that combines resting energy with activity and digestion. Activity multipliers already approximate the total, so components should not be added twice.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "For planning, TDEE can provide a starting estimate. Real-world weight trends, appetite, health context and professional guidance matter more than a single calculated number.",
                    isHighlight = true
                ),
                EducationalParagraph(
                    text = "Example: multiplying a BMR estimate by 1.55 is one planning convention for a moderately active day, not a measurement of your actual energy use."
                )
            )
        ),
        EducationalSection(
            title = "Factors Affecting BMR",
            emoji = "🧬",
            content = listOf(
                EducationalParagraph(
                    text = "Several factors influence your metabolic rate:"
                ),
                EducationalParagraph(
                    text = "Body size & composition — Larger bodies generally use more energy at rest. Lean mass is one factor, but this calculator cannot measure body composition directly.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Age — Energy needs often change with age as body composition, activity and health context change; no single percentage applies to everyone.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Sex-related averages — Many equations use sex-specific coefficients, but individual body composition and physiology vary widely.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Individual variation — Genetics, health, medication and prior weight change can affect energy needs; this calculator cannot quantify those effects.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Hormones and health — Hormonal or other health conditions can affect energy and wellbeing. Persistent concerns deserve professional evaluation rather than a self-adjusted calorie target.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Illness and temperature — Fever or illness can change energy needs, but this adult estimate is not designed to calculate those changes.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Environment — Climate can influence activity, appetite and fluid needs. Effects on resting energy are not reliably estimated by this calculator.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Pregnancy & lactation — this adult estimate does not adjust for pregnancy or breastfeeding. Energy needs vary by stage and individual context; ask a qualified prenatal or postpartum professional for guidance.",
                    isBullet = true
                )
            )
        ),
        EducationalSection(
            title = "How to Boost Your Metabolism",
            emoji = "🚀",
            content = listOf(
                EducationalParagraph(
                    text = "While you can't dramatically change your BMR, these evidence-based strategies can help:"
                ),
                EducationalParagraph(
                    text = "Build strength — Resistance training can support muscle and overall function; the effect on resting energy varies between people.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Stay active throughout the day — Walking, standing and other movement add to daily energy use, but the amount varies widely.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Include adequate protein from varied foods. A gram-per-kilogram range is only a planning reference; needs differ with health, age and activity.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Prioritize sleep — Consistent, restorative sleep supports wellbeing and activity; individual sleep needs vary.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Stay hydrated — Drink regularly and use thirst, food and climate as context rather than chasing a universal litre target.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Choose an eating pattern you can sustain. Meal timing is personal; extreme restriction can make adequate nutrition harder.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "Manage stress — Rest, support and enjoyable movement can help wellbeing; avoid claims that one hormone explains an individual's weight.",
                    isTip = true
                )
            )
        ),
        EducationalSection(
            title = "Why Crash Diets Harm Your BMR",
            emoji = "⚠️",
            content = listOf(
                EducationalParagraph(
                    text = "Very restrictive diets can make adequate nutrition, energy and long-term adherence difficult:",
                    isWarning = true
                ),
                EducationalParagraph(
                    text = "Energy adaptation — Energy use can change during weight loss, and estimates are uncertain; avoid treating a percentage as a personal prediction.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Loss of lean tissue — Rapid loss can affect body composition, which is one reason a gradual, adequately nourished approach may be easier to sustain.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Regain is common when a plan is too restrictive to maintain. A flexible routine and support can be more sustainable.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "Individual responses differ, and restrictive eating can affect energy, mood and nutrition. Discuss persistent concerns with a professional.",
                    isBullet = true
                ),
                EducationalParagraph(
                    text = "If weight change is your goal, use small, sustainable adjustments and review trends over time; a professional can help choose an appropriate target.",
                    isHighlight = true
                ),
                EducationalParagraph(
                    text = "Do not use BMR as a minimum intake rule. Very low targets should be discussed with a qualified professional.",
                    isWarning = true
                )
            )
        ),
        EducationalSection(
            title = "BMR and Weight Management",
            emoji = "📊",
            content = listOf(
                EducationalParagraph(
                    text = "Understanding your BMR can provide context for weight-management planning, but it is only one estimate among many inputs:"
                ),
                EducationalParagraph(
                    text = "For weight change — Consider gradual adjustments and watch multi-week trends rather than relying on an exact weekly prediction.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "For weight gain — Add nourishing foods gradually and consider strength training if appropriate for you; individual guidance is useful.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "For maintenance — A calculated TDEE can be a starting estimate. Compare it with multi-week trends and seek professional guidance if your needs or health context are changing.",
                    isTip = true
                ),
                EducationalParagraph(
                    text = "There is no universal calorie floor from BMR. Energy needs are individual, and very low targets deserve professional review.",
                    isHighlight = true
                ),
                EducationalParagraph(
                    text = "Track regularly — Revisit the estimate when your inputs or circumstances change. Your energy needs may change as your body and routine change."
                ),
                EducationalParagraph(
                    text = "Remember: BMR is an estimate with meaningful individual uncertainty. Use it as context, compare with real-world trends and avoid treating it as a personal calorie floor."
                )
            )
        )
    )
}
