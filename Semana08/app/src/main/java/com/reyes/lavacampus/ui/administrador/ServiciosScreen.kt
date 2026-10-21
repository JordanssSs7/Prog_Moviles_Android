package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.data.local.entity.Servicio
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit) {
    val servicios by vm.servicios.collectAsState()
    Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
            }
        }
    }
}

@Composable
private fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onEditar() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconoServicio(s.icono, MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(s.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "S/ %.2f por %s · %d h".format(s.precio, s.unidad.lowercase(), s.horasEntrega),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    if (s.activo) "Activo" else "Inactivo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
        }
    }
}
