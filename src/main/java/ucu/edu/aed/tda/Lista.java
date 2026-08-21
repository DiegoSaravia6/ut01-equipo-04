package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class Lista<T> implements TDALista<T> {

    private class Nodo {
        private T dato;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo primero;
    private int tamaño;

    public Lista() {
        primero = null;
        tamaño = 0;
    }

    @Override
    public void agregar(T elem) {
        Nodo nuevo = new Nodo(elem);

        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo actual = primero;

            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }

            actual.siguiente = nuevo;
        }

        tamaño++;
    }

    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }

        Nodo nuevo = new Nodo(elem);

        if (index == 0) {
            nuevo.siguiente = primero;
            primero = nuevo;
        } else {
            Nodo actual = primero;

            for (int i = 0; i < index - 1; i++) {
                actual = actual.siguiente;
            }

            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }

        tamaño++;
    }

    @Override
    public T obtener(int index) {
        validarIndice(index);

        Nodo actual = primero;

        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }

        return actual.dato;
    }

    @Override
    public T remover(int index) {
        validarIndice(index);

        Nodo eliminado;

        if (index == 0) {
            eliminado = primero;
            primero = primero.siguiente;
        } else {
            Nodo anterior = primero;

            for (int i = 0; i < index - 1; i++) {
                anterior = anterior.siguiente;
            }

            eliminado = anterior.siguiente;
            anterior.siguiente = eliminado.siguiente;
        }

        tamaño--;

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {
        Nodo actual = primero;
        Nodo anterior = null;

        while (actual != null) {

            if (Objects.equals(actual.dato, elem)) {

                if (anterior == null) {
                    primero = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }

                tamaño--;
                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem) {
        Nodo actual = primero;
        int indice = 0;

        while (actual != null) {

            if (Objects.equals(actual.dato, elem)) {
                return indice;
            }

            actual = actual.siguiente;
            indice++;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        Nodo actual = primero;

        while (actual != null) {

            if (criterio.test(actual.dato)) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Lista<T> resultado = new Lista<>();

        // Copiamos los elementos para no modificar la lista original.
        Nodo actual = primero;

        while (actual != null) {
            resultado.agregar(actual.dato);
            actual = actual.siguiente;
        }

        // Ordenamos directamente sobre los nodos de la copia.
        resultado.primero = mergeSort(
                resultado.primero,
                resultado.tamaño,
                comparator
        );

        return resultado;
    }

    /**
     * Ordena una lista enlazada utilizando Merge Sort.
     *
     * @param cabeza primer nodo de la lista
     * @param cantidad cantidad de elementos
     * @param comparator criterio de comparación
     * @return primer nodo de la lista ordenada
     */
    private Nodo mergeSort(
            Nodo cabeza,
            int cantidad,
            Comparator<T> comparator) {

        // Caso base: lista vacía o con un solo elemento.
        if (cabeza == null || cabeza.siguiente == null) {
            return cabeza;
        }

        int mitad = cantidad / 2;

        // Buscamos el comienzo de la segunda mitad.
        Nodo derecha = cabeza;

        for (int i = 0; i < mitad; i++) {
            derecha = derecha.siguiente;
        }

        // Buscamos el último nodo de la primera mitad.
        Nodo anterior = cabeza;

        for (int i = 1; i < mitad; i++) {
            anterior = anterior.siguiente;
        }

        // Separamos las dos mitades.
        anterior.siguiente = null;

        // Ordenamos recursivamente cada mitad.
        Nodo izquierdaOrdenada = mergeSort(
                cabeza,
                mitad,
                comparator
        );

        Nodo derechaOrdenada = mergeSort(
                derecha,
                cantidad - mitad,
                comparator
        );

        // Combinamos ambas mitades ordenadas.
        return mezclarNodos(
                izquierdaOrdenada,
                derechaOrdenada,
                comparator
        );
    }

    /**
     * Combina dos listas enlazadas ya ordenadas.
     */
    private Nodo mezclarNodos(
            Nodo izquierda,
            Nodo derecha,
            Comparator<T> comparator) {

        Nodo cabeza = null;
        Nodo ultimo = null;

        while (izquierda != null && derecha != null) {

            Nodo elegido;

            if (comparator.compare(
                    izquierda.dato,
                    derecha.dato
            ) <= 0) {

                elegido = izquierda;
                izquierda = izquierda.siguiente;

            } else {

                elegido = derecha;
                derecha = derecha.siguiente;
            }

            if (cabeza == null) {
                cabeza = elegido;
                ultimo = elegido;
            } else {
                ultimo.siguiente = elegido;
                ultimo = elegido;
            }
        }

        // Agregamos la parte restante.
        if (izquierda != null) {
            ultimo.siguiente = izquierda;
        } else {
            ultimo.siguiente = derecha;
        }

        return cabeza;
    }

    @Override
    public int tamaño() {
        return tamaño;
    }

    @Override
    public boolean esVacio() {
        return tamaño == 0;
    }

    @Override
    public void vaciar() {
        primero = null;
        tamaño = 0;
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }
    }
}