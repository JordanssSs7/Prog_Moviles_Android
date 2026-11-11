@file:OptIn(ExperimentalMaterial3Api::class)

package com.reyes.minecraft.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun BotonFavorito(esFavorito: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (esFavorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
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
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ImagenItem(item.imagenesCandidatas, Modifier.size(48.dp))
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Máx. por pila: ${item.stackSize ?: "-"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            BotonFavorito(item.esFavorito, onFavorito)
        }
    }
}

@Composable
fun BarraSuperior(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            if (onAtras != null) {
                androidx.compose.material3.TextButton(onClick = onAtras) { Text("Atrás") }
            }
        },
        actions = { acciones() },
    )
}
