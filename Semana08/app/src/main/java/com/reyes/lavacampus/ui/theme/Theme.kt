package com.reyes.lavacampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol

private val EsquemaClaro = lightColorScheme(
    primary = TurquesaMarca,
    onPrimary = Color.White,
    primaryContainer = TurquesaContenedor,
    onPrimaryContainer = Color(0xFF0B4651),
    secondary = Negro,
    onSecondary = Color.White,
    background = TurquesaFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFEAF3F5),
    onSurfaceVariant = TintaSuave,
    outline = Color(0xFFC3D5D9),
    outlineVariant = LineaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF7FBFC),
    surfaceContainer = Color(0xFFEFF6F8),
    surfaceContainerHigh = Color(0xFFE8F1F3),
    surfaceContainerHighest = Color(0xFFE1EDF0),
    error = Color(0xFFDC2626)
)

private val EsquemaOscuro = darkColorScheme(
    primary = TurquesaClaro,
    onPrimary = Color(0xFF062A31),
    primaryContainer = Color(0xFF1B5663),
    onPrimaryContainer = TurquesaContenedor,
    secondary = Color(0xFFE6F1F3),
    onSecondary = Color(0xFF0B1517),
    background = FondoOscuro,
    onBackground = Color(0xFFE6F1F3),
    surface = SuperficieOscura,
    onSurface = Color(0xFFE6F1F3),
    surfaceVariant = SuperficieOscuraAlta,
    onSurfaceVariant = Color(0xFF9DB6BD),
    outline = Color(0xFF3B525A),
    outlineVariant = Color(0xFF2A3D44),
    surfaceContainerLowest = Color(0xFF0E1A1D),
    surfaceContainerLow = SuperficieOscura,
    surfaceContainer = SuperficieOscuraAlta,
    surfaceContainerHigh = Color(0xFF233840),
    surfaceContainerHighest = Color(0xFF2A434C)
)

/** Esquinas muy redondeadas, como en el diseno de referencia. */
private val FormasLava = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

/** Degradado de marca (verde profundo arriba) para encabezados, splash y bienvenida. */
fun degradadoMarca(@Suppress("UNUSED_PARAMETER") rol: Rol = Rol.CLIENTE): Brush =
    Brush.verticalGradient(listOf(TurquesaProfundo, TurquesaIntermedio, TurquesaSuave))

/** Toda la app usa el mismo verde; el rol se conserva por si un perfil necesita distinguirse. */
@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (oscuro) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        shapes = FormasLava,
        content = content
    )
}
