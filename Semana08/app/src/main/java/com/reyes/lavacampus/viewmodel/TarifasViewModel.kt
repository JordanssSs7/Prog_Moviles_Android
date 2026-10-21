package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TarifasViewModel(repo: LavaRepository) : ViewModel() {

    /** Solo servicios ACTIVOS: si el administrador desactiva uno, desaparece de Tarifas. */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
