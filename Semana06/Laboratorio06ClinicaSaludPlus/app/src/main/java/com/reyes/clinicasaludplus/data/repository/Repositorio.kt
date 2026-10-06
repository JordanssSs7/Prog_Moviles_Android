package com.reyes.clinicasaludplus.data.repository

import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.model.Usuario

object Repositorio {

    // Sesión activa del usuario
    var usuarioActual: Usuario? = null

    // Colección de usuarios en memoria
    private val usuarios = mutableListOf(
        Usuario("u1", "Juan Pérez", "987654321", "juan@correo.com", "123456")
    )

    // Colección de especialidades en memoria
    private val especialidades = listOf(
        Especialidad("esp1", "Medicina General", "Atención primaria integral", "general", esDestacada = true),
        Especialidad("esp2", "Pediatría", "Salud y cuidado infantil", "pediatria", esDestacada = true),
        Especialidad("esp3", "Ginecología", "Salud integral de la mujer", "ginecologia", esDestacada = true),
        Especialidad("esp4", "Cardiología", "Cuidado del corazón", "cardiologia", esDestacada = true),
        Especialidad("esp5", "Dermatología", "Piel, cabello y uñas", "dermatologia", esDestacada = false),
        Especialidad("esp6", "Traumatología", "Huesos y articulaciones", "traumatologia", esDestacada = false),
        Especialidad("esp7", "Oftalmología", "Salud y visión ocular", "oftalmologia", esDestacada = false)
    )

    private val medicos = listOf(
        // Ginecólogos (esp3)
        Medico(
            id = "m1",
            nombre = "Dra. Ana Torres",
            especialidadId = "esp3",
            cmp = "Ginecóloga",
            calificacion = 4.9,
            precioConsulta = 120.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_ana_torres
        ),
        Medico(
            id = "m2",
            nombre = "Dra. Claudia Rojas",
            especialidadId = "esp3",
            cmp = "Ginecóloga",
            calificacion = 4.8,
            precioConsulta = 95.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_claudia_rojas
        ),
        Medico(
            id = "m3",
            nombre = "Dr. Luis Ramírez",
            especialidadId = "esp3",
            cmp = "Ginecólogo",
            calificacion = 4.7,
            precioConsulta = 88.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_luis_ramirez
        ),
        Medico(
            id = "m4",
            nombre = "Dra. Mariana Soto",
            especialidadId = "esp3",
            cmp = "Ginecóloga",
            calificacion = 4.6,
            precioConsulta = 76.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_mariana_soto
        ),

        // Medicina General (esp1)
        Medico(
            id = "m5",
            nombre = "Dr. Carlos Mendoza",
            especialidadId = "esp1",
            cmp = "Médico General",
            calificacion = 4.8,
            precioConsulta = 110.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_carlos_mendoza
        ),

        // Pediatría (esp2)
        Medico(
            id = "m6",
            nombre = "Dra. Elena Ramos",
            especialidadId = "esp2",
            cmp = "Pediatra",
            calificacion = 4.9,
            precioConsulta = 130.0,
            fotoRes = com.reyes.clinicasaludplus.R.drawable.doc_elena_ramos
        )
    )

    // Colección de citas agendadas
    private val citas = mutableListOf<Cita>()

    // Horarios base para la Fase 1
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00"
    )

    // Días fijos para la Fase 1
    val diasFijosFase1 = listOf("15", "16", "17", "18", "19")

    // --- Métodos de Usuario y Sesión ---
    fun registrarUsuario(nombre: String, telefono: String, correo: String, contrasena: String): Boolean {
        if (usuarios.any { it.correo.equals(correo, ignoreCase = true) }) return false
        val nuevo = Usuario("u${usuarios.size + 1}", nombre, telefono, correo, contrasena)
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val user = usuarios.find { it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena }
        if (user != null) {
            usuarioActual = user
            return true
        }
        return false
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // --- Especialidades ---
    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades
        return especialidades.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.filter { it.esDestacada }.take(4)
    }

    fun obtenerEspecialidad(id: String): Especialidad? = especialidades.find { it.id == id }

    // --- Médicos ---
    fun medicosPorEspecialidad(especialidadId: String): List<Medico> {
        return medicos.filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(especialidadId: String, query: String): List<Medico> {
        val lista = medicosPorEspecialidad(especialidadId)
        if (query.isBlank()) return lista
        return lista.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun obtenerMedico(id: String): Medico? = medicos.find { it.id == id }

    // --- Citas y Horarios ---
    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        val horasOcupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha && it.estado != "Cancelada" }
            .map { it.hora }

        return horariosBase.filter { it !in horasOcupadas }
    }

    fun agendarCita(medicoId: String, especialidadId: String, fecha: String, hora: String): Boolean {
        val usuario = usuarioActual ?: return false
        val yaExiste = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora && it.estado != "Cancelada" }
        if (yaExiste) return false

        val nuevaCita = Cita(
            id = "c${citas.size + 1}",
            usuarioId = usuario.id,
            medicoId = medicoId,
            especialidadId = especialidadId,
            fecha = fecha,
            hora = hora
        )
        citas.add(nuevaCita)
        return true
    }

    fun citasDelUsuario(): List<Cita> {
        val id = usuarioActual?.id ?: return emptyList()
        return citas.filter { it.usuarioId == id }
    }

    fun obtenerCita(citaId: String): Cita? = citas.find { it.id == citaId }

    fun cancelarCita(citaId: String): Boolean {
        return citas.removeIf { it.id == citaId }
    }

    fun estaHorarioOcupado(medicoId: String, fecha: String, hora: String): Boolean {
        return citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora && it.estado != "Cancelada" }
    }

    // Asocia cada ID de especialidad a su recurso en drawable
    fun obtenerIconoDrawable(id: String): Int {
        return when (id) {
            "esp1" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_general
            "esp2" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_pediatria
            "esp3" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_ginecologia
            "esp4" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_cardiologia
            "esp5" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_dermatologia
            "esp6" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_traumatologia
            "esp7" -> com.reyes.clinicasaludplus.R.drawable.ic_esp_oftalmologia
            else -> com.reyes.clinicasaludplus.R.drawable.ic_esp_general
        }
    }

}