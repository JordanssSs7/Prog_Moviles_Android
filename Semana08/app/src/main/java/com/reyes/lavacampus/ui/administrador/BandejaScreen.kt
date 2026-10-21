package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(vm: BandejaViewModel, onAbrirGestion: (Int) -> Unit, onVolver: () -> Unit) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()

    // Chips de filtro: Todos (null), Recibido, En proceso, Listo
    val chips: List<Pair<String, EstadoPedido?>> = listOf(
        "Todos" to null,
        EstadoPedido.RECIBIDO.etiqueta to EstadoPedido.RECIBIDO,
        EstadoPedido.EN_PROCESO.etiqueta to EstadoPedido.EN_PROCESO,
        EstadoPedido.LISTO.etiqueta to EstadoPedido.LISTO
    )

    Scaffold(topBar = { AppTopBar("Pedidos", onAtras = onVolver) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LavaTextField(
                valor = busqueda,
                onCambio = vm::buscar,
                etiqueta = "Buscar por estudiante o número",
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { (texto, estado) ->
                    FilterChip(selected = filtro == estado, onClick = { vm.elegirFiltro(estado) }, label = { Text(texto) })
                }
            }
            if (pedidos.isEmpty()) {
                Text("No hay pedidos con ese filtro", modifier = Modifier.padding(16.dp))
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { p ->
                    TarjetaBandeja(p, onAbrir = { onAbrirGestion(p.pedido.id) }, onIniciar = { vm.iniciar(p) })
                }
            }
        }
    }
}

@Composable
private fun TarjetaBandeja(p: PedidoConDetalle, onAbrir: () -> Unit, onIniciar: () -> Unit) {
    val estado = EstadoPedido.valueOf(p.pedido.estado)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onAbrir() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("#${p.pedido.id} · ${p.usuario.nombre}", fontWeight = FontWeight.Bold)
                EstadoPedidoChip(estado)
            }
            Text(p.detalles.joinToString(", ") { it.servicio.nombre }, style = MaterialTheme.typography.bodyMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("S/ %.2f".format(p.pedido.total), fontWeight = FontWeight.Bold)
                    Text(
                        if (p.pedido.pagado) "Cobrado" else "Pago pendiente",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                // Boton de accion segun el estado: Iniciar, Marcar listo o Entregar
                when (estado) {
                    EstadoPedido.RECIBIDO -> Button(onClick = onIniciar) { Text("Iniciar") }
                    EstadoPedido.EN_PROCESO -> Button(onClick = onAbrir) { Text("Marcar listo") }
                    EstadoPedido.LISTO -> Button(onClick = onAbrir) { Text("Entregar") }
                    else -> {}
                }
            }
        }
    }
}
