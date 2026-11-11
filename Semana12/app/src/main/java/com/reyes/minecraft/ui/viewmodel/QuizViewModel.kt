package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

data class QuizEstado(
    val item: Item,
    /** Opción elegida; null mientras no responde. */
    val respuesta: Int?,
    val aciertos: Int,
    val intentos: Int,
    val racha: Int,
) {
    val correcta: Int? get() = item.stackSize
    val acerto: Boolean get() = respuesta != null && respuesta == correcta
}

/** Mini juego: "¿Cuántos caben en una pila?", con los datos de stackSize que vienen de la API. */
class QuizViewModel(
    private val repositorio: ItemRepository,
    private val azar: Random = Random.Default,
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<QuizEstado>>(UiState.Cargando)
    val estado: StateFlow<UiState<QuizEstado>> = _estado.asStateFlow()

    private var candidatos: List<Item> = emptyList()

    init {
        siguiente()
    }

    fun siguiente() {
        viewModelScope.launch {
            if (candidatos.isEmpty()) {
                candidatos = repositorio.observarItems().first().filter { it.stackSize in OPCIONES }
            }
            if (candidatos.isEmpty()) {
                _estado.value = UiState.Error("Primero sincroniza el catálogo en la pestaña Catálogo.")
                return@launch
            }
            val previo = (_estado.value as? UiState.Exito)?.datos
            _estado.value = UiState.Exito(
                QuizEstado(
                    item = candidatos.random(azar),
                    respuesta = null,
                    aciertos = previo?.aciertos ?: 0,
                    intentos = previo?.intentos ?: 0,
                    racha = previo?.racha ?: 0,
                )
            )
        }
    }

    fun responder(opcion: Int) {
        val actual = (_estado.value as? UiState.Exito)?.datos ?: return
        if (actual.respuesta != null) return // ya respondió esta pregunta
        val acierto = opcion == actual.correcta
        _estado.value = UiState.Exito(
            actual.copy(
                respuesta = opcion,
                aciertos = actual.aciertos + if (acierto) 1 else 0,
                intentos = actual.intentos + 1,
                racha = if (acierto) actual.racha + 1 else 0,
            )
        )
    }

    companion object {
        val OPCIONES = listOf(1, 16, 64)
    }
}
