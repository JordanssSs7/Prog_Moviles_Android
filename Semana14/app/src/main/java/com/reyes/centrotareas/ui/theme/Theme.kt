package com.reyes.centrotareas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Azul,
    onPrimary = Color.White,
    secondary = AzulGris,
    tertiary = AzulGris
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulClaro,
    secondary = AzulGris,
    tertiary = AzulGris
)

@Composable
fun CentrotareasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
