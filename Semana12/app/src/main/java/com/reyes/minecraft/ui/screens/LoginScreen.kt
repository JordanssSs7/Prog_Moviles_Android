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
import com.reyes.minecraft.ui.components.TituloPanel
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia
import com.reyes.minecraft.ui.viewmodel.SesionViewModel

/** [cuentaCreada] llega cuando la persona acaba de registrarse: se muestra un aviso y se rellena el correo. */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    cuentaCreada: String?,
    onRegistrarse: () -> Unit,
    onVolver: () -> Unit,
) {
    val ingresando by vm.ingresando.collectAsStateWithLifecycle()
    val error by vm.errorLogin.collectAsStateWithLifecycle()
    var correo by rememberSaveable { mutableStateOf(cuentaCreada.orEmpty()) }
    var clave by rememberSaveable { mutableStateOf("") }

    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White).imePadding()) {
        val alto = maxHeight

        // Arriba: el creeper, difuminado hacia el blanco para que se funda con el fondo.
        Box(Modifier.fillMaxWidth().height(alto * 0.62f).align(Alignment.TopCenter)) {
            Image(
                painter = painterResource(R.drawable.creeper),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
            )
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(0.55f to Color.Transparent, 1f to Color.White)
                )
            )
        }

        BotonAtras(
            { vm.limpiarErrores(); onVolver() },
            Modifier.align(Alignment.TopStart).statusBarsPadding().padding(start = 20.dp, top = 12.dp),
        )

        // Abajo: el saludo y la tarjeta con los datos.
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .heightIn(max = alto)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
        ) {
            Column(Modifier.padding(start = 28.dp, end = 28.dp, bottom = 16.dp)) {
                TextoTitulo("Bienvenido", 28.sp, VerdeMedio)
                TextoTitulo("de vuelta", 60.sp, VerdeProfundo, sombra = VerdeSalvia)
            }
            PanelApp(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                TituloPanel("Iniciar sesión")
                Spacer(Modifier.height(22.dp))
                if (cuentaCreada != null) {
                    AvisoApp("¡Cuenta creada! Ahora entra con tu correo y tu contraseña.", esError = false)
                    Spacer(Modifier.height(18.dp))
                }
                CampoApp(correo, { correo = it }, "Correo electrónico", Icons.Default.Email, teclado = KeyboardType.Email)
                Spacer(Modifier.height(18.dp))
                CampoApp(clave, { clave = it }, "Contraseña", Icons.Default.Lock, esClave = true)
                error?.let {
                    Spacer(Modifier.height(18.dp))
                    AvisoApp(it, esError = true)
                }
                Spacer(Modifier.height(26.dp))
                BotonApp(
                    texto = if (ingresando) "Entrando..." else "Entrar",
                    onClick = { vm.iniciarSesion(correo, clave) },
                    enabled = !ingresando,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(onClick = { vm.limpiarErrores(); onRegistrarse() }) {
                        Text("¿Eres nuevo en Minecraft Catálogo? ", color = VerdeMedio, fontSize = 13.sp)
                        Text("Regístrate", color = VerdeOscuro, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
