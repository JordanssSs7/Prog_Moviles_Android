package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.data.local.relation.PedidoConDetalle
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.PORCENTAJE_DESCUENTO
import com.reyes.lavacampus.model.UMBRAL_DESCUENTO_KG
import com.reyes.lavacampus.ui.components.Avatar
import com.reyes.lavacampus.ui.components.BotonCircular
import com.reyes.lavacampus.ui.components.EstadoPedidoChip
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.IlustracionLavadora
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.PedidoCard
import com.reyes.lavacampus.ui.components.StepperEstado
import com.reyes.lavacampus.ui.components.fechaCorta
import com.reyes.lavacampus.ui.theme.TurquesaIntermedio
import com.reyes.lavacampus.ui.theme.TurquesaProfundo
import com.reyes.lavacampus.ui.theme.Negro
import com.reyes.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onVerPedido: (Int) -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onMenu: () -> Unit
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val recibiendo by vm.recibiendoPedidos.collectAsState()
    val primerNombre = vm.usuario.nombre.substringBefore(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        // Menu a la izquierda y saludo a la derecha
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BotonCircular(onClick = onMenu) { Icon(Icons.Filled.Menu, contentDescription = "Menú") }
            Spacer(Modifier.weight(1f))
            Text("Hola, $primerNombre", style = MaterialTheme.typography.titleLarge, maxLines = 1)
            Avatar(vm.usuario.nombre)
        }

        // Aviso claro cuando el administrador cerro la recepcion de pedidos
        if (!recibiendo) {
            LavaCard(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                colorFondo = Color(0xFFFEF3C7)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        "La lavandería no está recibiendo pedidos",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        "Por ahora no puedes crear uno nuevo. Vuelve a intentarlo más tarde.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF92400E)
                    )
                }
            }
        }

        TarjetaPedidoActivo(activo, onVerPedido, modifier = Modifier.padding(horizontal = 20.dp))

        BannerOferta(onPedir = onNuevoPedido, modifier = Modifier.padding(horizontal = 20.dp))

        // Servicios como categorias
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            EncabezadoSeccion("Servicios", "Ver tarifas", onTarifas)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(servicios, key = { it.id }) { s -> ChipServicio(s, onNuevoPedido) }
            }
        }

        // Ultimos pedidos
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EncabezadoSeccion("Últimos pedidos", "Ver todos", onMisPedidos, horizontal = 0)
            if (recientes.isEmpty()) {
                Text("Aún no has hecho pedidos", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                recientes.forEach { p -> PedidoCard(p, onClick = { onVerPedido(p.pedido.id) }) }
            }
        }
    }
}

@Composable
private fun EncabezadoSeccion(titulo: String, accion: String, onAccion: () -> Unit, horizontal: Int = 20) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = horizontal.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onAccion, contentPadding = PaddingValues(horizontal = 4.dp)) {
            Text(accion, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/** Pedido activo con la linea de seguimiento; si no hay ninguno, un mensaje amable. */
@Composable
private fun TarjetaPedidoActivo(activo: PedidoConDetalle?, onVerPedido: (Int) -> Unit, modifier: Modifier = Modifier) {
    LavaCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            if (activo == null) {
                Text("No tienes pedidos activos", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Cuando hagas un pedido, aquí verás cómo avanza tu ropa.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val estado = EstadoPedido.valueOf(activo.pedido.estado)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Tu pedido activo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Pedido #${activo.pedido.id}", style = MaterialTheme.typography.titleLarge)
                    }
                    EstadoPedidoChip(estado)
                }
                StepperEstado(estado = estado, vertical = false)
                if (estado == EstadoPedido.LISTO && activo.pedido.casillero != null) {
                    Text(
                        "Recógelo en el casillero ${activo.pedido.casillero}",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF15803D),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFE8F8EE))
                            .padding(12.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Entrega estimada: ${activo.pedido.fechaEntrega.fechaCorta()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = { onVerPedido(activo.pedido.id) }) {
                        Text("Ver detalle", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

/** Banner azul con la oferta por volumen y la lavadora (como el "Exclusive Offers" de la referencia). */
@Composable
private fun BannerOferta(onPedir: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(186.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(TurquesaProfundo, TurquesaIntermedio)))
    ) {
        // Circulos decorativos
        Box(Modifier.size(150.dp).align(Alignment.TopEnd).padding(end = 0.dp).background(Color.White.copy(alpha = 0.07f), CircleShape))
        Column(
            modifier = Modifier.fillMaxHeight().width(205.dp).padding(start = 22.dp, top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Oferta de la semana",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                Text(
                    "$PORCENTAJE_DESCUENTO % menos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    "en lavados de más de ${UMBRAL_DESCUENTO_KG.toInt()} kg",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
            Surface(onClick = onPedir, shape = CircleShape, color = Negro) {
                Text(
                    "Nuevo pedido",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
        IlustracionLavadora(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 14.dp, bottom = 14.dp)
                .size(width = 128.dp, height = 140.dp)
        )
    }
}

@Composable
private fun ChipServicio(s: Servicio, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 18.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconoServicio(s.icono, MaterialTheme.colorScheme.primary, tamano = 24.dp)
            Text(s.nombre, style = MaterialTheme.typography.labelLarge)
        }
    }
}
