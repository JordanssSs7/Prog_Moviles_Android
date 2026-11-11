package com.reyes.minecraft.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Endpoint 1 (lista): un elemento de items.json. */
@Serializable
data class ItemDto(
    val id: Int,
    val name: String,
    val displayName: String,
    val stackSize: Int? = null,
)

/** Endpoint 2 (detalle): respuesta de la API de MediaWiki de minecraft.wiki. */
@Serializable
data class WikiRespuestaDto(val query: WikiQueryDto? = null)

@Serializable
data class WikiQueryDto(val pages: Map<String, WikiPaginaDto> = emptyMap())

@Serializable
data class WikiPaginaDto(
    val title: String? = null,
    val extract: String? = null,
    val thumbnail: WikiMiniaturaDto? = null,
    val missing: Boolean? = null,
)

@Serializable
data class WikiMiniaturaDto(@SerialName("source") val url: String)
