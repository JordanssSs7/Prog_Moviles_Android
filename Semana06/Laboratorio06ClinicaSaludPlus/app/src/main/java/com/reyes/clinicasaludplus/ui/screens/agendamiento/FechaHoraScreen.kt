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

@Composable
fun FechaHoraScreen(
    medicoId: String,
    alContinuar: (String, String) -> Unit,
    alVolver: () -> Unit
) {
    val medico = remember(medicoId) { Repositorio.obtenerMedico(medicoId) }

    // Días fijos para la Fase 1
    val dias = listOf(
        Pair("Lun", "15"),
        Pair("Mar", "16"),
        Pair("Mié", "17"),
        Pair("Jue", "18"),
        Pair("Vie", "19")
    )
    var diaSeleccionado by remember { mutableStateOf("16") }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    // Fecha armada para el repositorio en Fase 1
    val fechaCompleta = "2026-09-$diaSeleccionado"

    // Recalcular horarios reactivamente
    val horariosDisponibles = remember(medicoId, fechaCompleta) {
        Repositorio.horariosDisponibles(medicoId, fechaCompleta)
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
                                alContinuar(fechaCompleta, hora)
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
            // Tarjeta superior resumen del médico
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

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de mes y flechas (Fase 1 visual)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Fase 2 */ }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Mes anterior", tint = TextoGris)
                }
                Text("Setiembre 2026", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextoOscuro)
                IconButton(onClick = { /* Fase 2 */ }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Mes siguiente", tint = TextoGris)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fila de días
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dias.forEach { (nombreDia, numDia) ->
                    val esSeleccionado = diaSeleccionado == numDia
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (esSeleccionado) AzulPrimario else Blanco)
                            .clickable {
                                diaSeleccionado = numDia
                                horaSeleccionada = null // Reiniciar hora al cambiar día
                            }
                            .padding(vertical = 12.dp, horizontal = 14.dp)
                    ) {
                        Text(
                            text = nombreDia,
                            fontSize = 12.sp,
                            color = if (esSeleccionado) Blanco else TextoGris
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = numDia,
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

            // Cuadrícula de horarios (LazyVerticalGrid)
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