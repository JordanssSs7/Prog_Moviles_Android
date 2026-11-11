package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleContenido(
    val item: Item,
    val cargandoDetalle: Boolean,
    val aviso: String?,
)

class DetalleViewModel(
    private val id: Int,
    private val repositorio: ItemRepository,
) : ViewModel() {

    private val cargandoDetalle = MutableStateFlow(true)
    private val aviso = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState<DetalleContenido>> = combine(
        repositorio.observarItem(id), cargandoDetalle, aviso
    ) { item, cargando, mensaje ->
        if (item == null) UiState.Error("No se encontró este elemento en tu catálogo guardado.")
        else UiState.Exito(DetalleContenido(item, cargando, mensaje))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    init {
        cargarDetalle()
    }

    /** Endpoint 2: pide el detalle; si falla, se sigue mostrando lo que ya hay en Room. */
    fun cargarDetalle() {
        viewModelScope.launch {
            cargandoDetalle.value = true
            aviso.value = null
            val r = repositorio.cargarDetalle(id)
            if (r !is ResultadoRed.Exito) aviso.value = "${mensajeError(r)} No se pudo cargar la descripción."
            cargandoDetalle.value = false
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch { repositorio.alternarFavorito(id) }
    }
}
