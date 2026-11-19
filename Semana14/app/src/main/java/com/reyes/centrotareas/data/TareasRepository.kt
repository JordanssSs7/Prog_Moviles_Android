package com.reyes.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    /** Mejora 1: la tarea larga vive en el Repository y avisa su avance de 0f a 1f. */
    fun progresoTarea(): Flow<Float>
    suspend fun contarPrimos(hasta: Int): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
