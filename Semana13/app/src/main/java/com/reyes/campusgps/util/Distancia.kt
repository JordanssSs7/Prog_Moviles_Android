package com.reyes.campusgps.util

import android.location.Location
import com.reyes.campusgps.model.Lugar
import com.reyes.campusgps.model.Posicion

// Distancia en metros desde tu posición hasta un lugar.
// Orden de los argumentos: (lat1, lon1, lat2, lon2, resultado)
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}
