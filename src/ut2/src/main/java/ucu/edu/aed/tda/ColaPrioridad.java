package ucu.edu.aed.tda;

import java.util.NoSuchElementException;

/**
 * Cola con prioridad.
 *
 * <p>Los elementos con menor número de prioridad son atendidos primero.
 * Si dos elementos tienen la misma prioridad, se respeta el orden de llegada.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class ColaPrioridad<T> {

    private Lista<ElementoPrioridad<T>> elementos;

    public ColaPrioridad() {
        elementos = new Lista<>();
    }

    /**
     * Agrega un elemento manteniendo el orden de prioridad.
     *
     * @param dato elemento a agregar
     * @param prioridad prioridad del elemento
     */
    public void poneEnCola(T dato, int prioridad) {

        ElementoPrioridad<T> nuevo =
                new ElementoPrioridad<>(dato, prioridad);

        int posicion = 0;

        // Buscamos la primera posición con prioridad menor
        // (es decir, más importante).
        while (posicion < elementos.tamaño()
                && elementos.obtener(posicion).getPrioridad() <= prioridad) {

            posicion++;
        }

        elementos.agregar(posicion, nuevo);
    }

    /**
     * Retorna el elemento de mayor prioridad sin removerlo.
     *
     * @return elemento con mayor prioridad
     */
    public T frente() {

        if (elementos.esVacio()) {
            throw new NoSuchElementException(
                    "La cola de prioridad está vacía"
            );
        }

        return elementos.obtener(0).getDato();
    }

    /**
     * Remueve y retorna el elemento de mayor prioridad.
     *
     * @return elemento removido
     */
    public T quitaDeCola() {

        if (elementos.esVacio()) {
            throw new NoSuchElementException(
                    "La cola de prioridad está vacía"
            );
        }

        return elementos.remover(0).getDato();
    }

    public int tamaño() {
        return elementos.tamaño();
    }

    public boolean esVacio() {
        return elementos.esVacio();
    }

    public void vaciar() {
        elementos.vaciar();
    }
}