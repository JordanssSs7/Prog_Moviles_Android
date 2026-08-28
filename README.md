# Prog_Moviles_Android

## Lab 02 -> Carrito de compras en Kotlin

**Jordan Arturo Reyes Saravia**

Este repositorio tiene dos versiones del laboratorio en ramas distintas:

- Rama `main`: **Versión 1** (sin IA) -> `data class Producto` + funciones sueltas.
- Rama `LaboratoriosIA`: **Versión 2** (con IA) -> misma salida en consola, pero
  reescrita aplicando los 4 pilares de la Programación Orientada a Objetos y con
  los datos ingresados por teclado.

---

## Versión 2 (rama LaboratoriosIA)

### Qué hace el programa

Simula un carrito de compras por consola. El usuario ingresa su nombre y luego,
producto por producto, escribe nombre, precio, cantidad y tipo. Al final el
programa imprime la boleta con el detalle alineado (2 decimales), el subtotal,
el IGV (18%), el total, el producto más caro, el descuento por monto y el total
con descuento. También permite consultar y quitar un producto.

### Los 4 pilares de la POO

| Pilar | Dónde está | Explicación |
|---|---|---|
| **Abstracción** | `model/Producto.kt` | Clase `abstract class Producto`: define lo común (nombre, precio, cantidad) y obliga a implementar `importe()`, pero no se puede instanciar directamente. |
| **Herencia** | `model/ProductoRegular.kt`, `model/ProductoImportado.kt`, `model/ProductoPerecible.kt` | Las tres clases heredan de `Producto` con `: Producto(...)`. |
| **Polimorfismo** | `model/Carrito.kt` (`subtotal()`) y `Carrito.kt` (`mostrarDetalle`) | Se recorre la lista llamando `p.importe()` sin saber si cada `p` es Regular, Importado o Perecible; cada uno responde con su fórmula y su `toString()`. |
| **Encapsulamiento** | `model/Carrito.kt` | La lista `private val items` no se expone; solo se toca con métodos públicos (`agregar`, `eliminar`, `listar`, `subtotal`...) que validan los datos con `require(...)`. |

### Tipos de producto

- **Regular**: `precio x cantidad`.
- **Importado**: `precio x cantidad x (1 + % impuesto de importación)`.
- **Perecible**: si le quedan 3 días o menos para vencer, se le aplica -20%.

### Cómo ejecutarlo (sin emulador)

**Opción A — botón Run:** abrir `app/src/main/java/com/reyes/lab02carritokotlin/Carrito.kt`
y pulsar el triángulo verde que aparece junto a `fun main()`. La entrada y la
salida se ven en la ventana **Run**.

**Opción B — Terminal de Android Studio:**

```
.\gradlew runCarrito -q --console=plain
```

### Estructura

```
app/src/main/java/com/reyes/lab02carritokotlin/
├── Carrito.kt          -> fun main(): lee por teclado e imprime la boleta
├── MainActivity.kt     -> pantalla Compose de la plantilla (no se usa en este lab)
└── model/
    ├── Producto.kt          -> clase abstracta (ABSTRACCIÓN)
    ├── ProductoRegular.kt    -> HERENCIA
    ├── ProductoImportado.kt  -> HERENCIA (+ impuesto de importación)
    ├── ProductoPerecible.kt  -> HERENCIA (+ descuento por vencimiento)
    └── Carrito.kt            -> clase Carrito (ENCAPSULAMIENTO + POLIMORFISMO)
```

### Captura de la consola

<!-- Reemplazar por la captura de la ejecución final -->
_(pendiente: subir la captura de la ventana Run con la boleta final)_

---

## Preguntas de reflexión

### ¿Por qué nombre y precio son `val` pero cantidad es `var`?

Precio y nombre son val porque son variables inmutable, es decir son variables que
no deben cambiar durante una sesión de compra, siendo el nombre y el precio datos
fijos, y cantidad es variable mutable porque va a estar variando constantemente es
decir es un dato dinámico, como es cantidad de una clase producto, este va a ir
actualizando su stock cada que se realice una compra.

### ¿Qué pasaría si intentas cambiar el precio después de crear el producto?

El precio al ser una variable inmutable kotlin prohíbe reasignar el valor
directamente, es decir el código no va a compilar y mostrará un error.

### ¿Qué diferencia hay entre la V1 y la V2?

La V1 modela el producto con una `data class` y hace los cálculos en funciones
sueltas. La V2 usa una jerarquía de clases (`Producto` abstracta + 3 subclases) y
una clase `Carrito` que encapsula la lista y los cálculos; además los datos ya no
están fijos en el código sino que los ingresa el usuario por teclado.
