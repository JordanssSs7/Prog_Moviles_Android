package com.reyes.minecraft

import com.reyes.minecraft.data.conectividad.Conectividad
import com.reyes.minecraft.data.repository.ItemRepository
import com.reyes.minecraft.data.repository.ResultadoRed
import com.reyes.minecraft.model.Item
import com.reyes.minecraft.model.Totales
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.notificacion.Notificador
import com.reyes.minecraft.ui.viewmodel.ListaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private fun item(id: Int, nombre: String, favorito: Boolean = false) = Item(
    id = id, clave = nombre.lowercase(), nombre = nombre, stackSize = 64,
    descripcion = null, imagenDetalle = null, esFavorito = favorito,
)

/** Repositorio falso: simula Room con un StateFlow y la red con un resultado configurable. */
private class RepositorioFalso(
    inicial: List<Item> = emptyList(),
    var respuestaRed: ResultadoRed = ResultadoRed.Exito(0),
    private val remoto: List<Item> = emptyList(),
) : ItemRepository {
    val room = MutableStateFlow(inicial)
    var llamadasSync = 0

    override fun observarItems(): Flow<List<Item>> = room
    override fun observarFavoritos(): Flow<List<Item>> = room.map { l -> l.filter { it.esFavorito } }
    override fun observarItem(id: Int): Flow<Item?> = room.map { l -> l.firstOrNull { it.id == id } }
    override fun observarTotales(): Flow<Totales> = room.map { Totales(it.size, it.count { i -> i.esFavorito }) }

    override suspend fun sincronizar(): ResultadoRed {
        llamadasSync++
        if (respuestaRed is ResultadoRed.Exito) {
            room.value = remoto
            return ResultadoRed.Exito(remoto.size)
        }
        return respuestaRed
    }

    override suspend fun cargarDetalle(id: Int) = ResultadoRed.Exito()

    override suspend fun alternarFavorito(id: Int) {
        room.value = room.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun limpiarCache() {
        room.value = emptyList()
    }
}

private class ConectividadFalsa(var internet: Boolean = true) : Conectividad {
    override fun hayInternet() = internet
}

private class NotificadorFalso : Notificador {
    val avisos = mutableListOf<Int>()
    override fun sincronizacionTerminada(cantidad: Int) {
        avisos += cantidad
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    @Before
    fun antes() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun despues() = Dispatchers.resetMain()

    @Test
    fun `con Room vacio descarga, muestra los datos y notifica la cantidad`() = runTest {
        val repo = RepositorioFalso(remoto = listOf(item(1, "Stone"), item(2, "Dirt")))
        val notificador = NotificadorFalso()
        val vm = ListaViewModel(repo, notificador, ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        val estado = vm.uiState.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
        assertEquals(listOf(2), notificador.avisos)
    }

    @Test
    fun `sin internet y sin datos guardados muestra Error`() = runTest {
        val repo = RepositorioFalso(respuestaRed = ResultadoRed.SinConexion)
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test
    fun `sin internet pero con datos guardados muestra lo guardado y no descarga sola`() = runTest {
        val repo = RepositorioFalso(
            inicial = listOf(item(1, "Stone")),
            respuestaRed = ResultadoRed.SinConexion,
        )
        val notificador = NotificadorFalso()
        val vm = ListaViewModel(repo, notificador, ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        val estado = vm.uiState.value
        assertTrue(estado is UiState.Exito)
        assertEquals(1, (estado as UiState.Exito).datos.size)
        assertEquals(0, repo.llamadasSync)
        assertTrue(notificador.avisos.isEmpty())
    }

    @Test
    fun `reintentar despues de un error carga los datos`() = runTest {
        val repo = RepositorioFalso(
            respuestaRed = ResultadoRed.SinConexion,
            remoto = listOf(item(1, "Stone")),
        )
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
        assertTrue(vm.uiState.value is UiState.Error)

        repo.respuestaRed = ResultadoRed.Exito(1)
        vm.sincronizar()

        assertTrue(vm.uiState.value is UiState.Exito)
    }

    @Test
    fun `buscar filtra la lista por nombre`() = runTest {
        val repo = RepositorioFalso(inicial = listOf(item(1, "Stone"), item(2, "Diamond Pickaxe")))
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        vm.buscar("pick")

        val datos = (vm.uiState.value as UiState.Exito).datos
        assertEquals(listOf("Diamond Pickaxe"), datos.map { it.nombre })
    }

    @Test
    fun `marcar favorito actualiza el item`() = runTest {
        val repo = RepositorioFalso(inicial = listOf(item(1, "Stone")))
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa())
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        vm.alternarFavorito(1)

        val datos = (vm.uiState.value as UiState.Exito).datos
        assertTrue(datos.first().esFavorito)
    }

    @Test
    fun `abrir sin internet con datos guardados muestra la lista y activa el aviso`() = runTest {
        val repo = RepositorioFalso(inicial = listOf(item(1, "Stone")))
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa(internet = false))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        assertTrue(vm.uiState.value is UiState.Exito)
        assertTrue(vm.sinConexion.value)
    }

    @Test
    fun `abrir con internet no muestra el aviso de sin conexion`() = runTest {
        val repo = RepositorioFalso(inicial = listOf(item(1, "Stone")))
        val vm = ListaViewModel(repo, NotificadorFalso(), ConectividadFalsa(internet = true))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }

        assertEquals(false, vm.sinConexion.value)
    }
}
