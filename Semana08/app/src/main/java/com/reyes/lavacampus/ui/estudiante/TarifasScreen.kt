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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.reyes.lavacampus.ui.components.EstadoVacio
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.viewmodel.TarifasViewModel

@Composable
fun TarifasScreen(vm: TarifasViewModel) {
    val servicios by vm.servicios.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Tarifas") }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (servicios.isEmpty()) {
                item {
                    EstadoVacio("Sin servicios por ahora", "La lavandería aún no tiene servicios activos.")
                }
            }
            items(servicios, key = { it.id }) { s -> TarjetaTarifa(s) }

            item {
                LavaCard(
                    modifier = Modifier.fillMaxWidth(),
                    colorFondo = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Horario y reglas", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text("Atención de lunes a sábado. Los domingos no se atiende.", style = MaterialTheme.typography.bodyMedium)
                        Text("• Mínimo 1 kg por servicio de lavado", style = MaterialTheme.typography.bodyMedium)
                        Text("• Máximo 3 pedidos activos por estudiante", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTarifa(s: Servicio) {
    LavaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) { IconoServicio(s.icono, MaterialTheme.colorScheme.primary, tamano = 34.dp) }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                Text(
                    "Entrega en ${s.horasEntrega} h",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (s.unidad == Unidad.KG.name) {
                    Text(
                        "Más de ${UMBRAL_DESCUENTO_KG.toInt()} kg: $PORCENTAJE_DESCUENTO % menos",
                        style = MaterialTheme.typography.labelSmall,
                        color = EstadoPedido.LISTO.color
                    )
                }
            }

            // Precio destacado a la derecha
            Column(horizontalAlignment = Alignment.End) {
                Text("S/ %.2f".format(s.precio), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    when (s.unidad) {
                        Unidad.KG.name -> "por kg"
                        Unidad.PRENDA.name -> "por prenda"
                        else -> "por unidad"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
