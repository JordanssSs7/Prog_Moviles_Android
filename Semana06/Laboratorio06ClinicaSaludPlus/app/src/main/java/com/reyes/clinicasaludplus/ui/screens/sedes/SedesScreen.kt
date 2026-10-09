package com.reyes.clinicasaludplus.ui.screens.sedes

import androidx.compose.foundation.BorderStroke
import com.reyes.clinicasaludplus.ui.theme.VerdeSecundario
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.clinicasaludplus.data.model.Sede
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.DestinoMenu
import com.reyes.clinicasaludplus.ui.components.PantallaConMenu
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.Tam

@Composable
fun SedesScreen(
    alSeleccionarSede: (String) -> Unit,
    alNavegar: (DestinoMenu) -> Unit,
    alIrAInicio: () -> Unit,
    alCerrarSesion: () -> Unit
) {
    val sedes = remember { Repositorio.sedes() }

    PantallaConMenu(
        titulo = "Sedes",
        seleccionado = DestinoMenu.Sede,
        alNavegar = alNavegar,
        alIrAInicio = alIrAInicio,
        alCerrarSesion = alCerrarSesion
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Elige la sede donde quieres atenderte",
                fontSize = Tam.Cuerpo,
                color = SlateTexto,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 12.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sedes, key = { it.id }) { sede ->
                    TarjetaSede(sede = sede, onClick = { alSeleccionarSede(sede.id) })
                }
            }
        }
    }
}

@Composable
private fun TarjetaSede(sede: Sede, onClick: () -> Unit) {
    val totalDoctores = remember(sede.id) { Repositorio.buscarMedicos(sedeId = sede.id).size }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Blanco,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, BordeSuave)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(sede.fotoRes),
                    contentDescription = "Sede ${sede.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
                // Degradado inferior para que el nombre se lea sobre la foto
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, VerdePrimario.copy(alpha = 0.85f)),
                                startY = 160f
                            )
                        )
                )
                Text(
                    text = sede.nombre,
                    fontSize = Tam.Titulo,
                    fontWeight = FontWeight.ExtraBold,
                    color = Blanco,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 12.dp)
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = Blanco.copy(alpha = 0.92f)
                ) {
                    Text(
                        text = if (totalDoctores == 1) "1 doctor" else "$totalDoctores doctores",
                        fontSize = Tam.Pequeno,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdePrimario,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = VerdeSecundario,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sede.direccion,
                    fontSize = Tam.Cuerpo,
                    color = SlateTexto,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = SlateTexto,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
