package com.reyes.tecsupfit.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen() {
    Scaffold(topBar = { TopAppBar(title = { Text("Rutinas") }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text("Sección de rutinas asignadas.", color = Color.Gray)
        }
    }
}