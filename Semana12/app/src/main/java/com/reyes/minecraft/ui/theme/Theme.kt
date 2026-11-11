package com.reyes.minecraft.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VerdeSalvia,
    onPrimary = Color(0xFF1B2A22),
    primaryContainer = VerdeOscuro,
    onPrimaryContainer = VerdePiedra,
    secondary = VerdeMedio,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3F5F45),
    onSecondaryContainer = VerdePiedra,
    tertiary = VerdePiedra,
    background = Color(0xFF17211B),
    onBackground = VerdePiedra,
    surface = Color(0xFF1B2620),
    onSurface = VerdePiedra,
    surfaceVariant = Color(0xFF2A3A31),
    onSurfaceVariant = Color(0xFFC9D2C4),
    outline = VerdeSalvia,
    surfaceContainerLowest = Color(0xFF131B16),
    surfaceContainerLow = Color(0xFF1F2B24),
    surfaceContainer = Color(0xFF23322A),
    surfaceContainerHigh = Color(0xFF293A30),
    surfaceContainerHighest = Color(0xFF2F4137),
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeOscuro,
    onPrimary = Color.White,
    primaryContainer = VerdePiedra,
    onPrimaryContainer = VerdeProfundo,
    secondary = VerdeMedio,
    onSecondary = Color.White,
    secondaryContainer = VerdeSalvia,
    onSecondaryContainer = VerdeProfundo,
    tertiary = VerdeSalvia,
    background = Color.White,
    onBackground = VerdeProfundo,
    surface = Color.White,
    onSurface = VerdeProfundo,
    surfaceVariant = Color(0xFFEFEDE5),
    onSurfaceVariant = Color(0xFF4F6355),
    outline = VerdeMedio,
    outlineVariant = VerdeSalvia,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8F7F3),
    surfaceContainer = Color(0xFFF3F2EC),
    surfaceContainerHigh = Color(0xFFEFEDE5),
    surfaceContainerHighest = Color(0xFFE9E7DD),
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
