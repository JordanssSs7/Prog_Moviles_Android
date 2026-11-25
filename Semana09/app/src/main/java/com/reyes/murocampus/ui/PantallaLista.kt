package com.reyes.murocampus.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.murocampus.viewmodel.PostsViewModel
import com.reyes.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onPostClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { MuroTopBar() }
    ) { innerPadding ->
        when (estado) {
            UiState.Cargando -> Cargando(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )

            is UiState.Error -> ErrorConReintento(
                mensaje = (estado as UiState.Error).mensaje,
                onReintentar = vm::cargar,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )

            is UiState.Exito -> {
                val posts = (estado as UiState.Exito).datos
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(posts, key = { it.id }) { post ->
                        TarjetaPost(
                            post = post,
                            onClick = { onPostClick(post.id) }
                        )
                    }
                }
            }
        }
    }
}
