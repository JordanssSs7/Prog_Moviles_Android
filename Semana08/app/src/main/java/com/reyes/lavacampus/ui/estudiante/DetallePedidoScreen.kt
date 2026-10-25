package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.data.local.relation.DetalleConServicio
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.ui.components.IlustracionCesto
import com.reyes.lavacampus.ui.components.IlustracionLavadoraLinea
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.ui.theme.TurquesaMarca
import com.reyes.lavacampus.ui.theme.TurquesaProfundo
import com.reyes.lavacampus.viewmodel.DetallePedidoViewModel

/** Los cuatro pasos del seguimiento, en orden, con el icono de cada uno. */
private val Pasos = listOf(
    Triple(EstadoPedido.RECIBIDO, "Recibido", Icons.Filled.Inbox),
    Triple(EstadoPedido.EN_PROCESO, "En proceso", Icons.Filled.LocalLaundryService),
    Triple(EstadoPedido.LISTO, "Listo", Icons.Filled.Inventory2),
    Triple(EstadoPedido.ENTREGADO, "Entregado", Icons.Filled.TaskAlt)
)

/**
 * Seguimiento del pedido como la referencia: encabezado de color con los cuatro pasos en fila, un circulo
 * grande con la ilustracion del estado, el mensaje y los detalles del pedido; abajo el boton de cancelar.
 */
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
                Column(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { confirmando = true },
                        enabled = puedeCancelar,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { Text("Cancelar pedido", style = MaterialTheme.typography.titleSmall) }
                    if (!puedeCancelar && EstadoPedido.valueOf(p.pedido.estado) != EstadoPedido.CANCELADO) {
                        Text(
                            "Solo se cancela antes de empezar el lavado",
                            modifier = Modifier.padding(top = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }
        val estado = EstadoPedido.valueOf(p.pedido.estado)

        BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
        val altoPantalla = maxHeight
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado de color: flecha, titulo y los cuatro pasos
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(TurquesaProfundo, TurquesaMarca)))
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                    Text(
                        "Pedido #${p.pedido.id}",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
                if (estado != EstadoPedido.CANCELADO) {
                    PasosEnFila(estado, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
                }
                // Espacio donde se apoya la mitad superior del circulo
                Spacer(Modifier.height(130.dp))
            }

            // Hoja blanca; el circulo grande queda a caballo entre el encabezado y la hoja
            run {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-28).dp)
                        .heightIn(min = altoPantalla - 290.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(116.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CirculoEstado(
                            estado,
                            Modifier.wrapContentSize(Alignment.TopCenter, unbounded = true).offset(y = (-92).dp)
                        )
                    }
                    Text(
                        tituloEstado(estado),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        mensajeEstado(estado, p.pedido.casillero),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(Modifier.height(24.dp))

                    // Detalles del pedido
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Detalles del pedido", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        p.detalles.forEach { LineaDetalle(it) }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        if (p.pedido.descuento > 0.0) {
                            FilaDato(
                                "Descuento por volumen ($PORCENTAJE_DESCUENTO %)",
                                "- S/ %.2f".format(p.pedido.descuento),
                                colorValor = EstadoPedido.LISTO.color
                            )
                        }
                        FilaDato("Total", "S/ %.2f".format(p.pedido.total), destacado = true)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        FilaDato("Registrado", p.pedido.fechaRegistro.fechaCorta())
                        FilaDato("Entrega estimada", p.pedido.fechaEntrega.fechaCorta())
                        FilaDato("Casillero", p.pedido.casillero ?: "Se asigna al estar listo")
                        FilaDato(
                            "Pago",
                            if (p.pedido.pagado) "Pagado" else "Pendiente, se paga al recoger",
                            colorValor = if (p.pedido.pagado) EstadoPedido.LISTO.color else EstadoPedido.RECIBIDO.color
                        )
                    }
                    Spacer(Modifier.height(24.dp))
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

/** Los cuatro pasos en fila unidos por una linea; los ya alcanzados van en blanco y los pendientes translucidos. */
@Composable
private fun PasosEnFila(estado: EstadoPedido, modifier: Modifier = Modifier) {
    val actual = Pasos.indexOfFirst { it.first == estado }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Pasos.forEachIndexed { i, (_, titulo, icono) ->
            val alcanzado = i <= actual
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth().height(44.dp), contentAlignment = Alignment.Center) {
                    // Linea hacia el paso anterior (hasta el centro del circulo)
                    if (i > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.5f)
                                .height(3.dp)
                                .background(Color.White.copy(alpha = if (alcanzado) 1f else 0.35f))
                        )
                    }
                    if (i < Pasos.lastIndex) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxWidth(0.5f)
                                .height(3.dp)
                                .background(Color.White.copy(alpha = if (i < actual) 1f else 0.35f))
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(if (i == actual) 44.dp else 38.dp)
                            .background(if (alcanzado) Color.White else TurquesaMarca, CircleShape)
                            .border(2.dp, Color.White.copy(alpha = if (alcanzado) 1f else 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icono,
                            contentDescription = titulo,
                            tint = if (alcanzado) TurquesaProfundo else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    titulo,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                    color = Color.White.copy(alpha = if (alcanzado) 1f else 0.75f),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

/** Circulo grande con aro de color y la ilustracion que corresponde al estado. */
@Composable
private fun CirculoEstado(estado: EstadoPedido, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(204.dp)
            .background(MaterialTheme.colorScheme.background, CircleShape)
            .padding(7.dp)
            .background(TurquesaMarca.copy(alpha = 0.35f), CircleShape)
            .padding(7.dp)
            .background(MaterialTheme.colorScheme.surface, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        when (estado) {
            EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO -> IlustracionLavadoraLinea(Modifier.size(132.dp))
            EstadoPedido.LISTO -> IlustracionCesto(Modifier.size(130.dp))
            EstadoPedido.ENTREGADO -> Icon(Icons.Filled.Check, contentDescription = null, tint = TurquesaMarca, modifier = Modifier.size(84.dp))
            EstadoPedido.CANCELADO -> Icon(Icons.Filled.Close, contentDescription = null, tint = EstadoPedido.CANCELADO.color, modifier = Modifier.size(84.dp))
        }
    }
}

private fun tituloEstado(estado: EstadoPedido): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Pedido recibido"
    EstadoPedido.EN_PROCESO -> "En proceso"
    EstadoPedido.LISTO -> "¡Listo para recoger!"
    EstadoPedido.ENTREGADO -> "Entregado"
    EstadoPedido.CANCELADO -> "Pedido cancelado"
}

private fun mensajeEstado(estado: EstadoPedido, casillero: String?): String = when (estado) {
    EstadoPedido.RECIBIDO -> "Recibimos tu pedido y pronto empezaremos a lavarlo."
    EstadoPedido.EN_PROCESO -> "Tu ropa se está lavando con cuidado."
    EstadoPedido.LISTO -> if (casillero != null) "Recógelo en el casillero $casillero." else "Tu ropa ya está lista para recoger."
    EstadoPedido.ENTREGADO -> "Gracias por usar LavaCampus."
    EstadoPedido.CANCELADO -> "Este pedido fue cancelado."
}

@Composable
private fun FilaDato(
    etiqueta: String,
    valor: String,
    destacado: Boolean = false,
    colorValor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Text(
            etiqueta,
            modifier = Modifier.weight(1f),
            style = if (destacado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (destacado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(12.dp))
        Text(
            valor,
            style = if (destacado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (destacado) MaterialTheme.colorScheme.primary else colorValor,
            textAlign = TextAlign.End
        )
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
