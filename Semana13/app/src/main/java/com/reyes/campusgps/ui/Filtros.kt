package com.reyes.campusgps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.campusgps.ui.theme.Lima
import com.reyes.campusgps.ui.theme.Negro
import com.reyes.campusgps.ui.theme.Superficie
import com.reyes.campusgps.ui.theme.TextoSecundario

// Mejora 1: selector de categoría tipo píldora. Solo dibuja; la lógica del filtro está en el ViewModel.
@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String?,
    onElegir: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Superficie)
            .horizontalScroll(rememberScrollState())
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OpcionPildora(texto = "Todos", activa = seleccionada == null) { onElegir(null) }
        categorias.forEach { c ->
            OpcionPildora(texto = c, activa = seleccionada == c) { onElegir(c) }
        }
    }
}

// Mejora 2: selector de radio (100, 200 o 500 m); el elegido se marca en lima
@Composable
fun SelectorRadio(
    radios: List<Int>,
    seleccionado: Int?,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Radio",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = TextoSecundario
        )
        Spacer(Modifier.width(10.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Superficie)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            radios.forEach { r ->
                OpcionPildora(
                    texto = "$r m",
                    activa = seleccionado == r,
                    fondoActivo = Lima,
                    textoActivo = Negro
                ) { onElegir(r) }
            }
        }
    }
}

@Composable
private fun OpcionPildora(
    texto: String,
    activa: Boolean,
    fondoActivo: Color = Negro,
    textoActivo: Color = Color.White,
    onClick: () -> Unit
) {
    Text(
        texto,
        fontSize = 13.sp,
        fontWeight = if (activa) FontWeight.SemiBold else FontWeight.Medium,
        color = if (activa) textoActivo else TextoSecundario,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (activa) fondoActivo else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}
