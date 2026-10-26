package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    LavaCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        // Los pedidos LISTO se destacan en verde
        colorFondo = if (estado == EstadoPedido.LISTO) Color(0xFFE8F8EE) else MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pedido #${p.pedido.id}", style = MaterialTheme.typography.titleMedium)
                EstadoPedidoChip(estado)
            }
            Text(
                p.detalles.joinToString(", ") { it.servicio.nombre },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Entrega ${p.pedido.fechaEntrega.fechaCorta()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "S/ %.2f".format(p.pedido.total),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                Text(
                    "Recógelo en el casillero ${p.pedido.casillero}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF15803D)
                )
            }
            acciones?.invoke()
        }
    }
}
