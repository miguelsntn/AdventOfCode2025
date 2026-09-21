# Day 12: Christmas Tree Farm

El desafío final nos ha llevado a un problema clásico y computacionalmente complejo: el **Empaquetamiento 2D** con poliminós (piezas estilo Tetris). El objetivo era determinar si un conjunto específico de regalos de formas irregulares podía encajar perfectamente en una cuadrícula bidimensional bajo un árbol, permitiendo rotaciones y volteos, pero sin apilamientos ni solapamientos.

## Fundamentos de la Arquitectura

* **Complejidad Computacional (NP-Hard):** El empaquetamiento de poliminós es un problema conocido por su explosión combinatoria. No existe una fórmula matemática rápida para resolverlo; requiere explorar exhaustivamente el espacio de soluciones. El éxito del diseño radicó en aplicar heurísticas y podas extremas para que el tiempo de ejecución no se volviera infinito.
* **YAGNI (You Aren't Gonna Need It) y KISS (Keep It Simple, Stupid):** A diferencia de otros días, este desafío carecía de una "Parte B". Anticipar su existencia separando el código en subpaquetes habría sido sobreingeniería. Se aplanó la estructura consolidando el dominio en un único paquete (`software.aoc.day12`), reduciendo la complejidad cognitiva y eliminando dependencias cruzadas innecesarias.
* **Prevención del Memory Churn (Stack vs. Heap):** Implementaciones fuertemente orientadas a objetos suelen depender de la clonación de matrices o la instanciación de clases como `BitSet` dentro del bucle recursivo. Esto satura el *Heap* de la JVM y paraliza el sistema por las constantes recolecciones de basura (*Garbage Collector Pauses*). En nuestra solución, la cuadrícula se representó como un arreglo de primitivos (`long[]`), operando íntegramente de forma contigua en el **Stack** y garantizando un rendimiento de bajo nivel excepcional.



## Principios de Diseño (SOLID)

El motor de resolución se ha diseñado respetando estrictamente los 5 principios SOLID:

* **Single Responsibility Principle (SRP):** Las responsabilidades están quirúrgicamente separadas. `FarmParser` se encarga exclusivamente del análisis léxico del texto; los *records* como `ShapeVariation` tienen la única tarea de traducir coordenadas a máscaras de bits; y `FarmAllocator` se dedica al 100% a la orquestación recursiva del algoritmo de encaje.
* **Open/Closed Principle (OCP):** El código está abierto a la extensión y cerrado a la modificación. Podemos introducir nuevas lógicas para generar variaciones de piezas (por ejemplo, piezas tridimensionales) dentro de `PresentShape` sin necesidad de alterar ni una coma del motor de búsqueda en `FarmAllocator`.
* **Liskov Substitution Principle (LSP) y Composition Over Inheritance (COI):** Se evitó por completo el uso de jerarquías de herencia profundas que pudieran violar las precondiciones del empaquetamiento. Al usar Composición (objetos inmutables que componen el estado) y *records*, garantizamos que cualquier variación geométrica inyectada se comporte predeciblemente sin alterar la corrección del programa.
* **Interface Segregation Principle (ISP) y Principio de Mínimo Compromiso:** Los componentes se comunican a través de interfaces mínimas. El orquestador `FarmAllocator` no recibe *Strings* masivos ni colecciones sucias; consume abstracciones destiladas (`TreeRegion`, `PresentShape`) que exponen únicamente los métodos necesarios para la evaluación (`getArea()`, `getVariations()`).
* **Dependency Inversion Principle (DIP):** Los módulos matemáticos de alto nivel no instancian sus propios datos leyendo el disco. Las pruebas unitarias actúan como clientes externos que inyectan el catálogo de piezas (`Map<Integer, PresentShape>`) directamente en el constructor de `FarmAllocator`, desacoplando completamente la lógica de negocio de las operaciones de Entrada/Salida.

## Técnicas y Patrones de Diseño

* **Backtracking:** El algoritmo principal intenta colocar un regalo. Si tiene éxito, se llama a sí mismo para el siguiente regalo. Si en el futuro se queda sin espacio, deshace su movimiento (*backtrack*) y prueba otra posición u otra rotación.
* **Bitwise Collision Detection (Operaciones de Bits):** En lugar de comprobar colisiones iterando celda por celda, cada fila de una variación geométrica se convierte en un número binario.
* Para comprobar si una pieza cabe, aplicamos un operador lógico `AND` (`&`) con desplazamiento `<<`.
* Para colocarla y retirarla de forma ultrarrápida (hacer y deshacer el *backtrack*), aplicamos un operador `XOR` (`^`).


## Paradigmas y Verificación

* **Programación Estructurada, Funcional y Recursiva:** Se utilizó un enfoque declarativo (API de Streams y `records`) para el parseo y modelado inmutable, combinado con llamadas recursivas de alto rendimiento para descender por el árbol de decisiones espaciales.
* **Desarrollo Guiado por Comportamiento (BDD):** El sistema fue probado rigurosamente empleando JUnit 5 y AssertJ, orquestando los contextos bajo el formato **Given-When-Then** para validar el comportamiento matemático exacto utilizando el catálogo de pruebas antes de procesar el inventario real.