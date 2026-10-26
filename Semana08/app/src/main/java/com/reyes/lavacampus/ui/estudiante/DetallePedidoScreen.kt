package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import com.reyes.lavacampus.data.local.relation.DetalleConServicio
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    var confirmando by remember { mutableStateOf(false) }

    Scaffold(topBar = { AppTopBar("Detalle del pedido", onAtras = onVolver) }) { padding ->
        val p = datos
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val estado = EstadoPedido.valueOf(p.pedido.estado)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera: fecha y chip de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pedido #${p.pedido.id}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Registrado el ${p.pedido.fechaRegistro.fechaCorta()}", style = MaterialTheme.typography.bodySmall)
                }
                EstadoPedidoChip(estado)
            }

            // Seguimiento vertical (un pedido cancelado ya no avanza)
            if (estado == EstadoPedido.CANCELADO) {
                Text("Este pedido fue cancelado", color = EstadoPedido.CANCELADO.color, fontWeight = FontWeight.SemiBold)
            } else {
                StepperEstado(estado = estado, vertical = true)
                Text("Entrega estimada: ${p.pedido.fechaEntrega.fechaCorta()}")
            }

            // Casillero
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = if (estado == EstadoPedido.LISTO && p.pedido.casillero != null)
                        "Casillero asignado: ${p.pedido.casillero}"
                    else "Se asignará al estar listo",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Lineas del pedido
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Servicios", fontWeight = FontWeight.Bold)
                    p.detalles.forEach { LineaDetalle(it) }
                    HorizontalDivider()
                    if (p.pedido.descuento > 0.0) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                "Descuento por volumen ($PORCENTAJE_DESCUENTO %)",
                                color = EstadoPedido.LISTO.color,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "- S/ %.2f".format(p.pedido.descuento),
                                color = EstadoPedido.LISTO.color,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("S/ %.2f".format(p.pedido.total), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Recuadro de pago
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = if (p.pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = if (p.pedido.pagado) EstadoPedido.LISTO.color else EstadoPedido.RECIBIDO.color
                )
            }

            // Cancelacion
            OutlinedButton(
                onClick = { confirmando = true },
                enabled = puedeCancelar,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancelar pedido") }
            if (!puedeCancelar && estado != EstadoPedido.CANCELADO) {
                Text(
                    "Solo se cancela antes de empezar el lavado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (confirmando) {
            AlertDialog(
                onDismissRequest = { confirmando = false },
                title = { Text("Cancelar pedido") },
                text = { Text("¿Seguro que quieres cancelar el pedido #${p.pedido.id}?") },
                confirmButton = {
                    TextButton(onClick = { confirmando = false; vm.cancelar() }) { Text("Sí, cancelar") }
                },
                dismissButton = {
                    TextButton(onClick = { confirmando = false }) { Text("No") }
                }
            )
        }
    }
}

@Composable
private fun LineaDetalle(d: DetalleConServicio) {
    val cantidad = d.detalle.cantidad
    val textoCantidad = if (cantidad % 1.0 == 0.0) cantidad.toInt().toString() else "%.1f".format(cantidad)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f)) {
            Text(d.servicio.nombre)
            Text(
                "$textoCantidad × S/ %.2f".format(d.detalle.precioUnitario),
                style = MaterialTheme.typography.bodySmall
            )
        }
        // El subtotal guardado ya lleva el descuento; aqui se muestra el importe bruto de la linea
        Text("S/ %.2f".format(d.detalle.cantidad * d.detalle.precioUnitario))
    }
}
