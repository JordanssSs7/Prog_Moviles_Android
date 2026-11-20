package com.reyes.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Un solo esquema claro: negro como color principal y lima como acento
private val EsquemaClaro = lightColorScheme(
    primary = Negro,
    onPrimary = Color.White,
    secondary = Lima,
    onSecondary = Negro,
    background = FondoHoja,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = GrisClaro,
    onSurfaceVariant = TextoSecundario,
    outline = GrisClaro,
    error = ErrorTexto
)

@Composable
fun CampusgpsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}
