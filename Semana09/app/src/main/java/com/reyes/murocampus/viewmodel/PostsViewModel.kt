package com.reyes.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.murocampus.data.PostRepository
import com.reyes.murocampus.model.Comentario
import com.reyes.murocampus.model.NuevoPost
import com.reyes.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _creacion = MutableStateFlow<UiState<Post>?>(null)
    val creacion: StateFlow<UiState<Post>?> = _creacion.asStateFlow()

    // Mejora 2: búsqueda por título sobre las publicaciones ya descargadas
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val resultados: StateFlow<UiState<List<Post>>> =
        combine(_lista, _busqueda) { estado, texto ->
            if (estado is UiState.Exito && texto.isNotBlank()) {
                UiState.Exito(estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) })
            } else {
                estado
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    // Mejora 3: estado del borrado y mensaje para la lista
    private val _borrado = MutableStateFlow<UiState<Int>?>(null)
    val borrado: StateFlow<UiState<Int>?> = _borrado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private var idDetalleActual: Int? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = when (val filtro = _usuarioFiltro.value) {
                null -> repo.listar()
                else -> repo.porUsuario(filtro)
            }
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
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

    fun publicar(titulo: String, contenido: String) {
        viewModelScope.launch {
            _creacion.value = UiState.Cargando
            val nuevo = NuevoPost(userId = 1, title = titulo, body = contenido)
            repo.crear(nuevo)
                .onSuccess { _creacion.value = UiState.Exito(it) }
                .onFailure { _creacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun borrar(id: Int) {
        viewModelScope.launch {
            _borrado.value = UiState.Cargando
            repo.borrar(id)
                .onSuccess {
                    // JSONPlaceholder no borra de verdad: se quita de la lista local
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(actual.datos.filterNot { it.id == id })
                    }
                    _mensaje.value = "Publicación $id eliminada (DELETE respondió 200)"
                    _borrado.value = UiState.Exito(id)
                }
                .onFailure { _borrado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarBorrado() {
        _borrado.value = null
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun limpiarCreacion() {
        _creacion.value = null
    }
}
