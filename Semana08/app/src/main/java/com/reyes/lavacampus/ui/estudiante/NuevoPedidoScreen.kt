package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.SelectorCantidad
import com.reyes.lavacampus.viewmodel.NuevoPedidoViewModel
import com.reyes.lavacampus.viewmodel.maximoCantidad
import com.reyes.lavacampus.viewmodel.minimoCantidad
import com.reyes.lavacampus.viewmodel.pasoCantidad
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    val fechaEntrega by vm.fechaEntrega.collectAsState()
    val nota by vm.nota.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creado by vm.pedidoCreado.collectAsState()

    LaunchedEffect(creado) { if (creado) onPedidoCreado() }

    Scaffold(topBar = { AppTopBar("Nuevo pedido", onAtras = onVolver) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("Elige tus servicios", fontWeight = FontWeight.Bold) }

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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Resumen", fontWeight = FontWeight.Bold)
                        if (elegidos.isEmpty()) {
                            Text("Aún no eliges ningún servicio")
                        } else {
                            elegidos.forEach { (s, c) ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("${s.nombre} · ${textoCantidad(c, s.unidad)}", modifier = Modifier.weight(1f))
                                    Text("S/ %.2f".format(c * s.precio))
                                }
                            }
                        }
                        HorizontalDivider()
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold)
                            Text("S/ %.2f".format(total), fontWeight = FontWeight.Bold)
                        }
                        fechaEntrega?.let { Text("Entrega estimada: ${it.formatoLargo()}") }
                    }
                }
            }

            mensaje?.let {
                item { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }
            }

            item {
                LavaButton(
                    texto = "Confirmar pedido",
                    onClick = vm::confirmar,
                    enabled = elegidos.isNotEmpty()
                )
            }
        }
    }
}

@Composable
private fun FilaServicio(s: Servicio, cantidad: Double, onCambio: (Double) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconoServicio(s.icono, MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(s.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "S/ %.2f por %s · %d h".format(s.precio, textoUnidad(s.unidad), s.horasEntrega),
                    style = MaterialTheme.typography.bodySmall
                )
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
