package com.reyes.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Pedido(
    val id: String,
    val productoNombre: String,
    val total: String,
    val estado: String
)

// ----------------- VISTA DE LOGIN -----------------
@Composable
fun LoginScreen(onLoginSuccess: (String, String) -> Unit) {
    var email by remember { mutableStateOf("jordan.reyes@tecsup.edu.pe") }
    var password by remember { mutableStateOf("123456") }
    var errorMsg by remember { mutableStateOf("") }
    val purpleBrand = Color(0xFF5E2E8C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3EAFB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = purpleBrand,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Iniciar Sesión",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = purpleBrand
        )
        Text(
            text = "Accede a TECSUP Store",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMsg = "" },
            label = { Text("Correo institucional") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMsg = "" },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.length >= 4) {
                    onLoginSuccess("Jordan Reyes", email)
                } else {
                    errorMsg = "Ingresa un correo y contraseña válidos"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purpleBrand)
        ) {
            Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ----------------- VISTA DE PERFIL -----------------
@Composable
fun PerfilScreen(
    nombre: String,
    email: String,
    totalFavoritos: Int,
    totalPedidos: Int,
    onCerrarSesion: () -> Unit
) {
    val purpleBrand = Color(0xFF5E2E8C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFFE9DCF8)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JR",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = purpleBrand
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = email, fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9FC))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = "Información académica", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = purpleBrand)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Institución: TECSUP", fontSize = 14.sp)
                Text(text = "Especialidad: Diseño y Desarrollo de Software", fontSize = 14.sp)
                Text(text = "Ciclo: 4to Ciclo", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EAFB))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$totalFavoritos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = purpleBrand)
                    Text(text = "Favoritos", fontSize = 13.sp, color = Color.DarkGray)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$totalPedidos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text(text = "Pedidos", fontSize = 13.sp, color = Color.DarkGray)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Cerrar Sesión")
        }
    }
}

// ----------------- VISTA DE MIS PEDIDOS -----------------
@Composable
fun MisPedidosScreen(pedidos: List<Pedido>) {
    if (pedidos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No has realizado ningún pedido aún", color = Color.Gray, fontSize = 15.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pedidos.size) { idx ->
                val p = pedidos[idx]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7FA))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = p.productoNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Orden: ${p.id}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = p.total, fontWeight = FontWeight.Bold, color = Color(0xFF5E2E8C), fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = if (p.estado == "Entregado") Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = p.estado,
                                    fontSize = 11.sp,
                                    color = if (p.estado == "Entregado") Color(0xFF2E7D32) else Color(0xFFE65100),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                //-
                            }
                        }
                    }
                }
            }
        }
    }
}