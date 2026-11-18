# PROMPTS.md: Fase 2 con IA (rama `campusgps-ia`)

Registro de los prompts importantes que usé para mejorar CampusGPS, qué respondió la IA, qué cambié o descarté y por qué.

- **Rama:** `campusgps-ia` (creada desde `main` al terminar la Fase 1)
- **Regla que seguí:** la IA propone, yo decido. Revisé cada cambio, lo probé en el emulador y comprobé que no rompiera MVVM + Repository.

## Mejoras elegidas

| Mejora | Qué logra | Capas que cambian | Commit |
|---|---|---|---|
| 1. Filtrar por categoría | Chips Todos, Estudio, Comida, Eventos y Servicios que muestran solo esos lugares en el mapa y en la lista | ViewModel + View | `feat(filtro): filtra lugares por categoria con chips` |
| 2. Radio de cercanía | Selector de 100, 200 o 500 m que dibuja un `Circle` alrededor de mi posición y marca en la lista los lugares que quedan dentro | ViewModel + View | `feat(radio): agrega radio de cercania con circulo en el mapa` |
| 3. Punto azul nativo | Activa `isMyLocationEnabled` y el botón de mi ubicación del mapa, solo si el permiso fue concedido | View | `feat(mapa): activa el punto azul nativo cuando hay permiso` |

Las mejoras 1 y 2 cambian el ViewModel, como pide la regla de la Fase 2.

## Tabla de prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Creé la rama campusgps-ia, ahora elige 3 mejoras para hacer y hazlas." | Eligió filtrar por categoría, radio de cercanía y punto azul nativo. Las implementó todas juntas en `MapaViewModel`, `MapaUbicacion`, `ListaLugares`, `PantallaMapa` y un archivo nuevo `Filtros.kt`. | Acepté las 3 mejoras, pero no el trabajo todo junto en un solo cambio. | La guía pide commits pequeños, uno por mejora. Un solo cambio con todo mezclado no deja ver qué hizo cada mejora. |
| "Quita todo lo que has hecho, inclúyeme al código la primera mejora y dame para hacer commit; luego así con las demás." | Guardó una copia, dejó la rama igual al último commit y separó el código en mejoras que se aplican una encima de otra. Comprobó que cada una compilara y que al final el código fuera el mismo que ya se había probado. | Hice un commit por mejora, probando cada una en el emulador antes del commit. | Así cada commit compila por sí solo y se puede explicar por separado en la sustentación. |
| (Mejora 1) Filtro por categoría | Puso el filtro en el ViewModel: `categoria`, `lugaresVisibles` (con `combine`) y `filtrar()`. Los chips (`FiltroCategorias`) solo dibujan y avisan con `onElegir`. | Revisé que `masCercano` se calcule desde `lugaresVisibles`, para que el verde respete el filtro: con Comida, el más cercano pasa a ser la Cafetería. También que `filtrar()` quite la selección si el lugar elegido queda oculto. | Si el filtro se hiciera dentro del Composable rompería MVVM. Además, el "más cercano" tiene que ser uno de los lugares que se ven; si no, el botón llevaría a un lugar oculto. |
| (Mejora 2) Radio de cercanía | Agregó `radios`, `radio`, `cambiarRadio()` y `dentroDelRadio` en el ViewModel; un `Circle` en `MapaUbicacion` y el texto "Dentro del radio" en `ListaLugares`. | Dejé el cálculo de qué lugares están dentro del radio en el ViewModel (`dentroDelRadio`). La lista solo recibe un `Set<Int>` de ids. Al tocar el mismo radio otra vez, se quita. | La regla de oro: ningún Composable calcula distancias. Comparar `metros <= radio` también es lógica y va en el ViewModel. |
| (Mejora 3) Punto azul nativo | Activó `MapProperties(isMyLocationEnabled = ...)` y `MapUiSettings(myLocationButtonEnabled = ...)` en el mapa. | Comprobé que solo se active si el permiso está concedido. `PantallaMapa` guarda `permisoConcedido` y lo actualiza al aceptar o negar el permiso y al pulsar el botón, por si cambió desde Ajustes. | Si `isMyLocationEnabled` se activa sin permiso, la app se cierra con `SecurityException`. El permiso es un tema de la vista (Activity), por eso no lo puse en el ViewModel. |
| (Revisión) Probar las mejoras en el emulador | Al probar con el emulador en horizontal, la IA detectó que el mapa desaparecía, porque la lista fija de 220 dp, los botones y los chips ocupaban toda la altura. Propuso poner el mapa a la izquierda y los controles a la derecha en horizontal. | Por ahora **descarté** esa mejora y dejé solo las 3 elegidas. | No era una de las 3 mejoras, y ese problema ya existía en la Fase 1. Lo anoto como pendiente para no mezclarlo con estas mejoras. |

## Qué revisé para no romper la arquitectura

- El ViewModel **no recibe Context**: sigue usando solo las interfaces `UbicacionRepository` y `LugaresRepository`.
- Los Composables (`Filtros.kt`, `MapaUbicacion`, `ListaLugares`) **solo dibujan** lo que reciben y avisan los eventos con lambdas (`onElegir`, `onClick`).
- El filtro, el radio y el "más cercano" se calculan en el ViewModel con `StateFlow` (`combine`, `map`, `stateIn`).
- La clave de Maps sigue en `local.properties`: nunca la pegué en un prompt ni en el código.
