package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.theme.AzulClaro
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.FondoGris
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro

@Composable
fun MedicosScreen(
    especialidadId: String,
    alSeleccionarMedico: (String) -> Unit,
    alVolver: () -> Unit
) {
    val especialidad = remember(especialidadId) {
        Repositorio.obtenerEspecialidad(especialidadId)
    }
    val medicos = remember(especialidadId) {
        Repositorio.medicosPorEspecialidad(especialidadId)
    }

    Scaffold(
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Médicos de ${especialidad?.nombre ?: "Especialidad"}",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FondoGris)
                .padding(16.dp)
        ) {
            if (medicos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay médicos disponibles para esta especialidad.",
                        color = TextoGris,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos) { medico ->
                        TarjetaMedico(
                            medico = medico,
                            onAgendarClick = { alSeleccionarMedico(medico.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaMedico(
    medico: Medico,
    onAgendarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(AzulClaro),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
                Text(
                    text = medico.cmp,
                    fontSize = 12.sp,
                    color = TextoGris
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${medico.calificacion}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoOscuro
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "S/ ${medico.precioConsulta}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                }
            }

            Button(
                onClick = onAgendarClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Seleccionar", fontSize = 12.sp)
            }
        }
    }
}