# PROMPTS.md: Fase 2 con IA (rama `centrotareas-ia`)

Aquí registro los prompts importantes que usé para mejorar CentroTareas. Para cada uno anoto qué respondió la IA, qué cambié o descarté y por qué.

- **Rama:** `centrotareas-ia`, adelantada hasta `main` al terminar la Fase 1.
- **Regla que seguí:** la IA propone y yo decido. Revisé cada cambio, lo probé en el emulador, comprobé que no rompiera MVVM + Repository e hice un commit por mejora.

## Mejoras elegidas

| Mejora | Qué logra | Capas que cambian | Commit |
|---|---|---|---|
| 1. Progreso desde el Repository | La tarea larga sale del ViewModel. El Repository la expone como `Flow<Float>` con `flowOn(Dispatchers.Default)` y el ViewModel solo la recolecta. | Repository + ViewModel | `feat(progreso): mueve la tarea al Repository y expone el progreso como Flow` |
| 2. Tiempo máximo con `withTimeout` | Un interruptor "Tiempo máximo 4 s" limita las cargas. Si una carga tarda más, se cancela y se muestra "Tardó demasiado". | ViewModel + View | `feat(timeout): limita las cargas con withTimeout y muestra error si tardan` |
| 3. Comparación de tiempos | Una tarjeta nueva muestra, por modo, cuántas veces se usó, el promedio y el último tiempo. También indica cuánto ahorra "A la vez" y tiene un botón Limpiar. | Model + ViewModel + View | `feat(comparacion): acumula y compara los tiempos de las dos cargas` |

Las tres mejoras cambian el ViewModel, y la Mejora 1 cambia además el Repository, como pide la regla de la Fase 2.

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Bien, ahora haz la parte de IA, ya estoy en la rama centrotareas-ia." | Revisó la rama y vio que `centrotareas-ia` se había creado desde el commit del LAB13, así que le faltaban los 9 commits del LAB14. Como la rama no tenía commits propios, la adelantó hasta `main` con `git merge --ff-only`. Luego eligió 3 mejoras. | Acepté adelantar la rama y las 3 mejoras. Pedí aplicarlas una por una, con un commit mío después de cada una. | Sin el código de la Fase 1, las mejoras no tenían sobre qué trabajar. `--ff-only` no pierde nada porque solo mueve la rama hacia adelante. Un commit por mejora permite explicar cada una por separado. |
| (Mejora 1) Mover la tarea con progreso al Repository | Agregó `fun progresoTarea(): Flow<Float>` a la interface. En `TareasRepositoryLocal` usó `flow { emit(i / 100f) }` con `.flowOn(Dispatchers.Default)`. `tareaLarga()` ahora solo hace `collect` y actualiza `_progreso`. | Mantuve el `Job`, el `finally` y el log `HILO` para seguir viendo `main` / `DefaultDispatcher-worker-1` / `main`. Quité del ViewModel los imports que ya no se usaban (`Dispatchers`, `delay`, `withContext`). | La regla de MVVM dice que el ViewModel no hace el trabajo pesado. Con `flowOn` el Flow emite en `Default`, pero se recolecta en el hilo del ViewModel. Además, la tarea bajó de unos 5,5 s a unos 4,3 s, porque ya no cambia de hilo 100 veces con `withContext`. |
| (Mejora 2) Tiempo máximo con `withTimeout` | Envolvió el bloque de carga en `withTimeout` y agregó un `catch (e: TimeoutCancellationException)` que muestra un error. | Al principio el límite era de 2 s. **Lo cambié a 4 s.** Revisé que el `catch` del timeout vaya **antes** del `catch (e: Exception)` que vuelve a lanzar la `CancellationException`. | Con 2 s fallaban los dos modos. Con 4 s se ve la diferencia: "Una por una" (5 s) falla y "A la vez" (3 s) termina bien. `TimeoutCancellationException` es una `CancellationException`, así que si se revisara después, se volvería a lanzar y la pantalla se quedaría en "Cargando". |
| (Mejora 3) Comparación de tiempos | Creó `model/Comparacion.kt` con `RegistroTiempo` (veces, total, último y promedio). Agregó `tiempos: StateFlow<Map<String, RegistroTiempo>>`, `registrarTiempo()` y `limpiarTiempos()` al ViewModel, y una tarjeta nueva en la pantalla. | Puse `RegistroTiempo` en un archivo nuevo en lugar de editar `Estados.kt`. Solo se guardan las cargas exitosas, no los errores ni los tiempos agotados. | El cálculo del promedio y del ahorro es lógica, así que no va en el Composable: la vista solo muestra los datos. Si se mezclaran los errores, el promedio dejaría de ser una comparación justa. |
| "Listo" (después de cada mejora) | Después de cada commit, aplicó la mejora siguiente y la probó en el emulador. Revisó el `catch` del timeout, el registro de tiempos y el botón Limpiar. | Hice yo los commits. No hice push hasta terminar. | Cada commit compila por sí solo y está probado antes de seguir. |

## Qué revisé para no romper la arquitectura

- El ViewModel **no recibe Context**: sigue usando solo la interface `TareasRepository`.
- El trabajo pesado está en el Repository (`progresoTarea`, `contarPrimos`). El ViewModel solo lanza corrutinas en `viewModelScope` y actualiza `StateFlow`.
- Los Composables **solo dibujan** el estado y avisan los eventos (`cambiarTiempoMaximo`, `limpiarTiempos`).
- Ninguna `CancellationException` se queda atrapada: el timeout se maneja antes y las demás se vuelven a lanzar.
- `git grep` no encuentra `GlobalScope`, `runBlocking` ni `Thread.sleep` en los `.kt`.
