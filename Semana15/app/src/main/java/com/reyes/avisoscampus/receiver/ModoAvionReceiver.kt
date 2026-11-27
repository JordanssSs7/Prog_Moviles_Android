package com.reyes.avisoscampus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Escucha el anuncio del sistema cuando cambia el modo avión.
 * onReceive es corto: solo lee el estado y se lo pasa a quien lo creó.
 */
class ModoAvionReceiver(
    private val alCambiar: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            alCambiar(intent.getBooleanExtra("state", false))
        }
    }
}
