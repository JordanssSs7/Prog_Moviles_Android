package com.reyes.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.components.AvatarGrande
import com.reyes.lavacampus.ui.components.DatoPerfil
import com.reyes.lavacampus.ui.components.EditarPerfilDialog
import com.reyes.lavacampus.ui.components.PerfilColor
import com.reyes.lavacampus.ui.components.SeccionClara
import com.reyes.lavacampus.ui.components.colorMarca
import com.reyes.lavacampus.ui.theme.Tinta
import com.reyes.lavacampus.ui.theme.TintaSuave
import com.reyes.lavacampus.viewmodel.SesionViewModel

/** Perfil del administrador (se abre desde el menu lateral): mismo estilo que el del estudiante. */
@Composable
fun PerfilAdminScreen(sesion: SesionViewModel, onAtras: () -> Unit, onConfiguracion: () -> Unit) {
    val usuario by sesion.usuarioActual.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()
    val rol = Rol.ADMIN
    val nombre = usuario?.nombre ?: "Administrador"
    var editando by remember { mutableStateOf(false) }

    PerfilColor(
        rol = rol,
        titulo = nombre,
        onAtras = onAtras,
        iconoAccion = Icons.Filled.Settings,
        descripcionAccion = "Ajustes",
        onAccion = onConfiguracion
    ) {
        AvatarGrande(rol, onEditar = { editando = true })

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DatoPerfil("Nombre completo", nombre)
            DatoPerfil("Correo", usuario?.correo ?: "")
            DatoPerfil("Perfil", "Administrador")

            Spacer(Modifier.height(4.dp))
            SeccionClara("Recepción de pedidos:")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (recibir) "Abierta" else "Cerrada",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (recibir) Color(0xFF16A34A) else colorMarca(rol))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
                Column {
                    Text(
                        if (recibir) "Los estudiantes pueden crear pedidos" else "Los estudiantes no pueden crear pedidos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Tinta
                    )
                    Text("Se cambia en Ajustes", style = MaterialTheme.typography.labelSmall, color = TintaSuave)
                }
            }
        }
    }

    if (editando) {
        EditarPerfilDialog(
            nombreActual = nombre,
            onGuardar = { n, alTerminar -> sesion.editarNombre(n, alTerminar) },
            onCerrar = { editando = false }
        )
    }
}
