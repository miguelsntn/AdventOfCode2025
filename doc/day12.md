# Día 12: Granja de Árboles de Navidad

El desafío final nos ha llevado a un problema clásico y computacionalmente complejo: el **Empaquetamiento 2D** (*2D Bin Packing* / *Polyomino Tiling*). El objetivo era determinar si un conjunto específico de regalos de formas irregulares (poliminós) podía encajar perfectamente en una cuadrícula bidimensional bajo un árbol, permitiendo rotaciones y volteos, pero sin apilamientos ni solapamientos.

Al ser un problema de categoría **NP-Hard**, la complejidad combinatoria es factorial. No existe una fórmula matemática rápida para resolverlo; requiere explorar el espacio de soluciones aplicando heurísticas de optimización y podas extremas (Pruning) para evitar tiempos de ejecución infinitos.

## 1. Lógica Estructural

Se diseñó un ecosistema geométrico puramente inmutable, delegando el estado a *records* y aislando el motor de búsqueda en una entidad orquestadora:

* **`Angle` (Enum):** Encapsula las fórmulas matemáticas de transformación bidimensional. Al aplicar el patrón funcional `RotatingFunction`, permite rotar coordenadas en un tiempo de $O(1)$.
* **`Position` (Record):** Modela las coordenadas $(X, Y)$ y provee traslaciones puras.
* **`PresentShape`:** Entidad matemática del regalo. Al instanciarse, genera declarativamente y purga duplicados de sus 8 variaciones espaciales posibles (4 rotaciones + 4 reflexiones).
* **`TreeRegion` (Record):** El tablero de juego inmutable. Expone un método funcional `place()` que, en caso de colisión, devuelve un `Optional.empty()`, y en caso de éxito, devuelve un nuevo tablero clonado dentro de un `Optional`.
* **`FarmAllocator`:** El orquestador funcional del algoritmo de búsqueda. Explora el árbol de decisiones mediante *Backtracking* declarativo y *Memoización* de ramas fallidas.
* **`FarmParser`:** Clase utilitaria o factoría que aísla la lógica sucia de la lectura del fichero de texto, retornando DTOs (Data Transfer Objects) listos para la simulación.

## 2. Paradigma Funcional

En problemas de *Backtracking*, el enfoque tradicional es mantener una matriz global y mutarla constantemente (hacer y deshacer el movimiento). Aunque esto puede parecer eficiente a nivel de memoria (Stack), es **extremadamente propenso a errores de estado ocultos (Side-Effects)**.

Para este desafío, decidimos pivotar radicalmente hacia la **Inmutabilidad Absoluta** y el **Paradigma Declarativo**:

* Cuando el sistema prueba a colocar una pieza, nunca ensucia el tablero actual. El método `place()` utiliza **Mónadas (`Optional`)**.
* Toda la búsqueda profunda se ha aplanado combinando `flatMap()` para generar variaciones espaciales y `anyMatch()` para la recursividad encadenada, logrando que un algoritmo de búsqueda exhaustiva tenga una **complejidad ciclomática aparente de cero bucles explícitos**.

## 3. Algoritmia Avanzada y Poda

Para compensar el coste de clonar objetos en el *Heap*, se implementaron tres heurísticas críticas que destrozan la complejidad temporal:

1. **Poda Estática Temprana ($O(1)$):** Antes de iniciar la costosa búsqueda recursiva, el método `solve()` verifica axiomas algebraicos. Si la suma del área de los regalos a colocar supera el área total de la cuadrícula, el cálculo se aborta instantáneamente retornando `0` (fallo).
2. **Heurística Largest-First (Ordenación Decreciente):** Intentar colocar primero las piezas pequeñas fragmenta el espacio, garantizando fallos tardíos. Al iniciar, el `FarmAllocator` preordena la lista de regalos descendentemente por área (`sorted(reverseOrder())`). Esto fuerza al árbol de búsqueda a acomodar primero los bloques más grandes, alcanzando los puntos de colisión (y por tanto, podando la rama) muchísimo más rápido.
3. **Memoización de Estados Fallidos (Dynamic Programming):** Se introdujo una memoria de ramas muertas (`Set<Integer> failedStates`). Si una configuración exacta del tablero con un índice de pieza específico ya demostró ser un callejón sin salida, la exploración se aborta en $O(1)$. El identificador único del estado se genera ultrarrápido combinando el índice de profundidad con el `Arrays.deepHashCode()` de la matriz.

## 4. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Las responsabilidades están quirúrgicamente separadas. `FarmParser` se encarga exclusivamente del análisis léxico; `Angle` maneja trigonometría discreta; `PresentShape` gestiona geometría estática, y `FarmAllocator` dirige el recorrido del árbol de decisiones.
* **Open/Closed Principle (OCP):** El diseño está abierto a la extensión. Podríamos introducir nuevas formas de transformar piezas (ej. distorsiones tridimensionales) modificando internamente `PresentShape`, sin necesidad de alterar el motor de búsqueda en `FarmAllocator`.
* **Liskov Substitution Principle (LSP) y Composition Over Inheritance (COI):** Se evitó heredar de clases de geometría pesadas (como `java.awt.Polygon`). Al preferir la composición con matrices de primitivos (`int[][]`), aseguramos un comportamiento predecible, determinista y sustituible.
* **Interface Segregation Principle (ISP) y Principio de Mínimo Compromiso:** Los componentes se comunican a través de contratos mínimos. El motor de búsqueda ignora el origen del texto, limitándose a consumir colecciones destiladas (`List<PresentShape>`).
* **Dependency Inversion Principle (DIP):** La lógica de negocio está completamente invertida. El `FarmAllocator` no lee ficheros ni insta sus propios catálogos; recibe las peticiones y las piezas en el momento de su inicialización a través del constructor, inyectadas por el orquestador de pruebas.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** usando **JUnit 5** y **AssertJ**.

* Se aplicó la estructura semántica **Given-When-Then** para validar el comportamiento del sistema.
* El sistema de pruebas no actúa como un mero validador pasivo; en la fase **When**, asume un rol funcional orquestando la lectura del `FarmParser` y la inicialización independiente de un `FarmAllocator` para cada petición del usuario (mediante `Stream.generate().limit()`). Esto garantiza una estricta **Independencia Transaccional**, asegurando que los historiales de memoización de una granja fallida no contaminen los cálculos bidimensionales de la siguiente.