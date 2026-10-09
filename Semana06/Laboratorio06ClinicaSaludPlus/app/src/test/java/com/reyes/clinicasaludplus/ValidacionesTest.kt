package com.reyes.clinicasaludplus

import com.reyes.clinicasaludplus.util.NivelContrasena
import com.reyes.clinicasaludplus.util.Validaciones
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionesTest {

    @Test
    fun contrasenaExigeMinimoOchoCaracteres() {
        assertNotNull(Validaciones.errorContrasena("abc123"))
        assertNull(Validaciones.errorContrasena("abcd1234"))
    }

    @Test
    fun fortalezaDeContrasena() {
        assertEquals(NivelContrasena.NINGUNO, Validaciones.nivelContrasena(""))
        assertEquals(NivelContrasena.DEBIL, Validaciones.nivelContrasena("Ab1!"))      // muy corta
        assertEquals(NivelContrasena.DEBIL, Validaciones.nivelContrasena("abcdefgh"))  // solo minúsculas
        assertEquals(NivelContrasena.MEDIA, Validaciones.nivelContrasena("Abcdefg1"))  // mayúscula + minúscula + número
        assertEquals(NivelContrasena.FUERTE, Validaciones.nivelContrasena("Abcdef1!"))  // los 4 tipos
    }

    @Test
    fun telefonoDeNueveDigitosQueEmpiezaEn9() {
        assertNull(Validaciones.errorTelefono("987654321"))
        assertNotNull(Validaciones.errorTelefono("887654321"))
        assertNotNull(Validaciones.errorTelefono("98765432"))
    }
}
