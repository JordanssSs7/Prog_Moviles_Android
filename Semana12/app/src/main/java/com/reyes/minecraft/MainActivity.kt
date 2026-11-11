package com.reyes.minecraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.reyes.minecraft.ui.MinecraftUi
import com.reyes.minecraft.ui.theme.MinecraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val contenedor = (application as MinecraftApp).contenedor
        setContent {
            MinecraftTheme {
                MinecraftUi(contenedor)
            }
        }
    }
}
