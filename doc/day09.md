# Day 9: Movie Theater (Geometría Computacional)

El problema de hoy se introduce en el mundo de la geometría computacional sobre una cuadrícula bidimensional discreta. En la Parte A, el objetivo es maximizar el área de un rectángulo definido por cualquier par de coordenadas. En la Parte B, las reglas cambian drásticamente introduciendo restricciones topológicas: el rectángulo debe estar estrictamente inscrito dentro de un polígono ortogonal (formado por un anillo continuo de baldosas).

## Fundamentos de la Ingeniería del Software

* **Modularidad y DRY (Don't Repeat Yourself):** El código se divide en módulos que pueden ser desarrollados de forma independiente, promoviendo la reutilización de componentes comunes para mejorar la mantenibilidad. Las entidades puras `GridPoint` y `PolygonEdge` se han extraído a un paquete compartido para no duplicar código entre la Parte A y la Parte B.


* **Código Expresivo (Good Naming):** Se han asignado nombres claros, significativos y relacionados con su propósito a las clases y métodos, mejorando la expresividad del código. Se evitan nombres genéricos en favor de nombres de dominio matemático como `TheaterAreaCalculator` y `ConstrainedAreaCalculator`.


* **Abstracción:** Los detalles complejos del trazado de rayos y la detección de colisiones se han ocultado detrás de métodos privados simples dentro de `ConstrainedAreaCalculator`, ofreciendo una interfaz clara al exterior.



## Principios de Diseño

El diseño arquitectónico de este día se ha guiado estrictamente por los principios SOLID y las directrices de código limpio:

* **Single Responsibility Principle (SRP):** Cada clase debe tener una única responsabilidad o razón para cambiar, favoreciendo la cohesión y la claridad del diseño. El parseo, la E/S y el filtrado del archivo de texto ocurren exclusivamente en las clases de test. Por su parte, los modelos geométricos solo almacenan estado y las calculadoras solo procesan reglas matemáticas.


* **Open/Closed Principle (OCP):** Las clases deben estar abiertas a la extensión pero cerradas a la modificación. En lugar de alterar destructivamente la lógica del cálculo de áreas de la Parte A introduciendo sentencias condicionales (`if(isPartB)`), se creó un nuevo componente (`ConstrainedAreaCalculator`) que extiende el sistema con un motor de validación topológico propio, dejando el código original intacto.


* **Liskov Substitution Principle (LSP) y Composition Over Inheritance (COI):** El principio de Liskov dicta que los objetos de una subclase deben poder reemplazar a los de su superclase sin alterar el funcionamiento del programa. Para evitar violar este principio (ya que el cálculo con restricciones altera drásticamente las precondiciones del cálculo normal), se aplicó el principio **COI**: se prefirió la composición de objetos y la separación de clases frente a la herencia.


* **Interface Segregation Principle (ISP) y Principio de Mínimo Compromiso:** Una interfaz debe exponer solo lo necesario para operar, ocultando detalles internos y reduciendo la dependencia entre módulos. Nuestras calculadoras solo exponen un único método público (`findMaxArea` y `findLargestValidArea`). Los detalles complejos de las aristas y el trazado de rayos están segregados y ocultos como métodos privados.


* **Dependency Inversion Principle (DIP):** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones. Aplicando la **Inyección de Dependencias**, que consiste en separar la creación de objetos de su uso, las calculadoras no instancian los lectores de archivos ni dependen del sistema de E/S. Reciben sus colecciones inmutables (`List<GridPoint>`) inyectadas directamente por el constructor.


* **Keep It Simple, Stupid (KISS) y Principio de Mínima Sorpresa:** El comportamiento de un componente debe ser predecible e intuitivo, y el código debe ser claro y directo, evitando la complejidad innecesaria. En la Parte B, en lugar de crear un enjambre de validadores externos inyectados que dificultarían el seguimiento del código, se optó por agrupar la lógica de validación del rectángulo en métodos privados altamente cohesivos dentro de la propia calculadora.



## Técnicas y Patrones de Diseño

* **Inmutabilidad del Modelo:** El estado de las clases no debe cambiar una vez creado, lo que evita errores relacionados con efectos secundarios. Se han modelado `GridPoint` y `PolygonEdge` como `records` en Java, sellando completamente sus datos geométricos.


* **Factory Method:** Patrón creacional que encapsula la creación de objetos mediante un método estático, en lugar de usar directamente el constructor. Implementado en `GridPoint.fromString()` para centralizar y abstraer el parseo de los vértices.


* **Point-In-Polygon (Trazado de Rayos):** Para determinar si un espacio despejado está dentro o fuera del polígono, lanzamos un "rayo" desde el punto evaluado hacia el infinito y contamos cuántas aristas verticales cruza. Si el número de cruces es impar, estamos dentro; si es par, estamos fuera.
* **Poda del Espacio de Búsqueda (Search Space Pruning):** Evaluar colisiones para cientos de miles de combinaciones destruiría el rendimiento. Se introdujo una condición simple: `if (area <= maxArea) continue;`. Si el área calculada no supera el récord existente, el programa ignora todas las complejas validaciones geométricas topológicas y salta a la siguiente iteración.

## Paradigmas y Gestión de Memoria

* **Gestión del Heap vs Stack:** En el entorno de la JVM, las variables locales y tipos primitivos operan muy eficientemente en el Stack, mientras que los objetos dinámicos e instancias temporales saturan el Heap, obligando al Garbage Collector a pausar la ejecución. Para la combinatoria masiva de pares de coordenadas, se utilizaron intencionalmente bucles imperativos `for` en lugar de flujos funcionales anidados (`flatMap`), manteniendo las referencias iterativas en el Stack y logrando un rendimiento óptimo de CPU y memoria.


* **Programación Funcional (API de Streams):** Los Streams no almacenan datos, sino que describen operaciones inmutables bajo la lógica FILTER -> MAP -> REDUCE. Aunque se evitaron en la combinatoria pesada por eficiencia, se aplicaron magistralmente donde aportan valor semántico declarativo:


* En la validación geométrica, se usaron operaciones finales como `anyMatch` y `count` para la comprobación de vértices y conteo de intersecciones.


* En la lectura del fichero dentro de las pruebas, se aplicó un filtro (`filter`), un mapeo referencial (`GridPoint::fromString`) y la operación de volcado `toList` para inyectar una colección limpia e inmutable a las calculadoras.
