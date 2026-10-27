package com.health.calculator.bmi.tracker.ui.screens.waterintake

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
internal fun ReservoirHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(Modifier.width(64.dp).height(110.dp)) {
            val rim = 7.dp.toPx()
            drawRoundRect(surface, cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f))
            drawRoundRect(outline, cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f, 24f), style = Stroke(2.dp.toPx()))
            for (i in 0..3) {
                val y = size.height * (.45f + i * .12f)
                val flow = Path().apply {
                    moveTo(rim, y)
                    cubicTo(size.width * .35f, y - 10f, size.width * .65f, y + 10f, size.width - rim, y)
                }
                drawPath(flow, accent, style = Stroke(2.dp.toPx()))
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        }
    }
}
