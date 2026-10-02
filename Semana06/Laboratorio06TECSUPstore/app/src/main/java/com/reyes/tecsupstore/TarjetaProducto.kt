package com.reyes.tecsupstore

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String,
    val isFavorite: Boolean = false
)

@Composable
fun TarjetaProducto(
    producto: Producto,
    isHighlighted: Boolean = false,
    onToggleFavorito: () -> Unit = {},
    onComprarClick: () -> Unit = {},
    onReportarConfirmado: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }
    var mostrarDialogoReporte by remember { mutableStateOf(false) }

    val purpleBrand = Color(0xFF5E2E8C)
    val lightPurpleBg = Color(0xFFF3EAFB)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .then(
                if (isHighlighted) Modifier.border(2.dp, purpleBrand, RoundedCornerShape(16.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isHighlighted) Color.White else Color(0xFFF7F7FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(lightPurpleBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = purpleBrand,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E1E24)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.precio,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onComprarClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Comprar",
                    tint = purpleBrand
                )
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = Color.DarkGray
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = 0.dp, y = 4.dp),
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (producto.isFavorite) "Quitar de favoritos" else "Favoritos",
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (producto.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (producto.isFavorite) Color.Red else Color.DarkGray,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onToggleFavorito()
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.8.dp)
                    DropdownMenuItem(
                        text = { Text("Compartir", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "¡Mira este producto en TECSUP Store!: ${producto.nombre} a solo ${producto.precio}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Compartir producto")
                            context.startActivity(shareIntent)
                        }
                    )
                    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.8.dp)
                    DropdownMenuItem(
                        text = { Text("Reportar", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            mostrarDialogoReporte = true
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoReporte) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoReporte = false },
            title = { Text("Reportar producto", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas reportar '${producto.nombre}' por contenido incorrecto o falta de stock?") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoReporte = false
                        onReportarConfirmado(producto.nombre)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = purpleBrand)
                ) {
                    Text("Confirmar reporte")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoReporte = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}