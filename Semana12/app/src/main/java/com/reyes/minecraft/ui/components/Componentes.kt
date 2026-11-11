@file:OptIn(ExperimentalMaterial3Api::class)

package com.reyes.minecraft.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import com.reyes.minecraft.ui.theme.FuenteTitulo
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeMedio
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.clip
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.reyes.minecraft.model.Item

/** Prueba cada URL en orden hasta que una cargue (primero como ítem, luego como bloque). */
@Composable
fun ImagenItem(urls: List<String>, modifier: Modifier = Modifier) {
    var indice by remember(urls) { mutableIntStateOf(0) }
    if (indice >= urls.size) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) { Text("?", style = MaterialTheme.typography.titleLarge) }
    } else {
        AsyncImage(
            model = urls[indice],
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Fit,
            filterQuality = FilterQuality.None, // conserva el aspecto pixelado de Minecraft
            onError = { indice++ },
        )
    }
}

@Composable
fun PantallaCargando(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun PantallaError(mensaje: String, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Ocurrió un problema", style = MaterialTheme.typography.titleLarge)
        Text(
            mensaje,
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
        )
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}

@Composable
fun PantallaVacia(
    mensaje: String,
    modifier: Modifier = Modifier,
    onReintentar: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(mensaje, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        if (onReintentar != null) {
            Button(onClick = onReintentar, modifier = Modifier.padding(top = 12.dp)) { Text("Reintentar") }
        }
    }
}

@Composable
fun BotonFavorito(
    esFavorito: Boolean,
    onClick: () -> Unit,
    tinteSinMarcar: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    // El corazón "salta" con un rebote al marcarlo.
    val escala by animateFloatAsState(
        targetValue = if (esFavorito) 1.25f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
        label = "corazon",
    )
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
            modifier = Modifier.graphicsLayer { scaleX = escala; scaleY = escala },
            tint = if (esFavorito) Color(0xFFE53935) else tinteSinMarcar,
        )
    }
}

@Composable
fun TarjetaItem(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // La imagen va en una casilla redonda con fondo suave, todas del mismo tamaño.
            Box(
                Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                ImagenItem(item.imagenesCandidatas, Modifier.size(40.dp))
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(
                    item.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "${item.categoria.titulo}  ·  Pila máx. ${item.stackSize ?: "-"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            BotonFavorito(item.esFavorito, onFavorito)
        }
    }
}

/** Etiqueta redondeada pequeña (categoría, pila máxima...). */
@Composable
fun Etiqueta(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
fun BarraSuperior(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    icono: (@Composable () -> Unit)? = null,
    acciones: @Composable () -> Unit = {},
) {
    // Encabezado con degradado verde y esquinas redondeadas abajo.
    val forma = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp, forma)
            .background(Brush.horizontalGradient(listOf(VerdeOscuro, VerdeMedio)), forma),
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .heightIn(min = 66.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            } else {
                Spacer(Modifier.width(10.dp))
            }
            icono?.let {
                it()
                Spacer(Modifier.width(10.dp))
            }
            Text(
                titulo.uppercase(),
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontFamily = FuenteTitulo,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            acciones()
        }
    }
}
