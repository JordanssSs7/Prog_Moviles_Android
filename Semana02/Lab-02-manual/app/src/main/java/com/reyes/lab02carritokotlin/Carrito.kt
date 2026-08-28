package com.reyes.lab02carritokotlin

import com.reyes.lab02carritokotlin.model.Carrito
import com.reyes.lab02carritokotlin.model.ProductoImportado
import com.reyes.lab02carritokotlin.model.ProductoPerecible
import com.reyes.lab02carritokotlin.model.ProductoRegular

fun main() {
    println("=========================================")
    println("     CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    // ---- Los datos ahora los ingresa el usuario por teclado ----
    val nombreCliente = leerTexto("Ingrese el nombre del cliente: ")
    val carrito = Carrito(nombreCliente)

    val cuantos = leerEntero("Cuantos productos desea agregar?: ", minimo = 1)
    for (n in 1..cuantos) {
        println()
        println("--- Producto $n de $cuantos ---")
        val nombre = leerTexto("Nombre: ")
        val precio = leerDouble("Precio unitario (S/): ", minimo = 0.01)
        val cantidad = leerEntero("Cantidad: ", minimo = 1)

        // POLIMORFISMO: segun el tipo elegido se crea una subclase distinta,
        // pero todas se guardan en el carrito como "Producto".
        val producto = when (leerTipo()) {
            1 -> ProductoRegular(nombre, precio, cantidad)
            2 -> {
                val porcentaje = leerDouble("  % de impuesto de importacion (ej. 10): ", minimo = 0.0)
                ProductoImportado(nombre, precio, cantidad, porcentaje / 100.0)
            }
            else -> {
                val dias = leerEntero("  Dias para vencer: ", minimo = 0)
                ProductoPerecible(nombre, precio, cantidad, dias)
            }
        }
        carrito.agregar(producto)
    }

    println()
    imprimirBoleta(carrito)

    // ---------- Reto: consultar y quitar un producto ----------
    print("\nConsultar un producto? Escriba el nombre (o Enter para omitir): ")
    val consulta = readLine()?.trim()
    if (!consulta.isNullOrEmpty()) {
        val p = carrito.buscar(consulta)
        if (p != null) println("Encontrado -> $p") else println("'$consulta' no esta en el carrito.")
    }

    print("\nQuitar un producto? Escriba el nombre (o Enter para terminar): ")
    val quitar = readLine()?.trim()
    if (!quitar.isNullOrEmpty()) {
        if (carrito.eliminar(quitar)) {
            println("Se elimino '$quitar'.")
            println()
            imprimirBoleta(carrito)
        } else {
            println("No se encontro '$quitar' en el carrito.")
        }
    }
}

// ============================ LECTURA DE DATOS ============================
// Cada funcion vuelve a preguntar hasta que el dato sea valido (no revienta
// el programa si el usuario escribe algo incorrecto).

private fun leerTexto(mensaje: String): String {
    while (true) {
        print(mensaje)
        val entrada = readLine()?.trim()
        if (!entrada.isNullOrEmpty()) return entrada
        println("  * El dato no puede estar vacio.")
    }
}

private fun leerEntero(mensaje: String, minimo: Int): Int {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()?.toIntOrNull()
        if (valor != null && valor >= minimo) return valor
        println("  * Ingrese un numero entero valido (>= $minimo).")
    }
}

private fun leerDouble(mensaje: String, minimo: Double): Double {
    while (true) {
        print(mensaje)
        val valor = readLine()?.trim()?.replace(",", ".")?.toDoubleOrNull()
        if (valor != null && valor >= minimo) return valor
        println("  * Ingrese un numero valido (>= $minimo).")
    }
}

private fun leerTipo(): Int {
    while (true) {
        print("Tipo (1=Regular, 2=Importado, 3=Perecible): ")
        when (readLine()?.trim()?.toIntOrNull()) {
            1 -> return 1
            2 -> return 2
            3 -> return 3
            else -> println("  * Opcion invalida. Elija 1, 2 o 3.")
        }
    }
}

// ============================ SALIDA / BOLETA ============================

/** Imprime la boleta completa con el formato de la Figura 1 de la guia. */
fun imprimirBoleta(carrito: Carrito) {
    println("=========================================")
    println("     CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")
    println("Cliente: ${carrito.nombreCliente}")
    println()

    for (p in carrito.listar()) {
        println("Producto agregado: ${p.nombre}")
    }
    println()

    mostrarDetalle(carrito)

    println(String.format("%-23s : %d", "Cantidad de productos", carrito.cantidadItems()))
    println(String.format("%-23s : S/ %8.2f", "Subtotal", carrito.subtotal()))
    println(String.format("%-23s : S/ %8.2f", "IGV (18%)", carrito.igv()))
    println(String.format("%-23s : S/ %8.2f", "TOTAL A PAGAR", carrito.total()))
    println("---------------------------------------")

    val masCaro = carrito.productoMasCaro()
    if (masCaro != null) {
        println(String.format("Producto mas caro: %s (S/ %.2f)", masCaro.nombre, masCaro.precioBase))
    }

    val descuento = carrito.descuento()
    if (descuento > 0.0) {
        val porcentaje = if (carrito.total() > 5000) "10%" else "5%"
        val montoMinimo = if (carrito.total() > 5000) 5000 else 3000
        println("Descuento aplicado: $porcentaje por compra mayor a S/ $montoMinimo")
    } else {
        println("Descuento aplicado: ninguno (el total no supera S/ 3000)")
    }
    println(String.format("%-23s : S/ %8.2f", "TOTAL CON DESCUENTO", carrito.totalConDescuento()))
    println()
    println("Gracias por su compra, ${carrito.nombreCliente}!")
}

/**
 * Detalle del carrito con columnas alineadas.
 * POLIMORFISMO: llama a p.importe() sin preguntar de que tipo es cada producto.
 */
fun mostrarDetalle(carrito: Carrito) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in carrito.listar()) {
        println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, p.importe()))
        i++
    }
    println("---------------------------------------")
}
