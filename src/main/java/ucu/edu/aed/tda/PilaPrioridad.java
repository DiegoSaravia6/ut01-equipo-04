package ucu.edu.aed.tda;

import java.util.NoSuchElementException;

/**
 * Pila con prioridad.
 *
 * <p>Los elementos con menor número de prioridad son retirados primero.
 * Si dos elementos tienen la misma prioridad, se respeta el orden LIFO.</p>
 *
 * @param <T> tipo de los elementos almacenados
 */
public class PilaPrioridad<T> {

    private Lista<ElementoPrioridad<T>> elementos;

    public PilaPrioridad() {
        elementos = new Lista<>();
    }

    /**
     * Inserta un elemento en la pila.
     *
     * @param dato elemento a insertar
     * @param prioridad prioridad del elemento
     */
    public void mete(T dato, int prioridad) {

        ElementoPrioridad<T> nuevo =
                new ElementoPrioridad<>(dato, prioridad);

        int posicion = 0;

        // Avanzamos mientras la prioridad existente sea mayor
        // o igual. Al insertar delante de los iguales,
        // conseguimos comportamiento LIFO en los empates.
        while (posicion < elementos.tamaño()
                && elementos.obtener(posicion).getPrioridad() < prioridad) {

            posicion++;
        }

        elementos.agregar(posicion, nuevo);
    }

    /**
     * Retorna el elemento de mayor prioridad sin removerlo.
     *
     * @return elemento de mayor prioridad
     */
    public T tope() {

        if (elementos.esVacio()) {
            throw new NoSuchElementException(
                    "La pila de prioridad está vacía"
            );
        }

        return elementos.obtener(0).getDato();
    }

    /**
     * Remueve y retorna el elemento de mayor prioridad.
     *
     * @return elemento removido
     */
    public T saca() {

        if (elementos.esVacio()) {
            throw new NoSuchElementException(
                    "La pila de prioridad está vacía"
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