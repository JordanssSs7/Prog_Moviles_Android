# Prog_Moviles_Android


Lab02 -> Carrito de compras en Kotlin: variables, funciones y operaciones
-------------------------------------------------------------------------

Jordan Arturo Reyes Saravia
---------------------------

Programa de consola en Kotlin que simula un carrito de compras de una tienda. El usuario ingresa su nombre y luego cada producto (nombre, precio, cantidad y tipo: regular, importado o perecible); el programa valida los datos y va agregando los productos al carrito. Al final genera una boleta con el detalle alineado en columnas y 2 decimales, calcula el subtotal, el IGV (18%) y el total, muestra el producto más caro y aplica un descuento según el monto (5% si supera S/ 3000, 10% si supera S/ 5000). También permite consultar y eliminar productos.

Esta versión aplica los cuatro pilares de la Programación Orientada a Objetos: abstracción (clase abstracta Producto), herencia (ProductoRegular, ProductoImportado, ProductoPerecible), polimorfismo (el carrito calcula el total con importe() sin conocer el tipo de cada producto) y encapsulamiento (la clase Carrito guarda los productos en una lista privada accesible solo por sus métodos). Se ejecuta por consola desde Android Studio, sin emulador.


Promt usado IA:
---------------

[ROL]
Eres un desarrollador Senior especializado en Kotlin y Programación Orientada a
Objetos (POO), con experiencia dictando laboratorios universitarios y explicando
herencia, encapsulamiento, abstracción y polimorfismo con ejemplos ejecutables
por consola.

[CONTEXTO]
Proyecto: "Lab02CarritoKotlin", paquete com.reyes.lab02carritokotlin. Es el
Laboratorio 02 del curso "Programación en Móviles / Diseño y Desarrollo de
Software" (Tecsup), guía GLAB-S02-JLEONS-2026-2, docente Juan León Suiyon.
Tema: "Carrito de compras en Kotlin: variables, funciones y operaciones".
Esta es la VERSIÓN 2 del laboratorio (asistida por IA). La Versión 1 (sin IA) ya
fue entregada; la V2 debe partir de los mismos requisitos de la guía y AÑADIR los
4 pilares de la POO y la entrada de datos por teclado.

Estado actual de la carpeta: es la plantilla "Empty Activity (Compose)" recién
creada. Solo existe MainActivity.kt (muestra "Hello Android!"), el tema Material3
en ui/theme/ y los tests de ejemplo. NO hay data class Producto, NO hay fun main,
NO hay ninguna lógica de carrito ni de POO.

Stack: Gradle 9.5.0 (wrapper), AGP 9.3.2 con Kotlin integrado (Kotlin 2.2.10, NO
se aplica el plugin org.jetbrains.kotlin.android), compileSdk/targetSdk 37,
minSdk 24, Java 11, configuration-cache activada. IDE: Android Studio en Windows.

Reglas del laboratorio (deben respetarse):
- El programa se ejecuta POR CONSOLA, SIN emulador y SIN dispositivo físico,
  desde la Terminal de Android Studio.
- El IGV es 18% del subtotal.
- El descuento se aplica sobre el TOTAL (subtotal + IGV): 5% si el total supera
  S/ 3000; 10% si supera S/ 5000; en otro caso 0%.
- Todos los montos se imprimen con EXACTAMENTE 2 decimales y columnas alineadas
  usando String.format.
- La salida en consola debe reproducir EXACTAMENTE este formato (Figura 1 de la
  guía), reemplazando nombre y productos por los que ingrese el usuario:

  =========================================
       CARRITO DE COMPRAS - TIENDA TECSUP
  =========================================
  Cliente: <nombreCliente>

  Producto agregado: <nombre>
  ... (una línea por producto)

  --------- DETALLE DEL CARRITO ---------
  1. <nombre>          x<cant>  S/ <importe>
  ... (alineado: nombre %-20s, importe %8.2f)
  ---------------------------------------
  Cantidad de productos : <n>
  Subtotal              : S/ <subtotal>
  IGV (18%)             : S/ <igv>
  TOTAL A PAGAR         : S/ <total>
  ---------------------------------------
  Producto mas caro: <nombre> (S/ <precio>)
  Descuento aplicado: <5%|10%> por compra mayor a S/ <3000|5000>
  TOTAL CON DESCUENTO   : S/ <totalConDescuento>

  Gracias por su compra, <nombreCliente>!

[TAREA]
Programa el laboratorio completo para que, además de cumplir la guía, aplique los
4 pilares de la POO y reciba los datos por teclado. Entrega el código completo.

1. ABSTRACCIÓN
   - Crea "abstract class Producto(val nombre: String, val precioBase: Double,
     var cantidad: Int)" en el paquete com.reyes.lab02carritokotlin.model.
   - Declara un método abstracto "fun importe(): Double" (precio de la línea) y
     un "abstract val tipo: String".
   - En el bloque init valida con require(): precioBase > 0 y cantidad > 0.

2. HERENCIA (mínimo 3 subclases de Producto)
   - ProductoRegular: importe() = precioBase * cantidad.
   - ProductoImportado(..., val impuestoImportacion: Double): importe() suma el
     impuesto de importación por unidad.
   - ProductoPerecible(..., val diasParaVencer: Int): si diasParaVencer <= 3
     aplica 20% de descuento a la línea.
   - Cada subclase hace override de importe() y de toString().

3. POLIMORFISMO
   - class Carrito recorre su lista de Producto y llama p.importe() SIN conocer
     el tipo concreto (subtotal polimórfico).
   - Sobrecarga de método: agregar(p: Producto) y agregar(p: Producto, veces: Int).
   - El descuento se resuelve con "when" (5% / 10% / 0%).
   - El producto más caro se obtiene con maxByOrNull { it.precioBase }.

4. ENCAPSULAMIENTO
   - class Carrito(val nombreCliente: String) con:
       private val items = mutableListOf<Producto>()
   - Métodos públicos: agregar(...), buscar(nombre): Producto?, eliminar(nombre):
     Boolean, listar(): List<Producto> (devuelve items.toList(), inmutable),
     cantidadItems(): Int, subtotal(): Double, igv(): Double, total(): Double,
     descuento(): Double, totalConDescuento(): Double, productoMasCaro():
     Producto?.
   - Ninguna clase de model/ puede importar android.* ni androidx.*.

5. ENTRADA INTERACTIVA (readLine)
   - En fun main(), pedir por teclado:
       * "Ingrese el nombre del cliente:"
       * "¿Cuantos productos desea agregar?"  -> Int
       * Por cada producto: nombre, precio (Double), cantidad (Int) y tipo:
         1 = Regular, 2 = Importado (pide monto de impuesto de importación),
         3 = Perecible (pide dias para vencer).
   - Validar cada entrada: si readLine() es null, vacío, no numérico o <= 0,
     mostrar un mensaje claro y volver a pedir ese dato (no reventar el programa).
   - Con esos datos construir el Carrito e imprimir la boleta con el formato de
     [CONTEXTO].

6. EJECUCIÓN DESDE LA TERMINAL DE ANDROID STUDIO (sin emulador)
   - Coloca "fun main()" en app/src/main/java/com/reyes/lab02carritokotlin/Main.kt
     (clase generada: com.reyes.lab02carritokotlin.MainKt).
   - Agrega en app/build.gradle.kts una tarea Gradle tipo JavaExec llamada
     "runCarrito" que ejecute MainKt con la classpath del variant debug y con
     standardInput = System.`in` (para que readLine funcione).
   - El comando a documentar y que DEBE funcionar es:
         .\gradlew runCarrito -q --console=plain
   - Indica también la alternativa: botón verde ▶ en el margen junto a fun main().
   - NO se debe requerir emulador ni "adb".

[RESTRICCIONES]
- Kotlin idiomático, guía de estilo oficial (kotlin.code.style=official).
- Nombres del dominio en español; comentarios en español.
- Solo stdlib de Kotlin. NO usar librerías de terceros.
- NO tocar MainActivity.kt, el tema ni la parte Compose.
- El dominio (model/) debe compilar y correr en la JVM (nada de Android).
- Casos borde obligatorios:
    * carrito vacío -> subtotal, igv y total = 0.00 y no imprime "producto mas caro".
    * eliminar(nombre) de un producto inexistente -> devuelve false, sin excepción.
    * precio o cantidad <= 0, o texto no numérico -> mensaje y se vuelve a pedir.
    * listar() nunca expone la lista mutable interna.
- Compatibilidad: debe compilar con .\gradlew assembleDebug y con
  configuration-cache activada.
- Caso de verificación: si el usuario ingresa "Juan Leon" y 4 productos Regulares
    Laptop HP  2500.00 x1
    Mouse Logitech  45.50 x2
    Audifonos Sony  120.00 x1
    USB Kingston 64GB  25.00 x3
  la salida debe dar Subtotal S/ 2786.00, IGV (18%) S/ 501.48,
  TOTAL A PAGAR S/ 3287.48, Descuento 5%, TOTAL CON DESCUENTO S/ 3123.11
  (idéntico a la Figura 1 de la guía).

[FORMATO DE SALIDA]
Devuelve únicamente:
1. Árbol de archivos nuevos y modificados.
2. El contenido completo de cada archivo .kt en bloques de código separados, con
   comentarios inline mínimos que marquen cada pilar:
   // ABSTRACCION, // HERENCIA, // POLIMORFISMO, // ENCAPSULAMIENTO.
3. El fragmento exacto a pegar en app/build.gradle.kts para la tarea runCarrito.
4. Los comandos exactos de la Terminal de Android Studio para compilar y ejecutar.
5. Una simulación de la consola: entrada del usuario + boleta final, usando el
   caso de verificación de [RESTRICCIONES].
6. Una tabla: Pilar POO -> archivo/clase -> método o línea donde se demuestra.
7. Las respuestas a las preguntas de reflexión de la guía (val vs var, data class,
   mutableListOf vs listOf, ": Double", when del descuento) en 2-3 líneas cada una.
8. Sin texto fuera de lo anterior.