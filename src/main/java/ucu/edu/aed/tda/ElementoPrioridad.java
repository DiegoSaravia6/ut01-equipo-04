package ucu.edu.aed.tda;

/**
 * Representa un elemento asociado a una prioridad.
 *
 * @param <T> tipo del elemento almacenado
 */
public class ElementoPrioridad<T> {

    private T dato;
    private int prioridad;

    public ElementoPrioridad(T dato, int prioridad) {
        this.dato = dato;
        this.prioridad = prioridad;
    }

    public T getDato() {
        return dato;
    }

    public int getPrioridad() {
        return prioridad;
    }
}