package com.reyes.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Diseño claro: se usa el mismo esquema aunque el teléfono esté en modo oscuro
private val Esquema = lightColorScheme(
    primary = Azul,
    onPrimary = Blanco,
    secondary = AzulCielo,
    onSecondary = Tinta,
    tertiary = Amarillo,
    background = Crema,
    onBackground = Tinta,
    surface = Papel,
    onSurface = Tinta,
    onSurfaceVariant = TintaSuave,
    outlineVariant = CremaOscuro
)

@Composable
fun CentrotareasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Esquema,
        typography = Typography,
        content = content
    )
}
