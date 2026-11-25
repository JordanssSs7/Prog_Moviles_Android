package com.reyes.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.murocampus.data.PostRepository
import com.reyes.murocampus.model.Comentario
import com.reyes.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var idDetalleActual: Int? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun abrir(id: Int) {
        idDetalleActual = id
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            coroutineScope {
                val postPendiente = async { repo.detalle(id) }
                val comentariosPendientes = async { repo.comentarios(id) }

                val resultadoPost = postPendiente.await()
                val resultadoComentarios = comentariosPendientes.await()
                val fallo = resultadoPost.exceptionOrNull() ?: resultadoComentarios.exceptionOrNull()

                if (fallo != null) {
                    _detalle.value = UiState.Error(mensajeDe(fallo))
                } else {
                    _detalle.value = UiState.Exito(
                        DetalleUi(
                            post = resultadoPost.getOrThrow(),
                            comentarios = resultadoComentarios.getOrThrow()
                        )
                    )
                }
            }
        }
    }

    fun reintentarDetalle() {
        idDetalleActual?.let { abrir(it) }
    }
}
