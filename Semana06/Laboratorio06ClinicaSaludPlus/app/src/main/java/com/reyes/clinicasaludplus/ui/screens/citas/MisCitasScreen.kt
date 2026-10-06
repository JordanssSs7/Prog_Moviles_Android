package com.reyes.clinicasaludplus.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.DialogoConfirmacion
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun MisCitasScreen(
    alIrAAgendar: () -> Unit,
    alVolver: () -> Unit
) {
    var citas by remember { mutableStateOf(Repositorio.citasDelUsuario()) }
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Mis citas médicas",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FondoGris)
                .padding(16.dp)
        ) {
            if (citas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(AzulClaro),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = AzulPrimario,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes citas agendadas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agenda una consulta con nuestros especialistas cuando lo necesites.",
                            fontSize = 13.sp,
                            color = TextoGris,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        BotonPrimario(
                            texto = "Agendar una cita",
                            onClick = alIrAAgendar
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(citas) { cita ->
                        TarjetaItemCita(
                            cita = cita,
                            alCancelar = {
                                citaACancelar = cita
                                mostrarDialogo = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación antes de borrar la cita
    DialogoConfirmacion(
        mostrar = mostrarDialogo,
        titulo = "¿Cancelar cita médica?",
        mensaje = "¿Estás seguro de quedeseas cancelar esta cita? El horario volverá a quedar disponible.",
        onConfirmar = {
            citaACancelar?.let {
                Repositorio.cancelarCita(it.id)
                citas = Repositorio.citasDelUsuario()
            }
            mostrarDialogo = false
            citaACancelar = null
        },
        onDescartar = {
            mostrarDialogo = false
            citaACancelar = null
        }
    )
}

@Composable
fun TarjetaItemCita(
    cita: Cita,
    alCancelar: () -> Unit
) {
    val medico = remember(cita.medicoId) { Repositorio.obtenerMedico(cita.medicoId) }
    val especialidad = remember(cita.especialidadId) { Repositorio.obtenerEspecialidad(cita.especialidadId) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AzulClaro),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = AzulPrimario,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = medico?.nombre ?: "Médico",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextoOscuro
                        )
                        Text(
                            text = especialidad?.nombre ?: "",
                            fontSize = 12.sp,
                            color = TextoGris
                        )
                    }
                }

                Surface(
                    color = VerdeExito.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = cita.estado,
                        color = VerdeExito,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Fecha y hora:", fontSize = 11.sp, color = TextoGris)
                    Text(
                        text = "${cita.fecha} • ${cita.hora} hrs",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoOscuro
                    )
                }

                OutlinedButton(
                    onClick = alCancelar,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoAlerta),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RojoAlerta.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Cancelar", fontSize = 12.sp)
                }
            }
        }
    }
}