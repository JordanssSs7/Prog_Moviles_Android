package com.reyes.minecraft.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.model.Totales
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/** Datos del perfil: cuántos ítems tiene guardados y cuántos marcó como favoritos. */
class PerfilViewModel(repositorio: ItemRepository) : ViewModel() {
    val totales: StateFlow<Totales> = repositorio.observarTotales()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Totales(0, 0))
}
