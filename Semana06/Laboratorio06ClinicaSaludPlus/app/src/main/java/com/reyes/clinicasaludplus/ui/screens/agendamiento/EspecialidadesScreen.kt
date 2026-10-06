package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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
import com.reyes.clinicasaludplus.R
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun EspecialidadesScreen(
    alSeleccionarEspecialidad: (String) -> Unit,
    alVolver: () -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    val especialidadesFiltradas = remember(busqueda) {
        Repositorio.buscarEspecialidades(busqueda)
    }

    Scaffold(
        containerColor = Color(0xFFFBFBFD),
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Especialidades",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Buscador redondeado
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar especialidad...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF94A3B8)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                singleLine = true
            )

            // Lista LazyColumn con los iconos cargados desde drawable
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(especialidadesFiltradas) { esp ->
                    TarjetaEspecialidadConImagen(
                        especialidad = esp,
                        drawableRes = obtenerDrawablePorId(esp.id),
                        fondoColor = obtenerColorPastelFondo(esp.id),
                        onClick = { alSeleccionarEspecialidad(esp.id) }
                    )
                }
            }
        }
    }
}

// Helpers para asignar el recurso y su fondo pastel característico
fun obtenerDrawablePorId(id: String): Int {
    return when (id) {
        "esp1" -> R.drawable.ic_esp_general
        "esp2" -> R.drawable.ic_esp_pediatria
        "esp3" -> R.drawable.ic_esp_ginecologia
        "esp4" -> R.drawable.ic_esp_cardiologia
        "esp5" -> R.drawable.ic_esp_dermatologia
        "esp6" -> R.drawable.ic_esp_traumatologia
        "esp7" -> R.drawable.ic_esp_oftalmologia
        else -> R.drawable.ic_esp_general
    }
}

fun obtenerColorPastelFondo(id: String): Color {
    return when (id) {
        "esp1" -> Color(0xFFEFF6FF)
        "esp2" -> Color(0xFFFEF3C7)
        "esp3" -> Color(0xFFFCE7F3)
        "esp4" -> Color(0xFFFEE2E2)
        "esp5" -> Color(0xFFFEF3C7)
        "esp6" -> Color(0xFFE0F2FE)
        "esp7" -> Color(0xFFDBEAFE)
        else -> Color(0xFFEFF6FF)
    }
}

@Composable
fun TarjetaEspecialidadConImagen(
    especialidad: Especialidad,
    drawableRes: Int,
    fondoColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Blanco,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fondo circular con la imagen PNG centrada
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(fondoColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = especialidad.nombre,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = especialidad.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = especialidad.descripcion,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Seleccionar",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}