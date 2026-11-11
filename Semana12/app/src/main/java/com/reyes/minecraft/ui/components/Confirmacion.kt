package com.reyes.minecraft.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.minecraft.R
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia
import kotlinx.coroutines.delay

/**
 * Mensaje de confirmación a pantalla completa: tarjeta con el zombi montado en la gallina y un check
 * que aparece con rebote. Se cierra solo a los ~2,5 s o al tocar la pantalla; entonces llama a [onTerminar].
 */
@Composable
fun ConfirmacionOverlay(titulo: String, mensaje: String, onTerminar: () -> Unit) {
    var fase by remember { mutableIntStateOf(0) } // 0 = empezando, 1 = visible, 2 = saliendo
    var mostrarCheck by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fase = 1
        delay(350)
        mostrarCheck = true
        delay(2200)
        if (fase == 1) fase = 2
    }
    LaunchedEffect(fase) {
        if (fase == 2) {
            delay(350)
            onTerminar()
        }
    }

    val visible = fase == 1
    val escalaCheck by animateFloatAsState(
        targetValue = if (mostrarCheck && visible) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "check",
    )
    val escalaTarjeta by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "tarjeta",
    )
    val flotar by rememberInfiniteTransition(label = "flotar").animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "flotarY",
    )

    AnimatedVisibility(visible = visible, enter = fadeIn(tween(250)), exit = fadeOut(tween(300))) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { fase = 2 },
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier
                    .padding(32.dp)
                    .graphicsLayer { scaleX = escalaTarjeta; scaleY = escalaTarjeta },
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 18.dp,
            ) {
                Column(
                    Modifier.padding(horizontal = 36.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(Modifier.size(width = 130.dp, height = 210.dp)) {
                        Image(
                            painter = painterResource(R.drawable.confirmacion),
                            contentDescription = "Zombi montado en una gallina",
                            modifier = Modifier.fillMaxSize().graphicsLayer { translationY = flotar },
                            contentScale = ContentScale.Fit,
                        )
                        // Check verde que "salta" al aparecer.
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 14.dp, y = 6.dp)
                                .size(58.dp)
                                .graphicsLayer { scaleX = escalaCheck; scaleY = escalaCheck }
                                .shadow(8.dp, CircleShape)
                                .background(VerdeOscuro, CircleShape)
                                .border(3.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Listo", tint = Color.White, modifier = Modifier.size(34.dp))
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    TextoTitulo(titulo, 30.sp, VerdeProfundo, sombra = VerdeSalvia)
                    Spacer(Modifier.height(8.dp))
                    Text(mensaje, color = VerdeMedio, fontSize = 15.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
