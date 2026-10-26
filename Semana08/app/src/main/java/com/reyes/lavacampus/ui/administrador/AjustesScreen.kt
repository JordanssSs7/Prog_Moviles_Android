package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.Avatar
import com.reyes.lavacampus.ui.components.EstiloBoton
import com.reyes.lavacampus.ui.components.FilaSwitch
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.theme.degradadoMarca
import com.reyes.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth().height(230.dp).background(degradadoMarca(Rol.ADMIN))) {
            Column(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Avatar(usuario?.nombre ?: "Administrador", tamano = 76.dp)
                Text(usuario?.nombre ?: "", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(usuario?.correo ?: "", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 195.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LavaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro)
                        FilaSwitch("Notificaciones", avisos, sesion::cambiarNotificaciones)
                        FilaSwitch(
                            "Recibir pedidos", recibir, sesion::cambiarRecibirPedidos,
                            "Si lo apagas, los estudiantes no pueden crear pedidos"
                        )
                    }
                }
                LavaButton(texto = "Cambiar de perfil", onClick = onCambiarPerfil, estilo = EstiloBoton.Suave)
                LavaButton(texto = "Cerrar sesión", onClick = onCerrarSesion, estilo = EstiloBoton.Peligro)
            }
        }
    }
}
