package com.reyes.clinicasaludplus.ui.screens.agendamiento

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    medicoId: String,
    alContinuar: (String, String) -> Unit,
    alVolver: () -> Unit
) {
    val medico = remember(medicoId) { Repositorio.obtenerMedico(medicoId) }

    // Estados dinámicos de semana y fecha
    var semanaOffset by remember { mutableStateOf(0L) }
    val diasHabiles = remember(semanaOffset) { FechaUtils.obtenerDiasHabiles(semanaOffset) }
    var fechaSeleccionada by remember(semanaOffset) { mutableStateOf(diasHabiles.first().fecha) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    // Recálculo automático de horarios disponibles en memoria
    val horariosDisponibles = remember(medicoId, fechaSeleccionada) {
        Repositorio.horariosDisponibles(medicoId, fechaSeleccionada.toString())
    }

    Scaffold(
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Seleccionar fecha y hora",
                alVolver = alVolver
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Blanco
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FondoGris)
                .padding(16.dp)
        ) {
            // Médico seleccionado
            medico?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blanco, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AzulClaro),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AzulPrimario)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(it.nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextoOscuro)
                        Text(it.cmp, fontSize = 12.sp, color = TextoGris)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Selector dinámico de mes con flechas
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
                        tint = if (semanaOffset > 0) TextoOscuro else TextoGris.copy(alpha = 0.4f)
                    )
                }

                Text(
                    text = FechaUtils.obtenerEncabezadoMes(fechaSeleccionada),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextoOscuro
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
                        tint = TextoOscuro
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fila de días hábiles dinámicos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                diasHabiles.forEach { dia ->
                    val esSeleccionado = dia.fecha == fechaSeleccionada
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (esSeleccionado) AzulPrimario else Blanco)
                            .clickable {
                                fechaSeleccionada = dia.fecha
                                horaSeleccionada = null // Reinicia hora seleccionada
                            }
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = dia.nombreDia,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (esSeleccionado) Blanco else TextoGris
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dia.numeroDia,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (esSeleccionado) Blanco else TextoOscuro
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Horarios disponibles", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextoOscuro)
            Spacer(modifier = Modifier.height(12.dp))

            // Grilla de horarios con LazyVerticalGrid
            if (horariosDisponibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay turnos disponibles para esta fecha.", color = TextoGris, fontSize = 14.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(horariosDisponibles) { hora ->
                        val estaSeleccionada = horaSeleccionada == hora
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (estaSeleccionada) AzulPrimario else Blanco)
                                .border(
                                    width = 1.dp,
                                    color = if (estaSeleccionada) AzulPrimario else BordeGris,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { horaSeleccionada = hora }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = hora,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (estaSeleccionada) Blanco else TextoOscuro
                            )
                        }
                    }
                }
            }
        }
    }
}