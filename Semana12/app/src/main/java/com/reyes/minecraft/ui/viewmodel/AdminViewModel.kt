package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Totales
import com.reyes.minecraft.notificacion.Notificador
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repositorio: ItemRepository,
    private val notificador: Notificador,
) : ViewModel() {

    val totales: StateFlow<Totales> = repositorio.observarTotales()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Totales(0, 0))

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensajes = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val mensajes: SharedFlow<String> = _mensajes.asSharedFlow()

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
}
