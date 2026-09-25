# Advent of Code 2025

Este proyecto se basa en la resolución de los retos del **Advent of Code 2025 (Días 1 al 12)**. No se limita a encontrar la respuesta correcta a los algoritmos propuestos; es una exhibición práctica y rigurosa de los conocimientos adquiridos en **Ingeniería del Software II**. Cada día ha sido modelado, refactorizado y testeado para cumplir con los más altos estándares académicos y profesionales.

---

## Filosofía de Diseño Global

El código de todos los retos está diseñado respetando una estricta jerarquía estructural y conceptual, garantizando que el software sea mantenible, escalable, eficiente y fácil de leer.

### 1. Paradigmas de Programación

* **Orientación a Objetos (OO):** Utilizada como base estructural para modelar los dominios. Las entidades encapsulan sus propias reglas de negocio y estado, protegiendo la integridad de la topología y la geometría en los problemas más complejos.
* **Programación Funcional:** Aplicada intensivamente al flujo de control, filtrado y cálculo algorítmico mediante la **API de Streams** de Java. Estrechamente relacionada con el uso de clases inmutables para evitar efectos secundarios (*side-effects*).
* **Programación Estructurada y Matemática:** Empleada de forma estratégica en los cuellos de botella de rendimiento (ej. *Backtracking*, Búsqueda en Espacio de Estados, Programación Dinámica), donde el control imperativo sobre tipos primitivos garantiza la máxima eficiencia en la CPU.

### 2. Fundamentos de la Ingeniería

* **Abstracción:** Ocultamiento de la enorme complejidad matemática, algorítmica e infraestructural detrás de interfaces o métodos con semántica de negocio clara.
* **Modularidad:** Separación tajante entre la capa de entrada (lectura de archivos y parseo léxico) y el núcleo de cálculo matemático y orquestación.
* **Alta Cohesión y Bajo Acoplamiento:** Los objetos tienen una única razón para cambiar. Los orquestadores interactúan con los modelos inyectando los datos sin acoplarse jamás a la implementación interna de los mismos.
* **Código Expresivo (Good Naming):** Uso estricto del lenguaje del dominio para que el código se lea de forma fluida, minimizando la necesidad de comentarios explicativos.

---

## Principios de Diseño

El diseño de la arquitectura general se rige por directrices fundamentales:

* **Principios SOLID:**
* **S** (*Single Responsibility*): Clases hiper-enfocadas (ej. `FarmParser` solo lee texto, `FarmAllocator` solo calcula áreas).
* **O** (*Open/Closed*): Sistemas abiertos a extensión mediante nuevas implementaciones sin modificar el código original (esencial en la transición de las Partes A a las Partes B de cada día).
* **L** (*Liskov Substitution*) & **COI** (*Composition Over Inheritance*): Preferencia absoluta por la composición de objetos frente a jerarquías de herencia rígidas, asegurando que las abstracciones puedan ser sustituidas sin romper la correctitud del programa.
* **I** (*Interface Segregation*): Exposición de APIs minimalistas, donde el cliente solo conoce los métodos estrictamente necesarios para operar (Principio de Mínimo Compromiso).
* **D** (*Dependency Inversion*): Las clases de alto nivel no instancian sus dependencias. Reciben colecciones e interfaces a través de sus constructores (Inyección de Dependencias).


* **Don’t Repeat Yourself (DRY):** Centralización de la lógica común, matemática de coordenadas o parseo en paquetes compartidos para evitar la duplicación de conocimiento.
* **Law of Demeter (LoD):** Aplicación de la regla *"Tell, Don't Ask"*. Los componentes de gestión ordenan a los objetos ejecutar acciones, en lugar de extraer sus datos internos. Esto minimiza el acoplamiento y facilita las pruebas unitarias aisladas.
* **Keep It Simple, Stupid (KISS) y YAGNI (You Aren't Gonna Need It):** Resoluciones directas enfocadas exclusivamente en los requerimientos del día, evitando abstracciones preventivas y sobre-ingeniería para problemas que aún no existen.

---

## Técnicas y Gestión de Memoria

* **Inmutabilidad del Modelo (Records):** Uso extensivo de `records` en Java. El estado de las entidades geométricas y topológicas se fija en su creación, garantizando un entorno seguro y libre de mutaciones accidentales.
* **Inversión del Control (IoC):** Delegación del flujo algorítmico e iteraciones internas a motores externos (como la API de Streams), liberando al código de la gestión manual de estado.
* **Métodos Delegados y Refactorización Continua:** Ruptura de algoritmos masivos en métodos privados, descriptivos y cohesivos, eliminando variables temporales en favor del *inlining*.

---

## Patrones de Diseño Aplicados

* **Factory Method (Creacional):** La inicialización de objetos complejos (parseo de texto a grafos o polígonos) se delega a métodos estáticos semánticos en lugar de constructores públicos. Encapsula la lógica de validación, garantizando que el sistema trabaje exclusivamente con entidades inmutables ya validadas.
* **Strategy (Comportamiento):** Aislamiento de diferentes motores de resolución matemática bajo una misma interfaz abstracta, permitiendo intercambiar algoritmos en tiempo de ejecución.
* **Template Method (Comportamiento):** Definición del esqueleto de algoritmos (como el escaneo de matrices espaciales), permitiendo que las subclases redefinan ciertos pasos sin cambiar la estructura algorítmica fundamental.
* **State Object (Comportamiento):** Encapsulación de estados de búsqueda y navegación en entidades ricas para sistemas de memoización (ej. DFS en Grafos).
* **Closure (Funcional):** Uso de lambdas en flujos funcionales que capturan limpiamente variables locales del entorno léxico.

---

## Verificación, Tests y BDD

Las soluciones de absolutamente todos los días se validan de forma automatizada mediante **pruebas unitarias** escritas con **JUnit 5 y AssertJ**.

* Los tests están estructurados semánticamente siguiendo el patrón **Given-When-Then** (*Dado un contexto, Cuando ocurre una acción, Entonces se espera un resultado*).
* Esta estructura, heredada del enfoque **BDD (Behavior-Driven Development)**, orienta los tests a comprobar el comportamiento del sistema maximizando su legibilidad.
* Los tests actúan como **documentación ejecutable** de los requerimientos. Al inyectar funcionalmente los datos mediante `Streams` directamente en las clases de prueba, se aísla por completo el núcleo matemático de cualquier acoplamiento con el sistema de archivos del sistema operativo (I/O).

---

## Índice de Soluciones

La siguiente tabla detalla la evolución arquitectónica diaria y los patrones aplicados para resolver cada reto matemático o algorítmico:

| Día      | Título | Documentación                                                                | Código                                                                                | Principios y Patrones Aplicados |
|----------| --- |------------------------------------------------------------------------------|---------------------------------------------------------------------------------------| --- |
| **1.A**  | *Secret Entrance* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day01.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day01/a) | SRP, Factory Method. |
| **1.B**  | *Secret Entrance* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day01.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day01/b) | OCP, DRY, Inmutabilidad de Estado. |
| **2.A**  | *Gift Shop* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day02.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day02/a) | SRP, Factory Method, Expresiones Regulares. |
| **2.B**  | *Gift Shop* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day02.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day02/b) | OCP, DRY, Optimización de Primitives Streams. |
| **3.A**  | *Lobby* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day03.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day03/a) | SRP, Factory Method. |
| **3.B**  | *Lobby* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day03.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day03/b) | OCP, Monotonic Stack, Suffix Arrays. |
| **4.A**  | *Printing Department* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day04.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day04/a) | SRP, Factory Method, Matrices Primitivas encapsuladas. |
| **4.B**  | *Printing Department* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day04.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day04/b) | OCP, DRY, Double Buffering (Zero Allocation). |
| **5.A**  | *Cafeteria* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day05.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day05/a) | SRP, ISP, Búsqueda Interseccional Estática. |
| **5.B**  | *Cafeteria* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day05.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day05/b) | OCP, Custom Collector, Merge Intervals Algorithm. |
| **6.A**  | *Trash Compactor* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day06.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day06/a) | SRP, Strategy, Template Method. |
| **6.B**  | *Trash Compactor* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day06.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day06/b) | OCP, LSP, DIP, Parsing Espacial 2D. |
| **7.A**  | *Laboratories* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day07.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day07/a) | SRP, Singleton, Simulación de Estados (Classical Physics). |
| **7.B**  | *Laboratories* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day07.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day07/b) | OCP, LSP, DIP, Programación Dinámica (State-Space Reduction). |
| **8.A**  | *Playground* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day08.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day08/a) | SRP, Factory Method, Union-Find (Disjoint-Set). |
| **8.B**  | *Playground* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day08.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day08/b) | OCP, LSP, DIP, Algoritmo de Kruskal (Minimum Spanning Tree). |
| **9.A**  | *Movie Theater* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day09.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day09/a) | SRP, Programación Declarativa Funcional. |
| **9.B**  | *Movie Theater* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day09.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day09/b) | OCP, ISP, Short-Circuiting Imperativo, Algoritmo Ray-Casting. |
| **10.A** | *Factory* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day10.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day10/a) | SRP, Bitwise Operations (Máscaras y XOR). |
| **10.B** | *Factory* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day10.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day10/b) | OCP, LSP, DIP, Programación Dinámica con Memoización. |
| **11.A** | *Reactor* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day11.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day11/a) | SRP, Búsqueda en Profundidad (DFS), Directed Acyclic Graphs (DAG). |
| **11.B** | *Reactor* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day11.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day11/b) | OCP, COI, State Object Pattern, Cache/Memoización. |
| **12**   | *Christmas Tree Farm* | [Doc](https://github.com/miguelsntn/AdventOfCode2025/blob/main/doc/day12.md) | [Main](https://github.com/miguelsntn/AdventOfCode2025/tree/main/src/java/software/aoc/day12) | SRP, OCP, LSP, ISP, DIP, COI, Backtracking Funcional, Mónadas (`Optional`). |