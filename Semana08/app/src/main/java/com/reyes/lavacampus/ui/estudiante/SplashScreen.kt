package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.LogoLava
import com.reyes.lavacampus.ui.theme.TurquesaHielo
import com.reyes.lavacampus.ui.theme.TurquesaIntermedio
import com.reyes.lavacampus.ui.theme.TurquesaProfundo
import kotlinx.coroutines.delay

/** [sesionLista]: el Splash espera a saber si hay un perfil guardado antes de continuar. */
@Composable
fun SplashScreen(sesionLista: Boolean = true, onTerminar: () -> Unit) {
    LaunchedEffect(sesionLista) {
        if (sesionLista) {
            delay(1500)
            onTerminar()
        }
    }
    // Turquesa profundo arriba que se aclara hacia abajo, con el logo al centro
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.0f to TurquesaProfundo,
                    0.55f to TurquesaIntermedio,
                    0.88f to TurquesaHielo,
                    1.0f to Color.White
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LogoLava(tamano = 52.dp)
    }
}
