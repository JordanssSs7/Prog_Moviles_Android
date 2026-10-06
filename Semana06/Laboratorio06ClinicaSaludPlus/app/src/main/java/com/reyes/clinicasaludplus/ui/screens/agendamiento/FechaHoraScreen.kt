package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
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
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*
import com.reyes.clinicasaludplus.util.FechaUtils

@Composable
fun FechaHoraScreen(
    medicoId: String,
    alContinuar: (String, String) -> Unit,
    alVolver: () -> Unit
) {
    val medico = remember(medicoId) { Repositorio.obtenerMedico(medicoId) }
    val especialidad = remember(medico) {
        medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    }

    var semanaOffset by remember { mutableStateOf(0L) }
    val diasHabiles = remember(semanaOffset) { FechaUtils.obtenerDiasHabiles(semanaOffset) }
    var fechaSeleccionada by remember(semanaOffset) { mutableStateOf(diasHabiles.first().fecha) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val horariosDisponibles = remember(medicoId, fechaSeleccionada) {
        Repositorio.horariosDisponibles(medicoId, fechaSeleccionada.toString())
    }

    Scaffold(
        containerColor = Color(0xFFFBFBFD),
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Seleccionar fecha y hora",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // --- 1. TARJETA DEL MÉDICO CON FOTO CIRCULAR ---
                medico?.let {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Blanco,
                        shadowElevation = 1.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (it.fotoRes != 0) {
                                Image(
                                    painter = painterResource(id = it.fotoRes),
                                    contentDescription = it.nombre,
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEFF6FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = AzulPrimario,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = it.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = especialidad?.nombre ?: it.cmp,
                                    fontSize = 13.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // --- 2. SELECTOR DE MES CON FLECHAS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (semanaOffset > 0) {
                                semanaOffset--
                                horaSeleccionada = null
                            }
                        },
                        enabled = semanaOffset > 0
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Semana anterior",
                            tint = if (semanaOffset > 0) Color(0xFF1E293B) else Color(0xFFCBD5E1)
                        )
                    }

                    Text(
                        text = FechaUtils.obtenerEncabezadoMes(fechaSeleccionada),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E293B)
                    )

                    IconButton(
                        onClick = {
                            semanaOffset++
                            horaSeleccionada = null
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Semana siguiente",
                            tint = Color(0xFF1E293B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- 3. CÁPSULAS DE DÍAS HÁBILES ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    diasHabiles.forEach { dia ->
                        val esSeleccionado = dia.fecha == fechaSeleccionada
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .clickable {
                                    fechaSeleccionada = dia.fecha
                                    horaSeleccionada = null
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (esSeleccionado) AzulPrimario else Blanco,
                            shadowElevation = if (esSeleccionado) 3.dp else 1.dp,
                            border = if (!esSeleccionado) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)) else null
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = dia.nombreDia,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (esSeleccionado) Blanco else Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dia.numeroDia,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (esSeleccionado) Blanco else Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // --- 4. GRILLA DE HORARIOS AMPLIOS (3x3) ---
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(horariosDisponibles) { hora ->
                        val estaSeleccionada = horaSeleccionada == hora
                        Surface(
                            modifier = Modifier
                                .height(52.dp)
                                .clickable { horaSeleccionada = hora },
                            shape = RoundedCornerShape(14.dp),
                            color = if (estaSeleccionada) AzulPrimario else Blanco,
                            shadowElevation = if (estaSeleccionada) 2.dp else 1.dp,
                            border = if (!estaSeleccionada) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = hora,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (estaSeleccionada) Blanco else Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }
            }

            // --- 5. BOTÓN CONTINUAR INFERIOR ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                BotonPrimario(
                    texto = "Continuar",
                    habilitado = horaSeleccionada != null,
                    onClick = {
                        horaSeleccionada?.let { hora ->
                            alContinuar(fechaSeleccionada.toString(), hora)
                        }
                    }
                )
            }
        }
    }
}