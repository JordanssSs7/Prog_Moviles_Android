package com.reyes.lavacampus.model

/** Una linea que el estudiante arma en Nuevo pedido: servicio + cantidad. */
data class LineaPedido(
    val servicioId: Int,
    val cantidad: Double
)
