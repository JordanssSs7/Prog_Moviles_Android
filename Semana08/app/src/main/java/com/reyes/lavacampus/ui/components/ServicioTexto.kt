package com.reyes.lavacampus.ui.components

import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.Unidad

/** KG, PRENDA y UNIDAD se muestran como "por kg", "por prenda" y "por unidad". */
fun Servicio.textoPrecio(): String {
    val porUnidad = when (unidad) {
        Unidad.KG.name -> "por kg"
        Unidad.PRENDA.name -> "por prenda"
        else -> "por unidad"
    }
    return "S/ %.2f %s".format(precio, porUnidad)
}
