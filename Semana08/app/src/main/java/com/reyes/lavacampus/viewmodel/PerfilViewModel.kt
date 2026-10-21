package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.Unidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Contadores del perfil del estudiante. */
class PerfilViewModel(
    repo: LavaRepository,
    val usuario: Usuario
) : ViewModel() {

    private val pedidos = repo.observarPedidosDeUsuario(usuario.id)

    val pedidosActivos: StateFlow<Int> = pedidos
        .map { lista ->
            lista.count {
                EstadoPedido.valueOf(it.pedido.estado) in
                    setOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Pedidos totales, sin cancelados. */
    val pedidosTotales: StateFlow<Int> = pedidos
        .map { lista -> lista.count { it.pedido.estado != EstadoPedido.CANCELADO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Suma de los kilos de los servicios por KG, sin contar pedidos cancelados. */
    val kilosLavados: StateFlow<Double> = pedidos
        .map { lista ->
            lista.filter { it.pedido.estado != EstadoPedido.CANCELADO.name }
                .flatMap { it.detalles }
                .filter { it.servicio.unidad == Unidad.KG.name }
                .sumOf { it.detalle.cantidad }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}
