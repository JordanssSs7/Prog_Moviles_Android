package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ClientesViewModel(repo: LavaRepository) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    /** Estudiantes con sus pedidos (sin cancelados) y lo gastado, filtrados por el buscador. */
    val clientes: StateFlow<List<ClienteResumen>> =
        combine(repo.observarClientes(), _busqueda) { lista, texto ->
            val t = texto.trim()
            if (t.isEmpty()) lista
            else lista.filter { it.nombre.contains(t, ignoreCase = true) || it.codigo.contains(t, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun buscar(texto: String) {
        _busqueda.value = texto
    }
}
