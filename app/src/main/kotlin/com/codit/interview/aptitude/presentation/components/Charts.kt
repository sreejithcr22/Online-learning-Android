package com.codit.interview.aptitude.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codit.interview.aptitude.presentation.theme.statusColors

/**
 * Circular progress ring with the percentage in the middle.
 *
 * Replaces the `dinuscxj/CircleProgressBar` view library; the sweep is animated by
 * Compose rather than by a custom `View.onDraw` + background thread.
 */
@Composable
fun CircularProgressIndicator(
    percent: Int,
    modifier: Modifier = Modifier,
    size: Dp = 74.dp,
    strokeWidth: Dp = 7.dp,
    trackColor: Color = MaterialTheme.colorScheme.primaryContainer,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    label: String = "$percent%",
) {
    val animated by animateFloatAsState(
        targetValue = percent.coerceIn(0, 100) / 100f,
        animationSpec = tween(durationMillis = 900),
        label = "ring",
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val inset = strokeWidth.toPx() / 2f
            val arcSize = Size(this.size.width - strokeWidth.toPx(), this.size.height - strokeWidth.toPx())

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = arcSize,
                style = stroke,
            )
            if (animated > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animated,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = stroke,
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
    }
}

/** One slice of [DonutChart]. */
data class DonutSlice(val label: String, val value: Int, val color: Color)

/**
 * Donut chart with an inline legend.
 *
 * Replaces the `achartengine` pie chart, which was a heavyweight jar used for exactly
 * one screen; this is ~50 lines of `Canvas` with the same visual result.
 */
@Composable
fun DonutChart(
    slices: List<DonutSlice>,
    centerLabel: String,
    centerCaption: String,
    modifier: Modifier = Modifier,
    chartSize: Dp = 190.dp,
    thickness: Dp = 34.dp,
) {
    val total = slices.sumOf { it.value }
    val emptyColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Box(modifier = Modifier.size(chartSize), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = Stroke(width = thickness.toPx())
                val inset = thickness.toPx() / 2f
                val arcSize = Size(this.size.width - thickness.toPx(), this.size.height - thickness.toPx())

                if (total <= 0) {
                    drawArc(
                        color = emptyColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke,
                    )
                } else {
                    var startAngle = -90f
                    slices.forEach { slice ->
                        val sweep = 360f * slice.value / total
                        if (slice.value > 0) {
                            drawArc(
                                color = slice.color,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = Offset(inset, inset),
                                size = arcSize,
                                style = stroke,
                            )
                        }
                        startAngle += sweep
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = centerLabel,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = centerCaption,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            slices.forEach { slice ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        drawCircle(color = slice.color)
                    }
                    Text(
                        text = slice.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                    )
                    Text(
                        text = slice.value.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

/**
 * Per-question time chart.
 *
 * Replaces the `achartengine` XY line chart. Draws axes, gridlines, the time polyline
 * and a dot per question.
 */
@Composable
fun TimePerQuestionChart(
    values: List<Int>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
) {
    val axisColor = MaterialTheme.colorScheme.outline
    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val emptyLabel = MaterialTheme.colorScheme.onSurfaceVariant

    val maxSeconds = (values.maxOrNull() ?: 0).coerceAtLeast(1)
    val yMaxMinutes = (maxSeconds / 60f + 1f).coerceAtLeast(3f)

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (values.isEmpty()) return@Canvas

            val yMax = yMaxMinutes
            val leftInset = 34f
            val bottomInset = 22f
            val topInset = 10f
            val rightInset = 6f

            val chartWidth = size.width - leftInset - rightInset
            val chartHeight = size.height - bottomInset - topInset
            if (chartWidth <= 0f || chartHeight <= 0f) return@Canvas

            fun xFor(index: Int) =
                leftInset + chartWidth * (index.toFloat() / (values.size - 1).coerceAtLeast(1))

            fun yFor(value: Int) = topInset + chartHeight * (1f - (value / 60f) / yMax)

            // Horizontal gridlines. The Y axis is labelled with Compose `Text`
            // overlaid on the canvas rather than painted into it.
            repeat(GRID_LINE_COUNT + 1) { step ->
                val fraction = step.toFloat() / GRID_LINE_COUNT
                val y = topInset + chartHeight * fraction
                drawLine(
                    color = gridColor,
                    start = Offset(leftInset, y),
                    end = Offset(size.width - rightInset, y),
                    strokeWidth = 1f,
                )
            }

            // Axis lines.
            drawLine(axisColor, Offset(leftInset, topInset), Offset(leftInset, topInset + chartHeight), 2f)
            drawLine(
                axisColor,
                Offset(leftInset, topInset + chartHeight),
                Offset(size.width - rightInset, topInset + chartHeight),
                2f,
            )

            // Time polyline.
            values.forEachIndexed { index, value ->
                val point = Offset(xFor(index), yFor(value))
                if (index > 0) {
                    val previous = Offset(xFor(index - 1), yFor(values[index - 1]))
                    drawLine(lineColor, previous, point, strokeWidth = 5f, cap = StrokeCap.Round)
                }
            }
            values.forEachIndexed { index, value ->
                drawCircle(lineColor, radius = 5f, center = Offset(xFor(index), yFor(value)))
            }
        }

        if (values.isEmpty()) {
            Text(
                text = "No timed questions yet",
                color = emptyLabel,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            // Y axis labels, overlaid so they stay crisp and theme-aware.
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                repeat(GRID_LINE_COUNT + 1) { step ->
                    val fraction = step.toFloat() / GRID_LINE_COUNT
                    val minutes = ((1f - fraction) * yMaxMinutes).toInt()
                    Text(
                        text = "$minutes",
                        style = MaterialTheme.typography.labelSmall,
                        color = labelColor,
                        fontSize = 9.sp,
                    )
                }
            }
            Text(
                text = "Time (minutes)",
                style = MaterialTheme.typography.labelSmall,
                color = labelColor,
                fontSize = 9.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 4.dp),
            )
            Text(
                text = "Question",
                style = MaterialTheme.typography.labelSmall,
                color = labelColor,
                fontSize = 9.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp),
            )
        }
    }
}

private const val GRID_LINE_COUNT = 4
