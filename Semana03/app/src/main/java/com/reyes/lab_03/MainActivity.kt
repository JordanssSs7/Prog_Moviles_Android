package com.reyes.lab_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.lab_03.ui.theme.Lab03Theme
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

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun CursoSlider(
    titulo: String,
    peso: String,
    nota: Float,
    onNotaChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    color = Color(0xFF6750A4)
                )
            }

            Slider(
                value = nota,
                onValueChange = { onNotaChanged(it.roundToInt().toFloat()) },
                valueRange = 0f..20f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF6750A4),
                    activeTrackColor = Color(0xFF6750A4)
                )
            )
        }

        Surface(
            color = Color(0xFFE8DEF8),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(
                text = "${nota.roundToInt()}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6750A4)
            )
        }
    }
}