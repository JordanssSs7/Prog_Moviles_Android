package com.reyes.minecraft

import com.reyes.minecraft.data.repository.AuthRepository
import com.reyes.minecraft.data.repository.ResultadoLogin
import com.reyes.minecraft.data.repository.ResultadoRegistro
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.ui.viewmodel.SesionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

private class AuthFalso(
    var resultadoRegistro: ResultadoRegistro = ResultadoRegistro.Exito,
    var resultadoLogin: ResultadoLogin = ResultadoLogin.CredencialesInvalidas,
) : AuthRepository {
    var registros = 0
    override suspend fun restaurarSesion(): Usuario? = null
    override suspend fun iniciarSesion(correo: String, clave: String) = resultadoLogin
    override suspend fun registrar(correo: String, clave: String, jugador: String): ResultadoRegistro {
        registros++
        return resultadoRegistro
    }
    override fun cerrarSesion() {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class SesionViewModelTest {

    @Before
    fun antes() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun despues() = Dispatchers.resetMain()

    @Test
    fun `registro valido deja la cuenta creada y no abre sesion`() = runTest {
        val auth = AuthFalso()
        val vm = SesionViewModel(auth)

        vm.registrar("alex@correo.com", "123456", "123456", "Alex_01")

        assertEquals("alex@correo.com", vm.cuentaCreada.value)
        assertEquals("alex@correo.com", vm.confirmacionRegistro.value) // la UI muestra "¡Cuenta creada!"
        vm.confirmacionRegistroMostrada()
        assertNull(vm.confirmacionRegistro.value)
        assertNull((vm.estado.value as UiState.Exito).datos) // sigue sin sesion: debe ir al login
        assertEquals(1, auth.registros)
    }

    @Test
    fun `registro con contrasenas distintas no llama al repositorio`() = runTest {
        val auth = AuthFalso()
        val vm = SesionViewModel(auth)

        vm.registrar("alex@correo.com", "123456", "654321", "Alex_01")

        assertEquals("Las contraseñas no coinciden", vm.errorRegistro.value)
        assertEquals(0, auth.registros)
    }

    @Test
    fun `registro con jugador o correo invalido muestra error`() = runTest {
        val auth = AuthFalso()
        val vm = SesionViewModel(auth)

        vm.registrar("alex@correo.com", "123456", "123456", "a!")
        assertNotNull(vm.errorRegistro.value)

        vm.registrar("sin-arroba", "123456", "123456", "Alex_01")
        assertEquals("Escribe un correo válido", vm.errorRegistro.value)
        assertEquals(0, auth.registros)
    }

    @Test
    fun `correo ya usado muestra el mensaje y no crea cuenta`() = runTest {
        val vm = SesionViewModel(AuthFalso(resultadoRegistro = ResultadoRegistro.CorreoEnUso))

        vm.registrar("alex@correo.com", "123456", "123456", "Alex_01")

        assertEquals("Ese correo ya tiene una cuenta", vm.errorRegistro.value)
        assertNull(vm.cuentaCreada.value)
    }

    @Test
    fun `login correcto abre la sesion con el rol`() = runTest {
        val usuario = Usuario("u1", "alex@correo.com", Rol.USUARIO, "Alex_01")
        val vm = SesionViewModel(AuthFalso(resultadoLogin = ResultadoLogin.Exito(usuario)))

        vm.iniciarSesion("alex@correo.com", "123456")

        assertEquals(usuario, (vm.estado.value as UiState.Exito).datos)
        assertEquals("Alex_01", vm.bienvenida.value) // la UI muestra "¡Bienvenido!" una vez
        vm.bienvenidaMostrada()
        assertNull(vm.bienvenida.value)
    }
}
