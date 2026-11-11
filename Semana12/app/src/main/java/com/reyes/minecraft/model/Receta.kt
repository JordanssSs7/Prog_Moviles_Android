package com.reyes.minecraft.model

/**
 * Receta de crafteo de un ítem. [filas] es la cuadrícula (cada celda es el id del ingrediente o null si está vacía).
 * Si [filas] está vacía, el ítem no se fabrica.
 */
data class Receta(val cantidad: Int, val filas: List<List<Int?>>) {
    val existe: Boolean get() = filas.isNotEmpty()
}
