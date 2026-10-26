package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.BotonContorno
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.ui.components.SelectorPastilla
import com.reyes.lavacampus.viewmodel.ServiciosViewModel

private val Iconos = listOf("wash" to "Lavadora", "iron" to "Plancha", "shirt" to "Camisa")
private val Horas = listOf(24, 48, 72)

/** [servicioId] = 0 crea un servicio nuevo; otro valor edita ese servicio. */
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, servicioId: Int, onVolver: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val original = servicios.firstOrNull { it.id == servicioId }
    val cargado = servicioId == 0 || original != null

    // Los campos se (re)inician cuando el servicio termina de cargar
    var nombre by remember(cargado) { mutableStateOf(original?.nombre ?: "") }
    var unidad by remember(cargado) { mutableStateOf(original?.unidad ?: Unidad.KG.name) }
    var precio by remember(cargado) { mutableStateOf(original?.precio?.toString() ?: "") }
    var horas by remember(cargado) { mutableStateOf(original?.horasEntrega ?: 24) }
    var descripcion by remember(cargado) { mutableStateOf(original?.descripcion ?: "") }
    var icono by remember(cargado) { mutableStateOf(original?.icono ?: "wash") }
    var activo by remember(cargado) { mutableStateOf(original?.activo ?: true) }

    LaunchedEffect(guardado) { if (guardado) onVolver() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar(if (servicioId == 0) "Nuevo servicio" else "Editar servicio", onAtras = onVolver) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            LavaTextField(valor = nombre, onCambio = { nombre = it }, etiqueta = "Nombre del servicio")

            Seccion("Se cobra por") {
                SelectorPastilla(
                    opciones = Unidad.entries.map { it.etiqueta },
                    seleccionada = Unidad.entries.indexOfFirst { it.name == unidad }.coerceAtLeast(0),
                    onSeleccionar = { unidad = Unidad.entries[it].name }
                )
            }

            LavaTextField(
                valor = precio,
                onCambio = { precio = it },
                etiqueta = "Precio (S/)",
                teclado = KeyboardType.Decimal
            )

            Seccion("Tiempo de entrega") {
                SelectorPastilla(
                    opciones = Horas.map { "$it h" },
                    seleccionada = Horas.indexOf(horas).coerceAtLeast(0),
                    onSeleccionar = { horas = Horas[it] }
                )
            }

            LavaTextField(valor = descripcion, onCambio = { descripcion = it }, etiqueta = "Descripción", singleLine = false)

            Seccion("Ícono") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Iconos.forEach { (clave, texto) ->
                        val elegido = icono == clave
                        Surface(
                            onClick = { icono = clave },
                            modifier = Modifier.weight(1f).height(86.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = if (elegido) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(2.dp, if (elegido) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconoServicio(clave, MaterialTheme.colorScheme.primary, tamano = 38.dp)
                                Text(texto, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                    }
                }
            }

            LavaCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Servicio activo", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Los estudiantes podrán pedirlo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = activo, onCheckedChange = { activo = it })
                }
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.titleSmall) }

            LavaButton(
                texto = "Guardar servicio",
                onClick = {
                    vm.guardar(
                        Servicio(
                            id = servicioId,
                            nombre = nombre.trim(),
                            descripcion = descripcion.trim(),
                            unidad = unidad,
                            precio = precio.replace(',', '.').toDoubleOrNull() ?: 0.0,
                            horasEntrega = horas,
                            activo = activo,
                            icono = icono
                        )
                    )
                }
            )
            BotonContorno("Cancelar", onVolver, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleSmall)
        contenido()
    }
}
