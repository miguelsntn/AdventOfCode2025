# Day 1: Secret Entrance

El problema nos pide descifrar la verdadera contraseña de una caja fuerte calculando cuántas veces el dial apunta al 0 al seguir una secuencia de instrucciones de rotación. En la Parte A se cuenta las veces que el dial termina su movimiento en 0, mientras que en la Parte B se cuenta cada vez que pasa por el 0 durante el propio movimiento.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `Dial` expone un único método público `applyOrder(Order order)` que abstrae toda la lógica matemática de las rotaciones circulares (ajustes de módulo, cálculo de distancias hasta el cero y conteos de cruces). El cliente que lo usa no necesita conocer la complejidad de cómo se calculan esos giros.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: Los atributos `currentPosition` y `zerosCount` de la clase `Dial`, así como `direction` y `distance` de `Order`, son privados y finales. El estado interno está completamente blindado y nadie desde fuera puede alterarlo directamente.
* **Modularidad** *(División del programa en módulos bien definidos e independientes)*: Se ha dividido la responsabilidad del sistema en dos entidades claras y separadas: `Order` gestiona el parseo y validación de las instrucciones de texto, mientras que `Dial` gestiona exclusivamente las físicas y reglas del dial de la caja fuerte.
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: `Order` tiene una alta cohesión al dedicarse únicamente a representar una instrucción válida. `Dial` hace lo propio calculando las posiciones. El acoplamiento es bajo porque `Dial` simplemente recibe un objeto `Order` y opera con él, sin importarle en absoluto de qué fichero vino o cómo fue convertido desde el texto original.

## Principios de Diseño

* **Good Naming** *(Nombres descriptivos y precisos)*: Se utilizan nombres expresivos en los métodos como `createStartingAt`, `fromString`, y `applyOrder`. Esto hace que el código se lea de forma natural y elimina por completo la necesidad de añadir comentarios explicativos para entender la intención del código.
* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: La clase `Order` cambiará solo si cambia el formato de entrada de las instrucciones de los elfos. La clase `Dial` cambiará solo si se alteran las reglas del conteo de la caja fuerte.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La separación de la Parte A y la Parte B en paquetes distintos (`software.aoc.day01.a` y `software.aoc.day01.b`) permite introducir las nuevas y más complejas reglas de conteo sin alterar, ensuciar ni romper el código original que resolvía la primera parte.

## Técnicas y Patrones

* **Factory Method (Creacional)** *(Encapsulación de la creación de objetos en métodos estáticos dedicados)*: Tanto `Dial.createStartingAt` como `Order.fromString` encapsulan la lógica de creación e instanciación. Los constructores de ambas clases se han marcado como `private` para prohibir instanciaciones directas inseguras y obligar al uso de estas factorías, permitiendo, por ejemplo, validar los strings de entrada antes de crear una `Order`.
* **Clases Inmutables** *(Objetos cuyo estado no puede ser modificado tras su creación)*: La clase `Dial` es completamente inmutable. En lugar de mutar su posición interna mediante setters al rotar, el método `applyOrder` devuelve una nueva instancia limpia de `Dial` con los nuevos valores calculados, eliminando por completo el riesgo de efectos secundarios imprevistos.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: Se han modelado los conceptos del dominio (el dial y las órdenes de rotación) como objetos reales con estado protegido y comportamiento bien definido, en lugar de utilizar variables sueltas y arrays de datos.
* **Programación Funcional** *(Estilo declarativo basado en funciones puras y datos inmutables)*: Apoyado fuertemente por la inmutabilidad de la clase `Dial`, la cual actúa como una función pura al calcular su siguiente estado devolviendo un objeto nuevo en base a una orden, permitiendo orquestar las llamadas mediante transformaciones y reducciones (Streams).