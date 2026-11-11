package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.viewmodel.AdminViewModel

@Composable
fun AdminScreen(vm: AdminViewModel, onCerrarSesion: () -> Unit) {
    val totales by vm.totales.collectAsStateWithLifecycle()
    val sincronizando by vm.sincronizando.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var confirmarLimpieza by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.mensajes.collect { snackbar.showSnackbar(it) } }

    Scaffold(
        topBar = { BarraSuperior("Panel Admin") { TextButton(onClick = onCerrarSesion) { Text("Salir") } } },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Contador("Guardados", totales.guardados, Modifier.weight(1f))
                Contador("Favoritos", totales.favoritos, Modifier.weight(1f))
            }
            Button(
                onClick = vm::sincronizar,
                enabled = !sincronizando,
                modifier = Modifier.fillMaxWidth(),
            ) { Text(if (sincronizando) "Sincronizando..." else "Sincronizar ahora") }
            OutlinedButton(
                onClick = { confirmarLimpieza = true },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Limpiar caché") }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text("¿Limpiar caché?") },
            text = { Text("Se borrarán todos los elementos guardados y también los favoritos de este celular.") },
            confirmButton = {
                TextButton(onClick = { confirmarLimpieza = false; vm.limpiarCache() }) { Text("Limpiar") }
            },
            dismissButton = { TextButton(onClick = { confirmarLimpieza = false }) { Text("Cancelar") } },
        )
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$valor", style = MaterialTheme.typography.displaySmall)
            Text(titulo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
