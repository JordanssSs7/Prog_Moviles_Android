package com.reyes.tecsupfit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun InicioFitScreen(onClaseClick: (Int) -> Unit) {
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    val clases = listOf(
        Triple(1, "Yoga funcional", "7:00 am · Sala 2"),
        Triple(2, "Cross Training", "6:00 pm · Sala 1"),
        Triple(3, "Spinning", "7:30 pm · Sala 3")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        Text("Clases disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))

        // LazyRow de filtros ("Hoy" / "Esta semana")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val filtros = listOf("Hoy", "Esta semana")
            items(filtros) { filtro ->
                val isSelected = filtroSeleccionado == filtro
                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    color = if (isSelected) GreenPrimary else GreenLight,
                    modifier = Modifier.clickable { filtroSeleccionado = filtro }
                ) {
                    Text(
                        text = filtro,
                        color = if (isSelected) Color.White else GreenPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn de clases de gimnasio (mínimo 3)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clases) { clase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClaseClick(clase.first) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            color = GreenLight,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = GreenPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(clase.second, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(clase.third, color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}