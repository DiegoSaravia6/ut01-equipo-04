# Correcciones aplicadas

Esta versión incorpora las correcciones revisadas antes de cerrar el diseño. Se mantuvieron deliberadamente simples para que puedan explicarse en una defensa.

1. **Diagnóstico automático para problemas informados**
   - Al registrar un vehículo por un problema informado se crea un `Trabajo` de diagnóstico.
   - Está aprobado automáticamente, cuesta 0 y se asocia al vehículo completo.
   - La orden ya no puede considerarse cerrada si no tiene trabajos.

2. **Costo autorizado corregido**
   - El costo propuesto suma todos los trabajos presupuestados.
   - El costo autorizado suma solamente trabajos con aprobación `APROBADO`.
   - Un trabajo `PENDIENTE` de aprobación no se cuenta como autorizado.

3. **Flujo de estados más claro**
   - Un trabajo normal debe seguir `PENDIENTE -> EN_PROGRESO -> TERMINADO`.
   - `finalizarTrabajo()` ya no permite terminar directamente un trabajo pendiente.

4. **Búsqueda de trabajo ejecutable simplificada**
   - `tieneTrabajoEjecutable()` recorre el árbol una sola vez de forma recursiva.
   - Ya no crea una lista de pendientes para después volver a buscar cada trabajo.
   - Complejidad: O(n) sobre la cantidad de trabajos de la orden.

No se agregó ninguna estructura avanzada extra ni un índice adicional para estas correcciones.


## Ajustes finales v3
- Talleristas identificados por ID único (el nombre deja de ser identificador).
- No se permite rechazar un trabajo que ya está EN_PROGRESO o TERMINADO.
- La reprogramación rechaza fechas comprometidas anteriores a la fecha de ingreso.
- Los vehículos guardan su posición en el montículo mediante `IndexableMonticulo`, evitando el recorrido lineal al reordenar por cambio de prioridad; la reparación del heap queda O(log n).
