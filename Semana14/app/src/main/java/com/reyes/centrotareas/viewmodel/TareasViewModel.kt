package com.reyes.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.centrotareas.data.TareasRepository
import com.reyes.centrotareas.model.CargaUi
import com.reyes.centrotareas.model.PrimosUi
import com.reyes.centrotareas.model.RegistroTiempo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val TAG = "HILO"
private const val LIMITE_MS = 4000L // Mejora 2: tiempo máximo para una carga

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Paso 3: progreso de 0f a 1f (solo se expone la versión de lectura)
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    // Paso 4: true mientras la tarea trabaja
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()
    private var trabajo: Job? = null

    // Pasos 2 a 4 + Mejora 1: el trabajo lo hace el Repository (Flow en Dispatchers.Default);
    // el ViewModel solo recolecta el avance y actualiza el estado.
    fun tareaLarga() {
        if (_corriendo.value) return // evita lanzar dos tareas a la vez
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            Log.d(TAG, "Antes: ${Thread.currentThread().name}")
            try {
                repo.progresoTarea().collect { valor ->
                    _progreso.value = valor
                }
                Log.d(TAG, "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                // termine bien o cancelada
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        if (trabajo?.isActive != true) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado" // la barra se queda donde estaba
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        if (_primos.value is PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e // se vuelve a lanzar para no romper la cancelación
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // Paso 6: primero el perfil (2 s) y después los pedidos (3 s) ≈ 5 s
    fun cargarUnaPorUna() = cargar("Una por una") {
        val perfil = repo.cargarPerfil()
        val pedidos = repo.cargarPedidos(_simularError.value)
        perfil to pedidos
    }

    // Paso 6: las dos cargas al mismo tiempo ≈ máximo(2 s, 3 s) = 3 s
    fun cargarALaVez() = cargar("A la vez") {
        coroutineScope {
            val perfil = async { repo.cargarPerfil() }
            val pedidos = async { repo.cargarPedidos(_simularError.value) }
            perfil.await() to pedidos.await()
        }
    }

    private fun cargar(
        modo: String,
        bloque: suspend () -> Pair<String, List<String>>
    ) {
        if (_carga.value is CargaUi.Cargando) return // no lanza una segunda carga
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                // Mejora 2: si el interruptor está activo, la carga no puede pasar de 4 s
                val (perfil, pedidos) =
                    if (_tiempoMaximo.value) withTimeout(LIMITE_MS) { bloque() } else bloque()
                val ms = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(
                    modo = modo,
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = ms
                )
                registrarTiempo(modo, ms) // Mejora 3: solo se guardan las cargas exitosas
            } catch (e: TimeoutCancellationException) {
                // va ANTES: también es una CancellationException, pero aquí sí es un error
                _carga.value = CargaUi.Error("Tardó demasiado (más de ${LIMITE_MS / 1000} s)")
            } catch (e: Exception) {
                if (e is CancellationException) throw e // la cancelación no es un error
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }

    // Paso 7: interruptor para simular que la red falla
    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    // Mejora 2: interruptor para poner un tiempo máximo a las cargas
    private val _tiempoMaximo = MutableStateFlow(false)
    val tiempoMaximo: StateFlow<Boolean> = _tiempoMaximo.asStateFlow()

    fun cambiarTiempoMaximo(valor: Boolean) {
        _tiempoMaximo.value = valor
    }

    // ----- Mejora 3: comparación de tiempos (modo -> veces, promedio y último)
    private val _tiempos = MutableStateFlow<Map<String, RegistroTiempo>>(emptyMap())
    val tiempos: StateFlow<Map<String, RegistroTiempo>> = _tiempos.asStateFlow()

    private fun registrarTiempo(modo: String, ms: Long) {
        val actual = _tiempos.value[modo] ?: RegistroTiempo()
        _tiempos.value = _tiempos.value + (modo to actual.agregar(ms))
    }

    fun limpiarTiempos() {
        _tiempos.value = emptyMap()
    }
}
