package com.health.calculator.bmi.tracker.ui.components.detail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/** Original vector motifs, not copies of the completed hubs' raster artwork. */
@Composable
fun DetailHero(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    feature: DetailFeature = LocalDetailFeature.current
) {
    val accent = DetailColors.Accent
    val surface = DetailColors.Surface
    val raised = DetailColors.SurfaceAlt
    val shape = when (feature) {
        DetailFeature.BMI, DetailFeature.BSA -> RoundedCornerShape(6.dp, 24.dp, 6.dp, 24.dp)
        DetailFeature.BMR, DetailFeature.CALORIES -> RoundedCornerShape(24.dp, 8.dp, 24.dp, 8.dp)
        DetailFeature.WATER, DetailFeature.WAIST_HIP -> RoundedCornerShape(28.dp, 28.dp, 8.dp, 28.dp)
        else -> RoundedCornerShape(20.dp)
    }
    Surface(modifier.fillMaxWidth().testTag("detail-hero-${feature.name.lowercase()}"),
        shape = shape, color = surface, contentColor = DetailColors.Text,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Box(Modifier.background(Brush.linearGradient(listOf(surface, raised)))) {
            Canvas(Modifier.align(Alignment.CenterEnd).width(104.dp).height(120.dp)
                .clearAndSetSemantics { }) {
                scale(size.width / 100f, size.height / 100f, pivot = Offset.Zero) {
                    drawDetailMotif(feature, accent)
                }
            }
            Column(Modifier.fillMaxWidth(.73f).padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.width(if (feature == DetailFeature.BMR) 34.dp else 22.dp)
                    .height(3.dp).background(accent, RoundedCornerShape(4.dp)))
                Text(title, color = DetailColors.Text, fontFamily = FontFamily.Serif,
                    fontSize = 24.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold)
                if (subtitle.isNotBlank()) Text(subtitle, color = DetailColors.Secondary,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

private fun DrawScope.drawDetailMotif(feature: DetailFeature, ink: Color) {
    val faint = ink.copy(alpha = .10f)
    val mid = ink.copy(alpha = .26f)
    val stroke = Stroke(1.5f)
    fun line(x: Float, y: Float, xx: Float, yy: Float, color: Color = ink, width: Float = 1.6f) =
        drawLine(color, Offset(x, y), Offset(xx, yy), width, StrokeCap.Round)
    fun leaf(x: Float, y: Float, w: Float, h: Float, left: Boolean = false) {
        val sign = if (left) -1f else 1f
        val p = Path().apply {
            moveTo(x, y)
            cubicTo(x + sign*w, y - h*.15f, x + sign*w, y - h*.8f, x + sign*w*.7f, y - h)
            cubicTo(x + sign*w*.1f, y - h*.8f, x - sign*w*.1f, y - h*.3f, x, y)
            close()
        }
        drawPath(p, faint); drawPath(p, mid, style = stroke)
        line(x, y, x + sign*w*.7f, y-h, mid, .8f)
    }
    // A quiet branch ties the family together, while each instrument below differs.
    val stem = Path().apply { moveTo(91f, 99f); cubicTo(96f, 78f, 93f, 58f, 84f, 30f) }
    drawPath(stem, mid, style = stroke)
    leaf(91f, 81f, 12f, 21f, true); leaf(93f, 65f, 9f, 20f)
    when (feature) {
        DetailFeature.BMI -> {
            drawRoundRect(faint, Offset(36f, 17f), Size(27f, 68f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
            for (i in 0..10) line(38f, 24f+i*5f, if (i%5==0) 53f else 47f, 24f+i*5f, mid)
            line(64f, 30f, 64f, 76f)
            drawCircle(ink, 4f, Offset(64f, 53f)); leaf(35f, 88f, 17f, 27f, true)
        }
        DetailFeature.BMR -> {
            drawCircle(faint, 27f, Offset(52f, 38f)); drawCircle(mid, 20f, Offset(52f, 38f), style = stroke)
            for (i in 0..7) {
                val angle=i*Math.PI/4
                line(52f+cos(angle).toFloat()*24,38f+sin(angle).toFloat()*24,
                    52f+cos(angle).toFloat()*29,38f+sin(angle).toFloat()*29,mid)
            }
            for (i in 0..2) drawRoundRect(if(i==1) ink else mid, Offset(25f+i*18f, 79f-i*8f),
                Size(11f, 16f+i*8f), androidx.compose.ui.geometry.CornerRadius(3f))
        }
        DetailFeature.WATER -> {
            val drop=Path().apply { moveTo(51f,16f); cubicTo(32f,43f,24f,48f,28f,62f)
                cubicTo(35f,84f,70f,84f,76f,62f); cubicTo(80f,47f,65f,35f,51f,16f); close() }
            drawPath(drop,faint); drawPath(drop,mid,style=stroke)
            drawArc(ink,15f,150f,false,Offset(35f,45f),Size(34f,24f),style=Stroke(2f))
            for(i in 0..2) line(25f+i*6f,86f+i*5f,79f-i*6f,86f+i*5f,mid)
        }
        DetailFeature.CALORIES -> {
            drawArc(faint,0f,180f,true,Offset(16f,49f),Size(68f,57f))
            drawArc(mid,0f,180f,false,Offset(16f,49f),Size(68f,57f),style=stroke)
            line(50f,65f,50f,20f); leaf(50f,46f,16f,24f); leaf(50f,57f,17f,22f,true)
            line(21f,75f,80f,75f,ink,2f)
        }
        DetailFeature.HEART_RATE -> {
            drawCircle(faint,34f,Offset(50f,52f))
            val pulse=Path().apply { moveTo(12f,56f); lineTo(32f,56f); lineTo(40f,37f)
                lineTo(50f,71f); lineTo(60f,44f); lineTo(67f,56f); lineTo(85f,56f) }
            drawPath(pulse,ink,style=Stroke(2.4f,cap=StrokeCap.Round))
            leaf(54f,89f,14f,23f,true)
        }
        DetailFeature.BLOOD_PRESSURE -> {
            for(i in 0..1) {
                val x=32f+i*29f
                drawRoundRect(faint,Offset(x-9,22f+i*13),Size(18f,62f-i*13),androidx.compose.ui.geometry.CornerRadius(9f))
                line(x,79f,x,32f+i*13,ink,2.5f); drawCircle(ink,5f,Offset(x,32f+i*13))
            }
            line(19f,89f,78f,89f,mid)
        }
        DetailFeature.WAIST_HIP -> {
            drawOval(faint,Offset(19f,22f),Size(59f,26f)); drawOval(ink,Offset(19f,22f),Size(59f,26f),style=stroke)
            drawOval(mid,Offset(13f,63f),Size(72f,30f),style=stroke)
            line(22f,41f,17f,73f,mid); line(75f,41f,81f,73f,mid)
            for(i in 0..6) line(26f+i*7f,36f,26f+i*7f,40f,mid,.7f)
        }
        DetailFeature.BSA -> {
            leaf(28f,89f,54f,75f)
            for(i in 0..5) line(32f+i*5f,83f-i*8f,65f+i*1.2f,70f-i*7f,mid,.7f)
            line(14f,20f,14f,86f,mid); line(10f,20f,18f,20f,mid); line(10f,86f,18f,86f,mid)
        }
        DetailFeature.IDEAL_WEIGHT -> {
            drawRoundRect(faint,Offset(13f,47f),Size(70f,16f),androidx.compose.ui.geometry.CornerRadius(8f))
            line(19f,55f,80f,55f,mid,2f); drawCircle(ink,5f,Offset(49f,55f))
            line(49f,49f,38f,24f,mid); leaf(38f,28f,13f,21f,true)
            line(49f,61f,64f,86f,mid); leaf(63f,87f,14f,22f)
        }
        DetailFeature.METABOLIC -> {
            drawCircle(faint,17f,Offset(49f,53f)); drawCircle(ink,4f,Offset(49f,53f))
            for(i in 0..4) {
                val a=i*2*Math.PI/5-Math.PI/2
                val point=Offset(49f+cos(a).toFloat()*31f,53f+sin(a).toFloat()*31f)
                drawLine(mid,Offset(49f,53f),point,1.4f); drawCircle(faint,10f,point); drawCircle(mid,8f,point,style=stroke)
            }
        }
        DetailFeature.WEIGHT -> {
            line(49f,30f,49f,88f,mid,2f); line(22f,40f,77f,32f,ink,2f)
            line(24f,40f,18f,64f,mid); line(24f,40f,34f,64f,mid)
            line(75f,33f,65f,62f,mid); line(75f,33f,84f,62f,mid)
            drawArc(mid,0f,180f,false,Offset(15f,56f),Size(23f,20f),style=stroke)
            drawArc(mid,0f,180f,false,Offset(62f,54f),Size(25f,20f),style=stroke)
            leaf(49f,89f,15f,24f,true)
        }
        DetailFeature.SETTINGS -> {
            for(i in 0..2) { val y=29f+i*24f; line(20f,y,80f,y,mid); drawCircle(faint,10f,Offset(34f+i*12f,y)); drawCircle(ink,4f,Offset(34f+i*12f,y)) }
        }
        DetailFeature.CONNECTIONS -> {
            val points=listOf(Offset(49f,51f),Offset(22f,22f),Offset(77f,29f),Offset(25f,82f),Offset(76f,82f))
            points.drop(1).forEach { drawLine(mid,points[0],it,1.5f); drawCircle(faint,10f,it); drawCircle(mid,8f,it,style=stroke) }
            drawCircle(ink,8f,points[0])
        }
        DetailFeature.MILESTONES -> {
            line(23f,86f,76f,23f,mid)
            for(i in 0..2) { val x=24f+i*23; val y=84f-i*27; drawCircle(faint,8f+i*3,Offset(x,y)); leaf(x,y,8f+i*3,15f+i*6) }
        }
        DetailFeature.REMINDERS -> {
            drawCircle(faint,32f,Offset(48f,51f)); drawCircle(mid,29f,Offset(48f,51f),style=stroke)
            line(48f,51f,48f,29f); line(48f,51f,64f,59f); drawCircle(ink,3f,Offset(48f,51f))
            leaf(35f,92f,16f,23f,true)
        }
        DetailFeature.REPORT -> {
            for(i in 0..6) drawRoundRect(if(i%2==0) faint else mid,Offset(16f+i*10f,35f),Size(7f,37f),androidx.compose.ui.geometry.CornerRadius(3f))
            line(18f,80f,82f,80f,mid); leaf(26f,29f,14f,20f)
        }
        DetailFeature.ASSISTANT -> {
            drawRoundRect(faint,Offset(17f,20f),Size(65f,50f),androidx.compose.ui.geometry.CornerRadius(16f))
            for(i in 0..2) drawCircle(ink,2.6f,Offset(34f+i*14f,45f))
            line(30f,70f,23f,81f,mid); leaf(58f,90f,18f,30f,true)
        }
        DetailFeature.ARTICLES -> {
            val book=Path().apply { moveTo(15f,25f); quadraticTo(32f,18f,49f,29f); quadraticTo(65f,18f,82f,25f)
                lineTo(82f,81f); quadraticTo(66f,75f,49f,85f); quadraticTo(32f,75f,15f,81f); close() }
            drawPath(book,faint); drawPath(book,mid,style=stroke); line(49f,30f,49f,84f,mid)
            for(i in 0..3) line(24f,39f+i*9,40f,39f+i*9,mid,.8f)
        }
        DetailFeature.DATA -> {
            for(i in 0..2) { drawOval(faint,Offset(19f,20f+i*23),Size(60f,23f)); drawArc(mid,0f,180f,false,Offset(19f,20f+i*23),Size(60f,23f),style=stroke) }
            line(19f,31f,19f,77f,mid); line(79f,31f,79f,77f,mid)
        }
        DetailFeature.WELCOME -> {
            drawCircle(faint,25f,Offset(48f,46f)); drawArc(mid,180f,180f,false,Offset(23f,21f),Size(50f,50f),style=stroke)
            line(19f,72f,80f,72f,mid); line(49f,87f,49f,56f); leaf(49f,74f,13f,22f); leaf(49f,82f,15f,24f,true)
        }
        DetailFeature.HISTORY, DetailFeature.EXPORT -> {
            drawRoundRect(faint,Offset(22f,15f),Size(53f,72f),androidx.compose.ui.geometry.CornerRadius(7f))
            for(i in 0..4) line(32f,31f+i*10,if(i%2==0) 64f else 55f,31f+i*10,mid)
            if(feature==DetailFeature.EXPORT) { line(61f,82f,61f,57f); line(61f,57f,54f,65f); line(61f,57f,68f,65f) }
            else { drawCircle(ink,3f,Offset(32f,21f)); drawCircle(ink,3f,Offset(64f,21f)) }
        }
    }
}
