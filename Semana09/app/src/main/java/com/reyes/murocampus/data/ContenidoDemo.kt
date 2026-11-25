package com.reyes.murocampus.data

import com.reyes.murocampus.model.Comentario
import com.reyes.murocampus.model.Post

/**
 * Contenido de ejemplo con temas reales de programación móvil.
 * Se aplica sobre la respuesta del API (que devuelve texto aleatorio en inglés)
 * conservando ids, autores y relaciones para que las pantallas sigan siendo reales.
 */
object ContenidoDemo {

    private val titulos = listOf(
        "Jetpack Compose sin recomposiciones infinitas",
        "StateFlow en el ViewModel: una sola fuente de verdad",
        "collectAsStateWithLifecycle y el ciclo de vida",
        "Retrofit con suspend functions en Kotlin",
        "kotlinx.serialization: modelos @Serializable",
        "Result<T> para errores de red controlados",
        "CancellationException también importa",
        "LazyColumn: listas largas sin dolor",
        "Modifier: el orden del padding sí importa",
        "Material 3 con color de marca propio",
        "Room DAO con Flow y corrutinas",
        "WorkManager para tareas en segundo plano",
        "DataStore en lugar de SharedPreferences",
        "Navigation Compose con argumentos",
        "Coil para imágenes remotas en Compose",
        "Permisos runtime desde Compose",
        "Coroutines: Dispatchers.IO y Dispatchers.Main",
        "OkHttp y su interceptor de logging",
        "Paging 3 con LazyPagingItems",
        "Pruebas unitarias de un Repository",
        "Accesibilidad: contentDescription en Compose",
        "Tema oscuro sin romper los colores",
        "rememberSaveable y los cambios de rotación",
        "GET, POST y códigos de estado HTTP"
    )

    private val cuerpos = listOf(
        "Usa remember para estado local y rememberSaveable cuando gires el celular: el valor sobrevive al cambio de configuración.",
        "Expón StateFlow<UiState> desde el ViewModel y nunca MutableStateFlow. La vista sólo observa y envía eventos.",
        "collectAsStateWithLifecycle pausa la recolección en segundo plano. Es la forma correcta de observar flujos desde Compose.",
        "Declara suspend fun en la interfaz con @GET o @POST. Retrofit hace la llamada fuera del hilo principal.",
        "Anota cada data class con @Serializable y activa ignoreUnknownKeys: el JSON puede crecer sin romper la app.",
        "Envuelve la respuesta en Result.success o Result.failure. Así la UI no conoce HttpException ni Retrofit.",
        "Relanza CancellationException en el catch: es la señal de cancelación de la corrutina, no un fallo de red.",
        "LazyColumn reutiliza cada fila al desplazarse. Con cien publicaciones no inflas cien tarjetas a la vez.",
        "padding antes de background deja el espacio por fuera; después, por dentro. En Compose el orden cambia el diseño.",
        "Define primary en Color.kt y pon dynamicColor a false para que el azul se vea igual en cualquier dispositivo.",
        "Un DAO que devuelve Flow emite cada cambio de la tabla. La lista se actualiza sola sin recargar manualmente.",
        "WorkManager sobrevive al cierre de la app. Ideal para sincronizar datos cuando vuelva la conexión.",
        "DataStore guarda pares clave-valor de forma asíncrona y evita bloquear el hilo principal.",
        "Pasa el id del post como argumento de navegación y recupéralo en la pantalla de detalle.",
        "Coil carga la imagen con AsyncImage y maneja caché y placeholder sin código extra.",
        "rememberLauncherForActivityResult pide el permiso y devuelve el resultado directamente en la composición.",
        "Lanza la red con Dispatchers.IO y vuelve a Dispatchers.Main para actualizar el estado de la interfaz.",
        "Un HttpLoggingInterceptor en el cliente OkHttp muestra método, URL y cuerpo de cada petición en Logcat.",
        "Paging 3 carga páginas bajo demanda. LazyPagingItems conecta el flujo directamente con la lista.",
        "Un repositorio con constructor por interfaz se prueba fácil: sustituyes la API por un doble y verificas el Result.",
        "Añade contentDescription a los iconos para que TalkBack pueda leer la acción del botón.",
        "Usa los colores del tema en vez de hexadecimales sueltos: el modo oscuro cambia sin tocar cada pantalla.",
        "Si el formulario pierde lo escrito al rotar, te falta rememberSaveable en ese campo de texto.",
        "GET lee, POST crea. El cuerpo del POST viaja con @Body y la respuesta trae el id generado por el servidor."
    )

    private val comentarios = listOf(
        "¿Esto también aplica para Compose 1.7?",
        "Me sirvió el ejemplo del StateFlow, gracias.",
        "Ojo: el padding va antes del background.",
        "Lo probé con el emulador y funciona igual.",
        "¿Y si el servidor devuelve un campo nuevo?",
        "Con ignoreUnknownKeys no se cae la app.",
        "Recuerda relanzar CancellationException.",
        "La lista se ve fluida con LazyColumn.",
        "Necesito esto para el laboratorio de hoy.",
        "El id 101 lo devuelve el POST, no se inventa.",
        "Usa collectAsStateWithLifecycle, no collectAsState.",
        "El azul de la barra viene de Color.kt.",
        "Cambié dynamicColor a false y quedó bien.",
        "¿Cómo probaste el modo sin conexión?",
        "Con modo avión aparece el botón Reintentar.",
        "Los comentarios llegan con el id del post.",
        "Buen apunte sobre Dispatchers.IO.",
        "El ViewModel no debe importar Retrofit.",
        "Result.failure guarda la excepción original.",
        "El filtro por usuario usa @Query(userId).",
        "La ruta posts/{id} usa @Path.",
        "Con rememberSaveable se conserva el texto.",
        "Mañana lo subo a la rama main.",
        "Funciona con la API de JSONPlaceholder."
    )

    private val correos = listOf(
        "dev.movil@tecsup.edu.pe", "ana.jetpack@gmail.com", "luis.compose@outlook.com",
        "ingles.tech@hotmail.com", "karen.kotlin@gmail.com", "rafa.appdev@proton.me",
        "qa.moviles@tecsup.edu.pe", "diego.retrofit@gmail.com", "sofia.ux@icloud.com",
        "andres.coroutines@gmail.com"
    )

    fun Post.conContenidoReal(): Post = copy(
        title = titulos[(id - 1) % titulos.size],
        body = cuerpos[(id - 1) % cuerpos.size]
    )

    fun Comentario.conContenidoReal(): Comentario = copy(
        name = "Dev Móvil ${(id - 1) % 10 + 1}",
        email = correos[(id - 1) % correos.size],
        body = comentarios[(id - 1) % comentarios.size]
    )
}
