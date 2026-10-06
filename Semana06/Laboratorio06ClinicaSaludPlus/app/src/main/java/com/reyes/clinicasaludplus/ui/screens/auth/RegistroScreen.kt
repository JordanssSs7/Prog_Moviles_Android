package com.reyes.clinicasaludplus.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.theme.*

@Composable
fun RegistroScreen(
    alRegistrarExitoso: () -> Unit,
    alIrALogin: () -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Encabezado
        Text(
            text = "Crear cuenta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Regístrate para agendar tus citas",
            fontSize = 14.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo 1: Nombre completo
        CampoRegistroPersonalizado(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValueChange = { nombre = it },
            icono = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo 2: Teléfono
        CampoRegistroPersonalizado(
            etiqueta = "Teléfono",
            valor = telefono,
            onValueChange = { telefono = it },
            icono = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo 3: Correo (opcional)
        CampoRegistroPersonalizado(
            etiqueta = "Correo (opcional)",
            valor = correo,
            onValueChange = { correo = it },
            icono = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo 4: Contraseña
        CampoRegistroPersonalizado(
            etiqueta = "Contraseña",
            valor = contrasena,
            onValueChange = { contrasena = it },
            icono = Icons.Default.Lock,
            esPassword = true,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Registrarme
        BotonPrimario(
            texto = "Registrarme",
            onClick = {
                if (nombre.isBlank() || telefono.isBlank() || contrasena.isBlank()) {
                    Toast.makeText(context, "Por favor completa los campos requeridos", Toast.LENGTH_SHORT).show()
                } else {
                    val exito = Repositorio.registrarUsuario(
                        nombre = nombre,
                        telefono = telefono,
                        correo = if (correo.isBlank()) "$telefono@saludplus.com" else correo,
                        contrasena = contrasena
                    )
                    if (exito) {
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        alRegistrarExitoso()
                    } else {
                        Toast.makeText(context, "El usuario ya está registrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Términos y condiciones
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Al registrarte aceptas nuestros",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = "Términos y Condiciones",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AzulPrimario
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Iniciar sesión
        TextButton(onClick = alIrALogin) {
            Row {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Iniciar sesión",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
            }
        }
    }
}

@Composable
fun CampoRegistroPersonalizado(
    etiqueta: String,
    valor: String,
    onValueChange: (String) -> Unit,
    icono: ImageVector,
    esPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cuadro independiente del icono con sombra suave
        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(12.dp),
            color = Blanco,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = etiqueta,
                    tint = AzulPrimario,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Caja de entrada con etiqueta superior integrada
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .background(Blanco, RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = etiqueta,
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 12.sp
                )
                BasicTextField(
                    value = valor,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    cursorBrush = SolidColor(AzulPrimario),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}