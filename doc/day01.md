# Día 1: La Entrada Secreta (Caja Fuerte)

El problema nos pide descifrar la verdadera contraseña de una caja fuerte calculando cuántas veces el dial apunta al 0 al seguir una secuencia de instrucciones de rotación.

* **En la Parte A**, la regla matemática es simple: solo contamos las veces que el dial *termina* su movimiento exactamente en el 0.
* **En la Parte B**, la complejidad matemática aumenta drásticamente: hay que llevar un registro de cada vez que el dial *cruza o pasa por* el 0 durante el transcurso del propio movimiento, calculando las distancias intermedias.


## 1. Fundamentos de la Ingeniería del Software

* **Abstracción (Simplificación de la complejidad):** He aplicado este fundamento en la clase `Dial`, la cual expone un único método público llamado `applyOrder(Order order)`. Este método actúa como un contrato claro. El orquestador que usa esta clase no necesita saber nada sobre cómo se calculan las operaciones de módulo matemático (`% 100`), cómo se reajustan los valores negativos, ni cómo se calculan las distancias hasta el cero. Toda esa complejidad algorítmica está abstraída; el cliente simplemente le dice al dial "aplica esta orden" y recibe el resultado.
* **Encapsulamiento (Protección de la integridad del estado):** Para garantizar que el programa sea robusto, los atributos internos de las clases (`currentPosition` y `zerosCount` en `Dial`; `direction` y `distance` en `Order`) están declarados como `private final`. Con esto logro un blindaje total del estado interno: ninguna otra clase desde fuera puede sobrescribir una posición o alterar un contador directamente, previniendo así errores de estado inconsistente.
* **Modularidad (División estratégica del sistema):** El sistema no es un bloque monolítico de código. He dividido el problema en piezas independientes y reutilizables. Por un lado, la clase `Order` se encarga exclusivamente de interpretar el texto (como `"L5"`). Por otro, la clase `Dial` maneja las físicas de la caja fuerte. Si mañana decido reutilizar la clase `Order` para otro problema del Advent of Code, puedo llevarme el archivo sin arrastrar dependencias innecesarias.
* **Alta Cohesión y Bajo Acoplamiento:**
* *Alta cohesión:* Cada clase hace una sola cosa y la hace muy bien. `Order` cohesiona los datos de una instrucción. `Dial` cohesiona la lógica de rotación.
* *Bajo acoplamiento:* El `Dial` ignora por completo si los datos vinieron de un archivo `.txt`, de una base de datos o de un input manual. Solo depende de recibir un objeto `Order` válido, lo que hace que los componentes sean altamente intercambiables.



## 2. Principios de Diseño (SOLID y Clean Code)

* **Good Naming (Código Expresivo y Auto-documentado):** En lugar de llenar el código de comentarios explicando qué hace cada bloque, he invertido esfuerzo en dar nombres precisos y semánticos. Nombres de métodos como `createStartingAt(50)`, `fromString(line)`, y `applyOrder(order)` permiten que el código se lea casi como lenguaje natural (inglés). Si el código es expresivo, los comentarios explicando "el qué" sobran.
* **Single Responsibility Principle - SRP (Principio de Responsabilidad Única):** Mi diseño garantiza que cada clase tenga un único motivo para cambiar. Si mañana los elfos deciden cambiar el formato del texto de las instrucciones (por ejemplo, pasar de `"L5"` a `"Left-5"`), la única clase que sufrirá modificaciones será `Order`. Por el contrario, si cambian las reglas matemáticas de cómo gira la caja fuerte, solo se modificará la clase `Dial`.
* **Open/Closed Principle - OCP (Abierto a la extensión, cerrado a la modificación):** Este es el principio más evidente de mi arquitectura. Al pasar de la Parte A a la Parte B, las reglas de conteo cambiaron radicalmente. En lugar de llenar mi clase original con código condicional , lo que rompería el OCP y ensuciaría el código, decidí empaquetar la solución en directorios separados (`software.aoc.day01.a` y `software.aoc.day01.b`). Mantuve el código de la Parte A cerrado y a salvo, y creé una extensión del dominio en la Parte B para alojar las nuevas reglas.

## 3. Técnicas y Patrones de Diseño

* **Patrón Creacional: Factory Method:** He evitado deliberadamente el uso de constructores públicos (`new Order()`). En su lugar, los constructores son privados y he expuesto métodos de factoría estáticos (`Order.fromString()` y `Dial.createStartingAt()`). Esto me permite un control absoluto sobre la instanciación. En el caso de `Order.fromString`, el método actúa como un guardián: valida que el string no sea nulo, que no esté vacío y que tenga el formato correcto antes de permitir que el objeto se asigne en memoria.
* **Inmutabilidad del Modelo (Clases Inmutables):** Esta es una de las decisiones técnicas más fuertes del proyecto. Mi clase `Dial` no tiene *setters*. Cuando se invoca el método `applyOrder`, la posición del dial actual no muta. En su lugar, el algoritmo calcula los nuevos valores y devuelve un `new Dial(nuevaPosicion, nuevoConteo)`. Trabajar con objetos inmutables elimina por completo los *side effects* (efectos secundarios) y las fugas de datos, haciendo que el rastreo de errores sea infinitamente más sencillo.

## 4. Paradigmas de Programación

* **Paradigma de Orientación a Objetos (OO):** He huido de la "obsesión por los tipos primitivos". En lugar de manejar las instrucciones manipulando arrays de strings en crudo o variables sueltas, he elevado los conceptos del problema a objetos de dominio reales. El dial y las órdenes existen como entidades modeladas con estado protegido y comportamiento restringido.
* **Acercamiento al Paradigma Funcional:** Aunque Java es un lenguaje predominantemente orientado a objetos, el diseño de la clase `Dial` bebe directamente de la programación funcional. Al hacer que el método `applyOrder` devuelva siempre una copia nueva del estado en lugar de modificar variables globales, el método se comporta como una **función pura**. Esto garantiza que si le paso la misma instrucción al mismo estado inicial, siempre producirá exactamente el mismo resultado nuevo, sin depender de estados externos ocultos.