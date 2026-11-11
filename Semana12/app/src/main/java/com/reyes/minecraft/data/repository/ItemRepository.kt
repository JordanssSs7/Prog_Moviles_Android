package com.reyes.minecraft.data.repository

import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Totales
import kotlinx.coroutines.flow.Flow

/** Resultado de una operación que usa la red. Nunca se lanza una excepción hacia el ViewModel. */
sealed interface ResultadoRed {
    data class Exito(val cantidad: Int = 0) : ResultadoRed
    data object SinConexion : ResultadoRed
    data class ErrorServidor(val codigo: Int) : ResultadoRed
    data object ErrorDatos : ResultadoRed
}

/**
 * Única puerta de entrada a los datos para los ViewModels.
 * Lo que se lee sale SIEMPRE de Room (Flow); la red solo sirve para actualizar Room.
 */
interface ItemRepository {
    fun observarItems(): Flow<List<Item>>
    fun observarFavoritos(): Flow<List<Item>>
    fun observarItem(id: Int): Flow<Item?>
    fun observarTotales(): Flow<Totales>

    /** Endpoint 1: descarga la lista y la guarda en Room conservando los favoritos. */
    suspend fun sincronizar(): ResultadoRed

    /** Endpoint 2: descarga el detalle del ítem y lo guarda en Room. */
    suspend fun cargarDetalle(id: Int): ResultadoRed

    suspend fun alternarFavorito(id: Int)
    suspend fun limpiarCache()
}
