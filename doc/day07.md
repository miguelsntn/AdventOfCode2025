# Day 7: Laboratories

El problema de hoy nos introduce a una simulación física en una cuadrícula bidimensional (el colector de taquiones). En la Parte A, modelamos el comportamiento clásico donde los rayos se dividen y se fusionan si coinciden en el mismo espacio. En la Parte B, las reglas cambian a un modelo cuántico ("Many-Worlds Interpretation"), donde cada división genera líneas temporales independientes, creando una explosión combinatoria masiva que exige un cambio radical en la estrategia de cálculo.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `TachyonManifold` oculta la complejidad del recorrido de la matriz. El cliente que consume la clase no necesita saber si internamente se está utilizando un `Set` para fusionar rayos (Parte A) o un arreglo de frecuencias para la Programación Dinámica (Parte B).
* **Eficiencia y Complejidad Espacial** *(Optimización de recursos)*: El diseño invierte el enfoque clásico: en lugar de modelar cada rayo y seguir su ruta recursivamente (lo cual generaría un árbol exponencial infinito en la Parte B), modelamos el *espacio* (la cuadrícula) y calculamos qué ocurre fila por fila. Esto garantiza un tiempo de ejecución predecible de $O(R \times C)$ (Filas por Columnas).
* **Inmutabilidad** *(Protección contra efectos secundarios)*: La matriz espacial (`List<String>`) se copia en el constructor y jamás se altera durante la simulación. El estado que muta (los rayos activos o las líneas temporales) se recrea desde cero en cada iteración de fila.

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: El modelo de dominio (`TachyonManifold`) se centra únicamente en la simulación física (gravedad descendente y colisión con divisores). La responsabilidad de leer los archivos y transformar el flujo de texto sigue delegada estrictamente en las clases `Test`.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La transición de la Parte A a la Parte B demuestra este principio a nivel de arquitectura. En lugar de plagar el código original con sentencias `if (isQuantumMode)`, se construyó un modelo matemático completamente nuevo en el paquete `b`, manteniendo la simulación clásica intacta y segura frente a regresiones.
* **Good Naming** *(Nombres descriptivos y precisos)*: Variables como `activeBeams`, `timelines` y `nextTimelines` reflejan directamente la naturaleza del modelo físico que se está simulando, haciendo que el código sea auto-documentado.

## Técnicas y Patrones

* **Programación Dinámica (Dynamic Programming)** *(Técnica algorítmica)*: Es la estrella de la Parte B. Para evitar calcular millones de bifurcaciones independientes (que colapsarían la memoria RAM y la CPU), se utiliza la memoria de la iteración anterior. El arreglo `timelines` actúa como un registro de estado: la cantidad de universos paralelos en la celda actual es simplemente la suma de los universos que cayeron en ella desde la fila inmediatamente anterior.
* **Deduplicación de Estados (State Merging)** *(Técnica algorítmica)*: En la Parte A, la colisión de rayos que viajan al mismo punto se resuelve elegantemente usando la estructura de datos `Set<Integer>`. Al añadir dos veces la misma columna, la colección descarta automáticamente el duplicado, previniendo cálculos redundantes.
* **Prevención de Desbordamiento (Overflow)** *(Buena práctica de ingeniería)*: Dado que en la Parte B el tiempo se bifurca constantemente, el crecimiento es exponencial (similar a la secuencia de Fibonacci o potencias de 2). Se utilizó un arreglo de tipos `long[]` para evitar el desbordamiento silencioso que habría ocurrido con enteros de 32 bits (`int`).

## Paradigmas

* **Orientación a Objetos** *(Organización del software en entidades encapsuladas)*: El tablero entero es modelado como un objeto que gestiona sus propias reglas de negocio y ciclo de vida.
* **Programación Imperativa Estructurada** *(Control de flujo explícito)*: Debido a que el estado de una fila depende intrínsecamente del estado *final* calculado en la fila anterior (dependencia temporal), se han utilizado bucles imperativos (`for`) clásicos. Este es un caso donde el paradigma funcional puro (como el uso de Streams de Java) introduciría una sobrecarga innecesaria y dificultaría la lectura del algoritmo de propagación de estado.