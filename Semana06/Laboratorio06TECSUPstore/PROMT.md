TECSUPstore
-----------


Promt utilizado:

Actúa como un desarrollador senior de Android especializado en Jetpack Compose y Material 3. 

Tengo la base de una aplicación llamada "TECSUP Store" (paquete com.reyes.tecsupstore) en la rama main con una lista de productos en tarjetas (TarjetaProducto.kt), un menú lateral (AppDrawer.kt), la coordinación de vistas (AppNavegacion.kt) y MainActivity.kt.

Necesito implementar la "Fase 2: Mejora con IA" con los siguientes requerimientos obligatorios y funcionales:

1. Requerimiento Principal (Badge reactivo):
- Conectar la acción de marcar o desmarcar un producto como favorito desde el DropdownMenu de cada tarjeta con un Badge numérico reactivo ubicado en el ítem "Favoritos" del NavigationDrawer.
- Cuando haya más de 0 favoritos, el Badge debe mostrar la cantidad en fondo morado (#5E2E8C) y texto blanco.
- En la sección "Favoritos", debe listarse únicamente los productos marcados; si no hay ninguno, mostrar un estado vacío informativo.

2. Funcionalidad completa del menú del producto (TarjetaProducto.kt):
- "Favoritos": Alternar el estado booleano isFavorite, cambiando el texto a "Quitar de favoritos" y el icono a rojo si está seleccionado.
- "Compartir": Ejecutar un Intent nativo del sistema (Intent.ACTION_SEND) con LocalContext para compartir el nombre y precio del producto a apps externas (WhatsApp, etc.).
- "Reportar": Desplegar un AlertDialog contextual para confirmar el reporte del producto e informar mediante un Snackbar en la pantalla principal.
- Agregar un botón directo de compra (ShoppingCart) en la tarjeta para registrar órdenes en tiempo real.

3. Pantallas del Drawer y arquitectura modular (AppScreens.kt):
- Para no sobrecargar AppNavegacion.kt, desacoplar y crear el archivo AppScreens.kt con:
  a) LoginScreen: Formulario de inicio de sesión real (correo institucional y contraseña) para el usuario "Jordan Reyes" (jordan.reyes@tecsup.edu.pe). Si no está autenticado, la app debe bloquearse en esta pantalla.
  b) PerfilScreen: Vista con avatar de iniciales "JR", información académica de TECSUP (Diseño y Desarrollo de Software), tarjetas de estadísticas (cantidad de favoritos y pedidos) y botón funcional de "Cerrar sesión" que limpie la sesión y redirija al Login.
  c) MisPedidosScreen: Historial interactivo de compras realizadas desde el catálogo, mostrando código de orden, nombre, total y badge de estado ("Entregado" o "En preparación"). El Drawer debe mostrar también un badge con el total de pedidos acumulados.

4. Consistencia visual:
- Mantener la paleta morada institucional (#5E2E8C), esquinas redondeadas (RoundedCornerShape), avatares circulares y tipografía acorde a las especificaciones.

Proporcióname el código completo, modularizado y sin errores para cada uno de los archivos del proyecto.


