package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.Avatar
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.EstadoVacio
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.LavaChip
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.viewmodel.BandejaViewModel

@Composable
fun BandejaScreen(vm: BandejaViewModel, onAbrirGestion: (Int) -> Unit) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val atrasados by vm.atrasados.collectAsState()

    // Chips de filtro: Todos (null), Recibido, En proceso, Listo
    val chips: List<Pair<String, EstadoPedido?>> = listOf(
        "Todos" to null,
        EstadoPedido.RECIBIDO.etiqueta to EstadoPedido.RECIBIDO,
        EstadoPedido.EN_PROCESO.etiqueta to EstadoPedido.EN_PROCESO,
        EstadoPedido.LISTO.etiqueta to EstadoPedido.LISTO
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Pedidos") }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LavaTextField(
                valor = busqueda,
                onCambio = vm::buscar,
                etiqueta = "Buscar por estudiante o número",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { (texto, estado) ->
                    LavaChip(texto = texto, seleccionado = filtro == estado, onClick = { vm.elegirFiltro(estado) })
                }
            }
            if (pedidos.isEmpty()) {
                EstadoVacio("Sin pedidos", "No hay pedidos que coincidan con ese filtro.")
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { p ->
                    TarjetaBandeja(
                        p = p,
                        atrasado = p.pedido.id in atrasados,
                        onAbrir = { onAbrirGestion(p.pedido.id) },
                        onIniciar = { vm.iniciar(p) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaBandeja(p: PedidoConDetalle, atrasado: Boolean, onAbrir: () -> Unit, onIniciar: () -> Unit) {
    val estado = EstadoPedido.valueOf(p.pedido.estado)
    val rojo = EstadoPedido.CANCELADO.color
    LavaCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onAbrir,
        // Un pedido atrasado se marca en rojo
        borde = if (atrasado) BorderStroke(2.dp, rojo) else null
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Avatar(p.usuario.nombre, tamano = 42.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(p.usuario.nombre, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "#${p.pedido.id} · ${p.detalles.joinToString(", ") { it.servicio.nombre }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                EstadoPedidoChip(estado)
            }
            if (atrasado) {
                Text(
                    "Atrasado: debía entregarse el ${p.pedido.fechaEntrega.fechaCorta()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = rojo
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("S/ %.2f".format(p.pedido.total), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Text(
                        if (p.pedido.pagado) "Cobrado" else "Pago pendiente",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
