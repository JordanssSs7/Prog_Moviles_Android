package com.reyes.firebase

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(onCursos: (Boolean) -> Unit, onLogout: () -> Unit) {
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid ?: ""
    val nombre = user?.displayName?.takeIf { it.isNotBlank() } ?: user?.email?.substringBefore("@") ?: ""
    val correo = user?.email ?: ""

    var total by remember { mutableIntStateOf(0) }
    var conDetalle by remember { mutableIntStateOf(0) }

    DisposableEffect(uid) {
        val reg = FirebaseFirestore.getInstance().collection("cursos").whereEqualTo("userId", uid)
            .addSnapshotListener { snap, _ ->
                val docs = snap?.documents ?: emptyList()
                total = docs.size
                conDetalle = docs.count { !it.getString("descripcion").isNullOrBlank() }
            }
        onDispose { reg.remove() }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Cabecera con bandas diagonales y avatar
        Box(Modifier.fillMaxWidth().height(220.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val celeste = Path().apply {
                    moveTo(0f, 0.62f * h); lineTo(0.62f * w, 0.30f * h)
                    lineTo(0.62f * w, 0.62f * h); lineTo(0f, 0.94f * h); close()
                }
                drawPath(celeste, Celeste)
                val vino = Path().apply {
                    moveTo(0.05f * w, 0.52f * h); lineTo(w, 0.10f * h)
                    lineTo(w, 0.50f * h); lineTo(0.05f * w, 0.92f * h); close()
                }
                drawPath(vino, Vino)
            }
            Box(
                Modifier.align(Alignment.Center).size(104.dp).clip(CircleShape).background(Color.White).padding(5.dp)
            ) {
                Box(Modifier.fillMaxSize().clip(CircleShape).background(Marino), contentAlignment = Alignment.Center) {
                    Text(
                        nombre.firstOrNull()?.uppercase() ?: "?",
                        color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(Modifier.padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(nombre, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Marino, textAlign = TextAlign.Center)
            Text(correo, fontSize = 13.sp, color = Color.Gray)
            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Dato("Cursos", total)
                Dato("Con detalle", conDetalle)
                Dato("Sin detalle", total - conDetalle)
            }
            Spacer(Modifier.height(28.dp))

            Text("Mi espacio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Marino,
                modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                TarjetaAccion("🎓", "Mis cursos", "$total registrados", Celeste, Modifier.weight(1f)) { onCursos(false) }
                TarjetaAccion("➕", "Nuevo curso", "Agregar ahora", Naranja, Modifier.weight(1f)) { onCursos(true) }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Cerrar sesión",
                color = Vino, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = onLogout).padding(12.dp)
            )
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(etiqueta, fontSize = 12.sp, color = Color.Gray)
        Text(valor.toString(), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Marino)
    }
}

@Composable
private fun TarjetaAccion(icono: String, titulo: String, detalle: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier.clip(RoundedCornerShape(18.dp)).background(color).clickable(onClick = onClick).padding(16.dp)
    ) {
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color.White), contentAlignment = Alignment.Center) {
            Text(icono, fontSize = 18.sp)
        }
        Spacer(Modifier.height(14.dp))
        Text(titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Marino)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(detalle, fontSize = 12.sp, color = Marino, modifier = Modifier.weight(1f))
            Text("→", fontSize = 16.sp, color = Marino)
        }
    }
}
