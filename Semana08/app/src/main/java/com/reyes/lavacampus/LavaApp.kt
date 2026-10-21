package com.reyes.lavacampus

import android.app.Application
import com.reyes.lavacampus.data.local.DatosIniciales
import com.reyes.lavacampus.data.local.LavaDatabase
import com.reyes.lavacampus.data.repository.LavaRepository
import com.reyes.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LavaApp : Application() {

    val database: LavaDatabase by lazy { LavaDatabase.obtener(this) }
    val repository: LavaRepository by lazy { LavaRepositoryRoom(database) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch { DatosIniciales.sembrar(database) }
    }
}
