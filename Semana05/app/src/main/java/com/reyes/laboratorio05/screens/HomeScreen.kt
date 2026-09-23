package com.reyes.laboratorio05.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reyes.laboratorio05.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    // Column centra todo con Arrangement.Center vertical y Alignment.CenterHorizontally
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto con estilo headlineMedium
        Text(
            text = "Pantalla Tecsup",
            style = MaterialTheme.typography.headlineMedium
        )

        // Espacio vacío vertical height = 32.dp
        Spacer(modifier = Modifier.height(32.dp))

        // Botón primario de relleno: onClick -> navigate(Screen.List.route)
        Button(
            onClick = { navController.navigate(Screen.List.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver lista de elementos")
        }

        // Espacio vacío vertical height = 12.dp
        Spacer(modifier = Modifier.height(12.dp))

        // Botón secundario solo borde: onClick -> navigate(Screen.Profile.route)
        OutlinedButton(
            onClick = { navController.navigate(Screen.Profile.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mi perfil")
        }
    }
}