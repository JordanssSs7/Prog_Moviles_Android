package com.reyes.clinicasaludplus.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun HomeScreen(
    alIrAAgendar: () -> Unit,
    alIrAMisCitas: () -> Unit,
    alIrAPerfil: () -> Unit
) {
    var destinoSeleccionado by remember { mutableStateOf(0) }
    val usuario = Repositorio.usuarioActual
    val destacadas = remember { Repositorio.especialidadesDestacadas() }

    Scaffold(
        containerColor = Color(0xFFFBFBFD),
        bottomBar = {
            NavigationBar(
                containerColor = Blanco,
                tonalElevation = 6.dp,
                modifier = Modifier.height(68.dp)
            ) {
                NavigationBarItem(
                    selected = destinoSeleccionado == 0,
                    onClick = { destinoSeleccionado = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        unselectedIconColor = TextoGris,
                        unselectedTextColor = TextoGris,
                        indicatorColor = AzulClaro
                    )
                )
                NavigationBarItem(
                    selected = destinoSeleccionado == 1,
                    onClick = {
                        destinoSeleccionado = 1
                        alIrAMisCitas()
                    },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Citas") },
                    label = { Text("Citas", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        unselectedIconColor = TextoGris,
                        unselectedTextColor = TextoGris,
                        indicatorColor = AzulClaro
                    )
                )
                NavigationBarItem(
                    selected = destinoSeleccionado == 2,
                    onClick = { destinoSeleccionado = 2 },
                    icon = { Icon(Icons.AutoMirrored.Filled.InsertDriveFile, contentDescription = "Resultados") },
                    label = { Text("Resultados", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        unselectedIconColor = TextoGris,
                        unselectedTextColor = TextoGris,
                        indicatorColor = AzulClaro
                    )
                )
                NavigationBarItem(
                    selected = destinoSeleccionado == 3,
                    onClick = {
                        destinoSeleccionado = 3
                        alIrAPerfil()
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        unselectedIconColor = TextoGris,
                        unselectedTextColor = TextoGris,
                        indicatorColor = AzulClaro
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Fila superior: menú y campana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = alIrAPerfil) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Menú",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { /* Notificaciones */ }) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Saludo de usuario
            Text(
                text = "¡Hola, ${usuario?.nombreCompleto?.split(" ")?.firstOrNull() ?: "Juan"}!",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "¿Qué deseas hacer hoy?",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- CUADRÍCULA DE TARJETAS AMPLIAS (2x2) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaAccionAmplia(
                    titulo = "Agendar cita",
                    icono = Icons.Default.EventAvailable,
                    colorFondo = Color(0xFFEFF5FF),
                    colorAcento = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAAgendar
                )
                TarjetaAccionAmplia(
                    titulo = "Mis citas",
                    icono = Icons.Default.CalendarToday,
                    colorFondo = Color(0xFFECFDF5),
                    colorAcento = Color(0xFF10B981),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAMisCitas
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaAccionAmplia(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    colorFondo = Color(0xFFF5F3FF),
                    colorAcento = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    onClick = alIrAPerfil
                )
                TarjetaAccionAmplia(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    colorFondo = Color(0xFFFFF7ED),
                    colorAcento = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Encabezado Especialidades destacadas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Ver todas",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulPrimario,
                    modifier = Modifier.clickable { alIrAAgendar() }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fila de tarjetas para especialidades destacadas
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(destacadas) { esp ->
                    TarjetaEspecialidadDestacada(
                        especialidad = esp,
                        onClick = alIrAAgendar
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
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
            .height(138.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colorFondo,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Blanco.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = colorAcento,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorAcento,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TarjetaEspecialidadDestacada(
    especialidad: Especialidad,
    onClick: () -> Unit
) {
    // Obtenemos el drawable asignado
    val drawableRes = when (especialidad.id) {
        "esp1" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_general
        "esp2" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_pediatria
        "esp3" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_ginecologia
        "esp4" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_cardiologia
        else -> com.reyes.clinicasaludplus.R.drawable.ic_esp_general
    }

    Surface(
        modifier = Modifier
            .width(105.dp)
            .height(118.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Blanco,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = especialidad.nombre,
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = especialidad.nombre,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 13.sp,
                maxLines = 2
            )
        }
    }
}