package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.model.UMBRAL_DESCUENTO_KG
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.EstadoVacio
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.IlustracionLavadora
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.theme.VerdeIntermedio
import com.reyes.lavacampus.ui.theme.VerdeProfundo
import com.reyes.lavacampus.viewmodel.TarifasViewModel

@Composable
fun TarifasScreen(vm: TarifasViewModel) {
    val servicios by vm.servicios.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Tarifas") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (servicios.isEmpty()) {
                EstadoVacio("Sin servicios por ahora", "La lavandería aún no tiene servicios activos.")
            }
            servicios.forEach { s -> TarjetaTarifa(s) }

            Text(
                "Lo que debes saber",
                modifier = Modifier.padding(top = 8.dp, start = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            LavaCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Regla(Icons.Filled.Schedule, "Horario de atención", "Lunes a sábado. Los domingos no se atiende.")
                    Regla(Icons.Filled.Scale, "Mínimo por lavado", "1 kg por servicio de lavado.")
                    Regla(Icons.Filled.Inventory2, "Pedidos activos", "Máximo 3 pedidos activos por estudiante.")
                }
            }
        }
    }
}

/** Una tarifa: imagen del servicio en un cuadro verde, los datos en el centro y el precio a la derecha. */
@Composable
private fun TarjetaTarifa(s: Servicio) {
    LavaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(VerdeProfundo, VerdeIntermedio))),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(66.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)))
                if (s.icono == "wash") {
                    IlustracionLavadora(Modifier.size(62.dp))
                } else {
                    IconoServicio(s.icono, Color.White, tamano = 44.dp)
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(s.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        "Entrega en ${s.horasEntrega} h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (s.unidad == Unidad.KG.name) {
                    Text(
                        "Más de ${UMBRAL_DESCUENTO_KG.toInt()} kg: $PORCENTAJE_DESCUENTO % menos",
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(EstadoPedido.LISTO.color.copy(alpha = 0.14f))
                            .padding(horizontal = 9.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = EstadoPedido.LISTO.color
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "S/ %.2f".format(s.precio),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
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

@Composable
private fun Regla(icono: ImageVector, titulo: String, detalle: String) {
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) { Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp)) }
        Column {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
