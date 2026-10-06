package com.reyes.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyes.clinicasaludplus.ui.screens.auth.LoginScreen
import com.reyes.clinicasaludplus.ui.screens.auth.RegistroScreen
import com.reyes.clinicasaludplus.ui.screens.auth.SplashScreen
import com.reyes.clinicasaludplus.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                alIrARegistro = { navController.navigate(Rutas.Registro.ruta) },
                alIrALogin = { navController.navigate(Rutas.Login.ruta) }
            )
        }

        composable(Rutas.Registro.ruta) {
            RegistroScreen(
                alRegistrarExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                alIrALogin = { navController.navigate(Rutas.Login.ruta) }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                alIniciarSesionExitoso = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                },
                alIrARegistro = { navController.navigate(Rutas.Registro.ruta) }
            )
        }

        composable(Rutas.Home.ruta) {
            HomeScreen(
                alIrAAgendar = { navController.navigate(Rutas.Especialidades.ruta) },
                alIrAMisCitas = { navController.navigate(Rutas.MisCitas.ruta) },
                alIrAPerfil = { navController.navigate(Rutas.Perfil.ruta) }
            )
        }

    }
}