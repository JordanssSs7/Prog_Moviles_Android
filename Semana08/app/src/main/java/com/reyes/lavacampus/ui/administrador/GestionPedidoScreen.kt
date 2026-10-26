package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Casilleros
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.viewmodel.GestionPedidoViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casilleroElegido.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var confirmando by remember { mutableStateOf(false) }

    Scaffold(topBar = { AppTopBar("Gestionar pedido", onAtras = onVolver) }) { padding ->
        val p = datos
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val estado = EstadoPedido.valueOf(p.pedido.estado)
        val siguiente = estado.siguiente()
        val puedeCancelar = estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Datos del estudiante
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pedido #${p.pedido.id}", fontWeight = FontWeight.Bold)
                        Text("${p.usuario.nombre} · ${p.usuario.codigo}")
                        Text(p.usuario.correo, style = MaterialTheme.typography.bodySmall)
                    }
                    EstadoPedidoChip(estado)
                }
            }

            if (estado == EstadoPedido.CANCELADO) {
                Text("Este pedido fue cancelado", color = EstadoPedido.CANCELADO.color, fontWeight = FontWeight.SemiBold)
            } else {
                StepperEstado(estado = estado, vertical = true)
            }

            // Selector de casillero: los ocupados salen en gris y deshabilitados
            if (estado != EstadoPedido.CANCELADO && estado != EstadoPedido.ENTREGADO) {
                Text("Casillero", fontWeight = FontWeight.Bold)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Casilleros.todos.forEach { c ->
                        FilterChip(
                            selected = elegido == c,
                            onClick = { vm.elegirCasillero(c) },
                            enabled = c !in ocupados,
                            label = { Text(c) }
                        )
                    }
                }
            }

            // Total y cobro
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    p.detalles.forEach {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(it.servicio.nombre)
                            Text("S/ %.2f".format(it.detalle.cantidad * it.detalle.precioUnitario))
                        }
                    }
                    HorizontalDivider()
                    if (p.pedido.descuento > 0.0) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Descuento por volumen", color = EstadoPedido.LISTO.color)
                            Text("- S/ %.2f".format(p.pedido.descuento), color = EstadoPedido.LISTO.color)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("S/ %.2f".format(p.pedido.total), fontWeight = FontWeight.Bold)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Cobrado")
                        Switch(checked = p.pedido.pagado, onCheckedChange = vm::marcarPagado)
                    }
                }
            }

            Text(
                "Para marcar Listo elige un casillero libre. Para entregar, el pedido debe estar cobrado.",
                style = MaterialTheme.typography.bodySmall
            )

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }

            if (siguiente != null) {
                LavaButton(texto = "Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar)
            }
            if (puedeCancelar) {
                OutlinedButton(onClick = { confirmando = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancelar pedido")
                }
            }
        }

        if (confirmando) {
            AlertDialog(
                onDismissRequest = { confirmando = false },
                title = { Text("Cancelar pedido") },
                text = { Text("¿Cancelar el pedido #${p.pedido.id}?") },
                confirmButton = {
                    TextButton(onClick = { confirmando = false; vm.cancelar() }) { Text("Sí, cancelar") }
                },
                dismissButton = { TextButton(onClick = { confirmando = false }) { Text("No") } }
            )
        }
    }
}
