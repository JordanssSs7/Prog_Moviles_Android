package com.reyes.clinicasaludplus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// Los estilos de Material usan la misma escala que Tam, para que ningún texto quede con un tamaño distinto
val Typography = Typography(
    headlineSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = Tam.Titulo, lineHeight = Tam.Titulo * 1.3f),
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = Tam.Barra, lineHeight = Tam.Barra * 1.3f),
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = Tam.Subtitulo, lineHeight = Tam.Subtitulo * 1.4f),
    titleSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = Tam.Cuerpo, lineHeight = Tam.Cuerpo * 1.4f),
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = Tam.Cuerpo, lineHeight = Tam.Cuerpo * 1.5f),
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = Tam.Cuerpo, lineHeight = Tam.Cuerpo * 1.5f),
    bodySmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = Tam.Pequeno, lineHeight = Tam.Pequeno * 1.4f),
    labelLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = Tam.Cuerpo, lineHeight = Tam.Cuerpo * 1.4f),
    labelMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = Tam.Pequeno, lineHeight = Tam.Pequeno * 1.4f),
    labelSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = Tam.Pequeno, lineHeight = Tam.Pequeno * 1.4f)
)
