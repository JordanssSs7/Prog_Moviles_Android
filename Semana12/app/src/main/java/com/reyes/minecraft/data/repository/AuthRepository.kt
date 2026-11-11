package com.reyes.minecraft.data.repository

import com.reyes.minecraft.model.Usuario

sealed interface ResultadoLogin {
    data class Exito(val usuario: Usuario) : ResultadoLogin
    data object CredencialesInvalidas : ResultadoLogin
    data object SinConexion : ResultadoLogin
    data object SinRol : ResultadoLogin
    data class Otro(val mensaje: String) : ResultadoLogin
}

interface AuthRepository {
    /** Si Firebase conserva una sesión abierta, devuelve el usuario con su rol; si no, null. */
    suspend fun restaurarSesion(): Usuario?
    suspend fun iniciarSesion(correo: String, clave: String): ResultadoLogin
    fun cerrarSesion()
}
