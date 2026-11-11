package com.reyes.minecraft.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.Usuario
import kotlinx.coroutines.tasks.await

/**
 * El rol NO se decide en la app al iniciar sesión: se lee de Firestore en usuarios/{uid}.rol.
 * Quien se registra siempre recibe el rol "usuario"; un admin solo se crea a mano en la consola.
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AuthRepository {

    override suspend fun restaurarSesion(): Usuario? {
        val actual = auth.currentUser ?: return null
        return try {
            leerPerfil(actual.uid, actual.email.orEmpty())
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin =
        try {
            val usuario = auth.signInWithEmailAndPassword(correo, clave).await().user
                ?: return ResultadoLogin.Otro("No se pudo iniciar sesión")
            val perfil = leerPerfil(usuario.uid, usuario.email.orEmpty())
            if (perfil == null) {
                auth.signOut()
                ResultadoLogin.SinRol
            } else {
                ResultadoLogin.Exito(perfil)
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultadoLogin.CredencialesInvalidas
        } catch (e: FirebaseAuthInvalidUserException) {
            ResultadoLogin.CredencialesInvalidas
        } catch (e: FirebaseNetworkException) {
            ResultadoLogin.SinConexion
        } catch (e: Exception) {
            ResultadoLogin.Otro(e.localizedMessage ?: "Error desconocido")
        }

    override suspend fun registrar(correo: String, clave: String, jugador: String): ResultadoRegistro =
        try {
            val usuario = auth.createUserWithEmailAndPassword(correo, clave).await().user
                ?: return ResultadoRegistro.Otro("No se pudo crear la cuenta")
            try {
                db.collection("usuarios").document(usuario.uid).set(
                    mapOf("rol" to "usuario", "jugador" to jugador, "correo" to correo)
                ).await()
            } catch (e: Exception) {
                // Sin perfil no hay rol: se deshace la cuenta para no dejar un usuario "huérfano".
                runCatching { usuario.delete().await() }
                auth.signOut()
                return ResultadoRegistro.Otro("No se pudo guardar tu perfil. Revisa las reglas de Firestore.")
            }
            auth.signOut() // debe iniciar sesión con la cuenta que acaba de crear
            ResultadoRegistro.Exito
        } catch (e: FirebaseAuthUserCollisionException) {
            ResultadoRegistro.CorreoEnUso
        } catch (e: FirebaseAuthWeakPasswordException) {
            ResultadoRegistro.ClaveDebil
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultadoRegistro.CorreoInvalido
        } catch (e: FirebaseNetworkException) {
            ResultadoRegistro.SinConexion
        } catch (e: Exception) {
            ResultadoRegistro.Otro(e.localizedMessage ?: "Error desconocido")
        }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerPerfil(uid: String, correo: String): Usuario? {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = when (doc.getString("rol")?.lowercase()) {
            "admin" -> Rol.ADMIN
            "usuario" -> Rol.USUARIO
            else -> return null
        }
        // Usuarios creados a mano en la consola pueden no tener "jugador": se usa el inicio del correo.
        val jugador = doc.getString("jugador")?.takeIf { it.isNotBlank() }
            ?: correo.substringBefore("@").filter { it.isLetterOrDigit() || it == '_' }.take(16).ifBlank { "Steve" }
        return Usuario(uid, correo, rol, jugador)
    }
}
