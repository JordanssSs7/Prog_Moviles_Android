package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponible
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponibleFondo

@Composable
fun MedicosScreen(
    especialidadId: String,
    alSeleccionarMedico: (String) -> Unit,
    alVolver: () -> Unit
) {
    val especialidad = remember(especialidadId) {
        Repositorio.obtenerEspecialidad(especialidadId)
    }
    var buscando by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    val todos = remember(especialidadId) { Repositorio.medicosPorEspecialidad(especialidadId) }
    val medicos = remember(especialidadId, busqueda) { Repositorio.buscarMedicos(especialidadId, busqueda) }

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Médicos de ${especialidad?.nombre ?: "Especialidad"}",
                alVolver = alVolver,
                acciones = {
                    if (todos.isNotEmpty()) {
                        IconButton(onClick = {
                            buscando = !buscando
                            if (!buscando) busqueda = ""
                        }) {
                            Icon(
                                imageVector = if (buscando) Icons.Default.Clear else Icons.Default.Search,
                                contentDescription = if (buscando) "Cerrar búsqueda" else "Buscar",
                                tint = NavyTitulo
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (buscando) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { if (it.length <= 40) busqueda = it },
                    placeholder = { Text("Buscar médico...", color = SlateTexto, fontSize = 16.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF1F5FB),
                        unfocusedContainerColor = Color(0xFFF1F5FB),
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            HorizontalDivider(color = BordeSuave)

            if (medicos.isEmpty()) {
                val sinMedicos = todos.isEmpty()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = AzulPrimario,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (sinMedicos) "Aún no hay médicos disponibles" else "Sin resultados",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyTitulo
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (sinMedicos) {
                                "Pronto habilitaremos especialistas para ${especialidad?.nombre ?: "esta especialidad"}."
                            } else {
                                "No hay médicos que coincidan con \"${busqueda.trim()}\"."
                            },
                            fontSize = 14.sp,
                            color = SlateTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        TarjetaMedicoFiel(
                            medico = medico,
                            onClick = { alSeleccionarMedico(medico.id) }
                        )
                        HorizontalDivider(color = BordeSuave)
                    }
                }
            }
        }
    }
}

/** Fila del médico: foto, nombre, profesión, calificación, flecha y etiqueta de disponibilidad. */
@Composable
fun TarjetaMedicoFiel(
    medico: Medico,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FotoMedico(medico = medico, tamano = 84.dp)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyTitulo
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = medico.profesion,
                    fontSize = 16.sp,
                    color = SlateTexto
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${medico.calificacion} (${medico.resenas})",
                        fontSize = 16.sp,
                        color = SlateTexto
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = SlateTexto,
                modifier = Modifier.size(26.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = VerdeDisponibleFondo
            ) {
                Text(
                    text = medico.disponibilidad,
                    color = VerdeDisponible,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
