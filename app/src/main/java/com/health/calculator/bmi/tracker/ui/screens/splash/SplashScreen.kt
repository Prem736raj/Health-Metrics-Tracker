package com.health.calculator.bmi.tracker.ui.screens.splash

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.health.calculator.bmi.tracker.R
import com.health.calculator.bmi.tracker.ui.theme.AppColors
import kotlinx.coroutines.delay

/**
 * Animated splash screen matching the calm botanical design.
 * Shows the botanical background with the concentric wellness mark,
 * serif app name, subtitle, and pulsing dots.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    val isDark = AppColors.isDark

    // Theme-driven colors matching media_1793021528957_fcbb4e2b.png
    val bgDrawable = if (isDark) R.drawable.bg_onboarding_nature else R.drawable.bg_onboarding_nature_light
    val titleColor = if (isDark) Color.White else Color(0xFF162018)
    val subtitleColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)
    val footerColor = if (isDark) Color(0xFF869488) else Color(0xFF5A685D)
    val dotColor = if (isDark) Color(0xFFA5C896) else Color(0xFF2E623A)

    val outerRingBorder = if (isDark) Color(0x33A5C896) else Color(0x332E623A)
    val middleRingBg = if (isDark) Color(0x1FA5C896) else Color(0x1F2E623A)
    val middleRingBorder = if (isDark) Color(0x40A5C896) else Color(0x402E623A)
    val innerCircleBg = if (isDark) Color(0xFF233527) else Color(0xFFDDEBDC)
    val innerCircleBorder = if (isDark) Color(0xFF384F3C) else Color(0xFFB8D5B6)
    val heartTint = if (isDark) Color(0xFFB5D3A7) else Color(0xFF2E623A)

    // Animation states
    val logoScale = remember { Animatable(0.2f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    var fadeOut by remember { mutableStateOf(false) }

    val screenAlpha by animateFloatAsState(
        targetValue = if (fadeOut) 0f else 1f,
        animationSpec = tween(300, easing = EaseOutCubic),
        label = "screen_fade",
        finishedListener = {
            if (fadeOut) onSplashComplete()
        }
    )

    // Heartbeat pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_pulse"
    )

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(500, easing = FastOutSlowInEasing)
        )
        delay(100)
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
        delay(150)
        subtitleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
        delay(700)
        fadeOut = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(screenAlpha)
    ) {
        // Full bleed botanical nature background
        Image(
            painter = painterResource(id = bgDrawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Concentric circle wellness logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale.value)
                    .size(220.dp)
            ) {
                // Outer ring
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(ringScale)
                        .clip(CircleShape)
                        .border(1.dp, outerRingBorder, CircleShape)
                )

                // Middle glowing circle
                Box(
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)
                        .background(middleRingBg)
                        .border(1.dp, middleRingBorder, CircleShape)
                )

                // Inner circle with heart
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .scale(heartbeatScale)
                        .clip(CircleShape)
                        .background(innerCircleBg)
                        .border(1.dp, innerCircleBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = heartTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // App Name (Serif Headline)
            Text(
                text = stringResource(R.string.txt_health_calculator),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp
                ),
                color = titleColor,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = stringResource(R.string.txt_your_personal_health_companion),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    letterSpacing = 0.1.sp
                ),
                color = subtitleColor,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // 3 Animated Loading Dots
            LoadingDots(
                modifier = Modifier.alpha(subtitleAlpha.value),
                dotColor = dotColor
            )
        }

        // Bottom educational disclaimer
        Text(
            text = stringResource(R.string.txt_for_educational_purposes_only),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp
            ),
            color = footerColor,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 28.dp)
                .alpha(subtitleAlpha.value)
        )
    }
}

@Composable
private fun LoadingDots(
    modifier: Modifier = Modifier,
    dotColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val dotAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600, delayMillis = index * 200),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_$index"
            )

            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .alpha(dotAlpha)
                    .background(dotColor)
            )

            if (index < 2) {
                Spacer(modifier = Modifier.width(7.dp))
            }
        }
    }
}
