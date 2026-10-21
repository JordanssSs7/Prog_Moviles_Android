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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Casilleros de pedidos LISTO. El casillero del propio pedido no cuenta como ocupado. */
    val casillerosOcupados: StateFlow<List<String>> =
        combine(repo.observarCasillerosOcupados(), pedido) { ocupados, p ->
            ocupados - (p?.pedido?.casillero ?: "")
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _casilleroElegido = MutableStateFlow<String?>(null)
    val casilleroElegido: StateFlow<String?> = _casilleroElegido.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        // Si el pedido ya tiene casillero, aparece elegido
        viewModelScope.launch {
            _casilleroElegido.value = repo.observarPedido(pedidoId).first()?.pedido?.casillero
        }
    }

    fun elegirCasillero(c: String) {
        if (c in casillerosOcupados.value) return
        _casilleroElegido.value = c
        _mensaje.value = null
    }

    fun marcarPagado(pagado: Boolean) {
        _mensaje.value = null
        viewModelScope.launch { repo.marcarPagado(pedidoId, pagado) }
    }

    /**
     * Solo avanza al estado siguiente (nunca retrocede).
     * LISTO exige un casillero elegido y libre; ENTREGADO exige pagado = true.
     */
    fun avanzar() {
        val p = pedido.value ?: return
        val siguiente = EstadoPedido.valueOf(p.pedido.estado).siguiente()
        if (siguiente == null) {
            _mensaje.value = "Este pedido ya no puede avanzar"
            return
        }
        viewModelScope.launch {
            when (siguiente) {
                EstadoPedido.LISTO -> {
                    val casillero = _casilleroElegido.value
                    when {
                        casillero == null -> {
                            _mensaje.value = "Elige un casillero antes de marcar como listo"
                            return@launch
                        }
                        casillero in casillerosOcupados.value -> {
                            _mensaje.value = "El casillero $casillero ya está ocupado"
                            return@launch
                        }
                        else -> repo.cambiarEstado(pedidoId, siguiente, casillero)
                    }
                }
                EstadoPedido.ENTREGADO -> {
                    if (!p.pedido.pagado) {
                        _mensaje.value = "Marca el pedido como cobrado antes de entregarlo"
                        return@launch
                    }
                    repo.cambiarEstado(pedidoId, siguiente, p.pedido.casillero)
                }
                else -> repo.cambiarEstado(pedidoId, siguiente, p.pedido.casillero)
            }
            _mensaje.value = null
        }
    }

    /** Solo desde RECIBIDO o EN_PROCESO. */
    fun cancelar() {
        val p = pedido.value ?: return
        val estado = EstadoPedido.valueOf(p.pedido.estado)
        if (estado != EstadoPedido.RECIBIDO && estado != EstadoPedido.EN_PROCESO) {
            _mensaje.value = "Solo se cancela un pedido recibido o en proceso"
            return
        }
        viewModelScope.launch { repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null) }
    }
}
