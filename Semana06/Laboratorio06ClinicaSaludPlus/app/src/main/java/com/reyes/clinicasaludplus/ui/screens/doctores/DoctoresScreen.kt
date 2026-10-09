package com.reyes.clinicasaludplus.ui.screens.doctores

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.DestinoMenu
import com.reyes.clinicasaludplus.ui.components.FotoMedico
import com.reyes.clinicasaludplus.ui.components.PantallaConMenu
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.ui.theme.Tam
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponible
import com.reyes.clinicasaludplus.ui.theme.VerdeDisponibleFondo
import com.reyes.clinicasaludplus.util.FechaUtils

/**
 * Lista de doctores. Con [sedeId] muestra los de esa sede (flujo "Agendar una cita" desde una sede, con flecha de volver);
 * sin él es el directorio general del menú lateral. Siempre se puede filtrar por especialidad.
 */
@Composable
fun DoctoresScreen(
    sedeId: String,
    especialidadId: String,
    alSeleccionarMedico: (String) -> Unit,
    alNavegar: (DestinoMenu) -> Unit,
    alIrAInicio: () -> Unit,
    alCerrarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    // Una sede o especialidad que no existe se ignora, para no mostrar una lista vacía por un argumento inválido
    val sedeInicial = sedeId.takeIf { Repositorio.obtenerSede(it) != null }.orEmpty()
    val espInicial = especialidadId.takeIf { Repositorio.obtenerEspecialidad(it) != null }.orEmpty()
    val desdeSede = sedeInicial.isNotEmpty()

    var sedeFiltro by rememberSaveable { mutableStateOf(sedeInicial) }
    var espFiltro by rememberSaveable { mutableStateOf(espInicial) }

    val sedes = remember { Repositorio.sedes() }
    val especialidades = remember(sedeFiltro) { Repositorio.especialidadesDeSede(sedeFiltro) }
    // Si la especialidad elegida no existe en la sede filtrada, se quita el filtro
    val espActiva = espFiltro.takeIf { id -> especialidades.any { it.id == id } }.orEmpty()
    val medicos = remember(sedeFiltro, espActiva) { Repositorio.buscarMedicos(sedeFiltro, espActiva) }

    val tituloSede = Repositorio.obtenerSede(sedeInicial)?.nombre
    PantallaConMenu(
        titulo = if (tituloSede != null) "Doctores · $tituloSede" else "Doctores",
        seleccionado = DestinoMenu.Doctor,
        alNavegar = alNavegar,
        alIrAInicio = alIrAInicio,
        alCerrarSesion = alCerrarSesion,
        alVolver = if (desdeSede) alVolver else null
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!desdeSede) {
                FilaFiltros(
                    titulo = "Sede",
                    opciones = sedes.map { it.id to it.nombre },
                    seleccionada = sedeFiltro,
                    textoTodas = "Todas",
                    alElegir = { sedeFiltro = it }
                )
            }
            FilaFiltros(
                titulo = "Especialidad",
                opciones = especialidades.map { it.id to it.nombre },
                seleccionada = espActiva,
                textoTodas = "Todas",
                alElegir = { espFiltro = it }
            )

            if (medicos.isEmpty()) {
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
                        Text(
                            text = "Sin doctores",
                            fontSize = Tam.Subtitulo,
                            fontWeight = FontWeight.Bold,
                            color = NavyTitulo
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No hay doctores con los filtros elegidos.",
                            fontSize = Tam.Cuerpo,
                            color = SlateTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        TarjetaDoctor(medico = medico, onClick = { alSeleccionarMedico(medico.id) })
                    }
                }
            }
        }
    }
}

/** Fila de chips con un "Todas" inicial; [seleccionada] vacío significa sin filtro. */
@Composable
private fun FilaFiltros(
    titulo: String,
    opciones: List<Pair<String, String>>,
    seleccionada: String,
    textoTodas: String,
    alElegir: (String) -> Unit
) {
    Text(
        text = titulo,
        fontSize = Tam.Pequeno,
        color = SlateTexto,
        modifier = Modifier.padding(start = 20.dp, top = 4.dp)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        item(key = "todas") { Chip(textoTodas, seleccionada.isEmpty()) { alElegir("") } }
        items(opciones, key = { it.first }) { (id, nombre) ->
            Chip(nombre, seleccionada == id) { alElegir(id) }
        }
    }
}

@Composable
private fun Chip(texto: String, activo: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = if (activo) VerdePrimario else SuperficieSuave,
        border = if (activo) null else BorderStroke(1.dp, BordeSuave)
    ) {
        Text(
            text = texto,
            fontSize = Tam.Pequeno,
            fontWeight = FontWeight.Medium,
            color = if (activo) Blanco else NavyTitulo,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/** Tarjeta del doctor: foto, nombre, especialidad, código, sede, teléfono y días de atención. */
@Composable
private fun TarjetaDoctor(medico: Medico, onClick: () -> Unit) {
    val sede = remember(medico.sedeId) { Repositorio.obtenerSede(medico.sedeId)?.nombre.orEmpty() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Blanco,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FotoMedico(medico = medico, tamano = 72.dp)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medico.nombre,
                        fontSize = Tam.Subtitulo,
                        fontWeight = FontWeight.Bold,
                        color = NavyTitulo
                    )
                    Text(text = medico.profesion, fontSize = Tam.Cuerpo, color = SlateTexto)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${medico.calificacion} (${medico.resenas})",
                            fontSize = Tam.Pequeno,
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

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Código: ${medico.codigo}", fontSize = Tam.Pequeno, color = SlateTexto)
            Text(text = "Sede: $sede", fontSize = Tam.Pequeno, color = SlateTexto)
            Text(text = "Teléfono: ${medico.telefono}", fontSize = Tam.Pequeno, color = SlateTexto)

            Spacer(modifier = Modifier.height(10.dp))
            Surface(shape = RoundedCornerShape(50), color = VerdeDisponibleFondo) {
                Text(
                    text = "Atiende: ${FechaUtils.resumenDias(medico.diasAtencion)}",
                    fontSize = Tam.Pequeno,
                    fontWeight = FontWeight.Medium,
                    color = VerdeDisponible,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}
