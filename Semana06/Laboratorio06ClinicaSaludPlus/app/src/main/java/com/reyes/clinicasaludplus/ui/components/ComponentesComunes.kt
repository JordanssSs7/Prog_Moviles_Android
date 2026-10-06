package com.reyes.clinicasaludplus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.TextoGris
import com.reyes.clinicasaludplus.ui.theme.TextoOscuro

@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrimario,
            contentColor = Blanco
        )
    ) {
        Text(text = texto, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorConVolver(
    titulo: String,
    alVolver: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
        },
        navigationIcon = {
            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoOscuro
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun DialogoConfirmacion(
    mostrar: Boolean,
    titulo: String,
    mensaje: String,
    onConfirmar: () -> Unit,
    onDescartar: () -> Unit
) {
    if (mostrar) {
        AlertDialog(
            onDismissRequest = onDescartar,
            title = {
                Text(text = titulo, fontWeight = FontWeight.Bold, color = TextoOscuro)
            },
            text = {
                Text(text = mensaje, color = TextoGris)
            },
            confirmButton = {
                TextButton(onClick = onConfirmar) {
                    Text(text = "Confirmar", color = RojoAlerta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDescartar) {
                    Text(text = "Cancelar", color = TextoGris)
                }
            },
            containerColor = Blanco,
            shape = RoundedCornerShape(14.dp)
        )
    }
}