package com.reyes.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String
)

@Composable
fun TarjetaProducto(
    producto: Producto,
    isHighlighted: Boolean = false
) {
    var menuExpanded by remember { mutableStateOf(false) }
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
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E1E24))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = producto.precio, fontSize = 14.sp, color = Color.Gray)
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.DarkGray)
                }
            }
        }
    }
}