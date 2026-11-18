package com.reyes.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reyes.campusgps.data.LugaresRepositoryLocal
import com.reyes.campusgps.data.UbicacionRepositoryGoogle

// Arma el ViewModel con sus repositorios. El Context se queda aquí y en el
// Repository; el ViewModel nunca lo recibe.
class CampusViewModelFactory(private val contexto: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(
                ubicacionRepo = UbicacionRepositoryGoogle(contexto.applicationContext),
                lugaresRepo = LugaresRepositoryLocal()
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
