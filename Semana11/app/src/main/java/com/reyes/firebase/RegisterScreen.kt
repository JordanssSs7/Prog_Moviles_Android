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
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun RegisterScreen(onRegisterOk: () -> Unit, onGoLogin: () -> Unit, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).imePadding()
            .padding(horizontal = 28.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(40.dp))
        Text("Hola,\nCrea tu cuenta.", color = Marino, fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(12.dp))
        EnlaceDoble("Si ya tienes cuenta", "Iniciar sesión", onGoLogin)
        Spacer(Modifier.height(28.dp))
        CampoPlopp(nombre, { nombre = it }, "Tu nombre", destacado = true)
        Spacer(Modifier.height(14.dp))
        CampoPlopp(email, { email = it }, "Correo", tipo = KeyboardType.Email)
        Spacer(Modifier.height(14.dp))
        CampoPlopp(password, { password = it }, "Contraseña (mín. 6)", esPassword = true)
        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }
        Spacer(Modifier.height(28.dp))
        BotonPlopp(if (loading) "Registrando..." else "Registrarme", enabled = !loading) {
            when {
                nombre.isBlank() || email.isBlank() || password.isBlank() -> { error = "Completa todos los campos"; return@BotonPlopp }
                password.length < 6 -> { error = "La contraseña debe tener al menos 6 caracteres"; return@BotonPlopp }
            }
            loading = true; error = null
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { t ->
                    if (t.isSuccessful) {
                        t.result.user?.updateProfile(userProfileChangeRequest { displayName = nombre.trim() })
                            ?.addOnCompleteListener { loading = false; onRegisterOk() }
                            ?: run { loading = false; onRegisterOk() }
                    } else {
                        loading = false
                        error = t.exception?.message ?: "Error al registrar"
                    }
                }
        }
        Text(
            "Volver", color = GrisTexto, fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable(onClick = onBack).padding(16.dp)
        )
    }
}
