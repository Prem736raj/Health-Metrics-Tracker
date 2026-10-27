package com.health.calculator.bmi.tracker.ui.screens.bsa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors

/** Decorative family identity; never encodes a calculated value. */
@Composable
internal fun SpecimenHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(Modifier.width(76.dp).height(108.dp)) {
            val leaf = Path().apply {
                moveTo(size.width * .5f, size.height)
                cubicTo(-size.width * .2f, size.height * .55f, size.width * .05f, size.height * .1f, size.width * .85f, 0f)
                cubicTo(size.width * 1.1f, size.height * .6f, size.width * .7f, size.height * .9f, size.width * .5f, size.height)
            }
            drawPath(leaf, surface)
            drawPath(leaf, accent, style = Stroke(2.dp.toPx()))
            drawLine(accent, Offset(size.width * .5f, size.height), Offset(size.width * .75f, size.height * .12f), 2.dp.toPx())
            repeat(4) { i ->
                val y = size.height * (.3f + i * .14f)
                drawLine(outline, Offset(size.width * .63f, y), Offset(size.width * .2f, y - 14f), 1.dp.toPx())
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        }
    }
}
