package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.theme.degradadoMarca

/**
 * Encabezado de Perfil y Ajustes: degradado con burbujas decorativas, avatar con aro blanco,
 * nombre, datos y una etiqueta en forma de pastilla.
 */
@Composable
fun EncabezadoPerfil(
    nombre: String,
    detalles: List<String>,
    etiqueta: String,
    rol: Rol,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .background(degradadoMarca(rol))
            .drawBehind {
                // Burbujas suaves de fondo
                drawCircle(Color.White, radius = size.width * 0.42f, center = Offset(size.width * 0.95f, size.height * 0.05f), alpha = 0.07f)
                drawCircle(Color.White, radius = size.width * 0.30f, center = Offset(size.width * 0.02f, size.height * 0.75f), alpha = 0.06f)
                drawCircle(Color.White, radius = size.width * 0.10f, center = Offset(size.width * 0.80f, size.height * 0.70f), alpha = 0.08f)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(top = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Aro blanco con sombra alrededor del avatar
            Surface(shape = CircleShape, color = Color.White, shadowElevation = 10.dp) {
                Box(modifier = Modifier.padding(5.dp)) { Avatar(nombre, tamano = 84.dp) }
            }
            Text(
                nombre,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp)
            )
            detalles.forEach {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(Modifier.size(7.dp).background(Color(0xFF86EFAC), CircleShape))
                Text(etiqueta, style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }
    }
}

/** Tarjeta con degradado para cambiar de perfil (modo practica). */
@Composable
fun TarjetaCambiarPerfil(subtitulo: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val primario = MaterialTheme.colorScheme.primary
    val forma = MaterialTheme.shapes.medium
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, forma)
            .clip(forma)
            .clickable(onClick = onClick)
            .background(Brush.horizontalGradient(listOf(primario, lerp(primario, Color.White, 0.22f))))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = Color.White) }
        Column(modifier = Modifier.weight(1f)) {
            Text("Cambiar de perfil", style = MaterialTheme.typography.titleSmall, color = Color.White)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
    }
}

/** Fila de preferencia con icono en un cuadro de color, descripcion corta y un interruptor. */
@Composable
fun FilaPreferencia(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    activo: Boolean,
    onCambio: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) { Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Text(descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = activo, onCheckedChange = onCambio)
    }
}

@Composable
fun TituloSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(start = 4.dp)
    )
}

/** Nota discreta para decir que las preferencias se guardan en el dispositivo. */
@Composable
fun NotaDispositivo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Text(
            "Tus preferencias se guardan en este dispositivo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Boton de contorno con icono, para "Cerrar sesion" y "Cancelar". */
@Composable
fun BotonContorno(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
    icono: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.6f))
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Box(Modifier.size(8.dp))
        }
        Text(texto, style = MaterialTheme.typography.titleSmall, color = color)
    }
}

/** Icono de cerrar sesion, para usar con [BotonContorno]. */
val IconoCerrarSesion: ImageVector get() = Icons.AutoMirrored.Filled.Logout
