# Día 8: Playground

El desafío de hoy se sitúa en un entorno tridimensional donde se debe conectar cajas de conexiones eléctricas utilizando la menor cantidad de cable posible. Esto se traduce en un problema clásico de Teoría de Grafos: la construcción de un **Árbol de Expansión Mínima (Minimum Spanning Tree - MST)**.

## 1. Diferencias entre la Parte A y la Parte B

La evolución algorítmica del problema exige escalar desde un análisis estático de clústeres hacia una unificación total de la red:

* **Parte A:** Construimos el árbol deteniéndonos tras un número límite de conexiones (1000 iteraciones). El objetivo es analizar el estado de los sub-circuitos o islas resultantes y multiplicar el tamaño de los 3 clústeres más grandes.
* **Parte B:** El objetivo cambia a **unificar completamente la red** (todos los nodos conectados en un único circuito continuo) descartando las conexiones redundantes (cortocircuitos). Al lograr la conexión final (cuando queda un solo clúster), debemos identificar el último cable instalado e interrogar las coordenadas de sus nodos para extraer un producto de verificación.

## 2. Lógica Estructural

Para resolver este problema sin colapsar la memoria con colecciones anidadas en cada iteración, se diseñó una arquitectura de alto rendimiento separando el grafo matemático de las reglas temporales del orquestador:

* **`CircuitNode` (Record):** Entidad física inmutable. Conoce su ubicación espacial en 3D ($X, Y, Z$) y su identificador unívoco.
* **`Wire` (Record):** Representa un cable. Relaciona dos cajas y encapsula la distancia espacial (coste) que las separa, implementando `Comparable` para poder ser ordenado.
* **`NetworkTracker` (Interfaz):** Abstracción del sistema de grafos. Define el contrato estricto para fusionar circuitos (`linkNodes`) y obtener sus tamaños.
* **`DisjointSetTracker`:** Implementación de altísimo rendimiento de la estructura de datos *Disjoint-Set (Union-Find)* apoyada en *arrays* primitivos. Su responsabilidad es detectar si dos nodos pertenecen a la misma red y fusionarlas sin instanciar objetos basura en memoria.
* **`WireOptimizer`:** Clase de servicio que encapsula la combinatoria espacial, generando el producto cartesiano de todos los cables posibles y ordenándolos por su distancia euclidiana de menor a mayor.
* **`CircuitAnalyzer` (A) y `NetworkUnifier` (B):** Orquestadores del negocio. Reciben la topología y el *tracker*, aplican el **Algoritmo de Kruskal** (seleccionando iterativamente los cables más cortos) y detienen la ejecución según las reglas específicas de su parte.

## 3. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Cada clase tiene una sola razón para cambiar. `CircuitNode` maneja topología 3D, `Wire` encapsula la conectividad, `WireOptimizer` asume la combinatoria, `DisjointSetTracker` maneja el grafo matemático abstracto, y `NetworkUnifier` orquesta la temporalidad de la construcción de la red.
* **Open/Closed Principle (OCP):** El orquestador y el grafo están cerrados a la modificación pero abiertos a la extensión. Si mañana se exige un algoritmo diferente al *Union-Find* (por ejemplo, búsquedas BFS/DFS en base de datos), podemos crear una nueva clase que implemente `NetworkTracker` y conectarla sin tocar ni una sola coma del código de los orquestadores.
* **Liskov Substitution Principle (LSP):** Cualquier estructura de datos topológica que implemente la interfaz `NetworkTracker` puede ser inyectada en los orquestadores garantizando una sustitución segura, ya que respeta firmemente el diseño por contrato de la fusión de nodos.
* **Interface Segregation Principle (ISP):** La interfaz `NetworkTracker` es completamente minimalista. Expone únicamente los 3 métodos estrictamente necesarios para consultar el grafo (`linkNodes`, `getClusterSizes`, `getRemainingClusters`), evitando forzar a las clases de infraestructura a implementar métodos de lectura de strings o parseo 3D.
* **Dependency Inversion Principle (DIP):** Los orquestadores de alto nivel (`CircuitAnalyzer` y `NetworkUnifier`) no están acoplados a la implementación del motor matemático `DisjointSetTracker`. Dependen única y exclusivamente de la abstracción `NetworkTracker`.

## 4. Fundamentos, Técnicas y Patrones

* **Abstracción y Encapsulamiento (Ocultación de la complejidad):** `NetworkTracker` funciona como un panel de control simple. Gracias a ella, el orquestador ordena conectar nodos sin preocuparse por la compresión de caminos, la optimización por tamaños ni las matemáticas subyacentes de grafos. Todo el estado matemático permanece ciegamente sellado dentro de los arrays privados del *tracker*.
* **Prevención de Pérdida de Precisión (Double vs Long):** Para ordenar la longitud de los cables de menor a mayor, no es necesario calcular raíces cuadradas. El uso de `Math.sqrt()` obligaría a usar tipos de punto flotante (`double`), los cuales consumen altos ciclos de CPU y sufren de pérdida de precisión por el estándar IEEE-754. Puesto que se comparaban distancias espaciales relativas, se calculó la *Distancia Euclidiana al Cuadrado* pura. Esto mantuvo toda la matemática anclada a enteros primitivos de 64 bits (`long`), maximizando la velocidad y blindando al sistema contra el *Integer Overflow*.
* **Inyección de Dependencias (DI):** El tracker topológico se instancia en los *tests* de infraestructura y se inyecta por constructor hacia los analizadores lógicos, promoviendo el aislamiento y la fácil capacidad de prueba (*mocking*).
* **Factory Method (Creacional):** Implementado en `CircuitNode.fromLine()` para controlar y abstraer estandarizadamente el parseo tridimensional de los datos crudos desde los ficheros planos de texto.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** usando **JUnit 5** y **AssertJ**.

* Se aplica la estructura semántica **Given-When-Then**, nativa del marco **Behavior-Driven Development (BDD)**, orientando los tests a describir casos de uso del dominio:
* **Test de la Parte A:** Inicializa un conjunto de nodos y valida que, tras interrumpir el algoritmo iterativo de Kruskal prematuramente en 10 conexiones, el tracker es capaz de devolver la lista del tamaño de las subredes restantes y calcular el producto correcto del Top 3 (ej. resultado esperado = 40).
* **Test de la Parte B:** Somete a estrés al algoritmo de compresión de caminos conectando clústeres hasta consolidar un único circuito maestro final. Verifica que el sistema devuelve adecuadamente las coordenadas del último cable instalado para emitir el producto final de control (ej. resultado esperado = 14136).