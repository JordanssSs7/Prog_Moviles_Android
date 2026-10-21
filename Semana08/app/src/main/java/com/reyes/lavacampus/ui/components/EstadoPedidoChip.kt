package com.reyes.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.EstadoPedido

@Composable
fun EstadoPedidoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    Text(
        text = estado.etiqueta,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = estado.color,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(estado.color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
