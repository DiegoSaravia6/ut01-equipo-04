package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class Pila<T> implements TDAPila<T> {

    private class Nodo {
        private T dato;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo tope;
    private int tamaño;

    public Pila() {
        tope = null;
        tamaño = 0;
    }

    // =========================================================
    // OPERACIONES PROPIAS DE PILA
    // =========================================================

    @Override
    public T tope() {
        if (tope == null) {
            throw new NoSuchElementException("La pila está vacía");
        }

        return tope.dato;
    }

    @Override
    public T saca() {
        if (tope == null) {
            throw new NoSuchElementException("La pila está vacía");
        }

        T dato = tope.dato;
        tope = tope.siguiente;
        tamaño--;

        return dato;
    }

    @Override
    public void mete(T dato) {
        Nodo nuevo = new Nodo(dato);

        nuevo.siguiente = tope;
        tope = nuevo;

        tamaño++;
    }

    // =========================================================
    // OPERACIONES HEREDADAS DE TDALista
    // =========================================================

    private Nodo nodoEnIndice(int index) {
    validarIndice(index);

    Nodo actual = tope;

    for (int i = 0; i < index; i++) {
        actual = actual.siguiente;
    }

    return actual;
    }
    @Override
    public void agregar(T elem) {
        mete(elem);
    }

    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }

        // Agregar al tope
        if (index == 0) {
            mete(elem);
            return;
        }

        Nodo anterior = nodoEnIndice(index - 1);
        Nodo nuevo = new Nodo(elem);

        nuevo.siguiente = anterior.siguiente;
        anterior.siguiente = nuevo;

        tamaño++;
    }

    @Override
    public T obtener(int index) {
        return nodoEnIndice(index).dato;
    }

    @Override
    public T remover(int index) {
        validarIndice(index);

        // Remover el tope
        if (index == 0) {
            return saca();
        }

        Nodo anterior = nodoEnIndice(index - 1);
        Nodo eliminado = anterior.siguiente;

        anterior.siguiente = eliminado.siguiente;

        tamaño--;

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {
        Nodo actual = tope;
        Nodo anterior = null;

        while (actual != null) {

            if (Objects.equals(actual.dato, elem)) {

                if (anterior == null) {
                    tope = actual.siguiente;
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
        Nodo actual = tope;
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
        Nodo actual = tope;

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
        Lista<T> lista = new Lista<>();

        Nodo actual = tope;

        while (actual != null) {
            lista.agregar(actual.dato);
            actual = actual.siguiente;
        }

        return lista.ordenar(comparator);
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
        tope = null;
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