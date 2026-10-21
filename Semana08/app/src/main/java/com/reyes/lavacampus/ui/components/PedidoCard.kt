package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.EstadoPedido
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** "2026-10-21" -> "21 oct" */
fun String.fechaCorta(): String = runCatching {
    LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd MMM", Locale("es", "PE")))
}.getOrDefault(this)

/** Tarjeta de un pedido. [acciones] es un espacio opcional (por ejemplo el enlace "Cancelar"). */
@Composable
fun PedidoCard(
    p: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    acciones: @Composable (() -> Unit)? = null
) {
    val estado = EstadoPedido.valueOf(p.pedido.estado)
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            // Los pedidos LISTO se destacan en verde
            containerColor = if (estado == EstadoPedido.LISTO) Color(0xFFDCFCE7)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pedido #${p.pedido.id}", fontWeight = FontWeight.Bold)
                EstadoPedidoChip(estado)
            }
            Text(
                p.detalles.joinToString(", ") { it.servicio.nombre },
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Registro ${p.pedido.fechaRegistro.fechaCorta()} · Entrega ${p.pedido.fechaEntrega.fechaCorta()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text("S/ %.2f".format(p.pedido.total), fontWeight = FontWeight.Bold)
            }
            if (estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                Text(
                    "Recógelo en el casillero ${p.pedido.casillero}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF16A34A)
                )
            }
            acciones?.invoke()
        }
    }
}
