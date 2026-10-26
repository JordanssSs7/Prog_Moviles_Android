package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.BotonContorno
import com.reyes.lavacampus.ui.components.EncabezadoPerfil
import com.reyes.lavacampus.ui.components.FilaPreferencia
import com.reyes.lavacampus.ui.components.IconoCerrarSesion
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.NotaDispositivo
import com.reyes.lavacampus.ui.components.TarjetaCambiarPerfil
import com.reyes.lavacampus.ui.components.TituloSeccion
import com.reyes.lavacampus.viewmodel.PerfilViewModel
import com.reyes.lavacampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    perfil: PerfilViewModel,
    sesion: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val activos by perfil.pedidosActivos.collectAsState()
    val totales by perfil.pedidosTotales.collectAsState()
    val kilos by perfil.kilosLavados.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val u = perfil.usuario

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        EncabezadoPerfil(
            nombre = u.nombre,
            detalles = listOf("Código ${u.codigo}", u.correo),
            etiqueta = "Estudiante",
            rol = Rol.CLIENTE
        )

        // Hoja que sube sobre el encabezado
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 255.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Dato(Icons.Filled.Schedule, "$activos", "Pedidos activos", Modifier.weight(1f))
                    Dato(Icons.Filled.Receipt, "$totales", "Pedidos totales", Modifier.weight(1f))
                    Dato(Icons.Filled.LocalLaundryService, "%.1f".format(kilos), "Kg lavados", Modifier.weight(1f))
                }

                TarjetaCambiarPerfil("Modo práctica: ir a la vista Administrador", onCambiarPerfil)

                TituloSeccion("Preferencias")
                LavaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FilaPreferencia(Icons.Filled.DarkMode, "Modo oscuro", "Cambia el tema de la app", oscuro, sesion::cambiarModoOscuro)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        FilaPreferencia(
                            Icons.Filled.NotificationsActive, "Avisos de pedido listo",
                            "Notificar cuando puedas recoger", avisos, sesion::cambiarNotificaciones
                        )
                    }
                }
                NotaDispositivo()

                BotonContorno("Cerrar sesión", onCerrarSesion, icono = IconoCerrarSesion)
            }
        }
    }
}

@Composable
private fun Dato(icono: ImageVector, valor: String, titulo: String, modifier: Modifier) {
    LavaCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) { Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)) }
            Text(valor, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(
                titulo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
