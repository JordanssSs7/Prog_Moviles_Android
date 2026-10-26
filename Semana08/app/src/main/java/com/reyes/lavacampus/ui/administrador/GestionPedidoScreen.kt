package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Casilleros
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.Avatar
import com.reyes.lavacampus.ui.components.BarraAccion
import com.reyes.lavacampus.ui.components.BotonCircular
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.EstiloBoton
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.LavaChip
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.ui.theme.degradadoMarca
import com.reyes.lavacampus.viewmodel.GestionPedidoViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onVolver: () -> Unit) {
    val datos by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casilleroElegido.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var confirmando by remember { mutableStateOf(false) }
    val p = datos

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (p != null) {
                val estado = EstadoPedido.valueOf(p.pedido.estado)
                val siguiente = estado.siguiente()
                // Importe a la izquierda y accion para avanzar de estado a la derecha
                BarraAccion(
                    etiqueta = "Total",
                    valor = "S/ %.2f".format(p.pedido.total),
                    textoBoton = when (estado) {
                        EstadoPedido.RECIBIDO -> "Iniciar"
                        EstadoPedido.EN_PROCESO -> "Marcar listo"
                        EstadoPedido.LISTO -> "Entregar"
                        EstadoPedido.ENTREGADO -> "Entregado"
                        EstadoPedido.CANCELADO -> "Cancelado"
                    },
                    onClick = vm::avanzar,
                    enabled = siguiente != null,
                    nota = if (p.pedido.pagado) "Cobrado" else "Pago pendiente",
                    error = mensaje
                )
            }
        }
    ) { padding ->
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }
        val estado = EstadoPedido.valueOf(p.pedido.estado)
        val puedeCancelar = estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO

        Box(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            // Encabezado con degradado
            Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(degradadoMarca(Rol.ADMIN))) {
                Column(modifier = Modifier.statusBarsPadding().padding(horizontal = 20.dp, vertical = 14.dp)) {
                    BotonCircular(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Spacer(Modifier.height(14.dp))
                    Text("Gestionar pedido", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.85f))
                    Text("Pedido #${p.pedido.id}", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text(p.usuario.nombre, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize().padding(top = 180.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Datos del estudiante
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Avatar(p.usuario.nombre, tamano = 46.dp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(p.usuario.nombre, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${p.usuario.codigo} · ${p.usuario.correo}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            EstadoPedidoChip(estado)
                        }
                    }

                    // Seguimiento
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Seguimiento", style = MaterialTheme.typography.titleMedium)
                            if (estado == EstadoPedido.CANCELADO) {
                                Text("Este pedido fue cancelado", color = EstadoPedido.CANCELADO.color, style = MaterialTheme.typography.titleSmall)
                            } else {
                                StepperEstado(estado = estado, vertical = true)
                            }
                        }
                    }

                    // Selector de casillero: los ocupados salen en gris y deshabilitados
                    if (estado != EstadoPedido.CANCELADO && estado != EstadoPedido.ENTREGADO) {
                        LavaCard(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text("Casillero para el recojo", style = MaterialTheme.typography.titleMedium)
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Casilleros.todos.forEach { c ->
                                        LavaChip(
                                            texto = c,
                                            seleccionado = elegido == c,
                                            onClick = { vm.elegirCasillero(c) },
                                            enabled = c !in ocupados
                                        )
                                    }
                                }
                                Text(
                                    "Los casilleros en gris ya están ocupados",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Total y cobro
                    LavaCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            p.detalles.forEach {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(it.servicio.nombre)
                                    Text("S/ %.2f".format(it.detalle.cantidad * it.detalle.precioUnitario))
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            if (p.pedido.descuento > 0.0) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Descuento por volumen", color = EstadoPedido.LISTO.color)
                                    Text("- S/ %.2f".format(p.pedido.descuento), color = EstadoPedido.LISTO.color)
                                }
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Total S/ %.2f".format(p.pedido.total), style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "Se cobra al entregarse",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
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
                        "Regla: no se entrega un pedido sin cobrar, ni se pasa a Listo sin casillero.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(14.dp)
                    )

                    if (puedeCancelar) {
                        LavaButton(texto = "Cancelar pedido", onClick = { confirmando = true }, estilo = EstiloBoton.Peligro)
                    }
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
