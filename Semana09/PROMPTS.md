# PROMPTS.md · Fase 2 (rama murocampus-ia)

Mejoras elegidas (mínimo 3, al menos una cambia la API y el Repository):

| Mejora | Qué logra | Capas que cambian |
| --- | --- | --- |
| 1. Encabezado con interceptor | Un `Interceptor` de OkHttp agrega `Accept: application/json` a todas las peticiones | RetrofitClient |
| 2. Búsqueda | Campo *Buscar por título* que filtra las publicaciones ya descargadas, sin volver a llamar a la red | ViewModel + View |
| 3. Borrar publicación | Botón *Eliminar* en el detalle con confirmación; `@DELETE("posts/{id}")` devuelve `Response<Unit>` y se revisa el código HTTP | API + Repository + ViewModel + View |

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Agrega un Interceptor de OkHttp que ponga el encabezado Accept: application/json en todas las peticiones» | Un `Interceptor` que copia la petición con `newBuilder().header(...)` y lo agrega al `OkHttpClient` | Lo puse **antes** del `HttpLoggingInterceptor` | Así el Logcat muestra la petición ya con el encabezado; si va después, el log no lo muestra |
| «Agrega una búsqueda por título que filtre las publicaciones ya descargadas» | Proponía filtrar la lista dentro del Composable con `remember` | Moví el filtro al ViewModel con `combine(_lista, _busqueda)` y `stateIn` | La pantalla solo debe dibujar el estado; la lógica va en el ViewModel y así sobrevive a la rotación |
| «Agrega un botón Eliminar en el detalle con @DELETE y Response<Unit>» | `@DELETE("posts/{id}") suspend fun borrar(...): Response<Unit>` y el manejo del código en el Repository | Agregué un `AlertDialog` de confirmación y, si el código no es exitoso, el Repository lanza `HttpException(respuesta)` dentro de `llamar { }` para devolver `Result.failure` | El ViewModel no debe conocer `HttpException` ni Retrofit; recibe solo `Result` y muestra `mensajeDe(it)` |
| «JSONPlaceholder no borra de verdad, ¿cómo lo reflejo?» | Volver a cargar la lista después del DELETE | Descartado: quito la publicación de la lista local del ViewModel y muestro *Publicación N eliminada (DELETE respondió 200)* | Si recargo, el servidor devuelve otra vez las 100 y la publicación "reaparece" |

Revisión de reglas: el ViewModel no importa Retrofit, PostApi, HttpException ni Context; ningún Composable llama a la API; no hay `catch` vacío ni `GlobalScope`; la URL sigue siendo `https://`.
