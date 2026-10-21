package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.model.Unidad
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.LavaButton
import com.reyes.lavacampus.ui.components.LavaTextField
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
        topBar = { AppTopBar(if (servicioId == 0) "Nuevo servicio" else "Editar servicio", onAtras = onVolver) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LavaTextField(valor = nombre, onCambio = { nombre = it }, etiqueta = "Nombre")

            Text("Se cobra por", fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Unidad.entries.forEach { u ->
                    FilterChip(selected = unidad == u.name, onClick = { unidad = u.name }, label = { Text(u.etiqueta) })
                }
            }

            LavaTextField(
                valor = precio,
                onCambio = { precio = it },
                etiqueta = "Precio (S/)",
                teclado = KeyboardType.Decimal
            )

            Text("Tiempo de entrega", fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Horas.forEach { h ->
                    FilterChip(selected = horas == h, onClick = { horas = h }, label = { Text("$h h") })
                }
            }

            LavaTextField(valor = descripcion, onCambio = { descripcion = it }, etiqueta = "Descripción", singleLine = false)

            Text("Ícono", fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Iconos.forEach { (clave, texto) ->
                    FilterChip(selected = icono == clave, onClick = { icono = clave }, label = { Text(texto) })
                }
            }
            IconoServicio(icono, MaterialTheme.colorScheme.primary, tamano = 56.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Servicio activo")
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold) }

            LavaButton(
                texto = "Guardar",
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
        }
    }
}
