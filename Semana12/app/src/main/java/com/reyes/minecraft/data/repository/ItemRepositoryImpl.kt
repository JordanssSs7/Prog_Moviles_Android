package com.reyes.minecraft.data.repository

import com.reyes.minecraft.data.local.ItemDao
import com.reyes.minecraft.data.remote.ApiService
import com.reyes.minecraft.data.toDomain
import com.reyes.minecraft.data.toEntity
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
) : ItemRepository {

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
        val entidades = api.obtenerItems().filter { it.name != "air" }.map { it.toEntity() }
        dao.sincronizar(entidades)
        ResultadoRed.Exito(entidades.size)
    }

    override suspend fun cargarDetalle(id: Int): ResultadoRed {
        val item = dao.observarPorId(id).first() ?: return ResultadoRed.ErrorDatos
        if (item.descripcion != null) return ResultadoRed.Exito() // ya estaba guardado
        return llamarRed {
            val pagina = api.obtenerDetalle(titulo = item.nombre)
                .query?.pages?.values?.firstOrNull { it.missing != true }
            dao.guardarDetalle(id, pagina?.extract, pagina?.thumbnail?.url)
            ResultadoRed.Exito()
        }
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)

    override suspend fun limpiarCache() = dao.borrarTodo()

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
