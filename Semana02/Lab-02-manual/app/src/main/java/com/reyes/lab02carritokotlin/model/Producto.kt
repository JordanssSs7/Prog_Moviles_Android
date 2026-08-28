package com.reyes.lab02carritokotlin.model

// ================================ ABSTRACCION ================================
// "Producto" es una clase ABSTRACTA: describe lo comun a todo producto del
// carrito (nombre, precio base y cantidad) y obliga a definir COMO se calcula
// su importe, pero NO se puede crear un "Producto" a secas: siempre sera uno
// de sus tipos concretos (ProductoRegular, ProductoImportado, ProductoPerecible).
abstract class Producto(
    val nombre: String,       // val -> el nombre no cambia luego de crear el producto
    val precioBase: Double,   // val -> precio unitario base fijo
    var cantidad: Int         // var -> la cantidad SI puede variar (se agrega/quita)
) {

    init {
        // Validacion en el constructor: nunca debe existir un producto invalido.
        require(nombre.isNotBlank()) { "El nombre del producto no puede estar vacio" }
        require(precioBase > 0.0)    { "El precio de '$nombre' debe ser mayor a 0" }
        require(cantidad > 0)        { "La cantidad de '$nombre' debe ser mayor a 0" }
    }

    /** Etiqueta del tipo de producto; cada subclase define la suya. */
    abstract val tipo: String

    /**
     * Importe total de esta linea del carrito (precio * cantidad + los ajustes
     * propios de cada tipo: impuestos de importacion, descuentos por vencimiento).
     * Metodo ABSTRACTO -> cada subclase esta OBLIGADA a implementarlo.
     */
    abstract fun importe(): Double
}
