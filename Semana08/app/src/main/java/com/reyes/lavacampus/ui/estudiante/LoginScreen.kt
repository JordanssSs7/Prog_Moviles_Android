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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.PlantillaAcceso
import com.reyes.lavacampus.viewmodel.SesionViewModel

/**
 * Un unico inicio de sesion para todos: no se elige el perfil, la cuenta ya dice si es
 * administrador o estudiante y la app lleva a su pantalla principal.
 */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExitoso: (Rol) -> Unit,
    onCrearCuenta: () -> Unit,
    onVolver: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()

    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Al iniciar sesion con exito se navega segun el rol de la cuenta
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }
    // Los errores de otra pantalla (por ejemplo del registro) no deben verse aqui
    DisposableEffect(Unit) {
        vm.limpiarError()
        onDispose { }
    }

    PlantillaAcceso(
        titulo = "Bienvenido de nuevo",
        subtitulo = "Ingresa con tu cuenta",
        onVolver = onVolver
    ) {
        LavaTextField(
            valor = correo,
            onCambio = { correo = it },
            etiqueta = "Correo",
            teclado = KeyboardType.Email
        )
        LavaTextField(valor = clave, onCambio = { clave = it }, etiqueta = "Contraseña", esClave = true)

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(4.dp))
        LavaButton(texto = "Ingresar", onClick = { vm.login(correo, clave) })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "¿No tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onCrearCuenta) {
                Text("Crear cuenta", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
