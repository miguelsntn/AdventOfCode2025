# Day 5: Cafeteria

El problema nos introduce a un sistema de inventario basado en rangos numéricos que determinan la frescura de los ingredientes. En la Parte A, el objetivo es validar una lista específica de identificadores contra una serie de rangos permitidos. En la Parte B, el requerimiento evoluciona hacia el cálculo del total absoluto de identificadores válidos en el sistema, lo que exige manejar el solapamiento de rangos para evitar duplicidades en el conteo.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `InventorySystem` abstrae por completo la matemática detrás de los rangos. El cliente solo invoca `countFresh()` o `countTotalFresh()` sin necesidad de saber si internamente se está iterando una lista o aplicando un algoritmo de ordenamiento y fusión.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: El concepto de límite inferior y superior se ha encapsulado en la clase interna `FreshRange`. Esta clase es la única que sabe cómo evaluar si un número está dentro de sus propios límites, evitando exponer variables de inicio y fin al resto del sistema.
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: La responsabilidad de interpretar el formato del archivo de texto (y saber qué hacer cuando aparece una línea en blanco) recae exclusivamente en las clases de Test. El modelo de dominio (`InventorySystem`) recibe colecciones de datos ya procesados, reduciendo su acoplamiento con el sistema de archivos.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: En la Parte B, el orquestador (`Day05BTest`) asume la responsabilidad de detener la lectura del archivo al encontrar la línea en blanco. De este modo, optimiza el rendimiento ignorando datos que ya no son relevantes para el nuevo caso de uso.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La solución de la Parte A se mantuvo intacta. Para la Parte B, se extendió la capacidad de la clase interna `FreshRange` implementando la interfaz `Comparable`, lo que permitió dotarla de un orden natural sin alterar su función original de validación.
* **Good Naming** *(Nombres descriptivos y precisos)*: Variables como `currentStart`, `currentEnd` y métodos como `countTotalFresh` documentan implícitamente el estado del algoritmo, facilitando el seguimiento lógico de la fusión de intervalos.

## Técnicas y Patrones

* **Algoritmo de Fusión de Intervalos (Interval Merging)** *(Técnica algorítmica)*: Para resolver la Parte B de forma eficiente y evitar iterar sobre millones de números individuales, se ordenaron los rangos por su punto de inicio ($O(N \log N)$) y se fusionaron aquellos que eran adyacentes o se solapaban. Esto permite calcular el área total cubierta mediante simples restas matemáticas ($O(N)$).
* **Copias Defensivas** *(Técnica de seguridad)*: Al instanciar `InventorySystem`, se crea una nueva lista a partir de los datos recibidos antes de aplicar la ordenación (`Collections.sort()`). Esto garantiza que la colección original del cliente no sufra mutaciones indeseadas por efectos secundarios.
* **Prevención de Desbordamiento (Overflow)** *(Buena práctica)*: Dado que los identificadores de un sistema a nivel industrial pueden exceder el límite de $2^{31}-1$, se ha utilizado el tipo de dato `long` para garantizar la precisión matemática en cálculos con números extraordinariamente grandes.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: Los rangos no se tratan como pares de números primitivos dispersos, sino como objetos `FreshRange` con identidad, comportamiento (`contains()`) y capacidad de compararse entre sí (`compareTo()`).
* **Programación Funcional** *(Estilo declarativo basado en funciones puras y datos inmutables)*: En la Parte A se utiliza la API de Streams (`stream().filter(...).count()`) para procesar la colección de identificadores, expresando la intención del código (qué se quiere lograr) en lugar de la mecánica del bucle (cómo lograrlo).