package com.reyes.campusgps.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.reyes.campusgps.R

// Tipografía Poppins (Google Fonts, licencia OFL), guardada en res/font
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

private val base = Typography()

private fun TextStyle.conPoppins() = copy(fontFamily = Poppins)

// Todos los estilos de Material 3 usan Poppins
val Typography = Typography(
    displayLarge = base.displayLarge.conPoppins(),
    displayMedium = base.displayMedium.conPoppins(),
    displaySmall = base.displaySmall.conPoppins(),
    headlineLarge = base.headlineLarge.conPoppins(),
    headlineMedium = base.headlineMedium.conPoppins(),
    headlineSmall = base.headlineSmall.conPoppins(),
    titleLarge = base.titleLarge.conPoppins(),
    titleMedium = base.titleMedium.conPoppins(),
    titleSmall = base.titleSmall.conPoppins(),
    bodyLarge = base.bodyLarge.conPoppins(),
    bodyMedium = base.bodyMedium.conPoppins(),
    bodySmall = base.bodySmall.conPoppins(),
    labelLarge = base.labelLarge.conPoppins(),
    labelMedium = base.labelMedium.conPoppins(),
    labelSmall = base.labelSmall.conPoppins()
)
