package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.R

/** Verde plano de la pantalla de configuracion de la referencia. */
val VerdeConfig = Color(0xFF319E74)
val FondoConfig: Brush = Brush.verticalGradient(listOf(VerdeConfig, VerdeConfig))

/**
 * Tipografia de la pantalla de configuracion: Poppins (geometrica y redondeada, como la referencia).
 * Poppins es de Indian Type Foundry y se distribuye con la licencia libre SIL Open Font License 1.1
 * (https://github.com/google/fonts/tree/main/ofl/poppins).
 */
val FuenteConfig: FontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val EstiloTituloConfig = TextStyle(fontFamily = FuenteConfig, fontWeight = FontWeight.Bold, fontSize = 22.sp)
private val EstiloSeccion = TextStyle(fontFamily = FuenteConfig, fontWeight = FontWeight.Bold, fontSize = 20.sp)
private val EstiloFila = TextStyle(fontFamily = FuenteConfig, fontWeight = FontWeight.Medium, fontSize = 13.sp)

/** Cabecera de seccion: icono con contorno a la izquierda y el titulo en negrita (Account, Notifications, More). */
@Composable
fun SeccionConfig(icono: ImageVector, titulo: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 17.dp, bottom = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        Spacer(Modifier.width(26.dp))
        Text(titulo, style = EstiloSeccion, color = Color.White)
    }
}

/** Fila pegada al borde izquierdo (no sangrada), con la flecha pequena a la derecha. */
@Composable
fun FilaConfig(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, style = EstiloFila, color = Color.White, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

/** Fila con el interruptor de linea y punto de la referencia. */
@Composable
fun FilaConfigLinea(texto: String, activo: Boolean, onCambio: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = activo, role = Role.Switch, onValueChange = onCambio)
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, style = EstiloFila, color = Color.White, modifier = Modifier.weight(1f))
        InterruptorLinea(activo)
    }
}

/** Interruptor minimo: una linea con un punto; el punto a la izquierda y tenue es apagado, a la derecha y blanco es encendido. */
@Composable
fun InterruptorLinea(activo: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.width(34.dp).height(14.dp)) {
        val y = size.height / 2
        val grosor = 3.dp.toPx()
        val radio = 5.dp.toPx()
        val tono = if (activo) 1f else 0.55f
        drawLine(Color.White.copy(alpha = tono), Offset(radio, y), Offset(size.width - radio, y), strokeWidth = grosor, cap = StrokeCap.Round)
        val x = if (activo) size.width - radio else radio
        drawCircle(Color.White.copy(alpha = if (activo) 1f else 0.8f), radius = radio, center = Offset(x, y))
    }
}

/** "Cerrar sesion" con la puerta, centrado y en negrita, al final de la lista. */
@Composable
fun BotonCerrarSesion(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.MeetingRoom, contentDescription = null, tint = Color.White, modifier = Modifier.size(34.dp))
        Spacer(Modifier.width(20.dp))
        Text("Cerrar sesión", style = EstiloSeccion, color = Color.White)
    }
}
