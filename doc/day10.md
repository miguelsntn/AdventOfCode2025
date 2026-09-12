# Day 10: Factory

El desafío de hoy comenzó como un clásico problema de optimización combinatoria y se transformó en una brutal lección sobre la **explosión de estados**.

En la Parte A, debíamos encontrar la combinación mínima de botones para encender y apagar luces (operaciones XOR). En la Parte B, el problema evolucionó: los botones sumaban voltajes enteros a múltiples contadores. Intentar resolver la Parte B explorando el espacio de estados paso a paso (Búsqueda en Anchura - BFS) provocó un inevitable `OutOfMemoryError`. La solución requirió abandonar la exploración algorítmica tradicional y modelar el problema como un **Sistema de Ecuaciones Lineales**.

## Fundamentos

* **Abstracción Matemática** *(Modelado declarativo)*: En lugar de decirle al programa *cómo* buscar la solución paso a paso (pulsar el botón 1, luego el 2, etc.), abstrajimos el problema definiendo sus restricciones matemáticas (una matriz de adyacencia de botones multiplicada por el vector de pulsaciones debe ser igual al vector objetivo).
* **Ocultación de Información (Information Hiding)**: La inmensa complejidad del motor de álgebra lineal está completamente escondida. El código cliente (`Day10BTest` y `FactorySystem`) solo ve el método `machine.getMinPresses()` y recibe un humilde entero, ignorando por completo que debajo hay un motor matemático iterativo.
* **Eficiencia Espacial (Memoria vs CPU)**: Cambiamos un algoritmo que consumía gigabytes de RAM guardando estados (BFS) por un algoritmo iterativo que usa matrices de tamaño fijo y apenas consume unos kilobytes de memoria, delegando el peso a ciclos matemáticos de la CPU.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase, una razón para cambiar)*: Dentro del modelo de dominio `Machine` de la Parte B, dividimos la lógica en clases estáticas internas muy enfocadas:
* `Simplex`: Su única responsabilidad es resolver la versión fraccional del sistema de ecuaciones (relajación lineal).
* `BranchAndBound`: Su única responsabilidad es orquestar llamadas a `Simplex` forzando que los resultados sean números enteros (no podemos pulsar un botón 0.5 veces).


* **Separation of Concerns (SoC)**: El parseo del texto usando Expresiones Regulares (Regex) está completamente separado de la creación de las matrices matemáticas.

## Técnicas y Patrones

* **Programación Lineal Entera (ILP - Integer Linear Programming)**: La técnica estrella de la Parte B. Modelamos el problema como la minimización de una función objetivo ($\sum x_j$) sujeta a restricciones lineales ($A \cdot x = \text{target}$).
* **Algoritmo Simplex (Método de la Gran M)**: Un algoritmo clásico de optimización matemática que navega por los vértices del politopo geométrico formado por las restricciones hasta encontrar el punto de coste mínimo.
* **Branch and Bound (Ramificación y Acotación)**: Dado que Simplex puede devolver resultados con decimales, este algoritmo "ramifica" el problema cada vez que encuentra una fracción (ej. si el botón 1 da $2.5$, divide la búsqueda en dos universos paralelos: uno donde el botón se pulsa $\le 2$ veces y otro donde se pulsa $\ge 3$ veces).
* **Backtracking Recursivo con Poda (Pruning)**: Utilizado en la Parte A. Explora el árbol de decisiones de forma recursiva, pero "corta" (poda) inmediatamente cualquier rama que ya haya consumido más pulsaciones que el mejor récord encontrado, ahorrando millones de cálculos innecesarios.

## Paradigmas

* **Programación Declarativa (Matemática)**: A diferencia de la programación imperativa usada en días anteriores (donde controlamos el flujo de estado), en la Parte B le "declaramos" al motor `Simplex` cuáles son las reglas y condiciones de éxito, y el algoritmo general se encarga de resolverlo.
* **Orientación a Objetos (OO)**: A pesar de la fuerte carga algorítmica, mantuvimos una arquitectura limpia encapsulando los vectores, costos y estados de optimalidad en objetos estructurados (`Constraint`, `Result`), evitando pasar docenas de variables sueltas entre métodos.