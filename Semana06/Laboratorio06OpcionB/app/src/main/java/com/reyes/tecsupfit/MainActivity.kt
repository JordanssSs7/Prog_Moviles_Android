package com.reyes.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reyes.tecsupfit.screen.*
import com.reyes.tecsupfit.ui.theme.GreenPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecsupFitApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // El bottomBar solo es visible en las 4 pestañas principales
    val showBottomBar = currentRoute in listOf("inicio", "reservas", "rutinas", "perfil")

    Scaffold(
        topBar = {
            if (currentRoute == "inicio") {
                TopAppBar(
                    title = {
                        Column {
                            Text("TECSUP Fit", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Hola, Diego", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Inicio") },
                        selected = currentRoute == "inicio",
                        onClick = { navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } } }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        label = { Text("Reservas") },
                        selected = currentRoute == "reservas",
                        onClick = { navController.navigate("reservas") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text("Rutinas") },
                        selected = currentRoute == "rutinas",
                        onClick = { navController.navigate("rutinas") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Perfil") },
                        selected = currentRoute == "perfil",
                        onClick = { navController.navigate("perfil") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("inicio") {
                InicioFitScreen(
                    onClaseClick = { claseId -> navController.navigate("detalle_clase/$claseId") }
                )
            }
            composable("reservas") { ReservasScreen() }
            composable("rutinas") { RutinasScreen() }
            composable("perfil") { PerfilFitScreen() }

            composable(
                route = "detalle_clase/{claseId}",
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1 //id
                DetalleClaseScreen(
                    claseId = claseId,
                    onBackClick = { navController.popBackStack() },
                    onReservarClick = { navController.navigate("confirmacion") }
                )
            }

            composable("confirmacion") {
                ConfirmacionFitScreen(
                    onVerReservasClick = {
                        navController.navigate("reservas") { popUpTo("inicio") }
                    }
                )
            }
        }
    }
}