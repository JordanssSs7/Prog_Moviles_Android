package com.reyes.minecraft.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemDao {

    @Query("SELECT * FROM items ORDER BY nombre")
    abstract fun observarTodos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY nombre")
    abstract fun observarFavoritos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    abstract fun observarPorId(id: Int): Flow<ItemEntity?>

    @Query("SELECT COUNT(*) FROM items")
    abstract fun contarTodos(): Flow<Int>

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    abstract fun contarFavoritos(): Flow<Int>

    @Query("SELECT * FROM items")
    abstract suspend fun obtenerTodosUnaVez(): List<ItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarTodos(items: List<ItemEntity>)

    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    abstract suspend fun alternarFavorito(id: Int)

    @Query("UPDATE items SET descripcion = :descripcion, imagenDetalle = :imagen WHERE id = :id")
    abstract suspend fun guardarDetalle(id: Int, descripcion: String?, imagen: String?)

    @Query("DELETE FROM items")
    abstract suspend fun borrarTodo()

    /**
     * Guarda lo descargado SIN perder los favoritos ni los detalles ya cacheados:
     * cada fila nueva se mezcla con la que ya existía antes de reemplazarla.
     */
    @Transaction
    open suspend fun sincronizar(nuevos: List<ItemEntity>) {
        val existentes = obtenerTodosUnaVez().associateBy { it.id }
        insertarTodos(nuevos.map { nuevo ->
            val previo = existentes[nuevo.id]
            if (previo == null) nuevo
            else nuevo.copy(
                esFavorito = previo.esFavorito,
                descripcion = previo.descripcion,
                imagenDetalle = previo.imagenDetalle,
            )
        })
    }
}
