# Día 2: Tienda de Regalos (Base de Datos)

El problema pide sanear la base de datos de la tienda de regalos del Polo Norte encontrando y sumando los IDs de productos inválidos dentro de una serie de rangos numéricos.

* **En la Parte A**, la regla de validación nos indica que un ID es inválido si está formado por una secuencia de dígitos simétrica que se repite exactamente dos veces.
* **En la Parte B**, la regla se amplía y se vuelve más compleja: un ID es inválido si está formado por cualquier secuencia repetida, sin importar la longitud del patrón ni si se repite 2, 3 o más veces.

## 1. Fundamentos de la Ingeniería del Software

* **Abstracción (Simplificación de la complejidad):** He aplicado este fundamento ocultando los detalles complejos detrás de una interfaz simple. La clase `Range` expone el método público `expandToSequence()`. El cliente o clase orquestadora no necesita saber si internamente hay un bucle `for`, ni cómo se manejan los límites exactos de los intervalos numéricos. Toda esa lógica está abstraída; el orquestador simplemente le pide al rango que se expanda y recibe el flujo de datos.


* **Encapsulamiento (Protección de la integridad del estado):** Para garantizar que el programa sea robusto, los atributos numéricos de la clase `Range` (`start` y `end`), así como la lista de rangos en `GiftShopDatabase`, están declarados como `private final`. Con esto logro esconder la complejidad y mostrar una interfaz más simple. Nadie desde fuera puede alterar un límite o modificar la lista original directamente.


* **Modularidad (División estratégica del sistema):** El sistema divide el código en módulos que pueden ser desarrollados y probados de forma independiente. He separado el concepto puramente matemático del intervalo (la clase `Range`) de la lógica de negocio y filtrado de la base de datos (la clase `GiftShopDatabase`).


* **Alta Cohesión y Bajo Acoplamiento:**
* *Alta cohesión:* Las partes de cada módulo están estrechamente relacionadas y enfocadas a una única tarea. `Range` se dedica en exclusiva a definir límites y generar números. `GiftShopDatabase` cohesiona la orquestación del filtrado y la suma total.


* *Bajo acoplamiento:* Los módulos tienen muy pocas interdependencias. La clase `GiftShopDatabase` opera directamente sobre la secuencia numérica generada, sin importarle la estructura interna del rango.





## 2. Principios de Diseño (SOLID y Clean Code)

* **Good Naming (Código Expresivo y Auto-documentado):** El código debe ser claro y comprensible, facilitando la lectura sin necesidad de comentarios. En lugar de comentarios superfluos, he asignado nombres claros y relacionados con su propósito a los métodos. Nombres como `sumInvalidIds()`, `expandToSequence()` e `isTwiceRepeatedPattern()` permiten que el código se lea casi como lenguaje natural, cumpliendo la regla de que los métodos deben nombrarse por lo que hacen.


* **Single Responsibility Principle - SRP (Principio de Responsabilidad Única):** Cada clase tiene una sola razón para cambiar, reflejando una alta cohesión. Mi diseño garantiza esto: si mañana cambia la matemática de los intervalos, solo se modificará `Range`. Si cambian las normativas de validación de los elfos, solo se modificará `GiftShopDatabase`.


* **Open/Closed Principle - OCP (Abierto a la extensión, cerrado a la modificación):** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación. Al pasar de la Parte A a la Parte B, mantuve el código original intacto. Empaqueté la solución extendida en `software.aoc.day02.b` para aplicar las nuevas reglas (usando expresiones regulares), dejando la Parte A cerrada y a salvo.


* **Don't Repeat Yourself - DRY (No repetir código):** Se debe evitar la duplicación de código promoviendo su reutilización. Al notar que la clase `Range` era idéntica para ambas partes del problema, la extraje a un paquete padre compartido (`software.aoc.day02`), asegurando que esa pieza de conocimiento tuviera una representación única e inequívoca.



## 3. Técnicas y Patrones de Diseño

* **Patrón Creacional: Factory Method:** He evitado el uso de constructores públicos directos con `new`. En su lugar, los constructores son privados para restringir la creación directa y he utilizado un método estático que encapsula la creación del objeto. Métodos como `Range.from()` y `GiftShopDatabase.from()` actúan como filtros: parsean el texto, validan que no sea nulo, dividen las cadenas de texto y aseguran que el objeto nazca en un estado perfectamente válido.


* **Inmutabilidad del Modelo (Clases Inmutables y Colecciones):** Las clases del modelo son inmutables, es decir, su estado no cambia una vez creado. La clase `Range` es inmutable. Además, en la instanciación de `GiftShopDatabase`, apliqué inmutabilidad estricta a las colecciones utilizando `List.copyOf()` al terminar de recolectar los rangos. Esto sella la lista y evita por completo las fugas de memoria o modificaciones externas.



## 4. Paradigmas de Programación

* **Paradigma de Orientación a Objetos (OO):** He modelado los conceptos del problema elevándolos a entidades reales con estado propio. En lugar de trabajar con *arrays* primitivos o simples variables de texto para representar los rangos numéricos, he diseñado clases robustas que agrupan lógicamente sus datos y comportamientos.


* **Programación Funcional (API de Streams):** Este paradigma trata la computación como la evaluación de funciones y favorece la inmutabilidad. He implementado el núcleo de procesamiento utilizando la API de Streams de Java, la cual facilita el procesamiento funcional de colecciones permitiendo operaciones más eficientes y legibles de manera declarativa. Específicamente, he utilizado la operación intermedia `flatMapToLong` para aplanar múltiples flujos numéricos en uno solo, seguida de `filter` y la operación final `sum` para consumir el *stream* y obtener el resultado de forma impecable sin recurrir a costosos bucles imperativos.