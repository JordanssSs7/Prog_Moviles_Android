package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.prefs.PreferenciasStore
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.LineaPedido
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.model.descuentoDeLinea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

private const val MAX_PEDIDOS_ACTIVOS = 3

// Reglas de cantidad: KG de 1.0 a 10.0 en pasos de 0.5; PRENDA y UNIDAD enteros de 1 a 30
fun pasoCantidad(unidad: String): Double = if (unidad == Unidad.KG.name) 0.5 else 1.0
fun minimoCantidad(unidad: String): Double = 1.0
fun maximoCantidad(unidad: String): Double = if (unidad == Unidad.KG.name) 10.0 else 30.0

/** Hoy + dias de entrega; si cae domingo, pasa al lunes. */
fun calcularFechaEntrega(hoy: LocalDate, dias: Int): LocalDate {
    var fecha = hoy.plusDays(dias.toLong())
    if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
    return fecha
}

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    prefs: PreferenciasStore,
    private val usuario: Usuario
) : ViewModel() {

    /** Si el administrador apaga "Recibir pedidos", el estudiante no puede crear uno. */
    val recibiendoPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // servicioId -> cantidad. Un servicio con cantidad 0 no esta en el mapa (apagado)
    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _pedidoCreado = MutableStateFlow(false)
    val pedidoCreado: StateFlow<Boolean> = _pedidoCreado.asStateFlow()

    /** Servicios elegidos con su cantidad (solo los que siguen activos). */
    val elegidos: StateFlow<List<Pair<Servicio, Double>>> = combine(servicios, _cantidades) { lista, mapa ->
        lista.mapNotNull { s -> mapa[s.id]?.let { s to it } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Suma de cantidad x precio, antes del descuento. */
    val subtotal: StateFlow<Double> = elegidos
        .map { lista -> lista.sumOf { (s, c) -> c * s.precio } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Descuento por volumen: 10 % en los servicios por kilo con mas de 5 kg. */
    val descuento: StateFlow<Double> = elegidos
        .map { lista -> lista.sumOf { (s, c) -> descuentoDeLinea(s.unidad, c, s.precio) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Se recalcula en cada cambio: subtotal menos descuento. */
    val total: StateFlow<Double> = elegidos
        .map { lista -> lista.sumOf { (s, c) -> c * s.precio - descuentoDeLinea(s.unidad, c, s.precio) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Hoy + el mayor horasEntrega de los servicios elegidos; null si no hay ninguno. */
    val fechaEntrega: StateFlow<LocalDate?> = elegidos
        .map { lista ->
            if (lista.isEmpty()) null
            else calcularFechaEntrega(LocalDate.now(), lista.maxOf { it.first.horasEntrega } / 24)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun cambiarNota(texto: String) {
        _nota.value = texto
    }

    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.firstOrNull { it.id == servicioId } ?: return
        val actual = _cantidades.value[servicioId] ?: 0.0
        var nueva = actual + delta
        if (nueva <= 0.0) {
            _cantidades.value = _cantidades.value - servicioId
            return
        }
        // Al activar un servicio se salta directo al minimo
        if (actual == 0.0) nueva = maxOf(nueva, minimoCantidad(servicio.unidad))
        nueva = nueva.coerceAtMost(maximoCantidad(servicio.unidad))
        _cantidades.value = _cantidades.value + (servicioId to nueva)
        _mensaje.value = null
    }

    fun confirmar() {
        // Solo servicios elegidos que siguen activos; cada linea lleva su descuento ya calculado
        val lineas = elegidos.value.map { (s, c) ->
            LineaPedido(s.id, c, descuentoDeLinea(s.unidad, c, s.precio))
        }
        val entrega = fechaEntrega.value
        if (lineas.isEmpty() || entrega == null) return
        if (!recibiendoPedidos.value) {
            _mensaje.value = "La lavandería no está recibiendo pedidos"
            return
        }
        viewModelScope.launch {
            if (repo.contarActivos(usuario.id) >= MAX_PEDIDOS_ACTIVOS) {
                _mensaje.value = "Ya tienes $MAX_PEDIDOS_ACTIVOS pedidos activos"
                return@launch
            }
            repo.crearPedido(usuario.id, lineas, entrega.toString(), _nota.value.trim())
            _pedidoCreado.value = true
        }
    }
}
