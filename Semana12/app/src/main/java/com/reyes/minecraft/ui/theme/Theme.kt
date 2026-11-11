package com.reyes.minecraft.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    secondary = TierraClara,
    tertiary = Tierra,
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePasto,
    secondary = Tierra,
    tertiary = VerdeClaro,
)

@Composable
fun MinecraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
