package com.clinicasaludplus.data.model

data class Especialidad(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val iconoNombre: String, // Referencia o identificador de icono
    val esDestacada: Boolean = false
)