# Day 10: Factory (Optimización Matemática y Programación Lineal)

El desafío de hoy comenzó como un clásico problema de optimización combinatoria y se transformó en una brutal lección sobre la **explosión de estados**.

En la Parte A, se debía encontrar la combinación mínima de botones para encender y apagar luces mediante lógica de bits (operaciones XOR). En la Parte B, los botones sumaban voltajes enteros a múltiples contadores. Intentar resolver la Parte B explorando el espacio de estados paso a paso provocó un inevitable desbordamiento. La solución requirió abandonar la exploración algorítmica tradicional y modelar el problema como un **Sistema de Ecuaciones Lineales** optimizado mediante Programación Lineal Entera (ILP).

## Fundamentos de la Ingeniería del Software

* **Modularidad y DRY (Don't Repeat Yourself):** El software se divide en módulos que pueden ser desarrollados y probados de forma independiente. Las estructuras de datos comunes y el parseo de los diagramas de las máquinas se han unificado en un `Record` compartido (`MachineBlueprint`) para evitar duplicar código en ambas partes del problema.


* **Abstracción Matemática:** En lugar de indicarle al programa *cómo* buscar la solución paso a paso, abstrajimos el problema definiendo sus restricciones matemáticas (una matriz de adyacencia de botones multiplicada por el vector de pulsaciones debe ser igual al vector objetivo).
* **Ocultación de Información (Information Hiding):** La inmensa complejidad del motor de álgebra lineal y optimización está completamente encapsulada. El código cliente y las pruebas solo interactúan con la interfaz limpia `MachineOptimizer` y el método `calculateMinimumPresses()`, ignorando por completo que debajo operan motores matemáticos iterativos.


* **Eficiencia Espacial (Memoria vs CPU):** Se sustituyó un algoritmo de búsqueda exhaustiva que consumía gigabytes de RAM por un enfoque iterativo y de matrices con costes de memoria acotados, delegando el peso computacional a ciclos matemáticos eficientes en el Stack.

## Principios de Diseño

El diseño arquitectónico de este día se rige estrictamente por los **5 principios SOLID**, garantizando una alta cohesión, un bajo acoplamiento y una extensibilidad robusta:

* Single Responsibility Principle (SRP): Cada clase o módulo tiene una única razón para cambiar.


* `MachineBlueprint` se encarga exclusivamente de la representación de los datos inmutables y su parseo.
* `LightOptimizer` asume la única responsabilidad de resolver las combinaciones de luces mediante operadores de bits.
* `JoltageOptimizer` se centra únicamente en la resolución del sistema lineal de voltajes.
* Asimismo, dentro de este último, dividimos la lógica algorítmica en clases internas estáticas ultra-especializadas: `Simplex` (resolución de relajación lineal) y `BranchAndBound` (orquestación de restricciones enteras).


* Open/Closed Principle (OCP): Las entidades de software deben estar abiertas a la extensión pero cerradas a la modificación. Cuando las reglas físicas de la máquina cambiaron radicalmente en la Parte B (pasando de luces lógicas a contadores de voltaje), no modificamos ni alteramos el código existente de la Parte A. En su lugar, extendimos el sistema implementando una nueva estrategia de cálculo (`JoltageOptimizer`) sobre la abstracción compartida.


* Liskov Substitution Principle (LSP): Los objetos de una subclase o implementación deben poder reemplazar a los de su tipo base sin alterar la correctness del programa. Cualquier componente del sistema puede invocar indistintamente a `LightOptimizer` o a `JoltageOptimizer` a través de la interfaz base, garantizando una interoperabilidad y sustitución segura.


* Interface Segregation Principle (ISP): Los clientes no deben ser forzados a depender de interfaces que no utilizan. La interfaz `MachineOptimizer` es altamente cohesiva y minimalista; expone exclusivamente el método necesario para operar (`calculateMinimumPresses`), ocultando por completo las pesadas estructuras matriciales y los algoritmos internos al resto de la aplicación.


* Dependency Inversion Principle (DIP): Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones. Los sistemas ejecutores de alto nivel y las pruebas unitarias no están acoplados a las implementaciones matemáticas concretas, sino que dependen contractualmente de la interfaz abstracta `MachineOptimizer`.



## Técnicas y Patrones de Diseño

* **Factory Method:** Patrón creacional que encapsula la instanciación de objetos mediante un método estático, en lugar de invocar directamente al constructor. Implementado en `MachineBlueprint.parse()` para centralizar el procesamiento del texto mediante expresiones regulares (`Matcher` y `Pattern`).


* **Inmutabilidad del Modelo:** El estado de las clases no debe cambiar una vez creado, previniendo efectos secundarios indeseados. La entidad `MachineBlueprint` se ha modelado como un `Record` de Java, blindando sus listas internas mediante copias defensivas (`List.copyOf`).


* **Programación Lineal Entera (ILP - Integer Linear Programming):** Técnica de optimización de la Parte B para minimizar una función objetivo ($\sum x_j$) sujeta a restricciones lineales ($A \cdot x = \text{target}$).
* **Branch and Bound (Ramificación y Acotación):** Ramifica el problema cada vez que el optimizador lineal devuelve fracciones, dividiendo la búsqueda en sub-problemas hasta garantizar soluciones enteras.
* **Backtracking Recursivo con Poda (Pruning):** Utilizado en la Parte A para explorar el árbol de decisiones de bits (operadores XOR), descartando de inmediato cualquier rama con un coste superior al mejor récord actual.
