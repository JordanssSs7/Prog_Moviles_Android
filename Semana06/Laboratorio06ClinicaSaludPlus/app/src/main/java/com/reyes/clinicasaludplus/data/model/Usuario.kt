package com.clinicasaludplus.data.model

data class Usuario(
    val id: String,
    val nombreCompleto: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)