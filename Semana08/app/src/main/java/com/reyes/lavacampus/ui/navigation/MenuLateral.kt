package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.colorMarca
import com.reyes.lavacampus.ui.components.colorTarjetaPerfil
import com.reyes.lavacampus.ui.components.fondoPerfil

/** Opcion del menu lateral. [ruta] es el patron del destino (para marcar la activa) y [destino] a donde se navega. */
data class ItemMenu(
    val titulo: String,
    val icono: ImageVector,
    val ruta: String,
    val destino: String,
    val esPestana: Boolean
)

/**
 * Menu lateral como el de la referencia: cabecera de color con el avatar y una onda blanca, lista de
 * opciones (la activa lleva una barra a la izquierda), "Cerrar sesion" y un boton de inicio abajo.
 */
@Composable
fun MenuLateral(
    nombre: String,
    subtitulo: String,
    rol: Rol,
    items: List<ItemMenu>,
    rutaActual: String?,
    onIr: (ItemMenu) -> Unit,
    onCerrarSesion: () -> Unit,
    onInicio: () -> Unit,
    onCerrar: () -> Unit
) {
    val marca = colorMarca(rol)
    val superficie = MaterialTheme.colorScheme.surface
    val textoItem = MaterialTheme.colorScheme.onSurface
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topEnd = 36.dp, bottomEnd = 36.dp),
        windowInsets = WindowInsets(0),
        modifier = Modifier.width(310.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Cabecera de color con onda blanca
                Box(modifier = Modifier.fillMaxWidth().height(230.dp).background(fondoPerfil(rol))) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val w = size.width
                        val h = size.height
                        val onda = Path().apply {
                            moveTo(0f, h * 0.84f)
                            cubicTo(w * 0.30f, h * 1.02f, w * 0.55f, h * 0.62f, w, h * 0.66f)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(onda, superficie)
                    }
                    IconButton(
                        onClick = onCerrar,
                        modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(top = 4.dp, end = 4.dp)
                    ) { Icon(Icons.Filled.Close, contentDescription = "Cerrar menú", tint = Color.White) }

                    Row(
                        modifier = Modifier.align(Alignment.CenterStart).statusBarsPadding().padding(start = 22.dp, bottom = 26.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(modifier = Modifier.size(74.dp), shape = CircleShape, color = colorTarjetaPerfil(), shadowElevation = 6.dp) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = marca, modifier = Modifier.size(52.dp))
                            }
                        }
                        Column {
                            Text(nombre, style = MaterialTheme.typography.titleMedium, color = Color.White, maxLines = 1)
                            Text(subtitulo, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.85f))
                        }
                    }
                }

                // Opciones
                Column(modifier = Modifier.weight(1f).padding(top = 6.dp)) {
                    items.forEach { item ->
                        val activa = rutaActual == item.ruta
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onIr(item) }
                                .padding(vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Barra de la opcion activa
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(22.dp)
                                    .background(if (activa) marca else Color.Transparent, RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                            )
                            Spacer(Modifier.width(24.dp))
                            Icon(item.icono, contentDescription = null, tint = marca, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(18.dp))
                            Text(
                                item.titulo,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textoItem,
                                fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Cerrar sesion y boton de inicio
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 28.dp, end = 20.dp, bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).clickable(onClick = onCerrarSesion).padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = marca, modifier = Modifier.size(22.dp))
                        Text("Cerrar sesión", style = MaterialTheme.typography.bodyLarge, color = textoItem)
                    }
                    Surface(
                        onClick = onInicio,
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = marca,
                        shadowElevation = 6.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Home, contentDescription = "Inicio", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}
