package com.health.calculator.bmi.tracker.presentation.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.domain.insights.WellnessInsight
import com.health.calculator.bmi.tracker.presentation.components.MedicalDisclaimerShort
import com.health.calculator.bmi.tracker.ui.components.WellnessEmptyState
import com.health.calculator.bmi.tracker.ui.components.WellnessIconBadge
import com.health.calculator.bmi.tracker.ui.components.WellnessInsightCallout
import com.health.calculator.bmi.tracker.ui.components.home.HealthScoreBreakdownSheet
import com.health.calculator.bmi.tracker.ui.components.home.HomeSearchBar
import com.health.calculator.bmi.tracker.ui.theme.ActionRowShape
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import com.health.calculator.bmi.tracker.ui.theme.FeatureColors
import com.health.calculator.bmi.tracker.ui.theme.HealthSpacing
import com.health.calculator.bmi.tracker.ui.theme.MetricTileShape
import com.health.calculator.bmi.tracker.ui.theme.WellnessPalette
import com.health.calculator.bmi.tracker.util.HealthMetricsSnapshot
import com.health.calculator.bmi.tracker.util.HealthScoreCategory
import com.health.calculator.bmi.tracker.util.HealthScoreResult
import com.health.calculator.bmi.tracker.util.RecommendationType
import com.health.calculator.bmi.tracker.util.SmartRecommendation
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A serene botanical wellness dashboard matching the calm nature design system.
 */
@Composable
fun HomeScreen(
    onNavigateToBmi: () -> Unit,
    onNavigateToBmr: () -> Unit,
    onNavigateToBp: () -> Unit,
    onNavigateToWhr: () -> Unit,
    onNavigateToWater: () -> Unit,
    onNavigateToMetabolic: () -> Unit,
    onNavigateToBsa: () -> Unit,
    onNavigateToIbw: () -> Unit,
    onNavigateToCalorie: () -> Unit,
    onNavigateToHeartRate: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAiCoach: () -> Unit,
    onNavigateToWeight: () -> Unit,
    onNavigateToHealthConnections: () -> Unit,
    onNavigateToCalculators: () -> Unit,
    onNavigateToTrack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    searchViewModel: HomeSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    val recentSearches by searchViewModel.recentSearches.collectAsStateWithLifecycle()
    var showScoreBreakdown by remember { mutableStateOf(false) }

    val isDark = AppColors.isDark
    val bgDrawable = if (isDark) R.drawable.bg_onboarding_nature else R.drawable.bg_onboarding_nature_light

    BackHandler(enabled = searchQuery.isNotEmpty()) {
        searchViewModel.updateSearchQuery("")
    }

    val navigateRoute: (String) -> Unit = { route ->
        when (route) {
            "bmi_calculator" -> onNavigateToBmi()
            "bmr_calculator" -> onNavigateToBmr()
            "blood_pressure_calculator", "blood_pressure_checker", "bp_calculator" -> onNavigateToBp()
            "whr_calculator" -> onNavigateToWhr()
            "water_calculator", "water_intake_calculator" -> onNavigateToWater()
            "calorie_calculator" -> onNavigateToCalorie()
            "heart_rate_calculator", "heart_rate_zone_calculator" -> onNavigateToHeartRate()
            "metabolic_syndrome", "metabolic_syndrome_checker" -> onNavigateToMetabolic()
            "bsa_calculator" -> onNavigateToBsa()
            "ibw_calculator" -> onNavigateToIbw()
            "history" -> onNavigateToHistory()
            "profile" -> onNavigateToProfile()
            "weight_tracking" -> onNavigateToWeight()
            "health_connections" -> onNavigateToHealthConnections()
            "track" -> onNavigateToTrack()
            "calculators" -> onNavigateToCalculators()
            "ai_coach" -> onNavigateToAiCoach()
            "settings" -> onNavigateToSettings()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = bgDrawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = HealthSpacing.section + 72.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HomeHeader(
                    onOpenHistory = onNavigateToHistory,
                    onOpenSettings = onNavigateToSettings
                )
            }

            item {
                HomeSearchBar(
                    query = searchQuery,
                    onQueryChange = searchViewModel::updateSearchQuery,
                    results = searchResults,
                    recentSearches = recentSearches,
                    onClearRecent = searchViewModel::clearRecentSearches,
                    onRemoveRecent = searchViewModel::removeRecentSearch,
                    onResultClick = { result ->
                        searchViewModel.onSearchResultClick(result)
                        navigateRoute(result.navigationRoute)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                WellnessScoreCard(
                    result = uiState.healthScore,
                    onOpenDetails = { showScoreBreakdown = true },
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item {
                DailyMetricsSection(
                    state = uiState,
                    stepsTrend = uiState.stepsTrend,
                    onOpenSteps = onNavigateToHealthConnections,
                    onOpenWater = onNavigateToWater,
                    onOpenWeight = onNavigateToWeight,
                    onOpenCalories = onNavigateToCalorie,
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item {
                LatestMetricsSection(
                    metrics = uiState.healthMetrics,
                    onOpenBmi = onNavigateToBmi,
                    onOpenBloodPressure = onNavigateToBp,
                    onOpenHeartRate = onNavigateToHeartRate,
                    onOpenWater = onNavigateToWater,
                    onOpenWeight = onNavigateToWeight,
                    onOpenCalories = onNavigateToCalorie,
                    onOpenSteps = onNavigateToHealthConnections,
                    onStartCheckIn = onNavigateToCalculators,
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item {
                InsightPreviewSection(
                    insights = HomeDashboardPolicy.insightPreview(uiState.deterministicInsights),
                    recommendations = HomeDashboardPolicy.insightPreview(uiState.recommendations),
                    onOpenRecommendation = navigateRoute,
                    onDismissRecommendation = viewModel::dismissRecommendation,
                    onOpenAiAssistant = onNavigateToAiCoach,
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item {
                QuickActionsSection(
                    onOpenWater = onNavigateToWater,
                    onOpenWeight = onNavigateToWeight,
                    onOpenBloodPressure = onNavigateToBp,
                    onOpenCalculators = onNavigateToCalculators,
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item {
                RelevantCalculatorsCard(
                    onOpenBmi = onNavigateToBmi,
                    onOpenCalories = onNavigateToCalorie,
                    onOpenAll = onNavigateToCalculators,
                    modifier = Modifier.padding(horizontal = HealthSpacing.screenHorizontal)
                )
            }

            item { MedicalDisclaimerShort() }
        }
    }

    if (showScoreBreakdown) {
        HealthScoreBreakdownSheet(
            healthScore = uiState.healthScore,
            onDismiss = { showScoreBreakdown = false }
        )
    }
}

@Composable
private fun HomeHeader(
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val isDark = AppColors.isDark
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Welcome back"
    }
    val dateString = remember {
        try {
            SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
        } catch (_: Exception) {
            "Today"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isDark) Color(0xFF1B281E) else Color(0xFFE2EFE0),
                    border = BorderStroke(1.dp, if (isDark) Color(0xFF2B3D2E) else Color(0xFFC0D8BE)),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Text(
                        text = dateString.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = if (isDark) Color.White else Color(0xFF162018),
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    text = "Small steps make big changes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isDark) Color(0xFF9EACA0) else Color(0xFF475649)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    onClick = onOpenHistory,
                    shape = CircleShape,
                    color = if (isDark) Color(0xFF162219) else Color(0xFFFFFFFF),
                    border = BorderStroke(1.dp, if (isDark) Color(0xFF263728) else Color(0xFFDCE6DA)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Open history",
                            tint = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    onClick = onOpenSettings,
                    shape = CircleShape,
                    color = if (isDark) Color(0xFF162219) else Color(0xFFFFFFFF),
                    border = BorderStroke(1.dp, if (isDark) Color(0xFF263728) else Color(0xFFDCE6DA)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open settings",
                            tint = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WellnessScoreCard(
    result: HealthScoreResult,
    onOpenDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = AppColors.isDark
    val hasEnoughData = result.category != HealthScoreCategory.INSUFFICIENT_DATA
    val safeScore = result.totalScore.coerceIn(0, 100)
    val displayedProgress = if (hasEnoughData) safeScore / 100f else 0.35f
    val animatedProgress by animateFloatAsState(
        targetValue = displayedProgress,
        animationSpec = tween(durationMillis = 900),
        label = "wellness_score_ring"
    )

    Surface(
        onClick = onOpenDetails,
        shape = RoundedCornerShape(24.dp),
        color = if (isDark) Color(0xFF152218) else Color(0xFFF0F6EF),
        border = BorderStroke(1.dp, if (isDark) Color(0xFF273C2A) else Color(0xFFC8DEC6)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(92.dp),
                    strokeWidth = 8.dp,
                    color = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                    trackColor = if (isDark) Color(0xFF223525) else Color(0xFFD5E8D3),
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (hasEnoughData) safeScore.toString() else "--",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        ),
                        color = if (isDark) Color.White else Color(0xFF162018)
                    )
                    Text(
                        text = "/100",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = if (isDark) Color(0xFF869788) else Color(0xFF5A6B5D)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Wellness Score",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = if (isDark) Color.White else Color(0xFF162018)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = if (hasEnoughData) {
                        "Based on recent check-ins and daily logging."
                    } else {
                        "Log two things today to\nsee your score."
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 17.sp,
                        fontSize = 12.5.sp
                    ),
                    color = if (isDark) Color(0xFFB4C1B5) else Color(0xFF475649)
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = onOpenDetails,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                        contentColor = if (isDark) Color(0xFF121B13) else Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (hasEnoughData) "View score" else "Add a check-in",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Informational only · not clinically validated",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isDark) Color(0xFF748576) else Color(0xFF7A8B7D)
                )
            }
        }
    }
}

@Composable
private fun SectionHeading(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val isDark = AppColors.isDark
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = 18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                ),
                color = if (isDark) Color.White else Color(0xFF162018),
                modifier = Modifier.weight(1f)
            )
            if (actionText != null && onActionClick != null) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    ),
                    color = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                    modifier = Modifier.clickable { onActionClick() }
                )
            }
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                color = if (isDark) Color(0xFF889689) else Color(0xFF5A685D),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun ModernTrackerCard(
    icon: ImageVector,
    iconTint: Color,
    iconCircleBg: Color,
    rimColor: Color,
    label: String,
    value: String,
    supportingText: String,
    progress: Float?,
    progressColor: Color,
    onClick: () -> Unit,
    rightAction: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isDark = AppColors.isDark
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isDark) Color(0xFF131C15) else Color(0xFFFFFFFF),
        border = BorderStroke(1.dp, if (isDark) Color(0xFF223224) else Color(0xFFDCE6DA)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(46.dp)) {
                        drawCircle(
                            color = rimColor.copy(alpha = if (isDark) 0.18f else 0.12f),
                            style = Stroke(width = 3.dp.toPx())
                        )
                        drawArc(
                            color = rimColor,
                            startAngle = -90f,
                            sweepAngle = (progress ?: 0.35f) * 360f,
                            useCenter = false,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(iconCircleBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = if (isDark) Color(0xFF98A69A) else Color(0xFF5A685D),
                        maxLines = 1
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = if (isDark) Color.White else Color(0xFF162018),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (isDark) Color(0xFF768678) else Color(0xFF748577),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (rightAction != null) {
                    rightAction()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isDark) Color(0xFF1E2B20) else Color(0xFFEBF1EA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (progress ?: 0.12f).coerceIn(0.06f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(progressColor)
                )
            }
        }
    }
}

@Composable
private fun DailyMetricsSection(
    state: HomeUiState,
    stepsTrend: com.health.calculator.bmi.tracker.domain.tracking.TrackingComparison?,
    onOpenSteps: () -> Unit,
    onOpenWater: () -> Unit,
    onOpenWeight: () -> Unit,
    onOpenCalories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val metrics = state.healthMetrics
    val cards = state.calculatorCardsState
    val numberFormat = remember { NumberFormat.getIntegerInstance() }
    val weight = cards.currentWeight?.takeIf { it > 0f }
    val isDark = AppColors.isDark

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeading(
            title = "Today's Trackers",
            subtitle = "Small daily check-ins create powerful healthy habits"
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ModernTrackerCard(
                icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                iconTint = if (isDark) Color(0xFFA5C896) else Color(0xFF2E7D32),
                iconCircleBg = if (isDark) Color(0xFF1A281D) else Color(0xFFE8F5E9),
                rimColor = Color(0xFF81C784),
                label = "Steps",
                value = metrics.stepsToday?.let(numberFormat::format) ?: "Connect",
                supportingText = when {
                    metrics.stepsToday == null -> "Health Connect"
                    stepsTrend == null -> "synced today"
                    else -> String.format(Locale.getDefault(), "%+.0f%% vs last week", stepsTrend.percentChange)
                },
                progress = metrics.stepsToday?.let { (it.toFloat() / 10000f).coerceIn(0f, 1f) } ?: 0.12f,
                progressColor = Color(0xFF81C784),
                onClick = onOpenSteps,
                rightAction = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Steps details",
                        tint = if (isDark) Color(0xFF6B7B6D) else Color(0xFF9EACA0),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )

            val waterProgress = if (metrics.waterGoalToday > 0) {
                (metrics.waterIntakeToday.toFloat() / metrics.waterGoalToday).coerceIn(0f, 1f)
            } else 0.08f

            ModernTrackerCard(
                icon = Icons.Outlined.WaterDrop,
                iconTint = Color(0xFF38BDF8),
                iconCircleBg = if (isDark) Color(0xFF162326) else Color(0xFFE0F2FE),
                rimColor = Color(0xFF38BDF8),
                label = "Water",
                value = "${numberFormat.format(metrics.waterIntakeToday)} ml",
                supportingText = if (metrics.waterGoalToday > 0) {
                    "of ${numberFormat.format(metrics.waterGoalToday)} ml"
                } else {
                    "of 2,500 ml"
                },
                progress = waterProgress,
                progressColor = Color(0xFF38BDF8),
                onClick = onOpenWater,
                rightAction = {
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onOpenWater() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add water",
                                tint = if (isDark) Color(0xFF121B13) else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ModernTrackerCard(
                icon = Icons.Outlined.MonitorWeight,
                iconTint = Color(0xFFFFB74D),
                iconCircleBg = if (isDark) Color(0xFF282218) else Color(0xFFFEF3C7),
                rimColor = Color(0xFFFFB74D),
                label = "Weight",
                value = weight?.let { String.format(Locale.getDefault(), "%.1f kg", it) } ?: "Log weight",
                supportingText = if (weight == null) "Start a trend" else "latest log",
                progress = if (weight != null) 0.65f else 0.10f,
                progressColor = Color(0xFFFFB74D),
                onClick = onOpenWeight,
                rightAction = {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A),
                        modifier = Modifier
                            .height(28.dp)
                            .clickable { onOpenWeight() }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Log",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isDark) Color(0xFF121B13) else Color.White
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )

            val calorieProgress = if (metrics.calorieTargetToday > 0) {
                (metrics.caloriesConsumedToday.toFloat() / metrics.calorieTargetToday).coerceIn(0f, 1f)
            } else 0.08f

            ModernTrackerCard(
                icon = Icons.Outlined.LocalDining,
                iconTint = Color(0xFFFB923C),
                iconCircleBg = if (isDark) Color(0xFF2B1E15) else Color(0xFFFFEDD5),
                rimColor = Color(0xFFFB923C),
                label = "Calories",
                value = "${numberFormat.format(metrics.caloriesConsumedToday)} kcal",
                supportingText = if (metrics.calorieTargetToday > 0) {
                    "target: ${numberFormat.format(metrics.calorieTargetToday)} kcal"
                } else {
                    "target: 2,000 kcal"
                },
                progress = calorieProgress,
                progressColor = Color(0xFFFB923C),
                onClick = onOpenCalories,
                rightAction = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Calories details",
                        tint = if (isDark) Color(0xFF6B7B6D) else Color(0xFF9EACA0),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LatestMetricsSection(
    metrics: HealthMetricsSnapshot,
    onOpenBmi: () -> Unit,
    onOpenBloodPressure: () -> Unit,
    onOpenHeartRate: () -> Unit,
    onOpenWater: () -> Unit,
    onOpenWeight: () -> Unit,
    onOpenCalories: () -> Unit,
    onOpenSteps: () -> Unit,
    onStartCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = AppColors.isDark
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeading(
            title = "Latest metrics",
            actionText = "See all >",
            onActionClick = onStartCheckIn
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MiniMetricCard(
                icon = Icons.Outlined.WaterDrop,
                iconTint = Color(0xFF38BDF8),
                iconBg = if (isDark) Color(0xFF162326) else Color(0xFFE0F2FE),
                label = "Water",
                value = "${metrics.waterIntakeToday} ml",
                trend = "--",
                onClick = onOpenWater
            )

            MiniMetricCard(
                icon = Icons.Outlined.Scale,
                iconTint = Color(0xFFFFB74D),
                iconBg = if (isDark) Color(0xFF282218) else Color(0xFFFEF3C7),
                label = "Weight",
                value = metrics.latestWeightKg?.let { String.format(Locale.getDefault(), "%.1f kg", it) } ?: "-- kg",
                trend = "--",
                onClick = onOpenWeight
            )

            MiniMetricCard(
                icon = Icons.Outlined.LocalDining,
                iconTint = Color(0xFFFB923C),
                iconBg = if (isDark) Color(0xFF2B1E15) else Color(0xFFFFEDD5),
                label = "Calories",
                value = "${metrics.caloriesConsumedToday} kcal",
                trend = "--",
                onClick = onOpenCalories
            )

            MiniMetricCard(
                icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                iconTint = Color(0xFF81C784),
                iconBg = if (isDark) Color(0xFF1A281D) else Color(0xFFDCFCE7),
                label = "Steps",
                value = metrics.stepsToday?.toString() ?: "--",
                trend = "--",
                onClick = onOpenSteps
            )

            metrics.bmi?.let { bmi ->
                MiniMetricCard(
                    icon = Icons.Outlined.Assessment,
                    iconTint = Color(0xFFA78BFA),
                    iconBg = if (isDark) Color(0xFF221C2B) else Color(0xFFEDE9FE),
                    label = "BMI",
                    value = String.format(Locale.getDefault(), "%.1f", bmi),
                    trend = metrics.bmiCategory ?: "--",
                    onClick = onOpenBmi
                )
            }
        }
    }
}

@Composable
private fun MiniMetricCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    label: String,
    value: String,
    trend: String,
    onClick: () -> Unit
) {
    val isDark = AppColors.isDark
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) Color(0xFF131C15) else Color(0xFFFFFFFF),
        border = BorderStroke(1.dp, if (isDark) Color(0xFF223224) else Color(0xFFDCE6DA)),
        modifier = Modifier
            .width(92.dp)
            .height(96.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (isDark) Color(0xFF889689) else Color(0xFF5A685D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = if (isDark) Color.White else Color(0xFF162018),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (isDark) Color(0xFF6B7B6D) else Color(0xFF9EACA0),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = trend,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isDark) Color(0xFF6B7B6D) else Color(0xFF9EACA0)
                )
            }
        }
    }
}

@Composable
private fun InsightPreviewSection(
    insights: List<WellnessInsight>,
    recommendations: List<SmartRecommendation>,
    onOpenRecommendation: (String) -> Unit,
    onDismissRecommendation: (String) -> Unit,
    onOpenAiAssistant: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeading(title = "For you", subtitle = "Explainable prompts from your recent activity")
        WellnessInsightCallout(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (insights.isNotEmpty()) {
                    insights.forEachIndexed { index, insight ->
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                WellnessIconBadge(
                                    icon = Icons.Outlined.ShowChart,
                                    tint = MaterialTheme.colorScheme.primary,
                                    container = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                    modifier = Modifier.size(34.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    insight.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Text(
                                insight.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                insight.evidence,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            TextButton(
                                onClick = { onOpenRecommendation(insight.actionRoute) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(insight.actionLabel, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            }
                        }
                        if (index < insights.lastIndex) HorizontalDivider()
                    }
                } else if (recommendations.isEmpty()) {
                    Text("Patterns will appear here", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Keep logging the metrics that matter to you. Suggestions are informational and never diagnoses.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    recommendations.forEachIndexed { index, recommendation ->
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                WellnessIconBadge(
                                    icon = recommendationIcon(recommendation.type),
                                    tint = recommendation.color,
                                    container = recommendation.color.copy(alpha = 0.16f),
                                    modifier = Modifier.size(34.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    recommendation.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { onDismissRecommendation(recommendation.id) }) {
                                    Text("Hide")
                                }
                            }
                            Text(
                                recommendation.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            TextButton(
                                onClick = { onOpenRecommendation(recommendation.actionRoute) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(recommendation.actionLabel, fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            }
                        }
                        if (index < recommendations.lastIndex) HorizontalDivider()
                    }
                }
                Text(
                    "App suggestions use saved activity rules. AI responses are separate.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onOpenAiAssistant,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (AppColors.isDark) MaterialTheme.colorScheme.onPrimary else WellnessPalette.OnHero)
                    Spacer(Modifier.width(8.dp))
                    Text("Open AI Wellness Assistant", fontWeight = FontWeight.Bold, color = if (AppColors.isDark) MaterialTheme.colorScheme.onPrimary else WellnessPalette.OnHero)
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onOpenWater: () -> Unit,
    onOpenWeight: () -> Unit,
    onOpenBloodPressure: () -> Unit,
    onOpenCalculators: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        HomeAction("Water", Icons.Outlined.WaterDrop, onOpenWater, FeatureColors.WaterDeep),
        HomeAction("Weight", Icons.Outlined.MonitorWeight, onOpenWeight, FeatureColors.WeightDeep),
        HomeAction("BP", Icons.Outlined.MonitorHeart, onOpenBloodPressure, FeatureColors.BpDeep),
        HomeAction("Calc", Icons.Default.Calculate, onOpenCalculators, FeatureColors.CalorieDeep)
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeading(title = "Quick actions", subtitle = "Go straight to a common task")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            actions.forEach { action ->
                Surface(
                    onClick = action.onClick,
                    modifier = Modifier.weight(1f),
                    shape = ActionRowShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    border = BorderStroke(1.dp, action.color.copy(alpha = 0.22f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = action.color,
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(action.icon, contentDescription = action.label, tint = AppColors.icon(WellnessPalette.OnHero), modifier = Modifier.size(20.dp))
                            }
                        }
                        Text(
                            action.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private data class HomeAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val color: Color = Color.Unspecified
)

private fun recommendationIcon(type: RecommendationType): ImageVector = when (type) {
    RecommendationType.BMI_CHECK -> Icons.Outlined.Assessment
    RecommendationType.BP_CHECK -> Icons.Outlined.MonitorHeart
    RecommendationType.WATER_REMINDER -> Icons.Outlined.WaterDrop
    RecommendationType.CALORIE_REMINDER -> Icons.Outlined.LocalDining
    RecommendationType.WEIGHT_TREND -> Icons.Outlined.MonitorWeight
    RecommendationType.GOAL_PROGRESS -> Icons.Outlined.Flag
    RecommendationType.WHR_CHECK -> Icons.Outlined.Straighten
    RecommendationType.HR_CHECK -> Icons.Outlined.FavoriteBorder
    RecommendationType.ALL_GOOD -> Icons.Outlined.CheckCircle
    RecommendationType.STREAK -> Icons.Outlined.Timeline
    RecommendationType.NEW_CALCULATOR -> Icons.Default.Calculate
    RecommendationType.PROFILE_INCOMPLETE -> Icons.Default.Person
}

@Composable
private fun RelevantCalculatorsCard(
    onOpenBmi: () -> Unit,
    onOpenCalories: () -> Unit,
    onOpenAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MetricTileShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionHeading(title = "Useful calculators", subtitle = "Estimates with methods, limits, and sources")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    onClick = onOpenBmi,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = AppColors.surface(FeatureColors.BmiDeep.copy(alpha = 0.10f), 2),
                    border = BorderStroke(1.dp, FeatureColors.BmiDeep.copy(alpha = 0.30f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = AppColors.icon(FeatureColors.BmiDeep), modifier = Modifier.size(20.dp))
                        Text("BMI", fontWeight = FontWeight.Bold, color = AppColors.text(FeatureColors.BmiDeep))
                    }
                }
                Surface(
                    onClick = onOpenCalories,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = AppColors.surface(FeatureColors.CalorieDeep.copy(alpha = 0.10f), 2),
                    border = BorderStroke(1.dp, FeatureColors.CalorieDeep.copy(alpha = 0.30f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Outlined.LocalDining, contentDescription = null, tint = AppColors.icon(FeatureColors.CalorieDeep), modifier = Modifier.size(20.dp))
                        Text("Daily energy", fontWeight = FontWeight.Bold, color = AppColors.text(FeatureColors.CalorieDeep))
                    }
                }
            }
            Button(
                onClick = onOpenAll,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Browse all calculators", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}
