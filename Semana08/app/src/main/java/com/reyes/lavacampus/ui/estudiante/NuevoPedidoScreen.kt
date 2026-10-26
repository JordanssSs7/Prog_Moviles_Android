package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.model.UMBRAL_DESCUENTO_KG
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.BarraAccion
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.SelectorCantidad
import com.reyes.lavacampus.viewmodel.NuevoPedidoViewModel
import com.reyes.lavacampus.viewmodel.maximoCantidad
import com.reyes.lavacampus.viewmodel.minimoCantidad
import com.reyes.lavacampus.viewmodel.pasoCantidad
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Verde = EstadoPedido.LISTO.color

private fun textoUnidad(unidad: String) = when (unidad) {
    Unidad.KG.name -> "kg"
    Unidad.PRENDA.name -> "prenda"
    else -> "unidad"
}

private fun textoCantidad(cantidad: Double, unidad: String) =
    if (unidad == Unidad.KG.name) "%.1f kg".format(cantidad) else "${cantidad.toInt()} ${textoUnidad(unidad)}"

@Composable
fun NuevoPedidoScreen(vm: NuevoPedidoViewModel, onVolver: () -> Unit, onPedidoCreado: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val elegidos by vm.elegidos.collectAsState()
    val total by vm.total.collectAsState()
    val subtotal by vm.subtotal.collectAsState()
    val descuento by vm.descuento.collectAsState()
    val fechaEntrega by vm.fechaEntrega.collectAsState()
    val nota by vm.nota.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val info by vm.info.collectAsState()
    val creado by vm.pedidoCreado.collectAsState()
    val recibiendo by vm.recibiendoPedidos.collectAsState()

    LaunchedEffect(creado) { if (creado) onPedidoCreado() }

    Scaffold(
        topBar = { AppTopBar("Nuevo pedido", onAtras = onVolver) },
        bottomBar = {
            // Importe a la izquierda y boton a la derecha
            BarraAccion(
                etiqueta = "Total",
                valor = "S/ %.2f".format(total),
                textoBoton = "Confirmar",
                onClick = vm::confirmar,
                enabled = elegidos.isNotEmpty() && recibiendo,
                nota = fechaEntrega?.let { "Entrega ${it.formatoCorto()}" },
                error = mensaje
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (!recibiendo) {
                item {
                    Text(
                        "La lavandería no está recibiendo pedidos",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            info?.let {
                item {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(14.dp)
                    )
                }
            }
            item { Text("Elige tus servicios", style = MaterialTheme.typography.titleMedium) }

            items(servicios, key = { it.id }) { s ->
                FilaServicio(
                    s = s,
                    cantidad = cantidades[s.id] ?: 0.0,
                    onCambio = { nueva -> vm.cambiarCantidad(s.id, nueva - (cantidades[s.id] ?: 0.0)) }
                )
            }

            item {
                LavaTextField(
                    valor = nota,
                    onCambio = vm::cambiarNota,
                    etiqueta = "Nota (opcional)",
                    singleLine = false
                )
            }

            item {
                LavaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Resumen", style = MaterialTheme.typography.titleMedium)
                        if (elegidos.isEmpty()) {
                            Text("Aún no eliges ningún servicio", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            elegidos.forEach { (s, c) ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("${s.nombre} · ${textoCantidad(c, s.unidad)}", modifier = Modifier.weight(1f))
                                    Text("S/ %.2f".format(c * s.precio))
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            // Descuento por volumen visible en el resumen
                            if (descuento > 0.0) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("S/ %.2f".format(subtotal))
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Descuento por volumen ($PORCENTAJE_DESCUENTO %)", color = Verde, style = MaterialTheme.typography.titleSmall)
                                    Text("- S/ %.2f".format(descuento), color = Verde, style = MaterialTheme.typography.titleSmall)
                                }
                            }
                            fechaEntrega?.let {
                                Text(
                                    "Entrega estimada: ${it.formatoLargo()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaServicio(s: Servicio, cantidad: Double, onCambio: (Double) -> Unit) {
    LavaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) { IconoServicio(s.icono, MaterialTheme.colorScheme.primary, tamano = 30.dp) }
            Column(modifier = Modifier.weight(1f)) {
                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                Text(
                    "S/ %.2f por %s · %d h".format(s.precio, textoUnidad(s.unidad), s.horasEntrega),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (s.unidad == Unidad.KG.name) {
                    Text(
                        "Más de ${UMBRAL_DESCUENTO_KG.toInt()} kg: $PORCENTAJE_DESCUENTO % menos",
                        style = MaterialTheme.typography.labelSmall,
                        color = Verde
                    )
                }
            }
            SelectorCantidad(
                valor = cantidad,
                paso = pasoCantidad(s.unidad),
                minimo = minimoCantidad(s.unidad),
                maximo = maximoCantidad(s.unidad),
                onCambio = onCambio
            )
        }
    }
}

private fun LocalDate.formatoLargo(): String =
    format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM", Locale("es", "PE")))

private fun LocalDate.formatoCorto(): String =
    format(DateTimeFormatter.ofPattern("EEE dd MMM", Locale("es", "PE")))
