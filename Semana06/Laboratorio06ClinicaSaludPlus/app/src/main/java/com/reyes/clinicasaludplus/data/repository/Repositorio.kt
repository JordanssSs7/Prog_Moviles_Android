package com.reyes.clinicasaludplus.data.repository

import com.reyes.clinicasaludplus.R
import com.reyes.clinicasaludplus.data.model.Cita
import com.reyes.clinicasaludplus.data.model.Especialidad
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.data.model.Sede
import com.reyes.clinicasaludplus.data.model.Usuario
import com.reyes.clinicasaludplus.util.FechaUtils
import com.reyes.clinicasaludplus.util.Validaciones
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

object Repositorio {

    // Sesión activa del usuario
    var usuarioActual: Usuario? = null

    // Última cita agendada (para mostrar el resumen en "Cita agendada")
    var ultimaCita: Cita? = null
        private set

    // Contadores para generar IDs únicos aunque se cancelen citas
    private var contadorUsuarios = 0
    private var contadorCitas = 0

    // Colección de usuarios en memoria
    private val usuarios = mutableListOf<Usuario>()

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

    // Sedes de la clínica
    private val sedes = listOf(
        Sede("s1", "Santa Anita", "Av. Los Eucaliptos 450, Santa Anita", "917100001", "Lunes a viernes, 8:00 a 19:00", R.drawable.sede_santa_anita),
        Sede("s2", "Ate", "Av. Nicolás Ayllón 2250, Ate", "917100002", "Lunes a viernes, 8:00 a 19:00", R.drawable.sede_ate),
        Sede("s3", "La Molina", "Av. La Molina 1820, La Molina", "917100003", "Lunes a viernes, 8:00 a 19:00", R.drawable.sede_la_molina),
        Sede("s4", "San Isidro", "Av. Javier Prado Oeste 1100, San Isidro", "917100004", "Lunes a viernes, 8:00 a 19:00", R.drawable.sede_san_isidro)
    )

    // Turnos y días de atención de los médicos
    private val manana = listOf("08:00", "09:00", "10:00", "11:00", "12:00")
    private val tarde = listOf("15:00", "16:00", "17:00", "18:00")
    private val lunMieVie = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
    private val marJue = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
    private val lunAVie = setOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    )
    private val lunMarJue = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
    private val marMieVie = setOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    private val medicos = listOf(
        // Santa Anita (s1)
        Medico("m5", "Dr. Carlos Mendoza", "esp1", "56789", "s1", "917200005", 4.8, 80.0, R.drawable.doc_carlos_mendoza, "Médico General", 110, lunAVie, manana + tarde),
        Medico("m6", "Dra. Elena Ramos", "esp2", "78901", "s1", "917200006", 4.9, 90.0, R.drawable.doc_elena_ramos, "Pediatra", 130, lunMieVie, manana),
        Medico("m1", "Dra. Ana Torres", "esp3", "12345", "s1", "917200001", 4.8, 120.0, R.drawable.doc_ana_torres, "Ginecóloga", 120, marJue, manana + tarde),
        Medico("m9", "Dr. Roberto Salas", "esp4", "90123", "s1", "917200009", 4.9, 150.0, R.drawable.doc_roberto_salas, "Cardiólogo", 140, lunMarJue, tarde),

        // Ate (s2)
        Medico("m7", "Dra. Rosa Delgado", "esp1", "67890", "s2", "917200007", 4.5, 80.0, R.drawable.doc_rosa_delgado, "Médica General", 64, marMieVie, manana + tarde),
        Medico("m8", "Dr. Pablo Rivas", "esp2", "89012", "s2", "917200008", 4.6, 90.0, R.drawable.doc_pablo_rivas, "Pediatra", 72, marJue, tarde),
        Medico("m2", "Dra. Claudia Rojas", "esp3", "23456", "s2", "917200002", 4.8, 95.0, R.drawable.doc_claudia_rojas, "Ginecóloga", 96, lunAVie, manana),
        Medico("m11", "Dra. Lucía Fernández", "esp5", "11223", "s2", "917200011", 4.8, 110.0, R.drawable.doc_lucia_fernandez, "Dermatóloga", 102, lunMieVie, tarde),

        // La Molina (s3)
        Medico("m10", "Dra. Patricia Vega", "esp4", "01234", "s3", "917200010", 4.7, 140.0, R.drawable.doc_patricia_vega, "Cardióloga", 83, marJue, manana),
        Medico("m13", "Dr. Miguel Quispe", "esp6", "33445", "s3", "917200013", 4.7, 130.0, R.drawable.doc_miguel_quispe, "Traumatólogo", 91, lunMieVie, manana + tarde),
        Medico("m3", "Dr. Luis Ramírez", "esp3", "34567", "s3", "917200003", 4.7, 88.0, R.drawable.doc_luis_ramirez, "Ginecólogo", 88, marMieVie, tarde),
        Medico("m15", "Dra. Sofía Navarro", "esp7", "55667", "s3", "917200015", 4.8, 115.0, R.drawable.doc_sofia_navarro, "Oftalmóloga", 77, lunAVie, manana),

        // San Isidro (s4)
        Medico("m12", "Dr. Jorge Paredes", "esp5", "22334", "s4", "917200012", 4.5, 100.0, R.drawable.doc_jorge_paredes, "Dermatólogo", 57, lunMarJue, manana + tarde),
        Medico("m14", "Dr. Andrés Castillo", "esp6", "44556", "s4", "917200014", 4.6, 125.0, R.drawable.doc_andres_castillo, "Traumatólogo", 68, marJue, tarde),
        Medico("m4", "Dra. Mariana Soto", "esp3", "45678", "s4", "917200004", 4.6, 76.0, R.drawable.doc_mariana_soto, "Ginecóloga", 76, lunMieVie, manana),
        Medico("m16", "Dr. Hugo Medina", "esp7", "66778", "s4", "917200016", 4.6, 105.0, R.drawable.doc_hugo_medina, "Oftalmólogo", 59, marMieVie, manana + tarde)
    )

    // Colección de citas agendadas
    private val citas = mutableListOf<Cita>()

    // Duración de cada consulta en minutos (cada horario de un médico ocupa este tiempo)
    const val DURACION_CONSULTA_MIN = 60L

    // Máximo de días hacia adelante en que se puede agendar
    private const val MAX_DIAS_ADELANTE = 120L

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

    fun obtenerEspecialidad(id: String): Especialidad? = especialidades.find { it.id == id }

    // --- Sedes ---
    fun sedes(): List<Sede> = sedes

    fun obtenerSede(id: String): Sede? = sedes.find { it.id == id }

    // --- Médicos ---
    fun obtenerMedico(id: String): Medico? = medicos.find { it.id == id }

    /** Médicos filtrados por sede y/o especialidad (cadena vacía = sin filtro), ordenados por calificación. */
    fun buscarMedicos(sedeId: String = "", especialidadId: String = "", query: String = ""): List<Medico> {
        val q = query.trim()
        return medicos
            .filter { sedeId.isEmpty() || it.sedeId == sedeId }
            .filter { especialidadId.isEmpty() || it.especialidadId == especialidadId }
            .filter { q.isEmpty() || it.nombre.contains(q, ignoreCase = true) }
            .sortedWith(compareByDescending<Medico> { it.calificacion }.thenBy { it.nombre })
    }

    /** Especialidades que existen en una sede (cadena vacía = todas las que tienen médicos). */
    fun especialidadesDeSede(sedeId: String = ""): List<Especialidad> {
        val ids = medicos.filter { sedeId.isEmpty() || it.sedeId == sedeId }.map { it.especialidadId }.toSet()
        return especialidades.filter { it.id in ids }
    }

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

    /** Horas libres del médico ese día: respeta sus días/horas de atención y las citas ya reservadas. */
    fun horariosDisponibles(medicoId: String, fecha: String): List<String> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()
        val dia = FechaUtils.parsearFecha(fecha) ?: return emptyList()
        if (dia.isBefore(LocalDate.now()) || dia.dayOfWeek !in medico.diasAtencion) return emptyList()

        val horasOcupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha && it.estado != ESTADO_CANCELADA }
            .map { it.hora }

        return medico.horas.filter { it !in horasOcupadas && !horaYaPaso(dia, it) }
    }

    // Devuelve el mensaje de error si la combinación médico/fecha/hora no puede agendarse; null si es válida
    fun validarCita(medicoId: String, fecha: String, hora: String): String? {
        val uid = usuarioActual?.id ?: return "Debes iniciar sesión para agendar una cita"
        val medico = obtenerMedico(medicoId) ?: return "No se encontró al médico seleccionado"
        val dia = FechaUtils.parsearFecha(fecha) ?: return "La fecha seleccionada no es válida"
        if (dia.isBefore(LocalDate.now())) return "No puedes agendar en una fecha pasada"
        if (dia.isAfter(LocalDate.now().plusDays(MAX_DIAS_ADELANTE))) return "Solo puedes agendar con hasta $MAX_DIAS_ADELANTE días de anticipación"
        if (dia.dayOfWeek !in medico.diasAtencion) return "El médico no atiende ese día"
        if (hora !in medico.horas) return "El médico no atiende en ese horario"
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
