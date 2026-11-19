package com.reyes.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import com.reyes.centrotareas.model.CargaUi
import com.reyes.centrotareas.model.PrimosUi
import com.reyes.centrotareas.ui.theme.VerdeFondo
import com.reyes.centrotareas.ui.theme.VerdeTexto
import com.reyes.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val tiempoMaximo by vm.tiempoMaximo.collectAsState()
    var textoN by rememberSaveable { mutableStateOf("500000") }
    var nCalculado by rememberSaveable { mutableIntStateOf(0) }
    // Prueba de vida: si deja de sumar, la pantalla está congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ----- Tarea con progreso (pasos 1 a 4)
            Tarjeta(titulo = "Tarea con progreso") {
                Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { vm.tareaLarga() },
                        enabled = !corriendo
                    ) { Text("Iniciar") }
                    OutlinedButton(
                        onClick = { vm.cancelar() },
                        enabled = corriendo
                    ) { Text("Cancelar") }
                }
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth(),
                    gapSize = 0.dp,
                    drawStopIndicator = {}
                )
                Text(
                    text = "${(progreso * 100).toInt()} %",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // ----- Contar números primos (paso 5)
            Tarjeta(titulo = "Contar números primos") {
                OutlinedTextField(
                    value = textoN,
                    // ignora lo que no sean dígitos
                    onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) textoN = nuevo },
                    label = { Text("Hasta el número") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                val calculando = primos is PrimosUi.Calculando
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val n = textoN.toIntOrNull()
                            if (n != null && n >= 2) { // ignora menores que 2
                                nCalculado = n
                                vm.contar(n)
                            }
                        },
                        enabled = !calculando
                    ) { Text("Contar") }
                    OutlinedButton(
                        onClick = { vm.cancelarPrimos() },
                        enabled = calculando
                    ) { Text("Cancelar") }
                }
                when (val p = primos) {
                    PrimosUi.Inicial -> Unit
                    PrimosUi.Calculando -> Text(
                        "Calculando...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    PrimosUi.Cancelado -> Text(
                        "Cancelado",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    is PrimosUi.Listo -> Recuadro(
                        fondo = VerdeFondo,
                        texto = VerdeTexto,
                        lineas = listOf(
                            "Primos hasta $nCalculado: ${p.cantidad}",
                            "Tiempo: ${p.milisegundos} ms"
                        )
                    )
                }
            }

            // ----- Dos cargas (pasos 6 y 7)
            Tarjeta(titulo = "Dos cargas") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = simularError,
                        onCheckedChange = { vm.cambiarSimularError(it) }
                    )
                    Text("Simular error", style = MaterialTheme.typography.bodyMedium)
                }
                // Mejora 2: tiempo máximo con withTimeout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = tiempoMaximo,
                        onCheckedChange = { vm.cambiarTiempoMaximo(it) }
                    )
                    Text("Tiempo máximo 4 s", style = MaterialTheme.typography.bodyMedium)
                }
                val cargando = carga is CargaUi.Cargando
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { vm.cargarUnaPorUna() },
                        enabled = !cargando
                    ) { Text("Una por una") }
                    Button(
                        onClick = { vm.cargarALaVez() },
                        enabled = !cargando
                    ) { Text("A la vez") }
                }
                when (val c = carga) {
                    CargaUi.Inicial -> Unit
                    CargaUi.Cargando -> CircularProgressIndicator()
                    is CargaUi.Exito -> Recuadro(
                        fondo = VerdeFondo,
                        texto = VerdeTexto,
                        lineas = listOf(
                            "Modo: ${c.modo}",
                            "Perfil: ${c.perfil}",
                            "Pedidos: ${c.pedidos}",
                            "Tiempo: ${c.milisegundos} ms"
                        )
                    )
                    is CargaUi.Error -> {
                        Recuadro(
                            fondo = MaterialTheme.colorScheme.errorContainer,
                            texto = MaterialTheme.colorScheme.onErrorContainer,
                            lineas = listOf(c.mensaje)
                        )
                        OutlinedButton(onClick = { vm.cargarALaVez() }) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}
