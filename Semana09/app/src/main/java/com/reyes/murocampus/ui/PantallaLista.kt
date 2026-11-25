package com.reyes.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.murocampus.viewmodel.UiState
import com.reyes.murocampus.viewmodel.PostsViewModel

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onPostClick: (Int) -> Unit,
    onCrearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.resultados.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = { MuroTopBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrearClick) {
                Text(
                    text = "+",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::buscar,
                label = { Text("Buscar por título") },
                singleLine = true,
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        TextButton(onClick = { vm.buscar("") }) { Text("Limpiar") }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp)
            )
            mensaje?.let { texto ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = texto,
                            color = Color(0xFF1B5E20),
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = vm::limpiarMensaje) { Text("OK") }
                    }
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filtro == null,
                        onClick = { vm.filtrar(null) },
                        label = { Text("Todos") }
                    )
                }
                items((1..10).toList()) { numero ->
                    FilterChip(
                        selected = filtro == numero,
                        onClick = { vm.filtrar(numero) },
                        label = { Text("Usuario $numero") }
                    )
                }
            }

            when (estado) {
                UiState.Cargando -> Cargando(modifier = Modifier.fillMaxSize())

                is UiState.Error -> ErrorConReintento(
                    mensaje = (estado as UiState.Error).mensaje,
                    onReintentar = vm::cargar,
                    modifier = Modifier.fillMaxSize()
                )

                is UiState.Exito -> {
                    val posts = (estado as UiState.Exito).datos
                    if (posts.isEmpty()) {
                        Text(
                            text = if (busqueda.isBlank()) "Sin publicaciones"
                            else "Sin resultados para \"$busqueda\"",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 88.dp)
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
    }
}
