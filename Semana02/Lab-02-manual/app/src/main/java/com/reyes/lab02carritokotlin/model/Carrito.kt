package com.reyes.lab02carritokotlin.model

// ============================= ENCAPSULAMIENTO =============================
// La clase Carrito guarda los productos en una lista PRIVADA: nadie de afuera
// puede modificarla directamente. El resto del programa solo puede tocar el
// carrito a traves de sus metodos publicos (agregar, eliminar, listar...),
// que son los que controlan y validan los datos.
class Carrito(val nombreCliente: String) {

    init {
        require(nombreCliente.isNotBlank()) { "El nombre del cliente no puede estar vacio" }
    }

    // Lista PRIVADA: fuera de esta clase no existe.
    private val items = mutableListOf<Producto>()

    /** Copia de solo lectura de los productos (no expone la lista interna). */
    fun listar(): List<Producto> = items.toList()

    /** Numero de lineas en el carrito. */
    fun cantidadItems(): Int = items.size

    fun estaVacio(): Boolean = items.isEmpty()

    /** Agrega un producto al carrito. */
    fun agregar(producto: Producto) {
        items.add(producto)
    }

    /** Busca un producto por nombre (ignora mayusculas); null si no existe. */
    fun buscar(nombre: String): Producto? =
        items.find { it.nombre.equals(nombre, ignoreCase = true) }

    /** Elimina el producto con ese nombre; devuelve true si elimino alguno. */
    fun eliminar(nombre: String): Boolean =
        items.removeIf { it.nombre.equals(nombre, ignoreCase = true) }

    fun vaciar() = items.clear()

    // ---- Calculos del carrito ----
    // POLIMORFISMO: subtotal() llama a p.importe() sin saber si cada p es
    // ProductoRegular, ProductoImportado o ProductoPerecible; cada uno
    // responde con su propia formula.

    fun subtotal(): Double {
        var suma = 0.0
        for (p in items) {
            suma += p.importe()
        }
        return suma
    }

    fun igv(): Double = subtotal() * IGV

    fun total(): Double = subtotal() + igv()

    /** Descuento sobre el TOTAL segun el monto (regla de la tienda). */
    fun descuento(): Double = when {
        total() > MONTO_DESC_10 -> total() * 0.10
        total() > MONTO_DESC_5  -> total() * 0.05
        else -> 0.0
    }

    fun totalConDescuento(): Double = total() - descuento()

    /** Producto de mayor precio base; null si el carrito esta vacio. */
    fun productoMasCaro(): Producto? = items.maxByOrNull { it.precioBase }

    private companion object {
        const val IGV = 0.18
        const val MONTO_DESC_5 = 3000.0
        const val MONTO_DESC_10 = 5000.0
    }
}
