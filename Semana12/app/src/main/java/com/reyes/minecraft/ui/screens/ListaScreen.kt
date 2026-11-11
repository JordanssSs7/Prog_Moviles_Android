package com.reyes.minecraft.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.Categoria
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.CabezaJugador
import com.reyes.minecraft.ui.components.CampoApp
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.components.PantallaVacia
import com.reyes.minecraft.ui.components.TarjetaItem
import com.reyes.minecraft.ui.viewmodel.ListaViewModel

@Composable
fun ListaScreen(
    usuario: Usuario,
    vm: ListaViewModel,
    onItemClick: (Int) -> Unit,
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    val sincronizando by vm.sincronizando.collectAsStateWithLifecycle()
    val sinConexion by vm.sinConexion.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    // Aviso (Snackbar) cuando no hay internet pero se muestran datos guardados.
    LaunchedEffect(Unit) { vm.avisos.collect { snackbar.showSnackbar(it) } }
    // "Sorpréndeme": el ViewModel elige un ítem y aquí se abre su detalle.
    LaunchedEffect(Unit) { vm.abrirDetalle.collect { onItemClick(it) } }

    // Android 13+: permiso para poder mostrar la notificación al terminar de sincronizar.
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Hola, ${usuario.jugador}",
                icono = { CabezaJugador(usuario.jugador, 34.dp) },
            ) {
                IconButton(onClick = vm::itemAleatorio) {
                    Icon(Icons.Default.Star, contentDescription = "Sorpréndeme con un ítem al azar")
                }
                IconButton(onClick = vm::sincronizar, enabled = !sincronizando) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar catálogo")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            CampoApp(
                valor = busqueda,
                alCambiar = vm::buscar,
                etiqueta = "Buscar ítem o bloque",
                icono = Icons.Default.Search,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(Categoria.entries) { c ->
                    FilterChip(
                        selected = c == categoria,
                        onClick = { vm.elegirCategoria(c) },
                        label = { Text(c.titulo) },
                    )
                }
            }
            // Aviso fijo cuando no hay internet pero se está mostrando lo guardado.
            if (sinConexion && (estado as? UiState.Exito)?.datos?.isNotEmpty() == true) {
                AvisoSinConexion(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            when (val e = estado) {
                UiState.Cargando -> PantallaCargando()
                is UiState.Error -> PantallaError(e.mensaje, onReintentar = vm::sincronizar)
                is UiState.Exito -> if (e.datos.isEmpty()) {
                    if (busqueda.isBlank() && categoria == Categoria.TODOS) {
                        PantallaVacia("No hay elementos guardados.", onReintentar = vm::sincronizar)
                    } else {
                        PantallaVacia("No se encontró nada con ese filtro.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(e.datos, key = { it.id }) { item ->
                            TarjetaItem(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavorito = { vm.alternarFavorito(item.id) },
                                modifier = Modifier.animateItem(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AvisoSinConexion(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
        Text(
            "Sin conexión a internet. Mostrando lo guardado.",
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
