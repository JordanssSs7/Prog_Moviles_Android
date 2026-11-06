package com.reyes.firebase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(onLoginOk: () -> Unit, onGoRegister: () -> Unit, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var info by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding()
            .padding(horizontal = 28.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(40.dp))
        Text("Hola,\nInicia sesión.", color = Marino, fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(12.dp))
        EnlaceDoble("Si eres nuevo", "Crear cuenta", onGoRegister)
        Spacer(Modifier.height(28.dp))
        CampoPlopp(email, { email = it }, "Correo", destacado = true, tipo = KeyboardType.Email)
        Spacer(Modifier.height(14.dp))
        CampoPlopp(password, { password = it }, "Contraseña", esPassword = true)
        Spacer(Modifier.height(8.dp))
        EnlaceDoble("¿Olvidaste tu contraseña?", "Restablecer") {
            if (email.isBlank()) {
                error = "Escribe tu correo para restablecer la contraseña"; info = null
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.trim())
                    .addOnCompleteListener { t ->
                        if (t.isSuccessful) { info = "Te enviamos un correo para restablecerla"; error = null }
                        else { error = t.exception?.message ?: "No se pudo enviar el correo"; info = null }
                    }
            }
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp) }
        info?.let { Text(it, color = Marino, fontSize = 13.sp) }
        Spacer(Modifier.height(28.dp))
        BotonPlopp(if (loading) "Ingresando..." else "Iniciar sesión", enabled = !loading) {
            if (email.isBlank() || password.isBlank()) {
                error = "Completa todos los campos"; info = null; return@BotonPlopp
            }
            loading = true; error = null; info = null
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { t ->
                    loading = false
                    if (t.isSuccessful) onLoginOk() else error = t.exception?.message ?: "Error al iniciar sesión"
                }
        }
        Text(
            "Volver", color = GrisTexto, fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable(onClick = onBack).padding(16.dp)
        )
    }
}
