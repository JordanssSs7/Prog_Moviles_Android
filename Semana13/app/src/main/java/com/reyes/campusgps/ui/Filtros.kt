package com.reyes.campusgps.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Mejora 1: chips de categoría. Solo dibuja; la lógica del filtro está en el ViewModel.
@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String?,
    onElegir: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Chip(texto = "Todos", activo = seleccionada == null) { onElegir(null) }
        categorias.forEach { c ->
            Chip(texto = c, activo = seleccionada == c) { onElegir(c) }
        }
    }
}

// Mejora 2: selector de radio (100, 200 o 500 m)
@Composable
fun SelectorRadio(
    radios: List<Int>,
    seleccionado: Int?,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Radio:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        radios.forEach { r ->
            Chip(texto = "$r m", activo = seleccionado == r) { onElegir(r) }
        }
    }
}

@Composable
private fun Chip(texto: String, activo: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = activo,
        onClick = onClick,
        label = { Text(texto, fontSize = 13.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = Color.White
        )
    )
}
