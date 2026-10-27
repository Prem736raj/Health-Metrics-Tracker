package com.health.calculator.bmi.tracker.ui.screens.heartrate

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
internal fun PulseHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        Canvas(Modifier.fillMaxWidth().height(40.dp)) {
            val mid = size.height * .6f
            val pulse = Path().apply {
                moveTo(0f, mid); lineTo(size.width * .3f, mid)
                lineTo(size.width * .4f, mid - 10f); lineTo(size.width * .46f, size.height * .95f)
                lineTo(size.width * .52f, 2f); lineTo(size.width * .6f, mid)
                lineTo(size.width, mid)
            }
            drawPath(pulse, accent, style = Stroke(2.dp.toPx()))
            repeat(5) { i ->
                val x = size.width * (.68f + i * .065f)
                drawLine(accent, Offset(x, mid), Offset(x + 8f, mid - 14f), 2.dp.toPx())
            }
        }
    }
}
