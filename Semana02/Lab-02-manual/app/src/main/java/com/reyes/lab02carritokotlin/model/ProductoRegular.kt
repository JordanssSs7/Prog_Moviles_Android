package com.reyes.lab02carritokotlin.model

// ================================ HERENCIA ================================
// ProductoRegular HEREDA de Producto (": Producto(...)"). Es el producto mas
// simple de la tienda: su importe es solo precio unitario * cantidad, sin
// impuestos ni descuentos extra.
class ProductoRegular(
    nombre: String,
    precioBase: Double,
    cantidad: Int
) : Producto(nombre, precioBase, cantidad) {

    override val tipo: String = "Regular"

    // Implementa a su manera el metodo abstracto heredado de Producto.
    override fun importe(): Double = precioBase * cantidad

    override fun toString(): String =
        String.format("[%s] %-20s S/ %8.2f x%d", tipo, nombre, precioBase, cantidad)
}
