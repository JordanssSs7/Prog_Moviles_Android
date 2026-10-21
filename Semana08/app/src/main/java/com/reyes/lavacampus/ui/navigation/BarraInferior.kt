package com.reyes.lavacampus.ui.navigation

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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

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

@Composable
fun BarraInferior(pestanas: List<Pestana>, rutaActual: String?, onSeleccionar: (Pestana) -> Unit) {
    NavigationBar {
        pestanas.forEach { p ->
            NavigationBarItem(
                selected = rutaActual == p.ruta,
                onClick = { onSeleccionar(p) },
                icon = { Icon(p.icono, contentDescription = p.titulo) },
                label = { Text(p.titulo) }
            )
        }
    }
}
