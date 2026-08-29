package com.reyes.tareaestudiante

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)

    println("=== Sistema de Matricula Universitaria ===")

    print("Nombre del estudiante: ")
    val nombreEstudiante = scanner.nextLine().trim()

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

    println("\n=== Ingreso de Cursos ===")
    for (i in 0 until cantidadCursos) {
        print("Nombre del curso ${i + 1}: ")
        cursoActual = scanner.nextLine().trim()

        print("Cantidad de creditos: ")
        creditosCurso = scanner.nextInt()
        scanner.nextLine()

        costoCurso = creditosCurso * valorPorCreditos
        println("  Costo: S/ $costoCurso")

        totalCreditos += creditosCurso
        totalAPagar += costoCurso
    }

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
    println("Total de creditos acumulados: $totalCreditos")
    println("Total a pagar: S/ $totalAPagar")

    println("Carga Academica: $cargaAcademica")
    println("Forma de Pago: $numeroCuotas cuotas de S/ $montoPorCuota")

    scanner.close()
}
