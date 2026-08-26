# Prog_Moviles_Android

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

 /// ¿por qué nombre y precio son val pero cantidad es var?/// 
 Precio y nombre son val porque son variables inmutable, es decir son variables que deben cambiar durante una sesión de compra, siendo el nombre y el precio datos fijos , y cantidad es variable mutable porque va a estar variando constantemente es decir es un dato dinámico,como es cantidad de una clase producto, este va a ir actualizando su stock cada que se realice una compra.
 
 /// ¿Qué pasaría si intentas cambiar el precio después de crear el producto? ///
 El precio al ser una variable inmutable kotlin prohíbe resignar el valor directamente, es decir el código no va a compilar y mostrará un error.
