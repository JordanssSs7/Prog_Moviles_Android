package com.reyes.lavacampus

import com.reyes.lavacampus.model.descuentoDeLinea
import com.reyes.lavacampus.viewmodel.calcularFechaEntrega
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DescuentoTest {

    @Test
    fun exactamente5kg_noTieneDescuento() {
        assertEquals(0.0, descuentoDeLinea("KG", 5.0, 8.0), 0.001)
    }

    @Test
    fun masDe5kg_aplica10porciento() {
        // 6 kg x S/ 8.00 = 48.00 -> 10 % = 4.80
        assertEquals(4.80, descuentoDeLinea("KG", 6.0, 8.0), 0.001)
    }

    @Test
    fun maximo10kg_aplica10porciento() {
        assertEquals(8.0, descuentoDeLinea("KG", 10.0, 8.0), 0.001)
    }

    @Test
    fun prendasNoTienenDescuento() {
        assertEquals(0.0, descuentoDeLinea("PRENDA", 30.0, 2.5), 0.001)
        assertEquals(0.0, descuentoDeLinea("UNIDAD", 30.0, 20.0), 0.001)
    }

    @Test
    fun precioInvalido_noTieneDescuento() {
        assertEquals(0.0, descuentoDeLinea("KG", 8.0, 0.0), 0.001)
    }

    @Test
    fun descuentoSeRedondeaACentimos() {
        // 5.5 kg x S/ 8.35 = 45.925 -> 10 % = 4.5925 -> 4.59
        assertEquals(4.59, descuentoDeLinea("KG", 5.5, 8.35), 0.0001)
    }

    @Test
    fun ejemploDelLaboratorio_sinDescuento_total40_50() {
        // 3.5 kg de lavado + 5 prendas de planchado = S/ 40.50 (no supera 5 kg)
        val total = 3.5 * 8.0 - descuentoDeLinea("KG", 3.5, 8.0) + 5 * 2.5
        assertEquals(40.50, total, 0.001)
    }

    @Test
    fun entregaQueCaeDomingo_pasaAlLunes() {
        // 2026-10-24 es sabado; +1 dia = domingo 25 -> lunes 26
        val fecha = calcularFechaEntrega(LocalDate.of(2026, 10, 24), 1)
        assertEquals(LocalDate.of(2026, 10, 26), fecha)
    }
}
