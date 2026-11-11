package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ComunidadRepository
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoComunidad
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Totales
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.notificacion.Notificador
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Un ítem del ranking de favoritos: [item] sirve para mostrar su imagen (puede faltar si no está guardado). */
data class ItemPopularUi(val nombre: String, val votos: Int, val item: Item?)

class AdminViewModel(
    private val repositorio: ItemRepository,
    private val comunidad: ComunidadRepository,
    private val notificador: Notificador,
) : ViewModel() {

    val totales: StateFlow<Totales> = repositorio.observarTotales()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Totales(0, 0))

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensajes = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val mensajes: SharedFlow<String> = _mensajes.asSharedFlow()

    private val _usuarios = MutableStateFlow<UiState<List<Usuario>>>(UiState.Cargando)
    val usuarios: StateFlow<UiState<List<Usuario>>> = _usuarios.asStateFlow()

    private val _ranking = MutableStateFlow<UiState<List<ItemPopularUi>>>(UiState.Cargando)
    val ranking: StateFlow<UiState<List<ItemPopularUi>>> = _ranking.asStateFlow()

    init {
        cargarComunidad()
    }

    /** Lee de Firestore los usuarios registrados y los ítems más marcados como favoritos. */
    fun cargarComunidad() {
        viewModelScope.launch {
            _usuarios.value = UiState.Cargando
            _ranking.value = UiState.Cargando

            _usuarios.value = when (val r = comunidad.obtenerUsuarios()) {
                is ResultadoComunidad.Exito -> UiState.Exito(r.datos)
                else -> UiState.Error(mensajeComunidad(r))
            }

            _ranking.value = when (val r = comunidad.obtenerRanking()) {
                is ResultadoComunidad.Exito -> {
                    val guardados = repositorio.observarItems().first().associateBy { it.id }
                    UiState.Exito(r.datos.map { ItemPopularUi(guardados[it.itemId]?.nombre ?: it.nombre, it.votos, guardados[it.itemId]) })
                }
                else -> UiState.Error(mensajeComunidad(r))
            }
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _sincronizando.value = true
            when (val r = repositorio.sincronizar()) {
                is ResultadoRed.Exito -> {
                    notificador.sincronizacionTerminada(r.cantidad)
                    _mensajes.emit("Sincronización lista: ${r.cantidad} elementos")
                }
                else -> _mensajes.emit(mensajeError(r))
            }
            _sincronizando.value = false
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repositorio.limpiarCache()
            _mensajes.emit("Caché limpiada")
        }
    }

    private fun mensajeComunidad(r: ResultadoComunidad<*>): String = when (r) {
        ResultadoComunidad.SinPermiso -> "Sin permiso para leer. Publica las reglas de Firestore para el admin."
        ResultadoComunidad.SinConexion -> "Sin conexión a internet."
        is ResultadoComunidad.Otro -> r.mensaje
        is ResultadoComunidad.Exito -> ""
    }
}
