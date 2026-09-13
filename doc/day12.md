# Day 12: Christmas Tree Farm

El desafío final nos ha llevado a un problema clásico y computacionalmente complejo: el **Empaquetamiento 2D (2D Bin Packing)** con poliminós (piezas estilo Tetris). El objetivo era determinar si un conjunto específico de regalos de formas irregulares podía encajar perfectamente en una cuadrícula bidimensional bajo un árbol, permitiendo rotaciones y volteos, pero sin apilamientos ni solapamientos.

## Fundamentos

* **Complejidad Computacional (NP-Hard)**: El empaquetamiento de poliminós es un problema conocido por su explosión combinatoria. No existe una fórmula matemática rápida para resolverlo; requiere explorar exhaustivamente el espacio de soluciones. El éxito del diseño radicó en aplicar heurísticas y podas extremas para que el tiempo de ejecución no se volviera infinito.
* **Inmutabilidad Geométrica**: Las piezas (`Shape` y `Variation`) se calculan una sola vez al cargar la fábrica. Sus matrices binarias y dimensiones quedan congeladas, garantizando que el motor de búsqueda en profundidad (`solve`) opere sobre datos seguros y en tiempo constante, sin efectos secundarios.

## Principios de Diseño

* **Separation of Concerns (SoC)** *(Separación de Intereses)*: Se dividió claramente la matemática puramente espacial (rotar, voltear y normalizar puntos al origen `0,0`) de la lógica de negocio (el motor recursivo que ubica los regalos en la cuadrícula).
* **Single Responsibility Principle (SRP)** *(Principio de Responsabilidad Única)*: La clase interna `Variation` tiene la única responsabilidad de traducir una lista de coordenadas cartesianas abstractas a una **Máscara de Bits** (`long[] masks`), preparando el terreno para el motor de colisiones de forma aislada.

## Técnicas y Patrones

* **Backtracking (Búsqueda en Profundidad)** *(Algoritmo base)*: El algoritmo principal intenta colocar un regalo. Si tiene éxito, se llama a sí mismo para el siguiente regalo. Si en el futuro se queda sin espacio, deshace su movimiento (*backtrack*) y prueba otra posición u otra rotación.
* **Bitwise Collision Detection (Máscaras de Bits)** *(Optimización extrema)*: En lugar de comprobar colisiones iterando celda por celda (lo cual sería lentísimo), cada fila de una variación se convierte en un número binario. Para comprobar si una pieza cabe en una posición, aplicamos un operador lógico `AND` (`&`). Para colocarla y quitarla del tablero, aplicamos un `XOR` (`^`). Esto permite evaluar colisiones de patrones complejos en una fracción de milisegundo.
* **Symmetry Breaking (Ruptura de Simetría)** *(Poda del árbol de búsqueda)*: Una optimización crítica. Si tenemos que colocar tres regalos cuadrados idénticos (A, B y C), el algoritmo ingenuo evaluaría "A, luego B, luego C", y más tarde "B, luego A, luego C", procesando el mismo tablero repetidas veces. Al exigir que las piezas idénticas se coloquen en un orden estrictamente secuencial de coordenadas espaciales (`placementId`), destruimos las permutaciones simétricas y reducimos el árbol de búsqueda en un factor factorial ($N!$).
* **Heurística de Ordenación Golosa (Greedy Sorting)**: Las piezas se ordenan por su área de mayor a menor antes de iniciar el Backtracking. Colocar primero las piezas más grandes y restrictivas fuerza al algoritmo a fallar rápidamente si va por un mal camino, podando ramas muertas en los primeros niveles de recursión.

## Paradigmas

* **Programación Estructurada y Recursividad**: El uso de llamadas recursivas para descender por el árbol de decisiones, combinado con bucles estructurados para deshacer los cambios, crea un flujo de control claro que emula la forma humana de hacer un rompecabezas ("pruebo esto aquí, si no encaja lo quito y pruebo allá").