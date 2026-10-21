package com.reyes.lavacampus.ui.estudiante

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.ui.components.AppTopBar
import com.reyes.lavacampus.ui.components.IconoServicio
import com.reyes.lavacampus.ui.components.textoPrecio
import com.reyes.lavacampus.viewmodel.TarifasViewModel

@Composable
fun TarifasScreen(vm: TarifasViewModel, onVolver: () -> Unit) {
    val servicios by vm.servicios.collectAsState()

    Scaffold(topBar = { AppTopBar("Tarifas", onAtras = onVolver) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        IconoServicio(s.icono, MaterialTheme.colorScheme.primary)
                        Column {
                            Text(s.nombre, fontWeight = FontWeight.Bold)
                            Text(s.textoPrecio(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            Text("Entrega en ${s.horasEntrega} h", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Horario y reglas", fontWeight = FontWeight.Bold)
                        Text("Atención: lunes a sábado. Los domingos no se atiende.")
                        Text("• Mínimo 1 kg por servicio de lavado")
                        Text("• Máximo 3 pedidos activos por estudiante")
                    }
                }
            }
        }
    }
}
