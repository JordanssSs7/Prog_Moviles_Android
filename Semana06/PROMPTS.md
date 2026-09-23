Clinica Salud+
--------------
Una aplicación de gestión médica que permite navegar mediante un menú lateral para buscar especialistas, agendar citas de forma interactiva y consultar historiales y perfiles de pacientes.
-----------------------------------


REQUERIMIENTOS FUNCIONALES :
- La aplicación permite al usuario desplegar un menú lateral estructurado para navegar de forma fluida entre las vistas de Inicio, Mis citas, Historial médico y Perfil.
- El sistema permite visualizar un listado de especialistas en la pantalla principal y acceder a una vista de detalle individual por cada médico mediante el paso de parámetros por ruta.
- El sistema permite visualizar un listado de especialistas en la pantalla principal y acceder a una vista de detalle individual por cada médico mediante el paso de parámetros por ruta.

Promt Utilizado:
----------------
Eres un Desarrollador Senior de Android experto en Jetpack Compose. Tu tarea es generar el código UI completo y funcional para la Opción A (Clínica Salud+) siguiendo exactamente el diseño de las imágenes de referencia del documento oficial.

REGLA DE ORO 1: ESTÁ ESTRICTAMENTE PROHIBIDO inventar textos, campos o pantallas que no correspondan a la Opción A de Clínica Salud+. Usa exactamente los textos indicados.
REGLA DE ORO 2: ESTÁ ESTRICTAMENTE PROHIBIDO omitir los íconos. Debes importar y usar androidx.compose.material.icons.filled.* en TODAS las pantallas.
REGLA DE ORO 3: No uses comentarios de relleno como // resto del código. Genera todo el código completo de inicio a fin para que compile directamente.
REGLA DE ORO 4: Toda sección o pantalla a la que se ingrese (Perfil Médico, Agendar, Mis Citas, Historial, Perfil) DEBE tener una flecha de retroceso (ArrowBack) en la parte superior para regresar a la vista anterior.

[CONFIGURACIÓN DEL PROYECTO]
Paquete base: com.reyes.clinicasalud (Usa las carpetas com.reyes.clinicasalud.screen y com.reyes.clinicasalud.navigation).
Navegación: Usa NavHost y rememberNavController().

[COLORES OBLIGATORIOS]
MoradoPrincipal = Color(0xFF512DA8)
FondoClaro = Color(0xFFF8F9FA)
CardFondo = Color(0xFFF4F1F8)
VerdeConfirmacion = Color(0xFFE8F5E9)
VerdeTexto = Color(0xFF2E7D32)

[PANTALLAS A GENERAR]

1. MainActivity.kt (Contenedor con ModalNavigationDrawer):
- ModalNavigationDrawer envolviendo el Scaffold principal.
- DrawerContent (Menú lateral idéntico a la Figura 2):
  * Cabecera del Drawer con un círculo morado con iniciales "JP", nombre "Juan Pérez" y subtítulo "Paciente", seguidos de una línea divisoria horizontal.
  * Opciones del menú lateral: "Inicio", "Mis citas", "Historial médico", y "Perfil" (con iconos y resaltando la opción activa con fondo lila/morado claro).

2. InicioScreen.kt (Figura 1 - Pantalla principal):
- TopAppBar o contenedor superior morado con esquinas inferiores redondeadas que muestra: "Clínica Salud+" y abajo "Hola, Juan".
- Fila de chips de filtro horizontales (LazyRow): "Cardiología" (seleccionado en morado oscuro) y "Pediatría" (en tono claro).
- Sección "Médicos disponibles" en un LazyColumn con tarjetas (CardFondo) que contengan:
  * Icono circular/avatar con el signo "+".
  * Nombre del médico (ej. "Dra. Ana Torres") y especialidad (ej. "Cardióloga").
  * Calificación a la derecha con estrella y puntaje (ej. "★ 4.9").
- Al hacer clic en un médico, debe navegar a la pantalla de detalle pasando su ID.

3. PerfilMedicoScreen.kt (Figura 1 - Detalle del médico):
- Botón de retroceso "← Perfil del médico" (usando Icono ArrowBack).
- Avatar circular grande con un ícono "+" centrado.
- Nombre del médico en negrita ("Dra. Ana Torres"), subtítulo ("Cardióloga · 12 años exp.") y calificación con estrellas ("★ 4.9 (128 reseñas)").
- Párrafo descriptivo: "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."
- Botón inferior anclado de color morado: "Agendar cita".

4. AgendarCitaScreen.kt (Figura 1 - Selección de fecha y hora):
- Cabecera "← Agendar cita" (usando Icono ArrowBack).
- Sección "Selecciona fecha" con chips horizontales (ej. Jue 26, Vie 27 [seleccionado en morado], Sáb 28).
- Sección "Selecciona hora" con chips o botones de hora (ej. 9:00, 10:30 [seleccionado], 3:00).
- Botón inferior morado de confirmación: "Confirmar cita".

5. ConfirmacionScreen.kt (Figura 1 - Pantalla de éxito):
- Ícono central de verificación (check verde) dentro de un círculo verde claro.
- Texto principal: "¡Cita agendada!".
- Subtítulos con los datos de la cita: "Dra. Ana Torres" y "Viernes 27, 10:30 am".
- Botón inferior: "Ver mis citas" (que redirige a la sección de Mis citas limpiando la pila con popUpTo).

6. MisCitasScreen.kt (Figura 2 - Historial de reservas):
- TopAppBar: Título "Mis citas" con ícono de flecha de retroceso (ArrowBack) a la izquierda.
- Lista de tarjetas con las citas agendadas:
  * Cita 1: "Dra. Ana Torres", "Viernes 27, 10:30 am", etiqueta de estado en verde "Confirmada".
  * Cita 2: "Dr. Luis Vega", "Miércoles 15, 3:00 pm", etiqueta de estado gris "Completada".

7. HistorialMedicoScreen.kt (Pantalla de Historial - CON CONTENIDO):
- TopAppBar: Título "Historial médico" con ícono de flecha de retroceso (ArrowBack) a la izquierda.
- Contenido detallado para evitar que esté vacía: Un LazyColumn con tarjetas informativas de consultas pasadas.
  * Tarjeta 1: "Consulta General - 12 Enero 2026". Detalle: "Chequeo de rutina. Presión arterial normal. Se recetan vitaminas."
  * Tarjeta 2: "Cardiología - 05 Noviembre 2025". Detalle: "Electrocardiograma sin anomalías. Próxima revisión en 1 año."

8. PerfilScreen.kt (Pantalla de Perfil del Paciente - CON CONTENIDO COMPLETO):
- TopAppBar: Título "Mi perfil" con ícono de flecha de retroceso (ArrowBack) a la izquierda.
- Contenido detallado que evite que la pantalla aparezca vacía:
  * Cabecera con un avatar circular grande con iniciales "JP", nombre completo "Juan Pérez" y correo "juan.perez@tecsup.edu.pe".
  * Secciones organizadas en tarjetas con iconos (`androidx.compose.material.icons.filled.*`):
    - Información Personal: Teléfono (+51 987 654 321), Dirección (Av. Lima 123, Lima).
    - Datos Médicos: Seguro de Salud (Pacífico Seguros), Tipo de Sangre (O+).
  * Botón inferior de color morado o rojo para cerrar sesión.

[ENTREGABLE]
Genera todo el código Kotlin completo, estructurado en archivos separados dentro de sus respectivos paquetes (`screen`), listo para ser copiado a tu proyecto de Android Studio sin errores de compilación.

  






TecsupFit
---------

Una plataforma de reserva de entrenamientos y clases de gimnasio estructurada con una barra de navegación inferior para explorar rutinas, filtrar horarios y apartar cupos exitosamente
--------------------------

REQUERIMIENTOS FUNCIONALES:
- La aplicación incorpora una barra de navegación inferior interactiva de cuatro pestañas principales (Inicio, Reservas, Rutinas y Perfil) que resalta de forma dinámica el icono activo.
- El sistema muestra dinámicamente un catálogo de clases de entrenamiento organizadas mediante componentes de desplazamiento fluido y filtros rápidos por categorías de tiempo.
- Permite al usuario seleccionar una clase específica para ver sus detalles de capacidad e iniciar una reserva exitosa con una pantalla de confirmación dedicada.

Promt utilizado:
----------------

Eres un Desarrollador Senior de Android experto en Jetpack Compose. Tu tarea es generar el código UI completo y funcional para la Opción B (TECSUP Fit) siguiendo exactamente el diseño de las imágenes de referencia del documento oficial.

REGLA DE ORO 1: ESTÁ ESTRICTAMENTE PROHIBIDO inventar textos, campos o pantallas que no correspondan a la Opción B de TECSUP Fit. Usa exactamente los textos indicados.
REGLA DE ORO 2: ESTÁ ESTRICTAMENTE PROHIBIDO omitir los íconos. Debes importar y usar androidx.compose.material.icons.filled.* en TODAS las pantallas.
REGLA DE ORO 3: No uses comentarios de relleno como // resto del código. Genera todo el código completo de inicio a fin para que compile directamente.
REGLA DE ORO 4: Toda sección o pantalla secundaria a la que se ingrese debe tener su respectiva flecha de retroceso (ArrowBack) en la parte superior para regresar a la vista anterior, excepto las 4 pestañas principales del bottomBar.

[CONFIGURACIÓN DEL PROYECTO]
Paquete base: com.reyes.tecsupfit (Usa las carpetas com.reyes.tecsupfit.screen y com.reyes.tecsupfit.navigation).
Navegación: Usa NavHost y rememberNavController().

[COLORES OBLIGATORIOS]
VerdePrimary = Color(0xFF006B3F) (o un tono verde institucional similar para fitness)
VerdeLight = Color(0xFFE8F5E9)
FondoClaro = Color(0xFFF9F9F9)
CardFondo = Color(0xFFF1F3F5)

[PANTALLAS A GENERAR]

1. MainActivity.kt (Contenedor principal con BottomBar):
- Scaffold envuelto con un sistema de navegación (NavHost).
- BottomBar inferior fija visible en las 4 pestañas principales ("Inicio", "Reservas", "Rutinas", "Perfil") con iconos limpios y resaltando la pestaña activa en verde.

2. InicioFitScreen.kt (Figura 3 - Pantalla principal):
- Contenedor superior verde con esquinas redondeadas que muestra: "TECSUP Fit" y abajo "Hola, Diego".
- Fila de filtros horizontales (LazyRow): "Hoy" (seleccionado en verde oscuro) y "Esta semana" (en tono claro).
- Sección "Clases disponibles" en un LazyColumn con tarjetas que contengan:
  * Icono circular/cuadrado con una mancuerna (FitnessCenter).
  * Nombre de la clase (ej. "Yoga funcional", "Cross Training", "Spinning") y horario/sala (ej. "7:00 am · Sala 2").
- Al hacer clic en una clase, navega a la pantalla de detalle pasando su ID.

3. DetalleClaseScreen.kt (Figura 3 - Detalle de la clase):
- TopAppBar: "← Detalle de clase" con flecha de retroceso.
- Tarjeta superior grande con el icono de la mancuerna en fondo verde claro.
- Información detallada: Nombre de la clase ("Cross Training"), horario ("6:00 pm · Sala 1 · 45 min"), descripción ("Entrenamiento funcional de alta intensidad. Cupos limitados.") y estado de cupos ("8 de 12 cupos disponibles").
- Botón inferior anclado de color verde: "Reservar cupo".

4. ConfirmacionFitScreen.kt (Figura 3 - Pantalla de éxito):
- Ícono central de verificación (check verde) dentro de un círculo verde claro.
- Texto principal: "¡Cupo reservado!".
- Subtítulos con los datos de la reserva: "Cross Training" y "Hoy, 6:00 pm · Sala 1".
- Botón inferior: "Ver mis reservas" (que redirige a la pestaña de Mis Reservas limpiando la pila con popUpTo).

5. ReservasScreen.kt (Figura 4 - Pestaña Mis reservas):
- TopAppBar: Título "Mis reservas".
- Lista de tarjetas con las reservas del usuario:
  * Reserva 1: "Cross Training", "Hoy, 6:00 pm", barra lateral verde y etiqueta "Confirmada".
  * Reserva 2: "Yoga funcional", "Ayer, 7:00 am", barra lateral gris y etiqueta "Completada".

6. RutinasScreen.kt (Pestaña de Rutinas):
- TopAppBar: Título "Rutinas".
- Contenido organizado con tarjetas informativas o rutinas asignadas al usuario para completar la sección.

7. PerfilFitScreen.kt (Figura 4 - Pestaña Mi perfil):
- TopAppBar: Título "Mi perfil".
- Contenido detallado:
  * Avatar circular grande con iniciales "DR", nombre completo "Diego Ramos" y suscripción "Plan Premium".
  * Tarjetas de estadísticas alineadas horizontalmente: "14 Clases" y "3 Rachas".
  * Secciones adicionales de configuración de cuenta y botón para cerrar sesión.

[ENTREGABLE]
Genera todo el código Kotlin completo, estructurado en archivos separados dentro de sus respectivos paquetes (`screen`), listo para ser copiado a tu proyecto de Android Studio sin errores de compilación.

