package com.example.clinicasalud.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
fun PerfilMedicoScreen(medicoId: Int, onBackClick: () -> Unit, onAgendarClick: () -> Unit) {
    val nombre = if (medicoId == 1) "Dra. Ana Torres" else if (medicoId == 2) "Dr. Luis Vega" else "Dra. Rosa Díaz"
    val especialidad = if (medicoId == 1) "Cardióloga" else if (medicoId == 2) "Pediatra" else "Dermatóloga"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
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
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = PurpleLight,
                modifier = Modifier.size(96.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(48.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("$especialidad · 12 años exp.", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("4.9 (128 reseñas)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("Especialista con amplia trayectoria y formación profesional.", color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onAgendarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text("Agendar cita", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}