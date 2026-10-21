package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove

/**
 * Control con botones menos y mas. Recibe el valor actual, el paso, el minimo y el maximo.
 * Con valor 0 el servicio esta apagado; el primer "+" salta directo al minimo.
 */
@Composable
fun SelectorCantidad(
    valor: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onCambio: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val texto = if (paso % 1.0 == 0.0) valor.toInt().toString() else "%.1f".format(valor)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledTonalIconButton(
            onClick = {
                val nuevo = valor - paso
                onCambio(if (nuevo < minimo) 0.0 else nuevo)
            },
            enabled = valor > 0.0
        ) { Icon(Icons.Filled.Remove, contentDescription = "Menos") }

        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(44.dp)
        )

        FilledTonalIconButton(
            onClick = {
                val nuevo = if (valor == 0.0) minimo else valor + paso
                onCambio(nuevo.coerceAtMost(maximo))
            },
            enabled = valor < maximo
        ) { Icon(Icons.Filled.Add, contentDescription = "Mas") }
    }
}
