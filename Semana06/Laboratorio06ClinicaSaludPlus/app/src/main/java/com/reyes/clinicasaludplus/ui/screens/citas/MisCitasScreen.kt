package com.reyes.clinicasaludplus.ui.screens.citas

import com.reyes.clinicasaludplus.ui.theme.Tam
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.DestinoMenu
import com.reyes.clinicasaludplus.ui.components.PantallaConMenu
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.DialogoConfirmacion
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponible
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponibleFondo
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MisCitasScreen(
    alIrAAgendar: () -> Unit,
    alNavegar: (DestinoMenu) -> Unit,
    alIrAInicio: () -> Unit,
    alCerrarSesion: () -> Unit
) {
    var citas by remember { mutableStateOf(Repositorio.citasDelUsuario()) }
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    PantallaConMenu(
        titulo = "Agenda",
        seleccionado = DestinoMenu.Agenda,
        alNavegar = alNavegar,
        alIrAInicio = alIrAInicio,
        alCerrarSesion = alCerrarSesion,
        bottomBar = {
            if (citas.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blanco)
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    BotonPrimario(texto = "Agendar nueva cita", onClick = alIrAAgendar)
                }
            }
        }
    ) { innerPadding ->
        if (citas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(104.dp),
                        shape = CircleShape,
                        color = Color(0xFFE6E2D6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = VerdePrimario,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "No tienes citas agendadas",
                        fontSize = Tam.Barra,
                        fontWeight = FontWeight.Bold,
                        color = NavyTitulo,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Agenda una consulta con nuestros especialistas cuando lo necesites.",
                        fontSize = Tam.Cuerpo,
                        color = SlateTexto,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    BotonPrimario(
                        texto = "Agendar una cita",
                        onClick = alIrAAgendar
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (citas.size == 1) "Tienes 1 cita agendada" else "Tienes ${citas.size} citas agendadas",
                    fontSize = Tam.Cuerpo,
                    color = SlateTexto,
                    modifier = Modifier.padding(start = 4.dp, bottom = 12.dp, top = 2.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(citas, key = { it.id }) { cita ->
                        TarjetaItemCita(
                            cita = cita,
                            alCancelar = { citaACancelar = cita }
                        )
                    }
                }
            }
        }
    }

    DialogoConfirmacion(
        mostrar = citaACancelar != null,
        titulo = "¿Cancelar cita médica?",
        mensaje = "¿Estás seguro de que deseas cancelar esta cita? El horario volverá a quedar disponible.",
        onConfirmar = {
            citaACancelar?.let { Repositorio.cancelarCita(it.id) }
            citas = Repositorio.citasDelUsuario()
            citaACancelar = null
        },
        onDescartar = { citaACancelar = null }
    )
}

// Una cita ya pasó si su fecha y hora son anteriores al momento actual
private fun citaYaPaso(cita: Cita): Boolean {
    val fecha = FechaUtils.parsearFecha(cita.fecha) ?: return false
    val hora = try { LocalTime.parse(cita.hora) } catch (e: Exception) { LocalTime.MIN }
    return LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now())
}

@Composable
fun TarjetaItemCita(
    cita: Cita,
    alCancelar: () -> Unit
) {
    val medico = remember(cita.medicoId) { Repositorio.obtenerMedico(cita.medicoId) }
    val especialidad = remember(cita.especialidadId) { Repositorio.obtenerEspecialidad(cita.especialidadId) }
    val sede = remember(medico) { medico?.let { Repositorio.obtenerSede(it.sedeId) } }
    val pasada = remember(cita.id) { citaYaPaso(cita) }
    val fecha = remember(cita.fecha) { FechaUtils.parsearFecha(cita.fecha) }

    val fechaLegible = fecha?.let { FechaUtils.formatearFechaCompleta(it) } ?: cita.fecha
    val mesCorto = fecha?.month?.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-PE"))
        ?.replace(".", "")?.uppercase().orEmpty()
    val diaNumero = fecha?.dayOfMonth?.toString().orEmpty()
    val rangoHora = remember(cita.hora) {
        try {
            "${cita.hora} a ${LocalTime.parse(cita.hora).plusMinutes(Repositorio.DURACION_CONSULTA_MIN)} hrs"
        } catch (e: Exception) {
            "${cita.hora} hrs"
        }
    }

    val textoEstado = if (pasada) "Realizada" else cita.estado
    val colorEstado = if (pasada) SlateTexto else VerdeDisponible
    val fondoEstado = if (pasada) SuperficieSuave else VerdeDisponibleFondo

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Blanco,
        shadowElevation = 3.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Médico y estado
            Row(verticalAlignment = Alignment.CenterVertically) {
                FotoMedico(medico = medico, tamano = 62.dp)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medico?.nombre ?: "Médico",
                        fontWeight = FontWeight.Bold,
                        fontSize = Tam.Subtitulo,
                        color = NavyTitulo
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = especialidad?.nombre ?: "",
                        fontSize = Tam.Cuerpo,
                        color = SlateTexto
                    )
                    Text(
                        text = "Sede ${sede?.nombre.orEmpty()}",
                        fontSize = Tam.Cuerpo,
                        color = SlateTexto
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(50), color = fondoEstado) {
                        Text(
                            text = textoEstado,
                            color = colorEstado,
                            fontSize = Tam.Pequeno,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fecha y hora destacadas
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = SuperficieSuave
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = if (pasada) SlateTexto else VerdePrimario
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = mesCorto,
                                fontSize = Tam.Pequeno,
                                fontWeight = FontWeight.SemiBold,
                                color = Blanco.copy(alpha = 0.85f)
                            )
                            Text(
                                text = diaNumero,
                                fontSize = Tam.Titulo,
                                fontWeight = FontWeight.ExtraBold,
                                color = Blanco
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = fechaLegible,
                            fontSize = Tam.Cuerpo,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyTitulo
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = VerdePrimario,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = rangoHora, fontSize = Tam.Cuerpo, color = SlateTexto)
                        }
                        if (cita.motivo.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = VerdePrimario,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = cita.motivo,
                                    fontSize = Tam.Cuerpo,
                                    color = SlateTexto,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            if (!pasada) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = alCancelar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoAlerta),
                    border = BorderStroke(1.dp, RojoAlerta.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Cancelar cita", fontSize = Tam.Cuerpo, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
