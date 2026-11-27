package com.reyes.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.reyes.avisoscampus.MainActivity
import com.reyes.avisoscampus.R
import com.reyes.avisoscampus.model.Aviso

object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // Los canales existen desde Android 8 (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de la app"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        // Antes de Android 13 (API 33) el permiso no existe: siempre se puede notificar
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission") // el permiso se revisa con tienePermiso
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // 1. Sin permiso no se muestra nada (y no se lanza error)
        if (!tienePermiso(context)) return false

        // Paso 4: al tocar la notificación se abre MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            // si la app ya está abierta, se reutiliza la misma pantalla
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendiente = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Construir la notificación
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendiente)
            .setAutoCancel(true) // se quita al tocarla
            .build()

        // 3. Mostrarla con el id del aviso: cada aviso es una notificación distinta
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}
