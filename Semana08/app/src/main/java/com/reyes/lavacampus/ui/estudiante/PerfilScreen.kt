package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
        // Encabezado con degradado, avatar y datos
        Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(degradadoMarca(Rol.CLIENTE))) {
            Column(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Avatar(u.nombre, tamano = 76.dp)
                Text(u.nombre, style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(u.codigo, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
                Text(u.correo, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
            }
        }

        // Hoja que sube sobre el encabezado
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 215.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Dato("Activos", "$activos", Modifier.weight(1f))
                    Dato("Pedidos", "$totales", Modifier.weight(1f))
                    Dato("Kilos", "%.1f".format(kilos), Modifier.weight(1f))
                }

                LavaCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        FilaSwitch("Modo oscuro", oscuro, sesion::cambiarModoOscuro)
                        FilaSwitch("Avisos", avisos, sesion::cambiarNotificaciones, "Recibir avisos de tu pedido")
                    }
                }

                LavaButton(texto = "Cambiar de perfil", onClick = onCambiarPerfil, estilo = EstiloBoton.Suave)
                LavaButton(texto = "Cerrar sesión", onClick = onCerrarSesion, estilo = EstiloBoton.Peligro)
            }
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String, modifier: Modifier) {
    LavaCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
