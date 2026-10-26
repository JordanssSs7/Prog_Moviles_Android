package com.reyes.lavacampus

import com.reyes.lavacampus.model.FuerzaClave
import com.reyes.lavacampus.model.fuerzaClave
import com.reyes.lavacampus.model.normalizarCodigo
import com.reyes.lavacampus.model.normalizarCorreo
import com.reyes.lavacampus.model.requisitosClave
import com.reyes.lavacampus.model.validarRegistro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistroTest {

    private fun validar(
        nombre: String = "Rosa Quispe",
        codigo: String = "251234",
        correo: String = "rosa@correo.test",
        clave: String = "Rosa#2025",
        confirmacion: String = clave
    ) = validarRegistro(nombre, codigo, correo, clave, confirmacion)

    // ---- Formulario ----

    @Test fun datosCorrectos_noHayError() = assertNull(validar())

    @Test fun camposVacios_pideCompletar() {
        assertEquals("Completa todos los campos", validar(nombre = ""))
        assertEquals("Completa todos los campos", validar(codigo = " "))
        assertEquals("Completa todos los campos", validar(correo = ""))
        assertEquals("Completa todos los campos", validar(clave = ""))
    }

    @Test fun nombreMuyCorto_seRechaza() =
        assertEquals("Escribe tu nombre completo", validar(nombre = "Al"))

    @Test fun correoInvalido_seRechaza() {
        assertEquals("Escribe un correo válido", validar(correo = "rosa"))
        assertEquals("Escribe un correo válido", validar(correo = "rosa@correo"))
        assertEquals("Escribe un correo válido", validar(correo = "ro sa@correo.test"))
    }

    @Test fun clavesDistintas_seRechazan() =
        assertEquals("Las contraseñas no coinciden", validar(clave = "Rosa#2025", confirmacion = "Rosa#2026"))

    // ---- Codigo de estudiante: 6 digitos, solo numeros ----

    @Test fun codigoDeSeisDigitos_seAcepta() {
        assertNull(validar(codigo = "231023"))
        assertNull(validar(codigo = "000123"))
    }

    @Test fun codigoQueNoTieneSeisDigitos_seRechaza() {
        val mensaje = "El código de estudiante debe tener 6 dígitos"
        assertEquals(mensaje, validar(codigo = "12345"))
        assertEquals(mensaje, validar(codigo = "1234567"))
        assertEquals(mensaje, validar(codigo = "U12345"))
        assertEquals(mensaje, validar(codigo = "12a456"))
        assertEquals(mensaje, validar(codigo = "12 456"))
        assertEquals(mensaje, validar(codigo = "U202310234"))
    }

    // ---- Contrasena: requisitos obligatorios ----

    @Test fun claveSinMayuscula_seRechaza() = assertEquals(MENSAJE_REQUISITOS, validar(clave = "rosa#2025"))
    @Test fun claveSinNumero_seRechaza() = assertEquals(MENSAJE_REQUISITOS, validar(clave = "Rosa#Quispe"))
    @Test fun claveSinEspecial_seRechaza() = assertEquals(MENSAJE_REQUISITOS, validar(clave = "Rosa20250"))

    @Test fun claveConEspacios_seRechaza() =
        assertEquals("La contraseña no puede tener espacios", validar(clave = "Rosa #2025"))

    @Test fun claveConLosTresRequisitosPeroCorta_esDebilYSeRechaza() {
        assertEquals(FuerzaClave.DEBIL, fuerzaClave("Ab1!"))
        assertEquals("La contraseña es débil: usa al menos 6 caracteres", validar(clave = "Ab1!"))
    }

    @Test fun claveMediaOFuerte_seAcepta() {
        assertNull(validar(clave = "ABC12!")) // media: mayuscula + numero + especial, 6 caracteres
        assertNull(validar(clave = "Rosa#2025")) // fuerte
    }

    // ---- Fuerza ----

    @Test fun fuerza_debil() {
        assertEquals(FuerzaClave.DEBIL, fuerzaClave(""))
        assertEquals(FuerzaClave.DEBIL, fuerzaClave("abc"))
        assertEquals(FuerzaClave.DEBIL, fuerzaClave("1234")) // corta
        assertEquals(FuerzaClave.DEBIL, fuerzaClave("abcdefgh")) // solo minusculas y largo: 2 criterios
        assertEquals(FuerzaClave.DEBIL, fuerzaClave("Aa1!")) // corta aunque tenga de todo
    }

    @Test fun fuerza_media() {
        assertEquals(FuerzaClave.MEDIA, fuerzaClave("ABC12!")) // 3 criterios
        assertEquals(FuerzaClave.MEDIA, fuerzaClave("Abc12!")) // 4 criterios
        assertEquals(FuerzaClave.MEDIA, fuerzaClave("abcd123!")) // minuscula + numero + especial + 8 (4)
    }

    @Test fun fuerza_fuerte() {
        assertEquals(FuerzaClave.FUERTE, fuerzaClave("Rosa#2025"))
        assertEquals(FuerzaClave.FUERTE, fuerzaClave("Ab1!abcd"))
    }

    @Test fun requisitos_detectaCadaCriterio() {
        val r = requisitosClave("Abc12!xy")
        assertTrue(r.mayuscula && r.minuscula && r.numero && r.especial && r.largoMinimo && r.largoFuerte)
        assertTrue(r.obligatoriosCumplidos)
        val s = requisitosClave("abc")
        assertFalse(s.mayuscula || s.numero || s.especial || s.largoMinimo)
        assertFalse(s.obligatoriosCumplidos)
        // un espacio no cuenta como caracter especial
        assertFalse(requisitosClave("Abc12 ").especial)
    }

    @Test fun normalizacion() {
        assertEquals("ana@correo.test", normalizarCorreo("  Ana@Correo.TEST "))
        assertEquals("231023", normalizarCodigo(" 231023 "))
    }

    private companion object {
        const val MENSAJE_REQUISITOS = "La contraseña debe tener una mayúscula, un número y un carácter especial"
    }
}
