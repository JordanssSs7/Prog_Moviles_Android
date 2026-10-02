package com.reyes.tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class DestinoDrawer(val titulo: String) {
    INICIO("Inicio"),
    MIS_PEDIDOS("Mis pedidos"),
    FAVORITOS("Favoritos"),
    PERFIL("Perfil"),
    CERRAR_SESION("Cerrar sesion")
}

@Composable
fun AppDrawerContent(
    currentRoute: DestinoDrawer,
    onNavigateTo: (DestinoDrawer) -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(310.dp),
        drawerContainerColor = Color.White
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        DestinoDrawer.values().forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                selected = (destino == currentRoute),
                onClick = {
                    onNavigateTo(destino)
                    closeDrawer()
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}