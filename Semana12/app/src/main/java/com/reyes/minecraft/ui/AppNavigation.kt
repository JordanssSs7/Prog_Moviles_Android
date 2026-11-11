package com.reyes.minecraft.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.reyes.minecraft.ui.components.BarraNavegacionFlotante
import com.reyes.minecraft.ui.components.ConfirmacionOverlay
import com.reyes.minecraft.ui.components.ItemNavegacion
import com.reyes.minecraft.ui.components.TituloApp
import com.reyes.minecraft.ui.screens.AdminScreen
import com.reyes.minecraft.ui.screens.DetalleScreen
import com.reyes.minecraft.ui.screens.FavoritosScreen
import com.reyes.minecraft.ui.screens.ListaScreen
import com.reyes.minecraft.ui.screens.PerfilScreen
import com.reyes.minecraft.ui.screens.QuizScreen
import com.reyes.minecraft.ui.viewmodel.AdminViewModel
import com.reyes.minecraft.ui.viewmodel.DetalleViewModel
import com.reyes.minecraft.ui.viewmodel.FavoritosViewModel
import com.reyes.minecraft.ui.viewmodel.ListaViewModel
import com.reyes.minecraft.ui.viewmodel.PerfilViewModel
import com.reyes.minecraft.ui.viewmodel.QuizViewModel
import com.reyes.minecraft.ui.viewmodel.SesionViewModel

private object Rutas {
    const val LISTA = "lista"
    const val FAVORITOS = "favoritos"
    const val QUIZ = "quiz"
    const val PERFIL = "perfil"
    const val ADMIN = "admin"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
}

/** Decide qué se ve según la sesión: carga, bienvenida/login/registro o la app principal. */
@Composable
fun MinecraftUi(contenedor: AppContainer) {
    val sesionVm: SesionViewModel = viewModel(
        factory = viewModelFactory { initializer { SesionViewModel(contenedor.authRepository) } }
    )
    val estado by sesionVm.estado.collectAsStateWithLifecycle()
    val bienvenida by sesionVm.bienvenida.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Column(
            Modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            TituloApp()
            CircularProgressIndicator()
        }
        is UiState.Error -> AuthFlow(sesionVm)
        is UiState.Exito -> {
            val usuario = e.datos
            if (usuario == null) AuthFlow(sesionVm)
            else Box {
                Principal(usuario, contenedor, onCerrarSesion = sesionVm::cerrarSesion)
                // Confirmación de "inicio de sesión correcto": solo al entrar con login, no al reabrir la app.
                bienvenida?.let {
                    ConfirmacionOverlay(
                        titulo = "¡Bienvenido!",
                        mensaje = "Hola, $it. Inicio de sesión correcto",
                        onTerminar = sesionVm::bienvenidaMostrada,
                    )
                }
            }
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
        add(ItemNavegacion(Rutas.LISTA, "Catálogo", Icons.Default.Home))
        add(ItemNavegacion(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite))
        add(ItemNavegacion(Rutas.QUIZ, "Quiz", Icons.Default.PlayArrow))
        add(ItemNavegacion(Rutas.PERFIL, "Perfil", Icons.Default.Person))
        if (usuario.rol == Rol.ADMIN) add(ItemNavegacion(Rutas.ADMIN, "Admin", Icons.Default.Settings))
    }
    val abrirDetalle: (Int) -> Unit = { nav.navigate(Rutas.detalle(it)) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (opciones.any { it.ruta == rutaActual }) {
                BarraNavegacionFlotante(
                    opciones = opciones,
                    rutaActual = rutaActual,
                    onSeleccion = { ruta ->
                        nav.navigate(ruta) {
                            popUpTo(Rutas.LISTA) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.navigationBarsPadding(),
                )
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Rutas.LISTA, modifier = Modifier.padding(padding)) {
            composable(Rutas.LISTA) {
                val vm: ListaViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            ListaViewModel(contenedor.itemRepository, contenedor.notificador, contenedor.conectividad)
                        }
                    }
                )
                ListaScreen(usuario, vm, onItemClick = abrirDetalle)
            }
            composable(Rutas.FAVORITOS) {
                val vm: FavoritosViewModel = viewModel(
                    factory = viewModelFactory { initializer { FavoritosViewModel(contenedor.itemRepository) } }
                )
                FavoritosScreen(vm, onItemClick = abrirDetalle)
            }
            composable(Rutas.QUIZ) {
                val vm: QuizViewModel = viewModel(
                    factory = viewModelFactory { initializer { QuizViewModel(contenedor.itemRepository) } }
                )
                QuizScreen(vm)
            }
            composable(Rutas.PERFIL) {
                val vm: PerfilViewModel = viewModel(
                    factory = viewModelFactory { initializer { PerfilViewModel(contenedor.itemRepository) } }
                )
                val totales by vm.totales.collectAsStateWithLifecycle()
                PerfilScreen(usuario, totales, onCerrarSesion)
            }
            composable(
                Rutas.DETALLE,
                arguments = listOf(navArgument("id") { type = NavType.IntType }),
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: 0
                val vm: DetalleViewModel = viewModel(
                    key = "detalle$id",
                    factory = viewModelFactory { initializer { DetalleViewModel(id, contenedor.itemRepository) } }
                )
                DetalleScreen(vm, onItemClick = abrirDetalle, onAtras = { nav.popBackStack() })
            }
            if (usuario.rol == Rol.ADMIN) {
                composable(Rutas.ADMIN) {
                    val vm: AdminViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer {
                                AdminViewModel(
                                    contenedor.itemRepository,
                                    contenedor.comunidadRepository,
                                    contenedor.notificador,
                                )
                            }
                        }
                    )
                    AdminScreen(vm)
                }
            }
        }
    }
}
