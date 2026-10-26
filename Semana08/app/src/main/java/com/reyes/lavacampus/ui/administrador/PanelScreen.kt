package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.BotonCircular
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.graficos.IngresosPorServicio
import com.reyes.lavacampus.ui.components.graficos.ResumenEstados
import com.reyes.lavacampus.ui.theme.degradadoMarca
import com.reyes.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, onVerListos: () -> Unit, onVerAtrasados: () -> Unit, onMenu: () -> Unit) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val atrasados by vm.atrasados.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val recibiendo by vm.recibiendoPedidos.collectAsState()
    val pedidosSemana by vm.pedidosSemana.collectAsState()
    val fechaHoy = remember {
        "Hoy · " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEE dd MMM", Locale("es", "PE")))
            .replaceFirstChar { it.uppercase() }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Encabezado con degradado: titulo e ingresos de la semana
        Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(degradadoMarca(Rol.ADMIN))) {
            Column(
                modifier = Modifier.statusBarsPadding().padding(horizontal = 22.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("Panel de la lavandería", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(fechaHoy, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                Text(
                    "Ingresos de la semana",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 18.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("S/ %.2f".format(ingresos), style = MaterialTheme.typography.displaySmall, color = Color.White)
                    Text(
                        "$pedidosSemana pedidos",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
                Text(
                    "Solo pedidos pagados, desde el lunes",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }

        // Abre el menu lateral
        BotonCircular(
            onClick = onMenu,
            modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(top = 14.dp, end = 20.dp)
        ) { Icon(Icons.Filled.Menu, contentDescription = "Menú") }

        // Hoja que sube sobre el encabezado
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 215.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // La recepcion de pedidos esta cerrada: se avisa y se puede abrir desde aqui
                if (!recibiendo) {
                    Alerta(
                        texto = "La recepción de pedidos está cerrada",
                        fondo = Color(0xFFFEF3C7),
                        colorTexto = Color(0xFF92400E),
                        textoBoton = "Abrir",
                        onVer = vm::abrirRecepcion
                    )
                }

                // 3 contadores
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Contador("Hoy", pedidosHoy, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                    Contador("En proceso", enProceso, Color(0xFF637757), Modifier.weight(1f))
                    Contador("Por recoger", porRecoger, Color(0xFF435B45), Modifier.weight(1f))
                }

                // Alerta de pedidos LISTO esperando recojo
                if (porRecoger > 0) {
                    Alerta("$porRecoger pedido(s) esperan recojo", Color(0xFFFEF3C7), Color(0xFF92400E), onVerListos, subtitulo = "Avisa a los estudiantes")
                }
                // Alerta de pedidos atrasados (entrega vencida y sin cerrar)
                if (atrasados > 0) {
                    Alerta("$atrasados pedido(s) atrasado(s)", Color(0xFFFEE2E2), Color(0xFF991B1B), onVerAtrasados)
                }

                // Pedidos por estado: total, barra apilada y cantidad de cada estado
                Tarjeta("Pedidos por estado") { ResumenEstados(porEstado) }

                // Ingresos por servicio: monto, porcentaje y barra horizontal
                Tarjeta("Ingresos por servicio") {
                    if (porServicio.isEmpty()) {
                        Text("Aún no hay ingresos esta semana", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        IngresosPorServicio(porServicio)
                    }
                }
                // Espacio para que la barra flotante no tape el ultimo grafico
                Box(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, color: Color, modifier: Modifier) {
    LavaCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$valor", style = MaterialTheme.typography.headlineMedium, color = color)
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun Alerta(texto: String, fondo: Color, colorTexto: Color, onVer: () -> Unit, textoBoton: String = "Ver", subtitulo: String? = null) {
    LavaCard(modifier = Modifier.fillMaxWidth(), colorFondo = fondo) {
        Row(modifier = Modifier.padding(start = 18.dp, end = 12.dp, top = 12.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(texto, color = colorTexto, style = MaterialTheme.typography.titleSmall)
                subtitulo?.let { Text(it, color = colorTexto.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall) }
            }
            Button(onClick = onVer) { Text(textoBoton) }
        }
    }
}

@Composable
private fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    LavaCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}
