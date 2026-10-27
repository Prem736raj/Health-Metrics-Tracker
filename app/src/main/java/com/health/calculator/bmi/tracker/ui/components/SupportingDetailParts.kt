package com.health.calculator.bmi.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.ui.components.detail.DetailColors

/** Small margin ornament for supporting ledgers; never replaces a control or status. */
@Composable
internal fun SupportingLeafMark(modifier: Modifier = Modifier) {
    val ink = DetailColors.Accent
    Canvas(modifier.size(28.dp, 36.dp)) {
        drawLine(ink, Offset(size.width * .25f, size.height), Offset(size.width * .65f, 0f), 2.dp.toPx(), StrokeCap.Round)
        drawOval(ink, Offset(size.width * .03f, size.height * .35f), androidx.compose.ui.geometry.Size(size.width * .42f, size.height * .22f))
        drawOval(ink, Offset(size.width * .52f, size.height * .13f), androidx.compose.ui.geometry.Size(size.width * .42f, size.height * .22f))
    }
}

@Composable
internal fun SupportingSectionLabel(title: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = DetailColors.Text)
        HorizontalDivider(Modifier.weight(1f), color = DetailColors.Outline)
    }
}

/** Retains the original Row outside detail scope; supporting actions can wrap inside it. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SupportingWrapRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    if (DetailColors.active) {
        FlowRow(modifier, horizontalArrangement = horizontalArrangement, verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
    } else {
        Row(modifier, horizontalArrangement = horizontalArrangement, verticalAlignment = verticalAlignment) { content() }
    }
}
