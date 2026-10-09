package com.reyes.clinicasaludplus.data.model

data class Medico(
    val id: String,
    val nombre: String,
    val especialidadId: String,
    val cmp: String,            // Número de colegiatura (CMP)
    val calificacion: Double,
    val precioConsulta: Double,
    val fotoRes: Int = 0,
    val profesion: String = "", // Ej: "Ginecóloga"
    val resenas: Int = 0,       // Cantidad de reseñas mostradas junto a la calificación
    val disponibilidad: String = "Disponible hoy"
)
