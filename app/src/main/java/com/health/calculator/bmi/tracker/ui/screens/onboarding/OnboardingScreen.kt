package com.health.calculator.bmi.tracker.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.MonitorWeight
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val icon: ImageVector,
    val decorativeIcons: List<ImageVector>,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    @StringRes val descriptionRes: Int
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
    OnboardingPage(
        icon = Icons.Outlined.FavoriteBorder,
        decorativeIcons = listOf(Icons.Outlined.MonitorHeart, Icons.AutoMirrored.Outlined.DirectionsWalk, Icons.Outlined.WaterDrop),
        titleRes = R.string.onboarding_welcome_title,
        subtitleRes = R.string.txt_your_personal_health_companion,
        descriptionRes = R.string.onboarding_welcome_description
    ),
    OnboardingPage(
        icon = Icons.Outlined.Analytics,
        decorativeIcons = listOf(Icons.Outlined.MonitorWeight, Icons.Outlined.Timeline, Icons.AutoMirrored.Outlined.Assignment),
        titleRes = R.string.onboarding_calculators_title,
        subtitleRes = R.string.onboarding_calculators_subtitle,
        descriptionRes = R.string.onboarding_calculators_description
    ),
    OnboardingPage(
        icon = Icons.Outlined.Timeline,
        decorativeIcons = listOf(Icons.Outlined.Analytics, Icons.Outlined.Flag, Icons.AutoMirrored.Outlined.DirectionsWalk),
        titleRes = R.string.onboarding_start_title,
        subtitleRes = R.string.onboarding_start_subtitle,
        descriptionRes = R.string.onboarding_start_description
    )
)

@Composable
private fun onboardingAccentColor(pageIndex: Int): Color = when (pageIndex % 3) {
    0 -> MaterialTheme.colorScheme.primary
    1 -> MaterialTheme.colorScheme.secondary
    else -> MaterialTheme.colorScheme.tertiary
}

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSetUpProfile: () -> Unit,
    onStartAction: (OnboardingStartAction) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingPages.size - 1
    var selectedStartAction by remember { mutableStateOf<OnboardingStartAction?>(null) }
    var showContent by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(100); showContent = true }

    Scaffold(containerColor = Color.Transparent) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.background))
            )
        ) {
            AnimatedVisibility(visible = showContent, enter = fadeIn(tween(500))) {
                Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    AnimatedVisibility(visible = !isLastPage, enter = fadeIn(tween(200)), exit = fadeOut(tween(200)), modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), contentAlignment = Alignment.CenterEnd) {
                            TextButton(onClick = onComplete) {
                                Text(stringResource(R.string.txt_skip), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                            }
                        }
                    }
                    HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxWidth()) { pageIndex ->
                        OnboardingPageContent(
                            page = onboardingPages[pageIndex], pageIndex = pageIndex,
                            isCurrentPage = pagerState.currentPage == pageIndex,
                            selectedAction = selectedStartAction, onSelectAction = { selectedStartAction = it }
                        )
                    }
                    BottomSection(
                        pagerState = pagerState, isLastPage = isLastPage,
                        onNext = { scope.launch { pagerState.animateScrollToPage(page = pagerState.currentPage + 1, animationSpec = tween(400, easing = FastOutSlowInEasing)) } },
                        onComplete = onComplete, onSetUpProfile = onSetUpProfile,
                        selectedAction = selectedStartAction, onStartAction = onStartAction
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage, pageIndex: Int, isCurrentPage: Boolean,
    selectedAction: OnboardingStartAction?, onSelectAction: (OnboardingStartAction) -> Unit
) {
    val accentColor = onboardingAccentColor(pageIndex)
    val isActionPage = pageIndex == onboardingPages.lastIndex
    val illustrationBoxSize = if (isActionPage) 148.dp else 200.dp
    val illustrationRadius = if (isActionPage) 58.dp else 80.dp
    val outerIllustrationSize = if (isActionPage) 120.dp else 160.dp
    val innerIllustrationSize = if (isActionPage) 84.dp else 110.dp
    val illustrationIconSize = if (isActionPage) 38.dp else 48.dp

    val iconScale = remember { Animatable(0.5f) }
    LaunchedEffect(isCurrentPage) {
        if (isCurrentPage) {
            iconScale.snapTo(0.5f)
            iconScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "float_$pageIndex")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_y_$pageIndex"
    )

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(illustrationBoxSize).scale(iconScale.value)) {
            page.decorativeIcons.forEachIndexed { index, icon ->
                val angle = (360f / page.decorativeIcons.size) * index
                val radians = Math.toRadians(angle.toDouble())
                val x = (illustrationRadius.value * Math.cos(radians)).toFloat()
                val y = (illustrationRadius.value * Math.sin(radians)).toFloat()
                val decorFloat by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 6f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000 + index * 300, easing = EaseInOut),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "decor_${pageIndex}_$index"
                )
                Icon(icon, null, tint = accentColor.copy(alpha = 0.65f), modifier = Modifier.size(22.dp).align(Alignment.Center).offset(x.dp, (y - decorFloat).dp).alpha(0.5f))
            }
            Box(modifier = Modifier.size(outerIllustrationSize).clip(CircleShape).background(Brush.radialGradient(listOf(accentColor.copy(alpha = 0.15f), accentColor.copy(alpha = 0.03f), Color.Transparent))))
            Box(
                modifier = Modifier.size(innerIllustrationSize).clip(CircleShape)
                    .background(Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.1f), accentColor.copy(alpha = 0.04f))))
                    .border(1.5.dp, accentColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(page.icon, null, tint = accentColor, modifier = Modifier.size(illustrationIconSize).padding(bottom = floatY.dp))
            }
        }

        Spacer(modifier = Modifier.height(if (isActionPage) 16.dp else 40.dp))

        // Title uses solid color for strong contrast in both light and dark mode
        Text(stringResource(page.titleRes), style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, lineHeight = 36.sp), color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        Text(stringResource(page.subtitleRes), style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp), color = accentColor, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(page.descriptionRes), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), textAlign = TextAlign.Center, lineHeight = 24.sp, modifier = Modifier.padding(horizontal = 8.dp))

        if (isActionPage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Choose a starting point", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OnboardingActionPicker(selectedAction = selectedAction, onSelectAction = onSelectAction)
        }
    }
}

@Composable
private fun OnboardingActionPicker(selectedAction: OnboardingStartAction?, onSelectAction: (OnboardingStartAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OnboardingStartAction.entries.chunked(2).forEach { rowActions ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowActions.forEach { action ->
                    val isSelected = selectedAction == action
                    val actionLabel = stringResource(action.labelRes)
                    val actionDescription = stringResource(action.descriptionRes)
                    Surface(
                        modifier = Modifier.weight(1f).height(58.dp)
                            .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelectAction(action) })
                            .semantics { contentDescription = "$actionLabel. $actionDescription"; selected = isSelected },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(action.icon, null, modifier = Modifier.size(20.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(actionLabel, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                                Text(actionDescription, style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f) else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                if (rowActions.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun BottomSection(
    pagerState: PagerState, isLastPage: Boolean, onNext: () -> Unit,
    onComplete: () -> Unit, onSetUpProfile: () -> Unit,
    selectedAction: OnboardingStartAction?, onStartAction: (OnboardingStartAction) -> Unit
) {
    val selectedActionLabel = selectedAction?.let { stringResource(it.labelRes) }
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        PageIndicators(pageCount = onboardingPages.size, currentPage = pagerState.currentPage)
        Spacer(modifier = Modifier.height(32.dp))
        AnimatedVisibility(
            visible = isLastPage,
            enter = fadeIn(tween(300)) + slideInVertically(
                animationSpec = tween(300, easing = EaseOutCubic),
                initialOffsetY = { it / 3 }
            ),
            exit = fadeOut(tween(200))
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { selectedAction?.let(onStartAction) ?: onComplete() },
                    modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
                    contentPadding = PaddingValues(0.dp), elevation = ButtonDefaults.buttonElevation(6.dp, pressedElevation = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(selectedAction?.icon ?: Icons.Outlined.Timeline, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(selectedActionLabel?.let { stringResource(R.string.onboarding_start_with_action, it) } ?: stringResource(R.string.onboarding_explore_home), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
                TextButton(onClick = onComplete) { Text(stringResource(R.string.onboarding_explore_home_first)) }
                if (selectedAction == null) TextButton(onClick = onSetUpProfile) { Text(stringResource(R.string.onboarding_set_up_profile_instead)) }
            }
        }
        AnimatedVisibility(visible = !isLastPage, enter = fadeIn(tween(300)), exit = fadeOut(tween(200))) {
            Button(
                onClick = onNext, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = onboardingAccentColor(pagerState.currentPage), contentColor = MaterialTheme.colorScheme.onPrimary),
                contentPadding = PaddingValues(0.dp), elevation = ButtonDefaults.buttonElevation(4.dp, pressedElevation = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(stringResource(R.string.txt_next), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PageIndicators(pageCount: Int, currentPage: Int) {
    val progressDescription = stringResource(R.string.onboarding_page_progress, currentPage + 1, pageCount)
    Row(modifier = Modifier.semantics { contentDescription = progressDescription }, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val width by animateFloatAsState(if (isSelected) 28f else 8f, tween(300, easing = FastOutSlowInEasing), label = "indicator_width_$index")
            val alpha by animateFloatAsState(if (isSelected) 1f else 0.3f, tween(300), label = "indicator_alpha_$index")
            Box(modifier = Modifier.height(8.dp).width(width.dp).clip(RoundedCornerShape(4.dp)).alpha(alpha).background(if (isSelected) onboardingAccentColor(currentPage) else MaterialTheme.colorScheme.onSurfaceVariant))
        }
    }
}
