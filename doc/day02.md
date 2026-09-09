# Day 2: Gift Shop Database

El problema nos pide sanear la base de datos de la tienda de regalos del Polo Norte encontrando y sumando los IDs de productos inválidos dentro de una serie de rangos numéricos. En la Parte A, un ID es inválido si está formado por una secuencia de dígitos repetida exactamente dos veces. En la Parte B, la regla se amplía y un ID es inválido si está formado por una secuencia repetida dos o más veces.

## Fundamentos

* **Abstracción** *(Simplificación de detalles complejos mediante interfaces o contratos claros)*: La clase `Range` expone el método público `expandToSequence()`, el cual abstrae la generación de números secuenciales. El cliente (`GiftShopDatabase`) no necesita programar bucles manuales ni conocer los límites exactos, simplemente pide la secuencia expandida.
* **Encapsulamiento** *(Ocultación del estado interno y protección de los datos)*: Los límites numéricos `start` y `end` de la clase `Range`, así como la lista interna de rangos en `GiftShopDatabase`, son privados y finales. Están blindados para que ninguna clase externa pueda alterarlos o recalcularlos directamente.
* **Modularidad** *(División del programa en módulos bien definidos e independientes)*: Se aísla el concepto puramente matemático del intervalo (`Range`) del concepto de negocio y filtrado de la base de datos (`GiftShopDatabase`).
* **Alta Cohesión y Bajo Acoplamiento** *(Los módulos hacen una sola cosa y dependen mínimamente entre sí)*: Existe alta cohesión porque `Range` se dedica únicamente a definir límites y `GiftShopDatabase` asume la única responsabilidad de orquestar la suma de los códigos inválidos. El acoplamiento es bajo porque el orquestador opera sobre el `expandToSequence()` sin tener que manipular los límites internos del rango manualmente.

## Principios de Diseño

* **Good Naming** *(Nombres descriptivos y precisos)*: El uso de nombres claros ligados al dominio del problema, como `sumInvalidIds` e `isRepeatedPattern`, permite que el código sea autoexplicativo y se lea de forma natural, evitando el uso de comentarios innecesarios.
* **Single Responsibility Principle (SRP)** *(Una clase debe tener una sola razón para cambiar)*: La clase `Range` solo cambiará si se modifica la forma en que se definen los límites matemáticos. La clase `GiftShopDatabase` solo cambiará si los elfos deciden modificar las reglas de validación de los IDs.
* **Open/Closed Principle (OCP)** *(Abierto a la extensión, cerrado a la modificación)*: La separación en paquetes `a` y `b` permite actualizar las reglas del negocio (pasar de buscar patrones repetidos exactamente dos veces, a buscarlos dos o más veces mediante expresiones regulares) extendiendo la funcionalidad sin alterar el código que resolvía la primera parte.

## Técnicas y Patrones

* **Factory Method (Creacional)** *(Encapsulación de la creación de objetos en métodos estáticos dedicados)*: Tanto `Range.from` como `GiftShopDatabase.from` encapsulan la lógica de instanciación a partir de textos planos separados por guiones o comas. Los constructores son privados, aislando al resto del sistema de la estructura del fichero de entrada y evitando instanciaciones incorrectas.
* **Clases Inmutables** *(Objetos cuyo estado no puede ser modificado tras su creación)*: La clase `Range` es inmutable; una vez creados sus límites de inicio y fin, estos no pueden variar, lo que garantiza la integridad de los datos durante el procesamiento masivo.

## Paradigmas

* **Orientación a Objetos** *(Organización del software en objetos que encapsulan estado y comportamiento)*: En lugar de trabajar con arrays primitivos de strings o tuplas numéricas, se han creado entidades robustas (`Range` y `GiftShopDatabase`) que controlan su propio estado interno y exponen comportamientos seguros.
* **Programación Funcional** *(Estilo declarativo basado en funciones puras y datos inmutables)*: El núcleo del procesamiento (`sumInvalidIds`) utiliza la API de Streams de Java (`flatMapToLong`, `filter`, `sum`). Esto permite leer el código como una tubería declarativa de transformaciones, procesando millones de IDs sin necesidad de utilizar bucles tradicionales ni mutar variables acumuladoras globales.