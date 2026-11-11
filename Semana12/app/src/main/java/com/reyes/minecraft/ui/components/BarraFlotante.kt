package com.reyes.minecraft.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.minecraft.ui.theme.FuenteTitulo
import com.reyes.minecraft.ui.theme.VerdeOscuro

class ItemNavegacion(val ruta: String, val titulo: String, val icono: ImageVector)

/**
 * Barra de navegación flotante en forma de píldora. El ítem elegido se expande en una cápsula
 * con ícono y nombre; los demás muestran solo el ícono.
 */
@Composable
fun BarraNavegacionFlotante(
    opciones: List<ItemNavegacion>,
    rutaActual: String?,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = RoundedCornerShape(36.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .shadow(16.dp, forma, ambientColor = VerdeOscuro.copy(alpha = 0.35f), spotColor = VerdeOscuro.copy(alpha = 0.35f))
            .background(MaterialTheme.colorScheme.surface, forma)
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        opciones.forEach { opcion ->
            ElementoBarra(opcion, seleccionado = opcion.ruta == rutaActual) { onSeleccion(opcion.ruta) }
        }
    }
}

@Composable
private fun ElementoBarra(opcion: ItemNavegacion, seleccionado: Boolean, onClick: () -> Unit) {
    val fondo by animateColorAsState(
        targetValue = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = tween(280),
        label = "fondoItem",
    )
    val tinte by animateColorAsState(
        targetValue = if (seleccionado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(280),
        label = "tinteItem",
    )
    val escala by animateFloatAsState(
        targetValue = if (seleccionado) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "escalaItem",
    )
    Row(
        modifier = Modifier
            .animateContentSize(spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow))
            .clip(RoundedCornerShape(28.dp))
            .background(fondo)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = opcion.icono,
            contentDescription = opcion.titulo,
            tint = tinte,
            modifier = Modifier.size(24.dp).graphicsLayer { scaleX = escala; scaleY = escala },
        )
        AnimatedVisibility(
            visible = seleccionado,
            enter = expandHorizontally(tween(260)) + fadeIn(tween(260)),
            exit = shrinkHorizontally(tween(200)) + fadeOut(tween(150)),
        ) {
            Text(
                opcion.titulo,
                color = tinte,
                fontFamily = FuenteTitulo,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
