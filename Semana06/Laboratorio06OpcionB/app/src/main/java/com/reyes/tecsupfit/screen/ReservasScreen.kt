package com.reyes.tecsupfit.screen

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
import com.reyes.tecsupfit.ui.theme.GreenPrimary
import com.reyes.tecsupfit.ui.theme.GreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen() {
    val reservas = listOf(
        Triple("Cross Training", "Hoy, 6:00 pm", "Confirmada"),
        Triple("Yoga funcional", "Ayer, 7:00 am", "Completada")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis reservas") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservas) { res ->
                val isConfirmada = res.third == "Confirmada"
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
                                .background(if (isConfirmada) GreenPrimary else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(res.first, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(res.second, color = Color.Gray, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                color = if (isConfirmada) GreenLight else Color(0xFFE9ECEF)
                            ) {
                                Text(
                                    text = res.third,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                    color = if (isConfirmada) GreenPrimary else Color.DarkGray,
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