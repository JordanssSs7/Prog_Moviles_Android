package com.reyes.minecraft.data.conectividad

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/** Abstracción para que los ViewModels sepan si hay internet sin conocer Context ni ConnectivityManager. */
interface Conectividad {
    fun hayInternet(): Boolean
}

class AndroidConectividad(private val context: Context) : Conectividad {
    override fun hayInternet(): Boolean {
        val administrador = context.getSystemService(ConnectivityManager::class.java) ?: return false
        val red = administrador.activeNetwork ?: return false
        val capacidades = administrador.getNetworkCapabilities(red) ?: return false
        return capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
