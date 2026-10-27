package com.health.calculator.bmi.tracker.ui.components.forest

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Original night roles; day values are scoped in BotanicalDayRoles. */
object ForestPalette {
    val Background = Color(0xFF07140C)
    val Surface = Color(0xFF102317)
    val Text = Color(0xFFF2F5F0)
    val Secondary = Color(0xFFBCC8BD)
    val Primary = Color(0xFFB3DD97)
    val Outline = Color(0xFF35533A)
    val Ink = Color(0xFF07120A)
    val Water = Color(0xFF7AD9EC)
    val Amber = Color(0xFFF4D17E)
    val Coral = Color(0xFFFFA49D)
    val Purple = Color(0xFFC09AF1)
    val Blue = Color(0xFF7ABDF2)
}

@Composable
@DrawableRes
fun botanicalArtwork(@DrawableRes night: Int, @DrawableRes day: Int): Int =
    if (BotanicalPalette.isLight) day else night

@Composable
fun ForestBackdrop(
    @DrawableRes resource: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier.fillMaxSize().background(BotanicalPalette.Background)) {
        Image(painterResource(resource), null, Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter, contentScale = ContentScale.FillWidth)
        content()
    }
}

@Composable
fun ForestText(
    text: String,
    size: Float,
    color: Color = BotanicalPalette.Text,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    serif: Boolean = false
) {
    Text(text, modifier, color = color, style = TextStyle(
        fontFamily = if (serif) FontFamily.Serif else FontFamily.SansSerif,
        fontSize = size.coerceAtLeast(12f).sp,
        lineHeight = (size * if (serif) 1.15f else 1.28f).coerceAtLeast(15f).sp,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        letterSpacing = 0.sp
    ))
}

@Composable
fun ForestGlass(
    modifier: Modifier = Modifier,
    accent: Color = BotanicalPalette.Primary,
    wave: Boolean = true,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(15.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val isLight = BotanicalPalette.isLight
    val surface = BotanicalPalette.Surface
    val waveColor = if (isLight) {
        if (accent == BotanicalDayRoles.Primary) BotanicalDayRoles.Selected else lerp(Color.White, accent, .25f)
    } else accent
    val shadow = if (isLight) Modifier.shadow(5.dp, shape, clip = false,
        ambientColor = Color(0xFF375C2F).copy(alpha = .05f),
        spotColor = Color(0xFF375C2F).copy(alpha = .05f)) else Modifier
    Box(
        modifier.then(shadow).clip(shape)
            .background(Brush.linearGradient(listOf(
                surface.copy(alpha = if (isLight) .96f else .86f),
                if (isLight) surface.copy(alpha = .94f) else Color(0xFF091B10).copy(alpha = .94f)
            )))
            .border(if (isLight) .5.dp else .8.dp, accent.copy(alpha = if (isLight) .18f else .22f), shape)
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
    ) {
        if (wave) {
            Canvas(Modifier.matchParentSize()) {
                val crest = Path().apply {
                    if (isLight) {
                        moveTo(0f, size.height)
                        cubicTo(size.width * .42f, size.height, size.width * .65f, size.height * .75f, size.width * .84f, size.height * .72f)
                        cubicTo(size.width * .92f, size.height * .70f, size.width * .98f, size.height * .74f, size.width, size.height * .78f)
                    } else {
                        moveTo(0f, size.height * .94f)
                        cubicTo(size.width * .27f, size.height * .7f, size.width * .47f, size.height * 1.13f, size.width * .70f, size.height * .90f)
                        cubicTo(size.width * .84f, size.height * .75f, size.width * .94f, size.height * .72f, size.width, size.height * .77f)
                    }
                }
                val fill = Path().apply {
                    addPath(crest); lineTo(size.width, size.height); lineTo(0f, size.height); close()
                }
                drawPath(fill, Brush.linearGradient(listOf(waveColor.copy(alpha = if (isLight) .10f else .025f), waveColor.copy(alpha = if (isLight) .65f else .17f))))
                drawPath(crest, accent.copy(alpha = if (isLight) .16f else .33f), style = Stroke(.6.dp.toPx()))
            }
        }
        content()
    }
}

@Composable
fun ForestBadge(
    icon: ImageVector,
    tint: Color = BotanicalPalette.Primary,
    modifier: Modifier = Modifier.size(40.dp)
) {
    val isLight = BotanicalPalette.isLight
    val center = if (isLight) {
        if (tint == BotanicalDayRoles.Primary) BotanicalDayRoles.Badge else lerp(Color.White, tint, .10f)
    } else tint.copy(alpha = .24f)
    val edge = if (isLight) {
        if (tint == BotanicalDayRoles.Primary) BotanicalDayRoles.Badge else lerp(Color.White, tint, .06f)
    } else tint.copy(alpha = .10f)
    Box(modifier.clip(CircleShape)
        .background(Brush.radialGradient(listOf(center, edge)))
        .border(.5.dp, tint.copy(alpha = .12f), CircleShape), Alignment.Center) {
        Icon(icon, null, Modifier.fillMaxSize(.56f), tint = tint)
    }
}

@Composable
fun ForestSectionTitle(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(Modifier.padding(top = 2.dp).width(4.dp).height(20.dp).background(BotanicalPalette.Primary, CircleShape))
        Spacer(Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ForestText(title, 16f, bold = true)
            subtitle?.let { ForestText(it, 12.5f, BotanicalPalette.Secondary) }
        }
    }
}
