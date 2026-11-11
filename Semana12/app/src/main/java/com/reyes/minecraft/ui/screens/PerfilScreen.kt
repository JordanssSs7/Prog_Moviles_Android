package com.reyes.minecraft.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.Totales
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.CabezaJugador
import com.reyes.minecraft.ui.components.TextoTitulo
import com.reyes.minecraft.ui.components.TipoBoton
import com.reyes.minecraft.ui.theme.Dorado
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia

/** Perfil: banner de pasto en píxeles, el jugador de cuerpo entero, sus datos y estadísticas. */
@Composable
fun PerfilScreen(usuario: Usuario, totales: Totales, onCerrarSesion: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Banner(usuario)

        Column(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextoTitulo(usuario.jugador, 34.sp, VerdeProfundo, sombra = VerdeSalvia)
            Text(
                usuario.correo,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
            Insignia(usuario.rol, Modifier.padding(top = 12.dp))

            Row(
                Modifier.fillMaxWidth().padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Estadistica("Ítems guardados", totales.guardados, Modifier.weight(1f))
                Estadistica("Favoritos", totales.favoritos, Modifier.weight(1f))
            }

            Spacer(Modifier.height(28.dp))
            BotonApp("Cerrar sesión", onCerrarSesion, Modifier.fillMaxWidth(), tipo = TipoBoton.PELIGRO)
        }
    }
}

/** Banner verde con textura de bloques de pasto; el jugador "sale" por debajo del borde. */
@Composable
private fun Banner(usuario: Usuario) {
    val flotar by rememberInfiniteTransition(label = "perfil").animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "perfilY",
    )
    var cuerpoFallo by remember(usuario.jugador) { mutableStateOf(false) }
    val limpio = usuario.jugador.filter { it.isLetterOrDigit() || it == '_' }.ifBlank { "Steve" }

    Box(
        Modifier
            .fillMaxWidth()
            .height(290.dp)
            .background(
                Brush.verticalGradient(listOf(VerdeOscuro, VerdeMedio)),
                RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp),
            ),
    ) {
        // Textura: cuadritos claros y oscuros, como los bloques de pasto.
        Canvas(Modifier.fillMaxSize()) {
            val bloque = 26.dp.toPx()
            val columnas = (size.width / bloque).toInt() + 1
            val filas = (size.height / bloque).toInt() + 1
            for (f in 0 until filas) for (c in 0 until columnas) {
                val n = (((c * 73856093) xor (f * 19349663)) and 0x7FFFFFFF) % 100
                val color = when {
                    n < 22 -> Color.White.copy(alpha = 0.07f)
                    n < 40 -> Color.Black.copy(alpha = 0.07f)
                    else -> null
                }
                color?.let { drawRect(it, Offset(c * bloque, f * bloque), Size(bloque, bloque)) }
            }
        }
        TextoTitulo(
            "Mi perfil", 24.sp, Color.White,
            Modifier.statusBarsPadding().padding(start = 22.dp, top = 18.dp),
            sombra = VerdeProfundo,
        )
        // Jugador de cuerpo entero (render de mc-heads); si no carga, solo la cabeza.
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 44.dp)
                .graphicsLayer { translationY = flotar },
            contentAlignment = Alignment.Center,
        ) {
            if (cuerpoFallo) {
                CabezaJugador(limpio, 130.dp)
            } else {
                AsyncImage(
                    model = "https://mc-heads.net/body/$limpio/400",
                    contentDescription = "Jugador $limpio",
                    modifier = Modifier.height(270.dp),
                    contentScale = ContentScale.Fit,
                    filterQuality = FilterQuality.None,
                    onError = { cuerpoFallo = true },
                )
            }
        }
    }
}

@Composable
private fun Insignia(rol: Rol, modifier: Modifier = Modifier) {
    val admin = rol == Rol.ADMIN
    Text(
        if (admin) "ADMINISTRADOR" else "JUGADOR",
        modifier = modifier
            .background(if (admin) Dorado else MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
            .padding(horizontal = 18.dp, vertical = 7.dp),
        color = if (admin) Color(0xFF4A3800) else MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 1.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun Estadistica(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.fillMaxWidth().padding(vertical = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            TextoTitulo("$valor", 36.sp, VerdeOscuro, sombra = VerdeSalvia)
            Text(
                titulo,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}
