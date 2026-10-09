package com.reyes.clinicasaludplus

import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.util.FechaUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class RepositorioCitasTest {

    private val medico = Repositorio.obtenerMedico("m5")!! // Lun-Vie, mañana y tarde

    @Before
    fun iniciarSesion() {
        // Los usuarios no se precargan: se registra uno de prueba la primera vez
        if (!Repositorio.iniciarSesion("prueba@correo.com", "prueba123")) {
            assertTrue(Repositorio.registrarUsuario("Usuario Prueba", "911111111", "prueba@correo.com", "prueba123"))
            assertNull(Repositorio.usuarioActual) // registrarse no inicia sesión
            assertTrue(Repositorio.iniciarSesion("prueba@correo.com", "prueba123"))
        }
    }

    private fun proximaFechaDeAtencion(): String =
        FechaUtils.obtenerDiasAtencion(medico.diasAtencion, 1).first().fecha.toString() // futuro, sin depender de la hora actual

    @Test
    fun cadaMedicoTieneSedeTelefonoCodigoYHorarios() {
        Repositorio.sedes().forEach { sede ->
            assertTrue(Repositorio.buscarMedicos(sedeId = sede.id).isNotEmpty())
        }
        Repositorio.buscarMedicos().forEach {
            assertNotNull(Repositorio.obtenerSede(it.sedeId))
            assertEquals(9, it.telefono.length)
            assertTrue(it.codigo.isNotBlank())
            assertTrue(it.horas.isNotEmpty() && it.diasAtencion.isNotEmpty())
        }
    }

    @Test
    fun citaAgendadaYaNoApareceComoDisponible() {
        val fecha = proximaFechaDeAtencion()
        val hora = Repositorio.horariosDisponibles(medico.id, fecha).first { it == "17:00" || it == "16:00" }

        assertNull(Repositorio.validarCita(medico.id, fecha, hora))
        assertTrue(Repositorio.agendarCita(medico.id, medico.especialidadId, fecha, hora))

        assertFalse(hora in Repositorio.horariosDisponibles(medico.id, fecha))
        assertFalse(Repositorio.agendarCita(medico.id, medico.especialidadId, fecha, hora))

        // Al cancelar el horario vuelve a estar libre
        Repositorio.cancelarCita(Repositorio.ultimaCita!!.id)
        assertTrue(hora in Repositorio.horariosDisponibles(medico.id, fecha))
    }

    @Test
    fun rechazaDiasYHorasFueraDelHorarioDelMedico() {
        val fecha = proximaFechaDeAtencion()
        assertNotNull(Repositorio.validarCita(medico.id, fecha, "03:00"))

        // m6 solo atiende Lun-Mié-Vie en la mañana
        val pediatra = Repositorio.obtenerMedico("m6")!!
        val martes = generateSequence(LocalDate.now().plusDays(1)) { it.plusDays(1) }
            .first { it.dayOfWeek == java.time.DayOfWeek.TUESDAY }.toString()
        assertTrue(Repositorio.horariosDisponibles(pediatra.id, martes).isEmpty())
        assertNotNull(Repositorio.validarCita(pediatra.id, martes, "09:00"))
        assertNotNull(Repositorio.validarCita(medico.id, "no-es-fecha", "09:00"))
        assertNotNull(Repositorio.validarCita("inexistente", fecha, "09:00"))
    }
}
