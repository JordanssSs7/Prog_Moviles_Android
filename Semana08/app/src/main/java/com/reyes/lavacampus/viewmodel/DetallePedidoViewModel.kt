package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    /** Se actualiza solo: si el administrador cambia el estado, la pantalla se refresca. */
    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Regla: el estudiante solo cancela si el pedido esta en RECIBIDO. */
    val puedeCancelar: StateFlow<Boolean> = pedido
        .map { it != null && it.pedido.estado == EstadoPedido.RECIBIDO.name }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun cancelar() {
        // Se vuelve a validar la regla por si el estado cambio mientras el dialogo estaba abierto
        if (!puedeCancelar.value) return
        viewModelScope.launch { repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null) }
    }
}
