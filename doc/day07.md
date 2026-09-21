# Day 7: Laboratories

**Parte A:** Simular el recorrido de un haz de taquiones a través de un colector bidimensional, donde el rayo se divide al chocar contra un divisor (`^`). El objetivo es calcular el número total de divisiones empleando un motor físico de mecánica clásica.

**Parte B:** Aplicar una interpretación de universos paralelos (Many-Worlds) a una única partícula. Al chocar contra un divisor, la realidad se bifurca. El objetivo es calcular la inmensa cantidad de líneas temporales activas al final del recorrido aplicando física cuántica.

## Fundamentos de la Ingeniería del Software

* **Abstracción:** Consiste en ocultar los detalles complejos detrás de una interfaz simple. La interfaz `TachyonPhysicsEngine` oculta las complejas fórmulas matemáticas, permitiendo que el cliente simplemente pida calcular un resultado sin conocer la mecánica interna.


* **Modularidad:** El software se divide en módulos que pueden ser desarrollados, modificados y probados de forma independiente. Existe una separación total entre el contenedor espacial (`TachyonManifold`) y los motores físicos.


* **Alta Cohesión:** Las partes de un módulo están estrechamente relacionadas y enfocadas a una única tarea. `TachyonManifold` se dedica exclusivamente a gestionar la topología del mapa inmutable, mientras que los motores físicos asumen únicamente las fórmulas de propagación.


* **Bajo Acoplamiento:** Los módulos tienen pocas interdependencias. `TachyonManifold` no sabe qué motor se está utilizando gracias a la abstracción.


* **Código Expresivo (Good Naming):** El código es claro, comprensible y facilita el mantenimiento sin necesitar comentarios. Se ha logrado asignando nombres claros, significativos y relacionados con su propósito a clases y métodos (ej. `ClassicalPhysicsEngine`, `simulate`, `findStart`).


## Principios de Diseño

* **Single Responsibility Principle (SRP):** Cada clase o módulo debe tener una única responsabilidad o razón para cambiar, favoreciendo la cohesión y la claridad del diseño.


* **Open/Closed Principle (OCP):** Las clases deben estar abiertas a la extensión pero cerradas a la modificación. Para integrar la física cuántica, no se alteró el `TachyonManifold`; simplemente se extendió el sistema inyectando una nueva implementación de la interfaz.


* **Dependency Inversion Principle (DIP):** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones, lo que disminuye la dependencia entre componentes. El simulador depende de la interfaz genérica `TachyonPhysicsEngine`.


* **Don't Repeat Yourself (DRY):** Evita la duplicación de código, promoviendo la reutilización. El escaneo del tablero y la búsqueda del punto de inicio se centralizan en el `TachyonManifold` para que los motores no repitan operaciones.


* **Keep It Simple, Stupid (KISS) & YAGNI:** El código debe ser claro y evitar complejidad innecesaria. En lugar de seguir millones de rayos individuales recursivamente, agrupamos las incidencias matemáticamente por coordenadas.

## Patrones de Diseño

* **Factory Method (Creacional):** Encapsula la creación de objetos mediante un método estático, en lugar de usar directamente el constructor de la clase. Implementado en `TachyonManifold.from()` para garantizar la integridad inicial de la cuadrícula inmutable.


* **Singleton (Creacional):** Restringe la instanciación de una clase a una única instancia. Se aplica en `ClassicalPhysicsEngine` y `QuantumPhysicsEngine` mediante constructores privados y un método `getInstance()`, ya que las leyes de la física no requieren múltiples representaciones en memoria.

## Técnicas y Paradigmas Avanzados

* **Inmutabilidad del modelo:** El estado de las clases no debe cambiar una vez creado, lo que favorece la abstracción y evita errores relacionados con efectos secundarios. Se utilizan colecciones estandarizadas como `List.copyOf()` para sellar la cuadrícula original.


* **Inyección de Dependencias:** Consiste en separar la creación de objetos de su uso. En lugar de que la cuadrícula cree la física, esta es proporcionada desde fuera (inyectada por el cliente de pruebas) a través del método `simulate()`, reduciendo el acoplamiento.


* **Clases Internas de Clase (Static):** Se pueden instanciar sin necesidad de una instancia de la clase externa y agrupan elementos relacionados. El `record SimulationState` se anida estáticamente dentro de su motor físico para encapsular su estado transitorio.


* **Mónadas (Patrón Funcional):** Una Mónada encapsula valores y sus operaciones dentro de un contexto. En la búsqueda del punto de inicio, se utiliza `Optional<T>` para encapsular la presencia o ausencia de un valor, evitando excepciones como `NullPointerException`.


* **Programación Funcional y Streams:** Los Streams describen operaciones sobre datos, son inmutables y siguen la lógica FILTER -> MAP -> REDUCE.


* Se emplean operaciones intermedias como `filter` (filtra según condición) y `flatMap` (aplana un stream de streams, clave para la división de universos).


* Se utilizan operaciones finales como `reduce` (combina todos los elementos en uno solo utilizando un acumulador) y `groupingBy` (agrupa elementos en un Map).
