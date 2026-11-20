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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.campusgps.model.LugarConDistancia
import com.reyes.campusgps.ui.theme.FondoHoja
import com.reyes.campusgps.ui.theme.Lima
import com.reyes.campusgps.ui.theme.Negro
import com.reyes.campusgps.ui.theme.TarjetaFondo
import com.reyes.campusgps.ui.theme.TextoSecundario
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            FilaLugar(
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
private fun FilaLugar(
    item: LugarConDistancia,
    seleccionada: Boolean,
    esMasCercano: Boolean,
    dentroRadio: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = TarjetaFondo),
        border = if (seleccionada) BorderStroke(1.5.dp, Negro) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo con la inicial de la categoría (lima si es el más cercano)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(if (esMasCercano) Lima else FondoHoja, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    item.lugar.categoria.take(1),
                    color = Negro,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    item.lugar.nombre,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        item.lugar.categoria,
                        color = TextoSecundario,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    // Mejora 2: el ViewModel ya calculó si está dentro del radio
                    if (dentroRadio) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "Dentro",
                            color = Negro,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            lineHeight = 14.sp,
                            modifier = Modifier
                                .background(Lima, RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Text(
                text = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                color = if (item.metros != null) Negro else TextoSecundario,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Spacer(Modifier.width(10.dp))
            // Botón circular negro con flecha (como en la referencia)
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Negro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "›",
                    color = if (seleccionada) Lima else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
