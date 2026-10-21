package com.reyes.lavacampus.data.repository

import com.reyes.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)

    // TODO: usuarios, pedidos y panel se agregan en los pasos siguientes
}
