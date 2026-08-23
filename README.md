# Herramientas — Algoritmos y Estructuras de Datos

Repositorio correspondiente al trabajo de **Algoritmos y Estructuras de Datos (AED)**.

El proyecto contiene las estructuras lineales desarrolladas para el Desafío 1 y su utilización en el sistema de gestión de un **taller mecánico**, correspondiente al Desafío 2.

---

## 1. Estructuras de datos

Para el Desafío 1 se implementaron diferentes estructuras lineales utilizando nodos enlazados.

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

El flujo de un vehículo puede representarse de la siguiente manera:

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

### Registrar vehículo

```java
registrarVehiculo(Vehiculo vehiculo)
```

Agrega el vehículo al final de la cola de espera.

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

# 7. Consultas implementadas

El sistema permite consultar información útil del taller, entre ella:

```java
cantidadVehiculosEnEspera()
cantidadVehiculosEnTrabajo()
cantidadEsperandoRepuestos()
cantidadTalleristas()
cantidadVehiculosProntos()
estaProntoParaRetirar(Vehiculo vehiculo)
```

Además se pueden consultar:

* el próximo vehículo a atender;
* el próximo vehículo esperando repuestos;
* la próxima reparación pendiente;
* el historial de reparaciones realizadas;
* el tallerista disponible para recibir un vehículo.

Estas consultas no se limitan solamente a mostrar atributos almacenados, sino que utilizan las estructuras implementadas para obtener la información requerida.

---

# 8. Casos de prueba

El sistema cuenta con pruebas específicas para el funcionamiento del taller en:

```text
src/test/java/ucu/edu/aed/tda/Taller/TallerTest.java
```

Entre los casos probados se encuentran:

* creación de un taller vacío;
* registro de vehículos;
* respeto del orden de llegada;
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
* liberación de talleristas;
* flujo completo de un vehículo.

Las pruebas del taller se ejecutan junto con las pruebas de las estructuras del Desafío 1.

Para ejecutarlas:

```bash
mvn clean test
```

El estado actual del proyecto fue verificado con:

```text
BUILD SUCCESS
```

---

# 9. Demostración

El proyecto incluye una clase:

```text
src/main/java/ucu/edu/aed/Main.java
```

Esta clase realiza una demostración del flujo del sistema.

La demostración incluye:

1. Registro de talleristas.
2. Registro de vehículos.
3. Atención de vehículos respetando el orden de llegada.
4. Registro de una reparación original.
5. Registro de una falla adicional.
6. Ejecución de la falla adicional antes de la original.
7. Registro del historial de reparaciones.
8. Envío de un vehículo a espera de repuestos.
9. Liberación del tallerista.
10. Llegada de los repuestos.
11. Continuación del trabajo.
12. Finalización del vehículo.
13. Consulta del estado final del taller.

Para compilar:

```bash
mvn clean compile
```

Para ejecutar directamente la demostración después de compilar:

```bash
java -cp target\classes ucu.edu.aed.Main
```

---

# 10. Organización del proyecto

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

# 11. Resumen de decisiones

| Necesidad                     | Estructura          | Motivo                                                  |
| ----------------------------- | ------------------- | ------------------------------------------------------- |
| Vehículos esperando atención  | `Cola<Vehiculo>`    | Respeta el orden de llegada                             |
| Vehículos esperando repuestos | `Cola<Vehiculo>`    | Mantiene el orden de espera                             |
| Reparaciones pendientes       | `Pila<Reparacion>`  | Las fallas nuevas deben atenderse primero               |
| Historial de reparaciones     | `Lista<Reparacion>` | Permite conservar y consultar los trabajos realizados   |
| Talleristas                   | `Lista<Tallerista>` | Permite registrar y buscar disponibilidad               |
| Vehículos en trabajo          | `Lista<Vehiculo>`   | Permite administrar los vehículos actualmente asignados |
| Vehículos prontos             | `Lista<Vehiculo>`   | Mantiene los vehículos que terminaron                   |

La elección de las estructuras se basa en el comportamiento requerido por el escenario y no solamente en almacenar los datos.

El objetivo principal del diseño es que las operaciones del taller hagan uso real de los TDA desarrollados en el Desafío 1.
