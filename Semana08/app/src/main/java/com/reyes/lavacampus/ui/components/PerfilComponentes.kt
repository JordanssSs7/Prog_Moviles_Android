package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.theme.VerdeMarca
import com.reyes.lavacampus.ui.theme.Tinta
import com.reyes.lavacampus.ui.theme.TintaSuave

// El perfil usa siempre un fondo de color intenso (tambien en modo oscuro) para que el texto blanco se lea bien
fun colorMarca(@Suppress("UNUSED_PARAMETER") rol: Rol) = VerdeMarca

fun fondoPerfil(rol: Rol): Brush =
    Brush.verticalGradient(listOf(Color(0xFF1B7556), VerdeMarca))

/**
 * Pantalla de color completo, como la referencia: arriba una flecha para volver (opcional), el titulo
 * centrado y un icono de accion (opcional, por ejemplo el engranaje o la luna).
 */
@Composable
fun PerfilColor(
    rol: Rol,
    titulo: String,
    modifier: Modifier = Modifier,
    onAtras: (() -> Unit)? = null,
    iconoAccion: ImageVector? = null,
    descripcionAccion: String = "",
    onAccion: (() -> Unit)? = null,
    fondo: Brush = fondoPerfil(rol),
    estiloTitulo: TextStyle = MaterialTheme.typography.titleLarge,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(fondo)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    if (onAtras != null) {
                        IconButton(onClick = onAtras) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    }
                }
                Text(
                    titulo,
                    style = estiloTitulo,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    if (iconoAccion != null && onAccion != null) {
                        IconButton(onClick = onAccion) {
                            Icon(iconoAccion, contentDescription = descripcionAccion, tint = Color.White)
                        }
                    }
                }
            }
            contenido()
        }
    }
}

/**
 * Circulo blanco con un icono de persona y aros decorativos alrededor. Si hay [onEditar] aparece el
 * boton del lapiz sobre el borde, como en la referencia.
 */
@Composable
fun AvatarGrande(rol: Rol, modifier: Modifier = Modifier, onEditar: (() -> Unit)? = null) {
    Box(modifier = modifier.size(220.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val trazo = Stroke(width = 2.dp.toPx())
            drawCircle(Color.White.copy(alpha = 0.85f), radius = 17.dp.toPx(), center = Offset(w * 0.22f, h * 0.15f), style = trazo)
            drawCircle(Color.White.copy(alpha = 0.55f), radius = 25.dp.toPx(), center = Offset(w * 0.07f, h * 0.50f), style = trazo)
            drawCircle(Color.White.copy(alpha = 0.9f), radius = 9.dp.toPx(), center = Offset(w * 0.30f, h * 0.88f), style = trazo)
            drawCircle(Color.White.copy(alpha = 0.5f), radius = 23.dp.toPx(), center = Offset(w * 0.93f, h * 0.84f), style = trazo)
            drawCircle(Color.White.copy(alpha = 0.7f), radius = 6.dp.toPx(), center = Offset(w * 0.90f, h * 0.20f))
        }
        Surface(
            modifier = Modifier.size(140.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 10.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = colorMarca(rol),
                    modifier = Modifier.size(96.dp)
                )
            }
        }
        if (onEditar != null) {
            Surface(
                onClick = onEditar,
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 34.dp, bottom = 40.dp).size(46.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar perfil", tint = colorMarca(rol), modifier = Modifier.size(22.dp))
                }
            }
        }
    }
}

/** Dato resaltado: tarjeta translucida con un icono, la etiqueta en mayusculas y el valor grande en blanco. */
@Composable
fun DatoPerfil(etiqueta: String, valor: String, modifier: Modifier = Modifier, icono: ImageVector? = null) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.16f))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (icono != null) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) { Icon(icono, contentDescription = null, tint = VerdeMarca, modifier = Modifier.size(24.dp)) }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                etiqueta.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.78f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(valor, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun SeccionClara(titulo: String, modifier: Modifier = Modifier) {
    Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White, modifier = modifier)
}

/** Nombre del nivel segun las estrellas ganadas. */
fun nombreNivel(estrellas: Int): String = when {
    estrellas <= 1 -> "Nuevo"
    estrellas <= 3 -> "Frecuente"
    estrellas == 4 -> "Plata"
    else -> "Oro"
}

/**
 * Tarjeta del nivel del cliente: el nombre del nivel y los pedidos hechos, cinco estrellas grandes y una
 * barra con lo que falta para la siguiente estrella (una por cada 2 pedidos, maximo 5).
 */
@Composable
fun BarraNivel(rol: Rol, pedidos: Int, modifier: Modifier = Modifier) {
    val marca = colorMarca(rol)
    val estrellas = estrellasPorPedidos(pedidos)
    val faltan = if (estrellas >= 5) 0 else 2 * estrellas + 1 - pedidos
    val avance = if (estrellas >= 5) 1f else if (estrellas == 0) 0f else (pedidos - (2 * estrellas - 1)) / 2f
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Column(
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(18.dp)).background(marca),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("$pedidos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("pedidos", fontSize = 11.sp, color = Color.White)
            }
            Column {
                Text(nombreNivel(estrellas), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Tinta)
                Text(
                    if (faltan == 0) "¡Nivel máximo alcanzado!"
                    else "Te ${if (faltan == 1) "falta 1 pedido" else "faltan $faltan pedidos"} para la siguiente estrella",
                    style = MaterialTheme.typography.bodySmall,
                    color = TintaSuave
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(5) { i ->
                Icon(
                    imageVector = if (i < estrellas) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = null,
                    tint = marca,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(marca.copy(alpha = 0.15f))
        ) {
            Box(Modifier.fillMaxWidth(avance.coerceIn(0f, 1f)).height(8.dp).clip(CircleShape).background(marca))
        }
    }
}

/** Una estrella por cada 2 pedidos (1 pedido ya da una), hasta 5. */
fun estrellasPorPedidos(pedidos: Int): Int = ((pedidos + 1) / 2).coerceIn(0, 5)

/** Fila de tarjetas blancas, una por dato: icono grande arriba, el valor en grande y la leyenda. */
@Composable
fun BarraActividad(rol: Rol, datos: List<Triple<ImageVector, String, String>>, modifier: Modifier = Modifier) {
    val marca = colorMarca(rol)
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        datos.forEach { (icono, valor, leyenda) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier.size(46.dp).clip(CircleShape).background(marca),
                    contentAlignment = Alignment.Center
                ) { Icon(icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(25.dp)) }
                Text(valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Tinta)
                Text(leyenda, style = MaterialTheme.typography.labelMedium, color = TintaSuave)
            }
        }
    }
}

/** Boton de contorno con icono sobre fondo claro (por ejemplo "Cancelar" en un formulario). */
@Composable
fun BotonContorno(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.error,
    icono: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(54.dp),
        shape = CircleShape,
        border = BorderStroke(1.5.dp, color.copy(alpha = 0.6f))
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Box(Modifier.size(8.dp))
        }
        Text(texto, style = MaterialTheme.typography.titleSmall, color = color)
    }
}

/** Icono de cerrar sesion. */
val IconoCerrarSesion: ImageVector get() = Icons.AutoMirrored.Filled.Logout
