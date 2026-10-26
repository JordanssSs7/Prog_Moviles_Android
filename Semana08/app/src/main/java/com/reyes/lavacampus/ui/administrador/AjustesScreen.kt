package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.reyes.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        EncabezadoPerfil(
            nombre = usuario?.nombre ?: "Administrador",
            detalles = listOf(usuario?.correo ?: ""),
            etiqueta = "Perfil activo",
            rol = Rol.ADMIN
        )

        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 255.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TarjetaCambiarPerfil("Modo práctica: ir a la vista Estudiante", onCambiarPerfil)

                TituloSeccion("Preferencias")
                LavaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FilaPreferencia(Icons.Filled.DarkMode, "Modo oscuro", "Cambia el tema de la app", oscuro, sesion::cambiarModoOscuro)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        FilaPreferencia(Icons.Filled.Notifications, "Notificaciones", "Avisar de pedidos nuevos", avisos, sesion::cambiarNotificaciones)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        FilaPreferencia(
                            Icons.Filled.Storefront, "Recibir pedidos",
                            "Abrir o cerrar la recepción", recibir, sesion::cambiarRecibirPedidos
                        )
                    }
                }
                NotaDispositivo()

                BotonContorno("Cerrar sesión", onCerrarSesion, icono = IconoCerrarSesion)
            }
        }
    }
}
