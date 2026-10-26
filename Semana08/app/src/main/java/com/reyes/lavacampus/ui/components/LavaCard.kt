package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Tarjeta blanca de esquinas muy redondeadas y sombra suave. Si recibe [onClick] es pulsable. */
@Composable
fun LavaCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colorFondo: Color = MaterialTheme.colorScheme.surface,
    borde: BorderStroke? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val colores = CardDefaults.cardColors(containerColor = colorFondo)
    val elevacion = CardDefaults.cardElevation(defaultElevation = 3.dp)
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = colores,
            elevation = elevacion,
            border = borde,
            content = contenido
        )
    } else {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            colors = colores,
            elevation = elevacion,
            border = borde,
            content = contenido
        )
    }
}
