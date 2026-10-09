package com.reyes.clinicasaludplus.data.model

import java.time.DayOfWeek

data class Medico(
    val id: String,
    val nombre: String,
    val especialidadId: String,
    val codigo: String,                 // Código del médico (colegiatura)
    val sedeId: String,
    val telefono: String,
    val calificacion: Double,
    val precioConsulta: Double,
    val fotoRes: Int = 0,
    val profesion: String = "",         // Ej: "Ginecóloga"
    val resenas: Int = 0,
    val diasAtencion: Set<DayOfWeek>,   // Días de la semana en que atiende
    val horas: List<String>             // Horas de inicio de cada consulta ("HH:mm")
)
