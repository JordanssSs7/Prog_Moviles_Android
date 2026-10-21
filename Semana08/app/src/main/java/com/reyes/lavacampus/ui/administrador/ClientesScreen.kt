package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.ClienteResumen
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.LavaTextField
import com.reyes.lavacampus.viewmodel.ClientesViewModel

@Composable
fun ClientesScreen(vm: ClientesViewModel) {
    val clientes by vm.clientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()

    Scaffold(topBar = { AppTopBar("Clientes") }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LavaTextField(
                valor = busqueda,
                onCambio = vm::buscar,
                etiqueta = "Buscar por nombre o código",
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            )
            if (clientes.isEmpty()) {
                Text("No hay clientes que coincidan", modifier = Modifier.padding(16.dp))
            }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clientes, key = { it.id }) { c -> TarjetaCliente(c) }
            }
        }
    }
}

@Composable
private fun TarjetaCliente(c: ClienteResumen) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Avatar con las iniciales
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(iniciales(c.nombre), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(c.nombre, fontWeight = FontWeight.Bold)
                Text(c.codigo, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${c.pedidos} pedidos", style = MaterialTheme.typography.bodySmall)
                Text("S/ %.2f".format(c.gastado), fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun iniciales(nombre: String): String =
    nombre.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
