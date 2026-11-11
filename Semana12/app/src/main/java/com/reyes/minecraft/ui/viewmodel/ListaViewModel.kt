package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.conectividad.Conectividad
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Categoria
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
    private val conectividad: Conectividad,
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _sincronizando = MutableStateFlow(true)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _categoria = MutableStateFlow(Categoria.TODOS)
    val categoria: StateFlow<Categoria> = _categoria.asStateFlow()

    private val _abrirDetalle = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    /** Id del ítem elegido al azar (botón "sorpréndeme"): la UI navega a su detalle. */
    val abrirDetalle: SharedFlow<Int> = _abrirDetalle.asSharedFlow()

    private val _sinConexion = MutableStateFlow(false)
    /** true cuando no hay internet: la UI muestra un aviso fijo y la lista sale de lo guardado en Room. */
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

    private val errorSinDatos = MutableStateFlow<String?>(null)

    private val _avisos = MutableSharedFlow<String>(extraBufferCapacity = 1)
    /** Mensajes para el Snackbar (por ejemplo, "sin conexión, mostrando lo guardado"). */
    val avisos: SharedFlow<String> = _avisos.asSharedFlow()

    /** La UI siempre sale del Flow de Room; la red solo actualiza Room. */
    val uiState: StateFlow<UiState<List<Item>>> = combine(
        repositorio.observarItems(), _busqueda, _sincronizando, errorSinDatos, _categoria
    ) { items, texto, sincronizando, error, categoria ->
        when {
            items.isEmpty() && sincronizando -> UiState.Cargando
            items.isEmpty() && error != null -> UiState.Error(error)
            else -> UiState.Exito(filtrar(items, texto, categoria))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    init {
        viewModelScope.launch {
            // Primera vez (Room vacío): descargamos. Si ya hay datos, se muestran al instante.
            val guardados = repositorio.observarItems().first()
            when {
                guardados.isEmpty() -> sincronizar()
                // Datos de una versión anterior (sin nombres en español): se actualizan solos, sin notificación.
                guardados.none { it.traducido } -> ejecutarSincronizacion(notificar = false)
                else -> {
                    _sincronizando.value = false
                    // Se abre sin descargar nada: si no hay internet, se avisa que se muestra lo guardado.
                    _sinConexion.value = !conectividad.hayInternet()
                }
            }
        }
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun sincronizar() = ejecutarSincronizacion(notificar = true)

    private fun ejecutarSincronizacion(notificar: Boolean) {
        viewModelScope.launch {
            _sincronizando.value = true
            errorSinDatos.value = null
            val hayDatos = repositorio.observarItems().first().isNotEmpty()
            val resultado = repositorio.sincronizar()
            _sinConexion.value = resultado is ResultadoRed.SinConexion
            when (val r = resultado) {
                is ResultadoRed.Exito -> if (notificar) notificador.sincronizacionTerminada(r.cantidad)
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

    fun elegirCategoria(categoria: Categoria) {
        _categoria.value = categoria
    }

    /** Elige un ítem al azar entre los guardados y avisa a la UI para abrir su detalle. */
    fun itemAleatorio() {
        viewModelScope.launch {
            repositorio.observarItems().first().randomOrNull()?.let { _abrirDetalle.emit(it.id) }
        }
    }

    private fun filtrar(items: List<Item>, texto: String, categoria: Categoria): List<Item> =
        items.filter { item ->
            (categoria == Categoria.TODOS || item.categoria == categoria) &&
                (texto.isBlank() ||
                    item.nombre.contains(texto.trim(), ignoreCase = true) ||
                    item.nombreIngles.contains(texto.trim(), ignoreCase = true))
        }
}

internal fun mensajeError(r: ResultadoRed): String = when (r) {
    ResultadoRed.SinConexion -> "Sin conexión a internet."
    is ResultadoRed.ErrorServidor -> "El servidor respondió con error ${r.codigo}."
    ResultadoRed.ErrorDatos -> "Los datos recibidos no son válidos."
    is ResultadoRed.Exito -> ""
}
