package com.reyes.lavacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.reyes.lavacampus.data.local.entity.DetallePedido
import com.reyes.lavacampus.data.local.entity.Pedido
import com.reyes.lavacampus.data.local.entity.Usuario

/** Un pedido con su estudiante y todas sus lineas (relacion uno a muchos anidada). */
data class PedidoConDetalle(
    @Embedded val pedido: Pedido,
    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: Usuario,
    @Relation(entity = DetallePedido::class, parentColumn = "id", entityColumn = "pedidoId")
    val detalles: List<DetalleConServicio>
)
