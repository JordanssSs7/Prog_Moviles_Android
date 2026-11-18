package com.reyes.campusgps.data

import com.reyes.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
}

// getCurrentLocation devolvió null: GPS apagado o ubicación no disponible
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")
