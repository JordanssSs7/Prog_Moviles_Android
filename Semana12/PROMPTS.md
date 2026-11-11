# PROMPTS.md — Fase 2 con IA (rama `minecraft-ia`)

Aquí están los pedidos que se le hicieron a la IA (Claude Code) para mejorar la app de la Fase 1.
Los textos están **resumidos y ordenados a partir de la conversación real**: se limpiaron para que se
entiendan, pero cada uno corresponde a algo que se pidió de verdad. La Fase 1 está en la rama `main`.

Para cada prompt se indica **qué se pidió**, **qué se obtuvo** y **cómo se comprobó**.

---

## Prompt 1 — Crear la rama y el estilo general

> Crea la rama `minecraft-ia` y comienza con la parte con IA. Quiero que la app sea un poco animada,
> parecida a la imagen de referencia (un menú de juego), pero con tema de Minecraft. Los botones pueden
> ser como bloques de pasto. Ahora sí agrega **registro**: cuando alguien se registre, debe mandarlo al
> login para que entre con la cuenta que acaba de crear. A los usuarios ponles la cabeza de jugador.
> Agrega más cosas para interactuar dentro de la app que usen la API.

**Qué se obtuvo:** rama `minecraft-ia`; pantalla de registro (jugador, correo, contraseña) que crea la
cuenta con rol `usuario` y guarda `usuarios/{uid}` en Firestore; cabezas de jugador (mc-heads.net);
categorías, búsqueda, ítem al azar, **receta de crafteo** en el detalle (tercer endpoint), **quiz** de
"cuántos caben en una pila" y pantalla de **perfil**.
**Cómo se comprobó:** la app compila y las pruebas del ViewModel de sesión cubren el registro (campos
válidos, contraseñas distintas, correo repetido).

## Prompt 2 — Inicio con dos botones y fondo con imagen

> En el inicio deja solo dos botones, en una misma fila: Iniciar sesión y Registrarse (sin Jugar ni
> Créditos). Que el título no se mueva. Cambia el fondo con la imagen que te paso, un poco oscurecida, y
> adapta los colores de los botones y de toda la app a esa imagen. Mejora el login: está muy básico.

**Qué se obtuvo:** paleta sacada de la imagen, botones por tipo (principal, secundario, peligro) y un
login con aspecto de inventario de Minecraft.

## Prompt 3 — Seguir una referencia de diseño

> Adapta mejor esta referencia (app "Pets Co"). Quita los iconos de Google, Facebook y Apple. Que diga
> "Bienvenidos a Minecraft Catálogo" y usa la imagen de Steve. Que sea bonito.

**Qué se obtuvo:** inicio con texto de bienvenida, Steve y los dos botones; login y registro con tarjeta
redondeada, campos con ícono y botón de volver.

## Prompt 4 — Fondo blanco y paleta verde

> Usa el mismo estilo de la referencia: fondo blanco, y cambia los colores a verdes (#A3B18A, #DAD7CD,
> #588157, #3A5A40, #344E41). En la segunda y tercera parte pon imágenes de Minecraft.

**Qué se obtuvo:** tema claro con esos cinco verdes aplicado a toda la app y botones redondeados.

## Prompt 5 — Ubicaciones exactas según las imágenes

> Hazlo tal cual a las imágenes: en el inicio Steve más grande, sin círculo verde y sin objetos
> flotando. En el login la tarjeta de datos va abajo, y arriba el creeper combinado con el fondo en
> degradado. En el registro la tarjeta también abajo; pon la imagen de Steve, Alex y el cerdo; quita el
> cuadrito junto a "Crear cuenta"; separa más los campos y haz más bonito el botón de volver.

**Qué se obtuvo:** imágenes recortadas del fondo (el cuadriculado "transparente" venía incluido en la
imagen), degradados hacia el blanco, tarjeta anclada abajo y botón de volver redondo.

## Prompt 6 — Ajustes del registro

> En el registro juega con los degradados y la opacidad. Mejor pon esta otra imagen (el portal con Steve
> y Alex) y hazla más grande, como en el login. Pon arriba un texto que diga "¿Eres nuevo?"; el texto
> chiquito se ve mal y creo que hay algo tapado detrás de la tarjeta.

**Qué se obtuvo:** imagen grande con degradado; el texto "¿Eres nuevo?" sobre un fondo que lo hace
legible; la tarjeta con fondo blanco sólido para que la imagen no se vea a los lados.

## Prompt 7 — Tipografía

> Cambia los tipos de letra a algo estilo Minecraft donde dice "Minecraft Catálogo". Si no queda bien el
> logo, quítalo y cambia la letra de todo el inicio, el login y el registro por algo que combine.

**Qué se obtuvo:** se descartó el logo en imagen y se usó la letra condensada gruesa del sistema, en
mayúsculas y con sombra dura. No se descargó ninguna fuente.

## Prompt 8 — Mensaje de confirmación

> Cuando me registre y cuando entre con login, que salga un mensaje de confirmación en pantalla con un
> check, usando esta imagen (zombi montado en una gallina).

**Qué se obtuvo:** tarjeta de confirmación con el check animado que se cierra sola. Solo aparece al
iniciar sesión con la clave, no al reabrir la app con la sesión ya guardada.
**Cómo se comprobó:** pruebas de que el aviso se emite una sola vez.

## Prompt 9 — Imágenes, volver, español y orden

> ¿Por qué algunos ítems no tienen imagen? El botón para retroceder en el detalle no se ve. Todo el
> texto sale en inglés. Quiero que se vea más ordenado.

**Qué se obtuvo:** más fuentes de imagen como respaldo (íconos de la wiki); flecha de volver blanca;
nombres en español desde el archivo de idioma del juego (cubre los 1255 ítems) y descripciones desde la
wiki en español con respaldo en inglés; tarjetas de lista y de detalle ordenadas.
**Cómo se comprobó:** se verificó que los 1255 ítems tienen nombre en español y que las imágenes
faltantes existen en la wiki.

## Prompt 10 — Barra de navegación y perfil

> Mejora el navbar, parecido a este video de Pinterest. Mejora todo en general para que se vea más
> estético. Que el perfil se vea mucho mejor y tenga que ver con Minecraft, pero estético.

**Qué se obtuvo:** barra flotante en forma de píldora donde la pestaña elegida se expande con su nombre;
encabezados con degradado; perfil con banner de bloques, jugador de cuerpo entero y estadísticas
(ítems guardados y favoritos).

## Prompt 11 — Más funciones para el admin

> ¿Qué puede hacer el admin? … Sí, agrégale cosas: ver la lista de usuarios registrados y cuáles son
> los ítems más marcados como favoritos.

**Qué se obtuvo:** el Panel Admin muestra el top de ítems favoritos de todos los usuarios y la lista de
usuarios registrados. Para eso los favoritos también se publican en Firestore (`favoritos/{uid}_{itemId}`)
y se actualizaron las reglas: solo el admin puede leer todo y cada persona escribe solo lo suyo.
**Dónde está el límite:** los contadores de arriba siguen siendo locales a cada celular.

## Prompt 12 — Aclarar un mensaje de error

> El mensaje "El jugador debe tener de 3 a 16 letras, números o _" se lee cortado, ¿qué quiere decir?

**Qué se obtuvo:** mensaje más claro: de 3 a 16 caracteres, solo letras sin tildes, números o guion bajo,
sin espacios.

## Prompt 13 — Revisión contra el enunciado

> ¿Cumple con todo? (tabla de requisitos R01–R13 y diagrama de arquitectura del laboratorio).

**Qué se obtuvo:** una revisión requisito por requisito. Se encontró un hueco en R08: al abrir la app sin
internet y con datos guardados no salía ningún aviso. Se agregó la interfaz `Conectividad` (para que el
ViewModel no conozca `Context`) y una franja "Sin conexión a internet. Mostrando lo guardado.".
**Cómo se comprobó:** dos pruebas nuevas (con y sin internet) y revisión de que ningún composable ni
ViewModel toca Context, Retrofit, Room ni Firebase.

## Prompt 14 — Documentación y control de versiones

> Arma el documento de evidencias con las capturas que debo tomar (primero la rama `main`, luego
> `minecraft-ia`) y dame la lista de commits de la rama con IA.

**Qué se obtuvo:** plantilla del documento Word con las capturas entre llaves y la lista de 13 commits.

---

## Resultado final de la Fase 2

- La arquitectura MVVM de la Fase 1 se mantuvo: los ViewModels hablan solo con interfaces
  (`ItemRepository`, `AuthRepository`, `ComunidadRepository`, `Notificador`, `Conectividad`).
- Room sigue siendo la única fuente de verdad (versión 3 de la base, con migraciones que no pierden favoritos).
- 17 pruebas unitarias en verde.

## Lo que revisé y decidí yo

_(Completa con tus palabras antes de entregar. Son preguntas guía, no respuestas.)_

- ¿Qué cambio de la IA tuviste que corregir o pedir de nuevo, y por qué?
- ¿Qué decisión de seguridad tomaste tú (roles, reglas de Firestore, qué datos van a la nube)?
- ¿Qué parte del código podrías explicar línea por línea en la sustentación?
