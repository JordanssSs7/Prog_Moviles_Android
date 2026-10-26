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
    primary = VerdeMarca,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = Color(0xFF0E4A35),
    secondary = Negro,
    onSecondary = Color.White,
    background = VerdeFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFEBF3EF),
    onSurfaceVariant = TintaSuave,
    outline = Color(0xFFC5D3CC),
    outlineVariant = LineaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FBF9),
    surfaceContainer = Color(0xFFF0F6F3),
    surfaceContainerHigh = Color(0xFFE9F1ED),
    surfaceContainerHighest = Color(0xFFE2ECE7),
    error = Color(0xFFDC2626)
)

private val EsquemaOscuro = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = Color(0xFF06291D),
    primaryContainer = Color(0xFF1B5A43),
    onPrimaryContainer = VerdeContenedor,
    secondary = Color(0xFFE6F0EB),
    onSecondary = Color(0xFF0B1512),
    background = FondoOscuro,
    onBackground = Color(0xFFE6F0EB),
    surface = SuperficieOscura,
    onSurface = Color(0xFFE6F0EB),
    surfaceVariant = SuperficieOscuraAlta,
    onSurfaceVariant = Color(0xFF9DB5AA),
    outline = Color(0xFF3B5249),
    outlineVariant = Color(0xFF2A3D35),
    surfaceContainerLowest = Color(0xFF0E1814),
    surfaceContainerLow = SuperficieOscura,
    surfaceContainer = SuperficieOscuraAlta,
    surfaceContainerHigh = Color(0xFF23352F),
    surfaceContainerHighest = Color(0xFF2A4038)
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
    Brush.verticalGradient(listOf(VerdeProfundo, VerdeIntermedio, VerdeSuave))

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
