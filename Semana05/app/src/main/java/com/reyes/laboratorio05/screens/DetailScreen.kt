package com.reyes.laboratorio05.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val MoradoPrincipal = Color(0xFF6750A4)
private val MoradoOscuro = Color(0xFF4A3479)
private val FondoLila = Color(0xFFF3EDF7)
private val GrisOscuroCard = Color(0xFFE0E0E0)
private val GrisTexto = Color(0xFF79747E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, alumnoId: Int) {
    val alumno = listaAlumnosMock.find { it.id == alumnoId }

    if (alumno == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Alumno no encontrado", color = GrisTexto)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
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
        containerColor = FondoLila
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Fondo Superior MoradoOscuro con Avatar superpuesto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(
                            color = MoradoOscuro,
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                )

                // Avatar circular de 110.dp en el borde inferior
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.BottomCenter)
                        .background(Color.LightGray, CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(66.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y Carrera
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = alumno.nombre,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = alumno.carrera,
                    fontSize = 14.sp,
                    color = MoradoPrincipal,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card Info con Biografía DENTRO y fondo GrisOscuroCard
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GrisOscuroCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailInfoRow(
                        icon = Icons.Filled.Badge,
                        label = "ID Estudiante",
                        value = alumno.codigo
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoRow(
                        icon = Icons.Filled.Email,
                        label = "Correo Electrónico",
                        value = alumno.correo
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoRow(
                        icon = Icons.Filled.School,
                        label = "Facultad / Programa",
                        value = alumno.carrera
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFFCCCCCC)
                    )

                    Text(
                        text = "Biografía",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Estudiante destacado del programa académico de ${alumno.carrera}. Registrado oficialmente en el sistema universitario con el código ${alumno.codigo}.",
                        fontSize = 14.sp,
                        color = GrisTexto,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DetailInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MoradoPrincipal,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, color = GrisTexto, fontSize = 11.sp)
            Text(text = value, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
