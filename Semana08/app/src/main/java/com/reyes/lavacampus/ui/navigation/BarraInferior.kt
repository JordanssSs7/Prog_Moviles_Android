package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.theme.TurquesaMarca
import com.reyes.lavacampus.ui.theme.TurquesaProfundo

/** [ruta] es el patron del destino (para saber cual esta activa); [destino] es a donde se navega. */
data class Pestana(val titulo: String, val icono: ImageVector, val ruta: String, val destino: String)

/** Estrella (tarifas y descuentos), casita (inicio) y persona (perfil): siempre en ese orden. */
val PestanasEstudiante = listOf(
    Pestana("Tarifas", Icons.Filled.Star, Rutas.TARIFAS, Rutas.TARIFAS),
    Pestana("Inicio", Icons.Filled.Home, Rutas.INICIO, Rutas.INICIO),
    Pestana("Perfil", Icons.Filled.Person, Rutas.PERFIL, Rutas.PERFIL)
)

/** Estrella (pedidos por atender), casita (panel) y persona (perfil). Lo demas esta en el menu lateral. */
val PestanasAdmin = listOf(
    Pestana("Pedidos", Icons.Filled.Star, Rutas.BANDEJA, Rutas.bandeja()),
    Pestana("Panel", Icons.Filled.Home, Rutas.PANEL, Rutas.PANEL),
    Pestana("Perfil", Icons.Filled.Person, Rutas.PERFIL_ADMIN, Rutas.PERFIL_ADMIN)
)

val PestanaAjustes = Pestana("Ajustes", Icons.Filled.Settings, Rutas.AJUSTES, Rutas.AJUSTES)

/** Pantallas donde se ve la barra: las pestanas y las que se abren desde el menu lateral. */
val RutasConBarraEstudiante = setOf(Rutas.TARIFAS, Rutas.INICIO, Rutas.PERFIL, Rutas.MIS_PEDIDOS)
val RutasConBarraAdmin = setOf(
    Rutas.BANDEJA, Rutas.PANEL, Rutas.PERFIL_ADMIN, Rutas.SERVICIOS, Rutas.CLIENTES, Rutas.AJUSTES
)

private val AltoBarra = 64.dp
private val AltoConBulto = 84.dp
private val Bulto = 68.dp

/**
 * Barra verde con tres circulos. Los inactivos son blancos; el activo se eleva sobre un bulto curvo de la misma
 * barra y se muestra en verde oscuro con un aro blanco.
 */
@Composable
fun BarraInferior(pestanas: List<Pestana>, rutaActual: String?, onSeleccionar: (Pestana) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(AltoConBulto)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(AltoBarra)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = TurquesaMarca,
            shadowElevation = 12.dp
        ) {}
        Row(
            modifier = Modifier.fillMaxWidth().height(AltoConBulto),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            pestanas.forEach { p ->
                val activa = rutaActual == p.ruta
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(AltoConBulto)
                        .selectable(selected = activa, role = Role.Tab, onClick = { onSeleccionar(p) }),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (activa) {
                        // Bulto curvo: un circulo del color de la barra que sobresale por arriba
                        Box(Modifier.offset(y = 0.dp).size(Bulto).clip(CircleShape).background(TurquesaMarca))
                        Box(
                            modifier = Modifier
                                .offset(y = 9.dp)
                                .size(50.dp)
                                .border(BorderStroke(3.dp, Color.White), CircleShape)
                                .clip(CircleShape)
                                .background(TurquesaProfundo),
                            contentAlignment = Alignment.Center
                        ) { Icon(p.icono, contentDescription = p.titulo, tint = Color.White) }
                    } else {
                        Box(
                            modifier = Modifier
                                .offset(y = 30.dp)
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) { Icon(p.icono, contentDescription = p.titulo, tint = TurquesaMarca) }
                    }
                }
            }
        }
    }
}
