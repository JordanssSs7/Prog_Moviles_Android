package com.reyes.lavacampus

import com.reyes.lavacampus.viewmodel.ajustarCantidad
import org.junit.Assert.assertEquals
import org.junit.Test

class RepetirPedidoTest {

    @Test
    fun cantidadValida_seConserva() {
        assertEquals(3.5, ajustarCantidad("KG", 3.5), 0.001)
        assertEquals(5.0, ajustarCantidad("PRENDA", 5.0), 0.001)
    }

    @Test
    fun kilos_seAcercanAlPasoDeMedioKilo() {
        assertEquals(3.5, ajustarCantidad("KG", 3.6), 0.001)
        assertEquals(4.0, ajustarCantidad("KG", 3.9), 0.001)
    }

    @Test
    fun cantidadMayorAlMaximo_seRecorta() {
        assertEquals(10.0, ajustarCantidad("KG", 14.0), 0.001)
        assertEquals(30.0, ajustarCantidad("PRENDA", 45.0), 0.001)
    }

    @Test
    fun cantidadMenorAlMinimo_sube1() {
        assertEquals(1.0, ajustarCantidad("KG", 0.2), 0.001)
        assertEquals(1.0, ajustarCantidad("UNIDAD", 0.0), 0.001)
    }

    @Test
    fun prendas_sonEnteras() {
        assertEquals(3.0, ajustarCantidad("PRENDA", 2.7), 0.001)
    }
}
