package com.reyes.minecraft.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val clave: String,
    val nombre: String,
    val stackSize: Int?,
    val descripcion: String? = null,
    val imagenDetalle: String? = null,
    val esFavorito: Boolean = false,
    /** Receta en texto ("cantidad|1,2,-1;3,-1,-1"); null = aún no descargada. */
    val receta: String? = null,
    /** Nombre en español tomado del archivo de idioma del juego (es_es.json). */
    val nombreEs: String? = null,
)
