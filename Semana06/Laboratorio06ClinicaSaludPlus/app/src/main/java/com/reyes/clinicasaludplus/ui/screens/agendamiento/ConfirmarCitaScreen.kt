package com.reyes.clinicasaludplus.ui.screens.agendamiento

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalTime

private const val MAX_MOTIVO = 120

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

    var motivoConsulta by rememberSaveable { mutableStateOf("Consulta de rutina") }
    var enviando by remember { mutableStateOf(false) }

    // Validación de los parámetros recibidos por navegación
    val errorCita = remember(medicoId, fecha, hora) { Repositorio.validarCita(medicoId, fecha, hora) }

    val fechaLegible = remember(fecha) {
        FechaUtils.parsearFecha(fecha)?.let { FechaUtils.formatearFechaCompleta(it) } ?: fecha
    }

    // Rango de la consulta (30 min): ej. 09:30 a 10:00
    val rangoHorario = remember(hora) {
        try {
            val inicio = LocalTime.parse(hora)
            "$hora a ${inicio.plusMinutes(30)}"
        } catch (e: Exception) {
            "$hora hrs"
        }
    }

    Scaffold(
        containerColor = Blanco,
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // --- 1. TARJETA DEL MÉDICO ---
            if (medico != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = SuperficieSuave
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FotoMedico(medico = medico, tamano = 90.dp)
                        Spacer(modifier = Modifier.width(18.dp))
                        Column {
                            Text(
                                text = medico.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = NavyTitulo
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = medico.profesion,
                                fontSize = 18.sp,
                                color = SlateTexto
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "CMP: ${medico.cmp}",
                                fontSize = 18.sp,
                                color = SlateTexto
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // --- Aviso de error si los datos de la cita no son válidos ---
            if (errorCita != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = RojoAlerta.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, RojoAlerta.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = errorCita,
                        color = RojoAlerta,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // --- 2. DETALLES DE LA RESERVA (filas con divisor) ---
            FilaDetalleCitaFiel(Icons.Default.CalendarMonth, "Fecha", fechaLegible)
            HorizontalDivider(color = BordeSuave)
            FilaDetalleCitaFiel(Icons.Outlined.Schedule, "Hora", rangoHorario)
            HorizontalDivider(color = BordeSuave)
            FilaDetalleCitaFiel(Icons.Default.LocationOn, "Tipo de atención", "Consulta presencial")
            HorizontalDivider(color = BordeSuave)
            FilaDetalleCitaFiel(Icons.Default.Place, "Dirección", "Av. Los Olivos 123\nLima")

            Spacer(modifier = Modifier.height(18.dp))

            // --- 3. MOTIVO DE CONSULTA (OPCIONAL) ---
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = NavyTitulo)) { append("Motivo de consulta ") }
                    withStyle(SpanStyle(color = SlateTexto)) { append("(opcional)") }
                },
                fontSize = 17.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = motivoConsulta,
                onValueChange = { if (it.length <= MAX_MOTIVO) motivoConsulta = it },
                placeholder = { Text("Consulta de rutina", color = SlateTexto, fontSize = 17.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 17.sp, color = Color(0xFF111827)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = BordeSuave
                ),
                minLines = 3,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. BOTÓN INFERIOR AGENDAR CITA ---
            BotonPrimario(
                texto = "Agendar cita",
                habilitado = errorCita == null && medico != null && especialidad != null && !enviando,
                onClick = {
                    if (medico == null || especialidad == null || enviando) return@BotonPrimario
                    enviando = true
                    val exito = Repositorio.agendarCita(
                        medicoId = medico.id,
                        especialidadId = especialidad.id,
                        fecha = fecha,
                        hora = hora,
                        motivo = motivoConsulta
                    )
                    if (exito) {
                        alConfirmarExitoso()
                    } else {
                        enviando = false
                        val motivoFallo = Repositorio.validarCita(medico.id, fecha, hora)
                            ?: "No se pudo agendar la cita"
                        Toast.makeText(context, motivoFallo, Toast.LENGTH_SHORT).show()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEAF2FF)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = titulo,
                fontSize = 16.sp,
                color = SlateTexto
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detalle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = NavyTitulo,
                lineHeight = 24.sp
            )
        }
    }
}
