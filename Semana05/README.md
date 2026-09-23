Laboratorio 05
--------------

Una aplicación móvil para un Portal Académico institucional, orientada a la gestión estudiantil. La app simula un entorno universitario donde los usuarios pueden iniciar sesión institucional, visualizar un directorio de alumnos, consultar expedientes académicos detallados y gestionar la configuración de su perfil personal.


Promt utilizado para tener la interfaz igual a la del documento planteado:
-------------------------------------------------------------------------

Eres un Desarrollador Senior de Android experto en Jetpack Compose. Tu tarea es generar el código UI completo de 5 pantallas.
REGLA DE ORO 1: ESTÁ ESTRICTAMENTE PROHIBIDO inventar textos, campos o funcionalidades que no se pidan. Usa EXACTAMENTE los textos indicados.
REGLA DE ORO 2: ESTÁ ESTRICTAMENTE PROHIBIDO omitir los íconos. Debes importar y usar androidx.compose.material.icons.filled.* en TODAS las pantallas.
REGLA DE ORO 3: No uses comentarios de relleno como // resto del código. Genera todo el código de inicio a fin para que compile directamente.

[CONFIGURACIÓN DEL PROYECTO]

Paquete base: com.reyes.laboratorio05 (Asegúrate de usar las rutas correctas como com.reyes.laboratorio05.screens y com.reyes.laboratorio05.navigation).

Navegación: Usa NavHost y rememberNavController(). El cierre de sesión en cualquier pantalla debe limpiar la pila: navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }.

[COLORES OBLIGATORIOS - DEFÍNELOS EN CADA ARCHIVO]

MoradoPrincipal = Color(0xFF6750A4)

MoradoOscuro = Color(0xFF4A3479)

FondoLila = Color(0xFFF3EDF7)

FondoPantallaBlanco = Color(0xFFFFFFFF)

GrisOscuroCard = Color(0xFFE0E0E0)

GrisTexto = Color(0xFF79747E)

RojoFuerte = Color(0xFFB3261E)

RojoClaro = Color(0xFFF9DEDC)

[PANTALLAS A GENERAR - REPLICAR EXACTAMENTE]

1. LoginScreen:

Fondo: Contenedor general ocupando fillMaxSize() con color sólido FondoLila.

Card Central: containerColor = GrisOscuroCard.

Textos Superiores: Título "Portal Académico" (MoradoPrincipal, negrita). Abajo: Spacer(modifier = Modifier.height(4.dp)), seguido de "Accede a tu cuenta" (GrisTexto).

Inputs (OBLIGATORIO USAR ICONOS):

Input 1: OutlinedTextField con placeholder "Correo Institucional". DEBE tener leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) }.

Input 2: OutlinedTextField con placeholder "Contraseña". DEBE tener leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) } y trailingIcon con el icono de Visibility / VisibilityOff.

Botones: Botón "INICIAR SESIÓN" (fondo MoradoPrincipal). Debajo el texto "¿Olvidaste tu contraseña?" (MoradoPrincipal).

2. HomeScreen (CENTRADOS EXTREMOS):

Fondo: Brush.verticalGradient(listOf(MoradoOscuro, Color.White)).

Alineación y Posición: La columna principal debe tener horizontalAlignment = Alignment.CenterHorizontally para que absolutamente todo (textos y tarjetas) esté centrado. Para ubicarlo en el centro-arriba, inicia la columna con un Spacer(modifier = Modifier.weight(0.5f)), coloca los textos ("Bienvenido," y "Jordan Reyes" en negrita/grande, subtítulo "¿Qué deseas gestionar hoy?"), las dos tarjetas de opciones con sus respectivos iconos (Groups y Person), y un Spacer(modifier = Modifier.weight(1f)) antes del botón de salir.

Botón Salir (FIJADO AL FONDO): Es un Row centrado con Icon(Icons.AutoMirrored.Filled.ExitToApp, tint = RojoFuerte) y Text("Cerrar Sesión Segura", color = RojoFuerte).

3. ListScreen (Directorio de Alumnos):

Fondo General: FondoPantallaBlanco.

TopAppBar (TODO LILA CONTINUO): La parte superior debe tener un fondo lila claro/pastel (FondoLila) ocupando todo el ancho (incluyendo barra de estado). Título "Directorio de Alumnos" (MoradoOscuro, negrita) e icono de flecha atrás a la izquierda.

Cards de Fila (CON AVATAR): containerColor = GrisOscuroCard. DENTRO de la card debe haber un Row. A la izquierda OBLIGATORIO un avatar circular con Icon(Icons.Filled.Person). Luego el nombre del alumno (20.sp, negrita, negro) y su carrera (morado). A la derecha un Icon(Icons.Filled.KeyboardArrowRight).

4. DetailScreen (Expediente Académico):

TopAppBar: Título "Expediente Académico" y fondo FondoLila con flecha de retroceso.

Fondo General: FondoLila.

Diseño Superior y Avatar Superpuesto: Contenedor superior MoradoOscuro con bordes inferiores redondeados a 32.dp. El avatar circular DEBE estar posicionado exactamente mitad dentro del color morado y mitad fuera (usando Alignment.BottomCenter y un offset adecuado). Nombre en negrita y carrera debajo del avatar.

Card de Datos: Tarjeta color GrisOscuroCard que contenga de forma unificada las 3 filas con iconos (ID Estudiante, Correo Electrónico, Facultad) y la sección inferior de "Biografía" con su línea divisoria.

5. ProfileScreen (Configuración de Perfil - CABECERA CORREGIDA):

TopAppBar: Flecha para regresar a la izquierda y título "Configuración de Perfil" al costado, con fondo FondoLila.

Cabecera Degradada con Altura Visible y Contenido Interno: El contenedor superior (el rectángulo con degradado) debe tener una altura generosa (por ejemplo, height(220.dp)) y un degradado horizontal de izquierda a derecha comenzando en MoradoOscuro y terminando en un tono ocre/amarillo muy sutil y oscuro (Color(0xFF6B5B5B)). DENTRO de este rectángulo con degradado, coloca una Column centrada vertical y horizontalmente que contenga obligatoriamente:

El avatar circular grande de la foto del estudiante (con borde blanco y tamaño aproximado de 90.dp).

Un Spacer(height = 8.dp).

El nombre completo del estudiante en color blanco, negrita y tamaño grande (ej. 20.sp). Todo debe quedar perfectamente contenido dentro del rectángulo con degradado.

Fondo Inferior: FondoLila.

Secciones Divisorias: Divide la información en dos bloques con títulos en mayúsculas y color morado:

"INFORMACIÓN PERSONAL": Nombre Completo (Icono Person), Correo Electrónico (Icono Email), Teléfono (Icono Phone).

"ACADÉMICO": Carrera (Icono School), Curso Actual (Icono DateRange). Los valores de respuesta deben ir en negrita y negro.

Botón Cerrar Sesión: Empujado al fondo absoluto usando un Spacer(weight = 1f). Ocupa todo el ancho, tiene una altura de 50.dp, fondo RojoClaro y texto RojoFuerte.

[ENTREGABLE]
Genera el código Kotlin completo, de inicio a fin, para los siguientes 7 archivos:

Screen.kt

AppNavigation.kt

LoginScreen.kt

HomeScreen.kt

ListScreen.kt (Incluyendo la data class Alumno)

DetailScreen.kt

ProfileScreen.kt
