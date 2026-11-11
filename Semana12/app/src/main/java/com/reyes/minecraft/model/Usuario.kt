package com.reyes.minecraft.model

enum class Rol { ADMIN, USUARIO }

/** [jugador] es el nombre de Minecraft; con él se pide la cabeza (avatar) del usuario. */
data class Usuario(
    val uid: String,
    val correo: String,
    val rol: Rol,
    val jugador: String = "Steve",
)
