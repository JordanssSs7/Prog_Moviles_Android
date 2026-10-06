package com.reyes.clinicasaludplus.ui.screens.home

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
        bottomBar = {
            NavigationBar(
                containerColor = Blanco,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = destinoSeleccionado == 0,
                    onClick = { destinoSeleccionado = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        indicatorColor = AzulClaro
                    )
                )
                NavigationBarItem(
                    selected = destinoSeleccionado == 1,
                    onClick = {
                        destinoSeleccionado = 1
                        alIrAMisCitas()
                    },
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Citas") },
                    label = { Text("Mis citas") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
                        indicatorColor = AzulClaro
                    )
                )
                NavigationBarItem(
                    selected = destinoSeleccionado == 2,
                    onClick = { destinoSeleccionado = 2 },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Resultados") },
                    label = { Text("Resultados") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
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
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AzulPrimario,
                        selectedTextColor = AzulPrimario,
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
                .background(FondoGris)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Encabezado de bienvenida
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, ${usuario?.nombreCompleto?.split(" ")?.firstOrNull() ?: "Paciente"}!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoOscuro
                    )
                    Text(
                        text = "¿Qué deseas realizar hoy?",
                        fontSize = 14.sp,
                        color = TextoGris
                    )
                }
                IconButton(onClick = { /* Notificaciones */ }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones",
                        tint = TextoOscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de Acciones Rápidas (Grid 2x2 simulado)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaAccion(
                    titulo = "Agendar cita",
                    icono = Icons.Default.AddCircleOutline,
                    colorFondo = Color(0xFFE8F1FF),
                    colorIcono = AzulPrimario,
                    modifier = Modifier.weight(1f),
                    onClick = alIrAAgendar
                )
                TarjetaAccion(
                    titulo = "Mis citas",
                    icono = Icons.Default.DateRange,
                    colorFondo = Color(0xFFE6F7F0),
                    colorIcono = VerdeExito,
                    modifier = Modifier.weight(1f),
                    onClick = alIrAMisCitas
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaAccion(
                    titulo = "Mi salud",
                    icono = Icons.Default.FavoriteBorder,
                    colorFondo = Color(0xFFF3E8FF),
                    colorIcono = Color(0xFF9333EA),
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                TarjetaAccion(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    colorFondo = Color(0xFFFFF3E0),
                    colorIcono = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Sección Especialidades Destacadas con LazyRow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
                TextButton(onClick = alIrAAgendar) {
                    Text("Ver todas", color = AzulPrimario, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(destacadas) { esp ->
                    ItemEspecialidadDestacada(
                        especialidad = esp,
                        onClick = alIrAAgendar
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaAccion(
    titulo: String,
    icono: ImageVector,
    colorFondo: Color,
    colorIcono: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorFondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = titulo,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoOscuro
            )
        }
    }
}

@Composable
fun ItemEspecialidadDestacada(
    especialidad: Especialidad,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(85.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(AzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = especialidad.nombre,
                tint = AzulPrimario,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = especialidad.nombre,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextoOscuro,
            maxLines = 1
        )
    }
}