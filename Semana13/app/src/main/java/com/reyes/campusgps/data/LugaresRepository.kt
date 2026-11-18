package com.reyes.campusgps.data

import com.reyes.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
