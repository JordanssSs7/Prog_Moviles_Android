package com.reyes.avisoscampus

import android.app.Application
import com.reyes.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Se crea una sola vez, al iniciar la app
        Notificador.crearCanal(this)
    }
}
