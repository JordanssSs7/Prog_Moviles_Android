package com.reyes.minecraft.model

enum class Rol { ADMIN, USUARIO }

data class Usuario(val uid: String, val correo: String, val rol: Rol)
