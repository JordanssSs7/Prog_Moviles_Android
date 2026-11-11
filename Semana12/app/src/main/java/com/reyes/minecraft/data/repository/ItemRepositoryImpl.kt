package com.reyes.minecraft.data.repository

import com.reyes.minecraft.data.SIN_RECETA
import com.reyes.minecraft.data.local.ItemDao
import com.reyes.minecraft.data.remote.ApiService
import com.reyes.minecraft.data.remote.RecetaDto
import com.reyes.minecraft.data.toDomain
import com.reyes.minecraft.data.toEntity
import com.reyes.minecraft.data.toTexto
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Totales
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao,
    private val comunidad: ComunidadRepository,
) : ItemRepository {

    /** recipes.json pesa ~500 KB: se descarga una sola vez y cada receta se guarda en Room. */
    @Volatile
    private var recetas: Map<String, List<RecetaDto>>? = null

    override fun observarItems(): Flow<List<Item>> =
        dao.observarTodos().map { lista -> lista.map { it.toDomain() } }

    override fun observarFavoritos(): Flow<List<Item>> =
        dao.observarFavoritos().map { lista -> lista.map { it.toDomain() } }

    override fun observarItem(id: Int): Flow<Item?> =
        dao.observarPorId(id).map { it?.toDomain() }

    override fun observarTotales(): Flow<Totales> =
        combine(dao.contarTodos(), dao.contarFavoritos()) { total, favoritos ->
            Totales(guardados = total, favoritos = favoritos)
        }

    override suspend fun sincronizar(): ResultadoRed = llamarRed {
        // "air" no es un ítem que el jugador pueda ver ni usar.
        val dtos = api.obtenerItems().filter { it.name != "air" }
        val idioma = cargarIdiomaEs()
        val entidades = dtos.map { it.toEntity(idioma) }
        dao.sincronizar(entidades)
        comunidad.publicarFavoritos(favoritosGuardados())
        ResultadoRed.Exito(entidades.size)
    }

    override suspend fun cargarDetalle(id: Int): ResultadoRed {
        val item = dao.observarPorId(id).first() ?: return ResultadoRed.ErrorDatos
        var resultado: ResultadoRed = ResultadoRed.Exito()

        // Endpoint 2: descripción e imagen. Primero la wiki en español; si no hay página, la de inglés.
        if (item.descripcion == null) {
            val r = llamarRed {
                val paginaEs = item.nombreEs?.let { titulo ->
                    api.obtenerDetalle(url = ApiService.WIKI_ES_URL, titulo = titulo)
                        .query?.pages?.values?.firstOrNull { it.missing != true }
                }
                val pagina = if (!paginaEs?.extract.isNullOrBlank()) paginaEs
                else api.obtenerDetalle(titulo = item.nombre)
                    .query?.pages?.values?.firstOrNull { it.missing != true }
                dao.guardarDetalle(id, pagina?.extract, pagina?.thumbnail?.url)
                ResultadoRed.Exito()
            }
            if (r !is ResultadoRed.Exito) resultado = r
        }

        // Endpoint 3: receta de crafteo (recipes.json)
        if (item.receta == null) {
            val r = llamarRed {
                val todas = recetas ?: api.obtenerRecetas().also { recetas = it }
                val texto = todas[id.toString()]?.firstOrNull()?.toTexto() ?: SIN_RECETA
                dao.guardarReceta(id, texto)
                ResultadoRed.Exito()
            }
            if (r !is ResultadoRed.Exito) resultado = r
        }
        return resultado
    }

    /** Si el archivo de idioma no se puede bajar, la app sigue con los nombres en inglés. */
    private suspend fun cargarIdiomaEs(): Map<String, String> =
        try {
            api.obtenerIdiomaEs()
        } catch (e: IOException) {
            emptyMap()
        } catch (e: HttpException) {
            emptyMap()
        } catch (e: SerializationException) {
            emptyMap()
        }

    override suspend fun alternarFavorito(id: Int) {
        dao.alternarFavorito(id)
        // También se publica en Firestore para que el admin pueda ver los ítems más marcados.
        dao.observarPorId(id).first()?.let { comunidad.publicarFavorito(id, it.nombreEs ?: it.nombre, it.esFavorito) }
    }

    override suspend fun limpiarCache() {
        // Los favoritos locales se borran, así que también se retiran de la nube.
        dao.obtenerTodosUnaVez().filter { it.esFavorito }.forEach {
            comunidad.publicarFavorito(it.id, it.nombreEs ?: it.nombre, false)
        }
        dao.borrarTodo()
    }

    private suspend fun favoritosGuardados(): Map<Int, String> =
        dao.obtenerTodosUnaVez().filter { it.esFavorito }.associate { it.id to (it.nombreEs ?: it.nombre) }

    /** Traduce los errores de red a un resultado; así el ViewModel no conoce IOException ni HttpException. */
    private suspend fun llamarRed(bloque: suspend () -> ResultadoRed): ResultadoRed =
        try {
            bloque()
        } catch (e: IOException) {
            ResultadoRed.SinConexion
        } catch (e: HttpException) {
            ResultadoRed.ErrorServidor(e.code())
        } catch (e: SerializationException) {
            ResultadoRed.ErrorDatos
        }
}
