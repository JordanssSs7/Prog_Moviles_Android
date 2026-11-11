package com.reyes.minecraft.data

import com.reyes.minecraft.data.local.ItemEntity
import com.reyes.minecraft.data.remote.ItemDto
import com.reyes.minecraft.data.remote.RecetaDto
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Receta

/** Texto que se guarda cuando un ítem no tiene receta (así no se vuelve a pedir a la red). */
const val SIN_RECETA = "0|"

/**
 * DTO (red) -> Entity (Room). Un ítem recién descargado nunca es favorito.
 * [idioma] es el archivo es_es.json del juego: el nombre sale de "item.minecraft.x" o "block.minecraft.x".
 */
fun ItemDto.toEntity(idioma: Map<String, String> = emptyMap()) = ItemEntity(
    id = id,
    clave = name,
    nombre = displayName,
    stackSize = stackSize,
    nombreEs = idioma["item.minecraft.$name"] ?: idioma["block.minecraft.$name"],
)

/** Entity (Room) -> dominio (UI). */
fun ItemEntity.toDomain() = Item(
    id = id,
    clave = clave,
    nombre = nombreEs ?: nombre,
    nombreIngles = nombre,
    traducido = nombreEs != null,
    stackSize = stackSize,
    descripcion = descripcion,
    imagenDetalle = imagenDetalle,
    esFavorito = esFavorito,
    receta = receta?.toReceta(),
)

/** RecetaDto (red) -> texto para guardar en Room. Las recetas sin forma se acomodan de a 3 por fila. */
fun RecetaDto.toTexto(): String {
    val filas = inShape ?: ingredients.orEmpty().chunked(3)
    val celdas = filas.joinToString(";") { fila -> fila.joinToString(",") { (it ?: -1).toString() } }
    return "${result.count}|$celdas"
}

/** Texto de Room -> Receta de dominio. */
fun String.toReceta(): Receta {
    val cantidad = substringBefore("|").toIntOrNull() ?: 1
    val cuerpo = substringAfter("|", "")
    val filas = if (cuerpo.isEmpty()) emptyList() else cuerpo.split(";").map { fila ->
        fila.split(",").map { celda -> celda.toIntOrNull()?.takeIf { it >= 0 } }
    }
    return Receta(cantidad, filas)
}
