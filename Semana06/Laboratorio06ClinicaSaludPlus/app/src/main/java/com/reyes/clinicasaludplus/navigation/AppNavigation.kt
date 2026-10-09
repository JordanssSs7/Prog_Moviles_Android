package com.reyes.clinicasaludplus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.DestinoMenu
import com.reyes.clinicasaludplus.ui.screens.doctores.DoctoresScreen
import com.reyes.clinicasaludplus.ui.screens.sedes.SedeDetalleScreen
import com.reyes.clinicasaludplus.ui.screens.sedes.SedesScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyes.clinicasaludplus.ui.screens.auth.LoginScreen
import com.reyes.clinicasaludplus.ui.screens.auth.RegistroScreen
import com.reyes.clinicasaludplus.ui.screens.auth.SplashScreen
import com.reyes.clinicasaludplus.ui.screens.home.HomeScreen
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
                // Recién registrado: se le lleva a iniciar sesión
                alRegistrarExitoso = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = false }
                        launchSingleTop = true
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
                alIrAPerfil = { navController.navigate(Rutas.Perfil.ruta) },
                alIrAMedico = { medId -> navController.navigate(Rutas.FechaHora.crearRuta(medId)) },
                alNavegar = { navController.irAMenu(it) },
                alCerrarSesion = { navController.cerrarSesion() }
            )
        }

        composable(Rutas.Sedes.ruta) {
            SedesScreen(
                alSeleccionarSede = { sedeId -> navController.navigate(Rutas.SedeDetalle.crearRuta(sedeId)) },
                alNavegar = { navController.irAMenu(it) },
                alIrAInicio = { navController.irAInicio() },
                alCerrarSesion = { navController.cerrarSesion() }
            )
        }

        composable(Rutas.SedeDetalle.ruta) { backStackEntry ->
            val sedeId = backStackEntry.arguments?.getString("sedeId").orEmpty()
            SedeDetalleScreen(
                sedeId = sedeId,
                alAgendar = { navController.navigate(Rutas.Doctores.crearRuta(sedeId = sedeId)) },
                alVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.Doctores.ruta,
            arguments = listOf(
                navArgument("sedeId") { type = NavType.StringType; defaultValue = "" },
                navArgument("especialidadId") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            DoctoresScreen(
                sedeId = backStackEntry.arguments?.getString("sedeId").orEmpty(),
                especialidadId = backStackEntry.arguments?.getString("especialidadId").orEmpty(),
                alSeleccionarMedico = { medId -> navController.navigate(Rutas.FechaHora.crearRuta(medId)) },
                alNavegar = { navController.irAMenu(it) },
                alIrAInicio = { navController.irAInicio() },
                alCerrarSesion = { navController.cerrarSesion() },
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
                alIrAAgendar = { navController.irAMenu(DestinoMenu.Sede) },
                alNavegar = { navController.irAMenu(it) },
                alIrAInicio = { navController.irAInicio() },
                alCerrarSesion = { navController.cerrarSesion() }
            )
        }

        composable(Rutas.Perfil.ruta) {
            PerfilScreen(
                alCerrarSesion = { navController.irASplash() },
                alVolver = { navController.popBackStack() }
            )
        }

    }
}

// Las opciones del menú lateral cuelgan del inicio: al volver atrás se regresa a Inicio
private fun NavHostController.irAMenu(destino: DestinoMenu) {
    val ruta = when (destino) {
        DestinoMenu.Sede -> Rutas.Sedes.ruta
        DestinoMenu.Doctor -> Rutas.Doctores.crearRuta()
        DestinoMenu.Agenda -> Rutas.MisCitas.ruta
    }
    navigate(ruta) {
        popUpTo(Rutas.Home.ruta) { inclusive = false }
        launchSingleTop = true
    }
}

private fun NavHostController.irAInicio() {
    popBackStack(Rutas.Home.ruta, inclusive = false)
}

private fun NavHostController.irASplash() {
    navigate(Rutas.Splash.ruta) {
        popUpTo(0) { inclusive = true }
    }
}

private fun NavHostController.cerrarSesion() {
    Repositorio.cerrarSesion()
    irASplash()
}
