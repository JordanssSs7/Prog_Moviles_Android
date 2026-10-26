package com.reyes.lavacampus.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.reyes.lavacampus.model.EstadoPedido
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.administrador.AjustesScreen
import com.reyes.lavacampus.ui.administrador.BandejaScreen
import com.reyes.lavacampus.ui.administrador.ClientesScreen
import com.reyes.lavacampus.ui.administrador.FormularioServicioScreen
import com.reyes.lavacampus.ui.administrador.GestionPedidoScreen
import com.reyes.lavacampus.ui.administrador.PanelScreen
import com.reyes.lavacampus.ui.administrador.PerfilAdminScreen
import com.reyes.lavacampus.ui.administrador.ServiciosScreen
import com.reyes.lavacampus.ui.components.IconosBarraEstado
import com.reyes.lavacampus.ui.estudiante.BienvenidaScreen
import com.reyes.lavacampus.ui.estudiante.ConfiguracionScreen
import com.reyes.lavacampus.ui.estudiante.DetallePedidoScreen
import com.reyes.lavacampus.ui.estudiante.InicioScreen
import com.reyes.lavacampus.ui.estudiante.LoginScreen
import com.reyes.lavacampus.ui.estudiante.MisPedidosScreen
import com.reyes.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.reyes.lavacampus.ui.estudiante.PerfilScreen
import com.reyes.lavacampus.ui.estudiante.RegistroScreen
import com.reyes.lavacampus.ui.estudiante.SplashScreen
import com.reyes.lavacampus.ui.estudiante.TarifasScreen
import com.reyes.lavacampus.viewmodel.BandejaViewModel
import com.reyes.lavacampus.viewmodel.ClientesViewModel
import com.reyes.lavacampus.viewmodel.DetallePedidoViewModel
import com.reyes.lavacampus.viewmodel.GestionPedidoViewModel
import com.reyes.lavacampus.viewmodel.InicioViewModel
import com.reyes.lavacampus.viewmodel.LavaViewModelFactory
import com.reyes.lavacampus.viewmodel.MisPedidosViewModel
import com.reyes.lavacampus.viewmodel.NuevoPedidoViewModel
import com.reyes.lavacampus.viewmodel.PanelViewModel
import com.reyes.lavacampus.viewmodel.PerfilViewModel
import com.reyes.lavacampus.viewmodel.ServiciosViewModel
import com.reyes.lavacampus.viewmodel.SesionViewModel
import com.reyes.lavacampus.viewmodel.TarifasViewModel

/** Va a la pantalla principal de un perfil limpiando toda la pila (popUpTo). */
private fun NavHostController.irAPrincipal(rol: Rol) {
    navigate(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO) {
        popUpTo(0) { inclusive = true }
    }
}

/** Cambia de pestana de la barra inferior conservando el estado de cada una. */
private fun NavHostController.irAPestana(p: Pestana, raiz: String) {
    // La casita vuelve a la pantalla principal que ya esta en la pila; navegar hacia ella con restoreState
    // la dejaba sin responder cuando se venia de otra pantalla abierta encima (por ejemplo Mis pedidos)
    if (p.destino == raiz && popBackStack(raiz, inclusive = false)) return
    navigate(p.destino) {
        popUpTo(raiz) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun NavLava(sesion: SesionViewModel, factory: LavaViewModelFactory) {
    val navController = rememberNavController()
    val sesionLista by sesion.sesionLista.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()
    val entrada by navController.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route
    val oscuro by sesion.modoOscuro.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val alcance = rememberCoroutineScope()

    // Iconos claros de la barra de estado sobre los fondos de color (splash, bienvenida, acceso, perfil y menu) y en modo oscuro
    IconosBarraEstado(
        claros = oscuro || drawerState.currentValue == DrawerValue.Open || rutaActual in setOf(
            Rutas.SPLASH, Rutas.BIENVENIDA, Rutas.LOGIN, Rutas.REGISTRO,
            Rutas.PERFIL, Rutas.PERFIL_ADMIN, Rutas.CONFIGURACION, Rutas.AJUSTES
        )
    )

    // Barra inferior: estrella, casita y persona; el resto de opciones esta en el menu lateral
    val esAdmin = usuario?.rol == Rol.ADMIN.name
    val pestanas = if (esAdmin) PestanasAdmin else PestanasEstudiante
    val raiz = if (esAdmin) Rutas.PANEL else Rutas.INICIO
    val mostrarBarra = usuario != null &&
        rutaActual in (if (esAdmin) RutasConBarraAdmin else RutasConBarraEstudiante)

    // El menu lateral arranca siempre cerrado y se cierra al llegar a pantallas sin barra (splash, acceso...)
    LaunchedEffect(mostrarBarra) { if (!mostrarBarra) drawerState.snapTo(DrawerValue.Closed) }

    val abrirMenu: () -> Unit = { alcance.launch { drawerState.open() } }
    val cambiarPerfil: () -> Unit = { sesion.cambiarPerfil { rol -> navController.irAPrincipal(rol) } }
    val cerrarSesion: () -> Unit = {
        sesion.cerrarSesion { navController.navigate(Rutas.BIENVENIDA) { popUpTo(0) { inclusive = true } } }
    }

    // Opciones del menu lateral segun el perfil
    val itemsMenu = if (esAdmin) listOf(
        ItemMenu("Panel", Icons.Filled.Dashboard, Rutas.PANEL, Rutas.PANEL, true),
        ItemMenu("Pedidos", Icons.Filled.Inbox, Rutas.BANDEJA, Rutas.bandeja(), true),
        ItemMenu("Servicios", Icons.Filled.LocalLaundryService, Rutas.SERVICIOS, Rutas.SERVICIOS, true),
        ItemMenu("Clientes", Icons.Filled.People, Rutas.CLIENTES, Rutas.CLIENTES, true),
        ItemMenu("Ver perfil", Icons.Filled.Person, Rutas.PERFIL_ADMIN, Rutas.PERFIL_ADMIN, true),
        ItemMenu("Ajustes", Icons.Filled.Settings, Rutas.AJUSTES, Rutas.AJUSTES, true)
    ) else listOf(
        ItemMenu("Inicio", Icons.Filled.Home, Rutas.INICIO, Rutas.INICIO, true),
        ItemMenu("Ver perfil", Icons.Filled.Person, Rutas.PERFIL, Rutas.PERFIL, true),
        ItemMenu("Mis pedidos", Icons.Filled.Receipt, Rutas.MIS_PEDIDOS, Rutas.MIS_PEDIDOS, true),
        ItemMenu("Configuración", Icons.Filled.Settings, Rutas.CONFIGURACION, Rutas.CONFIGURACION, false)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = mostrarBarra || drawerState.isOpen,
        drawerContent = {
            val u = usuario
            if (u == null) {
                // Sin sesion el panel va vacio pero con su ancho, para que el cajon arranque cerrado
                ModalDrawerSheet(modifier = Modifier.width(310.dp)) {}
            } else {
                MenuLateral(
                    nombre = u.nombre,
                    subtitulo = if (esAdmin) "Administrador" else "Estudiante · ${u.codigo}",
                    rol = if (esAdmin) Rol.ADMIN else Rol.CLIENTE,
                    items = itemsMenu,
                    rutaActual = rutaActual,
                    onIr = { item ->
                        alcance.launch { drawerState.close() }
                        if (item.esPestana) {
                            navController.irAPestana(Pestana(item.titulo, item.icono, item.ruta, item.destino), raiz)
                        } else {
                            navController.navigate(item.destino) { launchSingleTop = true }
                        }
                    },
                    onCerrarSesion = {
                        alcance.launch { drawerState.close() }
                        cerrarSesion()
                    },
                    onInicio = {
                        alcance.launch { drawerState.close() }
                        navController.irAPestana(pestanas[1], raiz)
                    },
                    onCerrar = { alcance.launch { drawerState.close() } }
                )
            }
        }
    ) {
    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                BarraInferior(pestanas, rutaActual) { navController.irAPestana(it, raiz) }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { interior ->
        val abajo = PaddingValues(bottom = interior.calculateBottomPadding())
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(abajo).consumeWindowInsets(abajo)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(sesionLista = sesionLista, onTerminar = {
                    val u = sesion.usuarioActual.value
                    if (u != null) {
                        // Ya hay perfil guardado: salta el login
                        navController.irAPrincipal(Rol.valueOf(u.rol))
                    } else {
                        navController.navigate(Rutas.BIENVENIDA) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                    }
                })
            }
            composable(Rutas.BIENVENIDA) {
                BienvenidaScreen(
                    onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                    onCrearCuenta = { navController.navigate(Rutas.REGISTRO) }
                )
            }
            composable(Rutas.LOGIN) {
                LoginScreen(
                    vm = sesion,
                    onLoginExitoso = { rol -> navController.irAPrincipal(rol) },
                    // Se reemplaza el login por el registro para no apilar pantallas de acceso
                    onCrearCuenta = {
                        navController.navigate(Rutas.REGISTRO) { popUpTo(Rutas.BIENVENIDA) }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    vm = sesion,
                    // Tras crear la cuenta se muestra la confirmacion y se pasa al login (sin sesion iniciada)
                    onCuentaCreada = {
                        navController.navigate(Rutas.LOGIN) { popUpTo(Rutas.BIENVENIDA) }
                    },
                    onIniciarSesion = {
                        navController.navigate(Rutas.LOGIN) { popUpTo(Rutas.BIENVENIDA) }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }

            // ---------- Estudiante ----------
            composable(Rutas.INICIO) {
                usuario?.let { u ->
                    val vm: InicioViewModel = viewModel(
                        key = "inicio-${u.id}-${u.nombre}",
                        factory = viewModelFactory { initializer { InicioViewModel(factory.repo, u, factory.prefs) } }
                    )
                    InicioScreen(
                        vm = vm,
                        onNuevoPedido = { navController.navigate(Rutas.nuevoPedido()) },
                        onVerPedido = { id -> navController.navigate(Rutas.detalle(id)) },
                        onMisPedidos = { navController.navigate(Rutas.MIS_PEDIDOS) },
                        onTarifas = { navController.navigate(Rutas.TARIFAS) },
                        onMenu = abrirMenu
                    )
                }
            }
            composable(
                route = Rutas.NUEVO_PEDIDO,
                arguments = listOf(navArgument("repetir") { type = NavType.IntType; defaultValue = 0 })
            ) { entradaNuevo ->
                val repetir = entradaNuevo.arguments?.getInt("repetir") ?: 0
                usuario?.let { u ->
                    val vm: NuevoPedidoViewModel = viewModel(
                        key = "nuevo-pedido-${u.id}-$repetir",
                        factory = viewModelFactory {
                            initializer { NuevoPedidoViewModel(factory.repo, factory.prefs, u, repetir) }
                        }
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
                usuario?.let { u ->
                    val vm: MisPedidosViewModel = viewModel(
                        key = "mis-pedidos-${u.id}",
                        factory = viewModelFactory { initializer { MisPedidosViewModel(factory.repo, u) } }
                    )
                    MisPedidosScreen(
                        vm = vm,
                        onVerPedido = { id -> navController.navigate(Rutas.detalle(id)) },
                        onNuevoPedido = { navController.navigate(Rutas.nuevoPedido()) },
                        onRepetir = { id -> navController.navigate(Rutas.nuevoPedido(repetir = id)) }
                    )
                }
            }
            composable(Rutas.TARIFAS) {
                val vm: TarifasViewModel = viewModel(factory = factory)
                TarifasScreen(vm = vm)
            }
            composable(
                route = Rutas.DETALLE_PEDIDO,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entradaDetalle ->
                val id = entradaDetalle.arguments?.getInt("id") ?: return@composable
                val vm: DetallePedidoViewModel = viewModel(
                    key = "detalle-$id",
                    factory = viewModelFactory { initializer { DetallePedidoViewModel(factory.repo, id) } }
                )
                DetallePedidoScreen(vm = vm, onVolver = { navController.popBackStack() })
            }
            composable(Rutas.PERFIL) {
                usuario?.let { u ->
                    val perfil: PerfilViewModel = viewModel(
                        key = "perfil-${u.id}-${u.nombre}",
                        factory = viewModelFactory { initializer { PerfilViewModel(factory.repo, u) } }
                    )
                    PerfilScreen(
                        perfil = perfil,
                        sesion = sesion,
                        onAtras = { navController.irAPestana(PestanasEstudiante[1], Rutas.INICIO) },
                        onConfiguracion = { navController.navigate(Rutas.CONFIGURACION) }
                    )
                }
            }
            composable(Rutas.CONFIGURACION) {
                ConfiguracionScreen(
                    rol = Rol.CLIENTE,
                    sesion = sesion,
                    onAtras = { navController.popBackStack() },
                    onCambiarPerfil = cambiarPerfil,
                    onCerrarSesion = cerrarSesion
                )
            }

            // ---------- Administrador ----------
            composable(Rutas.PANEL) {
                val vm: PanelViewModel = viewModel(factory = factory)
                PanelScreen(
                    vm = vm,
                    onVerListos = { navController.navigate(Rutas.bandeja(EstadoPedido.LISTO.name)) },
                    onVerAtrasados = { navController.navigate(Rutas.bandeja()) },
                    onMenu = abrirMenu
                )
            }
            composable(
                route = Rutas.BANDEJA,
                arguments = listOf(navArgument("estado") { type = NavType.StringType; defaultValue = "" })
            ) { entradaBandeja ->
                // Filtro inicial opcional (el boton "Ver" del panel abre la bandeja en LISTO)
                val filtro = entradaBandeja.arguments?.getString("estado").orEmpty()
                    .takeIf { it.isNotEmpty() }?.let { EstadoPedido.valueOf(it) }
                val vm: BandejaViewModel = viewModel(
                    key = "bandeja-$filtro",
                    factory = viewModelFactory { initializer { BandejaViewModel(factory.repo, filtro) } }
                )
                BandejaScreen(
                    vm = vm,
                    onAbrirGestion = { id -> navController.navigate(Rutas.gestion(id)) }
                )
            }
            composable(
                route = Rutas.GESTION,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entradaGestion ->
                val id = entradaGestion.arguments?.getInt("id") ?: return@composable
                val vm: GestionPedidoViewModel = viewModel(
                    key = "gestion-$id",
                    factory = viewModelFactory { initializer { GestionPedidoViewModel(factory.repo, id) } }
                )
                GestionPedidoScreen(vm = vm, onVolver = { navController.popBackStack() })
            }
            composable(Rutas.SERVICIOS) {
                val vm: ServiciosViewModel = viewModel(factory = factory)
                ServiciosScreen(
                    vm = vm,
                    onEditar = { id -> navController.navigate(Rutas.formularioServicio(id)) },
                    onNuevo = { navController.navigate(Rutas.formularioServicio(0)) }
                )
            }
            composable(
                route = Rutas.FORMULARIO_SERVICIO,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entradaForm ->
                val id = entradaForm.arguments?.getInt("id") ?: 0
                val vm: ServiciosViewModel = viewModel(factory = factory)
                FormularioServicioScreen(vm = vm, servicioId = id, onVolver = { navController.popBackStack() })
            }
            composable(Rutas.CLIENTES) {
                val vm: ClientesViewModel = viewModel(factory = factory)
                ClientesScreen(vm = vm)
            }
            composable(Rutas.PERFIL_ADMIN) {
                PerfilAdminScreen(
                    sesion = sesion,
                    onAtras = { navController.popBackStack() },
                    onConfiguracion = { navController.irAPestana(PestanaAjustes, Rutas.PANEL) }
                )
            }
            composable(Rutas.AJUSTES) {
                AjustesScreen(
                    sesion = sesion,
                    onCambiarPerfil = cambiarPerfil,
                    onCerrarSesion = cerrarSesion
                )
            }
        }
    }
    }
}
