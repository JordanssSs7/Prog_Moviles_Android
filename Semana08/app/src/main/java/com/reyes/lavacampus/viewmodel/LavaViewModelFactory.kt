package com.reyes.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reyes.lavacampus.data.prefs.PreferenciasStore
import com.reyes.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    val repo: LavaRepository,
    val prefs: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo)
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(repo, prefs)
        modelClass.isAssignableFrom(TarifasViewModel::class.java) -> TarifasViewModel(repo)
        modelClass.isAssignableFrom(PanelViewModel::class.java) -> PanelViewModel(repo, prefs)
        modelClass.isAssignableFrom(ClientesViewModel::class.java) -> ClientesViewModel(repo)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
