package com.reyes.clinicasaludplus.data.model

data class Cita(
    val id: String,
    val usuarioId: String,
    val medicoId: String,
    val especialidadId: String,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada" // Confirmada, Cancelada, etc.
)