package com.reyes.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio" // estudiante (Paso 7)
    const val PANEL = "panel" // administrador (Paso 11)
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val DETALLE_PEDIDO = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
    const val BANDEJA = "bandeja?estado={estado}"
    fun bandeja(estado: String = "") = "bandeja?estado=$estado"
    const val GESTION = "gestion/{id}"
    fun gestion(id: Int) = "gestion/$id"
    const val SERVICIOS = "servicios"
    // TODO: las demas rutas se agregan en los pasos siguientes
}
