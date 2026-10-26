package com.reyes.lavacampus

import com.reyes.lavacampus.model.validarCambioClave
import com.reyes.lavacampus.model.validarClaveNueva
import com.reyes.lavacampus.model.validarNombre
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CuentaTest {

    // ---- Editar nombre ----

    @Test fun nombreValido_seAcepta() {
        assertNull(validarNombre("Rosa Quispe"))
        assertNull(validarNombre("  Ana  "))
    }

    @Test fun nombreVacioOMuyCorto_seRechaza() {
        assertEquals("Escribe tu nombre completo", validarNombre(""))
        assertEquals("Escribe tu nombre completo", validarNombre("   "))
        assertEquals("Escribe tu nombre completo", validarNombre("Al"))
    }

    // ---- Cambiar contrasena ----

    private fun cambiar(
        guardada: String = "Rosa#2025",
        actual: String = "Rosa#2025",
        nueva: String = "Nueva#2026",
        confirmacion: String = nueva
    ) = validarCambioClave(guardada, actual, nueva, confirmacion)

    @Test fun cambioCorrecto_noHayError() = assertNull(cambiar())

    @Test fun camposVacios_pideCompletar() {
        assertEquals("Completa todos los campos", cambiar(actual = ""))
        assertEquals("Completa todos los campos", cambiar(nueva = "", confirmacion = ""))
        assertEquals("Completa todos los campos", cambiar(confirmacion = ""))
    }

    @Test fun claveActualIncorrecta_seRechaza() =
        assertEquals("La contraseña actual no es correcta", cambiar(actual = "otra"))

    @Test fun claveNuevaIgualALaActual_seRechaza() =
        assertEquals("La nueva contraseña debe ser distinta de la actual", cambiar(nueva = "Rosa#2025"))

    @Test fun claveNuevaDebil_seRechaza() {
        assertEquals("La contraseña debe tener una mayúscula, un número y un carácter especial", cambiar(nueva = "nueva2026"))
        assertEquals("La contraseña es débil: usa al menos 6 caracteres", cambiar(nueva = "Ab1!"))
    }

    @Test fun confirmacionDistinta_seRechaza() =
        assertEquals("Las contraseñas no coinciden", cambiar(nueva = "Nueva#2026", confirmacion = "Nueva#2027"))

    @Test fun claveNueva_mismasReglasQueElRegistro() {
        assertNull(validarClaveNueva("ABC12!", "ABC12!")) // media
        assertEquals("La contraseña no puede tener espacios", validarClaveNueva("Abc 12!", "Abc 12!"))
    }
}
