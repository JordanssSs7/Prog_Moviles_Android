package com.reyes.minecraft.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.Usuario
import kotlinx.coroutines.tasks.await

/**
 * Colección "favoritos": un documento por persona e ítem, con id "uid_itemId" y los campos uid, itemId y nombre.
 * Así el admin puede contar cuántas personas marcaron cada ítem sin tocar los datos privados de nadie.
 */
class FirestoreComunidadRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ComunidadRepository {

    override fun publicarFavorito(itemId: Int, nombre: String, marcado: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        val doc = db.collection("favoritos").document("${uid}_$itemId")
        // Sin await: si no hay internet, Firestore lo deja en cola y lo envía luego.
        if (marcado) doc.set(mapOf("uid" to uid, "itemId" to itemId, "nombre" to nombre)) else doc.delete()
    }

    override fun publicarFavoritos(favoritos: Map<Int, String>) {
        val uid = auth.currentUser?.uid ?: return
        if (favoritos.isEmpty()) return
        val lote = db.batch()
        favoritos.forEach { (itemId, nombre) ->
            lote.set(
                db.collection("favoritos").document("${uid}_$itemId"),
                mapOf("uid" to uid, "itemId" to itemId, "nombre" to nombre),
            )
        }
        lote.commit()
    }

    override suspend fun obtenerUsuarios(): ResultadoComunidad<List<Usuario>> = leer {
        db.collection("usuarios").get().await().documents.map { doc ->
            val correo = doc.getString("correo").orEmpty()
            Usuario(
                uid = doc.id,
                correo = correo,
                rol = if (doc.getString("rol")?.lowercase() == "admin") Rol.ADMIN else Rol.USUARIO,
                jugador = doc.getString("jugador")?.takeIf { it.isNotBlank() }
                    ?: correo.substringBefore("@").ifBlank { "Sin nombre" },
            )
        }.sortedWith(compareByDescending<Usuario> { it.rol == Rol.ADMIN }.thenBy { it.jugador.lowercase() })
    }

    override suspend fun obtenerRanking(): ResultadoComunidad<List<Popular>> = leer {
        db.collection("favoritos").get().await().documents
            .mapNotNull { doc ->
                val itemId = doc.getLong("itemId")?.toInt() ?: return@mapNotNull null
                itemId to doc.getString("nombre").orEmpty()
            }
            .groupBy({ it.first }, { it.second })
            .map { (itemId, nombres) -> Popular(itemId, nombres.first(), nombres.size) }
            .sortedByDescending { it.votos }
            .take(10)
    }

    private suspend fun <T> leer(bloque: suspend () -> T): ResultadoComunidad<T> =
        try {
            ResultadoComunidad.Exito(bloque())
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> ResultadoComunidad.SinPermiso
                FirebaseFirestoreException.Code.UNAVAILABLE -> ResultadoComunidad.SinConexion
                else -> ResultadoComunidad.Otro(e.localizedMessage ?: "Error de Firestore")
            }
        }
}
