package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.SteveGrande
import com.reyes.minecraft.ui.components.TextoTitulo
import com.reyes.minecraft.ui.components.TipoBoton
import com.reyes.minecraft.ui.components.TituloApp
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo

/** Inicio: bienvenida arriba, Steve grande al centro y los dos botones abajo, uno al lado del otro. */
@Composable
fun BienvenidaScreen(onIniciarSesion: () -> Unit, onRegistrarse: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .padding(horizontal = 28.dp, vertical = 24.dp),
    ) {
        TextoTitulo("Bienvenidos a", 26.sp, VerdeMedio)
        TituloApp(Modifier.padding(top = 6.dp))
        Text(
            "Todos los ítems y bloques del juego,\na un toque de distancia.",
            color = VerdeOscuro,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 10.dp),
        )

        // Steve ocupa todo el espacio libre del centro, así resalta en la pantalla.
        Box(Modifier.weight(1f).fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
            SteveGrande(Modifier.fillMaxSize())
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            BotonApp("Iniciar sesión", onIniciarSesion, Modifier.weight(1f))
            BotonApp("Registrarse", onRegistrarse, Modifier.weight(1f), tipo = TipoBoton.SECUNDARIO)
        }
    }
}
