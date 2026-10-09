package com.reyes.clinicasaludplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.Tam
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.VerdeSecundario

/** Diálogo de confirmación (registro / inicio de sesión) con el logo, un check, el mensaje y un botón. */
@Composable
fun DialogoExito(
    mostrar: Boolean,
    titulo: String,
    mensaje: String,
    textoBoton: String = "Continuar",
    onCerrar: () -> Unit
) {
    if (!mostrar) return
    Dialog(onDismissRequest = onCerrar) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Blanco
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LogoSaludPlus(escala = 0.5f)
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(VerdeSecundario),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Éxito",
                        tint = Blanco,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = titulo,
                    fontSize = Tam.Barra,
                    fontWeight = FontWeight.Bold,
                    color = VerdePrimario,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mensaje,
                    fontSize = Tam.Cuerpo,
                    color = SlateTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                BotonPrimario(texto = textoBoton, onClick = onCerrar)
            }
        }
    }
}
