package com.reyes.clinicasaludplus.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.R
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun SplashScreen(
    alIrARegistro: () -> Unit,
    alIrALogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Encabezado institucional
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AzulPrimario),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo SaludPlus",
                    tint = Blanco,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Clínica",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = TextoGris
            )
            Text(
                text = "SaludPlus",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AzulPrimario
            )
            Text(
                text = "Tu salud, nuestra prioridad",
                fontSize = 13.sp,
                color = TextoGris
            )
        }

        // Imagen del doctor centrada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_doctor_splash),
                contentDescription = "Doctor SaludPlus",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.85f),
                contentScale = ContentScale.Fit
            )
        }

        // Botones de acción
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonPrimario(
                texto = "Comenzar",
                onClick = alIrARegistro
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = alIrALogin) {
                Text(
                    text = "¿Ya tienes cuenta? Iniciar sesión",
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}