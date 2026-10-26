package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class BandejaViewModel(
    private val repo: LavaRepository,
    filtroInicial: EstadoPedido? = null
) : ViewModel() {

    /** null = chip "Todos". */
    private val _filtro = MutableStateFlow(filtroInicial)
    val filtro: StateFlow<EstadoPedido?> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    /** Ids de pedidos atrasados (entrega vencida y sin cerrar): la vista solo los marca en rojo. */
    val atrasados: StateFlow<Set<Int>> = repo.observarPedidosAtrasados(LocalDate.now().toString())
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    /** Pedidos filtrados por el chip elegido y por el buscador (nombre del estudiante o numero). */
    val pedidos: StateFlow<List<PedidoConDetalle>> =
        combine(repo.observarPedidos(), _filtro, _busqueda) { lista, filtro, texto ->
            val t = texto.trim().removePrefix("#")
            lista.filter { p ->
                (filtro == null || p.pedido.estado == filtro.name) &&
                    (t.isEmpty() ||
                        p.usuario.nombre.contains(t, ignoreCase = true) ||
                        p.pedido.id.toString().contains(t))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFiltro(estado: EstadoPedido?) {
        _filtro.value = estado
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    /** "Iniciar" no necesita casillero ni cobro: pasa de RECIBIDO a EN_PROCESO. */
    fun iniciar(p: PedidoConDetalle) {
        if (p.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.EN_PROCESO, p.pedido.casillero)
        }
    }
}
