package com.reyes.minecraft

import com.reyes.minecraft.data.remote.RecetaDto
import com.reyes.minecraft.data.remote.ResultadoRecetaDto
import com.reyes.minecraft.data.toReceta
import com.reyes.minecraft.data.toTexto
import com.reyes.minecraft.model.Categoria
import com.reyes.minecraft.model.categoriaDe
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoriaYRecetaTest {

    @Test
    fun `clasifica herramientas armadura y comida por su clave`() {
        assertEquals(Categoria.HERRAMIENTAS, categoriaDe("diamond_pickaxe"))
        assertEquals(Categoria.HERRAMIENTAS, categoriaDe("bow"))
        assertEquals(Categoria.ARMADURA, categoriaDe("iron_chestplate"))
        assertEquals(Categoria.COMIDA, categoriaDe("golden_apple"))
        assertEquals(Categoria.BLOQUES, categoriaDe("oak_planks"))
        assertEquals(Categoria.OTROS, categoriaDe("cod_spawn_egg"))
    }

    @Test
    fun `receta con forma se guarda y se lee igual`() {
        val dto = RecetaDto(
            result = ResultadoRecetaDto(id = 10, count = 4),
            inShape = listOf(listOf(5, 5), listOf(5, null)),
        )

        val receta = dto.toTexto().toReceta()

        assertEquals(4, receta.cantidad)
        assertEquals(listOf(listOf(5, 5), listOf(5, null)), receta.filas)
        assertTrue(receta.existe)
    }

    @Test
    fun `receta sin forma se acomoda de a tres por fila`() {
        val dto = RecetaDto(
            result = ResultadoRecetaDto(id = 1),
            ingredients = listOf(1, 2, 3, 4),
        )

        assertEquals(listOf(listOf(1, 2, 3), listOf(4)), dto.toTexto().toReceta().filas)
    }

    @Test
    fun `texto de sin receta indica que el item no se fabrica`() {
        assertFalse(com.reyes.minecraft.data.SIN_RECETA.toReceta().existe)
    }
}
