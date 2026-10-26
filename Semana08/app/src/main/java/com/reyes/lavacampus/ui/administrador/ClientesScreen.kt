package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.ClienteResumen
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.Avatar
import com.reyes.lavacampus.ui.components.EstadoVacio
import com.reyes.lavacampus.ui.components.LavaCard
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.viewmodel.ClientesViewModel

@Composable
fun ClientesScreen(vm: ClientesViewModel) {
    val clientes by vm.clientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Clientes") }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LavaTextField(
                valor = busqueda,
                onCambio = vm::buscar,
                etiqueta = "Buscar por nombre o código",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            if (clientes.isEmpty()) {
                EstadoVacio("Sin resultados", "No hay clientes que coincidan con tu búsqueda.")
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clientes, key = { it.id }) { c -> TarjetaCliente(c) }
            }
        }
    }
}

@Composable
private fun TarjetaCliente(c: ClienteResumen) {
    LavaCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Avatar(c.nombre, tamano = 48.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(c.nombre, style = MaterialTheme.typography.titleSmall)
                Text(c.codigo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("S/ %.2f".format(c.gastado), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text("${c.pedidos} pedidos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
