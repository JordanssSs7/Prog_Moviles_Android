package com.reyes.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, alto: Dp = 140.dp, modifier: Modifier = Modifier) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0.0).coerceAtLeast(1.0)
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(alto)) {
            if (datos.isEmpty()) return@Canvas
            val ancho = size.width / datos.size
            val barra = ancho * 0.55f
            datos.forEachIndexed { i, d ->
                val h = (d.valor / maximo).toFloat() * size.height
                drawRoundRect(
                    color = d.color,
                    topLeft = Offset(ancho * i + (ancho - barra) / 2, size.height - h),
                    size = Size(barra, h),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            datos.forEach { d ->
                Text(
                    text = d.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
