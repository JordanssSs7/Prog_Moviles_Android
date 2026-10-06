package com.reyes.clinicasaludplus.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun PerfilScreen(
    alCerrarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    Scaffold(
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Mi Perfil",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FondoGris)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AzulClaro),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AzulPrimario,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = usuario?.nombreCompleto ?: "Paciente",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
            Text(
                text = usuario?.correo ?: "",
                fontSize = 13.sp,
                color = TextoGris
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Blanco)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDatoPerfil(
                        icono = Icons.Default.Person,
                        etiqueta = "Nombre completo",
                        valor = usuario?.nombreCompleto ?: "-"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)
                    FilaDatoPerfil(
                        icono = Icons.Default.Email,
                        etiqueta = "Correo registrado",
                        valor = usuario?.correo ?: "-"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeGris)
                    FilaDatoPerfil(
                        icono = Icons.Default.Phone,
                        etiqueta = "Teléfono de contacto",
                        valor = usuario?.telefono ?: "-"
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    alCerrarSesion()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RojoAlerta.copy(alpha = 0.1f),
                    contentColor = RojoAlerta
                )
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cerrar sesión", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun FilaDatoPerfil(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(etiqueta, fontSize = 11.sp, color = TextoGris)
            Text(valor, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextoOscuro)
        }
    }
}