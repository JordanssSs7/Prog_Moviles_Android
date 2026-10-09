package com.reyes.clinicasaludplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.VerdeSecundario

/** Logo: cruz verde oscuro con burbuja verde y corazón. [escala] 1f = 128x116 dp. */
@Composable
fun LogoSaludPlus(escala: Float = 1f, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(width = 128.dp * escala, height = 116.dp * escala)) {
        // Brazo vertical
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(end = 14.dp * escala)
                .size(width = 50.dp * escala, height = 116.dp * escala)
                .clip(RoundedCornerShape(20.dp * escala))
                .background(VerdePrimario)
        )
        // Brazo horizontal
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(width = 112.dp * escala, height = 50.dp * escala)
                .clip(RoundedCornerShape(20.dp * escala))
                .background(VerdePrimario)
        )
        // Burbuja
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(bottom = 26.dp * escala)
                .size(width = 62.dp * escala, height = 56.dp * escala)
                .clip(RoundedCornerShape(24.dp * escala))
                .background(VerdeSecundario)
        )
        // Corazón
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "SaludPlus",
            tint = Blanco,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 4.dp * escala)
                .size(40.dp * escala)
        )
    }
}
