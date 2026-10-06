package com.reyes.clinicasaludplus.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun CitaExitosaScreen(
    alIrAInicio: () -> Unit,
    alIrAMisCitas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoGris)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(VerdeExito.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Éxito",
                tint = VerdeExito,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("¡Cita Agendada!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoOscuro)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Tu cita médica ha sido confirmada con éxito.",
            fontSize = 14.sp,
            color = TextoGris
        )

        Spacer(modifier = Modifier.height(36.dp))

        BotonPrimario(
            texto = "Ver mis citas",
            onClick = alIrAMisCitas
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = alIrAInicio) {
            Text("Volver al Inicio", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
        }
    }
}