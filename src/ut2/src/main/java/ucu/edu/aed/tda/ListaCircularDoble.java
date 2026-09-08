package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaCircularDoble<T> implements TDALista<T> {

    private class Nodo {
        private T dato;
        private Nodo anterior;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.anterior = null;
            this.siguiente = null;
        }
    }

    private Nodo primero;
    private Nodo ultimo;
    private int tamaño;

    public ListaCircularDoble() {
        primero = null;
        ultimo = null;
        tamaño = 0;
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    @Override
    public void agregar(T elem) {
        Nodo nuevo = new Nodo(elem);

        if (esVacio()) {
            primero = nuevo;
            ultimo = nuevo;

            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;

        } else {
            nuevo.anterior = ultimo;
            nuevo.siguiente = primero;

            ultimo.siguiente = nuevo;
            primero.anterior = nuevo;

            ultimo = nuevo;
        }

        tamaño++;
    }

    @Override
    public void agregar(int index, T elem) {

        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }

        // Agregar al final
        if (index == tamaño) {
            agregar(elem);
            return;
        }

        Nodo actual = nodoEnIndice(index);
        Nodo nuevo = new Nodo(elem);

        nuevo.anterior = actual.anterior;
        nuevo.siguiente = actual;

        actual.anterior.siguiente = nuevo;
        actual.anterior = nuevo;

        if (index == 0) {
            primero = nuevo;
        }

        tamaño++;
    }

    // =========================================================
    // OBTENER
    // =========================================================

    @Override
    public T obtener(int index) {
        return nodoEnIndice(index).dato;
    }

    // =========================================================
    // REMOVER
    // =========================================================

    @Override
    public T remover(int index) {

        Nodo eliminado = nodoEnIndice(index);

        // Único elemento
        if (tamaño == 1) {
            T dato = eliminado.dato;

            primero = null;
            ultimo = null;
            tamaño = 0;

            eliminado.anterior = null;
            eliminado.siguiente = null;

            return dato;
        }

        eliminado.anterior.siguiente = eliminado.siguiente;
        eliminado.siguiente.anterior = eliminado.anterior;

        if (eliminado == primero) {
            primero = eliminado.siguiente;
        }

        if (eliminado == ultimo) {
            ultimo = eliminado.anterior;
        }

        tamaño--;

        eliminado.anterior = null;
        eliminado.siguiente = null;

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {

        if (esVacio()) {
            return false;
        }

        Nodo actual = primero;

        for (int i = 0; i < tamaño; i++) {

            if (Objects.equals(actual.dato, elem)) {

                // Único elemento
                if (tamaño == 1) {
                    primero = null;
                    ultimo = null;
                    tamaño = 0;

                    return true;
                }

                actual.anterior.siguiente = actual.siguiente;
                actual.siguiente.anterior = actual.anterior;

                if (actual == primero) {
                    primero = actual.siguiente;
                }

                if (actual == ultimo) {
                    ultimo = actual.anterior;
                }

                tamaño--;

                return true;
            }

            actual = actual.siguiente;
        }

        return false;
    }

    // =========================================================
    // BUSQUEDA
    // =========================================================

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem) {

        Nodo actual = primero;

        for (int i = 0; i < tamaño; i++) {

            if (Objects.equals(actual.dato, elem)) {
                return i;
            }

            actual = actual.siguiente;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {

        Nodo actual = primero;

        for (int i = 0; i < tamaño; i++) {

            if (criterio.test(actual.dato)) {
                return actual.dato;
            }

            actual = actual.siguiente;
        }

        return null;
    }

    // =========================================================
    // ORDENAR
    // =========================================================

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {

        Lista<T> lista = new Lista<>();

        Nodo actual = primero;

        for (int i = 0; i < tamaño; i++) {
            lista.agregar(actual.dato);
            actual = actual.siguiente;
        }

        return lista.ordenar(comparator);
    }

    // =========================================================
    // INFORMACION
    // =========================================================

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
        ultimo = null;
        tamaño = 0;
    }

    // =========================================================
    // BUSCAR NODO
    // =========================================================

    private Nodo nodoEnIndice(int index) {

        validarIndice(index);

        /*
         * Al ser doblemente enlazada podemos elegir
         * el extremo más cercano.
         */

        if (index < tamaño / 2) {

            Nodo actual = primero;

            for (int i = 0; i < index; i++) {
                actual = actual.siguiente;
            }

            return actual;

        } else {

            Nodo actual = ultimo;

            for (int i = tamaño - 1; i > index; i--) {
                actual = actual.anterior;
            }

            return actual;
        }
    }

    private void validarIndice(int index) {

        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }
    }
}