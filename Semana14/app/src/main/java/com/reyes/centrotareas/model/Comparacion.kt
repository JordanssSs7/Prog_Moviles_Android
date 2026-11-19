package com.reyes.centrotareas.model

/** Mejora 3: tiempos acumulados de un modo de carga ("Una por una" o "A la vez"). */
data class RegistroTiempo(
    val veces: Int = 0,
    val totalMs: Long = 0,
    val ultimoMs: Long = 0
) {
    val promedioMs: Long get() = if (veces == 0) 0 else totalMs / veces

    fun agregar(ms: Long) = copy(veces = veces + 1, totalMs = totalMs + ms, ultimoMs = ms)
}
