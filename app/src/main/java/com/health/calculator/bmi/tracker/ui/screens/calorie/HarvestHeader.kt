package com.health.calculator.bmi.tracker.ui.screens.calorie

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
internal fun HarvestHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        Canvas(Modifier.fillMaxWidth().height(32.dp)) {
            drawLine(outline, Offset(0f, size.height * .7f), Offset(size.width, size.height * .7f), 1.dp.toPx())
            for (i in 0..2) {
                val x = size.width * (.18f + i * .32f)
                drawLine(accent, Offset(x, size.height * .7f), Offset(x, 2f), 2.dp.toPx())
                drawOval(accent, Offset(x - 10f, 3f), Size(10f, 17f))
                drawOval(accent, Offset(x, 10f), Size(10f, 17f))
            }
        }
    }
}
