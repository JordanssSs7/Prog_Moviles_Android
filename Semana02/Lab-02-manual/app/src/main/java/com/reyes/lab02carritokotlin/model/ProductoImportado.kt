package com.reyes.lab02carritokotlin.model

// ================================ HERENCIA ================================
// ProductoImportado HEREDA de Producto y AGREGA un dato propio: el impuesto
// de importacion (una tasa; 0.10 = 10%) que se cobra sobre cada unidad.
class ProductoImportado(
    nombre: String,
    precioBase: Double,
    cantidad: Int,
    val impuestoImportacion: Double   // 0.10 = 10 %
) : Producto(nombre, precioBase, cantidad) {

    init {
        require(impuestoImportacion >= 0.0) { "El impuesto de importacion no puede ser negativo" }
    }

    //override = sobreescribir
    override val tipo: String = "Importado"

    // El importe suma el impuesto de importacion al precio base de cada unidad.
    override fun importe(): Double =
        precioBase * cantidad * (1.0 + impuestoImportacion)

    override fun toString(): String {
        val porcentaje = (impuestoImportacion * 100).toInt()
        return String.format(
            "[%s] %-20s S/ %8.2f x%d (+%d%% imp.)",
            tipo, nombre, precioBase, cantidad, porcentaje
        )
    }
}
// %s : string(texto)
//%-20s : string, -20 hace que el texto ocupe 20 espacios alineados a la izquierda
//8.2f : ocupa 8 espacios en total y seran 2 decimales
//x%d : %d para digitos numeros enteroos
// (+%d%% imp.) : entero, %d es para el porocentaje, %% imprime el simboolo de porcentaje real
//ej : [Electrónico] Laptop HP            S/  2500.00 x2 (+18% imp.)