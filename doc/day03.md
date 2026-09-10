# Day 3: Lobby

El problema nos pide encontrar el mayor número (voltaje máximo) que se puede formar encendiendo un número específico de baterías en cada banco, manteniendo estrictamente su orden original. En la Parte A se deben encender exactamente 2 baterías (formando un número de 2 dígitos), mientras que en la Parte B el requerimiento sube a 12 baterías (12 dígitos), lo que exige un cambio de estrategia algorítmica para mantener el rendimiento.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `BatteryBank` expone un único método público `calculateMaxJoltage()`. El cliente que llama a este método se abstrae por completo de los detalles de implementación, sin necesidad de saber si internamente se usa un array de preálculo de sufijos (Parte A) o un algoritmo voraz con una pila (Parte B).
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: La cadena de texto `ratings` que define los valores del banco de baterías es privada y final. El estado interno está completamente blindado y protegido contra mutaciones externas.
* **Modularidad** *(División del programa en módulos bien definidos e independientes)*: Se ha dividido la responsabilidad del sistema separando la lógica matemática y de negocio (encapsulada en `BatteryBank`) de la responsabilidad de lectura de ficheros y orquestación (gestionada en las clases de Test).
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: `BatteryBank` tiene una alta cohesión al dedicarse exclusivamente a resolver el problema de maximización de su propia secuencia. El acoplamiento es bajo porque el orquestador se limita a invocar el método de cálculo y sumar el resultado, sin conocer la estructura interna de los datos.

## Principios de Diseño

* **Good Naming** *(Nombres descriptivos y precisos)*: Se han utilizado nombres que explican por sí mismos la intención del código (como `maxFromRight`, `removeCount`, `currentDigit` o `calculateMaxJoltage`), lo que facilita la lectura y reduce la necesidad de comentarios explicativos.
* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: La clase `BatteryBank` concentra la responsabilidad de calcular el voltaje máximo. Solo cambiará si las reglas de cálculo cambian. Las clases de Test solo cambiarán si cambia el origen de los datos.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La separación en paquetes (`a` y `b`) permitió introducir un algoritmo completamente nuevo y más sofisticado para resolver el reto de los 12 dígitos en la Parte B, sin necesidad de modificar o romper el código optimizado que ya resolvía la Parte A.

## Técnicas y Patrones

* **Factory Method (Creacional)** *(Encapsulación de la creación de objetos en métodos estáticos dedicados)*: La creación de los objetos se realiza mediante el método estático `BatteryBank.from()`. Al mantener el constructor privado, se fuerza a los clientes a utilizar esta factoría, la cual se encarga de validar la entrada (por ejemplo, comprobar la longitud mínima de la cadena) antes de permitir la instanciación.
* **Clases Inmutables** *(Objetos cuyo estado no puede ser modificado tras su creación)*: La clase `BatteryBank` es inmutable. No expone métodos *setter* ni modifica su cadena original, operando de forma segura sin efectos secundarios.
* **Algoritmo Greedy (Voraz) con Pila** *(Técnica de optimización)*: Para resolver la Parte B de forma eficiente, se implementó una estrategia voraz utilizando un `StringBuilder` a modo de pila (*Stack*). Esto permite descartar los dígitos subóptimos y encontrar la secuencia máxima en tiempo $O(N)$, evitando el uso de bucles anidados que penalizarían drásticamente el rendimiento con secuencias muy largas.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: Cada línea del fichero de texto no se trata simplemente como un String primitivo, sino que se instancia como un objeto real del dominio (`BatteryBank`) con su propio comportamiento y responsabilidades definidas.
* **Programación Funcional** *(Estilo declarativo basado en funciones puras y datos inmutables)*: El flujo principal de la aplicación procesa el fichero mediante la API de Streams de Java. El uso de `filter`, `map`, `mapToLong` y `sum` crea un *pipeline* de datos puramente declarativo, eliminando el uso de contadores y bucles iterativos tradicionales.