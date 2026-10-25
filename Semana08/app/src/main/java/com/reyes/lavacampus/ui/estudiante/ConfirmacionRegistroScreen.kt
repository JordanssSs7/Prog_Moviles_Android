package com.reyes.lavacampus.ui.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.LogoLava
import com.reyes.lavacampus.ui.theme.TurquesaMarca
import com.reyes.lavacampus.ui.theme.degradadoMarca

/**
 * Mensaje que ve quien acaba de crear su cuenta: logo, una palomita que aparece con un rebote y un boton
 * que lleva al inicio de sesion.
 */
@Composable
fun ConfirmacionRegistroScreen(nombre: String, onIrALogin: () -> Unit) {
    val escala = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        escala.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
    }
    BackHandler(onBack = onIrALogin)

    Box(modifier = Modifier.fillMaxSize().background(degradadoMarca(Rol.CLIENTE))) {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))
            LogoLava(tamano = 38.dp)
            Spacer(Modifier.weight(1f))

            Surface(
                modifier = Modifier.size(132.dp).scale(escala.value),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 12.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Check, contentDescription = null, tint = TurquesaMarca, modifier = Modifier.size(76.dp))
                }
            }
            Spacer(Modifier.height(32.dp))
            Text("¡Cuenta creada!", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
            Spacer(Modifier.height(10.dp))
            Text(
                "Bienvenido a LavaCampus, ${nombre.substringBefore(' ')}. Tu cuenta ya está lista: inicia sesión para hacer tu primer pedido.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.92f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))

            Button(
                onClick = onIrALogin,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TurquesaMarca)
            ) { Text("Ir a iniciar sesión", style = MaterialTheme.typography.titleSmall) }
            Spacer(Modifier.height(28.dp))
        }
    }
}
