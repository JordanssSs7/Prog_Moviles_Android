package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto

@Composable
fun EspecialidadesScreen(
    alSeleccionarEspecialidad: (String) -> Unit,
    alVolver: () -> Unit
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val especialidadesFiltradas = remember(busqueda) {
        Repositorio.buscarEspecialidades(busqueda)
    }

    Scaffold(
        containerColor = Blanco,
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
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { if (it.length <= 40) busqueda = it },
                placeholder = { Text("Buscar especialidad...", color = SlateTexto, fontSize = 18.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = SlateTexto
                    )
                },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { busqueda = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar búsqueda",
                                tint = SlateTexto
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5FB),
                    unfocusedContainerColor = Color(0xFFF1F5FB),
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = BordeSuave)

            if (especialidadesFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sin resultados",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyTitulo
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No encontramos especialidades para \"${busqueda.trim()}\".",
                            fontSize = 14.sp,
                            color = SlateTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(especialidadesFiltradas, key = { it.id }) { esp ->
                        TarjetaEspecialidadConImagen(
                            especialidad = esp,
                            drawableRes = obtenerDrawablePorId(esp.id),
                            fondoColor = obtenerColorPastelFondo(esp.id),
                            onClick = { alSeleccionarEspecialidad(esp.id) }
                        )
                        HorizontalDivider(color = BordeSuave)
                    }
                }
            }
        }
    }
}

// Helpers para asignar el recurso y su fondo pastel característico
fun obtenerDrawablePorId(id: String): Int = Repositorio.obtenerIconoDrawable(id)

fun obtenerColorPastelFondo(id: String): Color {
    return when (id) {
        "esp1" -> Color(0xFFDCEBFF)
        "esp2" -> Color(0xFFFFEBD2)
        "esp3" -> Color(0xFFFCE3F3)
        "esp4" -> Color(0xFFFDE2E6)
        "esp5" -> Color(0xFFFFEBD2)
        "esp6" -> Color(0xFFDDEFFE)
        "esp7" -> Color(0xFFDCEBFF)
        else -> Color(0xFFDCEBFF)
    }
}

/** Fila de especialidad: ícono en cuadro pastel, nombre, descripción y flecha (sin tarjeta, con divisor). */
@Composable
fun TarjetaEspecialidadConImagen(
    especialidad: Especialidad,
    drawableRes: Int,
    fondoColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen tal cual del diseño (incluye su fondo de color)
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = especialidad.nombre,
            modifier = Modifier
                .size(66.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = especialidad.nombre,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = especialidad.descripcion,
                fontSize = 16.sp,
                color = SlateTexto
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Seleccionar",
            tint = SlateTexto,
            modifier = Modifier.size(26.dp)
        )
    }
}
