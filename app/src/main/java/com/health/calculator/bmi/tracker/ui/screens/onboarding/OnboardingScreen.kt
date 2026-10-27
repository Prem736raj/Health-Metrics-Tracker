package com.health.calculator.bmi.tracker.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private data class OrbitItem(
    val icon: ImageVector,
    val tintDark: Color,
    val tintLight: Color,
    val bgDark: Color,
    val bgLight: Color,
    @StringRes val labelRes: Int,
    val labelAbove: Boolean = false
)

private data class FeatureCardItem(
    val icon: ImageVector,
    val iconTintDark: Color,
    val iconTintLight: Color,
    val iconBgDark: Color,
    val iconBgLight: Color,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int
)

private data class OnboardingPage(
    val orbitType: Int, // 1 = Welcome (Heart), 2 = Calculators (Calc), 3 = Step (Rising Chart)
    val orbitItems: List<OrbitItem>,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val descriptionRes: Int,
    val cards: List<FeatureCardItem>
)

enum class OnboardingStartAction(
    @StringRes val labelRes: Int,
    @StringRes val descriptionRes: Int,
    val analyticsValue: String,
    val icon: ImageVector
) {
    WATER(R.string.onboarding_action_water, R.string.onboarding_action_water_description, "water", Icons.Outlined.WaterDrop),
    WEIGHT(R.string.onboarding_action_weight, R.string.onboarding_action_weight_description, "weight", Icons.Outlined.MonitorWeight),
    STEPS(R.string.onboarding_action_steps, R.string.onboarding_action_steps_description, "steps", Icons.AutoMirrored.Outlined.DirectionsWalk),
    CALCULATORS(R.string.onboarding_action_bmi, R.string.onboarding_action_bmi_description, "calculator", Icons.Outlined.Analytics)
}

private val onboardingPages = listOf(
    // ── Screen 1: Welcome ────────────────────────────────────────────────
    OnboardingPage(
        orbitType = 1,
        orbitItems = listOf(
            OrbitItem(
                icon = Icons.Outlined.WaterDrop,
                tintDark = Color(0xFF38BDF8),
                tintLight = Color(0xFF0288D1),
                bgDark = Color(0xFF16272C),
                bgLight = Color(0xFFE1F5FE),
                labelRes = R.string.onboarding_orbit_hydration,
                labelAbove = true
            ),
            OrbitItem(
                icon = Icons.AutoMirrored.Outlined.DirectionsRun,
                tintDark = Color(0xFFFB923C),
                tintLight = Color(0xFFE65100),
                bgDark = Color(0xFF2B2117),
                bgLight = Color(0xFFFFF3E0),
                labelRes = R.string.onboarding_orbit_activity,
                labelAbove = false
            ),
            OrbitItem(
                icon = Icons.Outlined.MonitorHeart,
                tintDark = Color(0xFF60A5FA),
                tintLight = Color(0xFF1976D2),
                bgDark = Color(0xFF14202B),
                bgLight = Color(0xFFE3F2FD),
                labelRes = R.string.onboarding_orbit_vitals,
                labelAbove = false
            ),
            OrbitItem(
                icon = Icons.AutoMirrored.Outlined.DirectionsWalk,
                tintDark = Color(0xFFC084FC),
                tintLight = Color(0xFF7B1FA2),
                bgDark = Color(0xFF25172C),
                bgLight = Color(0xFFF3E5F5),
                labelRes = R.string.onboarding_orbit_movement,
                labelAbove = false
            )
        ),
        titleRes = R.string.onboarding_welcome_title,
        subtitleRes = R.string.txt_your_personal_health_companion,
        descriptionRes = R.string.onboarding_welcome_description,
        cards = listOf(
            FeatureCardItem(
                icon = Icons.Outlined.Analytics,
                iconTintDark = Color(0xFFA5D6A7),
                iconTintLight = Color(0xFF2E7D32),
                iconBgDark = Color(0xFF1E2E20),
                iconBgLight = Color(0xFFE8F5E9),
                titleRes = R.string.onboarding_card_track_title,
                subtitleRes = R.string.onboarding_card_track_subtitle
            ),
            FeatureCardItem(
                icon = Icons.Outlined.Lightbulb,
                iconTintDark = Color(0xFFFFB74D),
                iconTintLight = Color(0xFFE65100),
                iconBgDark = Color(0xFF2B2317),
                iconBgLight = Color(0xFFFFF3E0),
                titleRes = R.string.onboarding_card_understand_title,
                subtitleRes = R.string.onboarding_card_understand_subtitle
            ),
            FeatureCardItem(
                icon = Icons.Outlined.Lock,
                iconTintDark = Color(0xFFBA68C8),
                iconTintLight = Color(0xFF7B1FA2),
                iconBgDark = Color(0xFF271A2E),
                iconBgLight = Color(0xFFF3E5F5),
                titleRes = R.string.onboarding_card_private_title,
                subtitleRes = R.string.onboarding_card_private_subtitle
            )
        )
    ),

    // ── Screen 2: Calculators ────────────────────────────────────────────
    OnboardingPage(
        orbitType = 2,
        orbitItems = listOf(
            OrbitItem(
                icon = Icons.Outlined.MonitorWeight,
                tintDark = Color(0xFF38BDF8),
                tintLight = Color(0xFF0288D1),
                bgDark = Color(0xFF16272C),
                bgLight = Color(0xFFE1F5FE),
                labelRes = R.string.onboarding_action_weight,
                labelAbove = true
            ),
            OrbitItem(
                icon = Icons.Outlined.Analytics,
                tintDark = Color(0xFFF59E0B),
                tintLight = Color(0xFFD97706),
                bgDark = Color(0xFF2B2317),
                bgLight = Color(0xFFFFF3E0),
                labelRes = R.string.onboarding_action_bmi,
                labelAbove = false
            ),
            OrbitItem(
                icon = Icons.Outlined.FavoriteBorder,
                tintDark = Color(0xFF4ADE80),
                tintLight = Color(0xFF16A34A),
                bgDark = Color(0xFF1E2E20),
                bgLight = Color(0xFFE8F5E9),
                labelRes = R.string.onboarding_orbit_vitals,
                labelAbove = false
            ),
            OrbitItem(
                icon = Icons.Outlined.Timeline,
                tintDark = Color(0xFFA855F7),
                tintLight = Color(0xFF7E22CE),
                bgDark = Color(0xFF25172C),
                bgLight = Color(0xFFF3E5F5),
                labelRes = R.string.onboarding_orbit_movement,
                labelAbove = false
            )
        ),
        titleRes = R.string.onboarding_calculators_title,
        subtitleRes = R.string.onboarding_calculators_subtitle,
        descriptionRes = R.string.onboarding_calculators_description,
        cards = listOf(
            FeatureCardItem(
                icon = Icons.Outlined.CheckCircle,
                iconTintDark = Color(0xFFA5D6A7),
                iconTintLight = Color(0xFF2E7D32),
                iconBgDark = Color(0xFF1E2E20),
                iconBgLight = Color(0xFFE8F5E9),
                titleRes = R.string.onboarding_card_calc_evidence_title,
                subtitleRes = R.string.onboarding_card_calc_evidence_subtitle
            ),
            FeatureCardItem(
                icon = Icons.Outlined.Info,
                iconTintDark = Color(0xFFFFB74D),
                iconTintLight = Color(0xFFE65100),
                iconBgDark = Color(0xFF2B2317),
                iconBgLight = Color(0xFFFFF3E0),
                titleRes = R.string.onboarding_card_calc_context_title,
                subtitleRes = R.string.onboarding_card_calc_context_subtitle
            ),
            FeatureCardItem(
                icon = Icons.Outlined.Security,
                iconTintDark = Color(0xFF81D4FA),
                iconTintLight = Color(0xFF0288D1),
                iconBgDark = Color(0xFF1A2732),
                iconBgLight = Color(0xFFE1F5FE),
                titleRes = R.string.onboarding_card_calc_private_title,
                subtitleRes = R.string.onboarding_card_calc_private_subtitle
            )
        )
    ),

    // ── Screen 3: Start with one small step ──────────────────────────────
    OnboardingPage(
        orbitType = 3,
        orbitItems = emptyList(),
        titleRes = R.string.onboarding_start_title,
        subtitleRes = R.string.onboarding_start_subtitle,
        descriptionRes = R.string.onboarding_start_description,
        cards = emptyList()
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSetUpProfile: () -> Unit,
    onStartAction: (OnboardingStartAction) -> Unit
) {
    if (AppColors.isDark) {
        ForestOnboardingScreen(onComplete, onSetUpProfile, onStartAction)
        return
    }
    val isDark = AppColors.isDark
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingPages.size - 1
    var selectedStartAction by remember { mutableStateOf<OnboardingStartAction?>(null) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(80)
        showContent = true
    }

    val scaffoldBg = if (isDark) Color(0xFF0C120D) else Color(0xFFF6F8F5)
    val bgDrawable = if (isDark) R.drawable.bg_onboarding_nature else R.drawable.bg_onboarding_nature_light
    val skipTextColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)

    Scaffold(containerColor = scaffoldBg) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = bgDrawable),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            AnimatedVisibility(visible = showContent, enter = fadeIn(tween(400))) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Top Bar with plain Skip text (matches reference design exactly)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .height(44.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.txt_skip),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = skipTextColor,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onComplete)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) { pageIndex ->
                        OnboardingPageContent(
                            page = onboardingPages[pageIndex],
                            pageIndex = pageIndex,
                            isDark = isDark,
                            selectedAction = selectedStartAction,
                            onSelectAction = { selectedStartAction = it }
                        )
                    }

                    BottomSection(
                        pagerState = pagerState,
                        isLastPage = isLastPage,
                        isDark = isDark,
                        onNext = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1,
                                    animationSpec = tween(380, easing = FastOutSlowInEasing)
                                )
                            }
                        },
                        onComplete = onComplete,
                        onSetUpProfile = onSetUpProfile,
                        selectedAction = selectedStartAction,
                        onStartAction = onStartAction
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int,
    isDark: Boolean,
    selectedAction: OnboardingStartAction?,
    onSelectAction: (OnboardingStartAction) -> Unit
) {
    val isActionPage = pageIndex == 2
    val scrollState = rememberScrollState()

    val titleColorPrimary = if (isDark) Color.White else Color(0xFF162018)
    val titleColorSecondary = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val subtitleColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val bodyColor = if (isDark) Color(0xFF9EABA1) else Color(0xFF5A685D)

    val fullTitle = stringResource(page.titleRes)
    val titleAnnotated = remember(fullTitle, isDark) {
        buildAnnotatedString {
            val lines = fullTitle.split("\n")
            if (lines.size >= 2) {
                withStyle(SpanStyle(color = titleColorPrimary)) {
                    append(lines[0])
                    append("\n")
                }
                withStyle(SpanStyle(color = titleColorSecondary)) {
                    append(lines.drop(1).joinToString("\n"))
                }
            } else {
                withStyle(SpanStyle(color = titleColorPrimary)) {
                    append(fullTitle)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        when (page.orbitType) {
            1 -> PageOneOrbitIllustration(page = page, isDark = isDark)
            2 -> PageTwoOrbitIllustration(page = page, isDark = isDark)
            else -> PageThreeOrbitIllustration(isDark = isDark)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Headline with two-tone sans-serif styling matching reference images
        Text(
            text = titleAnnotated,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                letterSpacing = (-0.5).sp
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(page.subtitleRes),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                letterSpacing = 0.1.sp
            ),
            color = subtitleColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(page.descriptionRes),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                lineHeight = 19.sp
            ),
            color = bodyColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (isActionPage) {
            OnboardingActionGrid(
                selectedAction = selectedAction,
                onSelectAction = onSelectAction,
                isDark = isDark
            )
        } else if (page.cards.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                page.cards.forEach { card ->
                    FeatureHighlightCard(
                        modifier = Modifier.weight(1f),
                        card = card,
                        isDark = isDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ── Screen 1 Orbit Illustration (Heart Center) ──────────────────────────────
@Composable
private fun PageOneOrbitIllustration(page: OnboardingPage, isDark: Boolean) {
    val ringColor = if (isDark) Color(0xFF3D4F3F) else Color(0xFF8EAF8F)
    val centerBg = if (isDark) Color(0xFF213526) else Color(0xFFDDEBDC)
    val centerBorder = if (isDark) Color(0xFF4D6A52) else Color(0xFFB8D5B6)
    val heartTint = if (isDark) Color(0xFFC6E7BA) else Color(0xFF2E623A)

    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(240.dp)) {
            val radiusPx = 78.dp.toPx()
            drawCircle(
                color = ringColor,
                radius = radiusPx,
                style = Stroke(
                    width = 1.3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()), 0f)
                )
            )
        }

        // Center Disc with Heart
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(96.dp)
                .clip(CircleShape)
                .background(centerBg)
                .border(1.dp, centerBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = heartTint,
                modifier = Modifier.size(44.dp)
            )
        }

        if (page.orbitItems.size >= 4) {
            val topItem = page.orbitItems[0]
            val leftItem = page.orbitItems[1]
            val rightItem = page.orbitItems[2]
            val bottomItem = page.orbitItems[3]

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-78).dp - 9.dp),
                item = topItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-78).dp, y = 9.dp),
                item = leftItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 78.dp, y = 9.dp),
                item = rightItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 78.dp + 9.dp),
                item = bottomItem,
                isDark = isDark
            )
        }
    }
}

// ── Screen 2 Orbit Illustration (Calculator Center) ──────────────────────────
@Composable
private fun PageTwoOrbitIllustration(page: OnboardingPage, isDark: Boolean) {
    val ringColor = if (isDark) Color(0xFF3D4F3F) else Color(0xFF8EAF8F)
    val centerBg = if (isDark) Color(0xFF213526) else Color(0xFFD5ECE8)
    val centerBorder = if (isDark) Color(0xFF4D6A52) else Color(0xFFB8D5B6)
    val calcTint = if (isDark) Color(0xFFC6E7BA) else Color(0xFF00695C)

    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(240.dp)) {
            val radiusPx = 78.dp.toPx()
            drawCircle(
                color = ringColor,
                radius = radiusPx,
                style = Stroke(
                    width = 1.3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()), 0f)
                )
            )
        }

        // Center Disc with Calculator Icon
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(96.dp)
                .clip(CircleShape)
                .background(centerBg)
                .border(1.dp, centerBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Calculate,
                contentDescription = null,
                tint = calcTint,
                modifier = Modifier.size(44.dp)
            )
        }

        if (page.orbitItems.size >= 4) {
            val topItem = page.orbitItems[0]
            val leftItem = page.orbitItems[1]
            val rightItem = page.orbitItems[2]
            val bottomItem = page.orbitItems[3]

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-78).dp - 9.dp),
                item = topItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-78).dp, y = 9.dp),
                item = leftItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 78.dp, y = 9.dp),
                item = rightItem,
                isDark = isDark
            )

            OrbitSatellite(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 78.dp + 9.dp),
                item = bottomItem,
                isDark = isDark
            )
        }
    }
}

// ── Screen 3 Orbit Illustration (Rising Polyline with Nodes) ────────────────
@Composable
private fun PageThreeOrbitIllustration(isDark: Boolean) {
    val ringColor = if (isDark) Color(0xFF3D4F3F) else Color(0xFF8EAF8F)
    val centerBg = if (isDark) Color(0xFF213526) else Color(0xFFDDEBDC)
    val centerBorder = if (isDark) Color(0xFF4D6A52) else Color(0xFFB8D5B6)
    val iconTint = if (isDark) Color(0xFFC6E7BA) else Color(0xFF2E623A)
    val satBg = if (isDark) Color(0xFF1E2E20) else Color(0xFFFFFFFF)
    val satBorder = if (isDark) Color(0xFF3E5642) else Color(0xFFD4DFD3)

    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(240.dp)) {
            val radiusPx = 78.dp.toPx()
            // Dotted ring
            drawCircle(
                color = ringColor,
                radius = radiusPx,
                style = Stroke(
                    width = 1.3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()), 0f)
                )
            )

            // Golden sweeping bezier curve from center-left to top-right
            val arcColor = if (isDark) Color(0x66C2B480) else Color(0x6676885B)
            val path = Path().apply {
                moveTo(size.width * 0.15f, size.height * 0.65f)
                cubicTo(
                    size.width * 0.40f, size.height * 0.70f,
                    size.width * 0.75f, size.height * 0.40f,
                    size.width * 0.98f, size.height * 0.18f
                )
            }
            drawPath(
                path = path,
                color = arcColor,
                style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Center Disc with Rising 4-Node Polyline Chart
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(96.dp)
                .clip(CircleShape)
                .background(centerBg)
                .border(1.dp, centerBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(46.dp)) {
                val p1 = Offset(size.width * 0.18f, size.height * 0.65f)
                val p2 = Offset(size.width * 0.40f, size.height * 0.44f)
                val p3 = Offset(size.width * 0.62f, size.height * 0.58f)
                val p4 = Offset(size.width * 0.84f, size.height * 0.28f)

                val linePath = Path().apply {
                    moveTo(p1.x, p1.y)
                    lineTo(p2.x, p2.y)
                    lineTo(p3.x, p3.y)
                    lineTo(p4.x, p4.y)
                }

                drawPath(
                    path = linePath,
                    color = iconTint,
                    style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 4 vertex circle nodes
                listOf(p1, p2, p3, p4).forEach { pt ->
                    drawCircle(color = iconTint, radius = 3.6.dp.toPx(), center = pt)
                }
            }
        }

        // Satellites for Page 3 (Exact circular discs positioned along the orbit ring)
        // Satellite 1: Walking Person at angle 120 deg (top-left)
        val radiusDp = 78.0
        val offset1X = (-radiusDp * cos(Math.toRadians(60.0))).dp
        val offset1Y = (-radiusDp * sin(Math.toRadians(60.0))).dp

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offset1X, y = offset1Y)
                .size(46.dp)
                .clip(CircleShape)
                .background(satBg)
                .border(1.dp, satBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.DirectionsWalk,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        // Satellite 2: Bar Chart in square outline at angle 15 deg (right)
        val offset2X = (radiusDp * cos(Math.toRadians(15.0))).dp
        val offset2Y = (-radiusDp * sin(Math.toRadians(15.0))).dp

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offset2X, y = offset2Y)
                .size(46.dp)
                .clip(CircleShape)
                .background(satBg)
                .border(1.dp, satBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Analytics,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        // Satellite 3: Flag at angle 215 deg (bottom-left)
        val offset3X = (-radiusDp * cos(Math.toRadians(35.0))).dp
        val offset3Y = (radiusDp * sin(Math.toRadians(35.0))).dp

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offset3X, y = offset3Y)
                .size(46.dp)
                .clip(CircleShape)
                .background(satBg)
                .border(1.dp, satBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Flag,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun OrbitSatellite(
    modifier: Modifier = Modifier,
    item: OrbitItem,
    isDark: Boolean
) {
    val satBg = if (isDark) item.bgDark else item.bgLight
    val satBorder = if (isDark) Color(0xFF3E5642) else Color(0xFFD4DFD3)
    val satLabelColor = if (isDark) Color(0xFFC7D4C6) else Color(0xFF253528)
    val satTint = if (isDark) item.tintDark else item.tintLight

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (item.labelAbove) {
            Text(
                text = stringResource(item.labelRes),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = satLabelColor,
                letterSpacing = 0.2.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(satBg)
                .border(1.dp, satBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = satTint,
                modifier = Modifier.size(22.dp)
            )
        }
        if (!item.labelAbove) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(item.labelRes),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = satLabelColor,
                letterSpacing = 0.2.sp
            )
        }
    }
}

// ── Three Feature Highlight Cards (Pages 1 & 2) ──────────────────────────────
@Composable
private fun FeatureHighlightCard(
    modifier: Modifier = Modifier,
    card: FeatureCardItem,
    isDark: Boolean
) {
    val cardBg = if (isDark) Color(0xFF121B14) else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) Color(0xFF233324) else Color(0xFFDCE6DA)
    val titleColor = if (isDark) Color.White else Color(0xFF162018)
    val subtitleColor = if (isDark) Color(0xFF869488) else Color(0xFF5A685D)
    val iconBg = if (isDark) card.iconBgDark else card.iconBgLight
    val iconTint = if (isDark) card.iconTintDark else card.iconTintLight

    Surface(
        modifier = modifier.height(154.dp),
        shape = RoundedCornerShape(18.dp),
        color = cardBg,
        border = BorderStroke(1.dp, cardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = card.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(card.titleRes),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                ),
                color = titleColor,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(card.subtitleRes),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.5.sp,
                    lineHeight = 14.sp
                ),
                color = subtitleColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ── 2x2 Action Cards Grid (Page 3) ──────────────────────────────────────────
@Composable
private fun OnboardingActionGrid(
    selectedAction: OnboardingStartAction?,
    onSelectAction: (OnboardingStartAction) -> Unit,
    isDark: Boolean
) {
    val cardBgUnselected = if (isDark) Color(0xFF121B14) else Color(0xFFFFFFFF)
    val cardBgSelected = if (isDark) Color(0xFF1E2D20) else Color(0xFFE4EFE2)
    val cardBorderUnselected = if (isDark) Color(0xFF233324) else Color(0xFFDCE6DA)
    val cardBorderSelected = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val titleColor = if (isDark) Color.White else Color(0xFF162018)
    val subtitleColor = if (isDark) Color(0xFF869488) else Color(0xFF5A685D)

    val actions = listOf(
        // Row 1: Water (cyan), Weight (amber/gold)
        Pair(OnboardingStartAction.WATER, Pair(Color(0xFF38BDF8), Color(0xFF16272C))),
        Pair(OnboardingStartAction.WEIGHT, Pair(Color(0xFFD7C59A), Color(0xFF2B271B))),
        // Row 2: Steps (green), BMI calculator (sage green)
        Pair(OnboardingStartAction.STEPS, Pair(Color(0xFFA5D6A7), Color(0xFF1E2E20))),
        Pair(OnboardingStartAction.CALCULATORS, Pair(Color(0xFFA5D6A7), Color(0xFF1E2E20)))
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        actions.chunked(2).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowActions.forEach { (action, colors) ->
                    val isSelected = selectedAction == action
                    val actionLabel = stringResource(action.labelRes)
                    val actionDescription = stringResource(action.descriptionRes)
                    val (iconTint, iconBgDark) = colors
                    val iconBg = if (isDark) iconBgDark else Color(0xFFE8F5E9)

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp)
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { onSelectAction(action) }
                            )
                            .semantics {
                                contentDescription = "$actionLabel. $actionDescription"
                                selected = isSelected
                            },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) cardBgSelected else cardBgUnselected,
                        border = BorderStroke(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) cardBorderSelected else cardBorderUnselected
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(iconBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = action.icon,
                                    contentDescription = null,
                                    tint = iconTint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = actionLabel,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = titleColor,
                                    maxLines = 1
                                )
                                Text(
                                    text = actionDescription,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.5.sp,
                                        lineHeight = 13.5.sp
                                    ),
                                    color = subtitleColor,
                                    maxLines = 2
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = if (isDark) Color(0xFF5E6E60) else Color(0xFF9EACA0),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Bottom Indicators and Controls ──────────────────────────────────────────
@Composable
private fun BottomSection(
    pagerState: PagerState,
    isLastPage: Boolean,
    isDark: Boolean,
    onNext: () -> Unit,
    onComplete: () -> Unit,
    onSetUpProfile: () -> Unit,
    selectedAction: OnboardingStartAction?,
    onStartAction: (OnboardingStartAction) -> Unit
) {
    val btnContainer = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val btnContent = if (isDark) Color(0xFF111A13) else Color(0xFFFFFFFF)
    val profileLinkColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageIndicators(
            pageCount = onboardingPages.size,
            currentPage = pagerState.currentPage,
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLastPage) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { selectedAction?.let(onStartAction) ?: onComplete() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnContainer,
                        contentColor = btnContent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 1.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_explore_home),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = btnContent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = btnContent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                TextButton(onClick = onSetUpProfile) {
                    Text(
                        text = stringResource(R.string.onboarding_set_up_profile_instead),
                        color = profileLinkColor,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.5.sp
                        )
                    )
                }
            }
        } else {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = btnContainer,
                    contentColor = btnContent
                ),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.txt_next),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = btnContent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = btnContent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PageIndicators(pageCount: Int, currentPage: Int, isDark: Boolean) {
    val progressDescription = stringResource(R.string.onboarding_page_progress, currentPage + 1, pageCount)
    val activeColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val inactiveColor = if (isDark) Color(0xFF2F3D31) else Color(0xFFCCD8CB)

    Row(
        modifier = Modifier.semantics { contentDescription = progressDescription },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val width by animateFloatAsState(
                targetValue = if (isSelected) 24f else 8f,
                animationSpec = tween(300, easing = FastOutSlowInEasing),
                label = "indicator_width_$index"
            )
            val color = if (isSelected) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
