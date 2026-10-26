package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.EstadoVacio
import com.reyes.lavacampus.ui.components.PedidoCard
import com.reyes.lavacampus.ui.components.SelectorPastilla
import com.reyes.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onVerPedido: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Mis pedidos") },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNuevoPedido,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nuevo pedido", style = MaterialTheme.typography.titleSmall) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SelectorPastilla(
                opciones = listOf("Activos (${activos.size})", "Historial (${historial.size})"),
                seleccionada = pestana,
                onSeleccionar = { pestana = it },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
            val lista = if (pestana == 0) activos else historial
            if (lista.isEmpty()) {
                EstadoVacio(
                    titulo = if (pestana == 0) "No tienes pedidos activos" else "Tu historial está vacío",
                    texto = if (pestana == 0) "Crea un pedido y sigue aquí cómo avanza tu ropa."
                    else "Los pedidos entregados o cancelados aparecerán aquí."
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.pedido.id }) { p ->
                    PedidoCard(
                        p = p,
                        onClick = { onVerPedido(p.pedido.id) },
                        acciones = when {
                            vm.puedeCancelar(p) ->
                                ({ TextButton(onClick = { porCancelar = p }) { Text("Cancelar pedido") } })
                            // Desde el historial se puede repetir el pedido con las tarifas actuales
                            pestana == 1 ->
                                ({ TextButton(onClick = { onRepetir(p.pedido.id) }) { Text("Pedir de nuevo") } })
                            else -> null
                        }
                    )
                }
            }
        }

        porCancelar?.let { p ->
            AlertDialog(
                onDismissRequest = { porCancelar = null },
                title = { Text("Cancelar pedido") },
                text = { Text("¿Seguro que quieres cancelar el pedido #${p.pedido.id}?") },
                confirmButton = {
                    TextButton(onClick = { vm.cancelar(p); porCancelar = null }) { Text("Sí, cancelar") }
                },
                dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("No") } }
            )
        }
    }
}
