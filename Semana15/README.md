# AvisosCampus

App Android hecha con **Kotlin + Jetpack Compose + Material 3** que muestra notificaciones locales y escucha lo que pasa en el celular:

- **Mostrar aviso**: muestra al instante la notificación «Tarea lista».
- **Recordatorio**: programa un aviso que llega a los 10 segundos, con cuenta regresiva y opción de cancelar (corrutinas).
- **Receptor de anuncios**: con un `BroadcastReceiver` avisa cuando se activa o desactiva el modo avión.
- **Historial**: todo queda anotado, con lo más reciente arriba.
- **Permiso denegado**: la app sigue funcionando y permite ir a Ajustes para corregirlo.

Patrón **MVVM + Repository (interface)**. Solo datos ficticios.

## Estructura de paquetes

```
com.reyes.avisoscampus/
├── AvisosApp.kt                  Application: crea el canal
├── MainActivity.kt
├── model/
│   └── Modelos.kt                Aviso, Origen, PermisoUi
├── data/
│   └── AvisosRepository.kt       interface + AvisosRepositoryMemoria
├── notificacion/
│   └── Notificador.kt            canal, permiso y mostrar
├── receiver/
│   └── ModoAvionReceiver.kt      escucha ACTION_AIRPLANE_MODE_CHANGED
├── viewmodel/
│   ├── AvisosViewModel.kt        decide qué avisar (sin Context)
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt         tarjetas de la pantalla
    ├── HistorialAvisos.kt        tarjeta del historial
    └── theme/
```

## Diagrama MVVM

```
View (PantallaAvisos)
   | eventos: botones, switch          ^ collectAsState()
   v                                   |
ViewModel (AvisosViewModel) --- StateFlow (avisos, permiso, faltan...) --->
   | registra el aviso                 | emite: eventos (SharedFlow<Aviso>)
   v                                   v
AvisosRepository (interface)       View recibe el evento y llama a
 -> AvisosRepositoryMemoria        Notificador.mostrar(context, aviso)
```

El ViewModel **decide** qué avisar y lo anota en el Repository. Después **emite un evento**. La pantalla lo recibe y, como sí tiene `Context`, llama al `Notificador`. El ViewModel nunca usa `Context`, `NotificationManager` ni `BroadcastReceiver`.

## Capturas

| Notificación en el panel | Recordatorio | Modo avión |
|---|---|---|
| ![Notificación](capturas/notificacion.png) | ![Recordatorio](capturas/recordatorio.png) | ![Modo avión](capturas/modo_avion.png) |

| Historial | Permiso denegado |
|---|---|
| ![Historial](capturas/historial.png) | ![Permiso denegado](capturas/permiso_denegado.png) |

## Para pensar

**1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?**
El **canal** (Android 8+) agrupa las notificaciones y deja que el usuario controle su importancia, sonido o si las quiere ver; sin canal, Android 8+ descarta la notificación sin mostrarla. El **permiso** `POST_NOTIFICATIONS` (Android 13+) es el consentimiento del usuario; si no está concedido, `notify` no muestra nada. Por eso `Notificador.mostrar` revisa el permiso y devuelve `false`.

**2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?**
Mostrar una notificación necesita `Context`, y el ViewModel vive más que la Activity: guardar un `Context` ahí puede filtrar memoria. Además, sin dependencias de Android el ViewModel se puede probar con pruebas unitarias. El ViewModel decide y la vista (que sí tiene `Context`) ejecuta.

**3. ¿Qué pasa si usas el mismo id en dos notificaciones?**
La segunda **reemplaza** a la primera: en el panel solo queda una. Por eso se usa `aviso.id`, que es distinto para cada aviso.

**4. ¿Por qué onReceive debe ser corto y por qué hay que quitar el receptor al salir?**
`onReceive` corre en el hilo principal: si tarda, la app se congela y Android puede mostrar un ANR. Si no se llama a `unregisterReceiver`, el receptor sigue apuntando a una pantalla que ya no existe y se **filtra memoria**. Por eso se registra y se quita dentro de `DisposableEffect`.

**5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?**
**Local**: cuando el aviso nace en el propio celular (un recordatorio, una tarea terminada, un cambio del sistema como el modo avión). **Push (FCM)**: cuando el aviso nace en un servidor y debe llegar aunque la app esté cerrada (un mensaje nuevo, una nota publicada, una alerta para muchos usuarios).
