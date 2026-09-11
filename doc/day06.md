# Day 6: Trash Compactor

El problema de este día presenta un desafío de análisis espacial de texto (Parsing 2D). Nos enfrentamos a una "hoja de cálculo" matemática donde los problemas no están separados por saltos de línea tradicionales, sino agrupados en bloques visuales separados por columnas vacías. En la Parte A, los números se leen horizontalmente dentro de cada bloque. En la Parte B, el modelo mental cambia drásticamente: los números deben formarse leyendo los dígitos verticalmente, de arriba hacia abajo, por cada columna.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `CephalopodWorksheet` abstrae el complejo algoritmo de escaneo bidimensional. El cliente simplemente proporciona el archivo en crudo y solicita el `calculateGrandTotal()`, ignorando por completo cómo se identifican las columnas vacías o cómo se extraen los operandos.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: La clase interna `MathProblem` encapsula los números y el operador. Su estado interno es inmutable y está protegido; la única forma de interactuar con él es a través del método público `solve()`.
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: Se ha separado estrictamente la responsabilidad de "extraer los datos de la cuadrícula" (Parsing) de la responsabilidad de "resolver la operación aritmética". Ambas lógicas están altamente cohesionadas en sus respectivas clases y acopladas de manera mínima.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: El beneficio de este principio brilló en la transición a la Parte B. Dado que `MathProblem` solo tiene la responsabilidad de ejecutar matemáticas, y `CephalopodWorksheet` tiene la de leer el texto, el cambio de requisito (leer en vertical en lugar de horizontal) solo afectó a la lógica de lectura.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: Como consecuencia directa del SRP, la clase `MathProblem` (y su método `solve`) permaneció **100% intacta** en la Parte B. Estaba cerrada a modificaciones aritméticas, pero el sistema completo estaba abierto a extender su comportamiento cambiando únicamente el inyector de dependencias (el parser).
* **Good Naming** *(Nombres descriptivos y precisos)*: El uso de nombres explícitos como `isSpaceCol`, `operatorRow` o `inProblem` actúa como documentación ejecutable, facilitando la comprensión de un algoritmo de lectura de matrices que de otro modo estaría lleno de bucles crípticos.

## Técnicas y Patrones

* **Análisis Espacial Bidimensional (2D Parsing)** *(Técnica algorítmica)*: En lugar de procesar los datos línea a línea usando primitivas como `String.split()`, el algoritmo iteró sobre las columnas de una matriz irregular (donde las líneas no tienen la misma longitud), comprobando los límites de los índices (`c < line.length()`) de forma segura para evitar excepciones de fuera de rango (`IndexOutOfBoundsException`).
* **Factory Method (Creacional)** *(Encapsulación de la creación de objetos en métodos estáticos dedicados)*: El método `CephalopodWorksheet.from()` actúa como una factoría compleja. Toma las líneas de texto crudas, escanea la matriz para encontrar los límites de cada bloque y delega la creación de los sub-objetos matemáticos al método privado `parseProblem()`.
* **Limpieza Defensiva de Datos** *(Técnica de seguridad)*: En el orquestador de la Parte B, se implementó una rutina para purgar líneas vacías al final del archivo. Esto protege la asunción del modelo de dominio (que el operador siempre reside en la última línea absoluta del bloque), evitando fallos causados por saltos de línea invisibles introducidos por editores de texto.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: El problema no se resolvió con un script monolítico de expresiones regulares, sino creando entidades del dominio (`Worksheet` y `MathProblem`) que modelan fielmente la realidad del enunciado.
* **Programación Imperativa Estructurada** *(Control del flujo mediante secuencias y bucles explícitos)*: Debido a la complejidad de saltar entre filas y columnas en una matriz de texto irregular, se optó por bucles `for` anidados clásicos en lugar de Streams funcionales, ya que el paradigma imperativo ofrece un control de índices mucho más granular y eficiente para la lectura espacial.