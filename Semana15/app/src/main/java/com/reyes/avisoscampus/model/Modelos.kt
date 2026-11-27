package com.reyes.avisoscampus.model

enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión"),
    CARGADOR("Cargador") // Mejora 3
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String,
    // Mejora 1: si es urgente se muestra por el canal "Urgentes" (emergente)
    val urgente: Boolean = false,
    // Mejora 2: texto completo que se ve al expandir la notificación
    val detalle: String? = null
)

sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}
