# Day 6: Trash Compactor (Análisis Espacial Bidimensional)

El problema de este día presenta un desafío profundo de análisis espacial de texto (Parsing 2D). Nos enfrentamos a un registro de expresiones matemáticas donde los problemas no están separados por saltos de línea tradicionales, sino agrupados en bloques visuales separados por columnas vacías.
En la Parte A, los números se leen horizontalmente dentro de cada bloque. En la Parte B, el modelo mental cambia drásticamente: los números deben formarse leyendo los dígitos verticalmente, de arriba hacia abajo, por cada columna.

## Lógica Estructural (Domain-Driven Design)

Para este diseño, se aplicó **Domain-Driven Design (DDD)**, descartando nombres ligados a la narrativa trivial del rompecabezas en favor de un vocabulario técnico y matemático:

* **`CalculationLedger`**: El modelo inmutable que representa el registro completo de operaciones.
* **`MathExpression`**: Entidad de datos pura (Record) que contiene los operandos numéricos y la operación aritmética a aplicar.
* **`ExpressionScanner`**: Contrato (Interfaz) que define el comportamiento para escanear y extraer expresiones de una matriz de texto.

## Fundamentos de Ingeniería

* **Abstracción (Ocultación de la complejidad algorítmica)**: El cliente (las clases Test) solo interactúa con la interfaz genérica `ExpressionScanner` y el modelo `CalculationLedger`. La altísima complejidad de escanear la matriz bidimensional, buscar columnas vacías y concatenar dígitos verticalmente queda totalmente abstraída y oculta.
* **Encapsulamiento (Blindaje del estado)**: La entidad `MathExpression` se ha implementado como un **Record** de Java, lo que garantiza inmutabilidad absoluta por defecto. Su estado interno es de solo lectura y solo se expone comportamiento a través de `evaluate()`.
* **Alta Cohesión y Bajo Acoplamiento**: El sistema separa estrictamente la responsabilidad de "extraer los datos de la cuadrícula" (Parsers) de la responsabilidad de "ejecutar la aritmética" (`ArithmeticOperator`). El acoplamiento es mínimo gracias a la inyección del texto en bruto.

## Principios de Diseño (SOLID)

* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: El diseño brilla en su cumplimiento del OCP. Al llegar la Parte B (lectura vertical), el modelo de datos (`CalculationLedger` y `MathExpression`) permaneció **100% intacto**. El sistema se extendió simplemente creando un nuevo analizador (`VerticalExpressionScanner`), dejando el analizador original a salvo de regresiones.
* **Single Responsibility Principle (SRP)**: Cada módulo hace una sola cosa. Las clases Test sanean el texto, los Scanners aíslan los bloques, y el Enum `ArithmeticOperator` concentra en exclusiva las reglas matemáticas.
* **Don't Repeat Yourself (DRY)**: Puesto que la identificación espacial de los límites de cada bloque (búsqueda de columnas vacías) es idéntica en ambos problemas, esta lógica se extrajo a la clase `BaseExpressionScanner`, evitando duplicar código de escaneo complejo.

## Patrones de Diseño y Técnicas

* **Patrón de Comportamiento: Strategy (Estrategia)**: Se utilizó este patrón de la banda de los cuatro (GoF) mediante la interfaz `ExpressionScanner`. En lugar de forzar a la clase de datos a entender cómo se lee a sí misma, el algoritmo de lectura se inyecta dinámicamente según la necesidad (Horizontal o Vertical), logrando polimorfismo algorítmico puro.
* **Patrón de Comportamiento: Template Method (Método Plantilla)**: En la clase abstracta `BaseExpressionScanner`, se define el esqueleto inmutable del algoritmo espacial (encontrar las columnas vacías y acotar los bloques). Esta clase "plantilla" delega el paso final de extraer la expresión al método abstracto `extractExpression()`, el cual es implementado por las subclases concretas.
