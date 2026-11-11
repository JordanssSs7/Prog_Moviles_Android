package com.reyes.minecraft.notificacion

/** Abstracción para que los ViewModels avisen al usuario sin conocer Context ni NotificationManager. */
interface Notificador {
    fun sincronizacionTerminada(cantidad: Int)
}
