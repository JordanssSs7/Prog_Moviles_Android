package com.reyes.clinicasaludplus.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Home : Rutas("home")

    // Opciones del menú lateral y flujo de agendamiento
    object Sedes : Rutas("sedes")
    object SedeDetalle : Rutas("sede/{sedeId}") {
        fun crearRuta(sedeId: String) = "sede/$sedeId"
    }
    // Directorio de doctores; sedeId y especialidadId son filtros opcionales
    object Doctores : Rutas("doctores?sedeId={sedeId}&especialidadId={especialidadId}") {
        fun crearRuta(sedeId: String = "", especialidadId: String = ""): String {
            val params = listOfNotNull(
                sedeId.takeIf { it.isNotEmpty() }?.let { "sedeId=$it" },
                especialidadId.takeIf { it.isNotEmpty() }?.let { "especialidadId=$it" }
            )
            return if (params.isEmpty()) "doctores" else "doctores?" + params.joinToString("&")
        }
    }
    object FechaHora : Rutas("fecha_hora/{medicoId}") {
        fun crearRuta(medicoId: String) = "fecha_hora/$medicoId"
    }
    object ConfirmarCita : Rutas("confirmar_cita/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: String, fecha: String, hora: String) = "confirmar_cita/$medicoId/$fecha/$hora"
    }
    object CitaExitosa : Rutas("cita_exitosa")

    // Destinos de la barra inferior y perfil
    object MisCitas : Rutas("mis_citas")
    object Perfil : Rutas("perfil")
}