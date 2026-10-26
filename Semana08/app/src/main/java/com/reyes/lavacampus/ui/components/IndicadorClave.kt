package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.FuerzaClave
import com.reyes.lavacampus.model.LARGO_MINIMO_CLAVE
import com.reyes.lavacampus.model.fuerzaClave
import com.reyes.lavacampus.model.requisitosClave

private val Rojo = Color(0xFFDC2626)
private val Ambar = Color(0xFFD97706)
private val Verde = Color(0xFF16A34A)

private fun colorDe(fuerza: FuerzaClave) = when (fuerza) {
    FuerzaClave.DEBIL -> Rojo
    FuerzaClave.MEDIA -> Ambar
    FuerzaClave.FUERTE -> Verde
}

/**
 * Muestra la fuerza de la contrasena (Debil, Media o Fuerte) con una barra de tres tramos y la lista
 * de requisitos. Solo muestra lo que calcula el modelo; no decide nada.
 */
@Composable
fun IndicadorClave(clave: String, modifier: Modifier = Modifier) {
    val fuerza = fuerzaClave(clave)
    val color = colorDe(fuerza)
    val tramos = when (fuerza) {
        FuerzaClave.DEBIL -> 1
        FuerzaClave.MEDIA -> 2
        FuerzaClave.FUERTE -> 3
    }
    val r = requisitosClave(clave)

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(if (i < tramos) color else MaterialTheme.colorScheme.outlineVariant)
                )
            }
            Text(
                text = fuerza.etiqueta,
                style = MaterialTheme.typography.labelLarge,
                color = color,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Requisito("Una letra mayúscula", r.mayuscula)
            Requisito("Un número", r.numero)
            Requisito("Un carácter especial (por ejemplo ! # $ %)", r.especial)
            Requisito("Mínimo $LARGO_MINIMO_CLAVE caracteres", r.largoMinimo)
        }
        Text(
            "Solo se acepta una contraseña de fuerza Media o Fuerte.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun Requisito(texto: String, cumplido: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = if (cumplido) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (cumplido) Verde else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(16.dp)
        )
        Text(
            texto,
            style = MaterialTheme.typography.bodySmall,
            color = if (cumplido) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
