package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaCircular<T> implements TDALista<T> {

    private class Nodo {
        private T dato;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo primero;
    private Nodo ultimo;
    private int tamaño;

    public ListaCircular() {
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
            nuevo.siguiente = primero;
        } else {
            nuevo.siguiente = primero;
            ultimo.siguiente = nuevo;
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

        Nodo nuevo = new Nodo(elem);

        // Agregar al principio
        if (index == 0) {
            nuevo.siguiente = primero;
            primero = nuevo;
            ultimo.siguiente = primero;
            tamaño++;
            return;
        }

        Nodo anterior = nodoEnIndice(index - 1);

        nuevo.siguiente = anterior.siguiente;
        anterior.siguiente = nuevo;

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
        validarIndice(index);

        // Único elemento
        if (tamaño == 1) {
            T dato = primero.dato;

            primero = null;
            ultimo = null;
            tamaño = 0;

            return dato;
        }

        // Primero
        if (index == 0) {
            T dato = primero.dato;

            primero = primero.siguiente;
            ultimo.siguiente = primero;

            tamaño--;

            return dato;
        }

        Nodo anterior = nodoEnIndice(index - 1);
        Nodo eliminado = anterior.siguiente;

        anterior.siguiente = eliminado.siguiente;

        // Se eliminó el último
        if (eliminado == ultimo) {
            ultimo = anterior;
            ultimo.siguiente = primero;
        }

        tamaño--;

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {
        if (esVacio()) {
            return false;
        }

        Nodo actual = primero;
        Nodo anterior = ultimo;

        for (int i = 0; i < tamaño; i++) {

            if (Objects.equals(actual.dato, elem)) {

                // Único elemento
                if (tamaño == 1) {
                    primero = null;
                    ultimo = null;
                    tamaño = 0;
                    return true;
                }

                // Eliminar primero
                if (actual == primero) {
                    primero = primero.siguiente;
                    ultimo.siguiente = primero;
                } else {
                    anterior.siguiente = actual.siguiente;

                    // Eliminar último
                    if (actual == ultimo) {
                        ultimo = anterior;
                        ultimo.siguiente = primero;
                    }
                }

                tamaño--;
                return true;
            }

            anterior = actual;
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
    // NODO POR INDICE
    // =========================================================

    private Nodo nodoEnIndice(int index) {
        validarIndice(index);

        Nodo actual = primero;

        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }

        return actual;
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }
    }
}