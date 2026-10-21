package com.reyes.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.navigation.NavLava
import com.reyes.lavacampus.ui.theme.LavaTheme
import com.reyes.lavacampus.viewmodel.LavaViewModelFactory
import com.reyes.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as LavaApp
        val factory = LavaViewModelFactory(app.repository, app.prefs)
        setContent {
            val sesion: SesionViewModel = viewModel(factory = factory)
            val usuario by sesion.usuarioActual.collectAsState()
            val oscuro by sesion.modoOscuro.collectAsState()
            val rol = if (usuario?.rol == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

            LavaTheme(rol = rol, oscuro = oscuro) {
                NavLava(sesion = sesion, factory = factory)
            }
        }
    }
}
