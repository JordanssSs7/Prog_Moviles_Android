package com.reyes.lavacampus.model

/** Si el lavado (servicio por kilo) supera este peso, se aplica el descuento. */
const val UMBRAL_DESCUENTO_KG = 5.0
const val PORCENTAJE_DESCUENTO = 10 // %

/**
 * Descuento por volumen de UNA linea: 10 % del subtotal si es un servicio por KG con mas de 5 kg.
 * Devuelve el monto a descontar (0.0 si no aplica), redondeado a centimos.
 */
fun descuentoDeLinea(unidad: String, cantidad: Double, precio: Double): Double {
    if (unidad != Unidad.KG.name || cantidad <= UMBRAL_DESCUENTO_KG || precio <= 0.0) return 0.0
    val monto = cantidad * precio * PORCENTAJE_DESCUENTO / 100.0
    return Math.round(monto * 100.0) / 100.0
}
