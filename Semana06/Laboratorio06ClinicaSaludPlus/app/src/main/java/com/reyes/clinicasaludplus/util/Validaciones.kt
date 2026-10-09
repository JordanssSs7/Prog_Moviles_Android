package com.reyes.clinicasaludplus.util

enum class NivelContrasena(val etiqueta: String, val segmentos: Int) {
    NINGUNO("", 0),
    DEBIL("Débil", 1),
    MEDIA("Media", 2),
    FUERTE("Fuerte", 3)
}

/** Reglas de validación de formularios. Cada función devuelve el mensaje de error o null si es válido. */
object Validaciones {

    private val regexNombre = Regex("""^\p{L}+(['\-]\p{L}+)*( \p{L}+(['\-]\p{L}+)*)*$""")
    private val regexCorreo = Regex("""^[A-Za-z0-9._%+\-]+@[A-Za-z0-9\-]+(\.[A-Za-z0-9\-]+)*\.[A-Za-z]{2,}$""")
    private val regexEspacios = Regex("""\s+""")

    const val LARGO_TELEFONO = 9
    const val MIN_CONTRASENA = 8
    const val MAX_CONTRASENA = 30
    const val MAX_NOMBRE = 50

    fun normalizarNombre(nombre: String): String = nombre.trim().replace(regexEspacios, " ")

    fun errorNombre(nombre: String): String? {
        val n = normalizarNombre(nombre)
        return when {
            n.isEmpty() -> "Ingresa tu nombre completo"
            n.length < 5 -> "El nombre es demasiado corto"
            n.length > MAX_NOMBRE -> "Máximo $MAX_NOMBRE caracteres"
            !regexNombre.matches(n) -> "Usa solo letras y espacios"
            n.split(" ").size < 2 -> "Ingresa nombre y apellido"
            else -> null
        }
    }

    fun errorTelefono(telefono: String): String? {
        val t = telefono.trim()
        return when {
            t.isEmpty() -> "Ingresa tu teléfono"
            !t.all { it.isDigit() } -> "Solo se permiten números"
            t.length != LARGO_TELEFONO -> "Debe tener $LARGO_TELEFONO dígitos"
            !t.startsWith("9") -> "Debe empezar con 9"
            else -> null
        }
    }

    /** El correo es opcional: vacío es válido. */
    fun errorCorreoOpcional(correo: String): String? {
        val c = correo.trim()
        if (c.isEmpty()) return null
        return when {
            c.length > 60 -> "Correo demasiado largo"
            !regexCorreo.matches(c) -> "Correo no válido (ej: nombre@correo.com)"
            else -> null
        }
    }

    fun errorContrasena(contrasena: String): String? = when {
        contrasena.isEmpty() -> "Ingresa una contraseña"
        contrasena.any { it.isWhitespace() } -> "No puede contener espacios"
        contrasena.length < MIN_CONTRASENA -> "Mínimo $MIN_CONTRASENA caracteres"
        contrasena.length > MAX_CONTRASENA -> "Máximo $MAX_CONTRASENA caracteres"
        else -> null
    }

    /** Fortaleza de la contraseña: mide largo y variedad (minúsculas, mayúsculas, números y símbolos). */
    fun nivelContrasena(contrasena: String): NivelContrasena {
        if (contrasena.isEmpty()) return NivelContrasena.NINGUNO
        if (contrasena.length < MIN_CONTRASENA) return NivelContrasena.DEBIL
        val puntos = listOf(
            contrasena.any { it.isLowerCase() },
            contrasena.any { it.isUpperCase() },
            contrasena.any { it.isDigit() },
            contrasena.any { !it.isLetterOrDigit() },
            contrasena.length >= 12
        ).count { it }
        return when {
            puntos <= 2 -> NivelContrasena.DEBIL
            puntos == 3 -> NivelContrasena.MEDIA
            else -> NivelContrasena.FUERTE
        }
    }

    fun telefonoValido(t: String) = errorTelefono(t) == null
    fun nombreValido(n: String) = errorNombre(n) == null
    fun correoOpcionalValido(c: String) = errorCorreoOpcional(c) == null
    fun contrasenaValida(c: String) = errorContrasena(c) == null
}
