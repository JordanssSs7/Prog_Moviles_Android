package com.reyes.lavacampus.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.reyes.lavacampus.ui.components.esOscuro
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Dato generico que reciben los graficos del panel: etiqueta, valor y color. */
data class DatoGrafico(val etiqueta: String, val valor: Double, val color: Color)

/** Aclara el color en modo oscuro para que los verdes oscuros no se pierdan sobre la tarjeta. */
@Composable
private fun DatoGrafico.colorVisible(): Color = if (esOscuro()) lerp(color, Color.White, 0.4f) else color

/**
 * Pedidos por estado: el total en grande, una barra dividida en tramos de colores y una cuadricula
 * de 2 columnas con la cantidad de cada estado.
 */
@Composable
fun ResumenEstados(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor }
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("${total.toInt()}", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "pedidos en total",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // Barra apilada: cada tramo es proporcional a su cantidad
        Row(
            modifier = Modifier.fillMaxWidth().height(14.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            if (total > 0) {
                datos.filter { it.valor > 0 }.forEach { d ->
                    Box(Modifier.weight(d.valor.toFloat()).height(14.dp).background(d.colorVisible()))
                }
            }
        }
        datos.chunked(2).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                fila.forEach { d -> TeselaEstado(d, Modifier.weight(1f)) }
                if (fila.size == 1) Box(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TeselaEstado(d: DatoGrafico, modifier: Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(d.colorVisible().copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(d.colorVisible()))
        Column {
            Text("${d.valor.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(d.etiqueta, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Ingresos por servicio: el total de la semana y una fila por servicio con su monto, su porcentaje y una
 * barra horizontal proporcional al servicio que mas vendio.
 */
@Composable
fun IngresosPorServicio(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor }
    val maximo = datos.maxOfOrNull { it.valor } ?: 0.0
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("S/ %.2f".format(total), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "esta semana",
                modifier = Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        datos.forEach { d ->
            val parte = if (total > 0) (d.valor / total * 100).toInt() else 0
            val ancho = if (maximo > 0) (d.valor / maximo).toFloat() else 0f
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).clip(CircleShape).background(d.colorVisible()))
                    Text(
                        d.etiqueta,
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text("S/ %.2f".format(d.valor), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "  $parte %",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(d.colorVisible().copy(alpha = 0.14f))) {
                    Box(Modifier.fillMaxWidth(ancho.coerceIn(0.02f, 1f)).height(10.dp).clip(CircleShape).background(d.colorVisible()))
                }
            }
        }
    }
}
