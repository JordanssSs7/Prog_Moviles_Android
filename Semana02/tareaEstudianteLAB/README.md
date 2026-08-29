# Sistema de Matricula Universitaria

**Jordan Arturo Reyes Saravia**

## Descripcion

Programa de consola en Kotlin que simula la matricula de un estudiante en varios
cursos. El usuario ingresa su nombre, la cantidad de cursos y el valor del
credito; luego, por cada curso, ingresa el nombre y la cantidad de creditos. El
programa calcula el costo de cada curso, el total de creditos y el total a pagar,
clasifica la carga academica segun los creditos y define la forma de pago en
cuotas.

## Como funciona

- **Entrada de datos:** se usa un `Scanner` sobre la entrada estandar para leer el
  nombre (texto), la cantidad de cursos y los creditos (enteros) y el valor del
  credito (decimal).
- **Bucle de cursos:** un `for` recorre cada curso, calcula
  `costoCurso = creditosCurso * valorPorCreditos` y acumula `totalCreditos` y
  `totalAPagar`. El detalle de cada curso se guarda en un texto para mostrarlo al
  final.
- **Carga academica (`if / else if / else`):**
  - creditos <= 12 -> "Malla Regular (M.R.)"
  - creditos entre 13 y 18 -> "Carga Completa"
  - creditos > 18 -> "Requiere Autorizacion"
- **Forma de pago:** 3 cuotas si el total supera S/ 1500, en caso contrario 2
  cuotas. `montoPorCuota = totalAPagar / numeroCuotas`.
- **Formato:** los montos se muestran con 2 decimales usando `String.format("%.2f", ...)`.

## Como ejecutarlo

Abrir `app/src/main/java/com/reyes/tareaestudiante/Estudiante.kt` , Dar clic derecho dentro de ese file y presionar Run'Estudiante.KT' with Coverage . La entrada y la salida se ven en la ventana Run, sin necesidad de emulador.

## Ejemplo de salida

```
=== Resultado Final ===
Nombre del estudiante: Ana Torres
Total de cursos matriculados: 3
Detalle de cursos:
  - Programacion Movil    | 4 creditos | S/ 720.00
  - Base de Datos         | 3 creditos | S/ 540.00
  - Ingles IV             | 2 creditos | S/ 360.00
Total de creditos acumulados: 9
Total a pagar: S/ 1620.00
Carga Academica: Malla Regular (M.R.)
Forma de Pago: 3 cuotas de S/ 540.00
```

## Captura de la consola

<img width="691" height="704" alt="image" src="https://github.com/user-attachments/assets/ab5473e3-76e0-4923-9a90-7b8a6ef7dfa3" />
<img width="666" height="442" alt="image" src="https://github.com/user-attachments/assets/e36202ee-1e8b-4f7a-8bd9-534127122d36" />

