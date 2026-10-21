package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.FilaSwitch
import com.reyes.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(sesion: SesionViewModel, onCambiarPerfil: () -> Unit, onCerrarSesion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()

    Scaffold(topBar = { AppTopBar("Ajustes") }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(usuario?.nombre ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(usuario?.correo ?: "", style = MaterialTheme.typography.bodySmall)
                }
            }

            OutlinedButton(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar de perfil")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro)
                    FilaSwitch("Notificaciones", avisos, sesion::cambiarNotificaciones)
                    FilaSwitch(
                        "Recibir pedidos", recibir, sesion::cambiarRecibirPedidos,
                        "Si lo apagas, los estudiantes no pueden crear pedidos"
                    )
                }
            }

            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
