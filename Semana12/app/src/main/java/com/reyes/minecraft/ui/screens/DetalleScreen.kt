package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.BotonFavorito
import com.reyes.minecraft.ui.components.ImagenItem
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.viewmodel.DetalleViewModel

@Composable
fun DetalleScreen(vm: DetalleViewModel, onAtras: () -> Unit) {
    val estado by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BarraSuperior("Detalle", onAtras = onAtras) {
                (estado as? UiState.Exito)?.let {
                    BotonFavorito(it.datos.item.esFavorito, vm::alternarFavorito)
                }
            }
        },
    ) { padding ->
        when (val e = estado) {
            UiState.Cargando -> PantallaCargando(Modifier.padding(padding))
            is UiState.Error -> PantallaError(e.mensaje, onReintentar = vm::cargarDetalle, modifier = Modifier.padding(padding))
            is UiState.Exito -> {
                val item = e.datos.item
                Column(
                    modifier = Modifier.padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ImagenItem(item.imagenesCandidatas, Modifier.size(160.dp))
                    Text(item.nombre, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "Identificador: ${item.clave}  ·  Máx. por pila: ${item.stackSize ?: "-"}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    when {
                        e.datos.cargandoDetalle && item.descripcion == null -> CircularProgressIndicator()
                        item.descripcion != null -> Text(item.descripcion, modifier = Modifier.fillMaxWidth())
                        else -> {
                            Text(
                                e.datos.aviso ?: "No hay descripción disponible para este elemento.",
                                color = MaterialTheme.colorScheme.error,
                            )
                            if (e.datos.aviso != null) {
                                Button(onClick = vm::cargarDetalle) { Text("Reintentar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
