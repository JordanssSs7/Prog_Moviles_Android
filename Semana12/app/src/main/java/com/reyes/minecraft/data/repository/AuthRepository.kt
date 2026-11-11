package com.reyes.minecraft.data.repository

import com.reyes.minecraft.model.Usuario

sealed interface ResultadoLogin {
    data class Exito(val usuario: Usuario) : ResultadoLogin
    data object CredencialesInvalidas : ResultadoLogin
    data object SinConexion : ResultadoLogin
    data object SinRol : ResultadoLogin
    data class Otro(val mensaje: String) : ResultadoLogin
}

sealed interface ResultadoRegistro {
    data object Exito : ResultadoRegistro
    data object CorreoEnUso : ResultadoRegistro
    data object CorreoInvalido : ResultadoRegistro
    data object ClaveDebil : ResultadoRegistro
    data object SinConexion : ResultadoRegistro
    data class Otro(val mensaje: String) : ResultadoRegistro
}

interface AuthRepository {
    /** Si Firebase conserva una sesión abierta, devuelve el usuario con su rol; si no, null. */
    suspend fun restaurarSesion(): Usuario?
    suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin

    /**
     * Crea la cuenta con rol "usuario" y su perfil en Firestore. No deja la sesión abierta:
     * después de registrarse la persona debe iniciar sesión con la cuenta recién creada.
     */
    suspend fun registrar(correo: String, clave: String, jugador: String): ResultadoRegistro
    fun cerrarSesion()
}
