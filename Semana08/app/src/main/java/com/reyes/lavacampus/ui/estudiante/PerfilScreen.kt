package com.reyes.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.AvatarGrande
import com.reyes.lavacampus.ui.components.BarraActividad
import com.reyes.lavacampus.ui.components.BarraNivel
import com.reyes.lavacampus.ui.components.DatoPerfil
import com.reyes.lavacampus.ui.components.EditarPerfilDialog
import com.reyes.lavacampus.ui.components.PerfilColor
import com.reyes.lavacampus.ui.components.SeccionClara
import com.reyes.lavacampus.viewmodel.PerfilViewModel
import com.reyes.lavacampus.viewmodel.SesionViewModel

/**
 * Perfil del estudiante a color completo: flecha, nombre y engranaje arriba, avatar con el lapiz de
 * editar, datos con etiqueta y las barras de nivel y de actividad.
 */
@Composable
fun PerfilScreen(
    perfil: PerfilViewModel,
    sesion: SesionViewModel,
    onAtras: () -> Unit,
    onConfiguracion: () -> Unit
) {
    val activos by perfil.pedidosActivos.collectAsState()
    val totales by perfil.pedidosTotales.collectAsState()
    val kilos by perfil.kilosLavados.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()
    val u = usuario ?: perfil.usuario
    val rol = Rol.CLIENTE
    var editando by remember { mutableStateOf(false) }

    PerfilColor(
        rol = rol,
        titulo = u.nombre,
        onAtras = onAtras,
        iconoAccion = Icons.Filled.Settings,
        descripcionAccion = "Configuración",
        onAccion = onConfiguracion
    ) {
        AvatarGrande(rol, onEditar = { editando = true })

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DatoPerfil("Código de estudiante", u.codigo, icono = Icons.Filled.Badge)
            DatoPerfil("Nombre completo", u.nombre, icono = Icons.Filled.Person)
            DatoPerfil("Correo", u.correo, icono = Icons.Filled.Email)

            Spacer(Modifier.height(4.dp))
            SeccionClara("Nivel de cliente:")
            BarraNivel(rol, totales)

            SeccionClara("Mi actividad:")
            BarraActividad(
                rol,
                listOf(
                    Triple(Icons.Filled.Schedule, "$activos", "Activos"),
                    Triple(Icons.Filled.Receipt, "$totales", "Pedidos"),
                    Triple(Icons.Filled.LocalLaundryService, "%.1f".format(kilos), "Kg lavados")
                )
            )
        }
    }

    if (editando) {
        EditarPerfilDialog(
            nombreActual = u.nombre,
            onGuardar = { nombre, alTerminar -> sesion.editarNombre(nombre, alTerminar) },
            onCerrar = { editando = false }
        )
    }
}
