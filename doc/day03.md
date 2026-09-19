# Day 3: Lobby (Sistema de Energía de Emergencia)

## Definición del Problema

La historia nos sitúa en el vestíbulo del Polo Norte. Los ascensores y escaleras mecánicas se han quedado sin energía. Para reactivarlos, debemos conectar unos "bancos de baterías de emergencia".

Los datos de entrada son múltiples líneas de texto. Cada línea representa un **banco de baterías** compuesto por dígitos (ej. `987654321111111`). La regla de oro es que **no podemos reordenar los dígitos**, solo podemos "encender" (seleccionar) algunos de ellos manteniendo su orden original de izquierda a derecha para formar el número más alto posible.

* **En la Parte A**, el sistema de la escalera requiere encender exactamente **2 baterías** por banco. Por ejemplo, de `811111111111119`, el número máximo de dos cifras manteniendo el orden es `89`.
* **En la Parte B**, la escalera requiere mucha más energía para superar la fricción, obligándonos a encender exactamente **12 baterías** por banco. Esto cambia radicalmente la escala del problema, pasando de buscar un simple entero de dos cifras a construir un número masivo de 12 cifras (ej. `987654321111`).

El objetivo final en ambas partes es sumar el voltaje máximo de todos los bancos y devolver el gran total.

## 1. Fundamentos de la Ingeniería del Software

* **Abstracción (Ocultar el "cómo" para exponer solo el "qué"):**
  La clase orquestadora `EmergencyPowerSystem` expone un único método público: `calculateTotalOutputJoltage()`. Las clases de Test (que actúan como clientes) solo llaman a este método y reciben el resultado final. El test no tiene ni idea de si por debajo se está utilizando un array, una pila (*stack*), bucles `for` o si se saltan líneas vacías. Toda esa complejidad algorítmica está abstraída detrás de una interfaz extremadamente simple y fácil de usar.
* **Encapsulamiento (Blindaje del estado):**
  No basta con poner los atributos en `private`; hay que proteger su integridad a lo largo del tiempo. La cadena de texto `ratings` en el modelo `BatteryBank` y la lista de bancos en `EmergencyPowerSystem` son `private final`. Una vez que los objetos se instancian, el lenguaje Java garantiza que su estado interno está blindado y no puede ser alterado ni corrompido accidentalmente por otras partes del programa.
* **Modularidad (Dividir para vencer):**
  El sistema no es un bloque de código espagueti. Está dividido en dos módulos claramente diferenciados: un módulo de dominio o datos (`BatteryBank`) y un módulo de servicios lógicos (`EmergencyPowerSystem`). Esto permite aislar los fallos y probar cada pieza por separado.
* **Alta Cohesión y Bajo Acoplamiento:**
* *Alta cohesión:* Cada clase está hiperenfocada. `BatteryBank` solo sabe de sí mismo (almacenar un string y validarlo). `EmergencyPowerSystem` solo sabe de orquestar la lista y sumar.
* *Bajo acoplamiento:* Las dependencias entre clases son mínimas. El orquestador opera sobre los bancos sin depender en absoluto de cómo se leen del disco duro, aislando la lógica matemática del sistema de archivos.

## 2. Principios de Diseño

* **Open/Closed Principle - OCP (Abierto a la extensión, cerrado a la modificación):**
  Este es el principio arquitectónico estrella del diseño. El OCP dicta que un sistema debe permitir añadir nuevo comportamiento sin modificar el código que ya funciona y está testado.
* *¿Cómo se habría violado?* Si hubiera usado una sola clase `EmergencyPowerSystem` para ambas partes, habría tenido que entrar a modificarla, añadiendo sentencias `if (esParteB)` para cambiar de algoritmo, arriesgándome a introducir *bugs* en la Parte A.
* *¿Cómo lo he aplicado?* Dejé el orquestador original cerrado y a salvo en el paquete `a`. Para resolver los nuevos requisitos de los 12 dígitos, **extendí el sistema** creando un módulo completamente nuevo en el paquete `b`. De este modo, la Parte B se adapta a su nuevo algoritmo sin que la Parte A se entere siquiera de que existe.


* **Single Responsibility Principle - SRP (Principio de Responsabilidad Única):**
  Una clase debe tener solo un motivo para cambiar. En muchos diseños mediocres, el modelo de datos también realiza las matemáticas (lo que se conoce como un "Modelo Gordo"). Yo he optado por separar responsabilidades:
* `BatteryBank` actúa como un **Modelo Anémico**: su única responsabilidad es almacenar el texto. Solo cambiará si el formato físico de las baterías cambia.
* `EmergencyPowerSystem` actúa como un **Servicio**: concentra toda la responsabilidad matemática. Solo cambiará si las reglas de cálculo cambian.


* **Don't Repeat Yourself - DRY (Evitar duplicación):**
  A pesar de haber separado la orquestación en los paquetes `a` y `b` (por OCP), me di cuenta de que la estructura de los datos crudos era exactamente la misma. Copiar y pegar la clase `BatteryBank` en ambas carpetas habría sido un error de diseño. Por ello, la extraje a la carpeta raíz (`software.aoc.day03`), compartiendo el mismo modelo inmutable para todo el día y eliminando la redundancia.

## 3. Técnicas y Patrones

* **Patrón Creacional: Factory Method:**
  He prohibido el uso de constructores públicos (`public BatteryBank()`) haciéndolos privados. Para instanciar los objetos, se debe pasar por el método estático `from(String rawNotes)`. Este patrón actúa como un punto de control aduanero: limpia los espacios en blanco, verifica que las cadenas no sean nulas o excesivamente cortas, y garantiza que cualquier objeto que nazca en memoria sea 100% válido desde su creación.
* **Inmutabilidad Estricta de Colecciones:**
  Además de hacer finales los atributos, en el Factory Method del orquestador recolecto los datos en una lista temporal, pero la inyecto al constructor utilizando `List.copyOf()`. Esto genera una colección inmutable que previene que cualquier otra clase pueda hacer un `.add()` o `.remove()` malicioso en el futuro.
* **Algoritmos Diferenciados (Optimización de Rendimiento):**
  Buscar el número mayor manteniendo el orden parece fácil, pero con 12 dígitos el coste computacional se dispara.
* *Para la Parte A:* Utilizo un array precomputado de sufijos (`maxFromRight`) que me permite resolver el cruce de 2 dígitos en tiempo lineal $O(N)$.
* *Para la Parte B:* Implemento un algoritmo **Voraz (Greedy)** apoyado en un **Stack Monotónico** (usando un `StringBuilder`). Al recorrer la secuencia, si el dígito actual es mayor que el anterior guardado, lo expulsa de la pila (siempre que me queden "vidas" o descartes disponibles). Esto resuelve el problema masivo en una sola pasada $O(N)$, siendo inmensamente más eficiente que una solución recursiva o de fuerza bruta.

## 4. Paradigmas de Programación

* **Patrón Service Layer (Orientación a Objetos):**
  El software no es un script de funciones sueltas, sino un sistema real. He aplicado una arquitectura en capas donde la "Capa de Dominio" (`BatteryBank`) es inyectada en la "Capa de Servicio" (`EmergencyPowerSystem`), emulando cómo se construyen las aplicaciones empresariales reales (ej. Spring Boot).
* **Programación Imperativa vs. Declarativa (KISS):**
  A diferencia de la tendencia a forzar el uso de la API de *Streams* de Java (`.stream().map().reduce()`) para todo, he decidido conscientemente orquestar la separación de líneas y las sumas totales utilizando **bucles `for` estructurados tradicionales**. Esto cumple con el principio **KISS (Keep It Simple, Stupid)**. Un bucle `for` imperativo hace que el control de flujo sea obvio a simple vista y permite insertar puntos de ruptura (*breakpoints*) para depurar paso a paso sin la opacidad que generan las lambdas de los streams funcionales.