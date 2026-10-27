package com.health.calculator.bmi.tracker.ui.screens.bloodpressure

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
internal fun PairedStemHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(Modifier.width(48.dp).height(88.dp)) {
            val left = size.width * .3f
            val right = size.width * .72f
            drawLine(accent, Offset(left, size.height), Offset(left, 4f), 3.dp.toPx())
            drawLine(outline, Offset(right, size.height), Offset(right, size.height * .25f), 3.dp.toPx())
            repeat(3) { i ->
                val y = size.height * (.25f + i * .23f)
                drawLine(accent, Offset(left, y), Offset(0f, y - 10f), 2.dp.toPx())
                drawLine(outline, Offset(right, y + 14f), Offset(size.width, y + 4f), 2.dp.toPx())
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        }
    }
}
