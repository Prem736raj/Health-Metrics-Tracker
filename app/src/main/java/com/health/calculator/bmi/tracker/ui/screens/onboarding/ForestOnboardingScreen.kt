package com.health.calculator.bmi.tracker.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.R
import kotlinx.coroutines.launch

/** Scoped to the dark reference design; these colors never enter the light renderer. */
private object ForestColors {
    val background = Color(0xFF0B1712)
    val text = Color(0xFFF4F5F1)
    val secondary = Color(0xFFD0D5D0)
    val sage = Color(0xFFACCE95)
    val glow = Color(0xFFC8EDA9)
    val cyan = Color(0xFF90DFF2)
    val amber = Color(0xFFF4C668)
    val peach = Color(0xFFFFCC99)
    val blue = Color(0xFF9DCFF8)
    val card = Color(0xFF18251F)
    val border = Color(0xFF768A7B)
}

private enum class ForestSymbol {
    HEART, HEART_OUTLINE, DROP, WALK, PULSE, MOON, CALCULATOR, WEIGHT, WEIGHT_FILLED,
    FLAME, TREND, MOVEMENT, BARS, ASCENDING_BARS, CHART, BULB, LOCK, CHECK, INFO, SHIELD, FLAG
}

/** The three references use different vertical compositions, rather than one stretched column. */
@Composable
internal fun ForestOnboardingScreen(
    onComplete: () -> Unit,
    onSetUpProfile: () -> Unit,
    onStartAction: (OnboardingStartAction) -> Unit
) {
    val pager = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    var selectedAction by remember { mutableStateOf<OnboardingStartAction?>(null) }
    HorizontalPager(state = pager, modifier = Modifier.fillMaxSize().background(ForestColors.background)) { page ->
        ForestPage(
            page = page,
            selectedAction = selectedAction,
            onSelectAction = { selectedAction = if (selectedAction == it) null else it },
            onSkip = onComplete,
            onPrimary = {
                if (page < 2) scope.launch { pager.animateScrollToPage(page + 1) }
                else selectedAction?.let(onStartAction) ?: onComplete()
            },
            onSetUpProfile = onSetUpProfile
        )
    }
}

@Composable
private fun ForestPage(
    page: Int,
    selectedAction: OnboardingStartAction?,
    onSelectAction: (OnboardingStartAction) -> Unit,
    onSkip: () -> Unit,
    onPrimary: () -> Unit,
    onSetUpProfile: () -> Unit
) {
    val background = when (page) {
        0 -> R.drawable.bg_onboarding_forest_welcome
        1 -> R.drawable.bg_onboarding_forest_calculators
        else -> R.drawable.bg_onboarding_forest_start
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val width = maxWidth
        val unit = width.value / 390f
        val density = LocalDensity.current
        val topInset = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
        val bottomInset = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
        // Small/landscape windows and larger accessibility fonts can scroll, without overlapping controls.
        val fontExpansion = density.fontScale.coerceAtLeast(1f)
        val minimumHeight = width * (if (page == 0) 1.65f else 1.9f) *
            fontExpansion * fontExpansion * fontExpansion
        val height = maxOf(maxHeight - bottomInset, minimumHeight)
        Image(
            painterResource(background), null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(height)) {
                ForestSkip(page, unit, maxOf(topInset + 12.dp, height * .055f), onSkip)

                val orbitCenter = height * when (page) { 0 -> .247f; 1 -> .255f; else -> .212f }
                ForestOrbit(page, width, unit, Modifier.offset(y = orbitCenter - width * .32f))

                val title = stringResource(when (page) {
                    0 -> R.string.onboarding_welcome_title
                    1 -> R.string.onboarding_calculators_title
                    else -> R.string.onboarding_start_title
                })
                val titleSize = when (page) { 0 -> 28f; 1 -> 30f; else -> 34f } * unit
                ForestTextSlot(
                    title, height * when (page) { 0 -> .4095f; 1 -> .398f; else -> .3005f },
                    height * .12f,
                    titleSize, ForestColors.text, serif = true, bold = true, horizontalPadding = width * .028f
                )
                ForestTextSlot(
                    stringResource(when (page) {
                        0 -> R.string.txt_your_personal_health_companion
                        1 -> R.string.onboarding_calculators_subtitle
                        else -> R.string.onboarding_start_subtitle
                    }),
                    height * when (page) { 0 -> .527f; 1 -> .509f; else -> .416f },
                    height * .033f,
                    (if (page == 0) 14.3f else 16f) * unit,
                    if (page == 1) Color(0xFFE2EFDA) else ForestColors.sage,
                    serif = page == 1, bold = true, horizontalPadding = width * .04f
                )
                ForestTextSlot(
                    stringResource(when (page) {
                        0 -> R.string.forest_welcome_description
                        1 -> R.string.onboarding_calculators_description
                        else -> R.string.forest_start_description
                    }),
                    height * when (page) { 0 -> .562f; 1 -> .540f; else -> .446f },
                    height * when (page) { 0 -> .060f; 1 -> .080f; else -> .065f },
                    (if (page == 0) 13f else 14f) * unit, ForestColors.secondary,
                    horizontalPadding = width * .06f
                )
                if (page < 2) {
                    ForestFeatureCards(page, unit, height, width)
                } else {
                    ForestActionCards(unit, height, width, selectedAction, onSelectAction)
                }
                val progress = stringResource(R.string.onboarding_page_progress, page + 1, 3)
                Row(
                    Modifier.align(Alignment.TopCenter)
                        .offset(y = height * when (page) { 0 -> .849f; 1 -> .839f; else -> .712f })
                        .height(16.dp * unit).semantics { contentDescription = progress },
                    horizontalArrangement = Arrangement.spacedBy((if (page == 2) 8.dp else 10.dp) * unit),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        Box(Modifier.size(
                            width = (if (index == page && page > 0) { if (page == 1) 15 else 19 } else 9).dp * unit,
                            height = 9.dp * unit
                        ).background(
                            if (index == page) ForestColors.glow else Color(0xFF68736C), CircleShape
                        ))
                    }
                }
                val primaryLabel = if (page < 2) stringResource(R.string.txt_next)
                else selectedAction?.let {
                    stringResource(R.string.onboarding_start_with_action, stringResource(it.labelRes))
                } ?: stringResource(R.string.onboarding_explore_home)
                ForestPrimaryButton(
                    primaryLabel, unit,
                    Modifier.align(Alignment.TopCenter)
                        .offset(y = height * when (page) { 0 -> .886f; 1 -> .869f; else -> .750f })
                        .width(width * .87f)
                        .height(maxOf(52.dp, height * if (page == 0) .069f else .062f)),
                    onPrimary
                )
                if (page == 2) {
                    Box(
                        Modifier.align(Alignment.TopCenter).offset(y = height * .831f)
                            .height(maxOf(48.dp, height * .045f)).clip(RoundedCornerShape(24.dp))
                            .clickable(role = Role.Button, onClick = onSetUpProfile).padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ForestText(stringResource(R.string.onboarding_set_up_profile_instead), 13.5f * unit, ForestColors.sage)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ForestSkip(page: Int, unit: Float, top: Dp, onClick: () -> Unit) {
    Box(
        Modifier.align(Alignment.TopEnd).offset(x = (-12).dp * unit, y = top)
            .size(maxOf(48.dp, (when (page) { 0 -> 64.dp; 1 -> 76.dp; else -> 56.dp }) * unit), maxOf(48.dp, 40.dp * unit))
            .clip(CircleShape).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            if (page < 2) Modifier.fillMaxWidth().height((if (page == 0) 32.dp else 38.dp) * unit)
                .border(0.8.dp, ForestColors.sage.copy(alpha = if (page == 0) .28f else .64f), CircleShape)
                .background(ForestColors.card.copy(alpha = .2f), CircleShape)
            else Modifier,
            contentAlignment = Alignment.Center
        ) {
            ForestText(stringResource(R.string.txt_skip), 14.5f * unit, ForestColors.sage, bold = page < 2)
        }
    }
}

@Composable
private fun ForestText(
    text: String, size: Float, color: Color, modifier: Modifier = Modifier,
    serif: Boolean = false, bold: Boolean = false, align: TextAlign = TextAlign.Center,
    lineHeightRatio: Float = if (serif) 1.2f else 1.42f
) {
    Text(text, modifier, color = color, style = TextStyle(
        fontFamily = if (serif) FontFamily.Serif else FontFamily.SansSerif,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = size.coerceAtLeast(12f).sp,
        lineHeight = (size * lineHeightRatio).coerceAtLeast(16f).sp,
        letterSpacing = 0.sp, textAlign = align
    ))
}

@Composable
private fun BoxScope.ForestTextSlot(
    text: String, top: Dp, height: Dp, size: Float, color: Color,
    serif: Boolean = false, bold: Boolean = false, horizontalPadding: Dp = 16.dp
) {
    Box(Modifier.fillMaxWidth().offset(y = top).height(height).padding(horizontal = horizontalPadding), Alignment.Center) {
        ForestText(text, size, color, serif = serif, bold = bold)
    }
}

private data class ForestFeature(
    val symbol: ForestSymbol, val color: Color,
    @param:StringRes val title: Int, @param:StringRes val description: Int
)

@Composable
private fun BoxScope.ForestFeatureCards(page: Int, unit: Float, height: Dp, width: Dp) {
    val features = if (page == 0) listOf(
        ForestFeature(ForestSymbol.BARS, ForestColors.glow, R.string.onboarding_card_track_title, R.string.onboarding_card_track_subtitle),
        ForestFeature(ForestSymbol.BULB, ForestColors.amber, R.string.onboarding_card_understand_title, R.string.forest_understand_description),
        ForestFeature(ForestSymbol.LOCK, ForestColors.glow, R.string.onboarding_card_private_title, R.string.forest_private_description)
    ) else listOf(
        ForestFeature(ForestSymbol.CHECK, ForestColors.glow, R.string.onboarding_card_calc_evidence_title, R.string.onboarding_card_calc_evidence_subtitle),
        ForestFeature(ForestSymbol.INFO, ForestColors.amber, R.string.onboarding_card_calc_context_title, R.string.onboarding_card_calc_context_subtitle),
        ForestFeature(ForestSymbol.SHIELD, ForestColors.blue, R.string.onboarding_card_calc_private_title, R.string.onboarding_card_calc_private_subtitle)
    )
    Row(
        Modifier.fillMaxWidth().offset(y = height * if (page == 0) .637f else .633f)
            .padding(horizontal = width * if (page == 0) .055f else .04f)
            .height(height * if (page == 0) .193f else .170f),
        horizontalArrangement = Arrangement.spacedBy(7.dp * unit)
    ) {
        features.forEach { feature ->
            Column(
                Modifier.weight(1f).fillMaxSize().forestGlass(13.dp * unit)
                    .padding(horizontal = 5.dp * unit, vertical = (if (page == 0) 8.dp else 14.dp) * unit),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ForestDisc(feature.symbol, feature.color, Modifier.size((if (page == 0) 44.dp else 48.dp) * unit), feature.color.copy(alpha = .12f), iconFraction = .55f)
                Spacer(Modifier.height((if (page == 0) 5.dp else 7.dp) * unit))
                ForestText(stringResource(feature.title), 13.5f * unit, ForestColors.text, serif = page == 1, bold = true)
                Spacer(Modifier.height((if (page == 0) 3.dp else 4.dp) * unit))
                ForestText(stringResource(feature.description), (if (page == 0) 12f else 12.2f) * unit, ForestColors.secondary,
                    lineHeightRatio = if (page == 0) 1.3f else 1.42f)
            }
        }
    }
}

private fun Modifier.forestGlass(radius: Dp, selected: Boolean = false): Modifier =
    clip(RoundedCornerShape(radius))
        .background(ForestColors.card.copy(alpha = if (selected) .9f else .47f))
        .border(if (selected) 1.4.dp else .8.dp,
            if (selected) ForestColors.sage else ForestColors.border.copy(alpha = .38f), RoundedCornerShape(radius))

@Composable
private fun BoxScope.ForestActionCards(
    unit: Float, height: Dp, width: Dp, selected: OnboardingStartAction?,
    onSelect: (OnboardingStartAction) -> Unit
) {
    Column(
        Modifier.fillMaxWidth().offset(y = height * .520f).padding(horizontal = width * .04f)
            .height(maxOf(height * .165f, 96.dp + 9.dp * unit)),
        verticalArrangement = Arrangement.spacedBy(9.dp * unit)
    ) {
        OnboardingStartAction.entries.chunked(2).forEach { actions ->
            Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp * unit)) {
                actions.forEach { action ->
                    val label = stringResource(action.labelRes)
                    val description = stringResource(action.descriptionRes)
                    val tint = when (action) {
                        OnboardingStartAction.WATER -> ForestColors.cyan
                        OnboardingStartAction.WEIGHT -> ForestColors.peach
                        OnboardingStartAction.STEPS -> ForestColors.glow
                        OnboardingStartAction.CALCULATORS -> ForestColors.amber
                    }
                    Row(
                        Modifier.weight(1f).fillMaxSize().forestGlass(15.dp * unit, selected == action)
                            .selectable(selected == action, role = Role.RadioButton, onClick = { onSelect(action) })
                            .semantics(mergeDescendants = true) { contentDescription = "$label. $description" }
                            .padding(horizontal = 10.dp * unit),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ForestDisc(when (action) {
                            OnboardingStartAction.WATER -> ForestSymbol.DROP
                            OnboardingStartAction.WEIGHT -> ForestSymbol.WEIGHT_FILLED
                            OnboardingStartAction.STEPS -> ForestSymbol.WALK
                            OnboardingStartAction.CALCULATORS -> ForestSymbol.ASCENDING_BARS
                        }, tint, Modifier.size(39.dp * unit), tint.copy(alpha = .12f), .65f)
                        Spacer(Modifier.width(10.dp * unit))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp * unit)) {
                            ForestText(label, 12f * unit, ForestColors.text, bold = true, align = TextAlign.Start)
                            ForestText(description, 12f * unit, ForestColors.secondary, align = TextAlign.Start)
                        }
                        Icon(Icons.Filled.ChevronRight, null, tint = Color(0xFFACB5AD), modifier = Modifier.size(20.dp * unit))
                    }
                }
            }
        }
    }
}

@Composable
private fun ForestPrimaryButton(text: String, unit: Float, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier.clip(CircleShape)
            .background(Brush.linearGradient(listOf(Color(0xFFB4D69E), Color(0xFFA3C390), Color(0xFFB6D79E))))
            .border(.7.dp, Color(0xFFD5EBC4).copy(alpha = .7f), CircleShape)
            .clickable(role = Role.Button, onClick = onClick).padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ForestText(text, 16f * unit, Color(0xFF08120C), bold = true)
        Spacer(Modifier.width(10.dp * unit))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFF08120C), modifier = Modifier.size(24.dp * unit))
    }
}

@Composable
private fun ForestOrbit(page: Int, width: Dp, unit: Float, modifier: Modifier) {
    Box(modifier.fillMaxWidth().height(width * .64f)) {
        val centerY = width * .32f
        val radius = width * if (page == 2) .198f else .214f
        val verticalRadius = if (page == 2) radius else width * .19f
        Canvas(Modifier.fillMaxSize()) {
            drawOval(
                if (page == 2) Color(0xFF91A18D) else ForestColors.glow,
                topLeft = Offset(size.width / 2 - radius.toPx(), centerY.toPx() - verticalRadius.toPx()),
                size = Size(radius.toPx() * 2, verticalRadius.toPx() * 2),
                style = Stroke(.8.dp.toPx(), pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf((if (page == 1) 3.3.dp else 1.1.dp).toPx(), 4.dp.toPx())
                ), cap = StrokeCap.Round), alpha = if (page == 2) .8f else .9f
            )
        }
        val heroDiameter = width * when (page) { 0 -> .226f; 1 -> .242f; else -> .23f }
        ForestDisc(
            when (page) { 0 -> ForestSymbol.HEART_OUTLINE; 1 -> ForestSymbol.CALCULATOR; else -> ForestSymbol.TREND },
            if (page == 2) Color(0xFFD9FFC1) else ForestColors.glow,
            Modifier.align(Alignment.TopCenter).offset(y = centerY - heroDiameter / 2).size(heroDiameter),
            Color(0xFF91B85E).copy(alpha = .14f), iconFraction = if (page == 1) .46f else .55f, prominent = true
        )
        if (page == 2) {
            ForestSatellite(ForestSymbol.WALK, ForestColors.glow, null, false, width, unit, -.12f, centerY - width * .16f)
            ForestSatellite(ForestSymbol.CHART, ForestColors.glow, null, false, width, unit, .198f, centerY - width * .022f)
            ForestSatellite(ForestSymbol.FLAG, ForestColors.glow, null, false, width, unit, -.142f, centerY + width * .123f)
        } else {
            ForestSatellite(if (page == 0) ForestSymbol.DROP else ForestSymbol.WEIGHT,
                if (page == 0) ForestColors.cyan else ForestColors.glow,
                stringResource(if (page == 0) R.string.onboarding_orbit_hydration else R.string.onboarding_action_weight),
                true, width, unit, 0f, centerY - verticalRadius)
            ForestSatellite(if (page == 0) ForestSymbol.WALK else ForestSymbol.FLAME,
                if (page == 0) ForestColors.glow else ForestColors.amber,
                stringResource(if (page == 0) R.string.onboarding_orbit_activity else R.string.onboarding_action_bmi),
                false, width, unit, -radius / width, centerY)
            ForestSatellite(if (page == 0) ForestSymbol.PULSE else ForestSymbol.HEART,
                ForestColors.glow, stringResource(R.string.onboarding_orbit_vitals), false,
                width, unit, radius / width, centerY)
            ForestSatellite(if (page == 0) ForestSymbol.MOON else ForestSymbol.MOVEMENT,
                ForestColors.glow,
                stringResource(if (page == 0) R.string.onboarding_orbit_sleep else R.string.onboarding_orbit_movement),
                false, width, unit, 0f, centerY + verticalRadius)
        }
    }
}

@Composable
private fun BoxScope.ForestSatellite(
    symbol: ForestSymbol, tint: Color, label: String?, above: Boolean,
    width: Dp, unit: Float, x: Float, centerY: Dp
) {
    val diameter = width * .115f
    val satelliteWidth = width * .27f
    Box(
        Modifier.align(Alignment.TopCenter).offset(x = width * x, y = centerY - diameter / 2)
            .width(satelliteWidth).height(diameter + 27.dp * unit),
        contentAlignment = Alignment.TopCenter
    ) {
        ForestDisc(symbol, tint, Modifier.size(diameter), Color(0xFF7FA66B).copy(alpha = .09f), .6f, neutralOutline = true)
        if (label != null) {
            ForestText(label, 12f * unit, ForestColors.text,
                Modifier.offset(y = if (above) (-23).dp * unit else diameter + 3.dp * unit))
        }
    }
}

@Composable
private fun ForestDisc(
    symbol: ForestSymbol, tint: Color, modifier: Modifier, fill: Color,
    iconFraction: Float = .58f, prominent: Boolean = false, neutralOutline: Boolean = false
) {
    // Opaque glass colors mask the orbit behind each node, as in the reference.
    val base = if (prominent) Color(0xFF1B2A1D) else Color(0xFF10261C)
    Box(
        modifier.clip(CircleShape)
            .background(Brush.radialGradient(listOf(
                fill.copy(alpha = fill.alpha + .06f).compositeOver(base),
                fill.compositeOver(base), base
            )))
            .border(if (prominent) .9.dp else .8.dp,
                (if (neutralOutline) ForestColors.sage else tint).copy(alpha = if (prominent) .38f else .24f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        ForestGlyph(symbol, tint, Modifier.fillMaxSize(iconFraction))
    }
}

/** Code-drawn symbols keep the reference's simple strokes sharp at every display density. */
@Composable
private fun ForestGlyph(symbol: ForestSymbol, tint: Color, modifier: Modifier) {
    val material = when (symbol) {
        ForestSymbol.DROP -> Icons.Outlined.WaterDrop
        ForestSymbol.WALK -> Icons.AutoMirrored.Outlined.DirectionsWalk
        ForestSymbol.HEART -> Icons.Filled.Favorite
        ForestSymbol.FLAME -> Icons.Outlined.LocalFireDepartment
        ForestSymbol.BULB -> Icons.Outlined.Lightbulb
        ForestSymbol.LOCK -> Icons.Filled.Lock
        ForestSymbol.CHECK -> Icons.Outlined.CheckCircle
        ForestSymbol.INFO -> Icons.Outlined.Info
        else -> null
    }
    if (material != null) {
        Icon(material, null, modifier, tint = tint)
        return
    }
    Canvas(modifier) {
        scale(size.width / 100f, size.height / 100f, pivot = Offset.Zero) {
            val stroke = Stroke(width = 7f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            when (symbol) {
                ForestSymbol.HEART_OUTLINE -> {
                    val path = Path().apply {
                        moveTo(50f, 87f); cubicTo(39f, 76f, 10f, 52f, 10f, 30f)
                        cubicTo(10f, 6f, 39f, 1f, 50f, 25f)
                        cubicTo(61f, 1f, 90f, 6f, 90f, 30f)
                        cubicTo(90f, 52f, 61f, 76f, 50f, 87f); close()
                    }
                    drawPath(path, tint, style = stroke)
                }
                ForestSymbol.PULSE -> {
                    drawPath(Path().apply {
                        moveTo(7f, 46f); lineTo(27f, 46f); lineTo(39f, 22f)
                        lineTo(55f, 78f); lineTo(67f, 42f); lineTo(76f, 58f); lineTo(93f, 58f)
                    }, tint, style = stroke)
                }
                ForestSymbol.MOON -> {
                    drawPath(Path().apply {
                        moveTo(60f, 8f); cubicTo(7f, 12f, 5f, 79f, 49f, 88f)
                        cubicTo(70f, 93f, 86f, 81f, 91f, 68f)
                        cubicTo(48f, 78f, 28f, 37f, 60f, 8f); close()
                    }, tint)
                }
                ForestSymbol.TREND -> {
                    val points = listOf(Offset(14f, 70f), Offset(38f, 44f), Offset(57f, 59f), Offset(84f, 29f))
                    drawPath(Path().apply {
                        moveTo(points[0].x, points[0].y); points.drop(1).forEach { lineTo(it.x, it.y) }
                    }, tint, style = Stroke(7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    points.forEach { drawCircle(tint, 7f, it) }
                }
                ForestSymbol.CALCULATOR -> {
                    drawRoundRect(tint, Offset(13f, 12f), Size(74f, 76f), CornerRadius(4f), style = Stroke(9f))
                    drawLine(tint, Offset(26f, 36f), Offset(43f, 36f), 6f)
                    drawLine(tint, Offset(61f, 27f), Offset(61f, 45f), 6f)
                    drawLine(tint, Offset(52f, 36f), Offset(70f, 36f), 6f)
                    drawLine(tint, Offset(35f, 55f), Offset(35f, 75f), 6f)
                    drawLine(tint, Offset(25f, 65f), Offset(45f, 65f), 6f)
                    drawLine(tint, Offset(54f, 59f), Offset(72f, 59f), 5f)
                    drawLine(tint, Offset(54f, 71f), Offset(72f, 71f), 5f)
                }
                ForestSymbol.WEIGHT, ForestSymbol.WEIGHT_FILLED -> {
                    drawRoundRect(tint, Offset(20f, 15f), Size(60f, 70f), CornerRadius(3f),
                        style = if (symbol == ForestSymbol.WEIGHT) Stroke(6f) else androidx.compose.ui.graphics.drawscope.Fill)
                    if (symbol == ForestSymbol.WEIGHT_FILLED) {
                        drawCircle(Color(0xFF2C2A1B), 10f, Offset(50f, 33f))
                        drawCircle(tint, 3f, Offset(50f, 32f))
                    } else {
                        drawPath(Path().apply {
                            moveTo(44f, 25f); cubicTo(39f, 42f, 61f, 42f, 56f, 25f)
                        }, tint, style = Stroke(4f, cap = StrokeCap.Round))
                    }
                }
                ForestSymbol.MOVEMENT -> {
                    drawPath(Path().apply {
                        moveTo(13f, 73f); lineTo(41f, 43f); lineTo(58f, 57f); lineTo(84f, 27f)
                        moveTo(69f, 27f); lineTo(84f, 27f); lineTo(84f, 42f)
                    }, tint, style = Stroke(7f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    drawCircle(tint, 6f, Offset(13f, 73f))
                }
                ForestSymbol.FLAG -> {
                    drawPath(Path().apply {
                        moveTo(22f, 90f); lineTo(22f, 15f)
                        cubicTo(44f, 1f, 58f, 31f, 84f, 14f); lineTo(84f, 57f)
                        cubicTo(58f, 74f, 44f, 44f, 22f, 58f)
                    }, tint, style = Stroke(6f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
                ForestSymbol.BARS, ForestSymbol.ASCENDING_BARS, ForestSymbol.CHART -> {
                    if (symbol == ForestSymbol.CHART) {
                        drawRoundRect(tint, Offset(12f, 12f), Size(76f, 76f), CornerRadius(3f), style = Stroke(6f))
                    }
                    (if (symbol == ForestSymbol.BARS) listOf(48f, 12f, 32f) else listOf(48f, 30f, 12f)).forEachIndexed { index, top ->
                        val adjustedTop = if (symbol == ForestSymbol.CHART) top * .7f + 22f else top
                        drawRoundRect(tint, Offset(21f + index * 23f, adjustedTop),
                            Size(16f, 86f - adjustedTop), CornerRadius(3f),
                            style = if (symbol == ForestSymbol.CHART) Stroke(4f) else androidx.compose.ui.graphics.drawscope.Fill)
                    }
                    if (symbol == ForestSymbol.ASCENDING_BARS) {
                        drawLine(tint, Offset(19f, 91f), Offset(87f, 91f), 7f, cap = StrokeCap.Round)
                    }
                }
                ForestSymbol.SHIELD -> {
                    val path = Path().apply {
                        moveTo(50f, 7f); lineTo(86f, 22f); lineTo(82f, 56f)
                        quadraticTo(76f, 78f, 50f, 93f); quadraticTo(24f, 78f, 18f, 56f)
                        lineTo(14f, 22f); close()
                    }
                    drawPath(path, tint, style = Stroke(6f, join = StrokeJoin.Round))
                    drawPath(Path().apply {
                        moveTo(50f, 12f); lineTo(50f, 50f); lineTo(19f, 50f); lineTo(17f, 24f); close()
                        moveTo(50f, 50f); lineTo(81f, 50f); quadraticTo(78f, 74f, 50f, 89f); close()
                    }, tint)
                }
                else -> Unit
            }
        }
    }
}
