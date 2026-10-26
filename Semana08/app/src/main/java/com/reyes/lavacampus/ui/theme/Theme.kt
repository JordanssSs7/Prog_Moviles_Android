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

private val EstudianteClaro = lightColorScheme(
    primary = AzulMarca,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = Color(0xFF0B2A8A),
    secondary = Negro,
    onSecondary = Color.White,
    background = AzulFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFEEF2F8),
    onSurfaceVariant = TintaSuave,
    outline = Color(0xFFCBD5E1),
    outlineVariant = LineaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FAFD),
    surfaceContainer = Color(0xFFF1F4FA),
    surfaceContainerHigh = Color(0xFFEBEFF7),
    surfaceContainerHighest = Color(0xFFE5EAF4),
    error = Color(0xFFDC2626)
)

private val AdminClaro = lightColorScheme(
    primary = IndigoMarca,
    onPrimary = Color.White,
    primaryContainer = IndigoContenedor,
    onPrimaryContainer = Color(0xFF1E1B6E),
    secondary = Negro,
    onSecondary = Color.White,
    background = IndigoFondo,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = Color(0xFFEEEEF8),
    onSurfaceVariant = TintaSuave,
    outline = Color(0xFFCBD5E1),
    outlineVariant = LineaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFAF9FE),
    surfaceContainer = Color(0xFFF2F1FA),
    surfaceContainerHigh = Color(0xFFECEBF6),
    surfaceContainerHighest = Color(0xFFE6E5F3),
    error = Color(0xFFDC2626)
)

private val EstudianteOscuro = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Color(0xFF0A1B52),
    primaryContainer = Color(0xFF1B2E66),
    onPrimaryContainer = AzulContenedor,
    secondary = Color(0xFFE8ECF8),
    onSecondary = Color(0xFF0B1020),
    background = FondoOscuro,
    onBackground = Color(0xFFE8ECF8),
    surface = SuperficieOscura,
    onSurface = Color(0xFFE8ECF8),
    surfaceVariant = SuperficieOscuraAlta,
    onSurfaceVariant = Color(0xFF9AA6C4),
    outline = Color(0xFF3A4566),
    outlineVariant = Color(0xFF2A3452),
    surfaceContainerLowest = Color(0xFF0E1428),
    surfaceContainerLow = SuperficieOscura,
    surfaceContainer = SuperficieOscuraAlta,
    surfaceContainerHigh = Color(0xFF232E4C),
    surfaceContainerHighest = Color(0xFF2A3558)
)

private val AdminOscuro = darkColorScheme(
    primary = IndigoClaro,
    onPrimary = Color(0xFF1E1B6E),
    primaryContainer = Color(0xFF2E2A8F),
    onPrimaryContainer = IndigoContenedor,
    secondary = Color(0xFFE8ECF8),
    onSecondary = Color(0xFF0B1020),
    background = FondoOscuro,
    onBackground = Color(0xFFE8ECF8),
    surface = SuperficieOscura,
    onSurface = Color(0xFFE8ECF8),
    surfaceVariant = SuperficieOscuraAlta,
    onSurfaceVariant = Color(0xFF9AA6C4),
    outline = Color(0xFF3A4566),
    outlineVariant = Color(0xFF2A3452),
    surfaceContainerLowest = Color(0xFF0E1428),
    surfaceContainerLow = SuperficieOscura,
    surfaceContainer = SuperficieOscuraAlta,
    surfaceContainerHigh = Color(0xFF232E4C),
    surfaceContainerHighest = Color(0xFF2A3558)
)

/** Esquinas muy redondeadas, como en el diseno de referencia. */
private val FormasLava = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

/** Degradado de marca (azul profundo arriba) para encabezados, splash y bienvenida. */
fun degradadoMarca(rol: Rol = Rol.CLIENTE): Brush =
    if (rol == Rol.ADMIN) {
        Brush.verticalGradient(listOf(Color(0xFF312E81), IndigoMarca, Color(0xFF7C75F5)))
    } else {
        Brush.verticalGradient(listOf(AzulProfundo, AzulIntermedio, Color(0xFF5B94FF)))
    }

/** El esquema de color depende del perfil activo (rol) y del modo oscuro. */
@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val colores = when {
        rol == Rol.ADMIN && oscuro -> AdminOscuro
        rol == Rol.ADMIN -> AdminClaro
        oscuro -> EstudianteOscuro
        else -> EstudianteClaro
    }
    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        shapes = FormasLava,
        content = content
    )
}
