package com.reyes.clinicasaludplus.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class DiaCalendario(
    val fecha: LocalDate,
    val nombreDia: String,  // Lun, Mar, Mié...
    val numeroDia: String,  // 15, 16...
    val esHoy: Boolean
)

object FechaUtils {

    private val localeEs = Locale("es", "ES")

    // Retorna los 5 días hábiles correspondientes al offset semanal (0 = semana actual)
    fun obtenerDiasHabiles(semanaOffset: Long = 0): List<DiaCalendario> {
        val hoy = LocalDate.now()
        // Nos posicionamos al lunes de la semana actual + offset
        var fechaBase = hoy.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset)
        val dias = mutableListOf<DiaCalendario>()

        while (dias.size < 5) {
            val diaSemana = fechaBase.dayOfWeek
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                val nombre = diaSemana.getDisplayName(TextStyle.SHORT, localeEs)
                    .replaceFirstChar { it.uppercase() }
                    .replace(".", "")

                dias.add(
                    DiaCalendario(
                        fecha = fechaBase,
                        nombreDia = nombre.take(3),
                        numeroDia = fechaBase.dayOfMonth.toString(),
                        esHoy = fechaBase == hoy
                    )
                )
            }
            fechaBase = fechaBase.plusDays(1)
        }
        return dias
    }

    // Texto de encabezado de mes y año dinámico: "Setiembre 2026"
    fun obtenerEncabezadoMes(fecha: LocalDate): String {
        val mes = fecha.month.getDisplayName(TextStyle.FULL, localeEs)
            .replaceFirstChar { it.uppercase() }
        return "$mes ${fecha.year}"
    }

    // Formato formal para Confirmar Cita: "Martes 16 de setiembre 2026"
    fun formatearFechaCompleta(fecha: LocalDate): String {
        val diaSemana = fecha.dayOfWeek.getDisplayName(TextStyle.FULL, localeEs)
            .replaceFirstChar { it.uppercase() }
        val mes = fecha.month.getDisplayName(TextStyle.FULL, localeEs).lowercase()
        return "$diaSemana ${fecha.dayOfMonth} de $mes ${fecha.year}"
    }
}