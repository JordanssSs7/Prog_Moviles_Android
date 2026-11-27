package com.reyes.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.avisoscampus.model.Aviso
import com.reyes.avisoscampus.model.Origen
import com.reyes.avisoscampus.ui.theme.FilaHistorial
import com.reyes.avisoscampus.ui.theme.OrigenBoton
import com.reyes.avisoscampus.ui.theme.OrigenModoAvion
import com.reyes.avisoscampus.ui.theme.OrigenRecordatorio
import com.reyes.avisoscampus.ui.theme.TextoSuave

/** Tarjeta "Historial". No conoce el ViewModel: solo recibe la lista. */
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía", color = TextoSuave)
        } else {
            // Column + forEach (no LazyColumn): la pantalla ya se desplaza
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                avisos.take(20).forEach { aviso -> FilaAviso(aviso) }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = FilaHistorial,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EtiquetaOrigen(aviso.origen)
            Column(modifier = Modifier.weight(1f)) {
                Text(aviso.titulo, fontWeight = FontWeight.Bold)
                Text(
                    aviso.texto,
                    color = TextoSuave,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                aviso.hora,
                color = TextoSuave,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val color = when (origen) {
        Origen.BOTON -> OrigenBoton
        Origen.RECORDATORIO -> OrigenRecordatorio
        Origen.MODO_AVION -> OrigenModoAvion
    }
    Surface(color = color, shape = RoundedCornerShape(50)) {
        Text(
            text = origen.texto,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
