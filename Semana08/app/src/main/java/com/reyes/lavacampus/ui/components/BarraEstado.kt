package com.reyes.lavacampus.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** Iconos de la barra de estado claros (sobre fondos azules) u oscuros (sobre fondos claros). */
@Composable
fun IconosBarraEstado(claros: Boolean) {
    val vista = LocalView.current
    if (vista.isInEditMode) return
    SideEffect {
        val ventana = (vista.context as? Activity)?.window ?: return@SideEffect
        WindowCompat.getInsetsController(ventana, vista).isAppearanceLightStatusBars = !claros
    }
}
