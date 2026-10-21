package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /** Sirve para crear (id 0) y para editar. */
    fun guardar(s: Servicio) {
        when {
            s.nombre.isBlank() -> _mensaje.value = "El nombre es obligatorio"
            s.precio <= 0.0 -> _mensaje.value = "El precio debe ser mayor a 0"
            else -> viewModelScope.launch {
                repo.guardarServicio(s)
                _mensaje.value = null
                _guardado.value = true
            }
        }
    }

    /** Un servicio que ya esta en algun pedido no se elimina: solo se desactiva. */
    fun eliminar(s: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(s.id)) {
                _mensaje.value = "«${s.nombre}» ya está en pedidos: no se puede eliminar, solo desactivar"
            } else {
                repo.eliminarServicio(s)
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
