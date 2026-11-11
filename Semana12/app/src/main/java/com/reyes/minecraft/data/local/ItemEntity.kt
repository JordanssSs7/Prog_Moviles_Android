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
)
