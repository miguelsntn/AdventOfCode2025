# Día 3: Lobby 

## Definición del Problema

La historia nos sitúa en el vestíbulo del Polo Norte. Los ascensores y escaleras mecánicas se han quedado sin energía. Para reactivarlos, debemos conectar "bancos de baterías de emergencia".

Los datos de entrada son múltiples líneas de texto. Cada línea representa un **banco de baterías** compuesto por dígitos (ej. `987654321111111`). La regla de oro es que **no podemos reordenar los dígitos**, solo podemos "encender" (seleccionar) algunos de ellos manteniendo su orden original de izquierda a derecha para formar el número más alto posible.

## 1. Diferencias entre la Parte A y la Parte B

La evolución de los requisitos entre ambas partes ilustra la flexibilidad del diseño y exige un cambio algorítmico profundo:

* **Parte A:** El sistema requiere encender exactamente **2 baterías** por banco. Al buscar solo dos elementos, un enfoque de división directa del *array* (buscar el máximo y luego buscar el segundo mayor a su derecha) parece viable, aunque se puede optimizar.


* **Parte B:** La fricción exige encender exactamente **12 baterías** por banco. Esto cambia radicalmente la escala del problema, pasando de buscar un entero simple a un valor de 12 cifras (obligando al uso de enteros de 64 bits `long`). Un enfoque iterativo simple fallaría, ya que en cada paso debemos asegurarnos de que queden suficientes dígitos en la cadena para completar la cuota de 12.



## 2. Lógica Estructural

Se implementó una arquitectura basada en el patrón *Service Layer*:

* **`BatteryBank` (Record):** Modelo de dominio anémico. Su única responsabilidad es almacenar la secuencia de texto inmutable y validarla.


* **`EmergencyPowerSystem` (Record):** Actúa como el orquestador y la capa de servicio lógico. Se encarga de procesar el texto en bruto y concentra toda la responsabilidad matemática (los algoritmos de optimización) para sumar el voltaje final.

## 3. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** `BatteryBank` es un modelo puramente de almacenamiento (almacena y valida el texto), mientras que `EmergencyPowerSystem` asume la responsabilidad de la lógica algorítmica y orquestación del agregado, aislando las matemáticas de la topología del dato.


* **Open/Closed Principle (OCP):** En lugar de contaminar el orquestador de la Parte A con sentencias `if(esParteB)`, se empaquetó la solución masiva de los 12 dígitos en una nueva extensión del dominio (`software.aoc.day03.b`), dejando la Parte A intacta, cerrada y a salvo.


* **Liskov Substitution Principle (LSP) y Composition (COI):** El sistema favorece la composición (`List<BatteryBank> banks`) frente a la herencia. Al mantener una estructura plana (sin clases base con herencias), garantizamos que cualquier cambio subyacente o inyección de listas personalizadas funcionará correctamente.


* **Interface Segregation Principle (ISP):** Exposición minimalista. `BatteryBank` expone únicamente el *getter* de su *string* (mediante el contrato nativo de un *record*), ocultando detalles de inicialización.
* **Dependency Inversion Principle (DIP):** Las lógicas de orquestación ignoran por completo cómo se carga la información. Las pruebas unitarias de alto nivel actúan como un cliente que inyecta los `String` crudos en el constructor, desacoplando completamente el dominio algorítmico del sistema de ficheros y Entrada/Salida.

## 4. Fundamentos y Clean Code

* **Abstracción:** Toda la extrema complejidad algorítmica (los *Suffix Arrays* y *Monotonic Stacks*) queda encapsulada. El cliente solo llama a `calculateTotalOutputJoltage()` y obtiene su resultado.


* **Don't Repeat Yourself (DRY):** Al notar que la entidad fundamental (la batería cruda) era idéntica para ambas lógicas, el registro `BatteryBank` se alojó en la carpeta raíz (`software.aoc.day03`), compartiendo el modelo inmutable.


* **Law of Demeter (LoD):** En el ciclo de evaluación, `EmergencyPowerSystem` asume la responsabilidad algorítmica directamente sobre la capa transitoria que él mismo controla, sin exigir estados anidados complejos.


* **Programación Imperativa vs Declarativa (KISS):** A diferencia de forzar la API de Streams (`reduce`, `iterate`) para algoritmos densos, se decidió orquestar la separación de los algoritmos matemáticos usando bucles `for` estructurados y control de estado imperativo (`StringBuilder`). Un bucle `for` aquí hace que el control de flujo y la complejidad espacial sean transparentes, cumpliendo con el principio **Keep It Simple, Stupid (KISS)**.



## 5. Paradigmas, Técnicas y Patrones

* **Patrón Creacional (Factory Method):** La instanciación de objetos está protegida mediante el método estático `from()`. Este método actúa como aduana, ejecutando saneamiento defensivo de datos (`String::trim`, verificación contra cadenas nulas o insuficientemente largas), lo que previene excepciones silenciosas.


* **Inmutabilidad Estricta:** El modelo `BatteryBank` es un *Record* en Java, impidiendo mutaciones en su estado. Además, la lista del orquestador se blinda utilizando `List.copyOf()` (en el Día A) o directamente `.toList()`, asegurando que no se sufra corrupción de datos.


* **Robustez y Tolerancia a Desbordamientos:** Se previó la explosión numérica al pasar de 2 a 12 cifras (Parte B), escalando los contenedores de enteros convencionales a enteros de 64 bits (`long`), mitigando proactivamente excepciones del tipo *Integer Overflow*.

## 6. Verificación y Tests (BDD)

Las soluciones se validan de forma automática mediante **pruebas unitarias** escritas con JUnit 5 y aserciones de AssertJ.

* Los tests se estructuran semánticamente bajo la metodología **BDD (Behavior-Driven Development)** usando el patrón **Given-When-Then** (Dado un contexto, Cuando ocurre una acción, Entonces se espera un resultado).


* **Parte A (`aTest`):** Verifica que se extraigan y maximicen matemáticamente 2 baterías, probando la solidez del *Suffix Max Array*.


* **Parte B (`bTest`):** Evalúa un escenario que demanda enteros masivos y la selección precisa de 12 dígitos, validando el comportamiento lineal del *Monotonic Stack* frente a cadenas problemáticas o fricción alta.


* El resultado esperado para la Parte A es 17301 y para la Parte B es 172162399742349.