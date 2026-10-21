package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** Fila de ajuste con titulo, descripcion opcional y un switch. */
@Composable
fun FilaSwitch(
    titulo: String,
    activo: Boolean,
    onCambio: (Boolean) -> Unit,
    descripcion: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo)
            descripcion?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
        Switch(checked = activo, onCheckedChange = onCambio)
    }
}
