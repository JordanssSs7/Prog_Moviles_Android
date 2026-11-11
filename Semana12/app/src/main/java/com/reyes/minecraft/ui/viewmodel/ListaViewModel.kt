package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.notificacion.Notificador
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repositorio: ItemRepository,
    private val notificador: Notificador,
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _sincronizando = MutableStateFlow(true)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val errorSinDatos = MutableStateFlow<String?>(null)

    private val _avisos = MutableSharedFlow<String>(extraBufferCapacity = 1)
    /** Mensajes para el Snackbar (por ejemplo, "sin conexión, mostrando lo guardado"). */
    val avisos: SharedFlow<String> = _avisos.asSharedFlow()

    /** La UI siempre sale del Flow de Room; la red solo actualiza Room. */
    val uiState: StateFlow<UiState<List<Item>>> = combine(
        repositorio.observarItems(), _busqueda, _sincronizando, errorSinDatos
    ) { items, texto, sincronizando, error ->
        when {
            items.isEmpty() && sincronizando -> UiState.Cargando
            items.isEmpty() && error != null -> UiState.Error(error)
            else -> UiState.Exito(filtrar(items, texto))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    init {
        viewModelScope.launch {
            // Primera vez (Room vacío): descargamos. Si ya hay datos, se muestran al instante.
            if (repositorio.observarItems().first().isEmpty()) sincronizar()
            else _sincronizando.value = false
        }
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun sincronizar() {
        viewModelScope.launch {
            _sincronizando.value = true
            errorSinDatos.value = null
            val hayDatos = repositorio.observarItems().first().isNotEmpty()
            when (val r = repositorio.sincronizar()) {
                is ResultadoRed.Exito -> notificador.sincronizacionTerminada(r.cantidad)
                else -> {
                    val mensaje = mensajeError(r)
                    if (hayDatos) _avisos.emit("$mensaje Mostrando lo guardado.")
                    else errorSinDatos.value = mensaje
                }
            }
            _sincronizando.value = false
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repositorio.alternarFavorito(id) }
    }

    private fun filtrar(items: List<Item>, texto: String): List<Item> =
        if (texto.isBlank()) items else items.filter { it.nombre.contains(texto.trim(), ignoreCase = true) }
}

internal fun mensajeError(r: ResultadoRed): String = when (r) {
    ResultadoRed.SinConexion -> "Sin conexión a internet."
    is ResultadoRed.ErrorServidor -> "El servidor respondió con error ${r.codigo}."
    ResultadoRed.ErrorDatos -> "Los datos recibidos no son válidos."
    is ResultadoRed.Exito -> ""
}
