package com.reyes.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.campusgps.model.TipoError
import com.reyes.campusgps.model.UbicacionUi
import com.reyes.campusgps.ui.theme.ErrorFondo
import com.reyes.campusgps.ui.theme.ErrorTexto

// Recuadro de error y, debajo, la acción útil según el tipo
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val borde = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    val relleno = PaddingValues(horizontal = 40.dp, vertical = 12.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = error.mensaje,
            color = ErrorTexto,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .background(ErrorFondo, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        when (error.tipo) {
            TipoError.SIN_PERMISO -> OutlinedButton(
                border = borde,
                contentPadding = relleno,
                onClick = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", contexto.packageName, null)
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    contexto.startActivity(intent)
                }
            ) {
                Text("Abrir ajustes", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            TipoError.GPS_APAGADO, TipoError.OTRO -> OutlinedButton(
                onClick = onReintentar,
                border = borde,
                contentPadding = relleno
            ) {
                Text("Reintentar", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }

            // Sin Play Services no hay nada que reintentar
            TipoError.SIN_PLAY_SERVICES -> Unit
        }
    }
}
