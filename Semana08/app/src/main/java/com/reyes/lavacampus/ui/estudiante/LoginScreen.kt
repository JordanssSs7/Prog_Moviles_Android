package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.BotonCircular
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.LogoLava
import com.reyes.lavacampus.ui.theme.LavaTheme
import com.reyes.lavacampus.ui.theme.degradadoMarca
import com.reyes.lavacampus.viewmodel.SesionViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon

/**
 * Un solo Composable para ambos roles. Encabezado con degradado de marca y una hoja blanca
 * con esquinas redondeadas; el color cambia segun el perfil elegido.
 */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    rolInicial: Rol,
    onLoginExitoso: (Rol) -> Unit,
    onVolver: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    var rol by rememberSaveable { mutableStateOf(rolInicial) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Al iniciar sesion con exito se navega segun el rol del usuario
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }

    LavaTheme(rol = rol, oscuro = oscuro) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            // Encabezado con degradado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(degradadoMarca(rol))
            ) {
                Column(
                    modifier = Modifier.statusBarsPadding().padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    BotonCircular(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                    Spacer(Modifier.height(20.dp))
                    LogoLava(tamano = 30.dp)
                    Spacer(Modifier.height(18.dp))
                    Text(
                        "Bienvenido de nuevo",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        if (rol == Rol.ADMIN) "Ingresa para gestionar la lavandería"
                        else "Ingresa para seguir tus pedidos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Hoja blanca que sube sobre el encabezado
            Surface(
                modifier = Modifier.fillMaxSize().padding(top = 250.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SelectorPerfil(rol) { rol = it; vm.limpiarError() }

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
                    LavaButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol) })

                    Text(
                        text = if (rol == Rol.ADMIN) "Cuenta de práctica: admin@lavacampus.test · admin"
                        else "Cuenta de práctica: ana@correo.test · 1234",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** Selector Estudiante / Administrador en forma de pastilla. */
@Composable
private fun SelectorPerfil(rol: Rol, onElegir: (Rol) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(4.dp)
    ) {
        listOf(Rol.CLIENTE to "Estudiante", Rol.ADMIN to "Administrador").forEach { (r, texto) ->
            val elegido = rol == r
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(if (elegido) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable { onElegir(r) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (elegido) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
