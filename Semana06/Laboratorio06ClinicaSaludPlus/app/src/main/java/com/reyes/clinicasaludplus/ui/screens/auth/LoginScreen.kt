package com.reyes.clinicasaludplus.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro

@Composable
fun LoginScreen(
    alIniciarSesionExitoso: () -> Unit,
    alIrARegistro: () -> Unit
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("juan@correo.com") }
    var contrasena by remember { mutableStateOf("123456") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Bienvenido de nuevo",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )
        Text(
            text = "Inicia sesión para continuar",
            fontSize = 14.sp,
            color = TextoGris
        )

        Spacer(modifier = Modifier.height(36.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        BotonPrimario(
            texto = "Iniciar sesión",
            onClick = {
                if (correo.isBlank() || contrasena.isBlank()) {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val exito = Repositorio.iniciarSesion(correo, contrasena)
                    if (exito) {
                        Toast.makeText(context, "Sesión iniciada con éxito", Toast.LENGTH_SHORT).show()
                        alIniciarSesionExitoso()
                    } else {
                        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = alIrARegistro) {
            Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}