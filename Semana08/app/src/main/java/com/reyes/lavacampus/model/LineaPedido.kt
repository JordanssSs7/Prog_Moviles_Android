package com.reyes.lavacampus.model

/**
 * Una linea que el estudiante arma en Nuevo pedido: servicio + cantidad.
 * [descuento] es el monto de descuento por volumen ya calculado por el ViewModel (0.0 si no aplica).
 */
data class LineaPedido(
    val servicioId: Int,
    val cantidad: Double,
    val descuento: Double = 0.0
)
