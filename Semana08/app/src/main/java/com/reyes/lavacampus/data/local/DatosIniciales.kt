package com.reyes.lavacampus.data.local

import com.reyes.lavacampus.data.local.entity.DetallePedido
import com.reyes.lavacampus.data.local.entity.Pedido
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.model.descuentoDeLinea
import java.time.DayOfWeek
import java.time.LocalDate

/** Datos ficticios (Ley 29733). Se cargan solo la primera vez. */
object DatosIniciales {

    private val servicios = listOf(
        Servicio(
            id = 1, nombre = "Lavado y secado", descripcion = "Lavado y secado por kilo",
            unidad = "KG", precio = 8.0, horasEntrega = 24, icono = "wash"
        ),
        Servicio(
            id = 2, nombre = "Planchado", descripcion = "Planchado por prenda",
            unidad = "PRENDA", precio = 2.5, horasEntrega = 48, icono = "iron"
        ),
        Servicio(
            id = 3, nombre = "Lavado en seco", descripcion = "Para prendas delicadas",
            unidad = "PRENDA", precio = 12.0, horasEntrega = 48, icono = "shirt"
        ),
        Servicio(
            id = 4, nombre = "Edredón / frazada", descripcion = "Lavado de ropa de cama",
            unidad = "UNIDAD", precio = 20.0, horasEntrega = 48, activo = false, icono = "wash"
        )
    )

    private val usuarios = listOf(
        Usuario(1, "Ana Torres", "U202310234", "ana@correo.test", "1234", Rol.CLIENTE.name),
        Usuario(2, "Luis Paredes", "U202310871", "luis@correo.test", "1234", Rol.CLIENTE.name),
        Usuario(3, "Marta Salas", "U202411093", "marta@correo.test", "1234", Rol.CLIENTE.name),
        Usuario(4, "Administrador", "", "admin@lavacampus.test", "admin", Rol.ADMIN.name)
    )

    suspend fun sembrar(db: LavaDatabase) {
        if (db.usuarioDao().contar() != 0) return

        db.servicioDao().insertarTodos(servicios)
        db.usuarioDao().insertarTodos(usuarios)

        val hoy = LocalDate.now()
        // Dias desde el lunes (lunes = 0): asi los pedidos "de la semana" siempre caen en esta semana
        val desdeLunes = hoy.dayOfWeek.value - 1
        fun enSemana(n: Int): LocalDate = hoy.minusDays(minOf(n, desdeLunes).toLong())

        // Ana (id 1)
        sembrarPedido(db, 1, enSemana(2), EstadoPedido.ENTREGADO, null, true, listOf(1 to 3.0, 2 to 4.0))
        sembrarPedido(db, 1, enSemana(1), EstadoPedido.LISTO, "C-05", false, listOf(1 to 4.0))
        sembrarPedido(db, 1, hoy, EstadoPedido.RECIBIDO, null, false, listOf(1 to 2.5, 2 to 3.0))

        // Luis (id 2)
        sembrarPedido(db, 2, enSemana(3), EstadoPedido.ENTREGADO, null, true, listOf(1 to 5.0))
        sembrarPedido(db, 2, enSemana(1), EstadoPedido.LISTO, "C-07", false, listOf(2 to 6.0, 1 to 2.0))
        sembrarPedido(db, 2, hoy, EstadoPedido.EN_PROCESO, null, false, listOf(3 to 2.0))
        sembrarPedido(db, 2, hoy, EstadoPedido.RECIBIDO, null, false, listOf(3 to 1.0))
        sembrarPedido(db, 2, enSemana(2), EstadoPedido.CANCELADO, null, false, listOf(2 to 2.0))

        // Marta (id 3)
        sembrarPedido(db, 3, enSemana(2), EstadoPedido.ENTREGADO, null, true, listOf(1 to 6.0, 3 to 1.0))
        sembrarPedido(db, 3, hoy, EstadoPedido.EN_PROCESO, null, false, listOf(1 to 3.0))
        // Pedido atrasado: se registro hace 5 dias y su entrega ya vencio (sin cerrar)
        sembrarPedido(db, 3, hoy.minusDays(5), EstadoPedido.RECIBIDO, null, false, listOf(2 to 8.0))
    }

    /** Inserta un pedido y sus lineas; el total es la suma de los subtotales. */
    private suspend fun sembrarPedido(
        db: LavaDatabase,
        usuarioId: Int,
        registro: LocalDate,
        estado: EstadoPedido,
        casillero: String?,
        pagado: Boolean,
        lineas: List<Pair<Int, Double>> // servicioId a cantidad
    ) {
        // Triple(servicio, cantidad, descuento por volumen de la linea)
        val items = lineas.map { (servicioId, cantidad) ->
            val s = servicios.first { it.id == servicioId }
            Triple(s, cantidad, descuentoDeLinea(s.unidad, cantidad, s.precio))
        }
        val descuento = items.sumOf { it.third }
        var entrega = registro.plusDays((items.maxOf { it.first.horasEntrega } / 24).toLong())
        if (entrega.dayOfWeek == DayOfWeek.SUNDAY) entrega = entrega.plusDays(1)

        val pedidoId = db.pedidoDao().insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = registro.toString(),
                fechaEntrega = entrega.toString(),
                estado = estado.name,
                total = items.sumOf { (s, cantidad, desc) -> cantidad * s.precio - desc },
                descuento = descuento,
                casillero = casillero,
                pagado = pagado
            )
        ).toInt()

        db.detalleDao().insertarTodos(
            items.map { (s, cantidad, desc) ->
                DetallePedido(
                    pedidoId = pedidoId,
                    servicioId = s.id,
                    cantidad = cantidad,
                    precioUnitario = s.precio,
                    subtotal = cantidad * s.precio - desc // el subtotal ya lleva el descuento
                )
            }
        )
    }
}
