package com.reyes.lavacampus.data.repository

import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.local.entity.Usuario
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.ClienteResumen
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.IngresoServicio
import com.reyes.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow

interface LavaRepository {
    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?
    suspend fun obtenerUsuarioPorRol(rol: String): Usuario?
    suspend fun existeCorreo(correo: String): Boolean
    suspend fun existeCodigo(codigo: String): Boolean
    /** Guarda una cuenta nueva y la devuelve con su id. */
    suspend fun crearUsuario(usuario: Usuario): Usuario
    suspend fun actualizarNombre(id: Int, nombre: String)
    suspend fun actualizarClave(id: Int, clave: String)
    fun observarClientes(): Flow<List<ClienteResumen>>

    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneDetalles(id: Int): Boolean

    // Pedidos
    suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long
    suspend fun contarActivos(usuarioId: Int): Int
    fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidos(): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?)
    suspend fun marcarPagado(id: Int, pagado: Boolean)
    fun observarCasillerosOcupados(): Flow<List<String>>

    // Panell
    fun observarPedidosDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>>

    /** Pedidos registrados desde esa fecha (sin cancelados). */
    fun observarPedidosDesde(fechaInicio: String): Flow<Int>

    /** Ids de los pedidos cuya fecha de entrega ya paso y no estan ENTREGADO ni CANCELADO. */
    fun observarPedidosAtrasados(hoy: String): Flow<List<Int>>
}
