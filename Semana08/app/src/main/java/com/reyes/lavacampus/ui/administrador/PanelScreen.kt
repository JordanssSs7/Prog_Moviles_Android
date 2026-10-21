package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.graficos.DatoGrafico
import com.reyes.lavacampus.ui.components.graficos.GraficoBarras
import com.reyes.lavacampus.ui.components.graficos.GraficoDona
import com.reyes.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, onVerListos: () -> Unit) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    Scaffold(topBar = { AppTopBar("Panel") }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 3 contadores de color
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Contador("Hoy", pedidosHoy, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                Contador("En proceso", enProceso, EstadoPedido.EN_PROCESO.color, Modifier.weight(1f))
                Contador("Por recoger", porRecoger, EstadoPedido.LISTO.color, Modifier.weight(1f))
            }

            // Ingresos de la semana
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Ingresos de la semana", color = MaterialTheme.colorScheme.onPrimary)
                    Text(
                        "S/ %.2f".format(ingresos),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Solo pedidos pagados, desde el lunes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // Alerta de pedidos LISTO esperando recojo
            if (porRecoger > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "$porRecoger pedido(s) listo(s) esperando recojo",
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.SemiBold
                        )
                        Button(onClick = onVerListos) { Text("Ver") }
                    }
                }
            }

            // Dona: pedidos por estado
            Tarjeta("Pedidos por estado") {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GraficoDona(datos = porEstado)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        porEstado.forEach { Leyenda(it) }
                    }
                }
            }

            // Barras: ingresos por servicio
            Tarjeta("Ingresos por servicio") {
                if (porServicio.isEmpty()) Text("Aún no hay ingresos esta semana")
                else {
                    GraficoBarras(datos = porServicio)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 8.dp)) {
                        porServicio.forEach { d ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Punto(d.color)
                                Text("${d.etiqueta}: S/ %.2f".format(d.valor), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, color: Color, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$valor", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = Color.White)
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun Leyenda(d: DatoGrafico) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Punto(d.color)
        Text("${d.etiqueta}: ${d.valor.toInt()}", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun Punto(color: Color) {
    Box(Modifier.size(10.dp).clip(CircleShape).background(color))
}
