package com.reyes.minecraft.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    /** Endpoint 1: lista completa de ítems (PrismarineJS/minecraft-data). */
    @GET("PrismarineJS/minecraft-data/master/data/pc/1.20.2/items.json")
    suspend fun obtenerItems(): List<ItemDto>

    /** Endpoint 2: detalle de un ítem (descripción e imagen) en minecraft.wiki. */
    @GET
    suspend fun obtenerDetalle(
        @Url url: String = WIKI_URL,
        @Query("titles") titulo: String,
        @Query("action") accion: String = "query",
        @Query("prop") prop: String = "extracts|pageimages",
        @Query("exintro") intro: Int = 1,
        @Query("explaintext") textoPlano: Int = 1,
        @Query("pithumbsize") tamano: Int = 300,
        @Query("redirects") redirecciones: Int = 1,
        @Query("format") formato: String = "json",
    ): WikiRespuestaDto

    companion object {
        const val WIKI_URL = "https://minecraft.wiki/api.php"
    }
}
