package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuario: Usuario
) : ViewModel() {

    private val estadosActivos = setOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO)

    private val pedidos = repo.observarPedidosDeUsuario(usuario.id)

    /** RECIBIDO, EN_PROCESO y LISTO. */
    val activos: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { EstadoPedido.valueOf(it.pedido.estado) in estadosActivos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** ENTREGADO y CANCELADO. */
    val historial: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { lista -> lista.filter { EstadoPedido.valueOf(it.pedido.estado) !in estadosActivos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Misma regla del detalle: solo se cancela un pedido RECIBIDO. */
    fun puedeCancelar(p: PedidoConDetalle): Boolean = p.pedido.estado == EstadoPedido.RECIBIDO.name

    fun cancelar(p: PedidoConDetalle) {
        if (!puedeCancelar(p)) return
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, EstadoPedido.CANCELADO, null) }
    }
}
