package com.reyes.clinicasaludplus.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
            .statusBarsPadding() // Evita que se solape con la cámara o barra de estado
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        // --- 1. LOGO DE LA CLÍNICA (Cruz con corazón compuesto) ---
        Box(
            modifier = Modifier.size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            // Fondo de la cruz médica (cápsula vertical y horizontal)
            Box(
                modifier = Modifier
                    .size(width = 30.dp, height = 70.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF2563EB))
            )
            Box(
                modifier = Modifier
                    .size(width = 70.dp, height = 30.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF2563EB))
            )
            // Detalle superior derecho en celeste característico
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF60A5FA).copy(alpha = 0.85f))
            )
            // Corazón blanco central
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Corazón",
                tint = Blanco,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. TEXTOS INSTITUCIONALES ---
        Text(
            text = "Clínica",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E3A8A)
        )
        Text(
            text = "SaludPlus",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E40AF)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tu salud, nuestra prioridad",
            fontSize = 14.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- 3. ILUSTRACIÓN MÉDICA CENTRADA ---
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
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f),
                contentScale = ContentScale.Fit
            )
        }

        // --- 4. BOTONES INFERIORES ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonPrimario(
                texto = "Comenzar",
                onClick = alIrARegistro
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = alIrALogin) {
                Text(
                    text = "Ya tengo una cuenta",
                    color = Color(0xFF2563EB),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}