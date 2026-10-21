package com.reyes.lavacampus.model

/** Fila de la lista de clientes: pedidos (sin cancelados) y gastado (solo pagados). */
data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val pedidos: Int,
    val gastado: Double
)
