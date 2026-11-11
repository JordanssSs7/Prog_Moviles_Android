package com.reyes.minecraft.data.repository

import com.reyes.minecraft.model.Usuario

/** Un ítem y cuántas personas lo tienen como favorito. */
data class Popular(val itemId: Int, val nombre: String, val votos: Int)

sealed interface ResultadoComunidad<out T> {
    data class Exito<T>(val datos: T) : ResultadoComunidad<T>
    data object SinPermiso : ResultadoComunidad<Nothing>
    data object SinConexion : ResultadoComunidad<Nothing>
    data class Otro(val mensaje: String) : ResultadoComunidad<Nothing>
}

/**
 * Datos compartidos entre usuarios (viven en Firestore):
 * quiénes están registrados y qué ítems son los más marcados como favoritos.
 */
interface ComunidadRepository {
    /** Publica (o retira) un favorito del usuario actual. Es "dispara y olvida": nunca bloquea la UI. */
    fun publicarFavorito(itemId: Int, nombre: String, marcado: Boolean)

    /** Publica varios favoritos de una vez (por ejemplo, después de sincronizar). */
    fun publicarFavoritos(favoritos: Map<Int, String>)

    /** Solo el admin puede leerlo (lo exigen las reglas de Firestore). */
    suspend fun obtenerUsuarios(): ResultadoComunidad<List<Usuario>>

    /** Solo el admin puede leerlo. Devuelve los 10 ítems con más favoritos. */
    suspend fun obtenerRanking(): ResultadoComunidad<List<Popular>>
}
