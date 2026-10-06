package com.reyes.clinicasaludplus.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Home : Rutas("home")

    // Flujo de agendamiento
    object Especialidades : Rutas("especialidades")
    object Medicos : Rutas("medicos/{especialidadId}") {
        fun crearRuta(especialidadId: String) = "medicos/$especialidadId"
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
    object Resultados : Rutas("resultados")
}