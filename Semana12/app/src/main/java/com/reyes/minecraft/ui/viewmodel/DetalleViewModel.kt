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
    /** Ítems de la receta, por id, para dibujar cada ingrediente. */
    val ingredientes: Map<Int, Item>,
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
        repositorio.observarItem(id), repositorio.observarItems(), cargandoDetalle, aviso
    ) { item, todos, cargando, mensaje ->
        if (item == null) {
            UiState.Error("No se encontró este elemento en tu catálogo guardado.")
        } else {
            val idsReceta = item.receta?.filas?.flatten()?.filterNotNull()?.toSet().orEmpty()
            val ingredientes = todos.filter { it.id in idsReceta }.associateBy { it.id }
            UiState.Exito(DetalleContenido(item, ingredientes, cargando, mensaje))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    init {
        cargarDetalle()
    }

    /** Endpoints 2 y 3: pide descripción y receta; si falla, se sigue mostrando lo que ya hay en Room. */
    fun cargarDetalle() {
        viewModelScope.launch {
            cargandoDetalle.value = true
            aviso.value = null
            val r = repositorio.cargarDetalle(id)
            if (r !is ResultadoRed.Exito) aviso.value = "${mensajeError(r)} No se pudo cargar todo el detalle."
            cargandoDetalle.value = false
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch { repositorio.alternarFavorito(id) }
    }
}
