package com.reyes.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.campusgps.model.TipoError
import com.reyes.campusgps.model.UbicacionUi
import com.reyes.campusgps.ui.theme.ErrorFondo
import com.reyes.campusgps.ui.theme.ErrorTexto
import com.reyes.campusgps.ui.theme.Negro
import com.reyes.campusgps.ui.theme.TextoSecundario

// Tarjeta de error y, debajo, la acción útil según el tipo
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    Surface(
        color = ErrorFondo,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(10.dp)
                        .background(ErrorTexto, CircleShape)
                )
                Spacer(Modifier.width(10.dp))
                Text("Algo salió mal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(error.mensaje, color = TextoSecundario, fontSize = 14.sp, lineHeight = 20.sp)

            // Botón según el tipo de error (sin Play Services no hay acción)
            val accion: Pair<String, () -> Unit>? = when (error.tipo) {
                TipoError.SIN_PERMISO -> "Abrir ajustes" to {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", contexto.packageName, null)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    contexto.startActivity(intent)
                }
                TipoError.GPS_APAGADO, TipoError.OTRO -> "Reintentar" to onReintentar
                TipoError.SIN_PLAY_SERVICES -> null
            }
            if (accion != null) {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = accion.second,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, Negro),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Negro),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(accion.first, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}
