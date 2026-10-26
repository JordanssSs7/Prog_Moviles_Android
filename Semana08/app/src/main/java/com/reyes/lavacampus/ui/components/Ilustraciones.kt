package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.ui.theme.AzulMarca

/** Logo: circulo blanco con la lavadora en azul y el nombre al lado. */
@Composable
fun LogoLava(modifier: Modifier = Modifier, tamano: Dp = 48.dp, colorTexto: Color = Color.White) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(tamano * 0.25f)
    ) {
        Box(
            modifier = Modifier.size(tamano).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) { IconoServicio("wash", AzulMarca, tamano * 0.62f) }
        Text(
            text = "LavaCampus",
            color = colorTexto,
            fontSize = (tamano.value * 0.68f).sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.5).sp
        )
    }
}

/** Cesto de ropa con prendas azules y una toalla, dibujado con Canvas (estilo 3D suave). */
@Composable
fun IlustracionCesto(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val dx = (size.width - s) / 2f

        fun p(x: Float, y: Float) = Offset(dx + x * s, y * s)
        fun brocha(x1: Float, y1: Float, x2: Float, y2: Float, a: Color, b: Color) =
            Brush.linearGradient(listOf(a, b), start = p(x1, y1), end = p(x2, y2))
        val blanco = Brush.linearGradient(listOf(Color.White, Color.White))

        fun DrawScope.caja(x: Float, y: Float, w: Float, h: Float, r: Float, brush: Brush, alpha: Float = 1f) =
            drawRoundRect(brush, topLeft = p(x, y), size = Size(w * s, h * s), cornerRadius = CornerRadius(r * s), alpha = alpha)

        // Sombra en el suelo
        drawOval(Color.Black, topLeft = p(0.16f, 0.885f), size = Size(0.68f * s, 0.07f * s), alpha = 0.10f)

        // Cuerpo del cesto (trapecio con base redondeada)
        val cuerpo = Path().apply {
            moveTo(p(0.20f, 0.50f).x, p(0.20f, 0.50f).y)
            lineTo(p(0.80f, 0.50f).x, p(0.80f, 0.50f).y)
            lineTo(p(0.73f, 0.86f).x, p(0.73f, 0.86f).y)
            quadraticTo(p(0.72f, 0.90f).x, p(0.72f, 0.90f).y, p(0.68f, 0.90f).x, p(0.68f, 0.90f).y)
            lineTo(p(0.32f, 0.90f).x, p(0.32f, 0.90f).y)
            quadraticTo(p(0.28f, 0.90f).x, p(0.28f, 0.90f).y, p(0.27f, 0.86f).x, p(0.27f, 0.86f).y)
            close()
        }
        drawPath(cuerpo, brocha(0.2f, 0.5f, 0.8f, 0.9f, Color(0xFFFBF3E8), Color(0xFFE2D1BA)))

        // Ranuras del tejido: se ajustan al ancho del cesto en cada fila (el cesto se angosta hacia abajo)
        val ranura = Brush.linearGradient(listOf(Color(0xFFD3C0A4), Color(0xFFD3C0A4)))
        val lado = 0.194f // cuanto se mete cada borde por cada unidad de altura
        listOf(0.585f, 0.665f, 0.745f).forEach { y ->
            val izquierda = 0.20f + (y - 0.50f) * lado + 0.045f
            val derecha = 0.80f - (y - 0.50f) * lado - 0.045f
            val hueco = 0.028f
            val n = 4
            val ancho = ((derecha - izquierda) - hueco * (n - 1)) / n
            for (i in 0 until n) caja(izquierda + i * (ancho + hueco), y, ancho, 0.028f, 0.014f, ranura, 0.9f)
        }

        // Prendas dobladas dentro del cesto (de atras hacia adelante)
        caja(0.25f, 0.31f, 0.50f, 0.25f, 0.08f, brocha(0.25f, 0.31f, 0.75f, 0.56f, Color(0xFFA9CBFF), Color(0xFF5E98F6)))
        caja(0.29f, 0.22f, 0.42f, 0.17f, 0.075f, brocha(0.29f, 0.22f, 0.71f, 0.39f, Color(0xFF8DBBFF), Color(0xFF4A86F0)))
        caja(0.35f, 0.145f, 0.30f, 0.12f, 0.06f, brocha(0.35f, 0.145f, 0.65f, 0.265f, Color(0xFFC3DAFF), Color(0xFF72A5FB)))
        // Pliegues claros
        caja(0.31f, 0.41f, 0.38f, 0.011f, 0.0055f, blanco, 0.35f)
        caja(0.36f, 0.27f, 0.28f, 0.010f, 0.005f, blanco, 0.40f)

        // Borde del cesto
        caja(0.17f, 0.475f, 0.66f, 0.07f, 0.035f, brocha(0.17f, 0.475f, 0.83f, 0.545f, Color(0xFFFFF8EE), Color(0xFFE9D9C3)))

        // Toalla que cuelga por fuera del borde derecho
        rotate(degrees = 4f, pivot = p(0.84f, 0.55f)) {
            caja(0.765f, 0.40f, 0.155f, 0.42f, 0.04f, brocha(0.765f, 0.40f, 0.92f, 0.82f, Color(0xFF7DAEFF), Color(0xFF3B77EE)))
            caja(0.765f, 0.71f, 0.155f, 0.014f, 0.007f, blanco, 0.45f)
            caja(0.765f, 0.75f, 0.155f, 0.014f, 0.007f, blanco, 0.30f)
        }

        // Destellos de cuatro puntas con brillo suave
        fun DrawScope.destello(cx: Float, cy: Float, r: Float, alpha: Float) {
            val c = p(cx, cy)
            val rr = r * s
            val fino = rr * 0.18f
            val path = Path().apply {
                moveTo(c.x, c.y - rr)
                quadraticTo(c.x + fino, c.y - fino, c.x + rr, c.y)
                quadraticTo(c.x + fino, c.y + fino, c.x, c.y + rr)
                quadraticTo(c.x - fino, c.y + fino, c.x - rr, c.y)
                quadraticTo(c.x - fino, c.y - fino, c.x, c.y - rr)
                close()
            }
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White.copy(alpha = 0.45f * alpha), Color.Transparent),
                    center = c, radius = rr * 2.2f
                ),
                radius = rr * 2.2f, center = c
            )
            drawPath(path, Color.White, alpha = alpha)
        }
        destello(0.86f, 0.15f, 0.085f, 1f)
        destello(0.13f, 0.30f, 0.032f, 0.85f)
        destello(0.24f, 0.09f, 0.02f, 0.7f)
    }
}

/** Lavadora de carga frontal dibujada con Canvas, para el banner de oferta. */
@Composable
fun IlustracionLavadora(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        fun DrawScope.caja(x: Float, y: Float, ancho: Float, alto: Float, r: Float, brush: Brush) =
            drawRoundRect(brush, topLeft = Offset(x * w, y * h), size = Size(ancho * w, alto * h), cornerRadius = CornerRadius(r * w))

        // Sombra
        drawOval(Color.Black, topLeft = Offset(0.08f * w, 0.93f * h), size = Size(0.84f * w, 0.06f * h), alpha = 0.12f)
        // Cuerpo
        caja(0.06f, 0.02f, 0.88f, 0.93f, 0.12f, Brush.linearGradient(listOf(Color.White, Color(0xFFDDE5F2)), Offset(0f, 0f), Offset(w, h)))
        // Panel superior
        caja(0.06f, 0.02f, 0.88f, 0.20f, 0.12f, Brush.verticalGradient(listOf(Color(0xFFF4F7FC), Color(0xFFE3EAF6))))
        drawCircle(Color(0xFF9DB4D8), radius = 0.045f * w, center = Offset(0.22f * w, 0.12f * h))
        drawCircle(Color(0xFFC3D1EA), radius = 0.028f * w, center = Offset(0.35f * w, 0.12f * h))
        caja(0.52f, 0.085f, 0.32f, 0.07f, 0.04f, Brush.linearGradient(listOf(Color(0xFF1F5BFF), Color(0xFF5B94FF))))
        // Puerta: aro, vidrio azul y brillos
        val c = Offset(0.5f * w, 0.60f * h)
        drawCircle(Brush.linearGradient(listOf(Color(0xFFF1F5FB), Color(0xFFB8C7E2)), Offset(0f, 0f), Offset(w, h)), radius = 0.36f * w, center = c)
        drawCircle(Brush.radialGradient(listOf(Color(0xFF5B9BFF), Color(0xFF1A4FDB)), center = c, radius = 0.30f * w), radius = 0.30f * w, center = c)
        drawCircle(Color.White, radius = 0.05f * w, center = Offset(0.40f * w, 0.50f * h), alpha = 0.35f)
        drawCircle(Color.White, radius = 0.028f * w, center = Offset(0.58f * w, 0.70f * h), alpha = 0.30f)
        drawCircle(Color.White, radius = 0.02f * w, center = Offset(0.52f * w, 0.78f * h), alpha = 0.25f)
        // Ropa dentro del tambor
        drawCircle(Color(0xFF8FB7FF), radius = 0.17f * w, center = Offset(0.5f * w, 0.64f * h), alpha = 0.55f)
        // Brillo del vidrio
        drawArc(Color.White, startAngle = 200f, sweepAngle = 55f, useCenter = false,
            topLeft = Offset(0.24f * w, 0.34f * h), size = Size(0.52f * w, 0.52f * w),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 0.025f * w), alpha = 0.6f)
    }
}
