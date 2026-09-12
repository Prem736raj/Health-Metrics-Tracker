package com.health.calculator.bmi.tracker.ui.screens.calorie

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthColors

/** Legacy education labels remain in the content model, but are rendered as
 * stable Material icons so the guide is consistent across fonts and themes. */
private fun calorieEducationIcon(label: String): ImageVector = when {
    label.contains("⚡") -> Icons.Outlined.Bolt
    label.contains("🥗") || label.contains("🥩") || label.contains("🍚") || label.contains("🍽️") || label.contains("🥦") || label.contains("🥑") || label.contains("🍎") || label.contains("☕") -> Icons.Outlined.Restaurant
    label.contains("🔬") || label.contains("🧬") -> Icons.Outlined.Science
    label.contains("📐") || label.contains("⚖") -> Icons.Outlined.Straighten
    label.contains("⚠") || label.contains("🚨") -> Icons.Outlined.Warning
    label.contains("💪") || label.contains("🏋") -> Icons.Outlined.FitnessCenter
    label.contains("📊") || label.contains("📋") -> Icons.Outlined.Assessment
    label.contains("🔄") -> Icons.Outlined.Timeline
    label.contains("🎯") -> Icons.Outlined.Flag
    label.contains("🥤") -> Icons.Outlined.WaterDrop
    label.contains("📱") -> Icons.Outlined.Info
    label.contains("🌱") -> Icons.Outlined.FavoriteBorder
    label.contains("👩") || label.contains("👨") || label.contains("Women") || label.contains("Men") -> Icons.Outlined.Person
    else -> Icons.Outlined.Info
}

private val LeadingLegacyMarker = Regex("^[\\p{So}\\p{Sk}\\p{M}\\p{Cf}\\s]+")
private fun cleanLegacyMarker(value: String): String = value.replaceFirst(LeadingLegacyMarker, "").trim()

@Composable
fun CalorieEducationalContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
            Icon(
                Icons.Default.MenuBook,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(R.string.txt_calorie_education),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Section 1: Understanding Calories
        EducationCard(
            icon = Icons.Default.Lightbulb,
            iconColor = HealthColors.Warning,
            title = "Understanding Calories",
            content = { UnderstandingCaloriesContent() }
        )

        // Section 2: Deficit vs Surplus
        EducationCard(
            icon = Icons.Default.Balance,
            iconColor = HealthColors.Good,
            title = "Calorie Deficit vs Surplus",
            content = { DeficitVsSurplusContent() }
        )

        // Section 3: Danger of Too Few Calories
        EducationCard(
            icon = Icons.Default.Warning,
            iconColor = HealthColors.Danger,
            title = "The Danger of Too Few Calories",
            content = { TooFewCaloriesContent() }
        )

        // Section 4: Calories in Common Foods
        EducationCard(
            icon = Icons.Default.Restaurant,
            iconColor = HealthColors.Healthy,
            title = "Calories in Common Foods",
            content = { CommonFoodsReferenceContent() }
        )

        // Section 5: Accurate Tracking Tips
        EducationCard(
            icon = Icons.Default.CheckCircle,
            iconColor = HealthColors.Severe,
            title = "Tips for Accurate Calorie Tracking",
            content = { AccurateTrackingTipsContent() }
        )

        // Mindful Note
        MindfulNoteCard()
        
        // Medical Disclaimer
        MedicalDisclaimerEducation()
    }
}

@Composable
private fun EducationCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = iconColor.copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(200))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    content()
                }
            }
        }
    }
}

// ─── SECTION 1: Understanding Calories ───────────────────────────────────────

@Composable
private fun UnderstandingCaloriesContent() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        EduParagraph(
            "A calorie (kcal) is a unit of energy. Specifically, it's the amount of energy needed to raise the temperature of 1 kilogram of water by 1°C. Your body uses calories from food as its primary fuel source."
        )

        EduHighlight(
            emoji = "⚡",
            title = "Energy In, Energy Out",
            text = "Your body uses calories in three main ways:\n\n• BMR (Basal Metabolic Rate): ~60-75% — energy to keep you alive at rest (breathing, circulation, cell repair)\n• Physical Activity: ~15-30% — energy for movement and exercise\n• TEF (Thermic Effect of Food): ~10% — energy to digest and absorb food",
            color = HealthColors.Warning
        )

        EduHighlight(
            emoji = "🥗",
            title = "Quality Matters as Much as Quantity",
            text = "Calorie balance is one part of weight change, while food quality affects nutrition, satiety, and overall wellbeing:\n\n• 500 calories from broccoli and chicken provides a different mix of fibre, vitamins, minerals, and protein than 500 calories from cookies\n• Equal energy does not guarantee equal fullness or nutrition\n\nA sustainable approach combines an appropriate intake with foods you enjoy and can consistently access.",
            color = HealthColors.Healthy
        )

        EduHighlight(
            emoji = "🔬",
            title = "Not All Calories Are Processed Equally",
            text = "Your body processes different foods differently:\n• Protein generally has a higher thermic effect than carbohydrate or fat\n• Fat is energy-dense and is efficiently stored\n• Whole foods often provide more fibre and take longer to digest than many highly processed foods\n• Fibre can slow digestion and glucose absorption, although responses vary",
            color = HealthColors.Good
        )
    }
}

// ─── SECTION 2: Deficit vs Surplus ───────────────────────────────────────────

@Composable
private fun DeficitVsSurplusContent() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        EduParagraph("Weight change is fundamentally governed by the balance between calories consumed and calories burned over time.")

        // Visual balance scale
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = HealthColors.Good.copy(alpha = 0.08f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrendingDown,
                        contentDescription = null,
                        tint = HealthColors.Good,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        stringResource(R.string.txt_deficit),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = HealthColors.Good
                    )
                    Text(
                        "Calories In\n< Calories Out",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = HealthColors.Good
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.txt_weight_loss),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = HealthColors.Good
                    )
                }
            }
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                color = HealthColors.Severe.copy(alpha = 0.08f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrendingUp,
                        contentDescription = null,
                        tint = HealthColors.Severe,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        stringResource(R.string.txt_surplus),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = HealthColors.Severe
                    )
                    Text(
                        "Calories In\n> Calories Out",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        color = HealthColors.Severe
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.txt_weight_gain),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = HealthColors.Severe
                    )
                }
            }
        }

        EduHighlight(
            emoji = "📐",
            title = "A calorie-balance estimate",
            text = "A commonly used estimate is that 1 kg of body fat stores roughly 7,700 kcal.\n\n• 500 kcal/day deficit × 7 days ≈ 3,500 kcal/week\n• 1,000 kcal/day deficit × 7 days ≈ 7,000 kcal/week\n\nThese are simplified estimates, not promises. Real-world change varies with water shifts, muscle, appetite, activity, medicines, and metabolic adaptation.",
            color = HealthColors.Good
        )

        EduHighlight(
            emoji = "⚠️",
            title = "Why Extreme Deficits Backfire",
            text = "Cutting intake too sharply can make a plan harder to sustain:\n\n• Energy, concentration, training, and recovery may suffer\n• Inadequate protein and nutrients can increase the risk of muscle loss\n• Hunger and restriction can make eating patterns harder to manage\n• Adaptive changes in appetite and energy expenditure can slow progress\n\nA gradual approach with enough food and nutrients is usually easier to maintain. Consider a qualified professional for personalised guidance.",
            color = HealthColors.Caution
        )

        EduHighlight(
            emoji = "💪",
            title = "Lean Bulk vs Aggressive Surplus",
            text = "For weight gain/muscle building:\n\n• Lean bulk (250 cal surplus): slower but mostly muscle gain\n• Aggressive surplus: faster weight gain but more fat accumulation\n• Your body can only synthesize a limited amount of muscle per week regardless of calorie surplus\n• Excess calories beyond muscle synthesis needs are stored as fat",
            color = HealthColors.Severe
        )
    }
}

// ─── SECTION 3: Too Few Calories ─────────────────────────────────────────────

@Composable
private fun TooFewCaloriesContent() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = HealthColors.Danger.copy(alpha = 0.08f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                Icon(
                    Icons.Default.Warning, null,
                    tint = HealthColors.Danger, modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.txt_eating_too_few_calories_can_be),
                    style = MaterialTheme.typography.bodySmall,
                    color = HealthColors.Danger.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )
            }
        }

        // Minimum recommendations
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    stringResource(R.string.txt_minimum_daily_calorie_recommen),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MinCalCard("Women", "Varies", "individual needs", HealthColors.Caution)
                    MinCalCard("Men", "Varies", "individual needs", HealthColors.Good)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    stringResource(R.string.txt_these_are_absolute_minimums_mo),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }

        // Consequences
        listOf(
            Triple("🧬", "Metabolic Adaptation",
                "When intake stays well below needs, the body can adapt through changes in appetite, movement, and energy expenditure. The size and timing of these changes vary; 'survival mode' is not a precise diagnosis."),
            Triple("💪", "Muscle Loss",
                "Very low intake can make it harder to meet protein and training needs, which may increase the risk of losing lean tissue."),
            Triple("🥦", "Nutrient Gaps",
                "Very low-calorie diets can make it difficult to get enough vitamins, minerals, protein, fibre, and essential fats. Symptoms depend on the person and duration."),
            Triple("⚖️", "Changes in Appetite and Hormones",
                "Severe restriction can affect appetite signals, reproductive hormones, thyroid-related measures, mood, and stress responses. Persistent symptoms deserve professional support.")
        ).forEach { (emoji, title, text) ->
            EduHighlight(emoji = emoji, title = title, text = text, color = HealthColors.Danger)
        }

        // Warning signs
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = HealthColors.Danger.copy(alpha = 0.06f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    stringResource(R.string.txt_signs_you_may_be_eating_too_li),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = HealthColors.Danger
                )
                Spacer(modifier = Modifier.height(8.dp))
                val signs = listOf(
                    "Constantly thinking about food", "Low energy and fatigue",
                    "Difficulty concentrating", "Frequent illness",
                    "Irritability and mood changes", "Hair loss or thinning",
                    "Feeling cold all the time", "Weight loss plateau despite restriction",
                    "Dizziness when standing quickly", "Loss of menstrual cycle (women)"
                )
                signs.chunked(2).forEach { pair ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        pair.forEach { sign ->
                            Row(modifier = Modifier.weight(1f).padding(vertical = 2.dp)) {
                                Text("• ", color = HealthColors.Danger, style = MaterialTheme.typography.bodySmall)
                                Text(sign, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                            }
                        }
                        if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MinCalCard(label: String, value: String, unit: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = calorieEducationIcon(label),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(cleanLegacyMarker(label), style = MaterialTheme.typography.labelSmall, color = color)
            }
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(unit, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.7f))
        }
    }
}

// ─── SECTION 4: Common Foods Reference ───────────────────────────────────────

private data class FoodCategoryRef(
    val name: String,
    val emoji: String,
    val color: Color,
    val foods: List<Pair<String, String>> // name to "X kcal / serving"
)

@Composable
private fun CommonFoodsReferenceContent() {
    val categories = remember {
        listOf(
            FoodCategoryRef(
                "Proteins", "🥩", FeatureColors.HeartStart,
                listOf(
                    "Chicken breast (100g)" to "165 kcal",
                    "Salmon (100g)" to "208 kcal",
                    "Egg (1 large)" to "72 kcal",
                    "Tuna, canned (100g)" to "116 kcal",
                    "Greek yogurt (100g)" to "59 kcal",
                    "Beef, lean (100g)" to "215 kcal",
                    "Tofu (100g)" to "76 kcal",
                    "Whey protein (1 scoop)" to "120 kcal"
                )
            ),
            FoodCategoryRef(
                "Carbohydrates", "🍚", FeatureColors.StepsStart,
                listOf(
                    "Rice, white (1 cup, cooked)" to "206 kcal",
                    "Oatmeal (1 cup, cooked)" to "166 kcal",
                    "Bread, white (1 slice)" to "79 kcal",
                    "Bread, whole wheat (1 slice)" to "69 kcal",
                    "Pasta (1 cup, cooked)" to "220 kcal",
                    "Sweet potato (1 medium)" to "103 kcal",
                    "Potato (1 medium, baked)" to "161 kcal",
                    "Quinoa (1 cup, cooked)" to "222 kcal"
                )
            ),
            FoodCategoryRef(
                "Fats", "🥑", HealthColors.Healthy,
                listOf(
                    "Avocado (1 medium)" to "234 kcal",
                    "Olive oil (1 tbsp)" to "119 kcal",
                    "Butter (1 tbsp)" to "102 kcal",
                    "Almonds (28g/1oz)" to "164 kcal",
                    "Peanut butter (1 tbsp)" to "95 kcal",
                    "Cheddar cheese (28g)" to "114 kcal",
                    "Whole milk (1 cup)" to "149 kcal",
                    "Coconut oil (1 tbsp)" to "121 kcal"
                )
            ),
            FoodCategoryRef(
                "Fruits", "🍎", HealthColors.Caution,
                listOf(
                    "Apple (1 medium)" to "95 kcal",
                    "Banana (1 medium)" to "105 kcal",
                    "Orange (1 medium)" to "62 kcal",
                    "Grapes (1 cup)" to "104 kcal",
                    "Strawberries (1 cup)" to "49 kcal",
                    "Blueberries (1 cup)" to "84 kcal",
                    "Mango (1 cup, diced)" to "99 kcal",
                    "Watermelon (1 cup, diced)" to "46 kcal"
                )
            ),
            FoodCategoryRef(
                "Vegetables", "🥦", HealthColors.Healthy,
                listOf(
                    "Broccoli (1 cup)" to "55 kcal",
                    "Spinach (1 cup, raw)" to "7 kcal",
                    "Carrot (1 medium)" to "25 kcal",
                    "Tomato (1 medium)" to "22 kcal",
                    "Cucumber (1 cup)" to "16 kcal",
                    "Bell pepper (1 medium)" to "31 kcal",
                    "Lettuce (1 cup, raw)" to "5 kcal",
                    "Onion (1 medium)" to "44 kcal"
                )
            ),
            FoodCategoryRef(
                "Beverages", "☕", HealthColors.Info,
                listOf(
                    "Coffee, black (1 cup)" to "2 kcal",
                    "Tea, unsweetened (1 cup)" to "2 kcal",
                    "Orange juice (1 cup)" to "112 kcal",
                    "Whole milk (1 cup)" to "149 kcal",
                    "Cola (1 can, 355ml)" to "140 kcal",
                    "Beer (355ml, regular)" to "153 kcal",
                    "Red wine (1 glass, 148ml)" to "125 kcal",
                    "Sports drink (500ml)" to "125 kcal"
                )
            )
        )
    }

    var expandedCategory by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EduParagraph("Quick reference guide for common foods. Use this while logging to estimate calorie content.")

        categories.forEach { category ->
            val isExpanded = expandedCategory == category.name

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedCategory = if (isExpanded) null else category.name
                    },
                shape = RoundedCornerShape(12.dp),
                color = category.color.copy(alpha = 0.06f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = calorieEducationIcon(category.emoji),
                                contentDescription = null,
                                tint = category.color,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                category.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = category.color
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = category.color.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "${category.foods.size} foods",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = category.color,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 9.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                null,
                                modifier = Modifier.size(18.dp),
                                tint = category.color.copy(alpha = 0.6f)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            HorizontalDivider(color = category.color.copy(alpha = 0.2f), modifier = Modifier.padding(bottom = 8.dp))

                            // Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    stringResource(R.string.txt_food_item),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    stringResource(R.string.txt_calories),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            category.foods.forEachIndexed { index, (name, cal) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier.weight(1f),
                                        fontSize = 12.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = category.color.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            cal,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = category.color,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                if (index < category.foods.lastIndex) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    stringResource(R.string.txt_values_are_approximate_and_may),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 10.sp, lineHeight = 14.sp
                )
            }
        }
    }
}

// ─── SECTION 5: Accurate Tracking Tips ───────────────────────────────────────

@Composable
private fun AccurateTrackingTipsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        EduParagraph(
            "Accurate tracking is the foundation of effective calorie management. Here's how to make your logs as reliable as possible:"
        )

        val tips = listOf(
            Triple("⚖️", "Use a Food Scale When Possible",
                "Volume measurements (cups, tablespoons) are imprecise. A food scale measuring in grams provides much more accurate calorie counts. Even a 20% variation in portion size can mean 100-200 extra calories per meal."),
            Triple("🫒", "Don't Forget Cooking Oils and Sauces",
                "A single tablespoon of olive oil adds ~120 calories. Butter, sauces, dressings, and condiments are among the most commonly forgotten items. They add up quickly and are often the reason people \"can't figure out why they're not losing weight.\""),
            Triple("🥤", "Track All Beverages",
                "Liquid calories are often completely overlooked:\n• Juice: ~110 kcal/cup\n• Coffee with cream & sugar: ~50-150 kcal\n• Smoothies: 200-600 kcal\n• Alcohol: 100-250 kcal/drink\n\nDrink water or unsweetened beverages to reduce hidden calories."),
            Triple("📊", "Be Honest With Yourself",
                "Research consistently shows people underreport food intake by 20-50%. Common mistakes:\n• Estimating smaller portions than actual\n• Forgetting snacks and tastes while cooking\n• Not counting 'just a few' chips or bites\n\nAccurate tracking (even if imperfect) is far more valuable than inaccurate tracking."),
            Triple("🔄", "Consistency Beats Perfection",
                "You don't need a perfect day every day. Consistent, honest tracking that's occasionally off is more useful than sporadic perfect tracking. Aim for 80-90% accuracy consistently rather than 100% occasionally."),
            Triple("🍽️", "Weigh Food Before Cooking",
                "Cooking changes the weight of food. Meat shrinks as it cooks (water loss). Pasta and rice absorb water and expand. Always log the raw weight or check the label for 'cooked' values explicitly."),
            Triple("📱", "Log in Real Time",
                "The longer you wait to log a meal, the less accurate your memory becomes. Log food as you eat it or immediately after. 'I'll remember later' often leads to forgotten snacks and inaccurate estimations."),
            Triple("🎯", "Focus on Trends, Not Single Days",
                "One day of overeating won't derail progress. One day of under-eating won't fix a week of excess. Focus on weekly averages rather than obsessing over individual days.")
        )

        tips.forEach { (emoji, title, text) ->
            EduHighlight(emoji = emoji, title = title, text = text, color = FeatureColors.CalorieDeep)
        }
    }
}

// ─── SHARED HELPERS ──────────────────────────────────────────────────────────

@Composable
private fun EduParagraph(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        lineHeight = 22.sp
    )
}

@Composable
private fun EduHighlight(emoji: String, title: String, text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = calorieEducationIcon(emoji),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun MindfulNoteCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = HealthColors.Healthy.copy(alpha = 0.08f)
        )
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = HealthColors.Healthy,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    stringResource(R.string.txt_a_mindful_approach_to_tracking),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = HealthColors.Healthy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stringResource(R.string.txt_calorie_tracking_is_a_tool_not),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp, fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun MedicalDisclaimerEducation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = HealthColors.Danger.copy(alpha = 0.06f)
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Default.MedicalInformation, null,
                tint = HealthColors.Danger.copy(alpha = 0.7f), modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    stringResource(R.string.txt_medical_disclaimer),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = HealthColors.Danger.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    stringResource(R.string.txt_this_educational_content_is_fo_3),
                    style = MaterialTheme.typography.bodySmall,
                    color = HealthColors.Danger.copy(alpha = 0.7f),
                    fontSize = 11.sp, lineHeight = 16.sp
                )
            }
        }
    }
}
