package com.reyes.clinicasaludplus.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.AzulClaro
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro

@Composable
fun SplashScreen(
    alIrARegistro: () -> Unit,
    alIrALogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = AzulClaro
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = "Logo SaludPlus",
                        tint = AzulPrimario,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Clínica SaludPlus",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario
            )
            Text(
                text = "Tu salud, nuestra prioridad",
                fontSize = 15.sp,
                color = TextoGris
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonPrimario(
                texto = "Comenzar",
                onClick = alIrARegistro
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = alIrALogin) {
                Text(
                    text = "¿Ya tienes cuenta? Iniciar sesión",
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}