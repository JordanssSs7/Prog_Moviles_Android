package com.reyes.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reyes.clinicasalud.screen.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicaSaludApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaSaludApp() {
    val navController = rememberNavController()
    val drawerNavigationState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerNavigationState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = PurplePrimary,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("JP", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Juan Pérez", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Paciente", fontSize = 14.sp, color = Color.Gray)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerNavigationState.close() }
                        navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = false,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerNavigationState.close() }
                        navController.navigate("mis_citas")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = false,
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerNavigationState.close() }
                        navController.navigate("historial")
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerNavigationState.close() }
                        navController.navigate("perfil")
                    }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "inicio") {
            composable("inicio") {
                InicioScreen(
                    onMenuClick = { scope.launch { drawerNavigationState.open() } },
                    onMedicoClick = { medicoId ->
                        navController.navigate("perfil_medico/$medicoId")
                    }
                )
            }

            composable(
                route = "perfil_medico/{medicoId}",
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
                PerfilMedicoScreen(
                    medicoId = medicoId,
                    onBackClick = { navController.popBackStack() },
                    onAgendarClick = {
                        navController.navigate("agendar_cita")
                    }
                )
            }

            composable("agendar_cita") {
                AgendarCitaScreen(
                    onBackClick = { navController.popBackStack() },
                    onConfirmar = { fecha, hora ->
                        navController.navigate("confirmacion/$fecha, $hora")
                    }
                )
            }

            composable("confirmacion/{detalles}") { backStackEntry ->
                val detalles = backStackEntry.arguments?.getString("detalles") ?: ""
                ConfirmacionScreen(
                    detalles = detalles,
                    onVerCitasClick = {
                        navController.navigate("mis_citas") {
                            popUpTo("inicio")
                        }
                    }
                )
            }

            composable("mis_citas") {
                MisCitasScreen()
            }

            composable("historial") {
                Scaffold(topBar = { TopAppBar(title = { Text("Historial médico") }) }) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("Aquí se mostrará tu historial médico.", color = Color.Gray)
                    }
                }
            }

            composable("perfil") {
                Scaffold(topBar = { TopAppBar(title = { Text("Perfil") }) }) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("Configuración y datos de perfil.", color = Color.Gray)
                    }
                }
            }
        }
    }
}