package com.reyes.clinicasaludplus.ui.screens.agendamiento

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate

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

    var motivoConsulta by remember { mutableStateOf("Consulta de rutina") }

    val fechaLegible = remember(fecha) {
        try {
            val parsed = LocalDate.parse(fecha)
            FechaUtils.formatearFechaCompleta(parsed)
        } catch (e: Exception) {
            fecha
        }
    }

    // Calcula el rango de horario (ej: 09:30 a 10:00)
    val rangoHorario = remember(hora) {
        try {
            val partes = hora.split(":")
            val h = partes[0].toInt()
            val m = partes[1].toInt()
            val finM = (m + 30) % 60
            val finH = if (m + 30 >= 60) h + 1 else h
            "$hora a ${String.format("%02d:%02d", finH, finM)}"
        } catch (e: Exception) {
            "$hora hrs"
        }
    }

    Scaffold(
        containerColor = Color(0xFFFBFBFD),
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Confirmar cita",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // --- 1. TARJETA DEL MÉDICO CON FOTO CIRCULAR ---
                medico?.let {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Blanco,
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (it.fotoRes != 0) {
                                Image(
                                    painter = painterResource(id = it.fotoRes),
                                    contentDescription = it.nombre,
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = AzulPrimario,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = it.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = especialidad?.nombre ?: it.cmp,
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = it.cmp,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 2. DETALLES DE LA RESERVA (CON CAJAS DE ICONO INDEPENDIENTES) ---
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Blanco,
                    shadowElevation = 1.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilaDetalleCitaFiel(
                            icono = Icons.Default.CalendarToday,
                            titulo = "Fecha",
                            detalle = fechaLegible
                        )
                        FilaDetalleCitaFiel(
                            icono = Icons.Default.Schedule,
                            titulo = "Hora",
                            detalle = rangoHorario
                        )
                        FilaDetalleCitaFiel(
                            icono = Icons.Default.LocationOn,
                            titulo = "Tipo de atención",
                            detalle = "Consulta presencial"
                        )
                        FilaDetalleCitaFiel(
                            icono = Icons.Default.Place,
                            titulo = "Dirección",
                            detalle = "Av. Los Olivos 123\nLima"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 3. MOTIVO DE CONSULTA (OPCIONAL) ---
                Text(
                    text = "Motivo de consulta (opcional)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = motivoConsulta,
                    onValueChange = { motivoConsulta = it },
                    placeholder = { Text("Consulta de rutina", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Blanco,
                        unfocusedContainerColor = Blanco,
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 4. BOTÓN INFERIOR AGENDAR CITA ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
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
}

@Composable
fun FilaDetalleCitaFiel(
    icono: ImageVector,
    titulo: String,
    detalle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Caja cuadrada suave con icono azul
        Surface(
            modifier = Modifier.size(46.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEFF6FF)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = titulo,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detalle,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                lineHeight = 16.sp
            )
        }
    }
}