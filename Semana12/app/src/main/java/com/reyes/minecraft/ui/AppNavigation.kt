package com.reyes.minecraft.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reyes.minecraft.AppContainer
import com.reyes.minecraft.model.Rol
import com.reyes.minecraft.model.UiState
import com.reyes.minecraft.model.Usuario
import com.reyes.minecraft.ui.components.PantallaCargando
import com.reyes.minecraft.ui.screens.AdminScreen
import com.reyes.minecraft.ui.screens.DetalleScreen
import com.reyes.minecraft.ui.screens.FavoritosScreen
import com.reyes.minecraft.ui.screens.ListaScreen
import com.reyes.minecraft.ui.screens.LoginScreen
import com.reyes.minecraft.ui.viewmodel.AdminViewModel
import com.reyes.minecraft.ui.viewmodel.DetalleViewModel
import com.reyes.minecraft.ui.viewmodel.FavoritosViewModel
import com.reyes.minecraft.ui.viewmodel.ListaViewModel
import com.reyes.minecraft.ui.viewmodel.SesionViewModel

private object Rutas {
    const val LISTA = "lista"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
}

private class OpcionMenu(val ruta: String, val titulo: String, val icono: ImageVector)

/** Decide qué se ve según la sesión: carga, login o la app principal. */
@Composable
fun MinecraftUi(contenedor: AppContainer) {
    val sesionVm: SesionViewModel = viewModel(
        factory = viewModelFactory { initializer { SesionViewModel(contenedor.authRepository) } }
    )
    val estado by sesionVm.estado.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> PantallaCargando()
        is UiState.Error -> LoginScreen(sesionVm)
        is UiState.Exito -> {
            val usuario = e.datos
            if (usuario == null) LoginScreen(sesionVm)
            else Principal(usuario, contenedor, onCerrarSesion = sesionVm::cerrarSesion)
        }
    }
}

@Composable
private fun Principal(usuario: Usuario, contenedor: AppContainer, onCerrarSesion: () -> Unit) {
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    // El rol decide qué pantallas existen: el usuario normal ni siquiera tiene la ruta "admin".
    val opciones = buildList {
        add(OpcionMenu(Rutas.LISTA, "Catálogo", Icons.Default.Home))
        add(OpcionMenu(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite))
        if (usuario.rol == Rol.ADMIN) add(OpcionMenu(Rutas.ADMIN, "Admin", Icons.Default.Settings))
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (opciones.any { it.ruta == rutaActual }) {
                NavigationBar {
                    opciones.forEach { opcion ->
                        NavigationBarItem(
                            selected = rutaActual == opcion.ruta,
                            onClick = {
                                nav.navigate(opcion.ruta) {
                                    popUpTo(Rutas.LISTA) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(opcion.icono, contentDescription = null) },
                            label = { Text(opcion.titulo) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Rutas.LISTA, modifier = Modifier.padding(padding)) {
            composable(Rutas.LISTA) {
                val vm: ListaViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer { ListaViewModel(contenedor.itemRepository, contenedor.notificador) }
                    }
                )
                ListaScreen(vm, onItemClick = { nav.navigate(Rutas.detalle(it)) }, onCerrarSesion = onCerrarSesion)
            }
            composable(Rutas.FAVORITOS) {
                val vm: FavoritosViewModel = viewModel(
                    factory = viewModelFactory { initializer { FavoritosViewModel(contenedor.itemRepository) } }
                )
                FavoritosScreen(vm, onItemClick = { nav.navigate(Rutas.detalle(it)) }, onCerrarSesion = onCerrarSesion)
            }
            composable(
                Rutas.DETALLE,
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                val vm: DetalleViewModel = viewModel(
                    factory = viewModelFactory { initializer { DetalleViewModel(id, contenedor.itemRepository) } }
                )
                DetalleScreen(vm, onAtras = { nav.popBackStack() })
            }
            if (usuario.rol == Rol.ADMIN) {
                composable(Rutas.ADMIN) {
                    val vm: AdminViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer { AdminViewModel(contenedor.itemRepository, contenedor.notificador) }
                        }
                    )
                    AdminScreen(vm, onCerrarSesion = onCerrarSesion)
                }
            }
        }
    }
}
