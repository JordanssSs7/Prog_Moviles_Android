package com.reyes.clinicasaludplus.ui.screens.auth

import com.reyes.clinicasaludplus.ui.theme.Tam
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.R
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.components.LogoSaludPlus
import com.reyes.clinicasaludplus.ui.theme.SlateTexto

private val FondoSplash = Color(0xFFFAF8F2)

@Composable
fun SplashScreen(
    alIrARegistro: () -> Unit,
    alIrALogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSplash)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        LogoSaludPlus()

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Clínica",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = NavyTitulo
        )
        Text(
            text = "SaludPlus",
            fontSize = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyTitulo,
            lineHeight = 46.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tu salud, nuestra prioridad",
            fontSize = Tam.Subtitulo,
            color = SlateTexto
        )

        // Ilustración a todo el ancho sobre nubes de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .drawBehind { dibujarNubes() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_doctor_splash_t),
                contentDescription = "Doctor SaludPlus",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.BottomCenter
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BotonPrimario(
                texto = "Comenzar",
                onClick = alIrARegistro
            )
            TextButton(
                onClick = alIrALogin,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Ya tengo una cuenta",
                    color = VerdePrimario,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = Tam.Cuerpo
                )
            }
        }
    }
}

// Una nube = varios círculos y óvalos superpuestos
private fun DrawScope.nube(cx: Float, cy: Float, ancho: Float, color: Color) {
    val alto = ancho * 0.42f
    drawOval(color, Offset(cx - ancho / 2, cy - alto / 2 + alto * 0.18f), Size(ancho, alto * 0.82f))
    drawCircle(color, alto * 0.50f, Offset(cx - ancho * 0.20f, cy))
    drawCircle(color, alto * 0.64f, Offset(cx + ancho * 0.05f, cy - alto * 0.14f))
    drawCircle(color, alto * 0.46f, Offset(cx + ancho * 0.28f, cy + alto * 0.02f))
}

// Nubes suaves de fondo (azul muy claro) detrás del doctor
private fun DrawScope.dibujarNubes() {
    val w = size.width
    val h = size.height
    val claro = Color(0xFFE6E2D6)
    val medio = Color(0xFFE6E2D6)
    nube(w * 0.80f, h * 0.20f, w * 0.62f, claro)
    nube(w * 0.18f, h * 0.30f, w * 0.50f, claro)
    nube(w * 0.52f, h * 0.36f, w * 0.80f, medio.copy(alpha = 0.65f))
    nube(w * 0.14f, h * 0.62f, w * 0.44f, medio.copy(alpha = 0.60f))
    nube(w * 0.90f, h * 0.55f, w * 0.46f, medio.copy(alpha = 0.60f))
    nube(w * 0.50f, h * 0.88f, w * 0.95f, claro)
}
