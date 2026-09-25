package com.reyes.clinicasalud.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PurplePrimary = Color(0xFF5A2E83)
val PurpleLight = Color(0xFFF3EDF7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onMenuClick: () -> Unit,
    onMedicoClick: (Int) -> Unit
) {
    var especialidadSeleccionada by remember { mutableStateOf("Cardiología") }

    val medicos = listOf(
        Triple(1, "Dra. Ana Torres", "Cardióloga" to "4.9"),
        Triple(2, "Dr. Luis Vega", "Pediatra" to "4.7"),
        Triple(3, "Dra. Rosa Díaz", "Dermatóloga" to "4.8")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Hola, Juan", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurplePrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9F9F9))
                .padding(16.dp)
        ) {
            Text("Especialidades", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val especialidades = listOf("Cardiología", "Pediatría", "Dermatología")
                items(especialidades) { esp ->
                    val isSelected = especialidadSeleccionada == esp
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        color = if (isSelected) PurplePrimary else PurpleLight,
                        modifier = Modifier.clickable { especialidadSeleccionada = esp }
                    ) {
                        Text(
                            text = esp,
                            color = if (isSelected) Color.White else PurplePrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Médicos disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicos) { medico ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMedicoClick(medico.first) },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = androidx.compose.foundation.shape.CircleShape,
                                color = PurpleLight,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(medico.second, fontWeight = FontWeight.Bold)
                                Text(medico.third.first, color = Color.Gray, fontSize = 14.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(medico.third.second, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}