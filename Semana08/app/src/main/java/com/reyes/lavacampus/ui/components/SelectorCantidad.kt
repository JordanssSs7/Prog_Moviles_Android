package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Control con botones menos y mas dentro de una pastilla. Recibe el valor actual, el paso,
 * el minimo y el maximo. Con valor 0 el servicio esta apagado; el primer "+" salta al minimo.
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
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = {
                    val nuevo = valor - paso
                    onCambio(if (nuevo < minimo) 0.0 else nuevo)
                },
                enabled = valor > 0.0,
                modifier = Modifier.size(34.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
            ) { Icon(Icons.Filled.Remove, contentDescription = "Menos", modifier = Modifier.size(18.dp)) }

            Text(
                text = texto,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(40.dp)
            )

            IconButton(
                onClick = {
                    val nuevo = if (valor == 0.0) minimo else valor + paso
                    onCambio(nuevo.coerceAtMost(maximo))
                },
                enabled = valor < maximo,
                modifier = Modifier.size(34.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) { Icon(Icons.Filled.Add, contentDescription = "Mas", modifier = Modifier.size(18.dp)) }
        }
    }
}
