package com.reyes.minecraft.model

/** Modelo de dominio: lo único que ve la UI. No conoce Retrofit ni Room. */
data class Item(
    val id: Int,
    val clave: String,
    val nombre: String,
    val stackSize: Int?,
    val descripcion: String?,
    val imagenDetalle: String?,
    val esFavorito: Boolean,
) {
    /** Textura del ítem; si no existe como ítem se prueba como bloque. */
    val imagenesCandidatas: List<String>
        get() = listOf(
            "$BASE_ASSETS/items/$clave.png",
            "$BASE_ASSETS/blocks/$clave.png",
        )

    private companion object {
        const val BASE_ASSETS =
            "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.20.2"
    }
}

data class Totales(val guardados: Int, val favoritos: Int)
