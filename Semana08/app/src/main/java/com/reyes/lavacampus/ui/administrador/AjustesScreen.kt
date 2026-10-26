package com.reyes.lavacampus.ui.administrador

import androidx.compose.runtime.Composable
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.estudiante.ConfiguracionScreen
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Ajustes del administrador: la misma pantalla de configuracion a color completo. */
@Composable
fun AjustesScreen(
    sesion: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    ConfiguracionScreen(
        rol = Rol.ADMIN,
        sesion = sesion,
        onAtras = null, // es una pestana de la barra inferior
        onCambiarPerfil = onCambiarPerfil,
        onCerrarSesion = onCerrarSesion
    )
}
