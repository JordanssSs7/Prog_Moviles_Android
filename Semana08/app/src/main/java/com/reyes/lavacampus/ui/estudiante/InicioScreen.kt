package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.PedidoCard
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onVerPedido: (Int) -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()
    val primerNombre = vm.usuario.nombre.substringBefore(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text("Hola, $primerNombre", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(vm.usuario.codigo, style = MaterialTheme.typography.bodyMedium)
        }

        TarjetaPedidoActivo(activo, onVerPedido)

        LavaButton(texto = "Nuevo pedido", onClick = onNuevoPedido)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onMisPedidos, modifier = Modifier.weight(1f)) { Text("Mis pedidos") }
            OutlinedButton(onClick = onTarifas, modifier = Modifier.weight(1f)) { Text("Tarifas") }
        }

        Text("Últimos pedidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (recientes.isEmpty()) {
            Text("Aún no has hecho pedidos")
        } else {
            recientes.forEach { p -> PedidoCard(p, onClick = { onVerPedido(p.pedido.id) }) }
        }
    }
}

@Composable
private fun TarjetaPedidoActivo(activo: PedidoConDetalle?, onVerPedido: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (activo == null) {
                Text("No tienes pedidos activos", fontWeight = FontWeight.SemiBold)
            } else {
                val estado = EstadoPedido.valueOf(activo.pedido.estado)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Pedido #${activo.pedido.id}", fontWeight = FontWeight.Bold)
                    EstadoPedidoChip(estado)
                }
                StepperEstado(estado = estado, vertical = false)
                Text("Entrega estimada: ${activo.pedido.fechaEntrega.fechaCorta()}")
                if (estado == EstadoPedido.LISTO && activo.pedido.casillero != null) {
                    Text(
                        "Recógelo en el casillero ${activo.pedido.casillero}",
                        fontWeight = FontWeight.Bold,
                        color = EstadoPedido.LISTO.color
                    )
                }
                OutlinedButton(onClick = { onVerPedido(activo.pedido.id) }) { Text("Ver detalle") }
            }
        }
    }
}
