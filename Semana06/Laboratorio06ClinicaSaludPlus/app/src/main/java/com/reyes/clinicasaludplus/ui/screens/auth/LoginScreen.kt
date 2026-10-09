package com.reyes.clinicasaludplus.ui.screens.auth

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.CampoFormulario
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro
import com.reyes.clinicasaludplus.util.Validaciones

@Composable
fun LoginScreen(
    alIniciarSesionExitoso: () -> Unit,
    alIrARegistro: () -> Unit
) {
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var identificador by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var intentoEnviar by rememberSaveable { mutableStateOf(false) }
    var errorCredenciales by rememberSaveable { mutableStateOf(false) }

    val identificadorLimpio = identificador.trim()
    val errorIdentificador = when {
        identificadorLimpio.isEmpty() -> "Ingresa tu correo o teléfono"
        identificadorLimpio.all { it.isDigit() } && Validaciones.errorTelefono(identificadorLimpio) != null ->
            Validaciones.errorTelefono(identificadorLimpio)
        identificadorLimpio.contains("@") && Validaciones.errorCorreoOpcional(identificadorLimpio) != null ->
            Validaciones.errorCorreoOpcional(identificadorLimpio)
        else -> null
    }
    val errorContrasena = when {
        contrasena.isEmpty() -> "Ingresa tu contraseña"
        errorCredenciales -> "Correo/teléfono o contraseña incorrectos"
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { dispatcher?.onBackPressed() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoOscuro
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Iniciar sesión",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Ingresa para ver y agendar tus citas",
                fontSize = 14.sp,
                color = TextoGris
            )

            Spacer(modifier = Modifier.height(32.dp))

            CampoFormulario(
                etiqueta = "Correo o teléfono",
                valor = identificador,
                onValueChange = {
                    identificador = it.replace(" ", "")
                    errorCredenciales = false
                },
                icono = Icons.Default.Person,
                error = if (intentoEnviar) errorIdentificador else null,
                keyboardType = KeyboardType.Email,
                maxLargo = 60
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                etiqueta = "Contraseña",
                valor = contrasena,
                onValueChange = {
                    contrasena = it.replace(" ", "")
                    errorCredenciales = false
                },
                icono = Icons.Default.Lock,
                error = if (intentoEnviar) errorContrasena else null,
                esPassword = true,
                keyboardType = KeyboardType.Password,
                maxLargo = Validaciones.MAX_CONTRASENA
            )

            Spacer(modifier = Modifier.height(32.dp))

            BotonPrimario(
                texto = "Iniciar sesión",
                onClick = {
                    intentoEnviar = true
                    if (errorIdentificador != null || contrasena.isEmpty()) {
                        Toast.makeText(context, "Completa los campos correctamente", Toast.LENGTH_SHORT).show()
                    } else if (Repositorio.iniciarSesion(identificador, contrasena)) {
                        Toast.makeText(context, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show()
                        alIniciarSesionExitoso()
                    } else {
                        errorCredenciales = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = alIrARegistro) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí",
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
