# Herramientas — Algoritmos y Estructuras de Datos

Repositorio correspondiente al trabajo de **Algoritmos y Estructuras de Datos (AED)**.

El proyecto contiene las estructuras lineales desarrolladas para el Desafío 1 y su utilización en el sistema de gestión de un **taller mecánico**, correspondiente al Desafío 2.

---

## 1. Estructuras de datos

Para el Desafío 1 se implementaron diferentes estructuras lineales utilizando la representación correspondiente en cada caso: un arreglo dinámico propio para `ListaArreglo` y nodos enlazados para las listas, pilas y colas enlazadas.

Las principales estructuras desarrolladas son:

* `Lista`
* `ListaDoble`
* `ListaCircular`
* `ListaCircularDoble`
* `ListaArreglo`
* `Pila`
* `Cola`
* `Conjunto`
* `PilaPrioridad`
* `ColaPrioridad`

También se definieron las interfaces correspondientes a los TDA:

* `TDALista`
* `TDAPila`
* `TDACola`
* `TDAConjunto`

La idea es que las estructuras sean reutilizables desde otras partes del proyecto, sin que el código del sistema del taller tenga que implementar nuevamente su funcionamiento.

---

# 2. Desafío 2 — Taller Mecánico

## Escenario

El sistema representa un taller mecánico que recibe vehículos para realizar mantenimientos o reparaciones.

El ingreso tiene **dos caminos distintos**, tal como plantea el escenario:

1. **Mantenimiento planificado**: el trabajo ya es conocido cuando el vehículo llega, por lo que se registra inmediatamente como una reparación pendiente.
2. **Problema informado por el dueño**: se conserva la descripción del problema, pero no se inventa una reparación al ingreso. La reparación correspondiente se agrega después de diagnosticar la falla.

Ambos caminos terminan en la misma `Cola<Vehiculo>` de espera, por lo que el orden de atención continúa siendo FIFO independientemente del motivo de ingreso.

Un vehículo puede:

1. Llegar al taller y quedar esperando.
2. Ser asignado a un tallerista.
3. Tener una o más reparaciones pendientes.
4. Detectar problemas adicionales durante el diagnóstico.
5. Quedar esperando repuestos.
6. Continuar el trabajo cuando llegan los repuestos.
7. Finalizar y quedar pronto para retirar.

El modelo busca representar estos estados utilizando las estructuras desarrolladas en el Desafío 1.

---

# 3. Decisiones de diseño

## Vehículos esperando atención → `Cola`

Los vehículos que todavía no fueron atendidos se almacenan en:

```text
Cola<Vehiculo>
```

La elección de una cola se debe a que el escenario indica que los vehículos deben respetar el orden de llegada.

La estructura mantiene referencias al `frente` y al `fin`, por lo que agregar al final y quitar del frente son operaciones de tiempo constante.

### Complejidad

* `poneEnCola()` → **O(1)**
* `quitaDeCola()` → **O(1)**
* `frente()` → **O(1)**
* `tamaño()` → **O(1)**

Por lo tanto, registrar y obtener el próximo vehículo no requiere recorrer toda la estructura.

---

## Vehículos esperando repuestos → `Cola`

Los vehículos que no pueden continuar porque necesitan repuestos se almacenan en otra:

```text
Cola<Vehiculo>
```

Se utiliza una cola porque los vehículos que entran en esta situación también deben poder continuar respetando el orden en que quedaron esperando.

Cuando un vehículo pasa a esperar repuestos:

1. Se elimina de los vehículos actualmente en trabajo.
2. Se agrega al final de la cola de repuestos.
3. Se libera el tallerista que estaba trabajando con él.

Cuando llegan los repuestos:

1. Se toma el vehículo del frente.
2. Se busca un tallerista disponible.
3. Se vuelve a asignar el vehículo.
4. El vehículo vuelve a la lista de vehículos en trabajo.

---

## Reparaciones pendientes → `Pila`

Cada `Vehiculo` tiene:

```text
Pila<Reparacion> reparacionesPendientes
```

La pila permite representar una situación importante del escenario: durante la inspección puede aparecer una falla adicional que debe resolverse antes que el problema original.

Por ejemplo:

```text
Reparación original
        ↓
Cambio de aceite

Se detecta una falla adicional
        ↓
Cambio de pastillas
```

La reparación adicional se agrega después, por lo que queda arriba de la pila y se realiza primero.

Esto corresponde al comportamiento **LIFO (Last In, First Out)**.

### Complejidad

Como la pila está implementada mediante una referencia al nodo superior:

* `mete()` → **O(1)**
* `saca()` → **O(1)**
* `tope()` → **O(1)**
* `tamaño()` → **O(1)**

Por lo tanto, agregar o realizar la próxima reparación no requiere recorrer las reparaciones anteriores.

---

## Historial de reparaciones → `Lista`

Las reparaciones que ya fueron realizadas se guardan en:

```text
Lista<Reparacion> reparacionesRealizadas
```

La lista permite mantener un historial de los trabajos realizados sobre el vehículo.

Cuando una reparación se realiza, se quita de la pila de pendientes y se agrega al historial.

En nuestra implementación de `Lista`, los elementos están almacenados mediante nodos enlazados.

### Complejidad

* `obtener(i)` → **O(n)** en el peor caso.
* `remover(i)` → **O(n)** en el peor caso.
* `remover(elemento)` → **O(n)**.
* `contiene(elemento)` → **O(n)**.
* `buscar(criterio)` → **O(n)**.
* `tamaño()` → **O(1)**.
* `esVacio()` → **O(1)**.
* `agregar(elemento)` → **O(n)**.

`agregar(elemento)` es O(n) porque la implementación recorre la lista hasta encontrar el último nodo antes de insertar el nuevo elemento.

### Ordenamiento de `Lista`

`ordenar(Comparator)` utiliza **Merge Sort**. La copia previa de los nodos se construye en O(n) mediante un puntero local al último nodo de la copia, sin llamar repetidamente a `agregar(elemento)`. Luego Merge Sort divide recursivamente y mezcla los nodos en O(n log n). Por lo tanto, el método completo `ordenar()` queda en **O(n log n)** y no modifica la lista original.

---

# 4. Modelo de clases

## `Taller`

Es la clase principal del sistema.

Se encarga de administrar:

* vehículos esperando atención;
* vehículos esperando repuestos;
* talleristas;
* vehículos actualmente en trabajo;
* vehículos que ya están prontos para retirar.

Sus principales estructuras son:

```text
Cola<Vehiculo> vehiculosEnEspera
Cola<Vehiculo> esperandoRepuestos

Lista<Tallerista> talleristas
Lista<Vehiculo> vehiculosEnTrabajo
Lista<Vehiculo> vehiculosProntos
```

La clase `Taller` es la que coordina las operaciones entre estas estructuras.

---

## `Vehiculo`

Representa un vehículo que ingresa al taller.

Contiene información básica para identificarlo:

* patente;
* marca;
* modelo;
* dueño.

También conserva la información de ingreso:

* `TipoIngreso tipoIngreso`, con los valores `MANTENIMIENTO_PLANIFICADO` o `PROBLEMA_INFORMADO`;
* `String detalleIngreso`, que guarda el mantenimiento acordado o el problema descrito por el dueño.

El motivo se registra una sola vez cuando el vehículo es aceptado por `Taller`, evitando que el mismo objeto sea ingresado dos veces.

Además mantiene:

```text
Pila<Reparacion> reparacionesPendientes
Lista<Reparacion> reparacionesRealizadas
```

De esta forma, cada vehículo administra sus propias reparaciones pendientes y su historial.

---

## `Reparacion`

Representa un trabajo que debe realizarse sobre un vehículo.

Contiene:

* descripción;
* tipo.

El tipo permite diferenciar, por ejemplo, un mantenimiento de una falla adicional.

---

## `Tallerista`

Representa a una persona que trabaja en el taller.

Mantiene:

```text
String nombre
Vehiculo vehiculoActual
```

Si `vehiculoActual` es `null`, el tallerista está disponible.

Cuando se asigna un vehículo, pasa a estar ocupado. Cuando el vehículo deja de estar a su cargo, vuelve a estar disponible.

---

# 5. Flujo principal del sistema

Antes de entrar a la cola de espera se distingue el motivo de ingreso:

```text
                         Llega el vehículo
                               |
                  +------------+------------+
                  |                         |
       Mantenimiento planificado     Problema informado
                  |                         |
     Trabajo conocido: se agrega       Se conserva el síntoma;
     a la Pila de pendientes           primero se diagnostica
                  |                         |
                  +------------+------------+
                               |
                         Cola de espera
                               |
                         Atención FIFO
```

Después de ese ingreso, el flujo general es:

```text
                  ┌─────────────────────┐
                  │ Llega el vehículo   │
                  └──────────┬──────────┘
                             ↓
                  ┌─────────────────────┐
                  │ Cola de espera      │
                  └──────────┬──────────┘
                             ↓
                  ┌─────────────────────┐
                  │ Tallerista disponible│
                  └──────────┬──────────┘
                             ↓
                  ┌─────────────────────┐
                  │ Vehículo en trabajo │
                  └──────────┬──────────┘
                             ↓
                  ┌─────────────────────┐
                  │ Reparaciones        │
                  │ pendientes (Pila)   │
                  └──────────┬──────────┘
                             ↓
                    ¿Necesita repuestos?
                       /            \
                     Sí              No
                     ↓                ↓
          ┌─────────────────┐   ┌───────────────┐
          │ Espera repuestos│   │ Finalización  │
          │     (Cola)      │   └───────┬───────┘
          └────────┬────────┘           ↓
                   ↓             ┌───────────────┐
             Llegan repuestos    │ Pronto para   │
                   ↓             │    retirar    │
          ┌─────────────────┐    └───────────────┘
          │ Vuelve al       │
          │ trabajo         │
          └─────────────────┘
```

---

# 6. Operaciones principales

El sistema implementa, entre otras, las siguientes operaciones.

### Registrar vehículo por mantenimiento planificado

```java
registrarMantenimientoPlanificado(
    Vehiculo vehiculo,
    String descripcionMantenimiento
)
```

Registra el tipo de ingreso, crea el mantenimiento como reparación pendiente y agrega el vehículo al final de la cola de espera. Tanto `Pila.mete()` como `Cola.poneEnCola()` son O(1).

Complejidad: **O(1)**.

### Registrar vehículo por problema informado

```java
registrarProblemaInformado(
    Vehiculo vehiculo,
    String problemaInformado
)
```

Conserva el problema descrito por el dueño y agrega el vehículo a la cola sin crear una reparación ficticia. Una vez diagnosticada la falla, se utiliza `agregarReparacion(...)` para registrar el trabajo necesario.

Complejidad: **O(1)**.

---

### Consultar próximo vehículo

```java
proximoVehiculo()
```

Consulta el vehículo que está primero en la cola sin quitarlo.

Complejidad: **O(1)**.

---

### Registrar tallerista

```java
registrarTallerista(Tallerista tallerista)
```

Agrega un nuevo tallerista a la lista.

Complejidad: **O(n)** debido a la implementación de `Lista.agregar()`.

---

### Buscar tallerista disponible

```java
buscarTalleristaDisponible()
```

Recorre los talleristas hasta encontrar el primero disponible.

Complejidad: **O(n)**.

---

### Atender siguiente vehículo

```java
atenderSiguiente()
```

Toma el primer vehículo de la cola, busca un tallerista disponible y asigna el vehículo.

La operación depende de la búsqueda del tallerista y de la inserción en `vehiculosEnTrabajo`.

Complejidad: **O(n)**.

---

### Agregar reparación

```java
agregarReparacion(Vehiculo vehiculo, Reparacion reparacion)
```

Agrega la reparación a la pila de reparaciones pendientes.

Complejidad: **O(1)**.

---

### Realizar próxima reparación

```java
realizarProximaReparacion(Vehiculo vehiculo)
```

Saca la reparación que está en el tope de la pila y la agrega al historial del vehículo.

La operación tiene costo **O(n)** en nuestra implementación debido a `Lista.agregar()` del historial.

---

### Enviar vehículo a espera de repuestos

```java
esperarRepuestos(Vehiculo vehiculo)
```

Quita el vehículo de los vehículos en trabajo, lo agrega a la cola de repuestos y libera al tallerista correspondiente.

La búsqueda para liberar al tallerista recorre la lista de talleristas.

Complejidad: **O(n)**.

---

### Continuar trabajo con repuestos

```java
continuarConRepuestos()
```

Toma el primer vehículo de la cola de repuestos, busca un tallerista disponible y lo devuelve al trabajo.

Complejidad: **O(n)** por la búsqueda del tallerista.

---

### Finalizar vehículo

```java
finalizarVehiculo(Vehiculo vehiculo)
```

Quita el vehículo de los vehículos en trabajo, libera al tallerista y agrega el vehículo a la lista de vehículos prontos.

Complejidad: **O(n)**.

---

# 7. Consultas implementadas y justificación

La consigna exige al menos cinco operaciones/consultas relevantes que hagan uso real de las estructuras desarrolladas. Las cinco consultas principales elegidas son:

| Consulta | Estructura utilizada | Por qué es útil | Complejidad |
| --- | --- | --- | --- |
| `proximoVehiculo()` | `Cola<Vehiculo>` | Permite saber qué vehículo corresponde atender sin alterar el orden de llegada. | O(1) |
| `buscarTalleristaDisponible()` | `Lista<Tallerista>` | Permite asignar trabajo al primer tallerista libre recorriendo la estructura. | O(t), donde t es la cantidad de talleristas |
| `proximoEsperandoRepuestos()` | `Cola<Vehiculo>` | Permite saber qué vehículo debe retomar el trabajo primero cuando llegan repuestos. | O(1) |
| `proximaReparacion(Vehiculo)` | `Pila<Reparacion>` | Determina el próximo trabajo respetando LIFO, de modo que una falla adicional quede por encima del problema original. | O(1) |
| `estaProntoParaRetirar(Vehiculo)` | `Lista<Vehiculo>` | Verifica si el vehículo ya está dentro del conjunto operativo de vehículos finalizados. | O(p), donde p es la cantidad de vehículos prontos |

Además existen consultas auxiliares de cantidad (`cantidadVehiculosEnEspera`, `cantidadVehiculosEnTrabajo`, `cantidadEsperandoRepuestos`, `cantidadTalleristas` y `cantidadVehiculosProntos`) y consultas del historial de reparaciones. Estas se mantienen como apoyo al funcionamiento y a la demostración, pero no son las cinco consultas principales utilizadas para justificar el requerimiento.

---

# 8. Casos de prueba

El sistema cuenta con pruebas específicas para el funcionamiento del taller en:

```text
src/test/java/ucu/edu/aed/tda/Taller/TallerTest.java
```

Entre los casos probados se encuentran:

* creación de un taller vacío;
* registro de vehículos por **mantenimiento planificado**;
* registro de vehículos por **problema informado por el dueño**;
* comprobación de que un problema informado no crea una reparación antes del diagnóstico;
* rechazo del segundo ingreso del mismo vehículo;
* respeto del orden de llegada incluso mezclando ambos tipos de ingreso;
* consulta del próximo vehículo;
* registro de talleristas;
* búsqueda de talleristas disponibles;
* asignación de vehículos;
* comportamiento cuando no hay talleristas disponibles;
* registro de reparaciones;
* prioridad de fallas adicionales mediante una pila;
* historial de reparaciones realizadas;
* espera por repuestos;
* continuación del trabajo después de recibir repuestos;
* finalización de vehículos;
* rechazo de la finalización mientras existan reparaciones pendientes;
* rechazo de enviar a repuestos un vehículo que no está en trabajo;
* liberación de talleristas;
* flujo completo de un vehículo.

Las pruebas del taller se ejecutan junto con las pruebas de las estructuras del Desafío 1.

Para ejecutarlas:

```bash
mvn clean test
```

Durante la revisión se recompilaron las clases principales y se ejecutaron las pruebas del paquete de estructuras y del taller sin fallos. El comando estándar para repetir la validación en el entorno Maven del equipo es `mvn clean test`.

---

# 9. Demostración

El proyecto incluye una clase:

```text
src/main/java/ucu/edu/aed/Main.java
```

Esta clase realiza una demostración del flujo del sistema.

La demostración incluye:

1. Registro de talleristas.
2. Registro de vehículos eligiendo entre mantenimiento planificado y problema informado.
3. Conservación del motivo de ingreso.
4. Atención de vehículos respetando el orden de llegada.
5. Registro de la reparación diagnosticada o uso del mantenimiento ya planificado.
6. Registro de una falla adicional.
7. Ejecución de la falla adicional antes de la original.
8. Registro del historial de reparaciones.
9. Envío de un vehículo a espera de repuestos.
10. Liberación del tallerista.
11. Llegada de los repuestos.
12. Continuación del trabajo.
13. Finalización del vehículo.
14. Consulta del estado final del taller.

Para compilar:

```bash
mvn clean compile
```

Para ejecutar directamente la demostración después de compilar:

```bash
java -cp target\classes ucu.edu.aed.Main
```

---

# 10. Desafío 3 — Optimización

## Operación identificada

La operación elegida fue `Lista.agregar(elemento)`. La lista simplemente enlazada original conserva únicamente `primero`; por eso, para insertar al final debe recorrer todos los nodos existentes. Una inserción al final cuesta **O(n)** y construir una lista de n elementos mediante n inserciones sucesivas cuesta **O(n²)**.

## Alternativa propuesta

Se implementó una clase separada `ListaOptimizada<T>` que conserva:

```text
primero
ultimo
tamaño
```

Al mantener `ultimo`, `agregar(elemento)` enlaza el nuevo nodo directamente al final y actualiza la referencia, por lo que la inserción pasa a **O(1)**. Construir n elementos pasa a **O(n)**. La optimización de esta operación se mantiene en `ListaOptimizada`, una clase separada, para poder comparar claramente ambas representaciones.

## Comparación experimental

`BenchmarkLista` ejecuta inserciones sucesivas en ambas implementaciones, realiza un calentamiento previo de la JVM y promedia diez repeticiones. En una ejecución registrada por el equipo se obtuvieron los siguientes valores:

| Elementos | `Lista` promedio (µs) | `ListaOptimizada` promedio (µs) | Mejora aproximada |
| ---: | ---: | ---: | ---: |
| 1.000 | 1.506,40 | 39,90 | 37,7x |
| 5.000 | 64.972,30 | 203,70 | 318,9x |
| 10.000 | 206.185,10 | 804,10 | 256,3x |
| 25.000 | 1.753.136,50 | 1.556,70 | 1.126x |
| 50.000 | 6.626.318,30 | 317,70 | 20.857x |
| 100.000 | 30.216.326,20 | 28.410,60 | 1.063,5x |

Los tiempos concretos pueden variar por JIT, garbage collection y carga del equipo; por eso la conclusión principal no depende de un cociente puntual sino del crecimiento observado y del análisis asintótico: **O(n²) frente a O(n)** para construir la lista mediante inserciones al final.

---

# 11. Registro del uso de herramientas de IA

Se utilizaron herramientas de IA generativa como apoyo durante el desarrollo para:

* revisar implementaciones y detectar casos borde;
* discutir decisiones de representación y complejidad;
* proponer y revisar casos de prueba;
* detectar requisitos del escenario que no estaban representados explícitamente;
* mejorar documentación y preparación para la defensa.

El código y las decisiones finales fueron revisados por el equipo. El uso de IA se tomó como apoyo al proceso de desarrollo y no como sustituto de la comprensión de las estructuras, sus invariantes, sus costos y las modificaciones realizadas.

---

# 12. Organización del proyecto

La estructura principal es:

```text
src/
├── main/
│   └── java/
│       └── ucu/
│           └── edu/
│               └── aed/
│                   ├── Taller/
│                   │   ├── Reparacion.java
│                   │   ├── Taller.java
│                   │   ├── Tallerista.java
│                   │   ├── TipoIngreso.java
│                   │   └── Vehiculo.java
│                   │
│                   ├── tda/
│                   │   ├── Cola.java
│                   │   ├── ColaPrioridad.java
│                   │   ├── Conjunto.java
│                   │   ├── ElementoPrioridad.java
│                   │   ├── Lista.java
│                   │   ├── ListaArreglo.java
│                   │   ├── ListaCircular.java
│                   │   ├── ListaCircularDoble.java
│                   │   ├── ListaDoble.java
│                   │   ├── ListaOptimizada.java
│                   │   ├── Pila.java
│                   │   ├── PilaPrioridad.java
│                   │   ├── TDACola.java
│                   │   ├── TDAConjunto.java
│                   │   ├── TDALista.java
│                   │   └── TDAPila.java
│                   │
│                   └── utils/
│                       └── FileUtils.java
│
└── test/
    └── java/
        └── ucu/
            └── edu/
                └── aed/
                    └── tda/
                        └── Taller/
                            └── TallerTest.java
```

---

# 13. Resumen de decisiones

| Necesidad                     | Estructura / representación | Motivo                                             |
| ----------------------------- | -------------------------- | -------------------------------------------------- |
| Motivo de ingreso             | `TipoIngreso` + detalle    | Distingue mantenimiento planificado de problema informado |
| Vehículos esperando atención  | `Cola<Vehiculo>`           | Respeta el orden de llegada                        |
| Vehículos esperando repuestos | `Cola<Vehiculo>`    | Mantiene el orden de espera                             |
| Reparaciones pendientes       | `Pila<Reparacion>`  | Las fallas nuevas deben atenderse primero               |
| Historial de reparaciones     | `Lista<Reparacion>` | Permite conservar y consultar los trabajos realizados   |
| Talleristas                   | `Lista<Tallerista>` | Permite registrar y buscar disponibilidad               |
| Vehículos en trabajo          | `Lista<Vehiculo>`   | Permite administrar los vehículos actualmente asignados |
| Vehículos prontos             | `Lista<Vehiculo>`   | Mantiene los vehículos que terminaron                   |

La elección de las estructuras se basa en el comportamiento requerido por el escenario y no solamente en almacenar los datos.

El objetivo principal del diseño es que las operaciones del taller hagan uso real de los TDA desarrollados en el Desafío 1.
