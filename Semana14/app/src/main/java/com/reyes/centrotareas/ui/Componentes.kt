package com.reyes.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyes.centrotareas.ui.theme.Amarillo
import com.reyes.centrotareas.ui.theme.Azul
import com.reyes.centrotareas.ui.theme.AzulCielo
import com.reyes.centrotareas.ui.theme.Blanco
import com.reyes.centrotareas.ui.theme.CremaOscuro
import com.reyes.centrotareas.ui.theme.Papel
import com.reyes.centrotareas.ui.theme.Rosa
import com.reyes.centrotareas.ui.theme.Tinta
import com.reyes.centrotareas.ui.theme.TintaSuave

/** Rayas diagonales para las barras. */
fun Modifier.rayado(color: Color): Modifier = drawWithContent {
    drawContent()
    val paso = 12.dp.toPx()
    var x = -size.height
    while (x < size.width) {
        drawLine(color, Offset(x, size.height), Offset(x + size.height, 0f), strokeWidth = 1.5.dp.toPx())
        x += paso
    }
}

/** Encabezado azul con borde ondulado abajo. */
@Composable
fun EncabezadoOndulado(titulo: String, subtitulo: String) {
    Box(Modifier.fillMaxWidth()) {
        Canvas(Modifier.matchParentSize()) {
            val ola = 22.dp.toPx()
            val base = size.height - ola
            val w = size.width
            val forma = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w, base)
                cubicTo(w * 0.8f, base + ola * 1.3f, w * 0.6f, base - ola * 0.7f, w * 0.4f, base + ola * 0.5f)
                cubicTo(w * 0.22f, base + ola * 1.3f, w * 0.1f, base - ola * 0.2f, 0f, base + ola * 0.4f)
                close()
            }
            drawPath(forma, Azul)
            // segunda ola más clara, por detrás del borde
            drawPath(forma, AzulCielo.copy(alpha = 0.35f), style = Stroke(width = 6.dp.toPx()))
        }
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 22.dp, end = 22.dp, top = 6.dp, bottom = 40.dp)
        ) {
            Text(
                text = titulo,
                color = Blanco,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Text(subtitulo, color = Blanco.copy(alpha = 0.85f), fontSize = 14.sp)
        }
    }
}

enum class Animo { Feliz, Ocupado, Triste }

/** Mascota dibujada con Canvas: cambia de cara según lo que pasa en la app. */
@Composable
fun Mascota(animo: Animo, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val grosor = w * 0.035f
        val trazo = Stroke(width = grosor, cap = StrokeCap.Round)
        // antena con bolita
        drawLine(Tinta, Offset(w * 0.5f, h * 0.2f), Offset(w * 0.5f, h * 0.08f), grosor, StrokeCap.Round)
        drawCircle(Amarillo, w * 0.065f, Offset(w * 0.5f, h * 0.08f))
        drawCircle(Tinta, w * 0.065f, Offset(w * 0.5f, h * 0.08f), style = trazo)
        // cuerpo
        val esquina = Offset(w * 0.12f, h * 0.2f)
        val tam = Size(w * 0.76f, h * 0.74f)
        drawRoundRect(Papel, esquina, tam, CornerRadius(w * 0.36f))
        drawRoundRect(Tinta, esquina, tam, CornerRadius(w * 0.36f), style = trazo)
        // mejillas
        drawCircle(Rosa, w * 0.06f, Offset(w * 0.27f, h * 0.64f))
        drawCircle(Rosa, w * 0.06f, Offset(w * 0.73f, h * 0.64f))
        // ojos (miran a un lado cuando está ocupado)
        val dx = if (animo == Animo.Ocupado) w * 0.035f else 0f
        listOf(0.36f, 0.64f).forEach { x ->
            drawOval(Tinta, Offset(w * x - w * 0.04f + dx, h * 0.42f), Size(w * 0.08f, h * 0.13f))
            drawCircle(Blanco, w * 0.014f, Offset(w * x - w * 0.01f + dx, h * 0.45f))
        }
        // boca
        when (animo) {
            Animo.Feliz -> drawArc(
                Tinta, 20f, 140f, false, Offset(w * 0.4f, h * 0.56f), Size(w * 0.2f, h * 0.14f), style = trazo
            )
            Animo.Ocupado -> drawCircle(Tinta, w * 0.035f, Offset(w * 0.5f + dx, h * 0.69f), style = trazo)
            Animo.Triste -> drawArc(
                Tinta, 200f, 140f, false, Offset(w * 0.4f, h * 0.66f), Size(w * 0.2f, h * 0.12f), style = trazo
            )
        }
    }
}

/** Tarjeta con borde de tinta y sombra desplazada, como un cuadro dibujado. */
@Composable
fun Tarjeta(
    titulo: String,
    fondo: Color,
    texto: Color = Tinta,
    etiqueta: String? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(24.dp)
    Box(Modifier.fillMaxWidth().padding(end = 4.dp, bottom = 5.dp)) {
        Box(
            Modifier
                .matchParentSize()
                .offset(4.dp, 5.dp)
                .clip(forma)
                .background(Tinta)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(fondo)
                .border(2.dp, Tinta, forma)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = titulo,
                    color = texto,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (etiqueta != null) {
                    Text(
                        text = etiqueta,
                        color = Tinta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Amarillo)
                            .border(1.5.dp, Tinta, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
            contenido()
        }
    }
}

/** Botón redondeado con borde de tinta. */
@Composable
fun BotonPastilla(
    texto: String,
    onClick: () -> Unit,
    fondo: Color,
    contenido: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borde: Color? = Tinta
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = fondo,
            contentColor = contenido,
            disabledContainerColor = fondo.copy(alpha = fondo.alpha * 0.4f),
            disabledContentColor = contenido.copy(alpha = 0.45f)
        ),
        border = borde?.let { BorderStroke(2.dp, if (enabled) it else it.copy(alpha = 0.3f)) },
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

/** Barra de progreso gruesa con borde y rayas. */
@Composable
fun BarraPastilla(progreso: Float, pista: Color, relleno: Color, rayas: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
            .clip(CircleShape)
            .background(pista)
            .border(2.dp, Tinta, CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progreso.coerceIn(0f, 1f))
                .clip(CircleShape)
                .background(relleno)
                .rayado(rayas)
        )
    }
}

/** Interruptor dentro de un cuadro claro. */
@Composable
fun Interruptor(texto: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Papel)
            .border(1.5.dp, Tinta.copy(alpha = 0.6f), forma)
            .padding(start = 16.dp, end = 8.dp, top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, color = Tinta, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = activo,
            onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blanco,
                checkedTrackColor = Azul,
                checkedBorderColor = Tinta,
                uncheckedThumbColor = TintaSuave,
                uncheckedTrackColor = CremaOscuro,
                uncheckedBorderColor = TintaSuave
            )
        )
    }
}

/** Recuadro de resultado: la primera línea va en negrita si hay varias. */
@Composable
fun Recuadro(
    fondo: Color,
    texto: Color,
    lineas: List<String>
) {
    val forma = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondo)
            .border(1.5.dp, texto.copy(alpha = 0.5f), forma)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        lineas.forEachIndexed { i, linea ->
            Text(
                text = linea,
                color = texto,
                fontSize = if (i == 0 && lineas.size > 1) 16.sp else 14.sp,
                fontWeight = if (i == 0 && lineas.size > 1) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/** Fila tipo lista de pendientes: círculo marcado si ya hay datos. */
@Composable
fun FilaCheck(texto: String, marcado: Boolean) {
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(if (marcado) AzulCielo.copy(alpha = 0.45f) else Papel)
            .border(1.5.dp, Tinta.copy(alpha = 0.6f), forma)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(Modifier.size(20.dp)) {
            drawCircle(Tinta, size.minDimension / 2 - 1.dp.toPx(), style = Stroke(1.5.dp.toPx()))
            if (marcado) {
                val c = Path().apply {
                    moveTo(size.width * 0.28f, size.height * 0.52f)
                    lineTo(size.width * 0.44f, size.height * 0.68f)
                    lineTo(size.width * 0.74f, size.height * 0.34f)
                }
                drawPath(c, Tinta, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(texto, color = Tinta, fontSize = 13.sp)
    }
}

/** Barra vertical para comparar tiempos (alto proporcional al máximo). */
@Composable
fun BarraVertical(
    etiqueta: String,
    valorMs: Long?,
    maximoMs: Long,
    color: Color,
    rayas: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (valorMs == null) "—" else "$valorMs ms",
            color = Tinta,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        val forma = RoundedCornerShape(20.dp)
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(140.dp)
                .clip(forma)
                .background(CremaOscuro)
                .border(2.dp, Tinta, forma),
            contentAlignment = Alignment.BottomCenter
        ) {
            val fraccion = if (valorMs == null || maximoMs == 0L) 0f else valorMs.toFloat() / maximoMs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraccion)
                    .clip(forma)
                    .background(color)
                    .rayado(rayas)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(etiqueta, color = TintaSuave, fontSize = 13.sp)
    }
}

/** Barra inferior oscura: estado a los lados y el botón Tócame al centro. */
@Composable
fun BarraInferior(toques: Int, enCurso: Int, listas: Int, onTocar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Tinta)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Dato("En curso", enCurso, Modifier.weight(1f), Alignment.Start)
        BotonPastilla(
            texto = "Tócame: $toques",
            onClick = onTocar,
            fondo = AzulCielo,
            contenido = Tinta,
            borde = Blanco
        )
        Dato("Listas", listas, Modifier.weight(1f), Alignment.End)
    }
}

@Composable
private fun Dato(texto: String, numero: Int, modifier: Modifier, alineacion: Alignment.Horizontal) {
    Column(modifier = modifier, horizontalAlignment = alineacion) {
        Text("$numero", color = Blanco, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(texto, color = Blanco.copy(alpha = 0.6f), fontSize = 12.sp)
    }
}
