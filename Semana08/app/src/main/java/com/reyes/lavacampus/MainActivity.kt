package com.reyes.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.reyes.lavacampus.model.Rol
import com.reyes.lavacampus.ui.navigation.NavLava
import com.reyes.lavacampus.ui.theme.LavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // TODO (Paso 6): el rol y el modo oscuro vendran de DataStore
            LavaTheme(rol = Rol.ADMIN, oscuro = false) {
                NavLava()
            }
        }
    }
}
