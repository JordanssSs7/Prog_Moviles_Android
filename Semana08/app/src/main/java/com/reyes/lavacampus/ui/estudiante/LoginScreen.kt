package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.theme.LavaTheme
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Un solo Composable para ambos roles: el color cambia segun la pestana elegida. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: SesionViewModel, onLoginExitoso: (Rol) -> Unit) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Al iniciar sesion con exito se navega segun el rol del usuario
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }

    LavaTheme(rol = rol, oscuro = oscuro) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconoServicio("wash", MaterialTheme.colorScheme.primary, tamano = 72.dp)
            Spacer(Modifier.height(8.dp))
            Text(
                "LavaCampus",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(24.dp))

            val opciones = listOf(Rol.CLIENTE to "Estudiante", Rol.ADMIN to "Administrador")
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                opciones.forEachIndexed { i, (r, texto) ->
                    SegmentedButton(
                        selected = rol == r,
                        onClick = { rol = r; vm.limpiarError() },
                        shape = SegmentedButtonDefaults.itemShape(i, opciones.size)
                    ) { Text(texto) }
                }
            }
            Spacer(Modifier.height(16.dp))

            LavaTextField(
                valor = correo,
                onCambio = { correo = it },
                etiqueta = "Correo",
                teclado = KeyboardType.Email
            )
            Spacer(Modifier.height(12.dp))
            LavaTextField(valor = clave, onCambio = { clave = it }, etiqueta = "Contraseña", esClave = true)

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(20.dp))
            LavaButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol) })
        }
    }
}
