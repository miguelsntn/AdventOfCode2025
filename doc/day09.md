# Día 9: Sala de Cine

El problema de hoy nos exige evaluar áreas dentro de un plano bidimensional discreto. La entrada consiste en las coordenadas que definen los vértices del recinto de un cine (un polígono).

* **En la Parte A:** El objetivo es encontrar el área matemática del rectángulo más grande que se puede formar emparejando cualquier par de vértices del polígono, sin importar si el rectángulo resultante se sale físicamente de las paredes del edificio.
* **En la Parte B:** El objetivo incorpora restricciones físicas duras. Debemos encontrar el área del rectángulo más grande que resida **completamente dentro de los confines del polígono**. Ningún muro puede atravesarlo y ninguna sección de su área puede recaer en el exterior.

## 1. Lógica Estructural

El sistema se ha modelado utilizando primitivas inmutables y clases de servicio orientadas estrictamente al rendimiento algorítmico:

* **`GridPoint` (Record):** Entidad inmutable que modela las coordenadas $X, Y$ bidimensionales. Incluye la inteligencia matemática para calcular el área que forma al proyectarse contra otro punto.
* **`PolygonEdge` (Record):** Define un segmento de recta inmutable delimitado por dos `GridPoints`. Es esencial para modelar la topología de los muros del recinto y calcular intersecciones.
* **`TheaterAreaCalculator` (Capa de Servicio A):** Orquesta el pesado algoritmo combinatorio puro, calculando todas las áreas posibles utilizando la API de *Streams* de forma puramente declarativa.
* **`ConstrainedAreaCalculator` (Capa de Servicio B):** Extensión algorítmica que, además de la combinatoria, verifica la validez topológica del rectángulo generado contra todos los vértices y aristas del plano, utilizando técnicas de *Ray-Casting*.


## 2. Algoritmo Geométrico: Point-in-Polygon

Para comprobar si un rectángulo —tras pasar las validaciones de colisión de bordes— reside realmente en el interior del cine o en un espacio vacío exterior (como la cavidad de una herradura), se implementó el clásico algoritmo de "trazado de rayos" (`rayCastInside`).
Dispara una coordenada virtual con desplazamiento decimal (`+0.5`) hacia el infinito y cuenta mediante un *Stream* funcional los cruces con los bordes verticales del polígono. Si el número de cruces es impar, certifica matemáticamente que el punto está dentro; si es par, está fuera.

## 4. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Las clases de dominio (`GridPoint`, `PolygonEdge`) son estructuras puras de datos matemáticos pasivos. Toda la orquestación algorítmica y los complejos cálculos de trazado de rayos recaen exclusivamente en los calculadores de servicio.
* **Open/Closed Principle (OCP):** En lugar de contaminar el analizador de la Parte A con sentencias booleanas (`if (esParteB)`) o condicionales que evalúen colisiones, el código se mantuvo cerrado a la modificación. Las nuevas restricciones topológicas se resolvieron extendiendo el sistema e inyectándolas en un nuevo servicio calculador totalmente independiente.
* **Interface Segregation Principle (ISP) y Encapsulamiento:** El `ConstrainedAreaCalculator` no expone sus complejos submétodos geométricos (`rayCastInside`, `isIntersectedByEdges`). Todos están estrictamente ocultos como `private`, garantizando que los clientes externos solo deban consumir la firma limpia `findLargestValidArea()`.

## 5. Fundamentos y Clean Code

* **Abstracción:** Se encapsula la altísima complejidad geométrica bidimensional. El cliente simplemente inyecta una lista de puntos al calculador y obtiene un área escalar, ignorando la existencia del algoritmo *Ray-Casting*.
* **Inmutabilidad y Prevención de Desbordamiento:** Uso estricto del tipo primitivo de 64 bits (`long`) en lugar de `int` para evitar el temido *Integer Overflow* al multiplicar áreas extensas de coordenadas geográficas. Además, el calculador sella su estado interno inmediatamente inyectando los puntos mediante `List.copyOf()`, blindando al motor de cualquier efecto colateral originado desde el exterior.

## 6. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** usando **JUnit 5** y **AssertJ**.

* Se aplica la estructura semántica de comportamiento **Given-When-Then** (Behavior-Driven Development), lo que permite leer los tests como la documentación oficial del sistema.
* **Test de la Parte A:** Verifica que el producto cartesiano evalúa correctamente todas las combinaciones y emite el área teórica más grande ignorando la topología interior de los muros.
* **Test de la Parte B:** Somete a estrés los algoritmos geométricos de *Ray-Casting* y colisiones. Valida que el sistema logre detectar y descartar correctamente rectángulos que cruzan las paredes, contienen columnas internas o residen en cavidades "falsas" del exterior del polígono.
* El resultado esperado para la Parte A es 4739623064 y para la Parte B es 1654141440.