package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.DetalleConServicio
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.BarraAccion
import com.reyes.lavacampus.ui.components.BotonCircular
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.EstiloBoton
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.ui.theme.degradadoMarca
import com.reyes.lavacampus.viewmodel.DetallePedidoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    var confirmando by remember { mutableStateOf(false) }
    val p = datos

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (p != null) {
                // Importe a la izquierda y accion de cancelar a la derecha
                BarraAccion(
                    etiqueta = "Total",
                    valor = "S/ %.2f".format(p.pedido.total),
                    textoBoton = "Cancelar",
                    onClick = { confirmando = true },
                    enabled = puedeCancelar,
                    estilo = EstiloBoton.Peligro,
                    nota = if (p.pedido.descuento > 0.0) "Incluye descuento de S/ %.2f".format(p.pedido.descuento) else null
                )
            }
        }
    ) { padding ->
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }
        val estado = EstadoPedido.valueOf(p.pedido.estado)

        Box(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            // Encabezado con degradado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(degradadoMarca(Rol.CLIENTE))
            ) {
                Column(modifier = Modifier.statusBarsPadding().padding(horizontal = 20.dp, vertical = 14.dp)) {
                    BotonCircular(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Detalle del pedido", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f))
                    Text("Pedido #${p.pedido.id}", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text(
                        "Registrado el ${p.pedido.fechaRegistro.fechaCorta()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Hoja que sube sobre el encabezado
            Surface(
                modifier = Modifier.fillMaxSize().padding(top = 190.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Seguimiento
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
                                EstadoPedidoChip(estado)
                            }
                            if (estado == EstadoPedido.CANCELADO) {
                                Text(
                                    "Este pedido fue cancelado",
                                    color = EstadoPedido.CANCELADO.color,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            } else {
                                StepperEstado(estado = estado, vertical = true)
                                Text(
                                    "Entrega estimada: ${p.pedido.fechaEntrega.fechaCorta()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Casillero
                    LavaCard(
                        modifier = Modifier.fillMaxWidth(),
                        colorFondo = if (estado == EstadoPedido.LISTO) Color(0xFFE8F8EE) else MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = if (estado == EstadoPedido.LISTO && p.pedido.casillero != null)
                                "Casillero asignado: ${p.pedido.casillero}"
                            else "Se asignará al estar listo",
                            modifier = Modifier.padding(18.dp),
                            style = MaterialTheme.typography.titleSmall,
                            color = if (estado == EstadoPedido.LISTO) Color(0xFF15803D) else MaterialTheme.colorScheme.primary
                        )
                    }

                    // Servicios
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Servicios", style = MaterialTheme.typography.titleMedium)
                            p.detalles.forEach { LineaDetalle(it) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            if (p.pedido.descuento > 0.0) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        "Descuento por volumen ($PORCENTAJE_DESCUENTO %)",
                                        color = EstadoPedido.LISTO.color,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        "- S/ %.2f".format(p.pedido.descuento),
                                        color = EstadoPedido.LISTO.color,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "S/ %.2f".format(p.pedido.total),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Pago
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val color = if (p.pedido.pagado) EstadoPedido.LISTO.color else EstadoPedido.RECIBIDO.color
                            Box(Modifier.size(10.dp).background(color, CircleShape))
                            Text(
                                text = if (p.pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                                style = MaterialTheme.typography.titleSmall,
                                color = color
                            )
                        }
                    }

                    if (!puedeCancelar && estado != EstadoPedido.CANCELADO) {
                        Text(
                            "Solo se cancela antes de empezar el lavado",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
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
            Text(d.servicio.nombre, style = MaterialTheme.typography.titleSmall)
            Text(
                "$textoCantidad × S/ %.2f".format(d.detalle.precioUnitario),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // El subtotal guardado ya lleva el descuento; aqui se muestra el importe bruto de la linea
        Text("S/ %.2f".format(d.detalle.cantidad * d.detalle.precioUnitario), style = MaterialTheme.typography.titleSmall)
    }
}
