package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
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
import com.reyes.lavacampus.ui.components.PedidoCard
import com.reyes.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onVerPedido: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }

    Scaffold(
        topBar = { AppTopBar("Mis pedidos", onAtras = onVolver) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNuevoPedido,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nuevo pedido") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            PrimaryTabRow(selectedTabIndex = pestana) {
                Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text("Activos (${activos.size})") })
                Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text("Historial (${historial.size})") })
            }
            val lista = if (pestana == 0) activos else historial
            if (lista.isEmpty()) {
                Text(
                    if (pestana == 0) "No tienes pedidos activos" else "Aún no tienes pedidos en el historial",
                    modifier = Modifier.padding(16.dp)
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.pedido.id }) { p ->
                    PedidoCard(
                        p = p,
                        onClick = { onVerPedido(p.pedido.id) },
                        acciones = when {
                            vm.puedeCancelar(p) ->
                                ({ TextButton(onClick = { porCancelar = p }) { Text("Cancelar") } })
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
