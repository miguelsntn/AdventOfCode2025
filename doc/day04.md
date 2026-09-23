# Day 4: Printing Department

## 1. Diferencias entre la Parte A y la Parte B

El salto de complejidad entre ambas partes representa la transición clásica de una simple consulta espacial a un motor de simulación continua:

* **Parte A (Evaluación Estática):** El problema consiste en analizar el plano bidimensional estático del departamento de impresión para localizar grandes rollos de papel (`@`). Un rollo es accesible si tiene estrictamente menos de 4 rollos adyacentes a su alrededor (en su vecindad de Moore). El objetivo es devolver el conteo estático inicial.
* **Parte B (Autómata Celular Dinámico):** El reto evoluciona hacia una simulación iterativa por oleadas. Al retirar los rollos accesibles, se despeja el camino y rollos que estaban bloqueados en el interior pueden volverse accesibles en el siguiente turno. El objetivo es calcular el total histórico de rollos retirados hasta que la fábrica se queda estancada. Esto exige transicionar estados temporalmente sin que las eliminaciones prematuras de un turno corrompan los cálculos de los vecinos en ese mismo ciclo.

## 2. Lógica Estructural

Para evitar el desastroso *Memory Churn* (saturación de la memoria dinámica) que sufren las implementaciones que instancian miles de objetos para representar coordenadas, se diseñó una arquitectura de alto rendimiento basada en tipos primitivos:

* **`PaperGrid` (Modelo de Dominio Protegido):** Representación inmutable de la cuadrícula. Actúa exclusivamente como un entorno de datos blindado. Procesa el texto de entrada convirtiéndolo a una matriz primitiva hiperligera (`boolean[][]`), valida los límites espaciales y expone métodos de consulta (`isPaperRoll`).
* **`PaperRollManager` (Capa de Servicio Orquestadora):** Centraliza la lógica de negocio y la algoritmia espacial. Recibe el `PaperGrid` por inyección, calcula las adyacencias mediante vectores primitivos y ejecuta la mutación o el conteo.

## 3. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Hemos dividido el diseño eliminando cualquier "Modelo Gordo". El entorno (`PaperGrid`) cambia solo si lo hace el formato de lectura o las dimensiones del mapa. El actor de extracción (`PaperRollManager`) cambiará únicamente si las reglas físicas o matemáticas de acceso a los rollos se actualizan.
* **Open/Closed Principle (OCP):** El algoritmo de conteo estático (Parte A) y el de destrucción por oleadas (Parte B) son radicalmente distintos. Para no contaminar el código original con sentencias `if(esParteB)`, el paquete `a` queda cerrado a alteraciones. Las nuevas capacidades del autómata celular extienden el sistema en un paquete `b` completamente nuevo.
* **Liskov Substitution Principle (LSP) y Composición (COI):** El modelo no hereda de estructuras complejas ni de colecciones pesadas; compone su estado internamente mediante una matriz nativa (`boolean[][]`), garantizando un comportamiento predecible y aislando las reglas espaciales propias.
* **Interface Segregation Principle (ISP):** Exposición mínima requerida. `PaperGrid` no expone *getters* de su matriz interna para no vulnerar el estado. Cuando la simulación destructiva requiere alterar el terreno, el cliente está obligado a llamar a `getMutableCopy()`, recibiendo un clon aislado.
* **Dependency Inversion Principle (DIP):** El orquestador `PaperRollManager` desconoce cómo se genera el mapa desde el archivo de texto. Solo depende de recibir un objeto `PaperGrid` completamente formado por inyección de dependencias.

## 4. Fundamentos, Técnicas y Patrones

* **Double Buffering (Máscara de Eliminación) vs. Sobreingeniería Funcional:** Mientras que enfoques ingenuos utilizan `Streams` y encapsulan cada celda en objetos tipo `Coordinate`, instanciando millones de objetos iterables en la memoria *Heap* durante los bucles de simulación, aquí se priorizó el hardware. Se utilizó la técnica de **Double Buffering** asignando una máscara temporal (`boolean[][] toRemove`). Durante el ciclo, se marcan a nivel de bits los rollos a destruir, y al final del ciclo se aplica la mutación global. **Las instanciaciones dinámicas (creación de objetos nuevos con `new`) dentro del ciclo repetitivo de la simulación son exactamente 0**, erradicando las pausas del *Garbage Collector*.
* **Vectores de Desplazamiento Espacial:** Para calcular los vecinos de Moore, se utilizan dos arreglos unidimensionales primitivos (`dRow = {-1, -1, ...}`, `dCol`) para iterar sobre las 8 direcciones en tiempo constante.
* **Keep It Simple, Stupid (KISS):** Equilibrio perfecto entre paradigmas. La recolección de alto nivel fluye mediante `IntStream.range` (Programación Funcional), pero el cálculo intensivo espacial dentro de los filtros se resuelve de forma estructurada e imperativa para garantizar un rendimiento óptimo.
* **Factory Method (Patrón Creacional):** `PaperGrid.from()` actúa como una factoría y aduana. Aísla la conversión del texto a matriz y ejecuta una sanitización defensiva, protegiendo al sistema de matrices irregulares o entradas nulas.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante pruebas unitarias escritas con **JUnit 5** y **AssertJ**, garantizando la robustez geométrica antes de procesar los ficheros reales.

* Se sigue el patrón semántico **Given-When-Then** heredado del **Behavior-Driven Development (BDD)**, lo que transforma las pruebas en documentación viva del sistema.
* **Test de la Parte A:** Asegura que el conteo estático con vecindad de Moore calcule correctamente las adyacencias sin arrojar excepciones de desbordamiento en los bordes de la matriz (`IndexOutOfBoundsException`).
* **Test de la Parte B:** Valida que el motor de *Double Buffering* respete el estado temporal de la generación actual. Asegura que retirar un rollo en el cuadrante superior izquierdo no afecte indebidamente el cálculo de accesibilidad de su vecino adyacente dentro de la misma oleada de eliminación.