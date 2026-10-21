package com.reyes.lavacampus.data.local

import com.reyes.lavacampus.data.local.entity.Servicio

object DatosIniciales {

    // TODO (Paso 3): sembrar tambien usuarios, pedidos y detalles
    suspend fun sembrar(db: LavaDatabase) {
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(
                listOf(
                    Servicio(
                        nombre = "Lavado y secado", descripcion = "Lavado y secado por kilo",
                        unidad = "KG", precio = 8.0, horasEntrega = 24, icono = "wash"
                    ),
                    Servicio(
                        nombre = "Planchado", descripcion = "Planchado por prenda",
                        unidad = "PRENDA", precio = 2.5, horasEntrega = 48, icono = "iron"
                    ),
                    Servicio(
                        nombre = "Lavado en seco", descripcion = "Para prendas delicadas",
                        unidad = "PRENDA", precio = 12.0, horasEntrega = 48, icono = "shirt"
                    ),
                    Servicio(
                        nombre = "Edredón / frazada", descripcion = "Lavado de ropa de cama",
                        unidad = "UNIDAD", precio = 20.0, horasEntrega = 48,
                        activo = false, icono = "wash"
                    )
                )
            )
        }
    }
}
