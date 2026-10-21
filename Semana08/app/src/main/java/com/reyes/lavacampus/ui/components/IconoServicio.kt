package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dibuja con Canvas la lavadora ("wash"), la plancha ("iron") o la camisa ("shirt"). */
@Composable
fun IconoServicio(icono: String, color: Color, tamano: Dp = 40.dp, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(tamano)) {
        val w = size.width
        val h = size.height
        val trazo = Stroke(width = w * 0.07f)
        when (icono) {
            "wash" -> {
                drawRoundRect(
                    color = color, topLeft = Offset(w * 0.12f, h * 0.08f),
                    size = Size(w * 0.76f, h * 0.84f), cornerRadius = CornerRadius(w * 0.1f), style = trazo
                )
                drawCircle(color, radius = w * 0.24f, center = Offset(w * 0.5f, h * 0.58f), style = trazo)
                drawCircle(color, radius = w * 0.05f, center = Offset(w * 0.3f, h * 0.2f))
                drawCircle(color, radius = w * 0.05f, center = Offset(w * 0.45f, h * 0.2f))
            }
            "iron" -> {
                val p = Path().apply {
                    moveTo(w * 0.1f, h * 0.78f)
                    lineTo(w * 0.9f, h * 0.78f)
                    quadraticTo(w * 0.9f, h * 0.4f, w * 0.55f, h * 0.35f)
                    lineTo(w * 0.3f, h * 0.35f)
                    quadraticTo(w * 0.1f, h * 0.4f, w * 0.1f, h * 0.78f)
                    close()
                }
                drawPath(p, color, style = trazo)
                drawLine(color, Offset(w * 0.3f, h * 0.35f), Offset(w * 0.3f, h * 0.2f), strokeWidth = w * 0.07f)
                drawLine(color, Offset(w * 0.3f, h * 0.2f), Offset(w * 0.6f, h * 0.2f), strokeWidth = w * 0.07f)
            }
            else -> { // shirt
                val p = Path().apply {
                    moveTo(w * 0.35f, h * 0.1f)
                    lineTo(w * 0.1f, h * 0.25f)
                    lineTo(w * 0.2f, h * 0.45f)
                    lineTo(w * 0.28f, h * 0.4f)
                    lineTo(w * 0.28f, h * 0.9f)
                    lineTo(w * 0.72f, h * 0.9f)
                    lineTo(w * 0.72f, h * 0.4f)
                    lineTo(w * 0.8f, h * 0.45f)
                    lineTo(w * 0.9f, h * 0.25f)
                    lineTo(w * 0.65f, h * 0.1f)
                    quadraticTo(w * 0.5f, h * 0.22f, w * 0.35f, h * 0.1f)
                    close()
                }
                drawPath(p, color, style = trazo)
            }
        }
    }
}
