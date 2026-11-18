package com.reyes.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.campusgps.data.LugaresRepository
import com.reyes.campusgps.data.UbicacionNoDisponibleException
import com.reyes.campusgps.data.UbicacionRepository
import com.reyes.campusgps.model.Lugar
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.model.Posicion
import com.reyes.campusgps.model.TipoError
import com.reyes.campusgps.model.UbicacionUi
import com.reyes.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    // Mejora 1: filtrar por categoría (null = Todos)
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    // Lo que se dibuja en el mapa y en la lista: los lugares de la categoría elegida
    val lugaresVisibles: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoria) { lista, cat ->
        if (cat == null) lista else lista.filter { it.lugar.categoria == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _lugares.value)

    // Mejora 2: radio de cercanía en metros (null = sin radio)
    val radios: List<Int> = listOf(100, 200, 500)

    private val _radio = MutableStateFlow<Int?>(null)
    val radio: StateFlow<Int?> = _radio.asStateFlow()

    // Ids de los lugares que quedan dentro del radio (se calcula aquí, no en el Composable)
    val dentroDelRadio: StateFlow<Set<Int>> = combine(_lugares, _radio) { lista, r ->
        if (r == null) emptySet()
        else lista.filter { it.metros != null && it.metros <= r }.map { it.lugar.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Paso 7: el más cercano entre los lugares visibles (respeta el filtro)
    val masCercano: StateFlow<Lugar?> = lugaresVisibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun buscar() {
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    // Paso 6
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    // Mejora 1: si el lugar seleccionado queda oculto por el filtro, se deselecciona
    fun filtrar(categoria: String?) {
        _categoria.value = categoria
        val sigueVisible = categoria == null ||
            _lugares.value.any { it.lugar.id == _seleccionado.value && it.lugar.categoria == categoria }
        if (!sigueVisible) _seleccionado.value = null
    }

    // Mejora 2: tocar el mismo radio otra vez lo quita
    fun cambiarRadio(metros: Int) {
        _radio.value = if (_radio.value == metros) null else metros
    }

    // Paso 5: calcula la distancia a cada lugar y ordena de menor a mayor
    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
    }

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
        is UbicacionNoDisponibleException -> UbicacionUi.Error(
            "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
            TipoError.GPS_APAGADO
        )
        else -> UbicacionUi.Error(
            e.message ?: "No se pudo obtener la ubicación",
            TipoError.OTRO
        )
    }
}
