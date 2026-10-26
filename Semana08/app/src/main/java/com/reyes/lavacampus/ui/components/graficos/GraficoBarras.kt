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
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Barras con el valor escrito encima de cada una. */
@Composable
fun GraficoBarras(datos: List<DatoGrafico>, alto: Dp = 150.dp, modifier: Modifier = Modifier) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0.0).coerceAtLeast(1.0)
    val medidor = rememberTextMeasurer()
    val estiloValor = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface)
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(alto)) {
            if (datos.isEmpty()) return@Canvas
            val ancho = size.width / datos.size
            val barra = ancho * 0.55f
            val reservaTexto = 22.dp.toPx() // espacio arriba para el valor de la barra mas alta
            val areaBarras = size.height - reservaTexto
            datos.forEachIndexed { i, d ->
                val h = (d.valor / maximo).toFloat() * areaBarras
                val izquierda = ancho * i + (ancho - barra) / 2
                val arriba = size.height - h
                drawRoundRect(
                    color = d.color,
                    topLeft = Offset(izquierda, arriba),
                    size = Size(barra, h),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
                val medida = medidor.measure("%.0f".format(d.valor), estiloValor)
                drawText(
                    medida,
                    topLeft = Offset(izquierda + (barra - medida.size.width) / 2f, arriba - medida.size.height - 3.dp.toPx())
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
