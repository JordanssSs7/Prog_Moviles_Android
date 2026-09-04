package com.reyes.tareaestudiante

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    println("=== Sistema de Matricula Universitaria ===")

    print("Nombre del estudiante: ")
    val nombreEstudiante = scanner.nextLine().trim()

    // --- NUEVO: Petición del turno ---
    print("Turno (1=Mañana, 2=Tarde, 3=Noche): ")
    val turno = scanner.nextInt()
    scanner.nextLine()

    print("Cantidad de cursos a llevar: ")
    val cantidadCursos = scanner.nextInt()
    scanner.nextLine()

    print("Valor de cada credito (S/): ")
    val valorPorCreditos = scanner.nextDouble()
    scanner.nextLine()

    var totalCreditos = 0
    var totalAPagar = 0.0
    var cursoActual = ""
    var creditosCurso = 0
    var costoCurso = 0.0
    var detalleCursos = ""

    println("\n=== Ingreso de Cursos ===")
    for (i in 0 until cantidadCursos) {
        print("Nombre del curso ${i + 1}: ")
        cursoActual = scanner.nextLine().trim()

        print("Cantidad de creditos: ")
        creditosCurso = scanner.nextInt()
        scanner.nextLine()

        costoCurso = creditosCurso * valorPorCreditos
        detalleCursos += String.format(
            "  - %-20s | %d creditos | S/ %.2f%n",
            cursoActual, creditosCurso, costoCurso
        )

        println(String.format("  Costo: S/ %.2f", costoCurso))

        totalCreditos += creditosCurso
        totalAPagar += costoCurso
    }

    // --- NUEVO: Cálculo del recargo por turno ---
    var recargoTurno = 0.0
    if (turno == 1) {
        recargoTurno = totalAPagar * 0.10
    } else if (turno == 2) {
        recargoTurno = totalAPagar * 0.15
    } else if (turno == 3) {
        recargoTurno = totalAPagar * 0.20
    }
    totalAPagar += recargoTurno

    var cargaAcademica = ""
    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular (M.R.)"
    } else if (totalCreditos in 13..18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Requiere Autorizacion"
    }

    val numeroCuotas: Int
    if (totalAPagar > 1500) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }
    val montoPorCuota = totalAPagar / numeroCuotas

    println("\n=== Resultado Final ===")
    println("Nombre del estudiante: $nombreEstudiante")
    println("Total de cursos matriculados: $cantidadCursos")
    println("Detalle de cursos:")
    print(detalleCursos)

    println("Total de creditos acumulados: $totalCreditos")

    println(String.format("Recargo por turno: S/ %.2f", recargoTurno))
    println(String.format("Total a pagar: S/ %.2f", totalAPagar))

    println("Carga Academica: $cargaAcademica")
    println(String.format("Forma de Pago: %d cuotas de S/ %.2f", numeroCuotas, montoPorCuota))

    scanner.close()
}