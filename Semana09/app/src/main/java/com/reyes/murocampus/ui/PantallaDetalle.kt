package com.reyes.murocampus.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.murocampus.viewmodel.PostsViewModel
import com.reyes.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }

    BackHandler {
        vm.limpiarBorrado()
        onVolver()
    }

    // Mejora 3: al borrar con éxito se vuelve a la lista
    LaunchedEffect(borrado) {
        if (borrado is UiState.Exito) {
            vm.limpiarBorrado()
            onVolver()
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Eliminar publicación?") },
            text = { Text("Se enviará DELETE posts/$id al servidor.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmar = false
                    vm.borrar(id)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MuroTopBar(
                titulo = "Publicación #$id",
                subtitulo = fechaDeHoy(),
                onVolver = {
                    vm.limpiarBorrado()
                    onVolver()
                }
            )
        }
    ) { innerPadding ->
        when (estado) {
            UiState.Cargando -> Cargando(modifier = Modifier.fillMaxSize().padding(innerPadding))

            is UiState.Error -> ErrorConReintento(
                mensaje = (estado as UiState.Error).mensaje,
                onReintentar = vm::reintentarDetalle,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )

            is UiState.Exito -> {
                val datos = (estado as UiState.Exito).datos
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = datos.post.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = datos.post.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { confirmar = true },
                                    enabled = borrado !is UiState.Cargando,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text(if (borrado is UiState.Cargando) "Eliminando..." else "Eliminar")
                                }
                                if (borrado is UiState.Error) {
                                    Text(
                                        text = (borrado as UiState.Error).mensaje,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Comentarios (${datos.comentarios.size})",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                        )
                    }

                    items(datos.comentarios, key = { it.id }) { comentario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            androidx.compose.foundation.layout.Column(
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Text(
                                    text = comentario.email,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = comentario.body,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
