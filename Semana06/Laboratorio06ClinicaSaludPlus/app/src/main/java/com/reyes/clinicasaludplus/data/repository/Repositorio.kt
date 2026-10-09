package com.reyes.clinicasaludplus.data.repository

import com.reyes.clinicasaludplus.R
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.model.Usuario
import com.reyes.clinicasaludplus.util.FechaUtils
import com.reyes.clinicasaludplus.util.Validaciones
import java.time.LocalDate
import java.time.LocalTime

object Repositorio {

    // Sesión activa del usuario
    var usuarioActual: Usuario? = null

    // Última cita agendada (para mostrar el resumen en "Cita agendada")
    var ultimaCita: Cita? = null
        private set

    // Contadores para generar IDs únicos aunque se cancelen citas
    private var contadorUsuarios = 1
    private var contadorCitas = 0

    // Colección de usuarios en memoria
    private val usuarios = mutableListOf(
        Usuario("u1", "Juan Pérez", "987654321", "juan@correo.com", "123456")
    )

    // Colección de especialidades en memoria
    private val especialidades = listOf(
        Especialidad("esp1", "Medicina General", "Atención integral", "general", esDestacada = true),
        Especialidad("esp2", "Pediatría", "Niños y adolescentes", "pediatria", esDestacada = true),
        Especialidad("esp3", "Ginecología", "Salud de la mujer", "ginecologia", esDestacada = true),
        Especialidad("esp4", "Cardiología", "Corazón y vasos sanguíneos", "cardiologia", esDestacada = true),
        Especialidad("esp5", "Dermatología", "Piel, cabello y uñas", "dermatologia", esDestacada = false),
        Especialidad("esp6", "Traumatología", "Huesos y articulaciones", "traumatologia", esDestacada = false),
        Especialidad("esp7", "Oftalmología", "Salud visual", "oftalmologia", esDestacada = false)
    )

    private val medicos = listOf(
        // Ginecología (esp3)
        Medico("m1", "Dra. Ana Torres", "esp3", "12345", 4.8, 120.0, R.drawable.doc_ana_torres, "Ginecóloga", 120, "Disponible hoy"),
        Medico("m2", "Dra. Claudia Rojas", "esp3", "23456", 4.8, 95.0, R.drawable.doc_claudia_rojas, "Ginecóloga", 96, "Disponible mañana"),
        Medico("m3", "Dr. Luis Ramírez", "esp3", "34567", 4.7, 88.0, R.drawable.doc_luis_ramirez, "Ginecólogo", 88, "Disponible hoy"),
        Medico("m4", "Dra. Mariana Soto", "esp3", "45678", 4.6, 76.0, R.drawable.doc_mariana_soto, "Ginecóloga", 76, "Disponible esta semana"),

        // Medicina General (esp1)
        Medico("m5", "Dr. Carlos Mendoza", "esp1", "56789", 4.8, 80.0, R.drawable.doc_carlos_mendoza, "Médico General", 110),
        Medico("m7", "Dra. Rosa Delgado", "esp1", "67890", 4.5, 80.0, R.drawable.doc_rosa_delgado, "Médica General", 64, "Disponible mañana"),

        // Pediatría (esp2)
        Medico("m6", "Dra. Elena Ramos", "esp2", "78901", 4.9, 90.0, R.drawable.doc_elena_ramos, "Pediatra", 130),
        Medico("m8", "Dr. Pablo Rivas", "esp2", "89012", 4.6, 90.0, R.drawable.doc_pablo_rivas, "Pediatra", 72, "Disponible esta semana"),

        // Cardiología (esp4)
        Medico("m9", "Dr. Roberto Salas", "esp4", "90123", 4.9, 150.0, R.drawable.doc_roberto_salas, "Cardiólogo", 140),
        Medico("m10", "Dra. Patricia Vega", "esp4", "01234", 4.7, 140.0, R.drawable.doc_patricia_vega, "Cardióloga", 83, "Disponible mañana"),

        // Dermatología (esp5)
        Medico("m11", "Dra. Lucía Fernández", "esp5", "11223", 4.8, 110.0, R.drawable.doc_lucia_fernandez, "Dermatóloga", 102),
        Medico("m12", "Dr. Jorge Paredes", "esp5", "22334", 4.5, 100.0, R.drawable.doc_jorge_paredes, "Dermatólogo", 57, "Disponible esta semana"),

        // Traumatología (esp6)
        Medico("m13", "Dr. Miguel Quispe", "esp6", "33445", 4.7, 130.0, R.drawable.doc_miguel_quispe, "Traumatólogo", 91),
        Medico("m14", "Dr. Andrés Castillo", "esp6", "44556", 4.6, 125.0, R.drawable.doc_andres_castillo, "Traumatólogo", 68, "Disponible mañana"),

        // Oftalmología (esp7)
        Medico("m15", "Dra. Sofía Navarro", "esp7", "55667", 4.8, 115.0, R.drawable.doc_sofia_navarro, "Oftalmóloga", 77),
        Medico("m16", "Dr. Hugo Medina", "esp7", "66778", 4.6, 105.0, R.drawable.doc_hugo_medina, "Oftalmólogo", 59, "Disponible esta semana")
    )

    // Colección de citas agendadas
    private val citas = mutableListOf<Cita>()

    // Horarios base de atención
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00"
    )

    private const val ESTADO_CANCELADA = "Cancelada"

    // --- Métodos de Usuario y Sesión ---
    fun telefonoRegistrado(telefono: String): Boolean =
        usuarios.any { it.telefono == telefono.trim() }

    fun correoRegistrado(correo: String): Boolean {
        val c = correo.trim()
        return c.isNotEmpty() && usuarios.any { it.correo.equals(c, ignoreCase = true) }
    }

    fun registrarUsuario(nombre: String, telefono: String, correo: String, contrasena: String): Boolean {
        val nombreFinal = Validaciones.normalizarNombre(nombre)
        val tel = telefono.trim()
        val mail = correo.trim().lowercase()

        if (!Validaciones.nombreValido(nombreFinal)) return false
        if (!Validaciones.telefonoValido(tel)) return false
        if (!Validaciones.correoOpcionalValido(mail)) return false
        if (!Validaciones.contrasenaValida(contrasena)) return false
        if (telefonoRegistrado(tel) || correoRegistrado(mail)) return false

        contadorUsuarios++
        val nuevo = Usuario("u$contadorUsuarios", nombreFinal, tel, mail, contrasena)
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    // Acepta el correo o el teléfono como identificador
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val id = correo.trim()
        if (id.isEmpty() || contrasena.isEmpty()) return false
        val user = usuarios.find {
            ((it.correo.isNotEmpty() && it.correo.equals(id, ignoreCase = true)) || it.telefono == id) &&
                it.contrasena == contrasena
        }
        if (user != null) {
            usuarioActual = user
            return true
        }
        return false
    }

    fun cerrarSesion() {
        usuarioActual = null
        ultimaCita = null
    }

    // --- Especialidades ---
    fun buscarEspecialidades(query: String): List<Especialidad> {
        val q = query.trim()
        if (q.isEmpty()) return especialidades
        return especialidades.filter {
            it.nombre.contains(q, ignoreCase = true) || it.descripcion.contains(q, ignoreCase = true)
        }
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
        val q = query.trim()
        if (q.isEmpty()) return lista
        return lista.filter { it.nombre.contains(q, ignoreCase = true) }
    }

    fun obtenerMedico(id: String): Medico? = medicos.find { it.id == id }

    // --- Citas y Horarios ---
    private fun horaYaPaso(fecha: LocalDate, hora: String): Boolean {
        if (fecha != LocalDate.now()) return false
        val h = try {
            LocalTime.parse(hora)
        } catch (e: Exception) {
            return true
        }
        return !h.isAfter(LocalTime.now())
    }

    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        if (obtenerMedico(medicoId) == null) return emptyList()
        val dia = FechaUtils.parsearFecha(fecha) ?: return emptyList()
        if (dia.isBefore(LocalDate.now()) || !FechaUtils.esDiaHabil(dia)) return emptyList()

        val horasOcupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha && it.estado != ESTADO_CANCELADA }
            .map { it.hora }

        return horariosBase.filter { it !in horasOcupadas && !horaYaPaso(dia, it) }
    }

    // Devuelve el mensaje de error si la combinación médico/fecha/hora no puede agendarse; null si es válida
    fun validarCita(medicoId: String, fecha: String, hora: String): String? {
        val uid = usuarioActual?.id ?: return "Debes iniciar sesión para agendar una cita"
        if (obtenerMedico(medicoId) == null) return "No se encontró al médico seleccionado"
        val dia = FechaUtils.parsearFecha(fecha) ?: return "La fecha seleccionada no es válida"
        if (dia.isBefore(LocalDate.now())) return "No puedes agendar en una fecha pasada"
        if (!FechaUtils.esDiaHabil(dia)) return "Solo se atiende de lunes a viernes"
        if (hora !in horariosBase) return "El horario seleccionado no es válido"
        if (horaYaPaso(dia, hora)) return "Ese horario ya pasó, elige otro"
        if (estaHorarioOcupado(medicoId, fecha, hora)) return "El horario seleccionado ya no está disponible"
        if (citas.any { it.usuarioId == uid && it.fecha == fecha && it.hora == hora && it.estado != ESTADO_CANCELADA }) {
            return "Ya tienes otra cita a esa misma hora"
        }
        return null
    }

    fun agendarCita(
        medicoId: String,
        especialidadId: String,
        fecha: String,
        hora: String,
        motivo: String = ""
    ): Boolean {
        val usuario = usuarioActual ?: return false
        if (validarCita(medicoId, fecha, hora) != null) return false
        val medico = obtenerMedico(medicoId) ?: return false
        if (medico.especialidadId != especialidadId) return false

        contadorCitas++
        val nuevaCita = Cita(
            id = "c$contadorCitas",
            usuarioId = usuario.id,
            medicoId = medicoId,
            especialidadId = especialidadId,
            fecha = fecha,
            hora = hora,
            motivo = motivo.trim().take(120)
        )
        citas.add(nuevaCita)
        ultimaCita = nuevaCita
        return true
    }

    fun citasDelUsuario(): List<Cita> {
        val id = usuarioActual?.id ?: return emptyList()
        return citas
            .filter { it.usuarioId == id && it.estado != ESTADO_CANCELADA }
            .sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora })
    }

    fun obtenerCita(citaId: String): Cita? = citas.find { it.id == citaId }

    fun cancelarCita(citaId: String): Boolean {
        val uid = usuarioActual?.id ?: return false
        return citas.removeIf { it.id == citaId && it.usuarioId == uid }
    }

    fun estaHorarioOcupado(medicoId: String, fecha: String, hora: String): Boolean {
        return citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora && it.estado != ESTADO_CANCELADA }
    }

    // Asocia cada ID de especialidad a su recurso en drawable
    fun obtenerIconoDrawable(id: String): Int {
        return when (id) {
            "esp1" -> R.drawable.esp_tile_general
            "esp2" -> R.drawable.esp_tile_pediatria
            "esp3" -> R.drawable.esp_tile_ginecologia
            "esp4" -> R.drawable.esp_tile_cardiologia
            "esp5" -> R.drawable.esp_tile_dermatologia
            "esp6" -> R.drawable.esp_tile_traumatologia
            "esp7" -> R.drawable.esp_tile_oftalmologia
            else -> R.drawable.esp_tile_general
        }
    }
}
