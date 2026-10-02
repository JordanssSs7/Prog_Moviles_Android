package com.reyes.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    var isLoggedIn by remember { mutableStateOf(true) }
    var currentUserName by remember { mutableStateOf("Jordan Reyes") }
    var currentUserEmail by remember { mutableStateOf("jordan.reyes@tecsup.edu.pe") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentRoute by remember { mutableStateOf(DestinoDrawer.INICIO) }

    val productos = remember {
        mutableStateListOf(
            Producto(1, "Audifonos", "S/ 89.00", isFavorite = false),
            Producto(2, "Smartwatch", "S/ 199.00", isFavorite = false),
            Producto(3, "Funda celular", "S/ 25.00", isFavorite = false)
        )
    }

    val pedidos = remember {
        mutableStateListOf(
            Pedido("PED-001", "Audifonos", "S/ 89.00", "Entregado")
        )
    }

    val totalFavoritos = productos.count { it.isFavorite }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { name, email ->
                currentUserName = name
                currentUserEmail = email
                isLoggedIn = true
                currentRoute = DestinoDrawer.INICIO
            }
        )
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                userName = currentUserName,
                userEmail = currentUserEmail,
                currentRoute = currentRoute,
                badgeFavoritosCount = totalFavoritos,
                badgePedidosCount = pedidos.size,
                onNavigateTo = { destino ->
                    if (destino == DestinoDrawer.CERRAR_SESION) {
                        isLoggedIn = false
                        currentRoute = DestinoDrawer.INICIO
                    } else {
                        currentRoute = destino
                    }
                },
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5E2E8C))
                        .statusBarsPadding()
                        .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column(modifier = Modifier.padding(top = 6.dp)) {
                        Text(
                            text = "TECSUP Store",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (currentRoute) {
                                DestinoDrawer.INICIO -> "Mas vendidos"
                                DestinoDrawer.MIS_PEDIDOS -> "Mis pedidos realizados (${pedidos.size})"
                                DestinoDrawer.FAVORITOS -> "Artículos guardados ($totalFavoritos)"
                                DestinoDrawer.PERFIL -> "Mi cuenta de estudiante"
                                DestinoDrawer.CERRAR_SESION -> "Sesión"
                            },
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            },
            containerColor = Color.White
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentRoute) {
                    DestinoDrawer.INICIO -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 12.dp)
                        ) {
                            itemsIndexed(productos) { index, producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    isHighlighted = (index == 0),
                                    onToggleFavorito = {
                                        productos[index] = producto.copy(isFavorite = !producto.isFavorite)
                                    },
                                    onComprarClick = {
                                        pedidos.add(
                                            Pedido(
                                                id = "PED-00${pedidos.size + 1}",
                                                productoNombre = producto.nombre,
                                                total = producto.precio,
                                                estado = "En preparación"
                                            )
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("¡${producto.nombre} agregado a Mis Pedidos!")
                                        }
                                    },
                                    onReportarConfirmado = { nombreProd ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Reporte enviado para: $nombreProd")
                                        }
                                    }
                                )
                            }
                        }
                    }
                    DestinoDrawer.FAVORITOS -> {
                        val favoritosList = productos.filter { it.isFavorite }
                        if (favoritosList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No tienes productos en favoritos todavía",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 12.dp)
                            ) {
                                itemsIndexed(favoritosList) { _, producto ->
                                    val idx = productos.indexOfFirst { it.id == producto.id }
                                    TarjetaProducto(
                                        producto = producto,
                                        isHighlighted = false,
                                        onToggleFavorito = {
                                            if (idx != -1) {
                                                productos[idx] = producto.copy(isFavorite = !producto.isFavorite)
                                            }
                                        },
                                        onComprarClick = {
                                            pedidos.add(
                                                Pedido(
                                                    id = "PED-00${pedidos.size + 1}",
                                                    productoNombre = producto.nombre,
                                                    total = producto.precio,
                                                    estado = "En preparación"
                                                )
                                            )
                                            scope.launch {
                                                snackbarHostState.showSnackbar("¡${producto.nombre} agregado a Mis Pedidos!")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    DestinoDrawer.MIS_PEDIDOS -> {
                        MisPedidosScreen(pedidos = pedidos)
                    }
                    DestinoDrawer.PERFIL -> {
                        PerfilScreen(
                            nombre = currentUserName,
                            email = currentUserEmail,
                            totalFavoritos = totalFavoritos,
                            totalPedidos = pedidos.size,
                            onCerrarSesion = {
                                isLoggedIn = false
                                currentRoute = DestinoDrawer.INICIO
                            }
                        )
                    }
                    DestinoDrawer.CERRAR_SESION -> {}
                }
            }
        }
    }
}