# Prompts utilizados

> **Herramienta de IA usada en la Fase 2:** Claude (Claude Code), no Gemini. El profesor indicó que la regla de la herramienta
> se podía ignorar en este laboratorio. Rama de trabajo: `lavacampus-ia`.
>
> Estas respuestas describen lo que ocurrió en la sesión. Revísalas y ajústalas con tus propias palabras antes de entregar.

## Prompt 1 — Alcance general de la Fase 2

**Texto enviado a la IA:**
Estoy en la rama `lavacampus-ia`. Avanza todo lo de la parte con IA. Haz todo lo que pide el documento (pedidos atrasados, descuento
por volumen y repetir un pedido) con validaciones y todo. Además de esos 4 commits, quiero otros commits de mejora de interfaz: tiene
que ser tal cual a las imágenes de referencia (app de lavandería azul, con splash en degradado, pantalla de bienvenida con ilustración,
tarjetas redondeadas, barra de navegación flotante y banner de oferta), que se vea estético y que no parezca hecha por IA. Adáptalo a
mi aplicación. Tú no harás ningún commit, solo yo.

**Qué generó la IA:**
Los tres cambios funcionales (A, B y C) repartidos en DAO, Repository, ViewModel y View, pruebas unitarias del cálculo del descuento y de
la cantidad al repetir un pedido, y un rediseño completo de la interfaz en etapas: tema azul con formas redondeadas, splash con degradado,
pantalla de bienvenida con un cesto dibujado con Canvas, login con hoja blanca, barra de navegación flotante, inicio con banner de oferta,
barras de precio inferiores y las pantallas del administrador. También preparó una carpeta de etapas para que yo hiciera los commits.

**Qué tuve que corregir yo:**
- Hubo que decidir cómo repartir los cambios en commits separados aunque los archivos se tocaran en varias etapas; por eso se guardó una copia de cada etapa.
- El rediseño se revisó visualmente con capturas generadas en pruebas; en la primera versión de la ilustración de bienvenida la toalla tapaba las ranuras del cesto, el destello era un disco plano y había un hueco grande antes del título. Se corrigió.
- Una prueba de verificación contra Room tenía una aserción mal escrita (comparaba el conteo con 1 cuando había 7 líneas del servicio) y se corrigió; no era un fallo del código de la app.

**¿Respeta MVVM?:** Sí. Las vistas solo observan `StateFlow` de su ViewModel; ninguna usa DAO ni Repository. Todas las funciones nuevas se declararon primero en la interface `LavaRepository`.

---

## Prompt 2 — Pedidos atrasados en la bandeja (commit A)

**Texto enviado a la IA:**
Agrega en la bandeja del administrador una marca roja para los pedidos atrasados (fecha de entrega menor que hoy y estado distinto de
ENTREGADO y CANCELADO). La consulta va en `PedidoDao`, la función en `LavaRepository` y su implementación, y el valor se expone como
`StateFlow` en `BandejaViewModel`. La View solo lo muestra. El panel también debe contarlos. No uses XML ni librerías externas.

**Qué generó la IA:**
`PedidoDao.observarIdsAtrasados(hoy)`, `LavaRepository.observarPedidosAtrasados(hoy)` con su implementación, `BandejaViewModel.atrasados`
(conjunto de ids), `PanelViewModel.atrasados` (cantidad), borde rojo y texto "Atrasado: debía entregarse el …" en la tarjeta de la bandeja,
una alerta roja en el panel y un pedido atrasado sembrado en los datos de prueba.

**Qué tuve que corregir yo:**
Nada falló al compilar. Un detalle a tener en cuenta: el pedido atrasado de prueba solo aparece en una base nueva (al sembrar), así que
hay que reinstalar la app o usar la versión 2 de la base (ver prompt 3).

**¿Respeta MVVM?:** Sí. La fecha se compara en el DAO (texto `aaaa-MM-dd`), el ViewModel solo expone el resultado y la View lo pinta.

---

## Prompt 3 — Descuento por volumen (commit B)

**Texto enviado a la IA:**
Si el lavado supera 5 kg se aplica 10 % de descuento, visible en el resumen del pedido y guardado en el pedido. Respeta MVVM y, si cambias
una entidad, sube la versión de la base. Añade validaciones y pruebas.

**Qué generó la IA:**
La regla pura `descuentoDeLinea(unidad, cantidad, precio)` (solo servicios por KG con más de 5 kg, redondeada a céntimos), el campo
`Pedido.descuento` (base versión 2), `LineaPedido.descuento`, el cálculo de subtotal, descuento y total en `NuevoPedidoViewModel`, la
validación del descuento y la transacción en `LavaRepositoryRoom.crearPedido`, y las filas "Subtotal / Descuento por volumen / Total" en
Nuevo pedido, Detalle y Gestión. Más 8 pruebas unitarias (5 kg exactos no descuentan, 6 kg dan S/ 4.80, redondeo, prendas sin descuento, etc.).

**Qué tuve que corregir yo:**
- Se decidió guardar el subtotal de cada línea ya con el descuento, para que "ingresos por servicio" del panel coincida con el total del pedido; por eso las pantallas de detalle muestran el importe bruto de la línea y una fila aparte con el descuento.
- La primera versión de `confirmar()` armaba las líneas desde el mapa de cantidades, que podía incluir un servicio desactivado después de elegirlo; se cambió para usar solo los servicios elegidos que siguen activos.
- Al subir la versión de la base, Room falla con la base antigua del celular; se añadió `fallbackToDestructiveMigration` (modo práctica), que recrea y vuelve a sembrar los datos.

**¿Respeta MVVM?:** Sí. La regla vive en el ViewModel (y en una función pura probada). El Repository solo valida que el descuento no sea negativo ni mayor que la línea, y lo resta.

---

## Prompt 4 — Repetir un pedido (commit C)

**Texto enviado a la IA:**
Desde el historial, un botón "Pedir de nuevo" carga las mismas líneas en Nuevo pedido con los precios actuales. Cambian ViewModel y View.
Valida que el pedido sea del mismo estudiante y que los servicios sigan activos.

**Qué generó la IA:**
La ruta `nuevo_pedido?repetir={repetir}`, el botón "Pedir de nuevo" en la pestaña Historial de Mis pedidos, y en `NuevoPedidoViewModel` la
carga del pedido anterior: ignora pedidos de otro estudiante, omite los servicios inactivos, ajusta cada cantidad a las reglas actuales con
`ajustarCantidad()` y muestra un aviso con los precios actuales. Más 5 pruebas unitarias de `ajustarCantidad`.

**Qué tuve que corregir yo:**
Al hacer opcional el argumento de la ruta, el texto literal `nuevo_pedido?repetir={repetir}` ya no se podía usar como destino; hubo que cambiar
las llamadas de navegación por la función `Rutas.nuevoPedido()` y la clave del ViewModel para que cada "repetir" tenga el suyo.

**¿Respeta MVVM?:** Sí. La View solo avisa el clic y recibe el id por navegación; toda la lógica (validar dueño, omitir inactivos, ajustar
cantidades) está en el ViewModel y usa funciones que ya existían en la interface del Repository.

---

## Prompt 5 — Mejora de la interfaz (commits de diseño)

**Texto enviado a la IA:**
Mejora la interfaz para que sea como las imágenes de referencia: degradado azul, esquinas muy redondeadas, botones en forma de pastilla,
barra inferior flotante, banner de oferta con lavadora, categorías de servicios y una barra con el precio a la izquierda y el botón a la
derecha. Que se vea estético y natural.

**Qué generó la IA:**
Una paleta azul (índigo para el administrador), formas redondeadas, tipografía con jerarquía, componentes nuevos (`LavaCard`, `LavaChip`,
`BarraAccion`, `SelectorPastilla`, `EstadoVacio`, `Avatar`), ilustraciones con Canvas (cesto de ropa y lavadora), splash y bienvenida,
login rediseñado, barra inferior flotante, Inicio con banner y categorías, Detalle y Nuevo pedido con encabezado/barra de precio, y el
rediseño de Mis pedidos, Tarifas, Perfil y de todas las pantallas del administrador.

**Qué tuve que corregir yo:**
Se revisaron capturas de cada pantalla y se ajustaron: la ilustración de bienvenida (tamaño y posición), el texto del banner de oferta (se
apretaba contra el botón) y los íconos de la barra de estado (claros sobre el degradado, oscuros sobre fondos claros).

**¿Respeta MVVM?:** Sí. Es un cambio de presentación: solo se tocaron Composables y tema. Los ViewModels solo ganaron lo necesario
para la UI (`InicioViewModel.servicios`).

---

## Prompt 6 — Un solo acceso, registro y cuenta de administrador por defecto

**Texto enviado a la IA:**
Si no tengo cuenta, ¿cómo ingreso? No hay registro. Además, ¿por qué en el inicio me pregunta si soy estudiante o administrador? Debería haber
solo un apartado para iniciar sesión, con una cuenta de administrador por defecto que entre por el mismo login. Quita el texto de abajo que
dice que los datos son ficticios.

**Qué generó la IA:**
La bienvenida con dos botones ("Iniciar sesión" y "Crear cuenta") y sin el texto de datos ficticios; un único login sin selector de perfil
(`SesionViewModel.login(correo, clave)` toma el rol de la cuenta); una pantalla de registro de estudiantes con la plantilla común
`PlantillaAcceso`; y las reglas del registro en `validarRegistro()` (nombre, código, correo válido, contraseña y confirmación) con pruebas unitarias. En datos: `UsuarioDao.contarPorCorreo/contarPorCodigo/insertar`, y en el Repository
`existeCorreo`, `existeCodigo` y `crearUsuario`. El login ya no distingue mayúsculas en el correo. La cuenta de administrador
(`admin@lavacampus.test`) ya estaba en los datos iniciales y entra por el mismo login.

**Qué tuve que corregir yo:**
Había quitado el aviso de rol equivocado del login, pero el mensaje de error de una pantalla se veía en la otra (por ejemplo, un error del
registro aparecía al abrir el login); se limpia el error al entrar a cada pantalla de acceso.

**¿Respeta MVVM?:** Sí. Las vistas solo llaman a `login()` y `registrar()`; la validación y la comprobación de cuentas duplicadas están en el
ViewModel y el Repository solo guarda y consulta.

---

## Prompt 7 — Código de 6 dígitos y contraseña segura con indicador de fuerza

**Texto enviado a la IA:**
El código de estudiante solo son 6 dígitos y solo números. En la contraseña que se pida mínimo una mayúscula, un número y un carácter
especial, y que aparezca una parte que diga si es débil, media o fuerte; que solo se acepte cuando sea media o fuerte.

**Qué generó la IA:**
En `ValidacionCuenta.kt`: el código con exactamente 6 dígitos, `requisitosClave()` (mayúscula, minúscula, número, carácter especial y
largo), `fuerzaClave()` (débil, media o fuerte) y `validarRegistro()` actualizada para exigir los tres requisitos y rechazar las claves débiles.
En la pantalla de registro, el campo del código solo deja escribir números (máximo 6) y el componente `IndicadorClave` muestra una barra de
tres tramos con la etiqueta Débil, Media o Fuerte y la lista de requisitos marcándose mientras se escribe. Más 18 pruebas unitarias, y los
códigos de los estudiantes de prueba pasaron a 6 dígitos (base versión 3).

**Qué tuve que corregir yo:**
Definí cómo se calcula la fuerza para que fuera coherente con los requisitos: con menos de 6 caracteres siempre es débil, y con 6 o más se
cuentan cinco criterios (mayúscula, minúscula, número, especial y 8 o más caracteres): 0 a 2 débil, 3 a 4 media y 5 fuerte. Así una clave con los
tres requisitos y 6 caracteres ya es media. Los códigos sembrados tenían el formato anterior y hubo que cambiarlos.

**¿Respeta MVVM?:** Sí. Las reglas están en funciones puras del modelo que usa el ViewModel; `IndicadorClave` solo pinta lo que calcula el
modelo y la pantalla no decide nada.

---

## Prompt 8 — Perfil más cuidado, pantallas del administrador completas y "no puedo hacer pedidos"

**Texto enviado a la IA:**
(Con tres capturas de referencia: panel, bandeja, gestionar pedido, perfil, ajustes, servicios, nuevo servicio y clientes.) Que tenga todas las
opciones de estas pantallas pero adaptadas a mi diseño, sobre todo el perfil: mejóralo mucho, que sea más estético. Y no me deja hacer un
pedido, me dice que la lavandería no está recibiendo pedidos.

**Qué generó la IA:**
Un Perfil y unos Ajustes nuevos con encabezado de degradado con burbujas, avatar con aro blanco, etiqueta de rol, tarjetas de estadísticas con
íconos, tarjeta "Cambiar de perfil", preferencias con ícono y descripción, nota de que se guardan en el dispositivo y botón de cerrar sesión
(`PerfilComponentes.kt`). En el administrador: fecha y "N pedidos" de la semana en el panel (`observarPedidosDesde`), valores sobre las barras del
gráfico, tarjetas de la bandeja con ícono del servicio, precio y botón a la derecha, la regla y las leyendas en Gestionar pedido, lápiz de editar
en Servicios, formulario con selectores en pastilla e íconos en cajas, y clientes con flecha. Sobre el aviso de pedidos, lo investigó en el
código: el valor por defecto es "recibiendo", así que el aviso sale porque el interruptor "Recibir pedidos" del administrador estaba apagado.
Añadió un aviso claro en el Inicio del estudiante y una alerta con el botón "Abrir" en el Panel.

**Qué tuve que corregir yo:**
- La tarjeta "Cambiar de perfil" mostraba un recuadro interior por usar un degradado semitransparente sobre una sombra; se cambió por colores opacos.
- Con el lápiz extra, los nombres de las tarjetas de Servicios se partían en varias líneas; se reorganizó la tarjeta con las acciones en una fila inferior.
- Faltaba un `import` (`clip`) tras editar Gestionar pedido y el compilador lo señaló.

**¿Respeta MVVM?:** Sí. Los datos nuevos (pedidos de la semana, recepción de pedidos) llegan por `StateFlow` desde los ViewModels, y la función
nueva del Repository se declaró primero en la interface. Las vistas solo pintan y avisan clics.

---

## Prompt 9 — Perfil, menú lateral y configuración iguales a la referencia

**Texto enviado a la IA:**
(Con una captura de referencia de tres pantallas verdes: perfil, menú lateral y ajustes.) Que el perfil sea de color entero. Mejor adáptalo tal
cual a la imagen: hazlo igual y adáptalo a mi aplicación; en vez de la foto que tenga un ícono de una personita.

**Qué generó la IA:**
- **Perfil** a color completo (azul para el estudiante, índigo para el administrador, también en modo oscuro): flecha y engranaje arriba, el nombre
  como título, avatar blanco con un ícono de persona y aros decorativos dibujados con Canvas, el botón del lápiz para editar, los datos con etiqueta
  en mayúsculas pequeñas, una barra blanca de nivel con una insignia y cinco estrellas (una por cada 2 pedidos) y la barra de actividad.
- **Configuración** con secciones (Cuenta, Notificaciones y Más), filas con flecha o interruptor, la luna de arriba para el modo oscuro y
  "Cerrar sesión" con la puerta al final. La usan el estudiante (desde el engranaje) y el administrador (pestaña Ajustes).
- **Menú lateral** con cabecera de color, avatar y una onda blanca, opciones con una barra en la activa, "Cerrar sesión" y un botón de inicio; se
  abre con el botón de menú de Inicio y del Panel o deslizando.
- **Editar perfil** (cambia el nombre) y **Cambiar contraseña** con validaciones (`validarNombre`, `validarCambioClave`) y funciones nuevas en el
  DAO y el Repository, para que el lápiz y las filas de Cuenta hagan algo real. Más 9 pruebas unitarias.

**Qué tuve que corregir yo:**
- La referencia tiene un lápiz para cambiar la foto; como la app no tiene fotos, se hizo que edite el nombre (el código y el correo no se
  pueden cambiar).
- El nombre se mostraba desactualizado después de editarlo porque los ViewModels del Inicio y del Perfil guardaban el usuario original; se
  incluyó el nombre en su clave para que se recreen al cambiar.
- Una intención previa de mostrar las iniciales en el avatar se cambió por el ícono de persona, como se pidió.

**¿Respeta MVVM?:** Sí. Las vistas solo llaman a `editarNombre()` y `cambiarClave()` de `SesionViewModel`; la validación está en funciones puras
del modelo y el Repository solo guarda (la función nueva se declaró primero en la interface `LavaRepository`).

---

## Prompt 10 — Pantalla de configuración idéntica a la referencia

**Texto enviado a la IA:**
(Con la captura de la pantalla "Settings" verde.) Esta parte de configuración hazla tal cual: la ubicación de los textos, el mismo tipo de letra y
la misma ubicación que está en el medio. Además usa los mismos colores.

**Qué generó la IA:**
Se midió la imagen (color del fondo, márgenes y separación entre filas) y se rehízo `ConfiguracionScreen`: fondo verde plano `#319E74`, titulo con la
flecha y la luna, una lista centrada (márgenes de ~14 % a cada lado) con tres secciones (Cuenta, Notificaciones y Más), filas pegadas al borde
izquierdo con la flecha pequeña a la derecha, un interruptor mínimo de línea y punto (`ConfiguracionComponentes.kt`) y "Cerrar sesión" con la puerta.
Las opciones se adaptaron a la app (Editar perfil, Cambiar contraseña, Cambiar a la otra vista, aviso de pedido listo o pedidos nuevos y
recepción, y accesos a Mis pedidos y Tarifas, o a Servicios y Clientes). Para la letra se descargó **Poppins** (licencia libre SIL OFL 1.1) con permiso
del usuario y se usa solo en esta pantalla.

**Qué tuve que corregir yo:**
- La letra de la referencia no viene con Android, así que hubo que pedir permiso antes de descargarla y agregarla en `res/font`.
- En la primera versión las filas estaban sangradas bajo el título de cada sección; en la referencia empiezan en el mismo borde que el ícono, y se corrigió.
- La referencia tiene dos interruptores en Notificaciones; el estudiante solo tiene uno real (aviso de pedido listo) y el administrador tiene dos
  (pedidos nuevos y recepción de pedidos), así que no se inventaron opciones que no hacen nada.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación; los interruptores llaman a las mismas funciones de `SesionViewModel` y la pantalla no
decide nada.

---

## Prompt 11 — Verde en toda la app, barra inferior de tres íconos y opciones nuevas en "Más"

**Texto enviado a la IA:**
(Con capturas de la app y un recorte de la barra inferior verde.) En la parte de configuración, en "Más", que aparezca otra cosa que no sea
tarifas ni pedidos. Usa el color verde en toda la aplicación. Como las opciones ya están en el menú lateral, el menú de abajo debe tener solo tres
íconos como el de la imagen: una estrella, la casita para el inicio y la personita para el perfil.

**Qué generó la IA:**
- Una sola paleta verde (`#319E74`) en `Color.kt` y `Theme.kt` para estudiante y administrador (se quitó el índigo del administrador), con su
  versión para modo oscuro; el splash, la bienvenida, el banner de oferta, las ilustraciones y los gráficos se pasaron al verde.
- `BarraInferior` nueva: barra verde con tres círculos; los inactivos son blancos y el activo sube sobre un bulto curvo con aro blanco. La estrella
  lleva a las tarifas y descuentos (estudiante) o a los pedidos por atender (administrador). Las demás pantallas se abren desde el menú lateral y la
  barra sigue visible en ellas.
- "Más" ahora tiene "Cómo funciona" y "Acerca de LavaCampus", que abren un diálogo informativo.

**Qué tuve que corregir yo:**
- El usuario no sabía qué significaba la estrella de la referencia, así que decidí su destino (tarifas) y lo documenté.
- El estado "En proceso" seguía azul; se cambió a un turquesa para que no desentone.
- La barra inferior tapaba la navegación del administrador a Servicios, Clientes y Ajustes; se mantuvo visible en esas pantallas y el acceso queda en el menú lateral.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación y la navegación; los ViewModels y repositorios no se tocaron.

---

## Prompt 12 — Tarifas con tarjetas grandes, perfil más claro y menú sin Tarifas

**Texto enviado a la IA:**
(Con capturas de Tarifas y Perfil en el celular.) Mejora esta parte: que esté en tarjetas más grandes que ocupen la pantalla y con imágenes. En el
perfil, que el código de estudiante, el nombre completo y el correo estén resaltados y con la letra un poco más grande; que el nivel de cliente y
mi actividad tengan mejor diseño. En el menú lateral que ya no aparezca Tarifas, y que al iniciar la aplicación no se abra directamente el menú.

**Qué generó la IA:**
- `TarifasScreen`: cada servicio es una tarjeta alta que se reparte el alto de la pantalla, con un panel verde a la izquierda (la lavadora dibujada
  o el icono del servicio en grande), el nombre, la entrega, una etiqueta con el descuento y el precio en grande.
- `PerfilComponentes`: `DatoPerfil` pasó a ser una tarjeta translúcida con icono y letra de 19 sp; `BarraNivel` ahora muestra el nombre del nivel,
  cinco estrellas grandes y una barra con lo que falta para la siguiente estrella; `BarraActividad` son tres tarjetas con icono, valor y leyenda.
- Se quitó "Tarifas" del menú lateral (sigue en la estrella de la barra inferior) y el menú se fuerza a estar cerrado en las pantallas sin barra.

**Qué tuve que corregir yo:**
- No pude reproducir el menú abierto al iniciar (en el código no hay nada que lo abra); se agregó la protección de cerrarlo y queda pendiente
  confirmar en el celular.
- El nombre del nivel y el avance son una regla nueva (una estrella cada 2 pedidos, ya existente), solo se mostró mejor; no se inventaron datos.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación; la lógica de estrellas sigue en la función pura `estrellasPorPedidos`.

---

## Prompt 13 — Menú lateral cerrado al iniciar sesión y confirmación al registrarse

**Texto enviado a la IA:**
Cuando se inicia sesión directamente se abre el menú lateral, y no tiene que ser así. Además, cuando se registre alguien, que le salga un mensaje
de confirmación bonito con el logo y que le mande al login.

**Qué generó la IA:**
- Causa del menú abierto: mientras no había sesión, el contenido del menú lateral estaba vacío (ancho 0) y al iniciar sesión pasaba a medir 310 dp;
  el cajón de Material calculaba sus posiciones con el ancho viejo y quedaba abierto. Ahora, sin sesión, se compone un panel vacío pero de 310 dp
  (`ModalDrawerSheet`), de modo que el cajón conoce su tamaño desde el inicio.
- `SesionViewModel.registrar` ya no inicia sesión: guarda el nombre en `cuentaCreada` y la pantalla muestra `ConfirmacionRegistroScreen` (logo,
  palomita con rebote, mensaje con el nombre y botón "Ir a iniciar sesión"). Al salir se limpia el estado y se navega al login.

**Qué tuve que corregir yo:**
- La primera protección que agregué (cerrar el menú en pantallas sin barra) no atacaba la causa real; se explicó y se corrigió con el ancho fijo.
- La cuenta recién creada ya no entra sola a la app, así que se quitó el efecto que navegaba al detectar un usuario en `RegistroScreen`.

**¿Respeta MVVM?:** Sí. El estado de "cuenta creada" vive en el ViewModel y la pantalla solo lo muestra.

---

## Prompt 14 — Encabezado del inicio

**Texto enviado a la IA:**
(Con la captura del Inicio.) Esta parte no me gusta, se ve muy hecha por IA. El botón del menú lateral que esté a la izquierda, donde dice
"Hola, nombre" que esté a la derecha, y que no aparezca el código de alumno ahí.

**Qué generó la IA:**
En `InicioScreen` el encabezado quedó con el botón del menú a la izquierda y el saludo ("Hola, Jordan") con el avatar a la derecha. Se quitó el
código de estudiante del saludo; sigue visible en el perfil y en el menú lateral.

**Qué tuve que corregir yo:**
La frase "se ve muy IA" no dice qué parte es; se interpretó como el encabezado, que es lo que el usuario describió después, y se dejó el resto de la
pantalla igual a la espera de más indicaciones.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación.

---

## Prompt 15 — Modo oscuro en perfil, configuración y menú lateral

**Texto enviado a la IA:**
Cuando coloco el modo oscuro creo que todo debería estar en oscuro, ¿no? Porque cuando cambio a modo oscuro la parte de perfil y configuración se
queda verde.

**Qué generó la IA:**
El perfil, la configuración y el menú lateral tenían colores fijos (verde y blanco) que no seguían el tema. Se agregaron en `PerfilComponentes.kt`
`esOscuro()`, `colorMarca`, `colorTarjetaPerfil()` y `fondoPerfil()` que cambian según el modo: de día verde y tarjetas blancas, de noche un fondo
verde casi negro con tarjetas oscuras y el verde aclarado. `fondoConfig()`, el perfil del administrador y `MenuLateral` usan esos mismos colores.

**Qué tuve que corregir yo:**
- El primer resultado dejaba el círculo del avatar del menú lateral en blanco sobre el fondo oscuro; se pasó a la tarjeta oscura.
- En modo oscuro los textos sobre el verde claro (insignia de pedidos, íconos de actividad) pasan a oscuro para mantener el contraste.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación; la preferencia de modo oscuro sigue en `SesionViewModel` y las pantallas solo la leen.

---

## Prompt 16 — Gráficos del panel más estéticos

**Texto enviado a la IA:**
(Con la captura de "Pedidos por estado" e "Ingresos por servicio".) Mejora eso para que se vea más estético, o usa otra forma de representarlo.

**Qué generó la IA:**
Se cambió la forma de representar los datos en `GraficosPanel.kt`: "Pedidos por estado" ahora muestra el total en grande, una barra apilada con un
tramo de color por estado y una cuadrícula de cuatro tarjetas con la cantidad de cada uno; "Ingresos por servicio" muestra el total de la semana y
una fila por servicio con su monto, su porcentaje y una barra horizontal proporcional. Se eliminaron `GraficoDona.kt` y `GraficoBarras.kt`, que ya
no se usaban.

**Qué tuve que corregir yo:**
- En el primer intento el comando para crear el archivo falló por un problema de comillas en la terminal y no cambió nada; se repitió escribiendo el
  archivo directamente.
- Se dejó una barra mínima (2 %) para que un servicio con poco ingreso no desaparezca visualmente.

**¿Respeta MVVM?:** Sí. `PanelViewModel` sigue entregando los mismos datos; solo cambió cómo se dibujan.

---

## Prompt 17 — Gráficos del panel en escala de verdes

**Texto enviado a la IA:**
(Con una imagen de "Paleta Color Verde" de cinco tonos.) Los colores de eso que añadiste que sea en escala de verdes.

**Qué generó la IA:**
Los colores de las gráficas del panel pasaron a la escala de verdes de la imagen (`#A7B08E`, `#637757`, `#435B45` y un tono más oscuro `#2E4237`).
Los estados van del verde más claro (recibido) al más oscuro (entregado), los servicios usan la misma escala y los contadores de arriba también. En
`GraficosPanel.kt` los verdes se aclaran en modo oscuro para que no se pierdan sobre la tarjeta.

**Qué tuve que corregir yo:**
- La paleta tiene cinco tonos y los estados son cuatro; elegí los más separados y agregué un tono oscuro propio para no repetir colores.
- Los chips de estado del resto de la app (por ejemplo "En proceso" en turquesa) no se tocaron: el usuario habló solo de lo agregado en el panel.

**¿Respeta MVVM?:** Sí. Los colores se definen en `PanelViewModel` junto con los datos y las pantallas solo los dibujan.

---

## Prompt 18 — La casita no volvía al inicio

**Texto enviado a la IA:**
Cuando estoy en la parte de pedidos y quiero volver a inicio presiono la casita pero no me deja; creo que tengo que reiniciar la aplicación para que
me deje.

**Qué generó la IA:**
En `NavLava.kt`, `irAPestana` navegaba a la pantalla principal con `launchSingleTop` y `restoreState`, y cuando Inicio ya estaba en la pila debajo de
Mis pedidos el toque no tenía efecto. Ahora, si el destino es la pantalla principal, se hace `popBackStack(raiz, inclusive = false)`, que regresa a
la que ya está en la pila; solo si no estuviera se navega como antes.

**Qué tuve que corregir yo:**
- No pude reproducir el fallo en un celular; el arreglo se basa en cómo se arma la pila de navegación y queda pendiente confirmarlo en el dispositivo.

**¿Respeta MVVM?:** Sí. Solo cambió la navegación; los ViewModels no se tocaron.

---

## Prompt 19 — Tarifas más estéticas

**Texto enviado a la IA:**
Mejora la parte de tarifas, se ve raro, que sea más estético.

**Qué generó la IA:**
El rediseño anterior estiraba cada tarjeta hasta ocupar la pantalla y dejaba un hueco grande en el medio. Ahora `TarifasScreen` muestra tarjetas de
altura natural: un cuadro verde de esquinas redondeadas con la imagen del servicio, en el centro el nombre, la entrega con un reloj y la etiqueta del
descuento, y a la derecha el precio. Debajo hay una sección "Lo que debes saber" con tres filas con ícono (horario, mínimo por lavado y pedidos
activos), y toda la pantalla se desplaza si no cabe.

**Qué tuve que corregir yo:**
- Se verificó que el texto del descuento ("Más de 5 kg: 10 % menos") coincide con la regla real de `descuentoDeLinea` (solo si pasa de 5 kg); un
  cambio que había hecho a "5 kg o más" era incorrecto y se revirtió.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación; `TarifasViewModel` sigue igual.

---

## Prompt 20 — Seguimiento del pedido como la referencia y colores turquesa en toda la app

**Texto enviado a la IA:**
(Con la captura "Track Order" de una app de lavandería y mi detalle de pedido.) Hazlo como la primera imagen, la parte del detalle de pedido, y
adapta los colores también a toda la aplicación.

**Qué generó la IA:**
- Se tomó el color de la referencia con un muestreo de píxeles (`#46B0C0`) y se definió una paleta turquesa (`Color.kt` y `Theme.kt`, con su versión
  oscura). Los colores se renombraron de `Verde*` a `Turquesa*` y se actualizaron las ilustraciones, los gráficos del panel y la configuración.
- `DetallePedidoScreen` se rehízo: encabezado de color con los cuatro pasos en fila (recibido, en proceso, listo y entregado) unidos por una línea,
  un círculo grande con la ilustración del estado, el título y el mensaje, la lista "Detalles del pedido" (servicios, descuento, total, fechas,
  casillero y pago) y el botón de cancelar abajo.
- Segunda pasada: el usuario pidió usar la misma imagen y ocupar el mismo ancho y alto. Se dibujó con Canvas (`IlustracionLavadoraLinea`) una lavadora
  de trazo fino con burbujas como la de la referencia, el círculo se agrandó a unos 204 dp y la hoja blanca ocupa todo el alto de la pantalla.

**Qué tuve que corregir yo:**
- El círculo grande salía recortado porque la hoja blanca (`Surface`) recorta a sus hijos; se cambió por un contenedor con fondo para que sobresalga.
- Los estados "Listo" y los montos de descuento siguen en verde a propósito: el verde indica algo bueno y no se mezcla con el turquesa de la marca.
- El compilador de Kotlin falló una vez por un permiso del sistema y se resolvió compilando en el mismo proceso.

**¿Respeta MVVM?:** Sí. Solo cambió la presentación; `DetallePedidoViewModel` y la regla de cancelar no se tocaron.

---

## Reflexión

- **¿Qué mejoró la IA y qué no?** Aceleró mucho el trabajo repetitivo (consultas, ViewModels, pantallas) y propuso una estructura limpia; lo que no resolvió sola fue decidir las reglas de negocio ambiguas (por ejemplo, dónde guardar el descuento para que los ingresos cuadren).
- **¿Qué error cometió?** Una aserción incorrecta en una prueba y una primera versión de `confirmar()` que podía incluir un servicio inactivo; ambos se detectaron revisando y probando, no al compilar.
- **¿Qué aprendí sobre MVVM, Room y reglas de negocio?** Que cambiar una entidad obliga a subir la versión de la base y a pensar en los datos existentes, que una regla pura (sin Android) se prueba rápido con pruebas unitarias, y que el Repository debe validar pero no decidir las reglas.
- **¿Cómo mantuve la arquitectura?** Declarando primero cada función en la interface `LavaRepository`, dejando las reglas en los ViewModels y las Views solo observando estados.
- **¿Qué haría distinto?** Escribiría las pruebas de la regla antes de implementarla y revisaría cada pantalla en un dispositivo real, además de las capturas.
