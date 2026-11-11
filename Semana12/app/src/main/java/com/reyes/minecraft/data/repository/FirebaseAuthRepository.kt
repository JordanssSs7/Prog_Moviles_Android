package com.reyes.minecraft.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.Usuario
import kotlinx.coroutines.tasks.await

/**
 * El rol NO se decide en la app: se lee de Firestore en usuarios/{uid}.rol ("admin" o "usuario").
 */
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AuthRepository {

    override suspend fun restaurarSesion(): Usuario? {
        val actual = auth.currentUser ?: return null
        return try {
            leerRol(actual.uid)?.let { Usuario(actual.uid, actual.email.orEmpty(), it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin =
        try {
            val usuario = auth.signInWithEmailAndPassword(correo, clave).await().user
                ?: return ResultadoLogin.Otro("No se pudo iniciar sesión")
            val rol = leerRol(usuario.uid)
            if (rol == null) {
                auth.signOut()
                ResultadoLogin.SinRol
            } else {
                ResultadoLogin.Exito(Usuario(usuario.uid, usuario.email.orEmpty(), rol))
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

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerRol(uid: String): Rol? {
        val texto = db.collection("usuarios").document(uid).get().await().getString("rol")
        return when (texto?.lowercase()) {
            "admin" -> Rol.ADMIN
            "usuario" -> Rol.USUARIO
            else -> null
        }
    }
}
