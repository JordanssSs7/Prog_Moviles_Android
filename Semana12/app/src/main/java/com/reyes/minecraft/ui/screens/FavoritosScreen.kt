package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.components.PantallaVacia
import com.reyes.minecraft.ui.components.TarjetaItem
import com.reyes.minecraft.ui.viewmodel.FavoritosViewModel

@Composable
fun FavoritosScreen(
    vm: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    onCerrarSesion: () -> Unit,
) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { BarraSuperior("Favoritos") { TextButton(onClick = onCerrarSesion) { Text("Salir") } } },
    ) { padding ->
        when (val e = estado) {
            UiState.Cargando -> PantallaCargando(Modifier.padding(padding))
            is UiState.Error -> PantallaError(e.mensaje, onReintentar = {}, modifier = Modifier.padding(padding))
            is UiState.Exito -> if (e.datos.isEmpty()) {
                PantallaVacia("Aún no marcaste favoritos.\nToca el corazón en el catálogo.", Modifier.padding(padding))
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
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
