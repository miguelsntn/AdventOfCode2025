# Día 11: Reactor

El problema introduce la Teoría de Grafos, modelando una red de conductos de datos de la fábrica con un flujo unidireccional. Matemáticamente, esto representa un **Grafo Acíclico Dirigido (DAG - Directed Acyclic Graph)**.

* **En la Parte A:** El objetivo es encontrar el número total de caminos únicos posibles desde un nodo de inicio hasta un nodo de salida.
* **En la Parte B:** La complejidad matemática aumenta al introducir restricciones topológicas obligatorias. Los caminos válidos deben atravesar forzosamente dos nodos de control específicos (`dac` y `fft`) en cualquier orden antes de salir de la red. Esto convierte el problema en una compleja **Búsqueda en el Espacio de Estados**.

## 1. Lógica Estructural

Para evitar la sobreingeniería funcional que satura la memoria instanciando miles de copias de listas de sucesores o conjuntos temporales por cada nodo, se implementó una arquitectura limpia y altamente acoplada al rendimiento:

* **`ReactorNetwork` (Record):** El modelo inmutable de dominio. Retiene y sella el grafo de conexiones de la red (`Map<String, List<String>>`).
* **`NetworkParser`:** Una clase puramente utilitaria (Factory) que se encarga del parseo y análisis léxico del archivo de texto en bruto, garantizando que el grafo se inicialice sin nodos "fantasma".
* **`BasicRouteAnalyzer` (A):** Analizador algorítmico que implementa un modelo de búsqueda en profundidad (DFS) estándar para contar todos los caminos posibles hacia la salida.
* **`MandatoryRouteAnalyzer` (B):** Extensión algorítmica especializada. Resuelve el requerimiento de la Parte B mediante un DFS impulsado por un **State Object Pattern**, rastreando simultáneamente la navegación espacial y la consecución de hitos lógicos.

## 2. Paradigmas de Programación

El éxito de esta solución radica en la correcta aplicación de tres paradigmas de programación que trabajan en armonía:

* **Programación Dinámica (Top-Down con Memoización):** El corazón matemático de la solución. En un grafo altamente interconectado, explorar cada ruta individual genera una complejidad exponencial $O(2^N)$. Al dividir el conteo de rutas en subproblemas superpuestos y almacenar sus resultados en caché, podamos millones de ramas computacionales de forma instantánea.
* **Programación Funcional (Pureza e Inmutabilidad):** Todo el recorrido de grafos se ejecuta sin alterar variables de estado globales en bucles `for` sucios. El uso del registro `DfsState` asegura que cada rama de la recursión sea matemáticamente pura y libre de efectos colaterales (*side-effects*), garantizando que unas rutas no corrompan a las otras durante la exploración paralela conceptual.
* **Programación Declarativa:** En la instanciación de mapas inmutables y en la iteración de vecinos (`network.getNeighborsOf().stream()...`), se utiliza la API de *Streams* para declarar **qué** datos se quieren filtrar o mapear, delegando el **cómo** al motor de iteración interna de Java.

## 3. Principios de Diseño (SOLID)

El diseño de la arquitectura se rige estrictamente por los 5 principios SOLID:

* **Single Responsibility Principle (SRP):** Separación total de responsabilidades. El parseo de texto crudo (`NetworkParser`), el almacenamiento de la topología inmutable (`ReactorNetwork`) y las leyes algorítmicas (`RouteAnalyzers`) son entidades estrictamente aisladas.
* **Open/Closed Principle (OCP):** El sistema demostró su flexibilidad real en la Parte B. En lugar de ensuciar y corromper el analizador original (`BasicRouteAnalyzer`) inyectándole sentencias lógicas condicionales (`if (esParteB)`), el código se extendió creando un nuevo analizador independiente (`MandatoryRouteAnalyzer`) que operó sin problemas sobre el mismo modelo `ReactorNetwork` intacto.
* **Liskov Substitution Principle (LSP) y Composition Over Inheritance (COI):** Las lógicas espaciales de ambas partes cambian drásticamente. Obligar a un analizador a heredar del otro habría roto el diseño por contrato al forzar el manejo de nuevos estados. Por ello, se utilizó la composición (COI): analizadores autónomos que componen en su interior a `ReactorNetwork`.
* **Interface Segregation Principle (ISP):** La API expuesta por `ReactorNetwork` es minimalista, exponiendo únicamente el método de consulta `getNeighborsOf(node)`. Mantiene ocultos todos los demás atributos mutadores de su mapa interno, garantizando seguridad absoluta frente a modificaciones de agentes externos.
* **Dependency Inversion Principle (DIP):** Los analizadores (alto nivel) no construyen ni dependen de ficheros de texto (bajo nivel). Se inyecta exclusivamente la abstracción del mapa en su constructor, permitiendo probar la matemática de grafos inyectando redes simuladas desde los tests.

## 4. Patrones de Diseño, Fundamentos y Técnicas

* **Patrón de Diseño: State Object (Objeto de Estado):** En la Parte B, en lugar de pasar booleanos crudos (`visitedDac`, `visitedFft`) a través de la pila recursiva, se encapsula el contexto de la búsqueda en el *record* inmutable `DfsState`. Esto elimina el anti-patrón "Obsesión por los Tipos Primitivos" (Primitive Obsession) y proporciona una clave compuesta perfecta y semántica para la caché de memoización.
* **Patrón Creacional: Factory Method:** La clase `NetworkParser` actúa como una factoría pura. Oculta la lógica sucia de manipular cadenas de texto, el uso de `split()` y la sanitización, emitiendo únicamente objetos `ReactorNetwork` blindados.
* **Resolución de Condición de Carrera Lógica (El Nodo *Out*):** Algunos nodos no definen explícitamente sus conexiones salientes. Buscar en el mapa de adyacencia un nodo inexistente causaría un mortal `NullPointerException`. `ReactorNetwork` aplica programación defensiva utilizando `getOrDefault()`, retornando una lista vacía que detiene fluidamente la recursión.
* **Previsión de Desbordamiento Numérico (Integer Overflow):** La cantidad de posibles combinaciones de rutas en una red densa crece a velocidades titánicas. Para prevenir cálculos erróneos debidos a un desbordamiento de enteros de 32 bits, el sistema opera de forma unificada utilizando acumuladores del tipo primitivo escalado `long`.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** usando **JUnit 5** y **AssertJ**.

* Se siguió la estructura semántica nativa del marco **Behavior-Driven Development (BDD)**, orientada al patrón formal **Given-When-Then** (Dado un estado de la red, Cuando el analizador busca la ruta, Entonces emite la suma de caminos válidos).
* **Test de la Parte A:** Inyecta un grafo base de prueba funcional y valida la correcta lectura de nodos finales y sumideros, garantizando que el contador recursivo identifique un total de rutas viables a nivel de sistema base.
* **Test de la Parte B:** Somete a estrés los filtros de hitos obligatorios del `DfsState`. Valida la correcta resolución de los flujos de memoria en grafo, confirmando que las rutas memorizadas certifican matemáticamente haber atravesado los nodos limitantes antes de ser retornadas como válidas.
* El resultado esperado para la Parte A es 428 y para la Parte B es 331468292364745.