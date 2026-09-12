# Day 9: Movie Theater

El problema de hoy nos introduce al mundo de la geometría computacional sobre una cuadrícula bidimensional discreta. En la Parte A, el objetivo es maximizar el área de un rectángulo definido por cualquier par de coordenadas. En la Parte B, las reglas cambian drásticamente introduciendo restricciones topológicas: el rectángulo debe estar estrictamente inscrito dentro de un polígono ortogonal (formado por un anillo continuo de baldosas).

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces claras)*: La clase `MovieTheater` ofrece una fachada sencilla (`findLargestValidRectangleArea`). El cliente no necesita saber que internamente el sistema descompone los puntos en aristas (`Edge`), ni que aplica algoritmos de trazado de rayos para evaluar la inclusión topológica.
* **Encapsulamiento** *(Protección y ocultación del estado)*: Las clases `Coordinate` y `Edge` son inmutables y privadas dentro del modelo. Una vez inicializadas con el polígono base, no pueden ser alteradas externamente, protegiendo así la integridad geométrica del cálculo.
* **Alta Cohesión** *(Responsabilidad enfocada)*: La clase `Edge` es experta en su propio dominio: sabe inmediatamente si es horizontal o vertical al ser instanciada, pre-calculando sus propios límites ortogonales (`minBoundary`, `maxBoundary`, `fixedCoord`) para hacer las comprobaciones de cruce mucho más rápidas.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase, una razón para cambiar)*: Todo el parseo, entrada/salida y filtrado de líneas vacías del archivo de texto ocurre en las clases de test. El modelo `MovieTheater` es un componente de negocio puro que solo procesa reglas geométricas.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: Para la Parte B, en lugar de alterar destructivamente la lógica del cálculo de áreas, se amplió el constructor del modelo para generar las colecciones de aristas y se introdujo un motor de validación (`isValidRectangle`) que actúa como un filtro avanzado antes de considerar un área como válida.

## Técnicas y Patrones

* **Point-In-Polygon (Algoritmo de Trazado de Rayos / Ray-Casting)** *(Técnica de geometría computacional)*: Para determinar si un espacio despejado está dentro o fuera del polígono, lanzamos un "rayo" imaginario desde el centro del rectángulo evaluado hacia el infinito y contamos cuántas aristas verticales cruza. Si el número de cruces es impar, estamos dentro; si es par, estamos fuera.
* **Detección de Colisiones (Bounding Box Collision)** *(Técnica algorítmica)*: Para garantizar que el rectángulo está *estrictamente* vacío de obstáculos, se implementaron validaciones rápidas para asegurar que: 1) Ningún vértice original cae dentro del área evaluada, y 2) Ninguna arista horizontal o vertical atraviesa el rectángulo de lado a lado.
* **Poda del Espacio de Búsqueda (Search Space Pruning)** *(Optimización crítica)*: Evaluar vértices, intersecciones de aristas y trazado de rayos para cientos de miles de combinaciones destruiría el rendimiento. Se introdujo una condición simple pero letal: `if (area <= maxArea) continue;`. Si el área de los dos puntos actuales no supera el récord existente, el programa ignora todas las complejas validaciones geométricas y pasa al siguiente par al instante.
* **Matemática Discreta** *(Manejo del dominio)*: A diferencia de la geometría continua, en este problema las baldosas tienen volumen. El área no es simplemente `base * altura`, sino `(|x1 - x2| + 1) * (|y1 - y2| + 1)`, contando las propias baldosas de los extremos como parte de la superficie.

## Paradigmas

* **Orientación a Objetos (OO)** *(Modelado del dominio)*: Se utilizaron clases como `Coordinate` y `Edge` en lugar de arreglos primitivos o matrices, aportando semántica (significado) al código y facilitando razonar sobre problemas de espacio y forma.
* **Programación Imperativa Estructurada**: Dada la necesidad de realizar validaciones matemáticas altamente condicionales (casos de 1 dimensión horizontal, 1 dimensión vertical y 2 dimensiones), el flujo estructurado tradicional (if/else y bucles clásicos) ofreció el control exacto necesario para no omitir ningún caso límite de la geometría ortogonal.