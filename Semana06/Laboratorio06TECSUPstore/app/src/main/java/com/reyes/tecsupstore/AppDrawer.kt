package com.reyes.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DestinoDrawer(val titulo: String) {
    INICIO("Inicio"),
    MIS_PEDIDOS("Mis pedidos"),
    FAVORITOS("Favoritos"),
    PERFIL("Perfil"),
    CERRAR_SESION("Cerrar sesion")
}

@Composable
fun AppDrawerContent(
    userName: String,
    userEmail: String,
    currentRoute: DestinoDrawer,
    badgeFavoritosCount: Int,
    badgePedidosCount: Int,
    onNavigateTo: (DestinoDrawer) -> Unit,
    closeDrawer: () -> Unit
) {
    val purpleBrand = Color(0xFF5E2E8C)
    val lightPurpleActive = Color(0xFFF3EAFB)
    val circleBorderColor = Color(0xFF2C2C34)

    val iniciales = userName.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .ifEmpty { "JR" }

    ModalDrawerSheet(
        modifier = Modifier.width(310.dp),
        drawerContainerColor = Color.White
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE9DCF8)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = purpleBrand
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color(0xFF1E1E24)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = userEmail,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            color = Color(0xFFF2F2F2),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        DestinoDrawer.values().forEach { destino ->
            val isSelected = destino == currentRoute

            NavigationDrawerItem(
                label = {
                    Text(
                        text = destino.titulo,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) purpleBrand else Color(0xFF333333),
                        fontSize = 15.sp
                    )
                },
                selected = isSelected,
                onClick = {
                    onNavigateTo(destino)
                    closeDrawer()
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .border(
                                width = 1.8.dp,
                                color = if (isSelected) purpleBrand else circleBorderColor,
                                shape = CircleShape
                            )
                    )
                },
                badge = {
                    if (destino == DestinoDrawer.FAVORITOS && badgeFavoritosCount > 0) {
                        Badge(containerColor = purpleBrand, contentColor = Color.White) {
                            Text(text = badgeFavoritosCount.toString(), fontWeight = FontWeight.Bold)
                        }
                    } else if (destino == DestinoDrawer.MIS_PEDIDOS && badgePedidosCount > 0) {
                        Badge(containerColor = Color(0xFF4CAF50), contentColor = Color.White) {
                            Text(text = badgePedidosCount.toString(), fontWeight = FontWeight.Bold)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = lightPurpleActive,
                    unselectedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
                    .height(52.dp)
            ) // -
        }
    }
}