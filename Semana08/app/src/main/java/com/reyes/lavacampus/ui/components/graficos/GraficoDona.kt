package com.reyes.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dato generico que reciben los graficos: etiqueta, valor y color. */
data class DatoGrafico(val etiqueta: String, val valor: Double, val color: Color)

@Composable
fun GraficoDona(datos: List<DatoGrafico>, tamano: Dp = 160.dp, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor }
    Canvas(modifier = modifier.size(tamano)) {
        val grosor = size.minDimension * 0.18f
        val lado = size.minDimension - grosor
        val arco = Size(lado, lado)
        val origen = Offset(grosor / 2, grosor / 2)
        if (total <= 0.0) {
            drawArc(Color.LightGray, 0f, 360f, false, origen, arco, style = Stroke(grosor))
            return@Canvas
        }
        var inicio = -90f
        datos.forEach { d ->
            val barrido = (d.valor / total * 360.0).toFloat()
            drawArc(d.color, inicio, barrido, false, origen, arco, style = Stroke(grosor))
            inicio += barrido
        }
    }
}
