package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * Lavadora de trazo fino con burbujas, como el dibujo de la referencia de seguimiento: contorno turquesa,
 * panel con dos botones y una ranura, la puerta redonda con agua adentro y espuma abajo a la derecha.
 */
@Composable
fun IlustracionLavadoraLinea(modifier: Modifier = Modifier, trazo: Color = Color(0xFF2E8FA3)) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val dx = (size.width - s) / 2f
        fun p(x: Float, y: Float) = Offset(dx + x * s, y * s)
        val grosor = 2.4.dp.toPx()
        val linea = Stroke(width = grosor, cap = StrokeCap.Round)
        val agua = trazo.copy(alpha = 0.16f)

        // Sombra suave en el suelo
        drawOval(trazo.copy(alpha = 0.10f), topLeft = p(0.14f, 0.915f), size = Size(0.62f * s, 0.05f * s))

        // Cuerpo
        drawRoundRect(Color(0xFFF6FCFD), topLeft = p(0.12f, 0.14f), size = Size(0.64f * s, 0.77f * s), cornerRadius = CornerRadius(0.07f * s))
        drawRoundRect(trazo, topLeft = p(0.12f, 0.14f), size = Size(0.64f * s, 0.77f * s), cornerRadius = CornerRadius(0.07f * s), style = linea)
        // Panel superior
        drawLine(trazo, p(0.12f, 0.31f), p(0.76f, 0.31f), strokeWidth = grosor, cap = StrokeCap.Round)
        drawCircle(trazo, radius = 0.022f * s, center = p(0.22f, 0.225f))
        drawCircle(trazo, radius = 0.022f * s, center = p(0.30f, 0.225f))
        drawRoundRect(trazo, topLeft = p(0.43f, 0.195f), size = Size(0.24f * s, 0.06f * s), cornerRadius = CornerRadius(0.03f * s), style = linea)

        // Puerta: aro externo, cristal y agua con una ola
        val centro = p(0.44f, 0.62f)
        drawCircle(Color.White, radius = 0.235f * s, center = centro)
        drawCircle(trazo, radius = 0.235f * s, center = centro, style = linea)
        val cristal = 0.175f * s
        drawCircle(trazo.copy(alpha = 0.07f), radius = cristal, center = centro)
        val ola = Path().apply {
            val inicio = centro.x - cristal
            val ancho = 2 * cristal
            moveTo(inicio, centro.y + cristal)
            lineTo(inicio, centro.y + 0.01f * s)
            var x = 0f
            while (x <= ancho) {
                lineTo(inicio + x, centro.y + 0.01f * s + (sin(x / ancho * 2 * PI.toFloat() * 1.5f) * 0.018f * s))
                x += ancho / 24f
            }
            lineTo(inicio + ancho, centro.y + cristal)
            close()
        }
        val circulo = Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(centro.x - cristal, centro.y - cristal, centro.x + cristal, centro.y + cristal))
        }
        clipPath(circulo) { drawPath(ola, agua) }
        drawCircle(trazo, radius = cristal, center = centro, style = linea)
        // Brillo del cristal
        drawArc(
            trazo.copy(alpha = 0.55f), startAngle = 200f, sweepAngle = 40f, useCenter = false,
            topLeft = Offset(centro.x - cristal * 0.72f, centro.y - cristal * 0.72f),
            size = Size(cristal * 1.44f, cristal * 1.44f), style = Stroke(width = grosor * 0.8f, cap = StrokeCap.Round)
        )

        // Patas
        drawLine(trazo, p(0.2f, 0.91f), p(0.2f, 0.945f), strokeWidth = grosor * 1.4f, cap = StrokeCap.Round)
        drawLine(trazo, p(0.68f, 0.91f), p(0.68f, 0.945f), strokeWidth = grosor * 1.4f, cap = StrokeCap.Round)

        // Espuma y burbujas abajo a la derecha
        val espuma = Path().apply {
            addOval(androidx.compose.ui.geometry.Rect(p(0.74f, 0.74f), Size(0.18f * s, 0.16f * s)))
            addOval(androidx.compose.ui.geometry.Rect(p(0.82f, 0.68f), Size(0.14f * s, 0.14f * s)))
        }
        drawPath(espuma, Color(0xFFE7F6F9))
        drawPath(espuma, trazo, style = linea)
        drawCircle(trazo, radius = 0.025f * s, center = p(0.88f, 0.60f), style = linea)
        drawCircle(trazo, radius = 0.016f * s, center = p(0.80f, 0.64f), style = linea)

        // Destellos arriba a la izquierda
        drawLine(trazo.copy(alpha = 0.6f), p(0.05f, 0.12f), p(0.09f, 0.16f), strokeWidth = grosor * 0.8f, cap = StrokeCap.Round)
        drawLine(trazo.copy(alpha = 0.6f), p(0.09f, 0.07f), p(0.09f, 0.11f), strokeWidth = grosor * 0.8f, cap = StrokeCap.Round)
        drawLine(trazo.copy(alpha = 0.6f), p(0.86f, 0.10f), p(0.90f, 0.14f), strokeWidth = grosor * 0.8f, cap = StrokeCap.Round)
    }
}
