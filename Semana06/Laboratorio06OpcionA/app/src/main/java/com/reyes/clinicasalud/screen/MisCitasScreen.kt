package com.example.clinicasalud.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen() {
    val listaMisCitas = listOf(
        Triple("Dra. Ana Torres", "Viernes 27, 10:30 am", "Confirmada"),
        Triple("Dr. Luis Vega", "Miércoles 15, 3:00 pm", "Completada")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaMisCitas) { cita ->
                val isConfirmada = cita.third == "Confirmada"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F5))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(40.dp)
                                .background(if (isConfirmada) PurplePrimary else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(cita.first, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(cita.second, color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                color = if (isConfirmada) Color(0xFFE2F4EE) else Color(0xFFE9ECEF)
                            ) {
                                Text(
                                    text = cita.third,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                    color = if (isConfirmada) Color(0xFF0F5132) else Color.DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}