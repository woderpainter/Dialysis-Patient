package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.EmergencyRed

data class ChartPoint(
    val label: String,
    val value: Float,
    val secondaryValue: Float? = null
)

@Composable
fun ChartCanvas(
    title: String,
    points: List<ChartPoint>,
    targetValue: Float? = null,
    targetLabel: String? = null,
    unit: String = "",
    lineColor: Color = DialysisBluePrimary,
    secondaryLineColor: Color = EmergencyRed,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (targetValue != null && targetLabel != null) {
                    Text(
                        text = "$targetLabel: $targetValue $unit",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (points.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucune donnée enregistrée", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                val allValues = points.flatMap { listOfNotNull(it.value, it.secondaryValue) } + listOfNotNull(targetValue)
                val minY = (allValues.minOrNull() ?: 0f) * 0.9f
                val maxY = (allValues.maxOrNull() ?: 100f) * 1.1f
                val rangeY = if (maxY - minY == 0f) 1f else maxY - minY

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val paddingX = 40f
                    val paddingY = 30f
                    val chartWidth = width - (paddingX * 2)
                    val chartHeight = height - (paddingY * 2)

                    // Draw Grid Lines
                    val gridCount = 4
                    for (i in 0..gridCount) {
                        val y = paddingY + (chartHeight / gridCount) * i
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.4f),
                            start = Offset(paddingX, y),
                            end = Offset(width - paddingX, y),
                            strokeWidth = 1f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                        )
                    }

                    // Draw Target Reference Line if available
                    targetValue?.let { target ->
                        val targetY = height - paddingY - ((target - minY) / rangeY) * chartHeight
                        drawLine(
                            color = EmergencyRed.copy(alpha = 0.8f),
                            start = Offset(paddingX, targetY),
                            end = Offset(width - paddingX, targetY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f))
                        )
                    }

                    val xStep = if (points.size > 1) chartWidth / (points.size - 1) else chartWidth / 2

                    // Primary Path
                    val primaryPath = Path()
                    val primaryPoints = mutableListOf<Offset>()

                    points.forEachIndexed { index, point ->
                        val x = paddingX + (index * xStep)
                        val y = height - paddingY - ((point.value - minY) / rangeY) * chartHeight
                        primaryPoints.add(Offset(x, y))
                        if (index == 0) {
                            primaryPath.moveTo(x, y)
                        } else {
                            primaryPath.lineTo(x, y)
                        }
                    }

                    // Draw Primary Gradient Fill
                    if (primaryPoints.size > 1) {
                        val fillPath = Path().apply {
                            addPath(primaryPath)
                            lineTo(primaryPoints.last().x, height - paddingY)
                            lineTo(primaryPoints.first().x, height - paddingY)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(lineColor.copy(alpha = 0.3f), lineColor.copy(alpha = 0.0f)),
                                startY = paddingY,
                                endY = height - paddingY
                            )
                        )
                    }

                    // Draw Primary Line
                    drawPath(
                        path = primaryPath,
                        color = lineColor,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Draw Primary Circles
                    primaryPoints.forEach { pt ->
                        drawCircle(color = lineColor, radius = 6.dp.toPx(), center = pt)
                        drawCircle(color = Color.White, radius = 3.dp.toPx(), center = pt)
                    }

                    // Draw Secondary Path if secondaryValues present
                    if (points.any { it.secondaryValue != null }) {
                        val secondaryPath = Path()
                        val secondaryPoints = mutableListOf<Offset>()
                        points.forEachIndexed { index, point ->
                            point.secondaryValue?.let { secVal ->
                                val x = paddingX + (index * xStep)
                                val y = height - paddingY - ((secVal - minY) / rangeY) * chartHeight
                                secondaryPoints.add(Offset(x, y))
                                if (secondaryPoints.size == 1) {
                                    secondaryPath.moveTo(x, y)
                                } else {
                                    secondaryPath.lineTo(x, y)
                                }
                            }
                        }

                        drawPath(
                            path = secondaryPath,
                            color = secondaryLineColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        secondaryPoints.forEach { pt ->
                            drawCircle(color = secondaryLineColor, radius = 5.dp.toPx(), center = pt)
                        }
                    }
                }

                // X-Axis Labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    points.forEach { pt ->
                        Text(
                            text = pt.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
