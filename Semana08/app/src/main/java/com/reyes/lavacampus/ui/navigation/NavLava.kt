package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.administrador.ServiciosScreen
import com.reyes.lavacampus.ui.estudiante.InicioScreen
import com.reyes.lavacampus.ui.estudiante.LoginScreen
import com.reyes.lavacampus.ui.estudiante.SplashScreen
import com.reyes.lavacampus.viewmodel.InicioViewModel
import com.reyes.lavacampus.viewmodel.LavaViewModelFactory
import com.reyes.lavacampus.viewmodel.ServiciosViewModel
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Pantalla principal de cada perfil. */
private fun NavHostController.irAPrincipal(rol: Rol) {
    navigate(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO) {
        popUpTo(0) { inclusive = true }
    }
}

@Composable
fun NavLava(sesion: SesionViewModel, factory: LavaViewModelFactory) {
    val navController = rememberNavController()
    val sesionLista by sesion.sesionLista.collectAsState()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(sesionLista = sesionLista, onTerminar = {
                val usuario = sesion.usuarioActual.value
                if (usuario != null) {
                    // Ya hay perfil guardado: salta el login
                    navController.irAPrincipal(Rol.valueOf(usuario.rol))
                } else {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(vm = sesion, onLoginExitoso = { rol -> navController.irAPrincipal(rol) })
        }
        // Destinos temporales: se reemplazan en el Paso 7 (Inicio) y el Paso 11 (Panel)
        composable(Rutas.INICIO) {
            val usuario by sesion.usuarioActual.collectAsState()
            usuario?.let { u ->
                // Este ViewModel recibe al estudiante actual, por eso usa su propia fabrica
                val vm: InicioViewModel = viewModel(
                    key = "inicio-${u.id}",
                    factory = viewModelFactory { initializer { InicioViewModel(factory.repo, u) } }
                )
                InicioScreen(
                    vm = vm,
                    onNuevoPedido = { /* TODO (Paso 8) */ },
                    onVerPedido = { /* TODO (Paso 9) */ },
                    onMisPedidos = { /* TODO (Paso 10) */ },
                    onTarifas = { /* TODO (Paso 10) */ }
                )
            }
        }
        composable(Rutas.PANEL) { Marcador("Panel del administrador (Paso 11)") }
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            ServiciosScreen(vm = vm, onEditar = { /* TODO (Paso 13) */ })
        }
        // TODO: el resto del grafo se completa en los pasos siguientes
    }
}

@Composable
private fun Marcador(texto: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(texto) }
}
