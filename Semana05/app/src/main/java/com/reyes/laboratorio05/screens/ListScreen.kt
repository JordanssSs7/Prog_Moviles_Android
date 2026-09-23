package com.reyes.laboratorio05.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.reyes.laboratorio05.navigation.Screen

data class Alumno(
    val id: Int,
    val nombre: String,
    val carrera: String,
    val correo: String,
    val codigo: String
)

val listaAlumnosMock = listOf(
    Alumno(1, "Juan León", "Ingeniería de Sistemas", "juan.leon@example.com", "2024-0001"),
    Alumno(2, "Maria Garcia", "Arquitectura", "maria.g@example.com", "2024-0002"),
    Alumno(3, "Carlos Perez", "Medicina", "carlos.p@example.com", "2024-0003"),
    Alumno(4, "Ana Lopez", "Derecho", "ana.l@example.com", "2024-0004"),
    Alumno(5, "Luis Ramirez", "Administración", "luis.r@example.com", "2024-0005")
)

private val MoradoPrincipal = Color(0xFF6750A4)
private val MoradoOscuro = Color(0xFF4A3479)
private val FondoLila = Color(0xFFF3EDF7)
private val FondoPantallaBlanco = Color(0xFFFFFFFF)
private val GrisOscuroCard = Color(0xFFE0E0E0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
                        color = MoradoOscuro,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MoradoOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoLila)
            )
        },
        containerColor = FondoPantallaBlanco
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaAlumnosMock) { alumno ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.Detail.createRoute(alumno.id)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisOscuroCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFCCCCCC), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = alumno.nombre,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                fontSize = 20.sp
                            )
                            Text(
                                text = alumno.carrera,
                                color = MoradoPrincipal,
                                fontSize = 13.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
