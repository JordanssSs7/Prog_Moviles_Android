package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.EstadoPedido

private val Etapas = listOf(
    EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
)

/** Linea de seguimiento dibujada con Canvas. [vertical] = false para Inicio, true para Detalle. */
@Composable
fun StepperEstado(estado: EstadoPedido, vertical: Boolean = false, modifier: Modifier = Modifier) {
    val actual = Etapas.indexOf(estado).coerceAtLeast(0)
    val activo = MaterialTheme.colorScheme.primary
    val inactivo = MaterialTheme.colorScheme.outlineVariant
    if (vertical) StepperVertical(actual, activo, inactivo, modifier)
    else StepperHorizontal(actual, activo, inactivo, modifier)
}

@Composable
private fun StepperHorizontal(actual: Int, activo: Color, inactivo: Color, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(20.dp)) {
            val paso = size.width / Etapas.size
            val y = size.height / 2
            for (i in Etapas.indices) {
                val x = paso * i + paso / 2
                if (i < Etapas.lastIndex) {
                    drawLine(
                        color = if (i < actual) activo else inactivo,
                        start = Offset(x, y), end = Offset(x + paso, y), strokeWidth = 4.dp.toPx()
                    )
                }
                drawCircle(if (i <= actual) activo else inactivo, radius = 8.dp.toPx(), center = Offset(x, y))
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Etapas.forEachIndexed { i, e ->
                Text(
                    text = e.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepperVertical(actual: Int, activo: Color, inactivo: Color, modifier: Modifier) {
    val alto = 52.dp
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Canvas(modifier = Modifier.width(20.dp).height(alto * Etapas.size)) {
            val paso = size.height / Etapas.size
            val x = size.width / 2
            for (i in Etapas.indices) {
                val y = paso * i + paso / 2
                if (i < Etapas.lastIndex) {
                    drawLine(
                        color = if (i < actual) activo else inactivo,
                        start = Offset(x, y), end = Offset(x, y + paso), strokeWidth = 4.dp.toPx()
                    )
                }
                drawCircle(if (i <= actual) activo else inactivo, radius = 8.dp.toPx(), center = Offset(x, y))
            }
        }
        Column {
            Etapas.forEachIndexed { i, e ->
                Box(modifier = Modifier.height(alto), contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = e.etiqueta,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
