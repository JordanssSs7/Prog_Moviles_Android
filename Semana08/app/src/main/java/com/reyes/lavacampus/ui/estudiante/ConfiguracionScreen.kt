package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.BotonCerrarSesion
import com.reyes.lavacampus.ui.components.CambiarClaveDialog
import com.reyes.lavacampus.ui.components.EditarPerfilDialog
import com.reyes.lavacampus.ui.components.EstiloTituloConfig
import com.reyes.lavacampus.ui.components.FilaConfig
import com.reyes.lavacampus.ui.components.FilaConfigLinea
import com.reyes.lavacampus.ui.components.FondoConfig
import com.reyes.lavacampus.ui.components.PerfilColor
import com.reyes.lavacampus.ui.components.SeccionConfig
import com.reyes.lavacampus.viewmodel.SesionViewModel

/**
 * Configuracion igual a la referencia: fondo verde plano, titulo centrado con la flecha y la luna, y tres
 * secciones (Cuenta, Notificaciones, Mas) en el centro de la pantalla, con "Cerrar sesion" al final.
 * La usan el estudiante (se llega desde el engranaje del perfil) y el administrador (pestana Ajustes).
 * La luna de arriba activa o desactiva el modo oscuro. La seccion "Mas" abre dos dialogos informativos (como funciona y acerca de).
 */
@Composable
fun ConfiguracionScreen(
    rol: Rol,
    sesion: SesionViewModel,
    onAtras: (() -> Unit)?,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()
    var editando by remember { mutableStateOf(false) }
    var cambiandoClave by remember { mutableStateOf(false) }
    var informacion by remember { mutableStateOf<String?>(null) }

    PerfilColor(
        rol = rol,
        titulo = if (rol == Rol.ADMIN) "Ajustes" else "Configuración",
        onAtras = onAtras,
        iconoAccion = if (oscuro) Icons.Filled.LightMode else Icons.Filled.DarkMode,
        descripcionAccion = "Modo oscuro",
        onAccion = { sesion.cambiarModoOscuro(!oscuro) },
        fondo = FondoConfig,
        estiloTitulo = EstiloTituloConfig
    ) {
        // Espacio entre el titulo y la primera seccion, como en la referencia
        Spacer(Modifier.height(70.dp))

        // La lista ocupa el centro: margenes de ~14 % a cada lado
        Column(modifier = Modifier.fillMaxWidth(0.72f)) {
            SeccionConfig(Icons.Outlined.AccountCircle, "Cuenta")
            FilaConfig("Editar perfil", onClick = { editando = true })
            FilaConfig("Cambiar contraseña", onClick = { cambiandoClave = true })
            FilaConfig(
                if (rol == Rol.ADMIN) "Cambiar a vista Estudiante" else "Cambiar a vista Administrador",
                onClick = onCambiarPerfil
            )

            SeccionConfig(Icons.Outlined.Notifications, "Notificaciones")
            if (rol == Rol.ADMIN) {
                FilaConfigLinea("Pedidos nuevos", avisos, sesion::cambiarNotificaciones)
                FilaConfigLinea("Recibir pedidos", recibir, sesion::cambiarRecibirPedidos)
            } else {
                FilaConfigLinea("Aviso de pedido listo", avisos, sesion::cambiarNotificaciones)
            }

            SeccionConfig(Icons.Outlined.Map, "Más")
            FilaConfig("Cómo funciona", onClick = { informacion = "como" })
            FilaConfig("Acerca de LavaCampus", onClick = { informacion = "acerca" })

            Spacer(Modifier.height(18.dp))
            BotonCerrarSesion(onCerrarSesion)
        }
    }

    informacion?.let { clave ->
        val (titulo, texto) = textoInformativo(clave, rol)
        AlertDialog(
            onDismissRequest = { informacion = null },
            title = { Text(titulo) },
            text = { Text(texto) },
            confirmButton = { TextButton(onClick = { informacion = null }) { Text("Entendido") } }
        )
    }
    if (editando) {
        EditarPerfilDialog(
            nombreActual = usuario?.nombre ?: "",
            onGuardar = { nombre, alTerminar -> sesion.editarNombre(nombre, alTerminar) },
            onCerrar = { editando = false }
        )
    }
    if (cambiandoClave) {
        CambiarClaveDialog(
            onCambiar = { actual, nueva, confirmacion, alTerminar -> sesion.cambiarClave(actual, nueva, confirmacion, alTerminar) },
            onCerrar = { cambiandoClave = false }
        )
    }
}

private fun textoInformativo(clave: String, rol: Rol): Pair<String, String> = when {
    clave == "como" && rol == Rol.ADMIN -> "Cómo funciona" to
        "1. Los pedidos nuevos llegan a la bandeja.\n2. Cámbiales el estado: recibido, en proceso y listo.\n" +
            "3. Asigna el casillero cuando el pedido esté listo para que el estudiante lo recoja."
    clave == "como" -> "Cómo funciona" to
        "1. Elige tus prendas y la fecha de entrega en un pedido nuevo.\n2. Sigue el estado desde Mis pedidos.\n" +
            "3. Cuando esté listo te indicamos el casillero donde recogerlo."
    else -> "Acerca de LavaCampus" to
        "Aplicación de lavandería para estudiantes, hecha con Kotlin, Jetpack Compose y Room como laboratorio del curso."
}
