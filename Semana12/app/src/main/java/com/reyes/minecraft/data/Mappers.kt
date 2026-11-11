package com.reyes.minecraft.data

import com.reyes.minecraft.data.local.ItemEntity
import com.reyes.minecraft.data.remote.ItemDto
import com.reyes.minecraft.model.Item

/** DTO (red) -> Entity (Room). Un ítem recién descargado nunca es favorito. */
fun ItemDto.toEntity() = ItemEntity(
    id = id,
    clave = name,
    nombre = displayName,
    stackSize = stackSize,
)

/** Entity (Room) -> dominio (UI). */
fun ItemEntity.toDomain() = Item(
    id = id,
    clave = clave,
    nombre = nombre,
    stackSize = stackSize,
    descripcion = descripcion,
    imagenDetalle = imagenDetalle,
    esFavorito = esFavorito,
)
