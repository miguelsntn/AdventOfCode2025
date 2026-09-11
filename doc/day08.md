# Day 8: Playground

El desafío de hoy nos sitúa en un entorno tridimensional donde debemos conectar cajas de conexiones eléctricas utilizando la menor cantidad de cable posible. Esto se traduce en un problema clásico de grafos: la construcción de un Árbol de Expansión Mínima (Minimum Spanning Tree - MST). En la Parte A, detenemos la construcción tras un número arbitrario de conexiones y analizamos los sub-circuitos formados. En la Parte B, el objetivo es encontrar la arista crítica que unifica todos los sub-circuitos en un único circuito global continuo.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `CircuitSystem` provee una fachada limpia hacia el exterior. El cliente llama a `solveForFinalConnection()` sin necesidad de entender de teoría de grafos, algoritmos de ordenación o de la existencia interna de la estructura Union-Find.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: La estructura de datos `DisjointSet` gestiona internamente los arreglos de `parent` y `size`. Estos arreglos son estrictamente privados; el estado del circuito global solo puede alterarse a través de la operación pública `union()`, previniendo que código externo corrompa el árbol de conexiones.
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: Se han creado clases estáticas internas muy enfocadas: `Point3D` solo almacena coordenadas, `Edge` solo calcula y compara distancias, y `DisjointSet` solo agrupa conjuntos. Trabajan juntas sin mezclarse.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: El cálculo de la distancia espacial recae enteramente en el constructor de la clase `Edge`. Si mañana el problema nos pide usar la "Distancia Manhattan" en lugar de la distancia euclidiana, el cambio se realizaría exclusivamente en esa clase, sin tocar el algoritmo de agrupación.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: Para resolver la Parte B, extendimos el comportamiento del `DisjointSet` añadiendo un contador de `components` (circuitos aislados). Esta extensión permitió a la nueva clase `CircuitSystem` saber exactamente cuándo detenerse, sin alterar la lógica matemática fundamental de las uniones que ya funcionaba en la Parte A.

## Técnicas y Patrones

* **Algoritmo de Kruskal** *(Algoritmo de Teoría de Grafos)*: Para asegurar que usamos la menor cantidad de cable, generamos todas las aristas posibles, las ordenamos de menor a mayor distancia, y las vamos uniendo. Este enfoque codicioso (Greedy) garantiza la formación de un Árbol de Expansión Mínima.
* **Union-Find / Conjuntos Disjuntos** *(Estructura de Datos Avanzada)*: En lugar de hacer complejas búsquedas en profundidad (DFS) para saber si dos cajas ya están en el mismo circuito, se implementó un `DisjointSet`. Esta estructura permite saber si dos elementos están conectados en tiempo casi constante $O(1)$.
* **Optimización Matemática (Squared Distance)** *(Técnica de rendimiento)*: Para comparar las distancias entre puntos tridimensionales, normalmente usaríamos el Teorema de Pitágoras con una raíz cuadrada (`Math.sqrt()`). Sin embargo, dado que solo necesitamos *comparar* distancias (saber cuál es menor), omitimos la costosa operación de la raíz cuadrada y comparamos directamente las distancias al cuadrado. Esto ahorra ciclos de CPU y evita la pérdida de precisión inherente a los números flotantes (`double`).
* **Compresión de Ruta (Path Compression) y Unión por Tamaño** *(Optimización de algoritmos)*: Dentro del `DisjointSet`, el método `find` actualiza el padre de los nodos visitados para que apunten directamente a la raíz, aplanando el árbol. A su vez, `union` siempre cuelga el árbol más pequeño debajo del más grande. Esto mantiene la estructura balanceada y garantiza el máximo rendimiento en sistemas con miles de nodos.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: Modelamos el grafo mediante objetos `Edge` que implementan la interfaz `Comparable`. Esto permite delegar la ordenación del algoritmo de Kruskal directamente a las utilidades estándar de Java (`Collections.sort()`), combinando el polimorfismo con algoritmos genéricos.