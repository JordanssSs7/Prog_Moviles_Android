# Prog_Moviles_Android


Lab02 -> Carrito de compras en Kotlin: variables, funciones y operaciones

Jordan Arturo Reyes Saravia

El programa simula el funcionamiento de un carrito de compras por consola, así gestionando productos con sus respectivos nombres, precios y stock, además genera un reporte detallado con columnas alineadas, calcula el subtotal, igv, y determina cuál es el producto más caro, y para terminar aplica un descuento segun el monto de comprar y muestra el total final a pagar.
<img width="828" height="889" alt="image" src="https://github.com/user-attachments/assets/e9bc5fe9-d53e-4ddd-80a2-a5e91c7321a9" />


data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

 /// ¿por qué nombre y precio son val pero cantidad es var?/// 
 -------------------------------------------------------------
 Precio y nombre son val porque son variables inmutable, es decir son variables que deben cambiar durante una sesión de compra, siendo el nombre y el precio datos fijos , y cantidad es variable mutable porque va a estar variando constantemente es decir es un dato dinámico,como es cantidad de una clase producto, este va a ir actualizando su stock cada que se realice una compra.
 
 /// ¿Qué pasaría si intentas cambiar el precio después de crear el producto? ///
 ---------------------------------------------------------------------------------
 El precio al ser una variable inmutable kotlin prohíbe resignar el valor directamente, es decir el código no va a compilar y mostrará un error.

/// Reto ///
------------
<img width="576" height="645" alt="image" src="https://github.com/user-attachments/assets/364fac6b-6607-4bed-8cc7-45680c9ffe9b" />
<img width="513" height="600" alt="image" src="https://github.com/user-attachments/assets/eebbc4e8-f44b-4a64-aeb6-94db605be130" />

El programa actualizado con el reto se implementó la función buscarProducto utilizando .find para localizador un producto por nombre dentro de la lista, también se integró la eliminación de elementos con removeIf para quitar un ítem en específico , y también se cambiaron los totales a las variables var para hacer un nuevo cálculo del subtotal, igv y subtotal.

