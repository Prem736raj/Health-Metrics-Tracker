package com.health.calculator.bmi.tracker.ui.components.forest

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** Native outline glyphs matching the botanical reference's visual language. */
object ForestIcons {
    private fun icon(name: String, draw: ImageVector.Builder.() -> Unit) =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply(draw).build()

    val Weight: ImageVector by lazy {
        icon("ForestWeight") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(8f, 5f); lineTo(3f, 5f); lineTo(3f, 21f); lineTo(21f, 21f); lineTo(21f, 5f); lineTo(16f, 5f)
                moveTo(12f, 2f); curveTo(6.5f, 2f, 6.5f, 10f, 12f, 10f); curveTo(17.5f, 10f, 17.5f, 2f, 12f, 2f); close()
                moveTo(12f, 5f); lineTo(12f, 6.5f); moveTo(12f, 10f); lineTo(12f, 14f)
            }
        }
    }

    val HeartPulse: ImageVector by lazy {
        icon("ForestHeartPulse") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.7f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(12f, 21f); curveTo(8f, 18f, 2f, 13f, 2f, 7.5f)
                curveTo(2f, 2.5f, 9f, 1.5f, 12f, 6f); curveTo(15f, 1.5f, 22f, 2.5f, 22f, 7.5f)
                curveTo(22f, 13f, 16f, 18f, 12f, 21f)
                moveTo(2f, 12f); lineTo(7f, 12f); lineTo(9f, 9f); lineTo(12f, 16f)
                lineTo(15f, 11f); lineTo(17f, 12f); lineTo(22f, 12f)
            }
        }
    }

    val Dining: ImageVector by lazy {
        icon("ForestDining") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.9f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(3f, 2f); lineTo(3f, 8f); curveTo(3f, 12f, 9f, 12f, 9f, 8f); lineTo(9f, 2f)
                moveTo(6f, 2f); lineTo(6f, 21f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(20f, 2f); curveTo(12f, 2f, 12f, 13f, 17f, 13f)
                lineTo(17f, 21f); lineTo(20f, 21f); close()
            }
        }
    }

    val Clipboard: ImageVector by lazy {
        icon("ForestClipboard") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.7f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(8f, 5f); lineTo(4f, 5f); lineTo(4f, 21f); lineTo(20f, 21f); lineTo(20f, 5f); lineTo(16f, 5f)
                moveTo(9f, 3f); lineTo(15f, 3f); lineTo(15f, 7f); lineTo(9f, 7f); close()
                moveTo(8f, 12f); lineTo(8.1f, 12f); moveTo(12f, 12f); lineTo(16f, 12f)
                moveTo(8f, 16f); lineTo(8.1f, 16f); moveTo(12f, 16f); lineTo(16f, 16f)
            }
        }
    }

    val Tape: ImageVector by lazy {
        icon("ForestTape") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.7f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(2f, 8f); curveTo(2f, 2f, 22f, 2f, 22f, 8f)
                curveTo(22f, 14f, 2f, 14f, 2f, 8f); lineTo(2f, 17f)
                curveTo(2f, 22f, 22f, 22f, 22f, 17f); lineTo(22f, 8f)
                moveTo(6f, 12f); lineTo(6f, 17f); moveTo(10f, 13f); lineTo(10f, 16f)
                moveTo(15f, 13f); lineTo(15f, 18f); moveTo(19f, 12f); lineTo(19f, 16f)
            }
        }
    }

    val Trend: ImageVector by lazy {
        icon("ForestTrend") {
            path(fill = null, stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(3f, 19f); lineTo(9f, 9f); lineTo(15f, 14f); lineTo(22f, 4f)
            }
            path(fill = SolidColor(Color.Black)) {
                listOf(3f to 19f, 9f to 9f, 15f to 14f, 22f to 4f).forEach { (x, y) ->
                    moveTo(x - 2f, y); curveTo(x - 2f, y - 2.7f, x + 2f, y - 2.7f, x + 2f, y)
                    curveTo(x + 2f, y + 2.7f, x - 2f, y + 2.7f, x - 2f, y); close()
                }
            }
        }
    }
}
