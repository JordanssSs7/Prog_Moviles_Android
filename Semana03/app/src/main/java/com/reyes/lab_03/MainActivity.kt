package com.reyes.lab_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lab_03.ui.theme.Lab03Theme
import java.util.Locale
import kotlin.math.round
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03Theme {
                RegistroNotasScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {
    var nota1 by remember { mutableStateOf(0f) }
    var nota2 by remember { mutableStateOf(0f) }
    var nota3 by remember { mutableStateOf(0f) }
    var nota4 by remember { mutableStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    val primaryPurple = Color(0xFF6750A4)

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8DEF8),
            Color(0xFFF7F2FA),
            Color(0xFFFFFBFE)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryPurple)
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Notas del ciclo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    text = "Desliza para asignar cada nota (0 a 20)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            CursoSlider(titulo = "Fundamentos de Programación", peso = "(20%)", nota = nota1) { nota1 = it }
            CursoSlider(titulo = "Programación Orientada a Objetos", peso = "(25%)", nota = nota2) { nota2 = it }
            CursoSlider(titulo = "Programación en Móviles", peso = "(30%)", nota = nota3) { nota3 = it }
            CursoSlider(titulo = "Base de Datos", peso = "(25%)", nota = nota4) { nota4 = it }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Redondear promedio final",
                        color = Color.Black
                    )
                    Switch(
                        checked = redondear,
                        onCheckedChange = { redondear = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryPurple
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = { confirmado = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primaryPurple,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirmo que las notas son correctas",
                        color = Color.Black
                    )
                }

                Button(
                    onClick = { mostrarResultado = true },
                    enabled = confirmado,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryPurple)
                ) {
                    Text(
                        text = "CALCULAR PROMEDIO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            if (!mostrarResultado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Desarrollado por: Jordan Reyes Saravia",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } else {
                val n1 = nota1.roundToInt()
                val n2 = nota2.roundToInt()
                val n3 = nota3.roundToInt()
                val n4 = nota4.roundToInt()

                val aporte1 = n1 * 0.20
                val aporte2 = n2 * 0.25
                val aporte3 = n3 * 0.30
                val aporte4 = n4 * 0.25

                val ponderado = aporte1 + aporte2 + aporte3 + aporte4
                val promedioFinal = if (redondear) round(ponderado) else ponderado

                val (observacion, colorChipBg, colorChipText) = when {
                    promedioFinal >= 17.0 -> Triple("EXCELENTE", Color(0xFF1B5E20), Color(0xFFC8E6C9))
                    promedioFinal >= 13.0 -> Triple("APROBADO", Color(0xFFDCEDC8), Color(0xFF2E7D32))
                    promedioFinal >= 10.0 -> Triple("EN RECUPERACIÓN", Color(0xFFFFF9C4), Color(0xFFF57F17))
                    else -> Triple("DESAPROBADO", Color(0xFFFFCDD2), Color(0xFFC62828))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Desglose por curso:",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Fundamentos: $n1 × 20% = " + String.format(Locale.US, "%.2f", aporte1),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "POO: $n2 × 25% = " + String.format(Locale.US, "%.2f", aporte2),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Móviles: $n3 × 30% = " + String.format(Locale.US, "%.2f", aporte3),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Base de Datos: $n4 × 25% = " + String.format(Locale.US, "%.2f", aporte4),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .height(1.dp)
                                .background(Color(0xFFE0E0E0))
                        )

                        Text(
                            text = "Promedio ponderado: " + String.format(Locale.US, "%.2f", ponderado)
                        )
                        Text(
                            text = "Promedio final: " + (if (redondear) String.format(Locale.US, "%.0f", promedioFinal) else String.format(Locale.US, "%.2f", promedioFinal)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryPurple
                        )
                        if (redondear) {
                            Text(
                                text = "(redondeado)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Surface(
                            color = colorChipBg,
                            shape = CircleShape
                        ) {
                            Text(
                                text = observacion,
                                color = colorChipText,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "✓ Promedio calculado correctamente",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Desarrollado por: Jordan Reyes Saravia",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoSlider(
    titulo: String,
    peso: String,
    nota: Float,
    onNotaChanged: (Float) -> Unit
) {
    val valorEntero = nota.roundToInt()
    val badgeFondo = if (valorEntero < 13) Color(0xFFFFCDD2) else Color(0xFFDCEDC8)
    val badgeTexto = if (valorEntero < 13) Color(0xFFC62828) else Color(0xFF2E7D32)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = peso,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6750A4)
                )
            }

            Slider(
                value = nota,
                onValueChange = { onNotaChanged(it.roundToInt().toFloat()) },
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier
                    .offset(x = (-8).dp)
                    .fillMaxWidth(),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color(0xFF6750A4), shape = CircleShape)
                    )
                },
                track = { sliderState ->
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                    ) {
                        val trackHeight = size.height
                        val strokeRadius = trackHeight / 2
                        val fraction = (sliderState.value - sliderState.valueRange.start) /
                                (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                        val activeWidth = size.width * fraction

                        drawRoundRect(
                            color = Color(0xFFE6E0E9),
                            cornerRadius = CornerRadius(strokeRadius, strokeRadius)
                        )
                        drawRoundRect(
                            color = Color(0xFF6750A4),
                            size = size.copy(width = activeWidth),
                            cornerRadius = CornerRadius(strokeRadius, strokeRadius)
                        )
                    }
                }
            )
        }

        Surface(
            color = badgeFondo,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(
                text = "$valorEntero",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = badgeTexto
            )
        }
    }
}