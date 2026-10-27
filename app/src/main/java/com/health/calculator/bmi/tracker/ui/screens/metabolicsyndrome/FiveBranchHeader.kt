package com.health.calculator.bmi.tracker.ui.screens.metabolicsyndrome

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
internal fun FiveBranchHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Canvas(Modifier.width(64.dp).height(108.dp)) {
            val trunk = size.width * .25f
            drawLine(outline, Offset(trunk, 0f), Offset(trunk, size.height), 2.dp.toPx())
            repeat(5) { i ->
                val y = size.height * (.1f + i * .2f)
                drawLine(accent, Offset(trunk, y), Offset(size.width * .78f, y), 2.dp.toPx())
                drawCircle(surface, 7.dp.toPx(), Offset(size.width * .78f, y))
                drawCircle(accent, 7.dp.toPx(), Offset(size.width * .78f, y), style = Stroke(2.dp.toPx()))
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        }
    }
}
