package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** [ruta] es el patron del destino (para saber cual esta activa); [destino] es a donde se navega. */
data class Pestana(val titulo: String, val icono: ImageVector, val ruta: String, val destino: String)

val PestanasEstudiante = listOf(
    Pestana("Inicio", Icons.Filled.Home, Rutas.INICIO, Rutas.INICIO),
    Pestana("Pedidos", Icons.Filled.Receipt, Rutas.MIS_PEDIDOS, Rutas.MIS_PEDIDOS),
    Pestana("Tarifas", Icons.Filled.Sell, Rutas.TARIFAS, Rutas.TARIFAS),
    Pestana("Perfil", Icons.Filled.Person, Rutas.PERFIL, Rutas.PERFIL)
)

val PestanasAdmin = listOf(
    Pestana("Panel", Icons.Filled.Dashboard, Rutas.PANEL, Rutas.PANEL),
    Pestana("Pedidos", Icons.Filled.Inbox, Rutas.BANDEJA, Rutas.bandeja()),
    Pestana("Servicios", Icons.Filled.LocalLaundryService, Rutas.SERVICIOS, Rutas.SERVICIOS),
    Pestana("Clientes", Icons.Filled.People, Rutas.CLIENTES, Rutas.CLIENTES),
    Pestana("Ajustes", Icons.Filled.Settings, Rutas.AJUSTES, Rutas.AJUSTES)
)

/** Barra flotante: una pastilla blanca con sombra, margen alrededor y el item activo resaltado en azul. */
@Composable
fun BarraInferior(pestanas: List<Pestana>, rutaActual: String?, onSeleccionar: (Pestana) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(30.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            pestanas.forEach { p ->
                val activa = rutaActual == p.ruta
                val color = if (activa) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .selectable(selected = activa, role = Role.Tab, onClick = { onSeleccionar(p) })
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(58.dp)
                            .height(32.dp)
                            .clip(CircleShape)
                            .background(if (activa) MaterialTheme.colorScheme.primaryContainer else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) { Icon(p.icono, contentDescription = p.titulo, tint = color) }
                    Text(
                        p.titulo,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (activa) FontWeight.Bold else FontWeight.Medium,
                        color = color
                    )
                }
            }
        }
    }
}
