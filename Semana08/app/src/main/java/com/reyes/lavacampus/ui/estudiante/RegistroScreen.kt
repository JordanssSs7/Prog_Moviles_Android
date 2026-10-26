package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.DIGITOS_CODIGO
import com.reyes.lavacampus.ui.components.IndicadorClave
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.PlantillaAcceso
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Crear una cuenta de estudiante. Al terminar muestra la confirmacion y lleva al inicio de sesion. */
@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    onCuentaCreada: () -> Unit,
    onIniciarSesion: () -> Unit,
    onVolver: () -> Unit
) {
    val cuentaCreada by vm.cuentaCreada.collectAsState()
    val error by vm.error.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var codigo by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmacion by rememberSaveable { mutableStateOf("") }

    cuentaCreada?.let { nombreCreado ->
        ConfirmacionRegistroScreen(nombreCreado, onIrALogin = {
            vm.confirmarCuentaCreada()
            onCuentaCreada()
        })
        return
    }
    DisposableEffect(Unit) {
        vm.limpiarError()
        onDispose { }
    }

    PlantillaAcceso(
        titulo = "Crea tu cuenta",
        subtitulo = "Regístrate para hacer tus pedidos",
        onVolver = onVolver
    ) {
        LavaTextField(valor = nombre, onCambio = { nombre = it }, etiqueta = "Nombre completo")
        // El codigo son 6 digitos: el campo solo deja escribir numeros
        LavaTextField(
            valor = codigo,
            onCambio = { codigo = it.filter(Char::isDigit).take(DIGITOS_CODIGO) },
            etiqueta = "Código de estudiante (6 dígitos)",
            teclado = KeyboardType.Number
        )
        LavaTextField(valor = correo, onCambio = { correo = it }, etiqueta = "Correo", teclado = KeyboardType.Email)
        LavaTextField(valor = clave, onCambio = { clave = it }, etiqueta = "Contraseña", esClave = true)
        if (clave.isNotEmpty()) IndicadorClave(clave)
        LavaTextField(valor = confirmacion, onCambio = { confirmacion = it }, etiqueta = "Repite la contraseña", esClave = true)

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        LavaButton(texto = "Crear cuenta", onClick = { vm.registrar(nombre, codigo, correo, clave, confirmacion) })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "¿Ya tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onIniciarSesion) {
                Text("Inicia sesión", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
