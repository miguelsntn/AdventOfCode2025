# Día 5: Cafetería

## 1. Diferencias entre la Parte A y la Parte B

El problema presenta un sistema de inventario basado en rangos numéricos de frescura, y su evolución exige un cambio algorítmico profundo:

* **Parte A (Búsqueda Estática):** El objetivo es validar una lista de identificadores sueltos contra una serie de rangos permitidos. Se trata de un problema clásico de búsqueda e intersección, donde cada ID se evalúa de forma independiente para comprobar si está contenido dentro de algún intervalo válido.
* **Parte B (Fusión de Intervalos / Merge Intervals):** La lista de ingredientes individuales desaparece. El requerimiento evoluciona hacia el cálculo del área matemática total cubierta por el sistema (la "capacidad total"). Como los rangos pueden solaparse o ser contiguos, el reto algorítmico consiste en fusionar espacialmente los intervalos superpuestos de manera eficiente para evitar duplicidades masivas en el recuento final.

## 2. Lógica Estructural

Para resolver este problema sin incurrir en fugas de memoria o cuellos de botella por recolección de basura (*Memory Churn*), se estructuró un diseño apoyado en la pureza de los modelos:

* **`FreshRange` (Clase de Dominio Rica):** No es un modelo anémico. Posee la inteligencia espacial para calcular su propia extensión matemática (`size()`), detectar colisiones limítrofes (`connectsWith()`), ordenar los rangos por su punto de origen (`compareTo()`) y generar nuevas instancias inmutables fusionadas (`merge()`).
* **`InventorySystem` (Capa de Servicio Orquestadora):** Centraliza el flujo del negocio. Convierte el texto plano a modelos de dominio validados y orquesta la evaluación. En la Parte A, verifica las contenciones. En la Parte B, ejecuta el algoritmo de fusión de intervalos de alto rendimiento.

## 3. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Responsabilidades blindadas. Si cambia la matemática de fusión (cómo se calcula el área de colisión), se modifica exclusivamente `FreshRange`. Si cambia cómo se totalizan los rangos de toda la base de datos, se modifica `InventorySystem`. No existen clases todoterreno.
* **Open/Closed Principle (OCP):** En lugar de contaminar el código original con sentencias `if(esParteB)` para cambiar de la búsqueda estática a la fusión, el paquete `a` queda estrictamente cerrado. El sistema se extendió alojando las nuevas reglas de fusión en un orquestador adaptado en el paquete `b`.
* **Liskov Substitution Principle (LSP) y Composición (COI):** El modelo no hereda de estructuras de datos estándar (no hace un `extends ArrayList`). La composición garantiza que el orquestador tiene el control absoluto de sus listas inmutables (`List.copyOf()`), asegurando que clientes externos no corrompan el estado inyectado.
* **Interface Segregation Principle (ISP):** Exposición mínima. `FreshRange` no expone sus límites matemáticos de forma indiscriminada. El cliente externo interactúa mediante promesas lógicas como `contains()` o `connectsWith()`, cumpliendo la Ley de Demeter (LoD).
* **Dependency Inversion Principle (DIP):** El orquestador opera independientemente de la fuente de los rangos. Recibe la información a través de métodos Factory estáticos (`fromRanges`), desacoplando la lógica de negocio de los sistemas I/O o discos duros.

## 4. Fundamentos, Técnicas y Patrones

* **Abstracción:** El orquestador `InventorySystem` expone métodos declarativos y limpios como `countTotalFreshCapacity()`. Toda la pesada algoritmia de ordenación previa, los bucles de evaluación de solapamiento y la creación de listas temporales quedan abstraídas y ocultas a las clases consumidoras.
* **Encapsulamiento y Ocultación de Estado:** El rango (`FreshRange`) blinda sus fronteras espaciales (`start` y `end`) declarándolas `final` e inmutables tras pasar por el constructor privado.
* **Don't Repeat Yourself (DRY):** Al notar que la entidad topológica era idéntica para ambas lógicas, la clase `FreshRange` fue consolidada en el paquete raíz (`software.aoc.day05`), compartiendo el modelo inmutable sin duplicar las lógicas de validación o parseo de cadenas.
* **Programación Defensiva (Data Sanitization):** Se implementa sanitización de datos masiva en las factorías para mitigar entradas erróneas (`\r`, cadenas vacías).
* **Prevención de Desbordamiento (Overflow):** Dado que los sistemas logísticos manejan capacidades altísimas, el sistema se protegió estandarizando los flujos a primitivos de 64 bits (`long`, `mapToLong()`), evitando excepciones de tipo `Integer Overflow`.
* **Factory Method (Creacional):** La inicialización está restringida por métodos `from()` y `fromRanges()`. Estos actúan como filtros guardianes que rechazan cadenas anómalas e instancian objetos en estados matemáticamente seguros.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** usando **JUnit 5** y **AssertJ**.
Se aplicó la estructura semántica **Given-When-Then** propia del **Behavior-Driven Development (BDD)**, utilizando como contexto los ejemplos base del problema original para probar los flujos sin acoplar los tests al sistema de archivos local:

* **Test Parte A:** Se verifica el comportamiento del motor de cruce estático, evaluando si el recuento de los ingredientes individuales coincide matemáticamente con los intervalos de frescura activos.
* **Test Parte B:** Se somete el algoritmo de fusión (*Merge Intervals*) a estrés, asegurándose de que logre detectar solapamientos totales, solapamientos parciales e intervalos limítrofes, consolidando correctamente la capacidad del inventario.
* El resultado esperado para la Parte A es 640 y para la Parte B es 365804144481581.