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

    println("\n=== Resultado Final ===")
    println("Nombre del estudiante: $nombreEstudiante")
    println("Total de cursos matriculados: $cantidadCursos")
    println("Total de creditos acumulados: $totalCreditos")
    println("Total a pagar: S/ $totalAPagar")

    scanner.close()
}
