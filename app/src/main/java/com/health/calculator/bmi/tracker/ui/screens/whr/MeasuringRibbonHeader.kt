package com.health.calculator.bmi.tracker.ui.screens.whr

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
internal fun MeasuringRibbonHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        Canvas(Modifier.fillMaxWidth().height(36.dp)) {
            drawRoundRect(surface, cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))
            drawLine(accent, Offset(0f, size.height * .78f), Offset(size.width, size.height * .78f), 2.dp.toPx())
            repeat(25) { i ->
                val x = size.width * i / 24f
                val top = if (i % 4 == 0) .15f else .43f
                drawLine(accent, Offset(x, size.height * top), Offset(x, size.height * .78f), 1.dp.toPx())
            }
        }
    }
}
