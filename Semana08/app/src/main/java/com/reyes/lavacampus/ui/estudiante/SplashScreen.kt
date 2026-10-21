package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.IconoServicio
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconoServicio(icono = "wash", color = MaterialTheme.colorScheme.onPrimary, tamano = 96.dp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "LavaCampus",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Lavandería universitaria",
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
