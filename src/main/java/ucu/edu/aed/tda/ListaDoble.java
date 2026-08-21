package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaDoble<T> implements TDALista<T> {

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

    public ListaDoble() {
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
        } else {
            nuevo.anterior = ultimo;
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

        Nodo actual = nodoEnIndice(index);
        Nodo nuevo = new Nodo(elem);

        nuevo.siguiente = actual;
        nuevo.anterior = actual.anterior;

        if (actual.anterior != null) {
            actual.anterior.siguiente = nuevo;
        } else {
            primero = nuevo;
        }

        actual.anterior = nuevo;

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

        desconectar(eliminado);

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {
        Nodo actual = primero;

        while (actual != null) {
            if (Objects.equals(actual.dato, elem)) {
                desconectar(actual);
                return true;
            }

            actual = actual.siguiente;
        }

        return false;
    }

    private void desconectar(Nodo nodo) {

        if (nodo.anterior == null) {
            primero = nodo.siguiente;
        } else {
            nodo.anterior.siguiente = nodo.siguiente;
        }

        if (nodo.siguiente == null) {
            ultimo = nodo.anterior;
        } else {
            nodo.siguiente.anterior = nodo.anterior;
        }

        tamaño--;

        nodo.anterior = null;
        nodo.siguiente = null;
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

    // =========================================================
    // ORDENAR
    // =========================================================

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        Lista<T> lista = new Lista<>();

        Nodo actual = primero;

        while (actual != null) {
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
         * Como la lista es doblemente enlazada,
         * podemos decidir desde qué extremo recorrer.
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