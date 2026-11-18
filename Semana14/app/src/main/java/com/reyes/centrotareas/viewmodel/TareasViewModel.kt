package com.reyes.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.centrotareas.data.TareasRepository
import com.reyes.centrotareas.model.CargaUi
import com.reyes.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "HILO"

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

    // Pasos 2 a 4: el trabajo va en segundo plano; el hilo principal queda libre.
    fun tareaLarga() {
        if (_corriendo.value) return // evita lanzar dos tareas a la vez
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            _progreso.value = 0f
            _mensaje.value = "Trabajando..."
            Log.d(TAG, "Antes: ${Thread.currentThread().name}")
            try {
                for (i in 1..100) {
                    withContext(Dispatchers.Default) {
                        if (i == 1) Log.d(TAG, "Dentro: ${Thread.currentThread().name}")
                        delay(40)
                    }
                    _progreso.value = i / 100f
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
        val pedidos = repo.cargarPedidos(false)
        perfil to pedidos
    }

    // Paso 6: las dos cargas al mismo tiempo ≈ máximo(2 s, 3 s) = 3 s
    fun cargarALaVez() = cargar("A la vez") {
        coroutineScope {
            val perfil = async { repo.cargarPerfil() }
            val pedidos = async { repo.cargarPedidos(false) }
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
            val (perfil, pedidos) = bloque()
            _carga.value = CargaUi.Exito(
                modo = modo,
                perfil = perfil,
                pedidos = pedidos.size,
                milisegundos = System.currentTimeMillis() - inicio
            )
        }
    }

    // TODO Paso 7: simularError y manejo de errores
}
