package com.reyes.minecraft

import android.app.Application
import android.content.Context
import com.reyes.minecraft.data.local.AppDatabase
import com.reyes.minecraft.data.remote.RetrofitClient
import com.reyes.minecraft.data.repository.AuthRepository
import com.reyes.minecraft.data.repository.FirebaseAuthRepository
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ItemRepositoryImpl
import com.reyes.minecraft.notificacion.AndroidNotificador
import com.reyes.minecraft.notificacion.Notificador

/** Crea las dependencias una sola vez. Los ViewModels reciben solo interfaces. */
class AppContainer(context: Context) {
    private val baseDatos = AppDatabase.crear(context)

    val itemRepository: ItemRepository = ItemRepositoryImpl(RetrofitClient.api, baseDatos.itemDao())
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val notificador: Notificador = AndroidNotificador(context)
}

class MinecraftApp : Application() {
    lateinit var contenedor: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = AppContainer(this)
    }
}
