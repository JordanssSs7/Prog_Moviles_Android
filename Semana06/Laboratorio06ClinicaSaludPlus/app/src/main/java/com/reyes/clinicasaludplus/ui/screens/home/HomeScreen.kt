package com.reyes.clinicasaludplus.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.DialogoInformativo
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate

private val ColorInactivoNav = Color(0xFF6B7A99)

@Composable
fun HomeScreen(
    alIrAAgendar: () -> Unit,
    alIrAMisCitas: () -> Unit,
    alIrAPerfil: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val destacadas = remember { Repositorio.especialidadesDestacadas() }
    var menuAbierto by remember { mutableStateOf(false) }
    var mostrarNotificaciones by rememberSaveable { mutableStateOf(false) }
    var mostrarResultados by rememberSaveable { mutableStateOf(false) }

    val primerNombre = usuario?.nombreCompleto?.trim()?.split(" ")?.firstOrNull().orEmpty()

    Scaffold(
        containerColor = Blanco,
        bottomBar = {
            Column {
                HorizontalDivider(color = BordeSuave)
                NavigationBar(
                    containerColor = Blanco,
                    tonalElevation = 0.dp
                ) {
                    ItemBarraInferior("Inicio", Icons.Default.Home, Icons.Default.Home, seleccionado = true) { }
                    ItemBarraInferior("Citas", Icons.Outlined.CalendarMonth, Icons.Default.CalendarMonth, false, alIrAMisCitas)
                    ItemBarraInferior("Resultados", Icons.Outlined.Description, Icons.Default.Description, false) {
                        mostrarResultados = true
                    }
                    ItemBarraInferior("Perfil", Icons.Outlined.Person, Icons.Default.Person, false, alIrAPerfil)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Fila superior: menú y campana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = NavyTitulo,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        DropdownMenuItem(
                            text = { Text("Agendar cita") },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                            onClick = { menuAbierto = false; alIrAAgendar() }
                        )
                        DropdownMenuItem(
                            text = { Text("Mis citas") },
                            leadingIcon = { Icon(Icons.Default.Event, contentDescription = null) },
                            onClick = { menuAbierto = false; alIrAMisCitas() }
                        )
                        DropdownMenuItem(
                            text = { Text("Mis datos") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            onClick = { menuAbierto = false; alIrAPerfil() }
                        )
                    }
                }
                IconButton(onClick = { mostrarNotificaciones = true }) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = NavyTitulo,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = if (primerNombre.isNotEmpty()) "¡Hola, $primerNombre!" else "¡Hola!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyTitulo,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "¿Qué deseas hacer hoy?",
                fontSize = 18.sp,
                color = SlateTexto,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TarjetaAccionAmplia(
                    titulo = "Agendar cita",
                    icono = Icons.Default.CalendarMonth,
                    colorFondo = Color(0xFFE3F0FF),
                    colorAcento = Color(0xFF1366F0),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAAgendar
                )
                TarjetaAccionAmplia(
                    titulo = "Mis citas",
                    icono = Icons.Default.Event,
                    colorFondo = Color(0xFFDDF7E8),
                    colorAcento = Color(0xFF16A34A),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAMisCitas
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TarjetaAccionAmplia(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    colorFondo = Color(0xFFEEE6FF),
                    colorAcento = Color(0xFF8B3DF0),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAPerfil
                )
                TarjetaAccionAmplia(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    colorFondo = Color(0xFFFFEBD6),
                    colorAcento = Color(0xFFF2830F),
                    modifier = Modifier.weight(1f),
                    onClick = { mostrarResultados = true }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyTitulo
                )
                Text(
                    text = "Ver todas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = AzulPrimario,
                    modifier = Modifier.clickable { alIrAAgendar() }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tres tarjetas visibles a todo el ancho; la cuarta se alcanza deslizando
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(destacadas, key = { it.id }) { esp ->
                    TarjetaEspecialidadDestacada(
                        especialidad = esp,
                        modifier = Modifier.fillParentMaxWidth(0.31f),
                        onClick = alIrAAgendar
                    )
                }
            }

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

// Arma el texto de notificaciones a partir de las citas del usuario
private fun textoNotificaciones(): String {
    val proxima = Repositorio.citasDelUsuario().firstOrNull { cita ->
        val fecha = FechaUtils.parsearFecha(cita.fecha)
        fecha != null && !fecha.isBefore(LocalDate.now())
    } ?: return "No tienes notificaciones nuevas."
    val medico = Repositorio.obtenerMedico(proxima.medicoId)?.nombre ?: "tu médico"
    val fecha = FechaUtils.parsearFecha(proxima.fecha)?.let { FechaUtils.formatearFechaCompleta(it) } ?: proxima.fecha
    return "Recordatorio: tienes una cita con $medico el $fecha a las ${proxima.hora}."
}

@Composable
private fun RowScope.ItemBarraInferior(
    etiqueta: String,
    icono: ImageVector,
    iconoSeleccionado: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = seleccionado,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (seleccionado) iconoSeleccionado else icono,
                contentDescription = etiqueta,
                modifier = Modifier.size(28.dp)
            )
        },
        label = { Text(etiqueta, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AzulPrimario,
            selectedTextColor = AzulPrimario,
            unselectedIconColor = ColorInactivoNav,
            unselectedTextColor = ColorInactivoNav,
            indicatorColor = Color.Transparent
        )
    )
}

@Composable
fun TarjetaAccionAmplia(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorAcento: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(132.dp)
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
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = colorAcento,
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorAcento,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun fondoCirculoEspecialidad(id: String): Color = when (id) {
    "esp1" -> Color(0xFFDFF3FB)
    "esp2" -> Color(0xFFFFEFDC)
    "esp3" -> Color(0xFFFDE3EC)
    "esp4" -> Color(0xFFFDE2E6)
    else -> Color(0xFFDFF3FB)
}

@Composable
fun TarjetaEspecialidadDestacada(
    especialidad: Especialidad,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(136.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = Blanco,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = Repositorio.obtenerIconoDrawable(especialidad.id)),
                contentDescription = especialidad.nombre,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = especialidad.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = NavyTitulo,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                maxLines = 2
            )
        }
    }
}
