package com.reyes.clinicasaludplus.ui.screens.agendamiento

import com.reyes.clinicasaludplus.ui.theme.Tam
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.util.FechaUtils

private const val MAX_PAGINAS_DIAS = 7L

@Composable
fun FechaHoraScreen(
    medicoId: String,
    alContinuar: (String, String) -> Unit,
    alVolver: () -> Unit
) {
    val medico = remember(medicoId) { Repositorio.obtenerMedico(medicoId) }

    val diasAtencion = medico?.diasAtencion.orEmpty()

    // Calendario dinámico: 5 próximos días en que el médico atiende; cada flecha avanza 5 días de atención
    var semanaOffset by rememberSaveable { mutableStateOf(0L) }
    val dias = remember(medicoId, semanaOffset) { FechaUtils.obtenerDiasAtencion(diasAtencion, semanaOffset) }
    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf(FechaUtils.obtenerDiasAtencion(diasAtencion, 0).firstOrNull()?.fecha?.toString().orEmpty())
    }
    var horaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }

    // Si la fecha guardada ya no pertenece al grupo mostrado (p. ej. al rotar), vuelve al primer día
    if (dias.isNotEmpty() && dias.none { it.fecha.toString() == fechaSeleccionada }) {
        fechaSeleccionada = dias.first().fecha.toString()
    }

    // Se recalcula al cambiar de médico o de día; respeta los horarios ya reservados
    val horariosDisponibles = remember(medicoId, fechaSeleccionada, semanaOffset) {
        Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)
    }
    // Si la hora elegida ya no está disponible, se reinicia
    if (horaSeleccionada != null && horaSeleccionada !in horariosDisponibles) {
        horaSeleccionada = null
    }

    val fechaParaMes = FechaUtils.parsearFecha(fechaSeleccionada) ?: dias.firstOrNull()?.fecha
    val puedeRetroceder = semanaOffset > 0
    val puedeAvanzar = dias.isNotEmpty() && semanaOffset < MAX_PAGINAS_DIAS

    Scaffold(
        containerColor = Blanco,
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // --- 1. TARJETA DEL MÉDICO ---
                if (medico != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = SuperficieSuave
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FotoMedico(medico = medico, tamano = 92.dp)
                            Spacer(modifier = Modifier.width(18.dp))
                            Column {
                                Text(
                                    text = medico.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = Tam.Barra,
                                    color = NavyTitulo
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = medico.profesion,
                                    fontSize = Tam.Subtitulo,
                                    color = SlateTexto
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Sede ${Repositorio.obtenerSede(medico.sedeId)?.nombre.orEmpty()}",
                                    fontSize = Tam.Cuerpo,
                                    color = SlateTexto
                                )
                                Text(
                                    text = "Atiende: ${FechaUtils.resumenDias(medico.diasAtencion)}",
                                    fontSize = Tam.Cuerpo,
                                    color = SlateTexto
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No se encontró al médico seleccionado. Vuelve atrás y elige otro.",
                        color = RojoAlerta,
                        fontSize = Tam.Pequeno,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BordeSuave)

                // --- 2. SELECTOR DE MES CON FLECHAS (UNA SEMANA POR PASO) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (puedeRetroceder) {
                                semanaOffset--
                                fechaSeleccionada = FechaUtils.obtenerDiasAtencion(diasAtencion, semanaOffset).first().fecha.toString()
                                horaSeleccionada = null
                            }
                        },
                        enabled = puedeRetroceder
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Semana anterior",
                            tint = if (puedeRetroceder) NavyTitulo else Color(0xFFB8C3B2),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = fechaParaMes?.let { FechaUtils.obtenerEncabezadoMes(it) }.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        fontSize = Tam.Subtitulo,
                        color = NavyTitulo
                    )

                    IconButton(
                        onClick = {
                            if (puedeAvanzar) {
                                semanaOffset++
                                fechaSeleccionada = FechaUtils.obtenerDiasAtencion(diasAtencion, semanaOffset).first().fecha.toString()
                                horaSeleccionada = null
                            }
                        },
                        enabled = puedeAvanzar
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Semana siguiente",
                            tint = if (puedeAvanzar) NavyTitulo else Color(0xFFB8C3B2),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- 3. CÁPSULAS DE DÍAS HÁBILES ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dias.forEach { dia ->
                        val esSeleccionado = dia.fecha.toString() == fechaSeleccionada
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .clickable {
                                    if (!esSeleccionado) {
                                        fechaSeleccionada = dia.fecha.toString()
                                        horaSeleccionada = null
                                    }
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = if (esSeleccionado) VerdePrimario else SuperficieSuave
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = dia.nombreDia,
                                    fontSize = Tam.Cuerpo,
                                    color = if (esSeleccionado) Blanco else SlateTexto
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = dia.numeroDia,
                                    fontSize = Tam.Barra,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (esSeleccionado) Blanco else Color(0xFF2F4A33)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 4. GRILLA DE HORARIOS DISPONIBLES ---
                if (horariosDisponibles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No quedan horarios disponibles para este día. Prueba con otro día.",
                            fontSize = Tam.Pequeno,
                            color = SlateTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(horariosDisponibles, key = { it }) { hora ->
                            val estaSeleccionada = horaSeleccionada == hora
                            Surface(
                                modifier = Modifier
                                    .height(58.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { horaSeleccionada = hora },
                                shape = RoundedCornerShape(16.dp),
                                color = if (estaSeleccionada) VerdePrimario else SuperficieSuave,
                                border = if (!estaSeleccionada) BorderStroke(1.dp, BordeSuave) else null
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = hora,
                                        fontSize = Tam.Subtitulo,
                                        fontWeight = FontWeight.Medium,
                                        color = if (estaSeleccionada) Blanco else Color(0xFF2F4A33)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- 5. BOTÓN CONTINUAR: solo con médico, día y hora elegidos ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)
            ) {
                BotonPrimario(
                    texto = "Continuar",
                    habilitado = medico != null && horaSeleccionada != null,
                    onClick = {
                        val hora = horaSeleccionada
                        if (medico != null && hora != null) {
                            alContinuar(fechaSeleccionada, hora)
                        }
                    }
                )
            }
        }
    }
}
