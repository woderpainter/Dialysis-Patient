package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun QrCodeGenerator(
    contentString: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 200
) {
    val matrixSize = 21
    val hash = contentString.hashCode()

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = modifier.size(sizeDp.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellSize = size.width / matrixSize

                // Background
                drawRect(color = Color.White)

                // Finder Patterns at top-left, top-right, bottom-left
                val drawFinderPattern = { startRow: Int, startCol: Int ->
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(startCol * cellSize, startRow * cellSize),
                        size = Size(7 * cellSize, 7 * cellSize)
                    )
                    drawRect(
                        color = Color.White,
                        topLeft = Offset((startCol + 1) * cellSize, (startRow + 1) * cellSize),
                        size = Size(5 * cellSize, 5 * cellSize)
                    )
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset((startCol + 2) * cellSize, (startRow + 2) * cellSize),
                        size = Size(3 * cellSize, 3 * cellSize)
                    )
                }

                drawFinderPattern(0, 0)
                drawFinderPattern(0, matrixSize - 7)
                drawFinderPattern(matrixSize - 7, 0)

                // Pseudo Data Modules based on Hash
                for (r in 0 until matrixSize) {
                    for (c in 0 until matrixSize) {
                        // Skip Finder patterns
                        val isTL = r in 0..7 && c in 0..7
                        val isTR = r in 0..7 && c in (matrixSize - 8) until matrixSize
                        val isBL = r in (matrixSize - 8) until matrixSize && c in 0..7

                        if (!isTL && !isTR && !isBL) {
                            val seed = abs((hash * (r + 1) * 31 + c * 17) % 100)
                            if (seed < 48) {
                                drawRect(
                                    color = Color.Black,
                                    topLeft = Offset(c * cellSize, r * cellSize),
                                    size = Size(cellSize, cellSize)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
