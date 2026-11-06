package com.reyes.firebase

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BienvenidaScreen(onRegistro: () -> Unit, onLogin: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)) {
        // Ilustración: bandas diagonales con el birrete
        Box(Modifier.fillMaxWidth().weight(1f)) {
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val celeste = Path().apply {
                    moveTo(0f, 0.72f * h); lineTo(0.70f * w, 0.40f * h)
                    lineTo(0.70f * w, 0.74f * h); lineTo(0f, 1.0f * h); close()
                }
                drawPath(celeste, Celeste)
                val vino = Path().apply {
                    moveTo(0.04f * w, 0.52f * h); lineTo(w, 0.14f * h)
                    lineTo(w, 0.50f * h); lineTo(0.04f * w, 0.88f * h); close()
                }
                drawPath(vino, Vino)
            }
            // El dibujo del birrete ocupa el 17.5 % superior de su lienzo: se compensa para centrarlo
            Birrete(Modifier.align(Alignment.Center).size(260.dp).offset(y = 85.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text("Comencemos", color = GrisTexto, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Text(
            "Organiza tus cursos y alcanza tus metas.",
            color = Marino, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onRegistro,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Vino, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth().height(58.dp)
        ) { Text("Registrarme", fontSize = 17.sp, fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = onLogin,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, Marino),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Marino),
            modifier = Modifier.fillMaxWidth().height(58.dp)
        ) { Text("Ya tengo una cuenta", fontSize = 17.sp, fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(8.dp))
    }
}
