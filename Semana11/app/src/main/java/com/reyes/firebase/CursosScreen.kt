package com.reyes.firebase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Curso(val id: String = "", val nombre: String = "", val descripcion: String = "")

@Composable
fun CursosScreen(onBack: () -> Unit, abrirNuevo: Boolean = false) {
    val db = remember { FirebaseFirestore.getInstance() }
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var cursos by remember { mutableStateOf(listOf<Curso>()) }
    var error by remember { mutableStateOf<String?>(null) }
    var editing by remember { mutableStateOf<Curso?>(null) }
    var showDialog by remember { mutableStateOf(abrirNuevo) }

    // READ: solo los cursos del usuario autenticado, en tiempo real
    DisposableEffect(uid) {
        val reg = db.collection("cursos").whereEqualTo("userId", uid)
            .addSnapshotListener { snap, e ->
                if (e != null) { error = e.message; return@addSnapshotListener }
                cursos = snap?.documents?.map {
                    Curso(it.id, it.getString("nombre") ?: "", it.getString("descripcion") ?: "")
                }?.sortedBy { it.nombre.lowercase() } ?: emptyList()
            }
        onDispose { reg.remove() }
    }

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editing = null; showDialog = true },
                containerColor = Vino, contentColor = Color.White
            ) { Text("+ Nuevo curso", fontWeight = FontWeight.Bold) }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(horizontal = 20.dp)) {
            TextButton(onClick = onBack) { Text("‹ Atrás", color = Marino) }
            Text("Mis cursos", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Marino)
            Text("${cursos.size} registrados", fontSize = 13.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (cursos.isEmpty()) Text("Aún no tienes cursos. Agrega uno con + Nuevo curso.", color = Color.Gray)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 96.dp)) {
                itemsIndexed(cursos, key = { _, c -> c.id }) { i, c ->
                    Column(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                            .background(if (i % 2 == 0) Celeste else Naranja).padding(16.dp)
                    ) {
                        Text(c.nombre, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Marino)
                        if (c.descripcion.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(c.descripcion, fontSize = 13.sp, color = Marino)
                        }
                        Row {
                            TextButton(onClick = { editing = c; showDialog = true }) { Text("Editar", color = Marino) }
                            // DELETE
                            TextButton(onClick = { db.collection("cursos").document(c.id).delete() }) {
                                Text("Eliminar", color = Vino, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        var nombre by remember(editing) { mutableStateOf(editing?.nombre ?: "") }
        var desc by remember(editing) { mutableStateOf(editing?.descripcion ?: "") }
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = { Text(if (editing == null) "Nuevo curso" else "Editar curso", fontWeight = FontWeight.Bold, color = Marino) },
            text = {
                Column {
                    OutlinedTextField(nombre, { nombre = it }, placeholder = { Text("Nombre") }, singleLine = true,
                        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(desc, { desc = it }, placeholder = { Text("Descripción") },
                        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nombre.isBlank()) return@Button
                        val data = mapOf("userId" to uid, "nombre" to nombre.trim(), "descripcion" to desc.trim())
                        val col = db.collection("cursos")
                        val task = editing?.let { col.document(it.id).update(data) } ?: col.add(data)
                        task.addOnFailureListener { error = it.message }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Vino, contentColor = Color.White)
                ) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = Marino) } }
        )
    }
}
