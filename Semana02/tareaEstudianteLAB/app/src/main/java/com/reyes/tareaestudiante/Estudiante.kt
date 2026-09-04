package com.reyes.tareaestudiante

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    println("=== Sistema de Matricula Universitaria ===")

    print("Ingrese el aforo (cantidad maxima de alumnos a matricular): ")
    val aforo = scanner.nextInt()
    scanner.nextLine()

    for (alumno in 1..aforo) {
        println("\n--- Registrando Alumno $alumno de $aforo ---")

        print("Nombre del estudiante: ")
        val nombreEstudiante = scanner.nextLine().trim()


        var turno = ""
        while (true) {
            print("Turno (Mañana, Tarde, Noche): ")
            turno = scanner.nextLine().trim().lowercase()
            if (turno == "mañana" || turno == "manana" || turno == "tarde" || turno == "noche") {
                break
            }
            println("Error: Ingrese un turno valido (Mañana, Tarde o Noche).")
        }

        print("Categoria (Ordinario, Becario): ")
        val categoria = scanner.nextLine().trim().lowercase()

        var costoMatricula = 0.0
        if (categoria == "ordinario") {
            print("Ingrese el costo de matricula (S/): ")
            costoMatricula = scanner.nextDouble()
            scanner.nextLine()
        }

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

        var recargoTurno = 0.0
        if (turno == "mañana" || turno == "manana") {
            recargoTurno = totalAPagar * 0.10
        } else if (turno == "tarde") {
            recargoTurno = totalAPagar * 0.15
        } else if (turno == "noche") {
            recargoTurno = totalAPagar * 0.20
        }
        totalAPagar += recargoTurno

        totalAPagar += costoMatricula

        val igv = totalAPagar * 0.18
        totalAPagar += igv

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

        println("\n=== Resultado Final Alumno $alumno ===")
        println("Nombre del estudiante: $nombreEstudiante")
        println("Total de cursos matriculados: $cantidadCursos")
        println("Detalle de cursos:")
        print(detalleCursos)

        println("Total de creditos acumulados: $totalCreditos")
        println(String.format("Recargo por turno: S/ %.2f", recargoTurno))
        println(String.format("Costo de matricula: S/ %.2f", costoMatricula))
        println(String.format("IGV (18%%): S/ %.2f", igv))
        println(String.format("Total a pagar: S/ %.2f", totalAPagar))

        println("Carga Academica: $cargaAcademica")
        println(String.format("Forma de Pago: %d cuotas de S/ %.2f", numeroCuotas, montoPorCuota))

        if (alumno < aforo) {
            print("\n¿Desea matricular a otro alumno? (1=Si, 2=No): ")
            val continuar = scanner.nextInt()
            scanner.nextLine()

            if (continuar == 2) {
                println("Finalizando las matriculas por decision del usuario...")
                break
            }
        }
        println("-------------------------------------------\n")
    }

    println("Sistema cerrado.")
    scanner.close()
}