package com.reyes.lab02carritokotlin.model

// ================================ HERENCIA ================================
// ProductoPerecible HEREDA de Producto y AGREGA los dias que le faltan para
// vencer. Si esta por vencer (3 dias o menos) se le aplica 20% de descuento
// a la linea para impulsar su venta.
class ProductoPerecible(
    nombre: String,
    precioBase: Double,
    cantidad: Int,
    val diasParaVencer: Int
) : Producto(nombre, precioBase, cantidad) {

    init {
        require(diasParaVencer >= 0) { "Los dias para vencer no pueden ser negativos" }
    }

    override val tipo: String = "Perecible"

    /** true si el producto esta por vencer y le corresponde descuento. */
    val porVencer: Boolean
        get() = diasParaVencer <= DIAS_LIMITE

    // Aplica el descuento por vencimiento solo cuando corresponde.
    override fun importe(): Double {
        val bruto = precioBase * cantidad
        return if (porVencer) bruto * (1.0 - DESCUENTO_VENCIMIENTO) else bruto
    }

    override fun toString(): String {
        val nota = if (porVencer) "vence en $diasParaVencer d, -20%" else "vence en $diasParaVencer d"
        return String.format(
            "[%s] %-20s S/ %8.2f x%d (%s)",
            tipo, nombre, precioBase, cantidad, nota
        )
    }

    private companion object {
        const val DIAS_LIMITE = 3
        const val DESCUENTO_VENCIMIENTO = 0.20
    }
}
