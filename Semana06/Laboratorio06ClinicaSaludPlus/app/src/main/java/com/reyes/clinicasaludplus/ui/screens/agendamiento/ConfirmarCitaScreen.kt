package com.reyes.clinicasaludplus.ui.screens.agendamiento

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun ConfirmarCitaScreen(
    medicoId: String,
    fecha: String,
    hora: String,
    alConfirmarExitoso: () -> Unit,
    alVolver: () -> Unit
) {
    val context = LocalContext.current
    val medico = remember(medicoId) { Repositorio.obtenerMedico(medicoId) }
    val especialidad = remember(medico) {
        medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    }

    Scaffold(
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Confirmar cita",
                alVolver = alVolver
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Blanco
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    BotonPrimario(
                        texto = "Agendar cita",
                        onClick = {
                            if (medico != null && especialidad != null) {
                                val exito = Repositorio.agendarCita(
                                    medicoId = medico.id,
                                    especialidadId = especialidad.id,
                                    fecha = fecha,
                                    hora = hora
                                )
                                if (exito) {
                                    alConfirmarExitoso()
                                } else {
                                    Toast.makeText(context, "El horario seleccionado ya no está disponible", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FondoGris)
                .padding(16.dp)
        ) {
            // Tarjeta Médico
            medico?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Blanco)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(AzulClaro),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = AzulPrimario)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(it.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoOscuro)
                            Text(especialidad?.nombre ?: "", fontSize = 13.sp, color = TextoGris)
                            Text(it.cmp, fontSize = 12.sp, color = TextoGris)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Detalles de la reserva
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Blanco)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ItemDetalleCita(icono = Icons.Default.CalendarToday, titulo = "Fecha", detalle = fecha)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)
                    ItemDetalleCita(icono = Icons.Default.Schedule, titulo = "Hora", detalle = "$hora hrs")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)
                    ItemDetalleCita(icono = Icons.Default.LocationOn, titulo = "Sede", detalle = "Av. La Molina 123, Lima")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)
                    ItemDetalleCita(icono = Icons.Default.Payment, titulo = "Precio consulta", detalle = "S/ ${medico?.precioConsulta ?: 0.0}")
                }
            }
        }
    }
}

@Composable
fun ItemDetalleCita(icono: ImageVector, titulo: String, detalle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(titulo, fontSize = 12.sp, color = TextoGris)
            Text(detalle, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextoOscuro)
        }
    }
}