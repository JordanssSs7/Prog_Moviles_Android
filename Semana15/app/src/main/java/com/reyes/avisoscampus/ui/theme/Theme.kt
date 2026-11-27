package com.reyes.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresAvisos = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    primaryContainer = AzulOscuro,
    secondary = Azul,
    background = Fondo,
    surface = Fondo,
    onSurface = Color(0xFF1C1B1F),
    error = RojoTexto,
    errorContainer = RojoFondo,
    onErrorContainer = RojoTexto
)

@Composable
fun AvisoscampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresAvisos,
        typography = Typography,
        content = content
    )
}
