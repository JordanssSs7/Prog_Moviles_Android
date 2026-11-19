package com.reyes.centrotareas.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    // Mejora 1: 100 vueltas de 40 ms (≈ 4 s). El flow se ejecuta en Dispatchers.Default
    // gracias a flowOn; quien lo recolecta (el ViewModel) recibe los valores en su hilo.
    override fun progresoTarea(): Flow<Float> = flow {
        Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
        for (i in 1..100) {
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    // Paso 5: cálculo pesado en Dispatchers.Default (hilos para CPU)
    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        for (n in 2..hasta) {
            ensureActive() // permite cancelar a mitad del cálculo
            if (esPrimo(n)) cantidad++
        }
        cantidad
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var d = 2
        while (d.toLong() * d <= n) { // hasta la raíz de n, sin desbordes
            if (n % d == 0) return false
            d++
        }
        return true
    }
}
