package com.reyes.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Mejora 3: escucha cuando se conecta o desconecta el cargador.
 * onReceive es corto: solo dice si se conectó y se lo pasa a quien lo creó.
 */
class CargadorReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> alCambiar(true)
            Intent.ACTION_POWER_DISCONNECTED -> alCambiar(false)
        }
    }
}
