package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.EstiloBoton
import com.reyes.lavacampus.ui.components.IlustracionCesto
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LogoLava
import com.reyes.lavacampus.ui.theme.AzulIntermedio
import com.reyes.lavacampus.ui.theme.AzulProfundo
import com.reyes.lavacampus.ui.theme.Tinta

/** Primera pantalla: degradado azul con la ilustracion y los accesos Iniciar sesion y Crear cuenta. */
@Composable
fun BienvenidaScreen(onIniciarSesion: () -> Unit, onCrearCuenta: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Parte alta con degradado que se funde con el blanco
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.64f)
                .background(
                    Brush.verticalGradient(
                        0.0f to AzulProfundo,
                        0.45f to AzulIntermedio,
                        0.85f to Color(0xFFD6E4FF),
                        1.0f to Color.White
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            LogoLava(tamano = 34.dp)

            // La ilustracion es mas ancha que el area disponible para que el cesto se vea grande
            BoxWithConstraints(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                IlustracionCesto(modifier = Modifier.requiredSize(maxWidth * 1.22f))
            }

            Text(
                text = "Tu ropa limpia, sin complicarte.\nEn cualquier momento.",
                style = MaterialTheme.typography.headlineSmall,
                color = Tinta,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))
            LavaButton(texto = "Iniciar sesión", estilo = EstiloBoton.Oscuro, onClick = onIniciarSesion)
            Spacer(Modifier.height(12.dp))
            LavaButton(texto = "Crear cuenta", estilo = EstiloBoton.Suave, onClick = onCrearCuenta)
            Spacer(Modifier.height(12.dp))
        }
    }
}
