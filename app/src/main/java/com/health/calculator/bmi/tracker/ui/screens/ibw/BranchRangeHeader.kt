package com.health.calculator.bmi.tracker.ui.screens.ibw

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
internal fun BranchRangeHeader(title: String, subtitle: String) {
    val accent = DetailColors.Accent
    val outline = DetailColors.Outline
    val surface = DetailColors.SurfaceAlt
    Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = DetailColors.Text)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = DetailColors.Secondary)
        Canvas(Modifier.fillMaxWidth().height(44.dp)) {
            val base = size.height * .8f
            drawLine(outline, Offset(0f, base), Offset(size.width, base), 2.dp.toPx())
            drawLine(accent, Offset(size.width * .2f, base), Offset(size.width * .8f, base), 4.dp.toPx())
            repeat(4) { i ->
                val x = size.width * (.25f + i * .16f)
                val tip = Offset(x + 12f, size.height * (.15f + (i % 2) * .2f))
                drawLine(accent, Offset(x, base), tip, 2.dp.toPx())
                drawCircle(accent, 3.dp.toPx(), tip)
            }
        }
    }
}
