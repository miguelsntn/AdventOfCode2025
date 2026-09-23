# Día 2: Tienda de Regalos

El problema pide sanear la base de datos de la tienda de regalos del Polo Norte encontrando y sumando los IDs de productos inválidos dentro de una serie de rangos numéricos continuos.

## 1. Diferencias entre la Parte A y la Parte B

La evolución de los requisitos entre ambas partes ilustra la flexibilidad del diseño y cómo una buena arquitectura minimiza drásticamente el impacto del cambio:

* **Parte A:** La regla de validación indica que un ID es inválido si está formado por una secuencia de dígitos simétrica que se repite *exactamente* dos veces (ej. `1212`). Se resolvió utilizando operaciones matemáticas de subcadenas (`substring`).
* **Parte B:** La regla se amplía: un ID es inválido si está formado por *cualquier* secuencia repetida consecutivamente, sin importar si se repite 2, 3 o más veces (ej. `123123` o `1111`). Para soportar esta complejidad sin crear algoritmos imperativos ilegibles, se adaptó la lógica al uso de **Expresiones Regulares** (`^(.+)\1+$`), encapsuladas de forma óptima.

## 2. Lógica Estructural

Para evitar la penalización por recolección de basura (*Memory Churn*) que sufriría el sistema al instanciar millones de objetos por cada identificador (ej. crear una clase `Product`), se diseñó una arquitectura orientada al alto rendimiento operando directamente sobre tipos primitivos:

* **`Range` (Record):** Modelo de dominio inmutable. Encapsula matemáticamente el intervalo inicial y final de cada bloque. Su única responsabilidad pública es saber construirse a sí mismo desde un texto plano y generar una secuencia continua de números primitivos (`LongStream`).
* **`GiftShopDatabase` (Record):** Actúa como el orquestador principal. Coordina el flujo de datos desde el parseo inicial (Factory Method), aplica los filtros de validación basados en las reglas elfas y agrega matemáticamente los resultados finales.

## 3. Principios de Diseño (SOLID)

El motor de resolución se ha diseñado respetando estrictamente los 5 principios SOLID:

* **Single Responsibility Principle (SRP):** Cada componente tiene una única razón para cambiar. `Range` maneja exclusivamente la matemática de los límites del intervalo y su expansión, mientras que `GiftShopDatabase` asume en exclusiva la responsabilidad de implementar las normativas de detección de IDs inválidos y su suma.
* **Open/Closed Principle (OCP):** El diseño ha demostrado estar abierto a la extensión pero cerrado a la modificación. Al pasar a la Parte B, las nuevas reglas de validación múltiple se integraron implementando una nueva estrategia de filtro, dejando la estructura original del orquestador y del objeto `Range` completamente intactas.
* **Liskov Substitution Principle (LSP) y Composition:** Al utilizar abstracciones estándar nativas de Java (`LongStream`) para la transferencia de datos entre el modelo y el orquestador, garantizamos que cualquier cambio subyacente en la forma de generar los números no romperá los contratos del sistema ni la correctitud del programa.
* **Interface Segregation Principle (ISP):** Exposición de APIs minimalistas (Principio de Mínimo Compromiso). La clase `Range` no expone sus variables internas (`start`, `end`) mediante *getters*, sino que ofrece un único método funcional: `stream()`.
* **Dependency Inversion Principle (DIP):** El orquestador de datos no depende de implementaciones algorítmicas de bajo nivel para iterar (bucles `for` manuales), sino que depende de abstracciones de alto nivel (`Streams`), invirtiendo el control del flujo hacia la API de Java.

## 4. Fundamentos de la Ingeniería y Clean Code

* **Don't Repeat Yourself (DRY):** Al notar que el dominio topológico era idéntico para ambas partes del problema, la clase `Range` se extrajo a un paquete padre compartido (`software.aoc.day02`), evitando la duplicación de lógica.
* **Law of Demeter (LoD) / Tell, Don't Ask:** `GiftShopDatabase` no le extrae a `Range` sus límites internos para iterar sobre ellos manualmente; en su lugar, le "ordena" expandirse (`Range::stream`), respetando su encapsulamiento al máximo.
* **Good Naming (Código Expresivo y Autodocumentado):** El uso de *Fluent APIs* permite que el código se lea como lenguaje natural. La línea `ranges.stream().flatMapToLong(Range::stream).filter(GiftShopDatabase::isRepeatedPattern).sum();` se lee literalmente como: *"Toma los rangos, expándelos a una secuencia de números, filtra solo aquellos con un patrón repetido, y súmalos"*.
* **Keep It Simple, Stupid (KISS):** El parseo de los rangos se realiza mediante operaciones directas de cadenas (`split`) sin requerir pesados motores de análisis sintáctico.

## 5. Técnicas, Optimización y Patrones de Diseño

* **Patrón Creacional (Factory Method):** La creación de objetos se delega a métodos estáticos (`Range.from`, `GiftShopDatabase.from`) con constructores privados. Estos actúan como guardianes que parsean, dividen y validan el texto crudo asegurando que los objetos nazcan en estados válidos.
* **Inmutabilidad Absoluta:** Todas las estructuras de datos, instanciadas mediante `records` nativos y colecciones selladas (`List.copyOf` o `.toList()`), impiden mutaciones accidentales eliminando los *side-effects*.
* **Prevención de Memory Churn (Primitivos vs Objetos):** En lugar de mapear el flujo de datos hacia objetos complejos dentro del bucle, la arquitectura utiliza `flatMapToLong` para mantener todo el procesamiento en la pila (*Stack*) mediante tipos primitivos (`long`). Esto evita instanciar millones de objetos en la memoria dinámica (*Heap*) y paralizar la aplicación por pausas del *Garbage Collector*.
* **Compilación Estática de Expresiones Regulares:** En la Parte B, el validador de Regex se instanció como una constante precompilada (`private static final Pattern SILLY_PATTERN`). El uso ingenuo de `String.matches()` dentro de un Stream obligaría a Java a recompilar el patrón millones de veces ($O(N)$). Precompilarlo reduce drásticamente la huella computacional.

## 6. Paradigmas de Programación

* **Orientación a Objetos (OO):** Los conceptos del problema han sido modelados como entidades reales. En lugar de trabajar con *arrays* primitivos inconexos, se han diseñado clases que agrupan lógicamente sus datos e interacciones (Encapsulamiento).
* **Programación Funcional y Estilo Declarativo:** Sustitución de la iteración imperativa clásica por la API de `Stream` de Java, logrando flujos funcionales sin estado global mutable. El motor interno de Java asume el control del iterador, permitiendo un cálculo paralelo si fuese necesario.

## 7. Verificación y Tests (BDD)

Las soluciones se validan de forma automática mediante **pruebas unitarias** escritas con JUnit 5 y aserciones limpias de AssertJ.

* Las pruebas están estructuradas semánticamente bajo el patrón **Given-When-Then** (Dado un contexto, Cuando ocurre una acción, Entonces se espera un resultado), heredado del enfoque **BDD (Behavior-Driven Development)**.
* Se utilizan las especificaciones oficiales de los ejemplos del rompecabezas para comprobar el comportamiento del sistema, garantizando que el diseño de las *Regex* y las lógicas numéricas son completamente estancas antes de inyectarles los archivos de texto reales en el entorno de producción.