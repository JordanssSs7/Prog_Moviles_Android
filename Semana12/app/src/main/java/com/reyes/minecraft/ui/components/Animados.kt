package com.reyes.minecraft.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.reyes.minecraft.ui.theme.FuenteTitulo
import com.reyes.minecraft.ui.theme.Marron
import com.reyes.minecraft.ui.theme.RojoSuave
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia

enum class TipoBoton { PRINCIPAL, SECUNDARIO, PELIGRO }

/** Botón redondeado con sombra suave; al tocarlo se "hunde" con un pequeño rebote. */
@Composable
fun BotonApp(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tipo: TipoBoton = TipoBoton.PRINCIPAL,
    enabled: Boolean = true,
    tamanoTexto: TextUnit = 18.sp,
) {
    val fuente = remember { MutableInteractionSource() }
    val presionado by fuente.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionado) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "escalaBoton",
    )
    val (fondo, letra) = when (tipo) {
        TipoBoton.PRINCIPAL -> VerdeOscuro to Color.White
        TipoBoton.SECUNDARIO -> VerdeSalvia to VerdeProfundo
        TipoBoton.PELIGRO -> RojoSuave to Color.White
    }
    val forma = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .heightIn(min = 52.dp)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
                alpha = if (enabled) 1f else 0.5f
            }
            .shadow(if (enabled) 5.dp else 0.dp, forma)
            .background(fondo, forma)
            .clip(forma)
            .clickable(interactionSource = fuente, indication = null, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = texto.uppercase(),
            color = letra,
            fontFamily = FuenteTitulo,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontSize = tamanoTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

/** Cabeza (avatar) del jugador de Minecraft; si no carga, muestra la inicial. */
@Composable
fun CabezaJugador(nombre: String, tamano: Dp, modifier: Modifier = Modifier) {
    val limpio = nombre.filter { it.isLetterOrDigit() || it == '_' }.ifBlank { "Steve" }
    var fallo by remember(limpio) { mutableStateOf(false) }
    Box(
        modifier = modifier
            .size(tamano)
            .border(2.dp, VerdeProfundo, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
            .background(Marron),
        contentAlignment = Alignment.Center,
    ) {
        if (fallo) {
            Text(limpio.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        } else {
            AsyncImage(
                model = "https://mc-heads.net/avatar/$limpio/128",
                contentDescription = "Cabeza de $limpio",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                onError = { fallo = true },
            )
        }
    }
}
