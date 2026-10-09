package com.reyes.clinicasaludplus.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class DiaCalendario(
    val fecha: LocalDate,
    val nombreDia: String,  // Lun, Mar, Mié...
    val numeroDia: String,  // 15, 16...
    val esHoy: Boolean
)

object FechaUtils {

    private val localeEs: Locale = Locale.forLanguageTag("es-PE")

    const val DIAS_POR_SEMANA = 5

    fun esDiaHabil(fecha: LocalDate): Boolean =
        fecha.dayOfWeek != DayOfWeek.SATURDAY && fecha.dayOfWeek != DayOfWeek.SUNDAY

    /** Primer día hábil igual o posterior a la fecha dada (si cae fin de semana salta al lunes). */
    fun primerDiaHabil(desde: LocalDate = LocalDate.now()): LocalDate {
        var f = desde
        while (!esDiaHabil(f)) f = f.plusDays(1)
        return f
    }

    /** Convierte "2026-09-16" a LocalDate; devuelve null si el texto no es una fecha válida. */
    fun parsearFecha(texto: String): LocalDate? =
        try { LocalDate.parse(texto) } catch (e: Exception) { null }

    // Retorna 5 días hábiles consecutivos, a partir de HOY (nunca días pasados).
    // semanaOffset = 0 es la semana actual; cada +1 avanza otros 5 días hábiles.
    fun obtenerDiasHabiles(semanaOffset: Long = 0): List<DiaCalendario> {
        val hoy = LocalDate.now()
        var fecha = primerDiaHabil(hoy)
        repeat((semanaOffset.coerceAtLeast(0) * DIAS_POR_SEMANA).toInt()) {
            fecha = primerDiaHabil(fecha.plusDays(1))
        }
        val dias = mutableListOf<DiaCalendario>()
        while (dias.size < DIAS_POR_SEMANA) {
            val nombre = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, localeEs)
                .replace(".", "")
                .replaceFirstChar { it.uppercase() }
            dias.add(
                DiaCalendario(
                    fecha = fecha,
                    nombreDia = nombre.take(3),
                    numeroDia = fecha.dayOfMonth.toString(),
                    esHoy = fecha == hoy
                )
            )
            fecha = primerDiaHabil(fecha.plusDays(1))
        }
        return dias
    }

    private fun nombreMes(fecha: LocalDate): String =
        fecha.month.getDisplayName(TextStyle.FULL, localeEs)
            .lowercase()

    // Texto de encabezado de mes y año dinámico: "Setiembre 2026"
    fun obtenerEncabezadoMes(fecha: LocalDate): String =
        "${nombreMes(fecha).replaceFirstChar { it.uppercase() }} ${fecha.year}"

    // Formato formal para Confirmar Cita: "Martes 16 de setiembre 2026"
    fun formatearFechaCompleta(fecha: LocalDate): String {
        val diaSemana = fecha.dayOfWeek.getDisplayName(TextStyle.FULL, localeEs)
            .replaceFirstChar { it.uppercase() }
        return "$diaSemana ${fecha.dayOfMonth} de ${nombreMes(fecha)} ${fecha.year}"
    }
}
