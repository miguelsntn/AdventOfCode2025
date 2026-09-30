# Día 12: Granja de Árboles de Navidad

En este último día, el problema logístico de empaquetar regalos (Empaquetamiento 2D) presenta un desafío computacional de categoría NP-Hard. Sin embargo, el verdadero reto no es solo resolver el algoritmo, sino **diseñar una arquitectura de software capaz de soportar una carga combinatoria masiva sin colapsar la memoria del sistema**.

El enfoque de esta solución prioriza un diseño robusto, altamente testeable y estructurado sobre los fundamentos de la Programación Orientada a Objetos.

## 1. Modelado del Dominio y Topología de Clases

El sistema se ha modelado aplicando una estricta **Separación de Responsabilidades (Separation of Concerns)**. Cada clase tiene un propósito arquitectónico definido:

* **`Point` (Value Object):** Un *record* inmutable que encapsula las coordenadas $(r, c)$. Es el bloque de construcción geométrico base. Implementa traslaciones algebraicas puras (`rotate()`, `flip()`) devolviendo siempre nuevas instancias para evitar mutaciones de estado.
* **`TreeRegion` (Data Transfer Object):** Un *record* que actúa como DTO inmutable. Define las restricciones físicas del tablero (ancho y alto) y el inventario exacto de piezas (`pieceCounts`) requeridas para esa región.
* **`ShapeVariation` (Estructura Optimizada y Factory Method):** Representa una rotación específica de un regalo, pero abstraída a nivel binario. Utiliza el patrón **Factory Method** (`from(List<Point>)`) para ocultar la compleja conversión de coordenadas a un array de máscaras de bits primitivas (`long[] masks`). Sobrescribe rigurosamente `equals` y `hashCode` para garantizar el comportamiento correcto en colecciones matemáticas.
* **`PresentShape` (Entidad de Dominio):** Representa un regalo conceptual. Ejerce encapsulación fuerte y **Precomputación (Eager Initialization)**: en lugar de calcular rotaciones dinámicamente, al instanciarse genera y normaliza al origen $(0,0)$ todas sus variaciones espaciales válidas (rotaciones y reflejos), eliminando duplicados simétricos y exponiendo su estado final en modo de solo lectura.
* **`FarmParser` (Capa de Infraestructura / I/O):** Actúa como una factoría utilitaria. Su única misión es ingerir listas de texto crudo y transformar (parsear) esos caracteres en objetos de dominio puros (agrupados en el record `ParsedFarm`). Aísla por completo el análisis léxico del resto del sistema.
* **`FarmAllocator` (Servicio Orquestador):** El motor transaccional y algorítmico del sistema. No conoce la procedencia de los datos. Recibe un catálogo de piezas inyectado en su constructor y aplica el algoritmo de empaquetado para determinar si las regiones son válidas.

## 2. Estrategias de Optimización

El sistema se protegió contra tiempos de ejecución exponenciales aplicando programación dinámica:

1. **Diseño Fail-Fast (Poda Temprana):** Se aplican aserciones de negocio antes de la recursión. Si el área total de las piezas excede el área disponible en el `TreeRegion`, el cálculo aborta al instante, ahorrando CPU frente a precondiciones imposibles.
2. **Caché y Memoización (State Memoization):** Se implementó un registro (`Set<String> failedStates`) para cachear ramas de ejecución inválidas. Al serializar el estado exacto del tablero (`grid`) y el índice de pieza, el sistema aplica el principio de **no recalcular lo ya conocido**, podando ramas combinatorias muertas en tiempo $O(1)$.

## 3. Principios de Diseño (SOLID)

El ecosistema de clases respeta estrictamente los principios SOLID:

* **Single Responsibility Principle (SRP):** Cohesión máxima. `FarmParser` solo escanea texto, `ShapeVariation` transforma geometría a binario, y `FarmAllocator` solo evalúa encajes. Un cambio en el formato del fichero jamás obligará a tocar el motor algorítmico.
* **Open/Closed Principle (OCP):** El motor `FarmAllocator` está abierto a la extensión pero cerrado a la modificación. Podríamos añadir nuevas formas geométricas exóticas inyectándolas en el catálogo y el algoritmo funcionaría sin cambiar una sola línea, ya que depende de la abstracción matemática (máscaras de bits).
* **Liskov Substitution Principle (LSP):** Se respetó el contrato fundamental de Liskov en la sobrescritura de `equals()` y `hashCode()` de `ShapeVariation`. El *Collections Framework* de Java (`HashSet` dentro de `PresentShape`) confía ciegamente en este contrato; violar LSP aquí corrompería la purga de piezas duplicadas.
* **Dependency Inversion Principle (DIP):** Las dependencias fluyen hacia la abstracción. `FarmAllocator` exige que su dependencia (`Map<Integer, PresentShape> catalog`) le sea **inyectada** por constructor, logrando un Desacoplamiento (Decoupling) total entre la lógica de negocio y el origen de los datos.

## 4. Verificación

Las soluciones se validan de forma automatizada mediante pruebas unitarias usando JUnit 5 y AssertJ.

Se aplicó la estructura semántica Given-When-Then para validar el comportamiento del sistema.

La inyección de dependencias permite simular catálogos diminutos en memoria durante la fase Given, verificando la matemática pura de los desplazamientos de bits (<<) de forma asilada antes de lanzar el motor contra los complejos mapas del problema original.

El resultado final esperado para las regiones válidas es 403.