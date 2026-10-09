package com.reyes.clinicasaludplus.ui.components

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import com.reyes.clinicasaludplus.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reyes.clinicasaludplus.data.repository.Repositorio
import com.reyes.clinicasaludplus.ui.theme.VerdeClaro
import com.reyes.clinicasaludplus.ui.theme.VerdePrimario
import com.reyes.clinicasaludplus.ui.theme.Blanco
import com.reyes.clinicasaludplus.ui.theme.BordeSuave
import com.reyes.clinicasaludplus.ui.theme.NavyTitulo
import com.reyes.clinicasaludplus.ui.theme.RojoAlerta
import com.reyes.clinicasaludplus.ui.theme.SlateTexto
import com.reyes.clinicasaludplus.ui.theme.Tam
import kotlinx.coroutines.launch

/** Opciones del menú lateral (además de "Cerrar sesión"). */
enum class DestinoMenu(val etiqueta: String, @DrawableRes val icono: Int) {
    Sede("Sede", R.drawable.ic_menu_sede),
    Doctor("Doctor", R.drawable.ic_menu_doctor),
    Agenda("Agenda", R.drawable.ic_menu_agenda)
}

/**
 * Pantalla principal con menú lateral (Sede, Doctor, Agenda y Cerrar sesión).
 * Si se pasa [alVolver], la pantalla es secundaria: muestra la flecha de volver y no abre el menú.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaConMenu(
    titulo: String,
    seleccionado: DestinoMenu?,
    alNavegar: (DestinoMenu) -> Unit,
    alIrAInicio: () -> Unit,
    alCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
    alVolver: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    contenido: @Composable (PaddingValues) -> Unit
) {
    if (alVolver != null) {
        Scaffold(
            modifier = modifier,
            containerColor = Blanco,
            topBar = { BarraSuperiorConVolver(titulo = titulo, alVolver = alVolver, acciones = acciones) },
            bottomBar = bottomBar,
            content = contenido
        )
        return
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var confirmarSalida by rememberSaveable { mutableStateOf(false) }
    val nombre = Repositorio.usuarioActual?.nombreCompleto.orEmpty()

    BackHandler(enabled = drawerState.isOpen) { scope.launch { drawerState.close() } }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Blanco) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            scope.launch { drawerState.close() }
                            alIrAInicio()
                        }
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LogoSaludPlus(escala = 0.34f)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "CLÍNICA",
                                fontSize = Tam.Pequeno,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 3.sp,
                                color = SlateTexto
                            )
                            Text(
                                text = "SALUDPLUS",
                                fontSize = Tam.Barra,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = VerdePrimario
                            )
                        }
                    }
                    if (nombre.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(text = nombre, fontSize = Tam.Cuerpo, color = SlateTexto)
                    }
                }

                DestinoMenu.entries.forEach { destino ->
                    HorizontalDivider(color = BordeSuave, modifier = Modifier.padding(horizontal = 16.dp))
                    ItemMenu(
                        etiqueta = destino.etiqueta,
                        icono = {
                            Image(
                                painter = painterResource(destino.icono),
                                contentDescription = null,
                                modifier = Modifier.size(44.dp)
                            )
                        },
                        seleccionado = destino == seleccionado,
                        color = NavyTitulo
                    ) {
                        scope.launch { drawerState.close() }
                        if (destino != seleccionado) alNavegar(destino)
                    }
                }

                HorizontalDivider(color = BordeSuave, modifier = Modifier.padding(horizontal = 16.dp))

                ItemMenu(
                    etiqueta = "Cerrar sesión",
                    icono = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    },
                    seleccionado = false,
                    color = RojoAlerta
                ) {
                    scope.launch { drawerState.close() }
                    confirmarSalida = true
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Blanco,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = titulo,
                            fontSize = Tam.Barra,
                            fontWeight = FontWeight.Bold,
                            color = NavyTitulo
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú",
                                tint = NavyTitulo
                            )
                        }
                    },
                    actions = acciones,
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = bottomBar,
            content = contenido
        )
    }

    DialogoConfirmacion(
        mostrar = confirmarSalida,
        titulo = "¿Cerrar sesión?",
        mensaje = "Tendrás que iniciar sesión nuevamente para ver y agendar tus citas.",
        onConfirmar = {
            confirmarSalida = false
            alCerrarSesion()
        },
        onDescartar = { confirmarSalida = false }
    )
}

@Composable
private fun ItemMenu(
    etiqueta: String,
    icono: @Composable () -> Unit,
    seleccionado: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(text = etiqueta, fontSize = Tam.Subtitulo, fontWeight = FontWeight.Medium)
        },
        icon = icono,
        selected = seleccionado,
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(72.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = VerdeClaro,
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = VerdePrimario,
            unselectedTextColor = color,
            selectedIconColor = VerdePrimario,
            unselectedIconColor = color
        )
    )
}
