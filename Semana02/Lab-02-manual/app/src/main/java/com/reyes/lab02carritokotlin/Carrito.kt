package com.reyes.lab02carritokotlin

import com.reyes.lab02carritokotlin.model.Carrito
import com.reyes.lab02carritokotlin.model.ProductoImportado
import com.reyes.lab02carritokotlin.model.ProductoPerecible
import com.reyes.lab02carritokotlin.model.ProductoRegular

fun main() {
    val carrito = Carrito("Jordan Reyes")

    // POLIMORFISMO: agregamos productos de DISTINTOS tipos a la misma lista.
    // El carrito los trata a todos como "Producto" y cada uno calcula su
    // importe a su manera (regular, con impuesto de importacion, o perecible).
    carrito.agregar(ProductoImportado("Laptop HP", 2500.0, 1, 0.10))
    carrito.agregar(ProductoRegular("Mouse Logitech", 45.5, 2))
    carrito.agregar(ProductoImportado("iPhone 13 Pro Max", 4200.0, 1, 0.10))
    carrito.agregar(ProductoRegular("Reloj Casio", 55.5, 3))
    carrito.agregar(ProductoPerecible("Cafe en grano 1kg", 60.0, 2, 2))

    imprimirBoleta(carrito)

    // ---------- Reto: buscar y eliminar un producto ----------
    println()
    println("--- BUSQUEDA DE PRODUCTO ---")
    val buscado = "Mouse Logitech"
    val encontrado = carrito.buscar(buscado)
    if (encontrado != null) {
        println(String.format("Producto encontrado: %s - S/ %.2f", encontrado.nombre, encontrado.precioBase))
    } else {
        println("El producto '$buscado' no esta en el carrito.")
    }

    println()
    println("--- ELIMINACION DE PRODUCTO ---")
    val aEliminar = "Reloj Casio"
    if (carrito.eliminar(aEliminar)) {
        println("Se elimino '$aEliminar' del carrito.")
    } else {
        println("No se encontro '$aEliminar'.")
    }

    println()
    println("--- CARRITO ACTUALIZADO ---")
    imprimirBoleta(carrito)
}

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
