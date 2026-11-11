package com.reyes.minecraft.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.minecraft.R
import com.reyes.minecraft.ui.theme.FuenteTitulo
import com.reyes.minecraft.ui.theme.VerdeMedio
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdePiedra
import com.reyes.minecraft.ui.theme.VerdeProfundo
import com.reyes.minecraft.ui.theme.VerdeSalvia

/** Texto de título: letra condensada gruesa, mayúsculas y sombra dura (sin desenfoque) de bloque. */
@Composable
fun TextoTitulo(
    texto: String,
    tamano: TextUnit,
    color: Color,
    modifier: Modifier = Modifier,
    sombra: Color? = null,
) {
    Text(
        text = texto.uppercase(),
        color = color,
        fontFamily = FuenteTitulo,
        fontWeight = FontWeight.Bold,
        fontSize = tamano,
        lineHeight = tamano,
        letterSpacing = 1.sp,
        modifier = modifier,
        style = TextStyle(
            shadow = sombra?.let { Shadow(it, Offset(tamano.value * 0.09f, tamano.value * 0.09f), 0f) },
        ),
    )
}

/** Título "MINECRAFT CATÁLOGO" en dos líneas. */
@Composable
fun TituloApp(modifier: Modifier = Modifier) {
    Column(modifier) {
        TextoTitulo("Minecraft", 68.sp, VerdeProfundo, sombra = VerdeSalvia)
        TextoTitulo("Catálogo", 68.sp, VerdeMedio, sombra = VerdeSalvia)
    }
}

/** Steve grande, sin fondo; flota suavemente. Ocupa todo el espacio que se le dé. */
@Composable
fun SteveGrande(modifier: Modifier = Modifier) {
    val flotar by rememberInfiniteTransition(label = "steve").animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "steveY",
    )
    Image(
        painter = painterResource(R.drawable.steve),
        contentDescription = "Steve",
        modifier = modifier.graphicsLayer { translationY = flotar },
        contentScale = ContentScale.Fit,
    )
}

/** Botón redondo con flecha para volver: blanco, con borde verde y sombra suave. */
@Composable
fun BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(46.dp)
            .shadow(8.dp, CircleShape)
            .background(Color.White, CircleShape)
            .border(1.5.dp, VerdeSalvia, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = VerdeOscuro,
            modifier = Modifier.size(24.dp),
        )
    }
}

/** Tarjeta blanca redondeada con sombra, donde va el formulario. */
@Composable
fun PanelApp(modifier: Modifier = Modifier, contenido: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 14.dp,
    ) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 26.dp), content = contenido)
    }
}

@Composable
fun TituloPanel(texto: String) {
    TextoTitulo(texto, 28.sp, VerdeOscuro)
}

/** Campo con borde verde redondeado e ícono, como los de la referencia. */
@Composable
fun CampoApp(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    esClave: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(etiqueta, color = VerdeSalvia) },
        leadingIcon = { Icon(icono, contentDescription = null, tint = VerdeMedio) },
        shape = RoundedCornerShape(14.dp),
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else teclado),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VerdeOscuro,
            unfocusedBorderColor = VerdeMedio,
            cursorColor = VerdeOscuro,
            focusedTextColor = VerdeProfundo,
            unfocusedTextColor = VerdeProfundo,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
    )
}

/** Mensaje de error (rojo suave) o de éxito (verde piedra). */
@Composable
fun AvisoApp(texto: String, esError: Boolean, modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(10.dp)
    Text(
        texto,
        color = if (esError) Color(0xFF8C2F28) else VerdeProfundo,
        fontSize = 13.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(if (esError) Color(0xFFFDECEA) else VerdePiedra, forma)
            .border(1.dp, if (esError) Color(0xFFE0A29D) else VerdeSalvia, forma)
            .padding(10.dp),
    )
}
