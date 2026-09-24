# Día 1: La Entrada Secreta

El problema nos pide descifrar la verdadera contraseña de una caja fuerte calculando cuántas veces el dial apunta al 0 al seguir una secuencia de instrucciones de rotación.

## 1. Diferencias entre la Parte A y la Parte B

La evolución de los requisitos entre ambas partes ilustra la flexibilidad y resiliencia del diseño original:

* **Parte A (Paradas Estáticas):** La regla matemática es simple; solo contabilizamos las veces que el dial *termina* su movimiento y se detiene exactamente en el 0 al final de cualquier rotación.


* **Parte B (Cruces Dinámicos):** La complejidad aumenta mediante el método 0x434C49434B, exigiendo contabilizar cada vez que el dial cruza o pasa por el 0 durante el transcurso del propio movimiento. Como una rotación puede tener una magnitud inmensa (ej. R1000), el sistema calcula matemáticamente cuántas vueltas completas se han dado en un solo movimiento, siendo la contraseña final la suma de todas estas intersecciones.



## 2. Lógica Estructural

El sistema huye de la obsesión por los tipos primitivos modelando objetos del dominio real, dividiendo el programa en módulos bien definidos.

* **Order / Rotation:** Es el modelo de datos común e inmutable. Su única responsabilidad es interpretar las órdenes en formato de texto crudo (ej. "L50") y encapsular la magnitud y la dirección matemática del movimiento.


* **Dial:** Representa el estado inmutable de la rueda de la caja fuerte, guardando la posición actual y la puntuación acumulada. Se encarga exclusivamente de orquestar la matemática de las rotaciones circulares y transicionar a un nuevo estado, ocultando las fórmulas internas al exterior.


* **SafeDecoder:** Actúa exclusivamente como orquestador del flujo. Se encarga de procesar el documento completo, aplicando las rotaciones al dial de forma secuencial mediante el uso de flujos.



## 3. Fundamentos de la Ingeniería del Software

* **Abstracción:** `SafeDecoder` interactúa exclusivamente con el contrato público de `Dial` mediante su método de aplicación, abstrayéndose por completo de los detalles matemáticos de cómo se realiza el giro, las operaciones de módulo o el reajuste de valores negativos.


* **Encapsulamiento:** Para garantizar la integridad, los atributos internos de las clases están protegidos (`private final`) y toda la matemática compleja está oculta dentro de `Dial`. El exterior no tiene acceso a sus variables directamente, previniendo errores de estado inconsistente.


* **Modularidad y Acoplamiento nulo:** El `Dial` es completamente agnóstico al formato de texto del input, protegiendo la lógica central de cambios externos y logrando que los componentes sean altamente intercambiables.


* **Tell, Don't Ask & Ley de Demeter (LoD):** Se ha invertido el flujo de control para proteger la encapsulación, ordenando al dial ejecutar la rotación en lugar de extraer sus componentes internos para recalcular la lógica por fuera.


* **KISS, YAGNI y DRY:** La lógica común se comparte para evitar duplicación (DRY), y el modelado se limita exclusivamente a resolver el reto mediante estructuras simples de Java, sin añadir sobrediseño preventivo.


* **Código Expresivo (Good Naming):** Se utilizan nombres semánticos precisos alineados al dominio que permiten que el código se lea casi como lenguaje natural, aislando el "qué hace" del "cómo lo hace" y haciendo innecesarios los comentarios explicativos.



## 4. Principios de Diseño (SOLID)

* **Single Responsibility Principle (SRP):** Cada clase tiene un único motivo para cambiar. El modelo de rotación se encarga de analizar strings, el dial gestiona el estado matemático, y el decodificador orquesta el flujo.


* **Open/Closed Principle (OCP):** En lugar de llenar la clase original de código condicional al cambiar las reglas en la Parte B, se mantuvo el código cerrado y se empaquetó la solución en extensiones separadas del dominio. Asimismo, el orquestador está abierto a procesar datos de cualquier fuente sin modificar su código interno.


* **Liskov Substitution Principle (LSP):** El diseño asegura que las clases consumidoras desconocen absolutamente la implementación matemática subyacente. Solo dependen de contratos estables, garantizando una alta cohesión y permitiendo escalar el sistema sin alterar la lógica de las capas superiores.


* **Interface Segregation Principle (ISP):** Las clases exponen APIs minimalistas con un contrato claro. El orquestador interactúa únicamente con métodos imprescindibles, sin verse forzado a conocer o depender de comportamientos que no necesita utilizar.


* **Dependency Inversion Principle (DIP):** El decodificador no se acopla a clases concretas de lectura de ficheros de disco, sino que depende de la abstracción genérica `Stream<String>` nativa de Java, lo que permite inyectar dependencias y aislar el entorno.



## 5. Técnicas y Patrones de Diseño

* **Patrón Creacional (Factory Method):** Se oculta la lógica de instanciación utilizando constructores privados y métodos estáticos dedicados. Estos actúan como guardianes que validan y traducen la entrada de texto antes de permitir que un objeto válido exista en memoria.


* **Inmutabilidad del Modelo:** El sistema está libre de efectos secundarios (*side-effects*) al utilizar *Records* nativos y estados que no mutan. Todo método de rotación devuelve siempre una nueva instancia en lugar de alterar variables globales, facilitando pruebas aisladas.


* **Inyección de Dependencias e Inversión del Control (IoC):** La fuente de datos se inyecta por parámetro, delegando el control de los bucles (iteración interna) y la procedencia de la información al exterior.


* **Aritmética Modular y Complejidad Ciclomática Nula:** Se simula la naturaleza circular del dial usando aritmética de módulos (`% 100`) y fórmulas de compensación, lo que evita desbordamientos en índices negativos y erradica por completo la necesidad de sentencias condicionales anidadas (`if/else`).


* **Sustitución de Condicionales por Polimorfismo / Patrón Strategy:** Se aplican estrategias (como el uso de enumerados o inyección matemática) para delegar el comportamiento del giro matemático sin depender de interruptores de control condicionales.



## 6. Paradigmas de Programación

* **Orientación a Objetos (OO):** El software se organiza elevando los conceptos abstractos a objetos del mundo real que encapsulan estrictamente su estado y su comportamiento.


* **Programación Funcional y Declarativa:** Se sustituye la iteración imperativa clásica por la API `Stream` de Java, logrando flujos puramente inmutables. El uso de *Fluent APIs*, operaciones de filtrado y closures (lambdas que capturan limpiamente variables de su entorno) permite operaciones sin estado global mutable.



## 7. Verificación y Tests

* Las soluciones se validan de forma automática mediante pruebas unitarias escritas con la tecnología JUnit 5 y aserciones de AssertJ.


* Los tests se estructuran semánticamente siguiendo la metodología BDD (Behavior-Driven Development) bajo el patrón **Given-When-Then** (Dado un contexto, Cuando ocurre una acción, Entonces se espera un resultado).


* Esta estructura orienta las pruebas a comprobar el comportamiento del sistema, maximizando su legibilidad y sirviendo como validación robusta para las paradas estáticas de la Parte A y las intersecciones de la Parte B.


* El resultado esperado para la Parte A es 1145 y para la Parte B es 6561.