# Proyecto Integrador 1 — Segundo Hito (Equipo 04)

Este directorio `ut2/` es un proyecto Maven independiente dentro del mismo repositorio. De esta forma el Hito 1 puede conservarse intacto en la raíz del repo y el Hito 2 parte de una copia de las estructuras y del taller ya entregados, extendiéndolos sin incluir ni modificar los ejercicios ajenos al proyecto.

## Ejecutar

```bash
cd ut2
mvn clean test
mvn exec:java -Dexec.mainClass="ucu.edu.aed.Main"
```

Si no se usa Maven, `ucu.edu.aed.Main` puede ejecutarse directamente desde VS Code/Java y los tests desde el panel de Testing.

## Qué se conserva del Hito 1

Se conservan las implementaciones propias de listas, pila, cola, conjunto y prioridad, junto con las clases `Taller`, `Vehiculo`, `Tallerista`, `Reparacion` y `TipoIngreso`. Los tests del Hito 1 también se incluyen para detectar regresiones.

Las funcionalidades previas continúan disponibles. Cuando un vehículo no tiene fecha de entrega comprometida, el nuevo orden de atención mantiene FIFO mediante un número de llegada, reproduciendo la regla anterior.

## Desafío 1 — Caja de herramientas jerárquica

Se incorporan:

- `ArbolBinario<T>`: árbol binario general, inserción por niveles.
- `ArbolBinarioBusqueda<T>` + `ElementoABB<T>`: ABB que implementa las interfaces entregadas `TDAArbolBinario` y `TDAElemento`.
- `ArbolAVL<T>`: ABB balanceado mediante rotaciones AVL.
- `ArbolGeneral<T>` + `ElementoArbolGeneral<T>`: árbol n-ario de profundidad variable.
- `MonticuloBinario<T>`: montículo binario mínimo utilizado como cola de prioridad.
- Recorridos preorden, inorden, postorden y por niveles. El recorrido por niveles utiliza la `Cola` implementada en el Hito 1.

### Invariantes principales

**ABB:** todo elemento del subárbol izquierdo es menor que el nodo y todo elemento del derecho es mayor; no se insertan duplicados.

**AVL:** mantiene el invariante del ABB y además la diferencia de altura entre subárboles de todo nodo es como máximo 1 en valor absoluto.

**Árbol general:** la raíz no tiene padre; todo otro nodo tiene exactamente un padre; cada nodo puede tener cualquier cantidad de hijos; los hijos se mantienen en una estructura propia (`ListaArreglo`).

**Montículo:** el padre nunca tiene menor prioridad que un hijo según el `Comparator`; al representarse sobre arreglo, el árbol se mantiene completo.

### Complejidad de las nuevas estructuras

| Operación | ABB | AVL | Árbol general | Montículo |
|---|---:|---:|---:|---:|
| Buscar | O(h), peor O(n) | O(log n) | O(n) | O(n) si se busca por valor |
| Insertar | O(h), peor O(n) | O(log n) | O(n) para localizar padre / O(1) con referencia | O(log n) |
| Eliminar | O(h), peor O(n) | O(log n) | O(n) + tamaño del subárbol | O(log n) raíz; O(n + log n) por valor |
| Ver primero | — | — | — | O(1) |
| Recorrido completo | O(n) | O(n) | O(n) | O(n) para inspeccionar todos |
| Espacio auxiliar recursivo | O(h) | O(log n) | O(h) | O(1) en reordenamiento |

`h` es la altura. La diferencia esencial es que un ABB puede degenerarse hasta `h = n`, mientras un AVL mantiene `h = O(log n)`.

El test `ComparacionBalanceTest` inserta 1..1000 en ambos: el ABB queda con altura 999, mientras el AVL permanece por debajo de 20.

## Desafío 2 — Taller Mecánico jerárquico

### 1. Estructura del vehículo

Cada `Vehiculo` contiene un `ArbolGeneral<ParteVehiculo>`. La raíz técnica representa el vehículo completo y pueden agregarse tantos niveles como sean necesarios.

Ejemplo:

```text
Vehículo
├── Motor
│   └── Distribución
│       └── Correa
│           └── Tensor
└── Tren delantero
    └── Frenos delanteros
```

No existe una profundidad fija. Cada `Trabajo` referencia una `ParteVehiculo` concreta.

### 2. Orden de trabajo ramificada

`OrdenTrabajo` contiene un `ArbolGeneral<Trabajo>` con una raíz técnica de costo 0. Sus hijos son trabajos principales. Los hijos de un trabajo son fallas o trabajos que se desprendieron de él.

Esto conserva explícitamente la relación “qué trabajo originó cuál”, cosa que la pila lineal del Hito 1 no podía representar.

### 3. Cierre y orden de ejecución

Un trabajo solamente puede finalizar cuando sus hijos están cerrados (`TERMINADO` o `RECHAZADO`). Por transitividad, esto garantiza que todos los descendientes estén resueltos antes de cerrar un trabajo padre.

La consulta de orden pendiente utiliza **postorden**, porque los hijos deben resolverse antes que el padre. Entre trabajos hermanos se usa **LIFO**: el último problema detectado se procesa primero. Esta elección conserva la regla de la pila de reparaciones del Hito 1.

### 4. Espera de repuestos

Suspender un trabajo marca ese nodo y los trabajos que se desprenden de él como `ESPERANDO_REPUESTO` sin bloquear ramas independientes.

Si todavía existe otra rama ejecutable, el vehículo continúa en trabajo. Si no queda ninguna rama ejecutable, el vehículo pasa a la cola de espera de repuestos y se libera el tallerista.

Gracias al puntero al padre de `ElementoArbolGeneral`, se pueden consultar los trabajos ancestros bloqueados recorriendo solamente el camino hacia la raíz: O(h).

### 5. Presupuesto y aprobación

Cada trabajo registra costo y estado de aprobación.

Política elegida para rechazo: **un trabajo rechazado cierra esa rama sin ejecutarla**. El trabajo y sus descendientes quedan `RECHAZADO`; dejan de bloquear al padre y no se incluyen en el costo autorizado. Se conserva el costo propuesto para poder explicar qué se presupuestó.

Se puede consultar:

- costo propuesto de la orden completa;
- costo autorizado de la orden completa;
- costo propuesto de cualquier subárbol/rama.

### 6. Búsqueda por matrícula e índice de fechas

`Taller` mantiene:

- `ArbolAVL<Vehiculo> indiceVehiculosPorPatente`;
- `ArbolAVL<RegistroOrdenFecha> indiceOrdenesPorFecha`.

Buscar una patente cuesta O(log n). Consultar órdenes entre dos fechas utiliza poda del AVL y cuesta O(log n + k), donde `k` es la cantidad de resultados.

### 7. Orden de atención

Los vehículos en espera se almacenan en `MonticuloBinario<Vehiculo>`.

Criterio elegido:

1. un vehículo con fecha comprometida tiene prioridad sobre uno sin fecha;
2. entre vehículos con compromiso, va primero la fecha más cercana;
3. ante empate, se mantiene FIFO por orden de llegada;
4. entre vehículos sin fecha, se mantiene FIFO.

`proximoVehiculo()` es O(1) y `atenderSiguiente()` O(log n).

La reprogramación modifica la fecha y restaura el heap. En esta implementación localizar la posición dentro del heap cuesta O(n) y la reparación del heap O(log n). Se dejó así deliberadamente para mantener la estructura simple y poder justificar una posible optimización futura con un índice de posiciones.

## Cinco operaciones/consultas relevantes

1. **Buscar vehículo por patente** — AVL, O(log n). En el Hito 1 requería recorrer la lista O(n).
2. **Consultar órdenes por rango de fechas** — AVL, O(log n + k). En el Hito 1 requeriría recorrer todos los registros O(n).
3. **Consultar el próximo vehículo según compromiso** — heap, O(1). Con la representación lineal anterior sería necesario inspeccionar los vehículos en espera para encontrar el compromiso más cercano, O(n).
4. **Obtener el orden de trabajos pendientes** — postorden del árbol de trabajos, O(k) sobre la orden, respetando dependencias.
5. **Informar qué trabajos quedan bloqueados por una espera** — recorrido de ancestros O(h), sin recorrer la orden completa.

También se incluyen consultas de costo total y costo de una rama, cuyo costo depende únicamente del subárbol consultado.

## Desafío 3 — Optimización y comparación experimental

### Operación elegida

Búsqueda de vehículo por patente.

En el Hito 1, la aplicación mantenía vehículos en una `Lista` y utilizaba `buscar`, por lo que una búsqueda en peor caso era O(n). En el Hito 2 se agrega un índice AVL por matrícula: O(log n) incluso en el peor caso estructural del índice.

### Metodología

Clase: `ucu.edu.aed.BenchmarkBusquedaVehiculos`.

- tamaños: 1.000, 5.000, 10.000, 25.000, 50.000 y 100.000 vehículos;
- peor caso de la lista: matrícula ubicada al final;
- 100 búsquedas de calentamiento;
- 7 rondas por tamaño;
- 200 búsquedas por ronda;
- se informa la mediana de las rondas;
- el armado de las estructuras queda fuera de la medición;
- se utiliza `System.nanoTime()`.

Resultados obtenidos en una ejecución de referencia (deben esperarse variaciones por CPU/JVM/JIT):

| n | Lista, µs | AVL, µs | Relación observada |
|---:|---:|---:|---:|
| 1.000 | 25.245 | 0.543 | 46.51x |
| 5.000 | 41.383 | 0.254 | 162.97x |
| 10.000 | 293.155 | 0.159 | 1848.86x |
| 25.000 | 616.594 | 0.145 | 4251.64x |
| 50.000 | 986.294 | 0.143 | 6883.44x |
| 100.000 | 1633.565 | 0.104 | 15638.93x |

La magnitud exacta del factor no es la conclusión principal: lo importante es la tendencia coherente con el análisis asintótico **O(n) vs O(log n)**. Los números pequeños del AVL hacen que el cociente sea especialmente sensible al entorno de ejecución.

## Testing

Se incluyen los tests del Hito 1 y nuevos tests de:

- árbol vacío y de un nodo;
- inserción, búsqueda y recorridos;
- borrado de hoja, nodo con un hijo, nodo con dos hijos y raíz en ABB;
- rotaciones AVL LL, RR, LR y RL;
- árbol degenerado vs AVL balanceado;
- árbol general de profundidad variable;
- eliminación de subárbol y ancestros;
- heap, crecimiento y reprogramación de prioridad;
- estructura de partes del vehículo;
- trabajos con múltiples niveles de derivación;
- cierre bloqueado por descendientes;
- orden LIFO entre hermanos;
- aprobación y rechazo parcial;
- suspensión de una rama con otra rama disponible;
- suspensión que libera tallerista cuando no queda trabajo ejecutable;
- búsqueda por patente;
- rango de fechas;
- prioridad por fecha comprometida y FIFO sin fecha;
- reprogramación de un vehículo ya ingresado.

En la validación realizada durante el desarrollo, **255 tests pasaron y 0 fallaron** usando un runner local compatible con los tests JUnit 3 del proyecto. Antes de entregar conviene ejecutar también `mvn clean test` en la máquina del equipo.

## Uso de IA

Se utilizó IA generativa como apoyo para:

- revisar los requerimientos del enunciado;
- detectar requisitos no cubiertos por el Hito 1;
- proponer representaciones jerárquicas;
- revisar complejidades e invariantes;
- generar casos de prueba adicionales;
- revisar compatibilidad con el Hito 1 y documentación.

El código, las decisiones de diseño, las complejidades, los recorridos, las rotaciones y las modificaciones deben ser comprendidos y defendidos por todos los integrantes del equipo.
