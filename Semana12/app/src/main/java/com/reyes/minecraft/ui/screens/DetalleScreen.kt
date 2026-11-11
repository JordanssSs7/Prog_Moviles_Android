package com.reyes.minecraft.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Receta
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.BotonFavorito
import com.reyes.minecraft.ui.components.Etiqueta
import com.reyes.minecraft.ui.components.ImagenItem
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.viewmodel.DetalleViewModel

@Composable
fun DetalleScreen(vm: DetalleViewModel, onItemClick: (Int) -> Unit, onAtras: () -> Unit) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BarraSuperior("Detalle", onAtras = onAtras) {
                (estado as? UiState.Exito)?.let {
                    BotonFavorito(it.datos.item.esFavorito, vm::alternarFavorito, tinteSinMarcar = Color.White)
                }
            }
        },
    ) { padding ->
        when (val e = estado) {
            UiState.Cargando -> PantallaCargando(Modifier.padding(padding))
            is UiState.Error -> PantallaError(e.mensaje, onReintentar = vm::cargarDetalle, modifier = Modifier.padding(padding))
            is UiState.Exito -> {
                val item = e.datos.item
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    EncabezadoItem(item)

                    SeccionCard("Descripción") {
                        when {
                            item.descripcion != null -> Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)
                            e.datos.cargandoDetalle -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                            else -> Text(
                                "No hay descripción disponible para este elemento.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    SeccionCard("Receta de crafteo") {
                        ContenidoReceta(item, e.datos.ingredientes, e.datos.cargandoDetalle, onItemClick)
                    }

                    if (e.datos.aviso != null) {
                        Text(e.datos.aviso, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        BotonApp("Reintentar", vm::cargarDetalle, Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

/** Imagen grande (flotando), nombre, identificador y etiquetas. */
@Composable
private fun EncabezadoItem(item: Item) {
    val flotar by rememberInfiniteTransition(label = "flotar").animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
        label = "flotarY",
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                Modifier
                    .size(170.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                ImagenItem(item.imagenesCandidatas, Modifier.size(120.dp).graphicsLayer { translationY = flotar })
            }
            Text(
                item.nombre,
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                item.clave,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
            )
            Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Etiqueta(item.categoria.titulo)
                Etiqueta("Pila máx. ${item.stackSize ?: "-"}")
            }
        }
    }
}

/** Tarjeta con un título de sección y su contenido. */
@Composable
private fun SeccionCard(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Column(Modifier.padding(top = 10.dp), content = contenido)
        }
    }
}

@Composable
private fun ContenidoReceta(
    item: Item,
    ingredientes: Map<Int, Item>,
    cargando: Boolean,
    onItemClick: (Int) -> Unit,
) {
    val receta: Receta? = item.receta
    when {
        receta == null -> if (cargando) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Text("No se pudo cargar la receta.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        !receta.existe -> Text(
            "Este ítem no se fabrica: se consigue jugando.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        else -> Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Cuadrícula de ingredientes; tocar uno abre su propio detalle.
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                receta.filas.forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        fila.forEach { id ->
                            val ingrediente = id?.let { ingredientes[it] }
                            Celda(ingrediente?.let { { onItemClick(it.id) } }) {
                                ingrediente?.let { ImagenItem(it.imagenesCandidatas, Modifier.fillMaxWidth().padding(6.dp)) }
                            }
                        }
                    }
                }
            }
            Text("→", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
            Box {
                Celda(null) { ImagenItem(item.imagenesCandidatas, Modifier.fillMaxWidth().padding(6.dp)) }
                if (receta.cantidad > 1) {
                    Text(
                        "${receta.cantidad}",
                        modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

/** Casilla de la mesa de crafteo. */
@Composable
private fun Celda(onClick: (() -> Unit)?, contenido: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .background(Color(0xFFC6C6C6))
            .border(2.dp, Color(0xFF555555))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) { contenido() }
}
