package com.reyes.tecsupfit.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
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
fun DetalleClaseScreen(claseId: Int, onBackClick: () -> Unit, onReservarClick: () -> Unit) {
    val nombreClase = if (claseId == 1) "Yoga funcional" else if (claseId == 2) "Cross Training" else "Spinning"
    val horarioClase = if (claseId == 1) "7:00 am · Sala 2" else if (claseId == 2) "6:00 pm · Sala 1" else "7:30 pm · Sala 3"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                color = GreenLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(64.dp))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(nombreClase, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(horarioClase, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Entrenamiento enfocado en resistencia y acondicionamiento físico general. Cupos limitados.", color = Color.DarkGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("8 de 12 cupos disponibles", fontWeight = FontWeight.Bold, color = GreenPrimary, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onReservarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text("Reservar cupo", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}