# Day 8: Playground

El desafío de hoy se sitúa en un entorno tridimensional donde se debe conectar cajas de conexiones eléctricas utilizando la menor cantidad de cable posible. Esto se traduce en un problema clásico de grafos: la construcción de un Árbol de Expansión Mínima (Minimum Spanning Tree - MST).
En la Parte A, detenemos la construcción tras un número límite de conexiones para analizar los sub-circuitos resultantes. En la Parte B, el objetivo algorítmico cambia a unificar completamente la red hasta formar un único circuito continuo.

## Fundamentos de la Ingeniería del Software

* **Abstracción:** Consiste en ocultar los detalles complejos detrás de una interfaz simple. Hemos creado la interfaz `NetworkTracker` para que los ejecutores de alto nivel ignoren por completo la complejidad de los arreglos matemáticos subyacentes de la estructura *Union-Find*.


* **Modularidad:** El software se divide en módulos que pueden ser desarrollados y probados de forma independiente, permitiendo que sean aprovechados para otros proyectos. Las entidades físicas se extrajeron a un paquete común, mientras que los orquestadores lógicos residen en los paquetes independientes `a` y `b`.


* **Código Expresivo (Good Naming):** Consiste en asignar nombres claros, significativos y relacionados con su propósito a clases y métodos para mejorar la expresividad del código. Se han evitado nombres genéricos, prefiriendo conceptos de dominio exactos como `CircuitAnalyzer` y `NetworkUnifier`.



## Principios de Diseño (SOLID)

El diseño de la arquitectura cumple estrictamente con los 5 principios SOLID:

* **Single Responsibility Principle (SRP):** Cada clase debe tener una única responsabilidad o razón para cambiar, favoreciendo la cohesión. El modelo `CircuitNode` se encarga exclusivamente del estado espacial, `WireOptimizer` de la combinatoria de cables, y `DisjointSetTracker` de la agrupación matemática de conjuntos disjuntos.


* **Open/Closed Principle (OCP):** Las clases deben estar abiertas a la extensión pero cerradas a la modificación, permitiendo añadir funcionalidad sin alterar el código existente. Si en el futuro se requiere un algoritmo diferente al *Union-Find* para agrupar nodos, podemos crear una nueva clase que implemente `NetworkTracker` sin necesidad de tocar ni una sola coma de `CircuitAnalyzer` o `NetworkUnifier`.


* **Liskov Substitution Principle (LSP):** Los objetos de una subclase deben poder reemplazar a los de su superclase sin alterar el funcionamiento del programa, garantizando consistencia e interoperabilidad. Gracias al diseño por contrato, cualquier estructura de datos topológica que implemente la interfaz `NetworkTracker` puede ser inyectada en los orquestadores garantizando la sustitución segura de componentes.


* **Interface Segregation Principle (ISP):** No se debe obligar a una clase a implementar interfaces que no utiliza, reduciendo el acoplamiento y favoreciendo la especialización. La interfaz `NetworkTracker` es completamente minimalista: expone únicamente los 3 métodos estrictamente necesarios para la topología de grafos (`linkNodes`, `getClusterSizes`, `getRemainingClusters`), sin mezclar conceptos de cálculo de distancias o parseo de texto.


* **Dependency Inversion Principle (DIP):** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones. Las clases orquestadoras (`CircuitAnalyzer` y `NetworkUnifier`) no están acopladas a la implementación real de los arreglos (`DisjointSetTracker`), sino que dependen exclusivamente del contrato abstracto de la interfaz.



## Técnicas y Patrones de Diseño

* **Inyección de Dependencias:** Consiste en separar la creación de objetos de su uso; en lugar de que una clase cree sus dependencias, estas son proporcionadas desde fuera, facilitando la prueba del código. El tracker topológico es instanciado en las clases de Test e inyectado por constructor a los analizadores lógicos.


* **Factory Method:** Patrón creacional que encapsula la creación de objetos mediante un método estático, en lugar de usar directamente el constructor. Implementado en `CircuitNode.fromLine()` para controlar y abstraer el parseo de los datos crudos.


* **Inmutabilidad del Modelo:** El estado de las clases no debe cambiar una vez creado, lo que evita errores relacionados con efectos secundarios. Se utilizaron `records` en Java (`CircuitNode` y `Wire`) para modelar las entidades de forma puramente inmutable.



## Paradigmas y Gestión de Memoria

* **Gestión del Heap vs Stack (Optimización Matemática):** Los objetos instanciados se almacenan dinámicamente en el Heap, requiriendo del Garbage Collector para su limpieza, mientras que los primitivos operan de manera muy eficiente en el Stack. Para calcular la distancia espacial, se evitó el uso de `Math.sqrt()` y de objetos envoltorios (`Double`), calculando la distancia al cuadrado puramente con tipos primitivos `long`. Esto garantiza máxima precisión algorítmica y un coste de memoria nulo.


* **Programación Funcional (API de Streams):** Los Streams no almacenan datos, sino que describen operaciones inmutables bajo la lógica FILTER -> MAP -> REDUCE.


* Se utilizan **operaciones intermedias** como `sorted` y `limit` para restringir el número de elementos procesados.


* Se culminan los flujos con **operaciones finales** como `toList` (para volcar el resultado a una colección estática) y `reduce` (para combinar todos los elementos en uno solo, resolviendo la multiplicación de los clústeres más grandes).
