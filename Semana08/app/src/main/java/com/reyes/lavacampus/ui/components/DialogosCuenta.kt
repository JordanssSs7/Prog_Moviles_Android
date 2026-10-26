package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dialogo para cambiar el nombre de la cuenta. [onGuardar] recibe el nombre y una funcion a la que
 * se avisa el resultado (mensaje de error o null si salio bien).
 */
@Composable
fun EditarPerfilDialog(
    nombreActual: String,
    onGuardar: (nombre: String, alTerminar: (String?) -> Unit) -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreActual) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Editar perfil") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LavaTextField(valor = nombre, onCambio = { nombre = it; error = null }, etiqueta = "Nombre completo")
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                Text(
                    "El código y el correo no se pueden cambiar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(nombre) { mensaje -> if (mensaje == null) onCerrar() else error = mensaje }
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}

/** Dialogo para cambiar la contrasena: pide la actual, la nueva (con indicador de fuerza) y su confirmacion. */
@Composable
fun CambiarClaveDialog(
    onCambiar: (actual: String, nueva: String, confirmacion: String, alTerminar: (String?) -> Unit) -> Unit,
    onCerrar: () -> Unit
) {
    var actual by remember { mutableStateOf("") }
    var nueva by remember { mutableStateOf("") }
    var confirmacion by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text("Cambiar contraseña") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LavaTextField(valor = actual, onCambio = { actual = it; error = null }, etiqueta = "Contraseña actual", esClave = true)
                LavaTextField(valor = nueva, onCambio = { nueva = it; error = null }, etiqueta = "Contraseña nueva", esClave = true)
                if (nueva.isNotEmpty()) IndicadorClave(nueva, modifier = Modifier)
                LavaTextField(valor = confirmacion, onCambio = { confirmacion = it; error = null }, etiqueta = "Repite la nueva", esClave = true)
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onCambiar(actual, nueva, confirmacion) { mensaje -> if (mensaje == null) onCerrar() else error = mensaje }
            }) { Text("Cambiar") }
        },
        dismissButton = { TextButton(onClick = onCerrar) { Text("Cancelar") } }
    )
}
