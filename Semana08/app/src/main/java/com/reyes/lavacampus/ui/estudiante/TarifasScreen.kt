package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.reyes.lavacampus.ui.components.IlustracionLavadora
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.theme.VerdeIntermedio
import com.reyes.lavacampus.ui.theme.VerdeProfundo
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.viewmodel.TarifasViewModel

@Composable
fun TarifasScreen(vm: TarifasViewModel) {
    val servicios by vm.servicios.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Tarifas") }
    ) { padding ->
        // Las tarjetas se reparten todo el alto disponible
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (servicios.isEmpty()) {
                EstadoVacio("Sin servicios por ahora", "La lavandería aún no tiene servicios activos.")
            }
            servicios.forEach { s -> TarjetaTarifa(s, Modifier.weight(1f)) }

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

/** Tarjeta alta: a la izquierda un panel verde con la imagen del servicio y a la derecha los datos y el precio. */
@Composable
private fun TarjetaTarifa(s: Servicio, modifier: Modifier = Modifier) {
    LavaCard(modifier = modifier.fillMaxWidth().heightIn(min = 110.dp)) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(128.dp)
                    .background(Brush.linearGradient(listOf(VerdeProfundo, VerdeIntermedio))),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(92.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.16f)))
                if (s.icono == "wash") {
                    IlustracionLavadora(Modifier.size(86.dp))
                } else {
                    IconoServicio(s.icono, Color.White, tamano = 68.dp)
                }
            }
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(s.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Entrega en ${s.horasEntrega} h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (s.unidad == Unidad.KG.name) {
                        Text(
                            "Más de ${UMBRAL_DESCUENTO_KG.toInt()} kg: $PORCENTAJE_DESCUENTO % menos",
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(EstadoPedido.LISTO.color.copy(alpha = 0.14f))
                                .padding(horizontal = 10.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = EstadoPedido.LISTO.color
                        )
                    }
                }
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "S/ %.2f".format(s.precio),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        when (s.unidad) {
                            Unidad.KG.name -> "por kg"
                            Unidad.PRENDA.name -> "por prenda"
                            else -> "por unidad"
                        },
                        modifier = Modifier.padding(bottom = 5.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
