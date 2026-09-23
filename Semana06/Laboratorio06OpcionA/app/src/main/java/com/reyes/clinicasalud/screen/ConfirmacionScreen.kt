package com.example.clinicasalud.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmacionScreen(detalles: String, onVerCitasClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = Color(0xFFE2F4EE),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF198754), modifier = Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("¡Cita agendada!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Dra. Ana Torres", color = Color.Gray, fontSize = 14.sp)
        Text(detalles, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onVerCitasClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE9ECEF)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text("Ver mis citas", fontSize = 16.sp, color = Color.DarkGray)
        }
    }
}