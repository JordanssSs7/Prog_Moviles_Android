package com.reyes.lavacampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyes.lavacampus.LavaApp
import com.reyes.lavacampus.ui.administrador.ServiciosScreen
import com.reyes.lavacampus.ui.estudiante.SplashScreen
import com.reyes.lavacampus.viewmodel.LavaViewModelFactory
import com.reyes.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun NavLava() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as LavaApp
    val factory = LavaViewModelFactory(app.repository)

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                navController.navigate(Rutas.SERVICIOS) {
                    popUpTo(Rutas.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            ServiciosScreen(vm = vm, onEditar = { /* TODO (Paso 13) */ })
        }
        // TODO: el resto del grafo se completa en los pasos siguientes
    }
}
