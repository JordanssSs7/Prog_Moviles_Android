package com.reyes.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyes.clinicasaludplus.ui.screens.auth.LoginScreen
import com.reyes.clinicasaludplus.ui.screens.auth.RegistroScreen
import com.reyes.clinicasaludplus.ui.screens.auth.SplashScreen
import com.reyes.clinicasaludplus.ui.screens.home.HomeScreen
import com.reyes.clinicasaludplus.ui.screens.agendamiento.MedicosScreen
import com.reyes.clinicasaludplus.ui.screens.agendamiento.EspecialidadesScreen
import com.reyes.clinicasaludplus.ui.screens.agendamiento.FechaHoraScreen
import com.reyes.clinicasaludplus.ui.screens.agendamiento.CitaExitosaScreen
import com.reyes.clinicasaludplus.ui.screens.agendamiento.ConfirmarCitaScreen
import com.reyes.clinicasaludplus.ui.screens.perfil.PerfilScreen
import com.reyes.clinicasaludplus.ui.screens.citas.MisCitasScreen

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

        composable(Rutas.Especialidades.ruta) {
            EspecialidadesScreen(
                alSeleccionarEspecialidad = { espId ->
                    navController.navigate(Rutas.Medicos.crearRuta(espId))
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.Medicos.ruta
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getString("especialidadId") ?: ""
            MedicosScreen(
                especialidadId = especialidadId,
                alSeleccionarMedico = { medId ->
                    navController.navigate(Rutas.FechaHora.crearRuta(medId))
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(route = Rutas.FechaHora.ruta) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
            FechaHoraScreen(
                medicoId = medicoId,
                alContinuar = { fecha, hora ->
                    navController.navigate(Rutas.ConfirmarCita.crearRuta(medicoId, fecha, hora))
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(route = Rutas.ConfirmarCita.ruta) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getString("medicoId") ?: ""
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""

            ConfirmarCitaScreen(
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                alConfirmarExitoso = {
                    navController.navigate(Rutas.CitaExitosa.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = false }
                    }
                },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.CitaExitosa.ruta) {
            CitaExitosaScreen(
                alIrAInicio = {
                    navController.navigate(Rutas.Home.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = true }
                    }
                },
                alIrAMisCitas = {
                    navController.navigate(Rutas.MisCitas.ruta) {
                        popUpTo(Rutas.Home.ruta) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.MisCitas.ruta) {
            MisCitasScreen(
                alIrAAgendar = { navController.navigate(Rutas.Especialidades.ruta) },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                alCerrarSesion = {
                    navController.navigate(Rutas.Splash.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                alVolver = { navController.popBackStack() }
            )
        }

    }
}