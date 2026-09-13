# Day 11: Reactor

El problema de hoy nos introduce a la Teoría de Grafos, específicamente modelando una red de dispositivos con un flujo de datos unidireccional. Matemáticamente, esto representa un **Grafo Dirigido Acíclico (DAG - Directed Acyclic Graph)**. En la Parte A, el objetivo es encontrar el número total de caminos posibles entre dos nodos. En la Parte B, la complejidad aumenta al introducir restricciones topológicas obligatorias: los caminos válidos deben atravesar forzosamente dos nodos de control específicos (`dac` y `fft`) en cualquier orden.

## Fundamentos

* **Intercambio Espacio-Tiempo (Space-Time Tradeoff)** *(Optimización de rendimiento)*: En un grafo altamente interconectado, calcular cada ruta de forma independiente genera un tiempo de ejecución exponencial ($O(2^N)$). Para evitar esto, sacrificamos un poco de memoria RAM utilizando un diccionario (`HashMap`) que actúa como caché. Al memorizar los sub-problemas ya resueltos, reducimos drásticamente la complejidad computacional.
* **Abstracción** *(Simplificación de modelos complejos)*: La clase `Reactor` encapsula completamente la topología de la red. El consumidor de la clase no necesita saber que internamente el grafo está implementado como una Lista de Adyacencia (`Map<String, List<String>>`), protegiendo el diseño frente a futuros cambios (como migrar a una Matriz de Adyacencia).

## Principios de Diseño

* **Single Responsibility Principle (SRP)** *(Una clase, una razón para cambiar)*: Todo el procesamiento y filtrado del texto (manejo de I/O) se delega a las clases de prueba (`Day11ATest`, `Day11BTest`). El modelo `Reactor` tiene una única responsabilidad: parsear las conexiones lógicas y explorar matemáticamente el grafo.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: Para resolver la Parte B, la arquitectura demostró su flexibilidad. En lugar de reescribir la lógica de búsqueda, simplemente ampliamos la **clave del estado** en nuestra memoria caché (añadiendo el estado de visita a los nodos obligatorios), reutilizando el mismo esqueleto de recorrido en profundidad.

## Técnicas y Patrones

* **Búsqueda en Profundidad (DFS - Depth-First Search)** *(Algoritmo de grafos)*: Utilizamos un enfoque recursivo en el que profundizamos hasta llegar al nodo destino o a un callejón sin salida, acumulando los caminos válidos de regreso hacia la raíz.
* **Programación Dinámica / Memoización** *(Técnica algorítmica)*: Es la piedra angular matemática de esta solución. Cuando preguntamos "¿Cuántos caminos hay desde `ccc` hasta el final?", el programa lo calcula una sola vez y lo guarda. Si otra ruta vuelve a llegar a `ccc`, el programa devuelve instantáneamente el valor memorizado, podando millones de ramas de cálculo iterativo.
* **Máscaras de Bits (Bitmasking) para Estados** *(Optimización de control)*: En la Parte B, necesitamos saber si hemos visitado los nodos obligatorios. En lugar de usar complejas listas o múltiples variables booleanas (`hasVisitedDac`, `hasVisitedFft`), codificamos el estado en un número entero usando operaciones de bits: `0` (ninguno), `1` (solo dac), `2` (solo fft) y `3` (ambos). Esto permite generar claves de caché extremadamente rápidas y ligeras (`nodo_estado`).

## Paradigmas

* **Programación Recursiva** *(Descomposición de problemas)*: La naturaleza del grafo dirigido hace que el problema sea perfectamente recursivo: el número de rutas desde el nodo A hasta el destino final es exactamente la suma de las rutas desde todos los vecinos de A hasta el destino final.
* **Orientación a Objetos (OO)** *(Modelado del dominio)*: Encapsulamos el comportamiento del grafo, las listas de adyacencia y la memoria en un objeto cohesionado (`Reactor`) con un ciclo de vida claro.