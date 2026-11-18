package com.reyes.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyes.campusgps.ui.PantallaMapa
import com.reyes.campusgps.ui.theme.CampusgpsTheme
import com.reyes.campusgps.viewmodel.CampusViewModelFactory
import com.reyes.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusgpsTheme {
                val vm: MapaViewModel = viewModel(factory = CampusViewModelFactory(applicationContext))
                PantallaMapa(vm)
            }
        }
    }
}
