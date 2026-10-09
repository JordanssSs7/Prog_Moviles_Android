package com.reyes.clinicasaludplus.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.DestinoMenu
import com.reyes.clinicasaludplus.ui.components.DialogoInformativo
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.components.PantallaConMenu
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.ui.theme.Tam
import com.reyes.clinicasaludplus.ui.theme.VerdeClaro
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private val consejos = listOf(
    "Bebe al menos 8 vasos de agua al día para mantenerte hidratado.",
    "Dormir de 7 a 8 horas mejora tu defensa contra las enfermedades.",
    "Caminar 30 minutos al día cuida tu corazón y mejora tu ánimo.",
    "Lávate las manos con frecuencia: es la forma más simple de prevenir contagios.",
    "Incluye frutas y verduras en cada comida para fortalecer tu cuerpo.",
    "Un chequeo médico a tiempo puede evitar problemas mayores. ¡No lo postergues!",
    "Haz pausas activas si trabajas sentado: estira cuello, espalda y piernas."
)

@Composable
fun HomeScreen(
    alIrAPerfil: () -> Unit,
    alIrAMedico: (String) -> Unit,
    alNavegar: (DestinoMenu) -> Unit,
    alCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val proxima = proximaCita()
    val mejoresDoctores = remember { Repositorio.buscarMedicos().take(5) }
    var mostrarNotificaciones by rememberSaveable { mutableStateOf(false) }
    var mostrarResultados by rememberSaveable { mutableStateOf(false) }

    val primerNombre = usuario?.nombreCompleto?.trim()?.split(" ")?.firstOrNull().orEmpty()
    val hoy = LocalDate.now()

    PantallaConMenu(
        titulo = "Clínica SaludPlus",
        seleccionado = null,
        alNavegar = alNavegar,
        alIrAInicio = {},
        alCerrarSesion = alCerrarSesion,
        acciones = {
            IconButton(onClick = { mostrarNotificaciones = true }) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "Notificaciones",
                    tint = NavyTitulo,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = if (primerNombre.isNotEmpty()) "¡Hola, $primerNombre!" else "¡Hola!",
                fontSize = Tam.Titulo,
                fontWeight = FontWeight.ExtraBold,
                color = NavyTitulo,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = FechaUtils.formatearFechaCompleta(hoy),
                fontSize = Tam.Cuerpo,
                color = SlateTexto,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (proxima != null) {
                TarjetaProximaCita(proxima, onVerAgenda = { alNavegar(DestinoMenu.Agenda) })
            } else {
                TarjetaSinCitas(onVerSedes = { alNavegar(DestinoMenu.Sede) })
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TarjetaAccion(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    colorFondo = VerdeClaro,
                    modifier = Modifier.weight(1f),
                    onClick = alIrAPerfil
                )
                TarjetaAccion(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    colorFondo = SuperficieSuave,
                    modifier = Modifier.weight(1f),
                    onClick = { mostrarResultados = true }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Doctores mejor calificados",
                fontSize = Tam.Subtitulo,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(mejoresDoctores, key = { it.id }) { medico ->
                    TarjetaDoctorDestacado(medico = medico, onClick = { alIrAMedico(medico.id) })
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            TarjetaConsejo(consejos[hoy.dayOfYear % consejos.size])

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    DialogoInformativo(
        mostrar = mostrarResultados,
        titulo = "Resultados",
        mensaje = "Aún no tienes resultados médicos disponibles. Aparecerán aquí cuando tu clínica los publique.",
        onCerrar = { mostrarResultados = false }
    )

    DialogoInformativo(
        mostrar = mostrarNotificaciones,
        titulo = "Notificaciones",
        mensaje = textoNotificaciones(),
        onCerrar = { mostrarNotificaciones = false }
    )
}

// Primera cita del usuario que todavía no ha pasado
private fun proximaCita(): Cita? = Repositorio.citasDelUsuario().firstOrNull { cita ->
    val fecha = FechaUtils.parsearFecha(cita.fecha) ?: return@firstOrNull false
    val hora = try { LocalTime.parse(cita.hora) } catch (e: Exception) { return@firstOrNull false }
    !LocalDateTime.of(fecha, hora).isBefore(LocalDateTime.now())
}

// Arma el texto de notificaciones a partir de la próxima cita
private fun textoNotificaciones(): String {
    val proxima = proximaCita() ?: return "No tienes notificaciones nuevas."
    val medico = Repositorio.obtenerMedico(proxima.medicoId)?.nombre ?: "tu médico"
    val fecha = FechaUtils.parsearFecha(proxima.fecha)?.let { FechaUtils.formatearFechaCompleta(it) } ?: proxima.fecha
    return "Recordatorio: tienes una cita con $medico el $fecha a las ${proxima.hora}."
}

@Composable
private fun TarjetaProximaCita(cita: Cita, onVerAgenda: () -> Unit) {
    val medico = remember(cita.medicoId) { Repositorio.obtenerMedico(cita.medicoId) }
    val especialidad = remember(cita.especialidadId) { Repositorio.obtenerEspecialidad(cita.especialidadId)?.nombre.orEmpty() }
    val sede = remember(medico) { medico?.let { Repositorio.obtenerSede(it.sedeId)?.nombre }.orEmpty() }
    val fecha = FechaUtils.parsearFecha(cita.fecha)?.let { FechaUtils.formatearFechaCompleta(it) } ?: cita.fecha

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onVerAgenda() },
        shape = RoundedCornerShape(22.dp),
        color = VerdePrimario
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "TU PRÓXIMA CITA",
                fontSize = Tam.Pequeno,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp,
                color = VerdeClaro
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                FotoMedico(medico = medico, tamano = 64.dp)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = medico?.nombre ?: "Médico",
                        fontSize = Tam.Subtitulo,
                        fontWeight = FontWeight.Bold,
                        color = Blanco
                    )
                    Text(text = "$especialidad · Sede $sede", fontSize = Tam.Cuerpo, color = VerdeClaro)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            DatoCita(Icons.Outlined.CalendarMonth, fecha)
            Spacer(modifier = Modifier.height(4.dp))
            DatoCita(Icons.Outlined.Schedule, "${cita.hora} hrs")
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Ver mi agenda",
                fontSize = Tam.Cuerpo,
                fontWeight = FontWeight.SemiBold,
                color = Blanco
            )
        }
    }
}

@Composable
private fun DatoCita(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icono, contentDescription = null, tint = VerdeClaro, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto, fontSize = Tam.Cuerpo, color = Blanco)
    }
}

@Composable
private fun TarjetaSinCitas(onVerSedes: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = SuperficieSuave
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Aún no tienes citas",
                fontSize = Tam.Subtitulo,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Elige una sede y reserva con el especialista que necesitas.",
                fontSize = Tam.Cuerpo,
                color = SlateTexto
            )
            Spacer(modifier = Modifier.height(14.dp))
            BotonPrimario(texto = "Ver sedes", onClick = onVerSedes)
        }
    }
}

@Composable
private fun TarjetaAccion(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        color = colorFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icono, contentDescription = titulo, tint = VerdePrimario, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                fontSize = Tam.Cuerpo,
                fontWeight = FontWeight.Medium,
                color = VerdePrimario,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TarjetaDoctorDestacado(medico: Medico, onClick: () -> Unit) {
    val sede = remember(medico.sedeId) { Repositorio.obtenerSede(medico.sedeId)?.nombre.orEmpty() }

    Surface(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Blanco,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FotoMedico(medico = medico, tamano = 76.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = medico.nombre,
                fontSize = Tam.Cuerpo,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = medico.profesion, fontSize = Tam.Pequeno, color = SlateTexto, maxLines = 1)
            Text(text = "Sede $sede", fontSize = Tam.Pequeno, color = SlateTexto, maxLines = 1)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${medico.calificacion} (${medico.resenas})", fontSize = Tam.Pequeno, color = SlateTexto)
            }
        }
    }
}

@Composable
private fun TarjetaConsejo(texto: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = SuperficieSuave
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(16.dp), color = VerdeClaro, modifier = Modifier.size(52.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = VerdePrimario,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Consejo de salud del día",
                    fontSize = Tam.Cuerpo,
                    fontWeight = FontWeight.Bold,
                    color = NavyTitulo
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = texto, fontSize = Tam.Pequeno, color = SlateTexto)
            }
        }
    }
}
