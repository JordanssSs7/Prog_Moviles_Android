package com.reyes.minecraft.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.ui.components.BarraSuperior
import com.reyes.minecraft.ui.components.BotonApp
import com.reyes.minecraft.ui.components.ImagenItem
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.components.PantallaError
import com.reyes.minecraft.ui.components.TipoBoton
import com.reyes.minecraft.ui.viewmodel.QuizViewModel

@Composable
fun QuizScreen(vm: QuizViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Scaffold(topBar = { BarraSuperior("Quiz de pilas") }) { padding ->
        when (val e = estado) {
            UiState.Cargando -> PantallaCargando(Modifier.padding(padding))
            is UiState.Error -> PantallaError(e.mensaje, onReintentar = vm::siguiente, modifier = Modifier.padding(padding))
            is UiState.Exito -> {
                val q = e.datos
                val salto by rememberInfiniteTransition(label = "salto").animateFloat(
                    initialValue = 0f,
                    targetValue = -14f,
                    animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
                    label = "saltoY",
                )
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        "Aciertos ${q.aciertos}/${q.intentos}   ·   Racha ${q.racha}",
                        fontWeight = FontWeight.Bold,
                    )
                    ImagenItem(q.item.imagenesCandidatas, Modifier.size(130.dp).graphicsLayer { translationY = salto })
                    Text(q.item.nombre, style = MaterialTheme.typography.headlineSmall)
                    Text("¿Cuántos caben en una pila?", color = MaterialTheme.colorScheme.onSurfaceVariant)

                    QuizViewModel.OPCIONES.forEach { opcion ->
                        val tipo = when {
                            q.respuesta == null -> TipoBoton.PRINCIPAL
                            opcion == q.correcta -> TipoBoton.PRINCIPAL
                            opcion == q.respuesta -> TipoBoton.PELIGRO
                            else -> TipoBoton.SECUNDARIO
                        }
                        BotonApp(
                            texto = "$opcion",
                            onClick = { vm.responder(opcion) },
                            enabled = q.respuesta == null,
                            tipo = tipo,
                            modifier = Modifier.fillMaxWidth(0.6f),
                        )
                    }

                    AnimatedVisibility(visible = q.respuesta != null, enter = fadeIn() + scaleIn()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                if (q.acerto) "¡Correcto!" else "Casi: eran ${q.correcta}",
                                color = if (q.acerto) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                            )
                            BotonApp("SIGUIENTE", vm::siguiente, Modifier.fillMaxWidth(0.6f))
                        }
                    }
                }
            }
        }
    }
}
