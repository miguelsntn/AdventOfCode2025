# Day 5: Cafeteria (Sistema de Inventario)

El problema introduce un sistema de inventario basado en rangos numéricos que determinan la frescura de los ingredientes. En la Parte A, el objetivo es validar una lista de identificadores sueltos contra una serie de rangos permitidos. En la Parte B, el requerimiento evoluciona hacia el cálculo del área total cubierta por el sistema, lo que exige resolver el solapamiento de rangos de manera eficiente para evitar duplicidades en el conteo.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `InventorySystem` abstrae por completo la matemática y los flujos subyacentes. El cliente (los tests) simplemente inyecta bloques de texto y llama a `countFreshIngredients()` o `countTotalFreshCapacity()`, ignorando por completo la existencia de algoritmos de fusión o *Custom Collectors*.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: El concepto geométrico/matemático de un rango se ha encapsulado en la entidad pura `FreshRange`. Esta clase es la única que sabe cómo evaluar si un número cae dentro de sus límites (`contains`), si toca a otro rango (`connectsWith`) o cómo crear un nuevo rango combinado (`merge`), blindando su estado `start` y `end` (declarados como `final`).
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: Existe una separación radical de responsabilidades. La lectura física del archivo y el saneamiento del texto recaen exclusivamente en la capa de infraestructura (Tests). Los modelos de dominio (`InventorySystem`, `FreshRange`) operan puramente en memoria, logrando un acoplamiento nulo con el sistema de archivos o la consola.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: Hemos evitado crear una clase todoterreno. Si cambia el algoritmo de fusión, se modifica el servicio orquestador (`InventorySystem` en `b`). Si cambian las reglas matemáticas de los límites de un rango, se modifica la entidad `FreshRange`.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: El algoritmo de validación unitaria (Parte A) y el algoritmo de fusión de intervalos (Parte B) operan sobre lógicas muy distintas. Para respetar el OCP, el paquete original `a` queda cerrado a alteraciones. En su lugar, se extiende el sistema creando un nuevo servicio `InventorySystem` en el paquete `b`.
* **Don't Repeat Yourself (DRY)** *(Evitar duplicación de lógica)*: Dado que la estructura matemática de un rango es idéntica en ambos problemas, la clase `FreshRange` fue extraída al paquete raíz (`software.aoc.day05`). Esto permite compartir un único modelo robusto de dominio entre ambas implementaciones.
* **Good Naming** *(Nombres descriptivos y precisos)*: Nombres como `connectsWith()`, `mergeOverlappingRanges()`, `countTotalFreshCapacity()` y `fromRanges()` documentan el código implícitamente, eliminando la necesidad de comentarios explicativos.

## Técnicas y Patrones

* **Programación Defensiva y Data Sanitization (Saneamiento de Datos)**: Para evitar excepciones críticas durante el parseo masivo (ej. `NumberFormatException`), la lectura del archivo implementa saneamiento. Se eliminan los retornos de carro corruptos propios de Windows (`\r`) y se usa un split estricto por salto de línea doble (`split("\n\\s*\n", 2)`). Esto garantiza que la capa de dominio reciba un texto estandarizado e inmaculado.
* **Custom Collector (Patrón de Acumulación Avanzada)**: En lugar de resolver la fusión de intervalos (Merge Intervals) con bucles imperativos sucios, se implementó un `Collector.of(...)` personalizado. Este componente encapsula la lógica de estado (comprobar el solapamiento con el último rango procesado) manteniéndola oculta y permitiendo que el flujo principal se lea de forma pura y matemática.
* **Copias Defensivas (Defensive Copying)**: Al instanciar `InventorySystem`, se inyectan los datos usando `List.copyOf()`. Esto sella la colección, previniendo mutaciones o efectos secundarios (side-effects) desde el exterior y garantizando la inmutabilidad del orquestador.
* **Prevención de Desbordamiento (Overflow)**: Los identificadores de la base de datos industrial de los elfos exceden la capacidad estándar de los enteros de 32 bits. Se utiliza el tipo primitivo `long` y `mapToLong()` de manera consistente en todo el sistema.

## Paradigmas

* **Orientación a Objetos (Rich Domain Model)**: `FreshRange` no es un modelo anémico (un simple contenedor de datos), sino un *Modelo de Dominio Rico*. Es capaz de validarse instanciándose a través de su Factory Method (`from`), implementar su propio orden natural (`Comparable`), y exponer comportamiento encapsulado (como generar versiones fusionadas de sí mismo).
* **Programación Funcional Avanzada (Estilo Declarativo)**:
* En la **Parte A**, el sistema procesa los identificadores crudos con un *Pipeline Funcional* clásico (`lines().map().filter().count()`).
* En la **Parte B**, el paradigma se lleva al extremo. En lugar de iteradores mutables (`currentStart`, `currentEnd`), la recolección, fusión geométrica y suma de áreas de los intervalos se logra concatenando operaciones declarativas sobre el *Stream*, delegando todo el manejo del estado al *Custom Collector* (`mergeOverlappingRanges`). Todo el algoritmo se lee como lenguaje natural, qué se quiere lograr en vez del cómo.