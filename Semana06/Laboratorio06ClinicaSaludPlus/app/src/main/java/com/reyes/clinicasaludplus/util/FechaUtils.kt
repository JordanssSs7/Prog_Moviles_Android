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

    /** Convierte "2026-09-16" a LocalDate; devuelve null si el texto no es una fecha válida. */
    fun parsearFecha(texto: String): LocalDate? =
        try { LocalDate.parse(texto) } catch (e: Exception) { null }

    // Retorna 5 días consecutivos en que el médico atiende, a partir de HOY (nunca días pasados).
    // semanaOffset = 0 es el primer grupo; cada +1 avanza otros 5 días de atención.
    fun obtenerDiasAtencion(diasAtencion: Set<DayOfWeek>, semanaOffset: Long = 0): List<DiaCalendario> {
        if (diasAtencion.isEmpty()) return emptyList()
        val hoy = LocalDate.now()
        val dias = mutableListOf<DiaCalendario>()
        var fecha = hoy
        var saltar = semanaOffset.coerceAtLeast(0) * DIAS_POR_SEMANA
        while (dias.size < DIAS_POR_SEMANA) {
            if (fecha.dayOfWeek in diasAtencion) {
                if (saltar > 0) {
                    saltar--
                } else {
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
                }
            }
            fecha = fecha.plusDays(1)
        }
        return dias
    }

    /** Abreviatura de días para mostrar: "Lun · Mié · Vie". */
    fun resumenDias(dias: Set<DayOfWeek>): String =
        dias.sorted().joinToString(" · ") {
            it.getDisplayName(TextStyle.SHORT, localeEs).replace(".", "").replaceFirstChar { c -> c.uppercase() }.take(3)
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
