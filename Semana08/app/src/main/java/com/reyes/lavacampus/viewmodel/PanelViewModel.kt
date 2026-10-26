package com.reyes.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.prefs.PreferenciasStore
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class PanelViewModel(
    repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val hoy = LocalDate.now().toString()

    /** Si esta en false, los estudiantes no pueden crear pedidos (ajuste "Recibir pedidos"). */
    val recibiendoPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun abrirRecepcion() {
        viewModelScope.launch { prefs.guardarRecibirPedidos(true) }
    }

    // Lunes de esta semana (si hoy es domingo, retrocede al lunes anterior)
    private val inicioSemana = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    // Paleta de los graficos de barras: azules y acentos de la marca
    private val colores = listOf(
        Color(0xFF319E74), Color(0xFF22B8F0), Color(0xFF6366F1), Color(0xFFF59E0B), Color(0xFF16A34A)
    )

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val porRecoger: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Cantidad de pedidos atrasados (entrega vencida y sin cerrar). */
    val atrasados: StateFlow<Int> = repo.observarPedidosAtrasados(hoy)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Pedidos de la semana (desde el lunes, sin cancelados). */
    val pedidosSemana: StateFlow<Int> = repo.observarPedidosDesde(inicioSemana)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Suma del total de los pedidos pagados desde el lunes. */
    val ingresosSemana: StateFlow<Double> = repo.observarIngresosDesde(inicioSemana)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Pedidos por estado (activos o entregados) en formato de grafico. */
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        listOf(
            EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO, EstadoPedido.LISTO, EstadoPedido.ENTREGADO
        ).map { estado ->
            repo.observarCantidadPorEstado(estado).map { DatoGrafico(estado.etiqueta, it.toDouble(), estado.color) }
        }
    ) { it.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Ingresos por servicio (solo pedidos pagados de la semana). */
    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(inicioSemana)
        .map { lista ->
            lista.mapIndexed { i, ing -> DatoGrafico(ing.nombre, ing.total, colores[i % colores.size]) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
