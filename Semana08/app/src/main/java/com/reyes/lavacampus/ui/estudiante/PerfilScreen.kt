package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.FilaSwitch
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

    Scaffold(topBar = { AppTopBar("Perfil") }) { padding ->
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
                    Text(u.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(u.codigo)
                    Text(u.correo, style = MaterialTheme.typography.bodySmall)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Dato("Activos", "$activos", Modifier.weight(1f))
                Dato("Pedidos", "$totales", Modifier.weight(1f))
                Dato("Kilos", "%.1f".format(kilos), Modifier.weight(1f))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro)
                    FilaSwitch("Avisos", avisos, sesion::cambiarNotificaciones, "Recibir avisos de tu pedido")
                }
            }

            OutlinedButton(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar de perfil")
            }
            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
        }
    }
}
