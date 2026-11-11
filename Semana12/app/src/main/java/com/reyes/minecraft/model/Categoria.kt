package com.reyes.minecraft.model

enum class Categoria(val titulo: String) {
    TODOS("Todos"),
    HERRAMIENTAS("Herramientas"),
    ARMADURA("Armadura"),
    COMIDA("Comida"),
    BLOQUES("Bloques"),
    OTROS("Otros"),
}

private val SUFIJOS_HERRAMIENTA = listOf("_pickaxe", "_axe", "_shovel", "_hoe", "_sword")
private val EXACTAS_HERRAMIENTA = setOf("bow", "crossbow", "trident", "shield", "fishing_rod", "shears", "flint_and_steel")
private val SUFIJOS_ARMADURA = listOf("_helmet", "_chestplate", "_leggings", "_boots")
private val PALABRAS_COMIDA = listOf(
    "apple", "bread", "beef", "porkchop", "chicken", "mutton", "rabbit", "cod", "salmon", "cookie",
    "cake", "pie", "stew", "carrot", "potato", "melon_slice", "beetroot", "honey_bottle", "steak",
    "sweet_berries", "glow_berries", "dried_kelp", "chorus_fruit", "tropical_fish", "pufferfish",
)
private val PALABRAS_BLOQUE = listOf(
    "stone", "planks", "log", "wood", "dirt", "sand", "gravel", "ore", "brick", "concrete", "wool",
    "glass", "slab", "stairs", "wall", "fence", "leaves", "terracotta", "grass", "deepslate",
    "netherrack", "obsidian", "block", "door", "button", "pressure_plate", "trapdoor",
)

/** Clasifica un ítem según su identificador (clave) de Minecraft. Nunca devuelve TODOS. */
fun categoriaDe(clave: String): Categoria = when {
    "spawn_egg" in clave || "bucket" in clave -> Categoria.OTROS
    clave in EXACTAS_HERRAMIENTA || SUFIJOS_HERRAMIENTA.any { clave.endsWith(it) } -> Categoria.HERRAMIENTAS
    SUFIJOS_ARMADURA.any { clave.endsWith(it) } || clave == "elytra" -> Categoria.ARMADURA
    PALABRAS_COMIDA.any { it in clave } || clave.startsWith("cooked_") -> Categoria.COMIDA
    PALABRAS_BLOQUE.any { it in clave } -> Categoria.BLOQUES
    else -> Categoria.OTROS
}
