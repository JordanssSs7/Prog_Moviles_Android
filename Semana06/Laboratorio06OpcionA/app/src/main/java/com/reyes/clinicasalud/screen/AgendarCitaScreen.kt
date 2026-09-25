package com.reyes.clinicasalud.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(onBackClick: () -> Unit, onConfirmar: (String, String) -> Unit) {
    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00", "10:30", "3:00")

    var fechaSeleccionada by remember { mutableStateOf("Vie 27") }
    var horaSeleccionada by remember { mutableStateOf("10:30") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Selecciona fecha", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                fechas.forEach { fecha ->
                    val isSelected = fechaSeleccionada == fecha
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        color = if (isSelected) PurplePrimary else PurpleLight,
                        modifier = Modifier
                            .clickable { fechaSeleccionada = fecha }
                            .width(70.dp)
                            .height(60.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(fecha.split(" ")[0], fontSize = 12.sp, color = if (isSelected) Color.White else Color.Gray)
                            Text(fecha.split(" ")[1], fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Selecciona hora", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                horas.forEach { hora ->
                    val isSelected = horaSeleccionada == hora
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        color = if (isSelected) PurplePrimary else PurpleLight,
                        modifier = Modifier
                            .clickable { horaSeleccionada = hora }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = hora,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onConfirmar(fechaSeleccionada, horaSeleccionada) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text("Confirmar cita", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}