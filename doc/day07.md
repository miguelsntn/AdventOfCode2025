# Día 7: Laboratories 

**Parte A:** Simular el recorrido de un haz de taquiones a través de un colector bidimensional, donde el rayo se divide al chocar contra un divisor (`^`). El objetivo es calcular el número total de divisiones empleando un motor físico de mecánica clásica.

**Parte B:** Aplicar una interpretación de universos paralelos (Many-Worlds) a una única partícula. Al chocar contra un divisor, la realidad se bifurca. El objetivo es calcular la inmensa cantidad de líneas temporales activas al final del recorrido aplicando física cuántica.

## 1. Diferencias entre la Parte A y la Parte B

La evolución de los requisitos entre ambas partes ilustra el riesgo de la explosión combinatoria y cómo mitigarlo arquitectónicamente:

* **Parte A (Mecánica Clásica):** Es una simulación de estado espacial. Basta con rastrear en qué columnas existe un rayo activo (utilizando un `Set<Integer>`) y sumar cuántas veces estos rayos impactan contra un divisor.
* **Parte B (Física Cuántica y Explosión Combinatoria):** El problema cambia de una simulación visual a un problema combinatorio masivo. Puesto que cada divisor duplica la realidad, simular cada línea temporal de forma individual requeriría tiempo y memoria infinitos. Para evitar este colapso, el algoritmo transiciona hacia la **Programación Dinámica (State-Space Reduction)**. En lugar de simular rayos, se contabiliza la *cantidad acumulada* de universos que convergen en una misma columna empleando un `Map<Integer, Long>`.

## 2. Lógica Estructural 

El sistema huye de enfoques frágiles basados en recursividad profunda (que provocan `StackOverflowError` en cuadrículas inmensas), implementando una arquitectura de delegación inmutable:

* **`Coordinate` (Record):** Entidad inmutable que encapsula las posiciones espaciales (fila y columna).
* **`TachyonManifold`:** Actúa como el contenedor topológico. Almacena el diagrama de texto original de forma inmutable y dirige el recorrido de la simulación capa por capa, delegando las matemáticas puras al motor inyectado.
* **`TachyonPhysicsEngine` (Interfaz):** Define el contrato estricto (`calculate`) que cualquier ley física del universo debe cumplir, aislando el comportamiento matemático de la topología de la matriz.
* **`ClassicalPhysicsEngine`:** Implementación del motor para la Parte A. Utiliza un estado transitorio (`SimulationState`) para gestionar posiciones únicas y contar divisiones simples.
* **`QuantumPhysicsEngine`:** Implementación para la Parte B. Aplica programación dinámica para fusionar y sumar las superposiciones de líneas temporales en cada coordenada.

## 3. Fundamentos de la Ingeniería del Software

* **Abstracción:** Consiste en ocultar los detalles complejos detrás de una interfaz simple. La interfaz `TachyonPhysicsEngine` oculta las complejas fórmulas matemáticas y de bifurcación, permitiendo que el cliente simplemente pida calcular un resultado (`manifold.simulate(engine)`) sin conocer la mecánica interna.
* **Modularidad:** El software se divide en módulos que pueden ser desarrollados, modificados y probados de forma independiente. Existe una separación total entre el contenedor espacial y topológico (`TachyonManifold`) y los motores físicos que procesan los datos.
* **Alta Cohesión:** Las partes de un módulo están estrechamente relacionadas. `TachyonManifold` se dedica exclusivamente a gestionar la matriz, buscar el inicio y generar flujos de lectura. Los motores físicos asumen únicamente las fórmulas de propagación espacial.
* **Bajo Acoplamiento:** Los módulos tienen interdependencias mínimas. `TachyonManifold` ignora por completo qué motor o reglas de la física se están utilizando en su interior.
* **Código Expresivo (Good Naming):** El código es claro, comprensible y facilita el mantenimiento sin necesitar comentarios (ej. `ClassicalPhysicsEngine`, `simulate`, `findStart`).

## 4. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Cada clase gestiona un concepto aislado. El colector gestiona la iteración del espacio, la física clásica calcula la dispersión básica, y la cuántica asume únicamente la contabilización de universos paralelos.
* **Open/Closed Principle (OCP):** El simulador está cerrado a modificaciones. Para integrar la física cuántica (Parte B), no se alteró ni una sola línea de `TachyonManifold`; simplemente se extendió el sistema inyectando una nueva implementación de la interfaz física.
* **Liskov Substitution Principle (LSP):** El método `simulate()` está diseñado para recibir la interfaz `TachyonPhysicsEngine`. Gracias a este principio, cualquier clase que la implemente operará correctamente sin romper el flujo lógico del orquestador.
* **Interface Segregation Principle (ISP):** La interfaz física es minimalista. Expone un único método `calculate()`, ocultando los *Records* de estado interno, las conversiones de Streams y los mapas de reducción a los clientes externos.
* **Dependency Inversion Principle (DIP):** El colector depende de abstracciones y no de clases concretas.
* **Don't Repeat Yourself (DRY):** El escaneo de las filas y la búsqueda del punto de inicio (`S`) se centralizan en el `TachyonManifold` para que los motores físicos no repitan costosas operaciones de análisis de texto.
* **Keep It Simple, Stupid (KISS) & YAGNI:** En lugar de emplear recursividad estructural propensa a desbordamientos de pila, el recorrido de la matriz se realiza de forma declarativa e iterativa de arriba a abajo.

## 5. Patrones de Diseño

* **Factory Method (Creacional):** Implementado en `TachyonManifold.from()` para garantizar la integridad inicial. Valida entradas nulas y sella la cuadrícula inmutable, actuando como un constructor seguro.
* **Singleton (Creacional):** Puesto que las leyes de la física no tienen estado propio a largo plazo (son funciones puras aplicadas sobre parámetros), `ClassicalPhysicsEngine` y `QuantumPhysicsEngine` restringen su instanciación a una única instancia en memoria mediante constructores privados y el método `getInstance()`.

## 6. Técnicas y Paradigmas Avanzados

* **Inmutabilidad del Modelo y Evaluación de Estados:** El estado inicial de las clases no cambia una vez creado. Se emplean colecciones estandarizadas como `List.copyOf()` para sellar la cuadrícula. Durante la simulación, cada fila genera un nuevo estado inmutable que reemplaza al anterior en el reductor funcional.
* **Inyección de Dependencias:** La física del universo es inyectada por el cliente de pruebas a través del método `simulate()`, permitiendo intercambiar el comportamiento en tiempo de ejecución.
* **Clases Internas Estáticas (Static Records):** El `record SimulationState` se anida dentro de su motor físico clásico. Encapsula de forma limpia las variables del estado transitorio (rayos activos y divisiones) sin contaminar el espacio de nombres global.
* **Mónadas (Patrón Funcional `Optional`):** En la búsqueda del punto de inicio, se utiliza `findFirst()` que devuelve un `Optional<T>`. Esto encapsula la posible ausencia de un punto de inicio, forzando un manejo seguro con `orElseThrow` y previniendo `NullPointerExceptions`.
* **Programación Funcional y Streams (El Bucle Principal):**
* Se sustituyen los clásicos bucles `for` por un motor funcional puro (`streamRowsFrom`).
* **`flatMap`:** Esencial para modelar la bifurcación de universos; aplana las nuevas coordenadas generadas tras impactar contra un divisor (`^`) en un flujo unidimensional continuo.
* **`reduce`:** Actúa como el motor del tiempo. Consume la matriz fila por fila transfiriendo y actualizando el estado de forma inmutable, desde la configuración inicial hasta el total final.
* **`groupingBy` & `summingLong`:** Técnicas avanzadas de agrupación de *Streams* empleadas en el motor cuántico para fusionar líneas temporales solapadas y sumar sus ramas divergentes al vuelo.



## 7. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante pruebas unitarias escritas con **JUnit 5** y **AssertJ**.

* Se sigue el patrón semántico **Given-When-Then** heredado del enfoque **Behavior-Driven Development (BDD)**, orientando las pruebas al comportamiento del sistema y maximizando su valor como documentación.
* **Parte A:** Se inyecta la física clásica y se verifica la capacidad del sistema para detectar el punto de partida, rastrear trayectorias continuas y calcular con precisión el número de divisiones del haz (ej. resultado esperado = 16).
* **Parte B:** Se inyecta la física cuántica para someter a estrés al motor de Programación Dinámica, validando que el agrupamiento funcional previene la explosión combinatoria y suma correctamente las realidades paralelas (ej. resultado esperado = 1.048.576).