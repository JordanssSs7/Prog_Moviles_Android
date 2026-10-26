package com.reyes.lavacampus.model

private val FormatoCorreo = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
private val FormatoCodigo = Regex("^\\d{6}$")

const val DIGITOS_CODIGO = 6
const val LARGO_MINIMO_CLAVE = 6
const val LARGO_CLAVE_FUERTE = 8

/** Los correos se guardan en minusculas y sin espacios para que "Ana@Correo.test" y "ana@correo.test" sean la misma cuenta. */
fun normalizarCorreo(correo: String): String = correo.trim().lowercase()

/** El codigo de estudiante son 6 digitos (por ejemplo 231023); se guarda sin espacios. */
fun normalizarCodigo(codigo: String): String = codigo.trim()

/** Lo que cumple una contrasena. Un caracter especial es cualquier simbolo que no sea letra, digito ni espacio. */
data class RequisitosClave(
    val mayuscula: Boolean,
    val minuscula: Boolean,
    val numero: Boolean,
    val especial: Boolean,
    val largoMinimo: Boolean, // 6 o mas caracteres
    val largoFuerte: Boolean // 8 o mas caracteres
) {
    /** Los tres requisitos obligatorios: una mayuscula, un numero y un caracter especial. */
    val obligatoriosCumplidos: Boolean get() = mayuscula && numero && especial
}

fun requisitosClave(clave: String) = RequisitosClave(
    mayuscula = clave.any { it.isUpperCase() },
    minuscula = clave.any { it.isLowerCase() },
    numero = clave.any { it.isDigit() },
    especial = clave.any { !it.isLetterOrDigit() && !it.isWhitespace() },
    largoMinimo = clave.length >= LARGO_MINIMO_CLAVE,
    largoFuerte = clave.length >= LARGO_CLAVE_FUERTE
)

enum class FuerzaClave(val etiqueta: String) {
    DEBIL("Débil"),
    MEDIA("Media"),
    FUERTE("Fuerte")
}

/**
 * Fuerza de la contrasena: con menos de 6 caracteres siempre es debil; con 6 o mas se cuentan
 * cuantos de estos 5 criterios cumple (mayuscula, minuscula, numero, especial y 8+ caracteres):
 * 0-2 debil, 3-4 media y 5 fuerte.
 */
fun fuerzaClave(clave: String): FuerzaClave {
    val r = requisitosClave(clave)
    if (!r.largoMinimo) return FuerzaClave.DEBIL
    val puntos = listOf(r.mayuscula, r.minuscula, r.numero, r.especial, r.largoFuerte).count { it }
    return when {
        puntos <= 2 -> FuerzaClave.DEBIL
        puntos <= 4 -> FuerzaClave.MEDIA
        else -> FuerzaClave.FUERTE
    }
}

/** El nombre debe tener al menos 3 caracteres. */
fun validarNombre(nombre: String): String? =
    if (nombre.trim().length < 3) "Escribe tu nombre completo" else null

/**
 * Valida una contrasena nueva: sin espacios, con una mayuscula, un numero y un caracter especial,
 * de fuerza media o fuerte, y repetida igual en la confirmacion.
 */
fun validarClaveNueva(clave: String, confirmacion: String): String? {
    if (clave.any { it.isWhitespace() }) return "La contraseña no puede tener espacios"
    if (!requisitosClave(clave).obligatoriosCumplidos) {
        return "La contraseña debe tener una mayúscula, un número y un carácter especial"
    }
    if (fuerzaClave(clave) == FuerzaClave.DEBIL) {
        return "La contraseña es débil: usa al menos $LARGO_MINIMO_CLAVE caracteres"
    }
    if (clave != confirmacion) return "Las contraseñas no coinciden"
    return null
}

/**
 * Valida el cambio de contrasena: la actual debe coincidir con la guardada, la nueva debe ser distinta
 * y cumplir las mismas reglas que al registrarse.
 */
fun validarCambioClave(claveGuardada: String, actual: String, nueva: String, confirmacion: String): String? {
    if (actual.isEmpty() || nueva.isEmpty() || confirmacion.isEmpty()) return "Completa todos los campos"
    if (actual != claveGuardada) return "La contraseña actual no es correcta"
    if (nueva == actual) return "La nueva contraseña debe ser distinta de la actual"
    return validarClaveNueva(nueva, confirmacion)
}

/**
 * Valida el formulario de registro de un estudiante. Devuelve el mensaje del primer error
 * o null si todo esta bien. La contrasena solo se acepta si cumple los tres requisitos
 * obligatorios y su fuerza es media o fuerte.
 */
fun validarRegistro(
    nombre: String,
    codigo: String,
    correo: String,
    clave: String,
    confirmacion: String
): String? {
    if (nombre.isBlank() || codigo.isBlank() || correo.isBlank() || clave.isEmpty()) {
        return "Completa todos los campos"
    }
    validarNombre(nombre)?.let { return it }
    if (!FormatoCodigo.matches(normalizarCodigo(codigo))) {
        return "El código de estudiante debe tener $DIGITOS_CODIGO dígitos"
    }
    if (!FormatoCorreo.matches(normalizarCorreo(correo))) return "Escribe un correo válido"
    return validarClaveNueva(clave, confirmacion)
}
