package com.reyes.minecraft.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.components.PantallaVacia
import com.reyes.minecraft.ui.components.TarjetaItem
import com.reyes.minecraft.ui.viewmodel.ListaViewModel

@Composable
fun ListaScreen(
    vm: ListaViewModel,
    onItemClick: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val sincronizando by vm.sincronizando.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    // Aviso (Snackbar) cuando no hay internet pero se muestran datos guardados.
    LaunchedEffect(Unit) { vm.avisos.collect { snackbar.showSnackbar(it) } }

    // Android 13+: permiso para poder mostrar la notificación al terminar de sincronizar.
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior("Catálogo") {
                TextButton(onClick = vm::sincronizar, enabled = !sincronizando) { Text("Actualizar") }
                TextButton(onClick = onCerrarSesion) { Text("Salir") }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::buscar,
                label = { Text("Buscar ítem o bloque") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            )
            when (val e = estado) {
                UiState.Cargando -> PantallaCargando()
                is UiState.Error -> PantallaError(e.mensaje, onReintentar = vm::sincronizar)
                is UiState.Exito -> if (e.datos.isEmpty()) {
                    if (busqueda.isBlank()) {
                        PantallaVacia("No hay elementos guardados.", onReintentar = vm::sincronizar)
                    } else {
                        PantallaVacia("No se encontró «$busqueda».")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    ) {
                        items(e.datos, key = { it.id }) { item ->
                            TarjetaItem(
                                item = item,
                                onClick = { onItemClick(item.id) },
                                onFavorito = { vm.alternarFavorito(item.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}
