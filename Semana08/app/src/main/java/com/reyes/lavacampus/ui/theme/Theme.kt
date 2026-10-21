package com.reyes.lavacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.reyes.lavacampus.model.Rol

private val EstudianteClaro = lightColorScheme(
    primary = Turquesa,
    onPrimary = Color.White,
    primaryContainer = TurquesaContenedor,
    onPrimaryContainer = Color(0xFF083344),
    background = TurquesaFondo,
    surface = Color.White
)

private val AdminClaro = lightColorScheme(
    primary = Violeta,
    onPrimary = Color.White,
    primaryContainer = VioletaContenedor,
    onPrimaryContainer = Color(0xFF2E1065),
    background = VioletaFondo,
    surface = Color.White
)

private val EstudianteOscuro = darkColorScheme(
    primary = TurquesaClaro,
    onPrimary = Color(0xFF083344),
    primaryContainer = Color(0xFF155E75),
    onPrimaryContainer = TurquesaContenedor,
    background = FondoOscuro,
    surface = SuperficieOscura
)

private val AdminOscuro = darkColorScheme(
    primary = VioletaClaro,
    onPrimary = Color(0xFF2E1065),
    primaryContainer = Color(0xFF4C1D95),
    onPrimaryContainer = VioletaContenedor,
    background = FondoOscuro,
    surface = SuperficieOscura
)

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
        content = content
    )
}
