package com.reyes.clinicasaludplus.ui.screens.citas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.DialogoConfirmacion
import com.reyes.clinicasaludplus.ui.theme.*
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate

@Composable
fun MisCitasScreen(
    alIrAAgendar: () -> Unit,
    alVolver: () -> Unit
) {
    var citas by remember { mutableStateOf(Repositorio.citasDelUsuario()) }
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFFBFBFD),
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
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
                        Surface(
                            modifier = Modifier.size(70.dp),
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = AzulPrimario,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes citas agendadas",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agenda una consulta con nuestros especialistas cuando lo necesites.",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp),
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

    DialogoConfirmacion(
        mostrar = mostrarDialogo,
        titulo = "¿Cancelar cita médica?",
        mensaje = "¿Estás seguro de que deseas cancelar esta cita? El horario volverá a quedar disponible.",
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

    val fechaLegible = remember(cita.fecha) {
        try {
            val parsed = LocalDate.parse(cita.fecha)
            FechaUtils.formatearFechaCompleta(parsed)
        } catch (e: Exception) {
            cita.fecha
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Blanco,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila superior: foto del médico, nombre, especialidad y badge de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Carga la foto real del doctor si existe en drawable
                    if (medico != null && medico.fotoRes != 0) {
                        Image(
                            painter = painterResource(id = medico.fotoRes),
                            contentDescription = medico.nombre,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = AzulPrimario,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = medico?.nombre ?: "Médico",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = especialidad?.nombre ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Surface(
                    color = Color(0xFFECFDF5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = cita.estado,
                        color = Color(0xFF10B981),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color(0xFFF1F5F9)
            )

            // Fila inferior: fecha formal y botón cancelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fecha y hora:",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$fechaLegible • ${cita.hora} hrs",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                }

                OutlinedButton(
                    onClick = alCancelar,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Cancelar", fontSize = 12.sp)
                }
            }
        }
    }
}