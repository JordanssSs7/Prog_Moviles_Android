package com.reyes.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.material3.Switch
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.reyes.avisoscampus.receiver.ModoAvionReceiver
import com.reyes.avisoscampus.ui.theme.TextoSuave
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.reyes.avisoscampus.model.PermisoUi
import com.reyes.avisoscampus.ui.theme.GrisFondo
import com.reyes.avisoscampus.ui.theme.GrisTexto
import com.reyes.avisoscampus.ui.theme.RojoFondo
import com.reyes.avisoscampus.ui.theme.RojoTexto
import com.reyes.avisoscampus.ui.theme.VerdeFondo
import com.reyes.avisoscampus.ui.theme.VerdeTexto
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.avisoscampus.notificacion.Notificador
import com.reyes.avisoscampus.ui.theme.BordeTarjeta
import com.reyes.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current

    // Ya viene hecho: la pantalla recibe el evento y muestra la notificación
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel
        }
    }

    // Paso 2: pedir el permiso y mostrar su estado
    val permiso by vm.permiso.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    // Paso 5: segundos que faltan del recordatorio (null = no hay)
    val faltan by vm.faltan.collectAsState()

    LaunchedEffect(Unit) {
        // Si ya estaba concedido (o es Android < 13), se lo decimos al ViewModel
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    // Paso 6: registrar el receptor solo mientras el switch esté activo
    val escuchando by vm.escuchandoAvion.collectAsState()

    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) } // siempre se quita
        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Estado: ")
                    EtiquetaPermiso(permiso)
                }
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        vm.permisoRespondido(true)
                    }
                }) {
                    Text("Permitir notificaciones", fontWeight = FontWeight.Bold)
                }
            }
            Tarjeta("Avisos") {
                Button(onClick = { vm.avisarAhora() }) {
                    Text("Mostrar aviso", fontWeight = FontWeight.Bold)
                }
            }
            Tarjeta("Recordatorio") {
                val corriendo = faltan != null
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { vm.recordarEn(10) },
                        enabled = !corriendo
                    ) {
                        Text("Recordar en 10 s", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { vm.cancelarRecordatorio() },
                        enabled = corriendo,
                        border = BorderStroke(
                            1.5.dp,
                            if (corriendo) MaterialTheme.colorScheme.primary else BordeTarjeta
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                }
                faltan?.let {
                    Text("Faltan $it s", fontWeight = FontWeight.Bold)
                }
            }
            Tarjeta("Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Escuchar el modo avión", modifier = Modifier.weight(1f))
                    Switch(
                        checked = escuchando,
                        onCheckedChange = { vm.cambiarEscucha(it) }
                    )
                }
                Text(
                    "Activa o desactiva el modo avión del celular",
                    color = TextoSuave,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // TODO Paso 7: HistorialAvisos(avisos)
        }
    }
}

/** Etiqueta de color: Sin pedir (gris), Permitido (verde) o Denegado (rojo). */
@Composable
fun EtiquetaPermiso(permiso: PermisoUi) {
    val (texto, fondo, color) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", GrisFondo, GrisTexto)
        PermisoUi.Concedido -> Triple("Permitido", VerdeFondo, VerdeTexto)
        PermisoUi.Denegado -> Triple("Denegado", RojoFondo, RojoTexto)
    }
    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

/** Card blanca con borde suave y el título en azul. */
@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BordeTarjeta)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = titulo,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            contenido()
        }
    }
}
