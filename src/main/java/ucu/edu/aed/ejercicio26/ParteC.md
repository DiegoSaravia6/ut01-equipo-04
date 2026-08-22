PARTE C — COMPARACIÓN DE IMPLEMENTACIONES

1. Tiempo de ejecución

Ambas implementaciones presentan un orden de tiempo de ejecución O(n), donde n es la cantidad de caracteres de la expresión. En ambos casos se recorre la lista una sola vez y las operaciones utilizadas sobre la pila tienen costo O(1).

2. Consumo de memoria

Ambas implementaciones presentan un consumo de memoria auxiliar O(n) en el peor caso, ya que la pila puede almacenar hasta n caracteres de apertura. El consumo real puede variar según los detalles internos de cada implementación, por lo que no se puede afirmar que Stack consuma necesariamente menos memoria.

3. Mantenibilidad

La Pila propia permite controlar y modificar su implementación, pero requiere que el equipo mantenga y pruebe el código.

Stack permite reutilizar una implementación proporcionada por Java, reduciendo la cantidad de código propio y facilitando el mantenimiento, aunque limita el control sobre la implementación interna.

4. Control sobre la estructura interna

La Pila propia brinda control completo sobre cómo se almacenan y manipulan los elementos. En cambio, al utilizar Stack se trabaja mediante la interfaz proporcionada por Java y no se controla directamente su implementación interna.

Conclusión

Ambas implementaciones resuelven el problema con la misma complejidad asintótica de tiempo y memoria. La principal diferencia se encuentra en el nivel de control y mantenimiento: una implementación propia ofrece mayor control y permite adaptar la estructura, mientras que Stack reduce el código que debe desarrollar y mantener el equipo.