package com.reyes.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Estudiante
    const val INICIO = "inicio"
    const val NUEVO_PEDIDO = "nuevo_pedido?repetir={repetir}"
    /** [repetir] > 0 abre Nuevo pedido con las lineas de ese pedido ("Pedir de nuevo"). */
    fun nuevoPedido(repetir: Int = 0) = "nuevo_pedido?repetir=$repetir"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val PERFIL = "perfil"
    const val DETALLE_PEDIDO = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"

    // Administrador
    const val PANEL = "panel"
    const val BANDEJA = "bandeja?estado={estado}"
    fun bandeja(estado: String = "") = "bandeja?estado=$estado"
    const val GESTION = "gestion/{id}"
    fun gestion(id: Int) = "gestion/$id"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "servicio/{id}" // id 0 = nuevo servicio
    fun formularioServicio(id: Int) = "servicio/$id"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"
}
