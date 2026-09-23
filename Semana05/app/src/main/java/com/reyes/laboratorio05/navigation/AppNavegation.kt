package com.reyes.laboratorio05.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reyes.laboratorio05.screens.DetailScreen
import com.reyes.laboratorio05.screens.HomeScreen
import com.reyes.laboratorio05.screens.ListScreen
import com.reyes.laboratorio05.screens.ProfileScreen

@Composable
fun AppNavigation() {
    // rememberNavController crea y mantiene la instancia de navegación
    val navController = rememberNavController()

    // NavHost actúa como contenedor del grafo de rutas
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.List.route) {
            ListScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // Configuración de ruta dinámica con parámetro tipado Int
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(navController, itemId)
        }
    }
}