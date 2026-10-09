package com.reyes.clinicasaludplus.ui.screens.agendamiento

import com.reyes.clinicasaludplus.ui.theme.Tam
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeGris
import com.reyes.clinicasaludplus.ui.theme.Crema
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro
import com.reyes.clinicasaludplus.ui.theme.VerdeExito
import com.reyes.clinicasaludplus.util.FechaUtils

@Composable
fun CitaExitosaScreen(
    alIrAInicio: () -> Unit,
    alIrAMisCitas: () -> Unit
) {
    val cita = remember { Repositorio.ultimaCita }
    val medico = remember(cita) { cita?.let { Repositorio.obtenerMedico(it.medicoId) } }
    val especialidad = remember(cita) { cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) } }
    val fechaLegible = remember(cita) {
        cita?.let { FechaUtils.parsearFecha(it.fecha)?.let { f -> FechaUtils.formatearFechaCompleta(f) } ?: it.fecha }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(VerdeExito.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Éxito",
                tint = VerdeExito,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("¡Cita agendada!", fontSize = Tam.Titulo, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tu cita médica ha sido confirmada con éxito.",
            fontSize = Tam.Pequeno,
            color = TextoGris,
            textAlign = TextAlign.Center
        )

        // Resumen de la cita recién agendada
        if (cita != null) {
            Spacer(modifier = Modifier.height(28.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Blanco,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, BordeGris)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FotoMedico(medico = medico, tamano = 54.dp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = medico?.nombre ?: "Médico",
                                fontWeight = FontWeight.Bold,
                                fontSize = Tam.Cuerpo,
                                color = TextoOscuro
                            )
                            Text(
                                text = especialidad?.nombre ?: "",
                                fontSize = Tam.Pequeno,
                                color = TextoGris
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    FilaResumen("Fecha", fechaLegible ?: "")
                    Spacer(modifier = Modifier.height(8.dp))
                    FilaResumen("Hora", "${cita.hora} hrs")
                    Spacer(modifier = Modifier.height(8.dp))
                    FilaResumen("Sede", medico?.let { Repositorio.obtenerSede(it.sedeId)?.nombre }.orEmpty())
                    if (cita.motivo.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FilaResumen("Motivo", cita.motivo)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        BotonPrimario(
            texto = "Ver mis citas",
            onClick = alIrAMisCitas
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = alIrAInicio) {
            Text("Volver al inicio", color = VerdePrimario, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FilaResumen(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = etiqueta, fontSize = Tam.Pequeno, color = TextoGris)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = valor,
            fontSize = Tam.Pequeno,
            fontWeight = FontWeight.SemiBold,
            color = TextoOscuro,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}
