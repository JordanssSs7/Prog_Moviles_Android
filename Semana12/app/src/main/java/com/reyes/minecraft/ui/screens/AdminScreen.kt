package com.reyes.minecraft.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.CabezaJugador
import com.reyes.minecraft.ui.components.ImagenItem
import com.reyes.minecraft.ui.components.TextoTitulo
import com.reyes.minecraft.ui.components.TipoBoton
import com.reyes.minecraft.ui.theme.Dorado
import com.reyes.minecraft.ui.theme.VerdeOscuro
import com.reyes.minecraft.ui.theme.VerdeSalvia
import com.reyes.minecraft.ui.viewmodel.AdminViewModel
import com.reyes.minecraft.ui.viewmodel.ItemPopularUi

@Composable
fun AdminScreen(vm: AdminViewModel) {
    val totales by vm.totales.collectAsStateWithLifecycle()
    val sincronizando by vm.sincronizando.collectAsStateWithLifecycle()
    val usuarios by vm.usuarios.collectAsStateWithLifecycle()
    val ranking by vm.ranking.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var confirmarLimpieza by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.mensajes.collect { snackbar.showSnackbar(it) } }

    Scaffold(
        topBar = { BarraSuperior("Panel Admin") },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            // ---- Resumen de este celular ----
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Contador("Guardados", totales.guardados, Modifier.weight(1f))
                Contador("Favoritos", totales.favoritos, Modifier.weight(1f))
            }
            BotonApp(
                texto = if (sincronizando) "Sincronizando..." else "Sincronizar ahora",
                onClick = vm::sincronizar,
                enabled = !sincronizando,
                modifier = Modifier.fillMaxWidth(),
            )
            BotonApp(
                texto = "Limpiar caché",
                onClick = { confirmarLimpieza = true },
                modifier = Modifier.fillMaxWidth(),
                tipo = TipoBoton.PELIGRO,
            )

            // ---- Ítems más marcados como favoritos (de todos los usuarios) ----
            Seccion("Ítems más favoritos") {
                when (val e = ranking) {
                    UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorComunidad(e.mensaje, vm::cargarComunidad)
                    is UiState.Exito -> if (e.datos.isEmpty()) {
                        Vacio("Todavía nadie marcó favoritos.")
                    } else {
                        e.datos.forEachIndexed { i, popular -> FilaRanking(i + 1, popular) }
                    }
                }
            }

            // ---- Usuarios registrados ----
            Seccion(
                titulo = when (val e = usuarios) {
                    is UiState.Exito -> "Usuarios registrados (${e.datos.size})"
                    else -> "Usuarios registrados"
                },
            ) {
                when (val e = usuarios) {
                    UiState.Cargando -> Cargando()
                    is UiState.Error -> ErrorComunidad(e.mensaje, vm::cargarComunidad)
                    is UiState.Exito -> if (e.datos.isEmpty()) {
                        Vacio("No hay usuarios registrados.")
                    } else {
                        e.datos.forEach { FilaUsuario(it) }
                    }
                }
            }

            BotonApp("Actualizar datos", vm::cargarComunidad, Modifier.fillMaxWidth(), tipo = TipoBoton.SECUNDARIO)
            Spacer(Modifier.height(8.dp))
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
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            contenido()
        }
    }
}

@Composable
private fun Cargando() {
    Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
private fun Vacio(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun ErrorComunidad(mensaje: String, onReintentar: () -> Unit) {
    Text(mensaje, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Start)
    BotonApp("Reintentar", onReintentar, Modifier.fillMaxWidth())
}

@Composable
private fun FilaRanking(posicion: Int, popular: ItemPopularUi) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Medallas de oro, plata y bronce para los tres primeros.
        val medalla = when (posicion) {
            1 -> Dorado
            2 -> Color(0xFFB0B7BD)
            3 -> Color(0xFFCD7F32)
            else -> MaterialTheme.colorScheme.primaryContainer
        }
        Box(Modifier.size(30.dp).background(medalla, CircleShape), contentAlignment = Alignment.Center) {
            Text("$posicion", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2B2B2B))
        }
        Spacer(Modifier.width(12.dp))
        Box(
            Modifier.size(44.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            popular.item?.let { ImagenItem(it.imagenesCandidatas, Modifier.size(32.dp)) }
        }
        Spacer(Modifier.width(12.dp))
        Text(popular.nombre, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(
            "${popular.votos} ♥",
            color = Color(0xFFE53935),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun FilaUsuario(usuario: Usuario) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CabezaJugador(usuario.jugador, 44.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(usuario.jugador, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(
                usuario.correo.ifBlank { "Sin correo guardado" },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        val admin = usuario.rol == Rol.ADMIN
        Text(
            if (admin) "ADMIN" else "JUGADOR",
            modifier = Modifier
                .background(if (admin) Dorado else MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (admin) Color(0xFF4A3800) else MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun Contador(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            TextoTitulo("$valor", 36.sp, VerdeOscuro, sombra = VerdeSalvia)
            Text(titulo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
