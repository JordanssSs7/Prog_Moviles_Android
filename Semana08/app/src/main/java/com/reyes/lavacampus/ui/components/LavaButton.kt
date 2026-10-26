package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.theme.GrisBoton
import com.reyes.lavacampus.ui.theme.Negro
import com.reyes.lavacampus.ui.theme.Tinta

enum class EstiloBoton { Primario, Oscuro, Suave, Peligro }

/** Boton de ancho completo con forma de pastilla. */
@Composable
fun LavaButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    estilo: EstiloBoton = EstiloBoton.Primario
) {
    val colores = when (estilo) {
        EstiloBoton.Primario -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
            disabledContentColor = Color.White
        )
        EstiloBoton.Oscuro -> ButtonDefaults.buttonColors(
            containerColor = Negro,
            contentColor = Color.White,
            disabledContainerColor = Negro.copy(alpha = 0.35f),
            disabledContentColor = Color.White
        )
        EstiloBoton.Peligro -> ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFEE2E2),
            contentColor = Color(0xFFDC2626),
            disabledContainerColor = GrisBoton.copy(alpha = 0.6f),
            disabledContentColor = Tinta.copy(alpha = 0.4f)
        )
        EstiloBoton.Suave -> ButtonDefaults.buttonColors(
            containerColor = GrisBoton,
            contentColor = Tinta,
            disabledContainerColor = GrisBoton.copy(alpha = 0.6f),
            disabledContentColor = Tinta.copy(alpha = 0.4f)
        )
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = colores,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        Text(texto, style = MaterialTheme.typography.titleSmall)
    }
}
