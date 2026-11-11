package com.reyes.minecraft.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.ui.components.ConfirmacionOverlay
import com.reyes.minecraft.ui.screens.BienvenidaScreen
import com.reyes.minecraft.ui.screens.LoginScreen
import com.reyes.minecraft.ui.screens.RegistroScreen
import com.reyes.minecraft.ui.viewmodel.SesionViewModel

private enum class PantallaAuth { BIENVENIDA, LOGIN, REGISTRO }

/** Pantallas sin sesión: bienvenida -> login / registro. Al registrarse, vuelve al login. */
@Composable
fun AuthFlow(vm: SesionViewModel) {
    var pantalla by rememberSaveable { mutableStateOf(PantallaAuth.BIENVENIDA) }
    val cuentaCreada by vm.cuentaCreada.collectAsStateWithLifecycle()
    val confirmacion by vm.confirmacionRegistro.collectAsStateWithLifecycle()

    // Registro exitoso: se manda a la persona al login para que entre con la cuenta nueva.
    LaunchedEffect(cuentaCreada) {
        if (cuentaCreada != null) pantalla = PantallaAuth.LOGIN
    }
    BackHandler(enabled = pantalla != PantallaAuth.BIENVENIDA) { pantalla = PantallaAuth.BIENVENIDA }

    Box {
        AnimatedContent(
            targetState = pantalla,
            transitionSpec = {
                (fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 12 }) togetherWith fadeOut(tween(200))
            },
            label = "auth",
        ) { destino ->
            when (destino) {
                PantallaAuth.BIENVENIDA -> BienvenidaScreen(
                    onIniciarSesion = { pantalla = PantallaAuth.LOGIN },
                    onRegistrarse = { pantalla = PantallaAuth.REGISTRO },
                )
                PantallaAuth.LOGIN -> LoginScreen(
                    vm = vm,
                    cuentaCreada = cuentaCreada,
                    onRegistrarse = { pantalla = PantallaAuth.REGISTRO },
                    onVolver = { pantalla = PantallaAuth.BIENVENIDA },
                )
                PantallaAuth.REGISTRO -> RegistroScreen(
                    vm = vm,
                    onIrALogin = { pantalla = PantallaAuth.LOGIN },
                    onVolver = { pantalla = PantallaAuth.BIENVENIDA },
                )
            }
        }
        if (confirmacion != null) {
            ConfirmacionOverlay(
                titulo = "¡Cuenta creada!",
                mensaje = "Ahora inicia sesión con tu nueva cuenta",
                onTerminar = vm::confirmacionRegistroMostrada,
            )
        }
    }
}
