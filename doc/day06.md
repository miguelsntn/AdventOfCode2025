# Día 6: Trash Compactor

El problema de este día presenta un desafío profundo de análisis espacial de texto (Parsing 2D). Nos enfrentamos a un registro de expresiones matemáticas espaciales (una hoja de cálculo) donde los problemas no están separados por saltos de línea tradicionales, sino agrupados en bloques visuales separados por columnas vacías. El objetivo es calcular el "Grand Total" sumando los resultados de todas las operaciones extraídas.

## 1. Diferencias entre la Parte A y la Parte B

El cambio de orientación de los datos pone a prueba la resiliencia de la arquitectura frente a las matrices espaciales bidimensionales:

* **Parte A (Lectura Horizontal):** Los números se leen horizontalmente dentro de cada bloque, de izquierda a derecha. La última fila indica la operación matemática a aplicar a todos los operandos superiores.
* **Parte B (Lectura Vertical):** El modelo mental de la topología cambia drásticamente al sistema nativo de los cefalópodos: los números deben formarse leyendo los dígitos verticalmente, de arriba hacia abajo, por cada columna dentro del bloque.

## 2. Lógica Estructural

A diferencia de implementaciones basadas en iteradores anidados y complejos (`RowIterator`, `ColumnIterator`) que ofuscan el código y dificultan el mantenimiento, se aplicó un diseño limpio basado en **Domain-Driven Design (DDD)** y patrones de comportamiento:

* **`ExpressionScanner` (Interfaz)**: Define el contrato fluido que cualquier paradigma de lectura espacial (horizontal o vertical) debe cumplir para procesar el documento.
* **`BaseExpressionScanner` (Clase Abstracta)**: Contiene el esqueleto del algoritmo de procesamiento. Se encarga de la difícil tarea de escanear la matriz, encontrar las columnas completamente vacías y delimitar espacialmente los bloques, delegando la extracción fina a sus subclases.
* **`HorizontalExpressionScanner` / `VerticalExpressionScanner**`: Implementaciones concretas. Su única función es recibir los límites espaciales de un bloque y traducir el texto en bruto (ya sea horizontal o verticalmente) a instancias del dominio matemático.
* **`MathExpression` (Record)**: Entidad inmutable del dominio. Encapsula un bloque matemático ya traducido (una lista de operandos y su operador) y contiene la lógica para resolverse a sí mismo.
* **`ArithmeticOperator` (Enum)**: Motor matemático. Asocia los símbolos de texto (`+`, `*`) con sus funciones matemáticas reales para ser aplicadas sobre los operandos.
* **`CalculationLedger` (Record)**: El modelo final inmutable que representa el registro completo de operaciones y calcula el total general.

## 3. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Cada módulo hace una sola cosa. Los *Scanners* aíslan los bloques y parsean texto espacial. La entidad `MathExpression` se encarga exclusivamente de resolver la ecuación, y el `CalculationLedger` actúa como orquestador del total.
* **Open/Closed Principle (OCP):** El diseño brilla en su cumplimiento. Al llegar la Parte B (lectura vertical), el modelo de datos (`CalculationLedger` y `MathExpression`) y el buscador de columnas vacías (`BaseExpressionScanner`) permanecieron **100% intactos**. El sistema se extendió simplemente creando un nuevo analizador (`VerticalExpressionScanner`), manteniendo el código original cerrado y a salvo de regresiones.
* **Liskov Substitution Principle (LSP) y Composición (COI):** Cualquier implementación de escáner puede sustituir a la interfaz `ExpressionScanner` sin romper la corrección del programa. Además, en lugar de crear una jerarquía de herencia frágil (ej. `AddExpression` y `MultiplyExpression`), se prefirió **componer** el objeto agregándole el comportamiento aritmético como un atributo (`ArithmeticOperator operator`), haciendo el diseño infinitamente más flexible.
* **Interface Segregation Principle (ISP):** La interfaz `ExpressionScanner` es minimalista. Expone un único método `scan()`, sin obligar al cliente (o a las pruebas) a depender de las complejas lógicas de escaneo interno bidimensional.
* **Dependency Inversion Principle (DIP):** El flujo principal opera sobre abstracciones. El sistema que resuelve el problema final no depende de algoritmos concretos de lectura ni se acopla a las direcciones geométricas, garantizando su adaptabilidad.

## 4. Fundamentos y Clean Code

* **Abstracción (Ocultación de la complejidad algorítmica):** El cliente solo interactúa con la interfaz genérica `scan()`. La altísima complejidad de escanear la matriz bidimensional, buscar columnas vacías y concatenar dígitos verticalmente queda totalmente abstraída y oculta en el backend.
* **Encapsulamiento y Diseño por Contrato (Tell, Don't Ask):** Se respeta estrictamente la encapsulación en el flujo de ejecución. El `CalculationLedger` le pide a la `MathExpression` que se evalúe (`evaluate()`), en lugar de extraer sus operandos para calcularlo externamente. A su vez, `MathExpression` le delega la responsabilidad matemática al enum `ArithmeticOperator`.
* **Don't Repeat Yourself (DRY):** Puesto que la identificación espacial de los límites de cada bloque (búsqueda de columnas vacías) es idéntica en ambos problemas, esta lógica se extrajo a la clase `BaseExpressionScanner`, erradicando la duplicación de código de escaneo iterativo.
* **Alta Cohesión y Bajo Acoplamiento:** El sistema separa estrictamente la responsabilidad de "extraer los datos de la cuadrícula" (Parsers espaciales) de la responsabilidad de "ejecutar la aritmética" (Dominio Matemático).

## 5. Patrones de Diseño y Técnicas

* **Patrón de Comportamiento: Strategy (Polimorfismo):** Para resolver las operaciones matemáticas, se ha evitado el uso de sentencias `switch` o `if/else`. El enumerado `ArithmeticOperator` implementa el patrón Strategy almacenando referencias a funciones funcionales puras mediante lambdas y referencias a métodos (`LongStream::sum`). Evaluar un operador es tan inmutable como delegar en su función asociada.
* **Inmutabilidad y Records:** Las clases de dominio (`MathExpression`, `CalculationLedger`) son *Records* nativos de Java, garantizando que el estado ensamblado sea final e inmutable, previniendo efectos secundarios (*side-effects*) durante el ciclo de vida del cálculo.
* **Programación Funcional y Evaluación Declarativa:** En lugar de manipular matrices pesadas con complejos bucles anidados para la lectura vertical (lo que dispararía la complejidad ciclomática), el ensamblaje de números se procesa declarativamente con la API de `Streams` (`filter`, `reduce`, `mapToLong`), concatenando caracteres verticalmente y transformándolos a `long[]` al vuelo.

## 6. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante pruebas unitarias escritas con **JUnit 5** y **AssertJ**.

* Están estructuradas semánticamente siguiendo el patrón **Given-When-Then** (Dado un contexto, Cuando ocurre una acción, Entonces se espera un resultado), propio del enfoque **BDD (Behavior-Driven Development)**, lo que las convierte en documentación ejecutable y viva del proyecto.
* **Test Parte A:** Verifica la correcta delimitación de bloques por columnas vacías, el parseo horizontal numérico y la sumatoria del *Grand Total*.
* **Test Parte B:** Valida la resiliencia del sistema ante el cambio de topología. Comprueba que el escáner vertical sea capaz de aislar los dígitos de forma descendente y aplicar el operador de la fila base de manera matemáticamente estanca.
* El resultado esperado para la Parte A es 6171290547579 y para la Parte B es 8811937976367.