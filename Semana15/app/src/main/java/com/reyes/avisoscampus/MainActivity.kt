package com.reyes.avisoscampus

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyes.avisoscampus.ui.PantallaAvisos
import com.reyes.avisoscampus.ui.theme.AvisoscampusTheme
import com.reyes.avisoscampus.viewmodel.AvisosViewModel
import com.reyes.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Íconos blancos en la barra de estado (sobre la barra azul)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            AvisoscampusTheme {
                val vm: AvisosViewModel = viewModel(factory = AvisosViewModelFactory())
                PantallaAvisos(vm)
            }
        }
    }
}
