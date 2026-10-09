package com.reyes.clinicasaludplus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.model.Medico
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.ui.theme.Tam

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
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VerdePrimario,
            contentColor = Blanco,
            disabledContainerColor = VerdePrimario.copy(alpha = 0.35f),
            disabledContentColor = Blanco
        )
    ) {
        Text(text = texto, fontWeight = FontWeight.SemiBold, fontSize = Tam.Subtitulo)
    }
}

/** Barra superior del diseño: flecha a la izquierda, título centrado en azul oscuro y acciones opcionales. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorConVolver(
    titulo: String,
    alVolver: () -> Unit,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                fontSize = Tam.Barra,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo
            )
        },
        navigationIcon = {
            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = NavyTitulo
                )
            }
        },
        actions = acciones,
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
                Text(text = titulo, fontWeight = FontWeight.Bold, color = NavyTitulo, fontSize = Tam.Barra)
            },
            text = {
                Text(text = mensaje, color = SlateTexto, fontSize = Tam.Cuerpo)
            },
            confirmButton = {
                TextButton(onClick = onConfirmar) {
                    Text(text = "Confirmar", color = RojoAlerta, fontWeight = FontWeight.Bold, fontSize = Tam.Cuerpo)
                }
            },
            dismissButton = {
                TextButton(onClick = onDescartar) {
                    Text(text = "Cancelar", color = SlateTexto, fontSize = Tam.Cuerpo)
                }
            },
            containerColor = Blanco,
            shape = RoundedCornerShape(14.dp)
        )
    }
}

/** Diálogo informativo con un solo botón "Entendido". */
@Composable
fun DialogoInformativo(
    mostrar: Boolean,
    titulo: String,
    mensaje: String,
    onCerrar: () -> Unit,
    textoBoton: String = "Entendido"
) {
    if (mostrar) {
        AlertDialog(
            onDismissRequest = onCerrar,
            title = { Text(text = titulo, fontWeight = FontWeight.Bold, color = NavyTitulo, fontSize = Tam.Barra) },
            text = { Text(text = mensaje, color = SlateTexto, fontSize = Tam.Cuerpo) },
            confirmButton = {
                TextButton(onClick = onCerrar) {
                    Text(text = textoBoton, color = VerdePrimario, fontWeight = FontWeight.Bold, fontSize = Tam.Cuerpo)
                }
            },
            containerColor = Blanco,
            shape = RoundedCornerShape(14.dp)
        )
    }
}

/** Foto circular del médico; si no tiene foto muestra un ícono de persona. */
@Composable
fun FotoMedico(medico: Medico?, tamano: Dp = 62.dp) {
    if (medico != null && medico.fotoRes != 0) {
        Image(
            painter = painterResource(id = medico.fotoRes),
            contentDescription = medico.nombre,
            modifier = Modifier
                .size(tamano)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(tamano)
                .clip(CircleShape)
                .background(Color(0xFFE6E2D6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = VerdePrimario,
                modifier = Modifier.size(tamano * 0.58f)
            )
        }
    }
}

/**
 * Campo del formulario del diseño: cuadro de ícono a la izquierda y, a su derecha,
 * la etiqueta arriba y la caja de texto debajo. Muestra [error] debajo y borde rojo si lo hay.
 */
@Composable
fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onValueChange: (String) -> Unit,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    esPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLargo: Int = 60,
    soloDigitos: Boolean = false
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    val colorBorde = if (error != null) RojoAlerta else BordeSuave

    Column(modifier = modifier.fillMaxWidth()) {
        // La caja de texto pasa por detrás del cuadro del ícono (el ícono queda encima de la tarjeta)
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp)
            ) {
                Text(
                    text = etiqueta,
                    fontSize = Tam.Cuerpo,
                    color = SlateTexto,
                    modifier = Modifier.padding(start = 28.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFFAF8F2), RoundedCornerShape(14.dp))
                        .border(1.dp, colorBorde, RoundedCornerShape(14.dp))
                        .padding(start = 28.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = valor,
                            onValueChange = { nuevo ->
                                val limpio = if (soloDigitos) nuevo.filter { it.isDigit() } else nuevo
                                if (limpio.length <= maxLargo) onValueChange(limpio)
                            },
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF2F4A33),
                                fontWeight = FontWeight.Medium,
                                fontSize = Tam.Subtitulo
                            ),
                            visualTransformation = if (esPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
                            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                            cursorBrush = SolidColor(VerdePrimario),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (esPassword) {
                        IconButton(onClick = { visible = !visible }, modifier = Modifier.size(40.dp)) {
                            Icon(
                                imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = Color(0xFF6F8A6D),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Cuadro del ícono, encima de la tarjeta
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(64.dp),
                shape = RoundedCornerShape(18.dp),
                color = SuperficieSuave,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (error != null) RojoAlerta else BordeSuave)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = etiqueta,
                        tint = if (error != null) RojoAlerta else VerdePrimario,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        if (error != null) {
            Text(
                text = error,
                color = RojoAlerta,
                fontSize = Tam.Pequeno,
                modifier = Modifier.padding(start = 76.dp, top = 4.dp)
            )
        }
    }
}
