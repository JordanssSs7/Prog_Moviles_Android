package com.reyes.murocampus.data

import com.reyes.murocampus.data.ContenidoDemo.conContenidoReal
import com.reyes.murocampus.data.remote.PostApi
import com.reyes.murocampus.model.Comentario
import com.reyes.murocampus.model.NuevoPost
import com.reyes.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    override suspend fun listar(): Result<List<Post>> = llamar {
        api.listar().map { it.conContenidoReal() }
    }

    override suspend fun detalle(id: Int): Result<Post> = llamar {
        api.detalle(id).conContenidoReal()
    }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = llamar {
        api.comentarios(id).map { it.conContenidoReal() }
    }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> = llamar {
        api.porUsuario(userId).map { it.conContenidoReal() }
    }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar {
        api.crear(nuevo)
    }

    override suspend fun borrar(id: Int): Result<Unit> = llamar {
        val respuesta = api.borrar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
