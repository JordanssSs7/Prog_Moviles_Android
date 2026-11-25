package com.reyes.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.murocampus.model.Post
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Fecha de hoy en español, por ejemplo "Martes, 25 de noviembre". */
fun fechaDeHoy(): String {
    val texto = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "PE")).format(Date())
    return texto.replaceFirstChar { it.uppercase() }
}

/** Encabezado grande: fecha pequeña arriba y título en negrita (estilo lector de noticias). */
@Composable
fun MuroTopBar(
    titulo: String = "MuroCampus",
    subtitulo: String = fechaDeHoy(),
    onVolver: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 4.dp)
    ) {
        if (onVolver != null) {
            TextButton(
                onClick = onVolver,
                modifier = Modifier.padding(start = 0.dp)
            ) {
                Text("‹  Volver", color = MaterialTheme.colorScheme.tertiary, fontSize = 15.sp)
            }
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }
        Text(
            text = subtitulo.uppercase(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            letterSpacing = 0.8.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = titulo,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 36.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private val paletas = listOf(
    listOf(Color(0xFF1F2A44), Color(0xFF3E5C8A)),
    listOf(Color(0xFF8E2433), Color(0xFFE5484D)),
    listOf(Color(0xFF0F5257), Color(0xFF2A9D8F)),
    listOf(Color(0xFF3D2C5C), Color(0xFF7B61C9)),
    listOf(Color(0xFF6B4423), Color(0xFFC98A4B))
)

/** Miniatura cuadrada con la inicial del título (no hay imágenes en el servicio). */
@Composable
fun Miniatura(post: Post, tamano: Int = 76) {
    val colores = paletas[(post.userId - 1).mod(paletas.size)]
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(tamano.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.linearGradient(colores))
    ) {
        Text(
            text = post.title.firstOrNull()?.uppercase() ?: "M",
            color = Color.White,
            fontSize = (tamano / 2.4).sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}

/** Etiqueta tipo "fuente" en mayúsculas y serif, como el nombre del medio en la referencia. */
@Composable
fun EtiquetaUsuario(userId: Int) {
    Text(
        text = "Usuario $userId".uppercase(),
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun TarjetaPost(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                EtiquetaUsuario(post.userId)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = post.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.body,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Publicación #${post.id}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Miniatura(post)
        }
    }
}

/** Barra inferior flotante con la pestaña activa resaltada, como Today / For You / For Later. */
@Composable
fun BarraInferior(
    seleccion: Int,
    onMuro: () -> Unit,
    onPublicar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(6.dp)
        ) {
            ItemBarra("▤", "Muro", seleccion == 0, onMuro)
            ItemBarra("+", "Publicar", seleccion == 1, onPublicar)
        }
    }
}

@Composable
private fun ItemBarra(icono: String, texto: String, activo: Boolean, onClick: () -> Unit) {
    val fondo = if (activo) MaterialTheme.colorScheme.tertiary else Color.Transparent
    val color = if (activo) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(fondo)
            .clickable { onClick() }
            .padding(horizontal = 22.dp, vertical = 6.dp)
    ) {
        Text(icono, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(texto, color = color, fontSize = 11.sp)
    }
}

@Composable
fun Cargando(
    mensaje: String = "Cargando publicaciones...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("!", color = MaterialTheme.colorScheme.error, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(onClick = onReintentar) {
                    Text(text = "Reintentar")
                }
            }
        }
    }
}
