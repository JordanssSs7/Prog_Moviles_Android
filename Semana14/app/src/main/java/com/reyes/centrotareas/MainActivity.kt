package com.reyes.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyes.centrotareas.data.TareasRepositoryLocal
import com.reyes.centrotareas.ui.PantallaTareas
import com.reyes.centrotareas.ui.theme.CentrotareasTheme
import com.reyes.centrotareas.viewmodel.TareasViewModel
import com.reyes.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentrotareasTheme {
                val vm: TareasViewModel = viewModel(
                    factory = TareasViewModelFactory(TareasRepositoryLocal())
                )
                PantallaTareas(vm)
            }
        }
    }
}
