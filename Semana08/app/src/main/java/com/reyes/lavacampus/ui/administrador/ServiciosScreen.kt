package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.textoPrecio
import com.reyes.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit, onNuevo: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var porEliminar by remember { mutableStateOf<Servicio?>(null) }

    // Mensajes del ViewModel (por ejemplo "no se puede eliminar un servicio usado")
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = { AppTopBar("Servicios") },
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNuevo,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Nuevo servicio") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                ServicioItem(
                    s = s,
                    onToggle = { vm.alternar(s) },
                    onEditar = { onEditar(s.id) },
                    onEliminar = { porEliminar = s }
                )
            }
        }

        porEliminar?.let { s ->
            AlertDialog(
                onDismissRequest = { porEliminar = null },
                title = { Text("Eliminar servicio") },
                text = { Text("¿Eliminar «${s.nombre}»?") },
                confirmButton = {
                    TextButton(onClick = { vm.eliminar(s); porEliminar = null }) { Text("Eliminar") }
                },
                dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("No") } }
            )
        }
    }
}

@Composable
private fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onEditar() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconoServicio(s.icono, MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(s.nombre, fontWeight = FontWeight.Bold)
                Text("${s.textoPrecio()} · ${s.horasEntrega} h", style = MaterialTheme.typography.bodySmall)
                Text(
                    if (s.activo) "Activo" else "Inactivo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
