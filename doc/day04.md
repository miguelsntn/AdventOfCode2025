# Day 4: Printing Department

El problema nos plantea el análisis espacial de una cuadrícula bidimensional que representa un almacén de rollos de papel. En la Parte A, debemos calcular cuántos rollos cumplen una regla de accesibilidad basada en el número de vecinos en sus ocho direcciones adyacentes. En la Parte B, el problema se transforma en una simulación de autómata celular iterativo, donde debemos retirar por oleadas los rollos accesibles, lo que dinámicamente despeja el camino y altera la accesibilidad de los rollos restantes.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `PaperGrid` abstrae la complejidad de la navegación por una matriz bidimensional. El cliente simplemente pide contar o eliminar rollos, sin necesidad de conocer los cálculos matemáticos de desplazamientos cartesianos (vectores `dRow` y `dCol`) ni los chequeos de límites de la matriz.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: En la Parte A se protege el estado mediante una lista inmutable copiada en el constructor. En la Parte B, aunque el estado interno es mutable (`char[][]`) para permitir la simulación, este jamás se expone al exterior, asegurando que ninguna otra clase pueda alterar el mapa de forma incontrolada.
* **Modularidad** *(División del programa en módulos bien definidos e independientes)*: Se mantiene la separación estricta entre la capa de infraestructura (lectura de ficheros y arranque en las clases Test) y la capa de dominio (lógica espacial en `PaperGrid`).
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: `PaperGrid` tiene métodos altamente cohesivos (`isPaperRoll`, `countAdjacentRolls`, `isValidPosition`) que se combinan para resolver el problema mayor. El orquestador depende únicamente de la interfaz pública de la clase.

## Principios de Diseño

* **Good Naming** *(Nombres descriptivos y precisos)*: Se han empleado nombres de métodos booleanos que responden preguntas precisas (`isValidPosition`, `isPaperRoll`) y métodos de cálculo autoexplicativos (`countAdjacentRolls`, `removeAllAccessibleRolls`), eliminando por completo la necesidad de comentarios en el flujo lógico.
* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: Durante la simulación de la Parte B, se separa estrictamente la responsabilidad de "identificar elementos a eliminar" de la responsabilidad de "eliminar los elementos". Hacer ambas cosas simultáneamente corrompería la lógica del programa.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La estructura en paquetes separados ha permitido extender el comportamiento estático de la Parte A hacia un modelo de simulación dinámica en la Parte B, sin riesgo de introducir regresiones en el código funcional del primer requerimiento.

## Técnicas y Patrones

* **Factory Method (Creacional)** *(Encapsulación de la creación de objetos en métodos estáticos dedicados)*: El método `PaperGrid.from()` actúa como una factoría que no solo inicializa la clase, sino que se encarga de la transformación de datos (de `List<String>` a `char[][]` en la Parte B), garantizando que el objeto siempre nazca en un estado válido.
* **Simulación por Oleadas (Autómata Celular)** *(Técnica de modelado)*: Para la Parte B se ha implementado un mecanismo donde el estado futuro de la cuadrícula depende de una fotografía estática de su estado actual. Se recopilan todas las coordenadas candidatas en una lista temporal y se aplican las mutaciones en bloque al final del ciclo, imitando el comportamiento por generaciones de un autómata.
* **Vectores de Desplazamiento Espacial** *(Técnica algorítmica)*: En lugar de programar ocho bloques de código condicional (if-else) para cada punto cardinal, se utilizan dos arreglos unidimensionales para iterar sobre los vecinos, reduciendo drásticamente la complejidad ciclomática del método `countAdjacentRolls`.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: La cuadrícula bidimensional no es un simple tipo de dato primitivo flotando en el programa, sino una entidad del dominio con reglas, límites y comportamiento propios.
* **Programación Imperativa Estructurada** *(Control del flujo mediante secuencias y bucles explícitos)*: Debido a la naturaleza algorítmica del recorrido de matrices y al alto rendimiento requerido en el bucle iterativo de la Parte B, se ha priorizado el uso de estructuras imperativas (`for`, `do-while`), demostrando que diferentes problemas requieren diferentes paradigmas para una solución óptima.