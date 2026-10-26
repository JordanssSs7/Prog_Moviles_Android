package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class InicioViewModel(
    repo: LavaRepository,
    val usuario: Usuario
) : ViewModel() {

    private val activos = setOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO)

    // Ya vienen ordenados del mas reciente al mas antiguo (id descendente)
    private val pedidos = repo.observarPedidosDeUsuario(usuario.id)

    /** El pedido activo es el mas reciente en RECIBIDO, EN_PROCESO o LISTO. */
    val pedidoActivo: StateFlow<PedidoConDetalle?> = pedidos
        .map { lista -> lista.firstOrNull { EstadoPedido.valueOf(it.pedido.estado) in activos } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Servicios activos: se muestran como categorias en el inicio. */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Los 2 ultimos pedidos. */
    val recientes: StateFlow<List<PedidoConDetalle>> = pedidos
        .map { it.take(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
