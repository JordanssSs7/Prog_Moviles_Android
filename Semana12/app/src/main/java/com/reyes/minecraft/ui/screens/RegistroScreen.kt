package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.R
import com.reyes.minecraft.ui.components.AvisoApp
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.BotonAtras
import com.reyes.minecraft.ui.components.CampoApp
import com.reyes.minecraft.ui.components.PanelApp
import com.reyes.minecraft.ui.components.TextoTitulo
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia
import com.reyes.minecraft.ui.viewmodel.SesionViewModel

/** Registro: nombre de jugador, correo y contraseña. Al terminar va al login. */
@Composable
fun RegistroScreen(vm: SesionViewModel, onIrALogin: () -> Unit, onVolver: () -> Unit) {
    val registrando by vm.registrando.collectAsStateWithLifecycle()
    val error by vm.errorRegistro.collectAsStateWithLifecycle()
    var jugador by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White).imePadding()) {
        val alto = maxHeight

        // Arriba: la imagen completa, centrada, sin recortar los bordes del portal.
        Image(
            painter = painterResource(R.drawable.aventura),
            contentDescription = "Steve, Alex y mobs de Minecraft",
            modifier = Modifier
                .fillMaxWidth()
                .height(alto * 0.56f)
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(start = 36.dp, end = 36.dp, top = 14.dp),
            contentScale = ContentScale.Fit,
            alignment = Alignment.TopCenter,
        )

        BotonAtras(
            { vm.limpiarErrores(); onVolver() },
            Modifier.align(Alignment.TopStart).statusBarsPadding().padding(start = 20.dp, top = 12.dp),
        )

        // Abajo: el texto y la tarjeta. Todo lo que está debajo del texto tiene fondo blanco sólido,
        // así la imagen no se ve a los lados de la tarjeta ni por detrás.
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .heightIn(max = alto)
                .verticalScroll(rememberScrollState()),
        ) {
            // El texto arranca sobre un degradado transparente -> blanco, para que se lea bien.
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(0f to Color.Transparent, 0.4f to Color.White, 1f to Color.White))
                    .padding(start = 28.dp, end = 28.dp, top = 72.dp, bottom = 16.dp),
            ) {
                TextoTitulo("¿Eres nuevo?", 52.sp, VerdeProfundo, sombra = VerdeSalvia)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Crea tu cuenta y empieza a explorar",
                    color = VerdeOscuro,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Column(Modifier.fillMaxWidth().background(Color.White).navigationBarsPadding()) {
                PanelApp(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    CampoApp(jugador, { jugador = it }, "Nombre de jugador", Icons.Default.Person)
                    Spacer(Modifier.height(18.dp))
                    CampoApp(correo, { correo = it }, "Correo electrónico", Icons.Default.Email, teclado = KeyboardType.Email)
                    Spacer(Modifier.height(18.dp))
                    CampoApp(clave, { clave = it }, "Contraseña (mínimo 6)", Icons.Default.Lock, esClave = true)
                    Spacer(Modifier.height(18.dp))
                    CampoApp(confirmar, { confirmar = it }, "Repite la contraseña", Icons.Default.Lock, esClave = true)
                    error?.let {
                        Spacer(Modifier.height(18.dp))
                        AvisoApp(it, esError = true)
                    }
                    Spacer(Modifier.height(26.dp))
                    BotonApp(
                        texto = if (registrando) "Creando..." else "Crear cuenta",
                        onClick = { vm.registrar(correo, clave, confirmar, jugador) },
                        enabled = !registrando,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TextButton(onClick = { vm.limpiarErrores(); onIrALogin() }) {
                            Text("¿Ya tienes cuenta? ", color = VerdeMedio, fontSize = 13.sp)
                            Text("Inicia sesión", color = VerdeOscuro, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
