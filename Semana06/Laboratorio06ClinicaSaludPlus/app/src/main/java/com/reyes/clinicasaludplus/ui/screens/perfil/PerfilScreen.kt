package com.reyes.clinicasaludplus.ui.screens.perfil

import com.reyes.clinicasaludplus.ui.theme.Tam
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.theme.SuperficieSuave
import com.reyes.clinicasaludplus.ui.theme.VerdeClaro
import com.reyes.clinicasaludplus.util.FechaUtils
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import com.reyes.clinicasaludplus.ui.components.BarraSuperiorConVolver
import com.reyes.clinicasaludplus.ui.components.DialogoConfirmacion
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto

@Composable
fun PerfilScreen(
    alCerrarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    var confirmarSalida by rememberSaveable { mutableStateOf(false) }
    val citas = Repositorio.citasDelUsuario()
    val totalCitas = citas.size
    val textoProxima = citas.firstNotNullOfOrNull { cita ->
        FechaUtils.parsearFecha(cita.fecha)?.takeIf { !it.isBefore(LocalDate.now()) }?.let { fecha ->
            val mes = fecha.month.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-PE")).replace(".", "")
            "${fecha.dayOfMonth} $mes"
        }
    } ?: "Sin citas"

    val nombre = usuario?.nombreCompleto ?: "Paciente"
    val iniciales = nombre.trim().split(" ").filter { it.isNotBlank() }
        .take(2).joinToString("") { it.first().uppercase() }
        .ifEmpty { "P" }
    val correo = usuario?.correo?.ifBlank { null }

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperiorConVolver(
                titulo = "Mis datos",
                alVolver = alVolver
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Encabezado con degradado: avatar con iniciales, nombre y contacto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF2F4A33), Color(0xFF6F8A6D))
                        )
                    )
                    .drawBehind {
                        drawCircle(Blanco.copy(alpha = 0.08f), radius = size.width * 0.35f, center = Offset(size.width * 0.95f, size.height * 0.05f))
                        drawCircle(Blanco.copy(alpha = 0.06f), radius = size.width * 0.28f, center = Offset(size.width * 0.02f, size.height * 0.98f))
                    }
                    .padding(vertical = 28.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(92.dp),
                        shape = CircleShape,
                        color = Blanco,
                        shadowElevation = 4.dp,
                        border = BorderStroke(4.dp, Blanco.copy(alpha = 0.35f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = iniciales,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = VerdePrimario
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = nombre,
                        fontSize = Tam.Titulo,
                        fontWeight = FontWeight.Bold,
                        color = Blanco
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = correo ?: usuario?.telefono ?: "Sesión no iniciada",
                        fontSize = Tam.Cuerpo,
                        color = Blanco.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de citas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TarjetaEstadistica(
                    icono = Icons.Default.CalendarMonth,
                    valor = totalCitas.toString(),
                    etiqueta = if (totalCitas == 1) "Cita agendada" else "Citas agendadas",
                    modifier = Modifier.weight(1f)
                )
                TarjetaEstadistica(
                    icono = Icons.Default.Event,
                    valor = textoProxima,
                    etiqueta = "Próxima cita",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Información personal",
                fontSize = Tam.Subtitulo,
                fontWeight = FontWeight.Bold,
                color = NavyTitulo,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Blanco,
                shadowElevation = 3.dp,
                border = BorderStroke(1.dp, BordeSuave)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    FilaDatoPerfil(Icons.Default.Person, "Nombre completo", nombre)
                    HorizontalDivider(color = BordeSuave)
                    FilaDatoPerfil(Icons.Default.Phone, "Teléfono de contacto", usuario?.telefono ?: "-")
                    HorizontalDivider(color = BordeSuave)
                    FilaDatoPerfil(Icons.Default.Email, "Correo registrado", correo ?: "No registrado")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { confirmarSalida = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = RojoAlerta.copy(alpha = 0.06f),
                    contentColor = RojoAlerta
                ),
                border = BorderStroke(1.dp, RojoAlerta.copy(alpha = 0.45f))
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cerrar sesión", fontSize = Tam.Cuerpo, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    DialogoConfirmacion(
        mostrar = confirmarSalida,
        titulo = "¿Cerrar sesión?",
        mensaje = "Tendrás que iniciar sesión nuevamente para ver y agendar tus citas.",
        onConfirmar = {
            confirmarSalida = false
            Repositorio.cerrarSesion()
            alCerrarSesion()
        },
        onDescartar = { confirmarSalida = false }
    )
}

@Composable
fun FilaDatoPerfil(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFE6E2D6)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icono, contentDescription = null, tint = VerdePrimario, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(etiqueta, fontSize = Tam.Pequeno, color = SlateTexto)
            Spacer(modifier = Modifier.height(2.dp))
            Text(valor, fontSize = Tam.Cuerpo, fontWeight = FontWeight.Medium, color = NavyTitulo)
        }
    }
}

@Composable
private fun TarjetaEstadistica(
    icono: ImageVector,
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = SuperficieSuave
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = VerdeClaro, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, contentDescription = null, tint = VerdePrimario, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(valor, fontSize = Tam.Titulo, fontWeight = FontWeight.ExtraBold, color = NavyTitulo)
            Text(etiqueta, fontSize = Tam.Pequeno, color = SlateTexto)
        }
    }
}
