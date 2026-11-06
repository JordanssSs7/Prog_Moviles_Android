package com.reyes.firebase

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Marino = Color(0xFF0B1033)
val Vino = Color(0xFFB0173F)
val Celeste = Color(0xFFA5D8F5)
val Naranja = Color(0xFFFAC98B)
val NaranjaCampo = Color(0xFFFFD08A)
val GrisCampo = Color(0xFFF4F4F4)
val GrisTexto = Color(0xFF8A8A8A)

/** Birrete de graduación (lienzo cuadrado). Ocupa x 0.18-0.82 y 0.04-0.31 del lado. */
@Composable
fun Birrete(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val base = Path().apply {
            moveTo(0.33f * w, 0.19f * h)
            lineTo(0.33f * w, 0.27f * h)
            quadraticTo(0.50f * w, 0.35f * h, 0.67f * w, 0.27f * h)
            lineTo(0.67f * w, 0.19f * h)
            close()
        }
        drawPath(base, Color(0xFF1C2466))
        val tabla = Path().apply {
            moveTo(0.50f * w, 0.04f * h)
            lineTo(0.82f * w, 0.17f * h)
            lineTo(0.50f * w, 0.30f * h)
            lineTo(0.18f * w, 0.17f * h)
            close()
        }
        drawPath(tabla, Marino)
        val dorado = Color(0xFFFFC107)
        drawLine(dorado, Offset(0.50f * w, 0.17f * h), Offset(0.76f * w, 0.19f * h), strokeWidth = 0.012f * w, cap = StrokeCap.Round)
        drawLine(dorado, Offset(0.76f * w, 0.19f * h), Offset(0.76f * w, 0.31f * h), strokeWidth = 0.012f * w, cap = StrokeCap.Round)
        drawCircle(dorado, radius = 0.02f * w, center = Offset(0.76f * w, 0.32f * h))
        drawCircle(dorado, radius = 0.015f * w, center = Offset(0.50f * w, 0.17f * h))
    }
}

@Composable
fun CampoPlopp(
    valor: String,
    alCambiar: (String) -> Unit,
    pista: String,
    destacado: Boolean = false,
    esPassword: Boolean = false,
    tipo: KeyboardType = KeyboardType.Text
) {
    val fondo = if (destacado) NaranjaCampo else GrisCampo
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        placeholder = { Text(pista, fontSize = 14.sp) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esPassword) KeyboardType.Password else tipo),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fondo,
            unfocusedContainerColor = fondo,
            focusedBorderColor = Vino,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Marino,
            unfocusedTextColor = Marino,
            cursorColor = Vino,
            focusedPlaceholderColor = GrisTexto,
            unfocusedPlaceholderColor = GrisTexto
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BotonPlopp(texto: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Vino, contentColor = Color.White),
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) { Text(texto, fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }
}

/** "Texto gris / Acción" como en el diseño (ej. «Si eres nuevo / Crear cuenta»). */
@Composable
fun EnlaceDoble(pregunta: String, accion: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$pregunta / ", color = GrisTexto, fontSize = 13.sp)
        Text(
            accion, color = Marino, fontSize = 15.sp, fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(onClick = onClick).padding(vertical = 6.dp)
        )
    }
}
