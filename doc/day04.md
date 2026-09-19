# Day 4: Printing Department

### Parte A

Analizar el plano bidimensional del departamento de impresión para localizar los grandes rollos de papel (`@`). Una carretilla elevadora (forklift) solo puede acceder a un rollo si este tiene menos de 4 rollos adyacentes a su alrededor (considerando las 8 direcciones posibles, es decir, vecindad de Moore). El objetivo es contar cuántos rollos son directamente accesibles en el estado estático inicial de la fábrica.

### Parte B

El reto evoluciona hacia una **simulación de autómata celular iterativo**. Al retirar los rollos accesibles, se despeja el camino y rollos que estaban bloqueados en el interior pueden volverse accesibles en las siguientes oleadas. El objetivo es calcular el total histórico de rollos retirados de la matriz hasta que la fábrica se queda estancada (no quedan rollos accesibles).

## Lógica Estructural

* **`PaperGrid` (Modelo Inmutable Compartido)**: Representación de la cuadrícula en la capa de dominio. Actúa exclusivamente como un entorno de datos protegido. Procesa las listas de texto de entrada, valida los límites de la matriz bidimensional y expone métodos de consulta (`isPaperRoll`, `isValidPosition`).
* **`PaperRollManager` (A y B) (Capa de Servicio)**: Orquesta la lógica de negocio. Recibe el `PaperGrid` por inyección de dependencias y aplica las restricciones matemáticas de accesibilidad de los elfos.

## Algoritmos

* **Vectores de Desplazamiento Espacial (Optimización)**: Para calcular los vecinos de Moore, se descarta la costosa creación de objetos "Coordenada". En su lugar, se utilizan dos arreglos unidimensionales primitivos (`dRow = {-1, -1, ...}`, `dCol`) para iterar sobre las 8 direcciones.
* **Simulación Iterativa por Oleadas (Autómata Celular)**: En lugar de usar recursividad pura (que satura la pila de llamadas), la Parte B se ejecuta mediante un ciclo estructurado `do-while`. En cada pasada, se genera una fotografía del estado actual, se calculan en bloque (mediante Streams) los candidatos a eliminar, y solo al final del ciclo se aplica la eliminación en la matriz mediante copias mutables.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante contratos claros)*: La clase de servicio `PaperRollManager` expone métodos abstractos de muy alto nivel como `countAccessibleRolls()` y `removeAllAccessibleRolls()`. Las clases de Test (clientes) ignoran por completo los vectores de desplazamiento espaciales y las copias profundas que ocurren por debajo.
* **Encapsulamiento** *(Protección del estado interno)*: La cuadrícula bidimensional `char[][] grid` es privada y final. Durante la destructiva simulación de la Parte B, el servicio invoca el método `getDeepCopy()` del `PaperGrid` para obtener un clon seguro, garantizando que ninguna otra clase pueda corromper el modelo original en memoria.
* **Modularidad** *(División en módulos bien definidos e independientes)*: Se aísla por completo el modelo físico del terreno (`PaperGrid`) del gestor de reglas de extracción (`PaperRollManager`), permitiendo testearlos por separado.
* **Alta Cohesión y Bajo Acoplamiento**: Existe alta cohesión porque `PaperGrid` maneja exclusivamente la topología bidimensional y `PaperRollManager` las matemáticas espaciales. El acoplamiento es mínimo: el gestor solo depende de métodos booleanos de consulta públicos expuestos por el mapa.

## Principios de Diseño

* **SOLID**
* **Single Responsibility Principle (SRP)** *(Un único motivo para cambiar)*: Hemos dividido el diseño para que el modelo `PaperGrid` cambie solo si lo hace el formato de lectura de la fábrica, mientras que el actor `PaperRollManager` cambiará si las reglas de acceso numérico se actualizan. No existe un "Modelo Gordo".
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: El algoritmo de conteo estático (Parte A) y el de destrucción por oleadas (Parte B) son radicalmente distintos. Para respetar el OCP, el paquete original `a` queda cerrado a alteraciones. Se crea un nuevo `PaperRollManager` adaptado en el paquete `b` para extender las capacidades del sistema.


* **Don't Repeat Yourself (DRY)** *(Evitar la duplicación)*: El modelo geométrico inmutable `PaperGrid` y su instanciación se extrajeron al paquete raíz `software.aoc.day04`, sirviendo de base estandarizada para ambas partes.
* **Keep It Simple, Stupid (KISS) & You Aren't Gonna Need It (YAGNI)** *(Simplicidad)*: En lugar de construir un motor recursivo complejo y abstraer cada celda en un objeto, se utilizó un enfoque directo y performante apoyado en primitivos para el manejo de las coordenadas espaciales.

## Técnicas

* **Copias Defensivas (Defensive Copying)**: En la simulación (Parte B), en lugar de exponer y mutar directamente los arrays internos de la clase, se retorna una matriz clonada (`Deep Copy`).
* **Inyección de Dependencias**: El orquestador `PaperRollManager` no asume la responsabilidad de crear o instanciar la cuadrícula; simplemente recibe el `PaperGrid` por parámetro en su método principal (`removeAllAccessibleRolls(PaperGrid originalGrid)`), desacoplando la lógica de negocio de la lectura en disco.
* **Good Naming**: Nombres claros de consulta y ejecución como `isValidPosition`, `isPaperRoll` y `removeAllAccessibleRolls`.

## Patrones de Diseño

* **Factory Method (Creacional)**: La clase `PaperGrid` utiliza el método estático `from(List<String> lines)` para aislar la validación de entrada nula/vacía y la conversión masiva de texto a arreglos de caracteres bidimensionales.

## Paradigmas 

* **Orientación a Objetos (Capa de Servicio)**: El sistema implementa un patrón "Service Layer", donde el estado protegido (`PaperGrid`) transita hacia actores lógicos (`PaperRollManager`), aislando cada responsabilidad.
* **Programación Funcional **: El flujo primario se modela utilizando la API de Streams de Java. El uso de `IntStream.range`, `flatMap`, `filter` y `mapToObj` permite recolectar los rollos de forma fluida y declarativa, elevando la limpieza del código.
* **Programación Imperativa Estructurada **: Dentro de los filtros funcionales, el cálculo espacial intensivo de los vecinos se resuelve de forma puramente imperativa (con arreglos de desplazamiento primitivos). Esto logra un equilibrio perfecto: legibilidad funcional en el alto nivel y rendimiento extremo (cero creaciones de objetos en memoria) en el bajo nivel algorítmico.
