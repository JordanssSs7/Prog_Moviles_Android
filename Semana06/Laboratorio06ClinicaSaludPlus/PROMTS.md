**Proyecto:** Clínica SaludPlus — App Paciente  
**Estudiante:** Jordan Arturo Reyes Saravia  
**Fecha:** Octubre 2026  

---

## 1. Registro y Trazabilidad de Prompts

### Interacción 1: Lógica de Calendario Dinámico y Días Hábiles (`FechaUtils.kt`)
* **Prompt formulado:**
  > "Necesito implementar la lógica de selección de fecha para una app médica en Jetpack Compose usando `java.time.LocalDate`. La app debe mostrar solo 5 días hábiles a partir de hoy (sin sábados ni domingos). Debe permitir avanzar y retroceder de semana mediante flechas `<` y `>`, pero con una restricción estricta: no puede retroceder antes de la semana actual. Además, necesito que el encabezado indique el mes y año en español (ej. 'Octubre 2026'). Proporciona un objeto utilitario reutilizable."

* **Respuesta resumida de la IA:**
  Generó un objeto `FechaUtils` que utilizaba bucles sobre `LocalDate.now()` sumando días hasta completar 5 días ignorando `DayOfWeek.SATURDAY` y `DayOfWeek.SUNDAY`. Para el encabezado propuso `DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))`.

* **Errores encontrados y ajustes manuales aplicados:**
  1. *Desfase al cambiar de semana:* La IA calculaba la semana sumando días directamente desde el día actual (`LocalDate.now()`), lo que causaba que si hoy era miércoles, la siguiente semana comenzara en miércoles en lugar de un lunes estándar. Se corrigió manualmente fijando el inicio de semana con `.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset)`.
  2. *Formato de texto:* Los nombres de meses y días se generaban en minúscula ("octubre"). Se agregó una función de capitalización `.replaceFirstChar { it.uppercase() }` para que coincidiera con la interfaz de la maqueta.

---

### Interacción 2: Selector de Horarios y Reactividad en `FechaHoraScreen.kt`
* **Prompt formulado:**
  > "Tengo una pantalla `FechaHoraScreen` en Jetpack Compose. Al cambiar de día mediante el selector de días hábiles o las flechas de semana, ¿cómo logro que los horarios disponibles consultados desde un `Repositorio` singleton se recalculen de inmediato en un `LazyVerticalGrid` y la hora previamente seleccionada se deseleccione automáticamente para evitar que el usuario confirme una hora inválida?"

* **Respuesta resumida de la IA:**
  Planteó el uso de `remember(medicoId, fechaSeleccionada)` para recalcular la lista devuelta por `Repositorio.horariosDisponibles()`, y reiniciar la variable de estado `horaSeleccionada = null` en el callback de click de cada día y de las flechas de navegación semanal.

* **Errores encontrados y ajustes manuales aplicados:**
  1. *Validación del botón:* El botón 'Continuar' se mantenía habilitado con datos residuales. Se condicionó la propiedad `enabled` del botón a `horaSeleccionada != null`.
  2. *Ajuste visual:* Inicialmente la IA propuso chips pequeños que dejaban media pantalla vacía. Se rediseñaron las celdas del `LazyVerticalGrid` a una cuadrícula de 3 columnas con altura fija (`52.dp`), bordes definidos y esquinas redondeadas para ajustarse fielmente a la referencia visual.

---

### Interacción 3: Formateo Formal en Español en `ConfirmarCitaScreen.kt`
* **Prompt formulado:**
  > "En `ConfirmarCitaScreen` recibo la fecha en formato ISO 'YYYY-MM-DD'. Necesito convertirla a un formato legible formal en español como 'Lunes 5 de octubre 2026' y mostrar el rango de atención calculando 30 minutos adicionales sobre la hora de inicio (ej. '09:30 a 10:00'). Los ítems de la pantalla deben tener un recuadro independiente para los iconos en color celeste suave y un campo de texto opcional para el motivo de consulta."

* **Respuesta resumida de la IA:**
  Proporcionó una función de parseo con `LocalDate.parse(fecha)` y formateo manual en español. Además, generó la lógica aritmética para sumar 30 minutos a la cadena de hora y renderizó el bloque con `OutlinedTextField`.

* **Errores encontrados y ajustes manuales aplicados:**
  1. *Manejo de excepciones:* Si la fecha llegaba en un formato imprevisto, la aplicación lanzaba un `DateTimeParseException`. Se envolvió en un bloque `try-catch` de seguridad para devolver la cadena original en caso de error.
  2. *Estructura visual:* La IA intentó generar divisores horizontales genéricos. Se reemplazaron por componentes `FilaDetalleCitaFiel` donde cada fila posee una tarjeta `Surface` cuadrada (`46.dp`) con fondo pastel `0xFFEFF6FF` e icono azul, respetando la maqueta.

---

### Interacción 4: Integración de Recursos Gráficos y Consistencia Visual
* **Prompt formulado:**
  > "Tengo imágenes PNG de médicos y especialidades en `res/drawable/`. ¿Cómo integro dinámicamente estas fotos en las tarjetas de `MedicosScreen`, `FechaHoraScreen`, `ConfirmarCitaScreen` y `MisCitasScreen`, manteniendo un avatar por defecto si el recurso no existe (`fotoRes == 0`) y asegurando que las pantallas manejen estados vacíos cuando una especialidad no tenga doctores?"

* **Respuesta resumida de la IA:**
  Propuso un condicional `if (medico.fotoRes != 0)` para alternar entre `Image(painterResource(...))` y un `Box` con icono vectorial `Icons.Default.Person`. Para el estado vacío, propuso un `if (medicos.isEmpty())` que muestra un contenedor centrado con mensaje descriptivo.

* **Errores encontrados y ajustes manuales aplicados:**
  1. *Falta de recorte circular:* Las imágenes importadas se mostraban cuadradas distorsionando la tarjeta. Se agregó el modificador `.clip(CircleShape)` y `contentScale = ContentScale.Crop`.
  2. *Estado vacío en Mis Citas:* Se armonizó el diseño del estado vacío de citas con el de médicos para mantener coherencia en colores, tipografía y llamadas a la acción.

---







## 2. Preguntas de Reflexión 

### 1. ¿Qué ventajas y desventajas experimentaste al comparar el desarrollo sin IA (Fase 1) versus el desarrollo asistido por IA (Fase 2)?
* **Ventajas:** En la Fase 2, la velocidad para estructurar algoritmos específicos (como el filtrado de días de semana con la API de `java.time` y el cálculo de rangos horarios) aumentó considerablemente. La IA redujo el tiempo dedicado a escribir código repetitivo (*boilerplate*) y facilitó la exploración rápida de modificadores visuales en Jetpack Compose.
* **Desventajas:** El código generado por IA rara vez funciona a la primera sin intervención. Frecuentemente omite detalles de contexto del proyecto (nombres de paquetes, arquitectura en memoria existente o estados mutables de Compose) o asume layouts genéricos de Material Design en lugar de replicar las maquetas requeridas. El desarrollo manual de la Fase 1 brindó un conocimiento profundo de la arquitectura, lo que resultó indispensable para saber qué corregirle a la IA en la Fase 2.

### 2. ¿Tuviste que corregir o ajustar el código generado por la IA? ¿Por qué?
Sí, en todas las interacciones fue necesario realizar ajustes manuales:
* **Lógica de negocio:** La IA calculaba los días hábiles tomando la fecha actual como día 1 en lugar de sincronizar con el lunes de la semana en curso, rompiendo la coherencia de la navegación por semanas.
* **Integridad del estado en Jetpack Compose:** La IA no siempre gestionaba el reinicio de estados derivados; por ejemplo, al cambiar de fecha, la hora seleccionada anteriormente se quedaba guardada en memoria si no se forzaba su limpieza manual a `null`.
* **Diseño milimétrico (UI/UX):** La IA proponía divisores y botones por defecto. Se tuvo que reestructurar manualmente la jerarquía de componentes para incorporar las tarjetas redondeadas con sombras sutiles, los fondos pastel característicos de cada categoría y los paddings de barra de estado (`statusBarsPadding`) para evitar solapamientos con la cámara del dispositivo.

### 3. ¿Consideras que el uso de IA mejoró tu productividad o tu comprensión del código? Justifica tu postura.
* **Productividad:** **Sí, notablemente.** Para tareas de utilidades algorítmicas (formateadores, filtros de colecciones y cálculo de fechas), la IA aceleró el proceso de entrega al proporcionar borradores funcionales en segundos.
* **Comprensión del código:** La IA ayuda a descubrir componentes o APIs modernas que uno podría no tener presentes (como `DayOfWeek`, `TextStyle` o `remember` con múltiples claves de dependencia). Sin embargo, esto solo fortalece la comprensión si el desarrollador **analiza, depura y refactoriza críticamente** el código en lugar de copiarlo a ciegas. Si se acepta el código sin revisarlo, la comprensión disminuye y se introducen fallos sutiles de sincronización de estado.
