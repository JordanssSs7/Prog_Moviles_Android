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
    /** null = aún no se descargó; Receta sin filas = el ítem no se fabrica. */
    val receta: Receta? = null,
    /** Nombre original en inglés (sirve para buscar y para pedir el ícono a la wiki). */
    val nombreIngles: String = nombre,
    /** true si el nombre ya viene en español (false = datos viejos, falta sincronizar). */
    val traducido: Boolean = true,
) {
    val categoria: Categoria get() = categoriaDe(clave)

    /**
     * Imágenes a probar en orden hasta que una cargue: textura de ítem, de bloque, ícono de la wiki
     * (que existe para casi todo) y variantes de textura de bloque.
     */
    val imagenesCandidatas: List<String>
        get() = listOf(
            "$BASE_ASSETS/items/$clave.png",
            "$BASE_ASSETS/blocks/$clave.png",
            "https://minecraft.wiki/w/Special:FilePath/Invicon_${nombreIngles.replace(' ', '_')}.png",
            "$BASE_ASSETS/blocks/${clave}_side.png",
            "$BASE_ASSETS/blocks/${clave}_top.png",
        )

    private companion object {
        const val BASE_ASSETS =
            "https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.20.2"
    }
}

data class Totales(val guardados: Int, val favoritos: Int)
