package com.reyes.clinicasaludplus.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.components.BotonPrimario
import com.reyes.clinicasaludplus.ui.components.CampoFormulario
import com.reyes.clinicasaludplus.ui.theme.AzulPrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.util.Validaciones

@Composable
fun RegistroScreen(
    alRegistrarExitoso: () -> Unit,
    alIrALogin: () -> Unit
) {
    val context = LocalContext.current
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var intentoEnviar by rememberSaveable { mutableStateOf(false) }
    var mostrarTerminos by rememberSaveable { mutableStateOf(false) }

    // Errores: de formato y de duplicados (solo se muestran después del primer intento)
    val errorNombre = Validaciones.errorNombre(nombre)
    val errorTelefono = Validaciones.errorTelefono(telefono)
        ?: if (Repositorio.telefonoRegistrado(telefono)) "Este teléfono ya está registrado" else null
    val errorCorreo = Validaciones.errorCorreoOpcional(correo)
        ?: if (Repositorio.correoRegistrado(correo)) "Este correo ya está registrado" else null
    val errorContrasena = Validaciones.errorContrasena(contrasena)
    val hayErrores = listOf(errorNombre, errorTelefono, errorCorreo, errorContrasena).any { it != null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Crear cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NavyTitulo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Regístrate para agendar tus citas",
            fontSize = 17.sp,
            color = SlateTexto
        )

        Spacer(modifier = Modifier.height(34.dp))

        CampoFormulario(
            etiqueta = "Nombre completo",
            valor = nombre,
            onValueChange = { nombre = it },
            icono = Icons.Default.Person,
            error = if (intentoEnviar) errorNombre else null,
            maxLargo = Validaciones.MAX_NOMBRE
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoFormulario(
            etiqueta = "Teléfono",
            valor = telefono,
            onValueChange = { telefono = it },
            icono = Icons.Default.Phone,
            error = if (intentoEnviar) errorTelefono else null,
            keyboardType = KeyboardType.Phone,
            maxLargo = Validaciones.LARGO_TELEFONO,
            soloDigitos = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoFormulario(
            etiqueta = "Correo (opcional)",
            valor = correo,
            onValueChange = { correo = it.replace(" ", "") },
            icono = Icons.Default.Email,
            error = if (intentoEnviar) errorCorreo else null,
            keyboardType = KeyboardType.Email,
            maxLargo = 60
        )

        Spacer(modifier = Modifier.height(14.dp))

        CampoFormulario(
            etiqueta = "Contraseña",
            valor = contrasena,
            onValueChange = { contrasena = it.replace(" ", "") },
            icono = Icons.Default.Lock,
            error = if (intentoEnviar) errorContrasena else null,
            esPassword = true,
            keyboardType = KeyboardType.Password,
            maxLargo = Validaciones.MAX_CONTRASENA
        )

        Spacer(modifier = Modifier.height(28.dp))

        BotonPrimario(
            texto = "Registrarme",
            onClick = {
                intentoEnviar = true
                if (hayErrores) {
                    Toast.makeText(context, "Revisa los campos marcados en rojo", Toast.LENGTH_SHORT).show()
                } else {
                    val exito = Repositorio.registrarUsuario(
                        nombre = nombre,
                        telefono = telefono,
                        correo = correo,
                        contrasena = contrasena
                    )
                    if (exito) {
                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        alRegistrarExitoso()
                    } else {
                        Toast.makeText(context, "No se pudo completar el registro", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Al registrarte aceptas nuestros",
            fontSize = 16.sp,
            color = SlateTexto,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Términos y Condiciones",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = AzulPrimario,
            modifier = Modifier
                .clickable { mostrarTerminos = true }
                .padding(vertical = 2.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        HorizontalDivider(color = BordeSuave)

        TextButton(
            onClick = alIrALogin,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFF111827))) { append("¿Ya tienes cuenta? ") }
                    withStyle(SpanStyle(color = AzulPrimario, fontWeight = FontWeight.SemiBold)) { append("Iniciar sesión") }
                },
                fontSize = 17.sp
            )
        }
    }

    if (mostrarTerminos) {
        AlertDialog(
            onDismissRequest = { mostrarTerminos = false },
            title = { Text("Términos y Condiciones", fontWeight = FontWeight.Bold, color = NavyTitulo) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        "1. Uso de la aplicación: Clínica SaludPlus te permite agendar y administrar tus citas médicas.\n\n" +
                            "2. Datos personales: tus datos se usan únicamente para gestionar tus citas y no se comparten con terceros.\n\n" +
                            "3. Citas: debes asistir puntualmente. Puedes cancelar una cita desde \"Mis citas\" para liberar el horario.\n\n" +
                            "4. Responsabilidad: la información ingresada debe ser veraz y estar actualizada.",
                        fontSize = 14.sp,
                        color = SlateTexto
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarTerminos = false }) {
                    Text("Entendido", color = AzulPrimario, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Blanco,
            shape = RoundedCornerShape(14.dp)
        )
    }
}
