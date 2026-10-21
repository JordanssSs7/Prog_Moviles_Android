package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.administrador.PanelScreen
import com.reyes.lavacampus.ui.administrador.ServiciosScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.reyes.lavacampus.ui.estudiante.DetallePedidoScreen
import com.reyes.lavacampus.ui.estudiante.InicioScreen
import com.reyes.lavacampus.ui.estudiante.LoginScreen
import com.reyes.lavacampus.ui.estudiante.MisPedidosScreen
import com.reyes.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.reyes.lavacampus.ui.estudiante.TarifasScreen
import com.reyes.lavacampus.ui.estudiante.SplashScreen
import com.reyes.lavacampus.viewmodel.DetallePedidoViewModel
import com.reyes.lavacampus.viewmodel.InicioViewModel
import com.reyes.lavacampus.viewmodel.LavaViewModelFactory
import com.reyes.lavacampus.viewmodel.MisPedidosViewModel
import com.reyes.lavacampus.viewmodel.NuevoPedidoViewModel
import com.reyes.lavacampus.viewmodel.PanelViewModel
import com.reyes.lavacampus.viewmodel.TarifasViewModel
import com.reyes.lavacampus.viewmodel.ServiciosViewModel
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Pantalla principal de cada perfil. */
private fun NavHostController.irAPrincipal(rol: Rol) {
    navigate(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO) {
        popUpTo(0) { inclusive = true }
    }
}

@Composable
fun NavLava(sesion: SesionViewModel, factory: LavaViewModelFactory) {
    val navController = rememberNavController()
    val sesionLista by sesion.sesionLista.collectAsState()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(sesionLista = sesionLista, onTerminar = {
                val usuario = sesion.usuarioActual.value
                if (usuario != null) {
                    // Ya hay perfil guardado: salta el login
                    navController.irAPrincipal(Rol.valueOf(usuario.rol))
                } else {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(vm = sesion, onLoginExitoso = { rol -> navController.irAPrincipal(rol) })
        }
        composable(Rutas.NUEVO_PEDIDO) {
            val usuario by sesion.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: NuevoPedidoViewModel = viewModel(
                    key = "nuevo-pedido-${u.id}",
                    factory = viewModelFactory { initializer { NuevoPedidoViewModel(factory.repo, u) } }
                )
                NuevoPedidoScreen(
                    vm = vm,
                    onVolver = { navController.popBackStack() },
                    // Al confirmar se vuelve a Mis pedidos (Inicio queda debajo en la pila)
                    onPedidoCreado = {
                        navController.navigate(Rutas.MIS_PEDIDOS) { popUpTo(Rutas.INICIO) }
                    }
                )
            }
        }
        composable(Rutas.MIS_PEDIDOS) {
            val usuario by sesion.usuarioActual.collectAsState()
            usuario?.let { u ->
                val vm: MisPedidosViewModel = viewModel(
                    key = "mis-pedidos-${u.id}",
                    factory = viewModelFactory { initializer { MisPedidosViewModel(factory.repo, u) } }
                )
                MisPedidosScreen(
                    vm = vm,
                    onVerPedido = { id -> navController.navigate(Rutas.detalle(id)) },
                    onNuevoPedido = { navController.navigate(Rutas.NUEVO_PEDIDO) },
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        composable(Rutas.TARIFAS) {
            val vm: TarifasViewModel = viewModel(factory = factory)
            TarifasScreen(vm = vm, onVolver = { navController.popBackStack() })
        }
        composable(
            route = Rutas.DETALLE_PEDIDO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: return@composable
            val vm: DetallePedidoViewModel = viewModel(
                key = "detalle-$id",
                factory = viewModelFactory { initializer { DetallePedidoViewModel(factory.repo, id) } }
            )
            DetallePedidoScreen(vm = vm, onVolver = { navController.popBackStack() })
        }
        // Destinos temporales: se reemplazan en el Paso 7 (Inicio) y el Paso 11 (Panel)
        composable(Rutas.INICIO) {
            val usuario by sesion.usuarioActual.collectAsState()
            usuario?.let { u ->
                // Este ViewModel recibe al estudiante actual, por eso usa su propia fabrica
                val vm: InicioViewModel = viewModel(
                    key = "inicio-${u.id}",
                    factory = viewModelFactory { initializer { InicioViewModel(factory.repo, u) } }
                )
                InicioScreen(
                    vm = vm,
                    onNuevoPedido = { navController.navigate(Rutas.NUEVO_PEDIDO) },
                    onVerPedido = { id -> navController.navigate(Rutas.detalle(id)) },
                    onMisPedidos = { navController.navigate(Rutas.MIS_PEDIDOS) },
                    onTarifas = { navController.navigate(Rutas.TARIFAS) }
                )
            }
        }
        composable(Rutas.PANEL) {
            val vm: PanelViewModel = viewModel(factory = factory)
            // TODO (Paso 12): "Ver" abrira la bandeja filtrada por pedidos LISTO
            PanelScreen(vm = vm, onVerListos = { })
        }
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            ServiciosScreen(vm = vm, onEditar = { /* TODO (Paso 13) */ })
        }
        // TODO: el resto del grafo se completa en los pasos siguientes
    }
}

@Composable
private fun Marcador(texto: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(texto) }
}
