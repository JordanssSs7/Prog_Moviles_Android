# PROMPTS.md · Fase 2 (rama avisoscampus-ia)

Registro de los prompts importantes que usé con el asistente de IA para mejorar AvisosCampus sin romper la arquitectura MVVM + Repository.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un segundo canal *Urgentes* con IMPORTANCE_HIGH para que el recordatorio salga como notificación emergente; los demás avisos siguen en el canal *Avisos*. No pongas Context en el ViewModel.» | Propuso crear los dos canales con `createNotificationChannels`, agregar `urgente: Boolean = false` a `Aviso` y a `registrar(...)` del Repository, y que el ViewModel marque el recordatorio con `urgente = true`. El Notificador elige el canal según `aviso.urgente`. | Acepté la propuesta. Descarté la alternativa de que el Notificador decidiera el canal mirando `aviso.origen == RECORDATORIO`. | Decidir qué es urgente es una regla de la app y le toca al ViewModel. El Notificador solo traduce esa decisión a un canal, y el ViewModel sigue sin conocer `NotificationManager`. |
| «Quiero que al expandir la notificación se lea un texto más largo con BigTextStyle, sin cambiar los textos cortos del laboratorio.» | Propuso agregar `detalle: String? = null` a `Aviso` y a `registrar(...)`, que el ViewModel pase el texto largo de «Tarea lista» y del recordatorio, y que el Notificador use `BigTextStyle().bigText(aviso.detalle ?: aviso.texto)`. | Acepté la propuesta. Descarté alargar directamente el texto corto del aviso. | Así la notificación cerrada y el historial siguen mostrando los datos ficticios pedidos, y el texto largo solo aparece al expandir. Si no hay detalle, usa el texto corto y no falla. |
| «Agrega un receptor del cargador que avise con ACTION_POWER_CONNECTED y ACTION_POWER_DISCONNECTED, con su propio switch, igual que el del modo avión.» | Propuso `CargadorReceiver` con un `onReceive` corto, el origen `CARGADOR` con su color en el historial, `escuchandoCargador`, `cambiarEscuchaCargador()` y `alCambiarCargador()` en el ViewModel, y registrarlo en la pantalla con `DisposableEffect`. | Acepté, pero revisé que el receptor se registre con `RECEIVER_NOT_EXPORTED` y que `unregisterReceiver` esté en `onDispose`. Descarté declararlo en el AndroidManifest. | Desde Android 8 un receptor del Manifest ya no recibe estos anuncios. Además, si no se quita al salir, se filtra memoria. El ViewModel sigue sin `BroadcastReceiver` ni `Context`. |

## Mejora descartada

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «¿Conviene hacer el historial persistente con DataStore?» | Para usar DataStore, el Repository necesita un `Context` (o el DataStore creado con él) desde la Factory. | Descarté esta mejora y elegí el receptor del cargador. | La guía dice que el Repository no debe usar `Context`. Preferí no romper esa regla de la arquitectura. |
