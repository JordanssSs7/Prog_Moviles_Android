package com.reyes.clinicasaludplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.Tam
import com.reyes.clinicasaludplus.util.NivelContrasena

// Colores de semáforo: se mantienen aparte de la paleta porque comunican un estado
private val RojoSemaforo = Color(0xFFE53935)
private val AmbarSemaforo = Color(0xFFF59E0B)
private val VerdeSemaforo = Color(0xFF2E9E4F)

/** Barra de 3 tramos (rojo, ámbar, verde) con el texto de la fortaleza de la contraseña. */
@Composable
fun IndicadorSeguridad(nivel: NivelContrasena, modifier: Modifier = Modifier) {
    if (nivel == NivelContrasena.NINGUNO) return
    val color = when (nivel) {
        NivelContrasena.DEBIL -> RojoSemaforo
        NivelContrasena.MEDIA -> AmbarSemaforo
        else -> VerdeSemaforo
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(3) { indice ->
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (indice < nivel.segmentos) color else BordeSuave)
                )
            }
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            Text(text = "Seguridad: ", fontSize = Tam.Pequeno, color = SlateTexto)
            Text(text = nivel.etiqueta, fontSize = Tam.Pequeno, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
