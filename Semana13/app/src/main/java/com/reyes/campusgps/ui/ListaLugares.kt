package com.reyes.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.ui.theme.AzulMarcador
import com.reyes.campusgps.ui.theme.TarjetaFondo
import com.reyes.campusgps.ui.theme.TarjetaSeleccionada
import com.reyes.campusgps.ui.theme.VerdeCercano
import com.reyes.campusgps.util.formatearDistancia

// No conoce el ViewModel: recibe la lista, el seleccionado y qué hacer al tocar
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    dentroDelRadio: Set<Int> = emptySet()
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            TarjetaLugar(
                item = item,
                seleccionada = item.lugar.id == seleccionado,
                esMasCercano = item.lugar.id == masCercanoId,
                dentroRadio = item.lugar.id in dentroDelRadio,
                onClick = { onClick(item.lugar.id) }
            )
        }
    }
}

@Composable
private fun TarjetaLugar(
    item: LugarConDistancia,
    seleccionada: Boolean,
    esMasCercano: Boolean,
    dentroRadio: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionada) TarjetaSeleccionada else TarjetaFondo
        ),
        border = if (seleccionada) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(if (esMasCercano) VerdeCercano else AzulMarcador, CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    item.lugar.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.lugar.categoria,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 16.sp
                    )
                    // Mejora 2: el ViewModel ya calculó si está dentro del radio
                    if (dentroRadio) {
                        Text(
                            "  · Dentro del radio",
                            color = VerdeCercano,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            Text(
                text = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
