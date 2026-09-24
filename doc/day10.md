# Día 10: Factory

El desafío de hoy exige restaurar las máquinas de una fábrica, configurándolas mediante complejos arreglos de botones interconectados.

* **En la Parte A (Álgebra de Boole):** El sistema consta de simples interruptores de encendido/apagado. Al operar en un campo de Módulo 2, la pulsación de un botón alterna o invierte (conmuta) un circuito preestablecido. El objetivo es calcular el número mínimo de pulsaciones para alcanzar el patrón de luces objetivo operando a nivel de bits.
* **En la Parte B (Acumulación Aritmética):** Las reglas físicas del motor evolucionan radicalmente. Los botones ya no alternan luces lógicas, sino que inyectan voltaje físico a múltiples contadores interdependientes. Al inyectarse, la máquina procesa la energía reduciendo o dividiendo el voltaje residual. Esto transforma el problema de un mapa lógico directo a la exploración de un árbol infinito de estados, que requiere de Programación Dinámica para no colapsar.

## 1. Diferencias entre la Parte A y la Parte B

La evolución de la máquina exige un rediseño total de la estrategia de optimización algorítmica:

* **Parte A:** Puesto que los botones simplemente conmutan un estado independiente (una acción que se revierte si se pulsa dos veces), la solución se resuelve mediante fuerza bruta inteligente. Se evalúan todas las combinaciones posibles de forma inmutable, asumiendo una profundidad plana de $2^N$ estados generados directamente desde máscaras numéricas enteras.
* **Parte B:** Introduce dependencias aritméticas y temporales con la regla de reducción $(( \text{valor} - \text{efecto} ) / 2)$. Explorar un árbol de profundidad variable donde la decisión actual depende de un divisor recurrente generaría una **explosión combinatoria**. La solución requirió abandonar la evaluación secuencial y recurrir a **Programación Dinámica con Memoización** (Top-Down), para podar y almacenar estados convergentes.

## 2. Lógica Estructural

Para resolver este desafío sin duplicar código ni caer en sobreingeniería algorítmica, se estructuró un diseño limpio y puramente funcional:

* **`MachineBlueprint` (Record Compartido):** Actúa como el modelo base inmutable de la máquina. Almacena las configuraciones de luces, los objetivos de voltaje y el ruteo interno de los cables. Centraliza además el complejo parseo de datos estructurados mediante expresiones regulares (`Regex`), unificando el punto de entrada.
* **`MachineOptimizer` (Interfaz):** Abstracción matemática. Estandariza la firma de cálculo (`calculateMinimumPresses`) para los motores, garantizando el aislamiento de responsabilidades.
* **`LightOptimizer` (Capa de Servicio A):** Implementa el motor de optimización aplicando *Bitwise Operations* (máscaras y operadores lógicos XOR). Convierte los *arrays* físicos en matemática booleana de alta eficiencia.
* **`JoltageOptimizer` (Capa de Servicio B):** Implementa el motor algorítmico avanzado. Gestiona la recursividad profunda y orquesta el mapa de la memoria caché para resolver los problemas geométricos del voltaje.

## 3. Principios de Diseño (SOLID)

El diseño de la arquitectura cumple estrictamente con los 5 principios SOLID:

* **Single Responsibility Principle (SRP):** Cada clase tiene un único motivo para cambiar. `MachineBlueprint` centraliza la representación de datos espaciales y su parseo de texto. `LightOptimizer` asume la responsabilidad algorítmica de la matemática binaria, y `JoltageOptimizer` se enfoca únicamente en la orquestación del árbol recursivo de estados.
* **Open/Closed Principle (OCP):** Ante la radical evolución física del motor de la máquina en la Parte B (de luces booleanas a contadores divisores de voltaje), no modificamos ni alteramos el código existente de la Parte A. En su lugar, el sistema se cerró y extendió inyectando una nueva estrategia de optimización funcional (`JoltageOptimizer`), respetando el ecosistema de la interfaz sin introducir variables condicionales tipo `if(isPartB)`.
* **Liskov Substitution Principle (LSP):** Cualquier objeto que herede o implemente `MachineOptimizer` puede reemplazar su base sin alterar el comportamiento general ni las dependencias. El orquestador o la clase de test confía en que cualquier optimizador inyectado respetará el contrato matemático devuelto (`long`).
* **Interface Segregation Principle (ISP):** La interfaz `MachineOptimizer` es minimalista, declarando únicamente el comportamiento principal `calculateMinimumPresses`. Esto evita forzar a las clases de cálculo a exponer lógicas o mapas internos.
* **Dependency Inversion Principle (DIP):** Los flujos de control o clientes externos no están acoplados estáticamente a algoritmos particulares (no invocan directamente el motor booleano o el recursivo). Simplemente entregan el plano (el *Blueprint*) a una abstracción, invirtiendo el control.

## 4. Fundamentos, Técnicas y Patrones

* **Alta Eficiencia mediante Bitwise Operations:** En la Parte A se evade la creación de colecciones mutables (`boolean[]` o listas gigantes). Las lógicas y botones colapsan directamente a representaciones enteras enmascaradas con `(1 << idx)`. Las pulsaciones conmutadas se resuelven matemáticamente mediante un pliegue XOR (`^`), simulando transiciones sin instanciar objetos basura en memoria dinámica (*Heap*).
* **Programación Dinámica (Memoización Recursiva):** Para evitar la explosión combinatoria del voltaje en la Parte B, se diseñó una función recursiva pura (`solveDP`). Para proteger la CPU, se interceptan las llamadas mediante una caché en mapa (`Map<List<Integer>, Long> memo`). Si un estado intermedio ya fue resuelto desde otra rama, su subárbol entero se obvia y se devuelve el coste en complejidad $O(1)$.
* **Poda del Espacio de Búsqueda (Pruning Matemático):** Antes de iniciar una pesada rama recursiva, el estado se evalúa con una criba matemática (`canApply`). Exige paridad (`diff % 2 == 0`) para garantizar que la posterior división entera será matemáticamente exacta. Esta heurística poda millones de ramas inútiles (*Dead-ends*) en etapas tempranas.
* **Factory Method (Creacional):** La lógica de inicialización y escaneo está encapsulada en la factoría estática `MachineBlueprint.parse()`. Esto garantiza que los optimizadores trabajen con *Records* pre-validados.

## 5. Verificación y Tests (BDD)

Las soluciones se validan de forma automatizada mediante **pruebas unitarias** escritas con **JUnit 5** y **AssertJ**.

* Se sigue el patrón semántico **Given-When-Then**, nativo de la metodología **Behavior-Driven Development (BDD)**, estructurando los casos de prueba para garantizar la auto-documentación.
* **Test Parte A:** Inyecta un entorno virtual booleano. Verifica la correcta resolución del sistema de interruptores en el campo Módulo-2 (Galois), cerciorando el encuentro del patrón lumínico objetivo.
* **Test Parte B:** Somete a estrés al árbol de recursividad cuántico inyectando la necesidad de optimizar voltajes. Valida el correcto funcionamiento de la caché de Programación Dinámica al encontrar el voltaje óptimo a pesar del incremento del coste en las ramas ramificadas.
* El resultado esperado para la Parte A es 457 y para la Parte B es 17576.