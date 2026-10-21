package com.reyes.lavacampus.model

/** Resultado de la consulta GROUP BY del panel: ingreso total por servicio. */
data class IngresoServicio(
    val nombre: String,
    val total: Double
)
