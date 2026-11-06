package com.reyes.firebase

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

private enum class Pantalla { BIENVENIDA, LOGIN, REGISTRO, HOME, CURSOS }

@Composable
fun AuthApp() {
    val auth = FirebaseAuth.getInstance()
    var pantalla by remember { mutableStateOf(Pantalla.BIENVENIDA) }
    var abrirNuevo by remember { mutableStateOf(false) }

    BackHandler(enabled = pantalla == Pantalla.LOGIN || pantalla == Pantalla.REGISTRO || pantalla == Pantalla.CURSOS) {
        pantalla = if (pantalla == Pantalla.CURSOS) Pantalla.HOME else Pantalla.BIENVENIDA
    }

    Box(Modifier.fillMaxSize().background(Color.White)) {
        Box(Modifier.fillMaxSize().statusBarsPadding().padding(top = 56.dp)) {
            when (pantalla) {
                Pantalla.BIENVENIDA -> BienvenidaScreen(
                    onRegistro = { pantalla = Pantalla.REGISTRO },
                    onLogin = { pantalla = Pantalla.LOGIN }
                )
                Pantalla.LOGIN -> LoginScreen(
                    onLoginOk = { pantalla = Pantalla.HOME },
                    onGoRegister = { pantalla = Pantalla.REGISTRO },
                    onBack = { pantalla = Pantalla.BIENVENIDA }
                )
                Pantalla.REGISTRO -> RegisterScreen(
                    onRegisterOk = { pantalla = Pantalla.HOME },
                    onGoLogin = { pantalla = Pantalla.LOGIN },
                    onBack = { pantalla = Pantalla.BIENVENIDA }
                )
                Pantalla.HOME -> HomeScreen(
                    onCursos = { nuevo -> abrirNuevo = nuevo; pantalla = Pantalla.CURSOS },
                    onLogout = { auth.signOut(); pantalla = Pantalla.BIENVENIDA }
                )
                Pantalla.CURSOS -> CursosScreen(onBack = { pantalla = Pantalla.HOME }, abrirNuevo = abrirNuevo)
            }
        }

        // Nombre del alumno, siempre visible arriba
        Text(
            text = "Jordan Reyes Saravia",
            color = Marino,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 12.dp)
        )
    }
}
