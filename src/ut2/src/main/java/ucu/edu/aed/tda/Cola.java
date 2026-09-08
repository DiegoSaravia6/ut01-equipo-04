package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Cola<T> implements TDACola<T> {

    private class Nodo {
        private T dato;
        private Nodo siguiente;

        public Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int tamaño;

    public Cola() {
        frente = null;
        fin = null;
        tamaño = 0;
    }

    // =========================================================
    // OPERACIONES PROPIAS DE COLA
    // =========================================================

    @Override
    public T frente() {
        if (frente == null) {
            throw new NoSuchElementException("La cola está vacía");
        }

        return frente.dato;
    }

    @Override
    public boolean poneEnCola(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (frente == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }

        tamaño++;
        return true;
    }

    @Override
    public T quitaDeCola() {
        if (frente == null) {
            throw new NoSuchElementException("La cola está vacía");
        }

        T dato = frente.dato;
        frente = frente.siguiente;

        if (frente == null) {
            fin = null;
        }

        tamaño--;

        return dato;
    }

    // =========================================================
    // OPERACIONES HEREDADAS DE TDALista
    // =========================================================

    @Override
    public void agregar(T elem) {
        poneEnCola(elem);
    }

    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }

        Nodo nuevo = new Nodo(elem);

        if (index == 0) {
            nuevo.siguiente = frente;
            frente = nuevo;

            if (fin == null) {
                fin = nuevo;
            }
        } else {
            Nodo anterior = frente;

            for (int i = 1; i < index; i++) {
                anterior = anterior.siguiente;
            }

            nuevo.siguiente = anterior.siguiente;
            anterior.siguiente = nuevo;

            if (nuevo.siguiente == null) {
                fin = nuevo;
            }
        }

        tamaño++;
    }

    @Override
    public T obtener(int index) {
        validarIndice(index);

        Nodo actual = frente;

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
            eliminado = frente;
            frente = frente.siguiente;

            if (frente == null) {
                fin = null;
            }
        } else {
            Nodo anterior = frente;

            for (int i = 1; i < index; i++) {
                anterior = anterior.siguiente;
            }

            eliminado = anterior.siguiente;
            anterior.siguiente = eliminado.siguiente;

            if (eliminado == fin) {
                fin = anterior;
            }
        }

        tamaño--;

        return eliminado.dato;
    }

    @Override
    public boolean remover(T elem) {
        Nodo actual = frente;
        Nodo anterior = null;

        while (actual != null) {
            if (java.util.Objects.equals(actual.dato, elem)) {

                if (anterior == null) {
                    frente = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }

                if (actual == fin) {
                    fin = anterior;
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
        Nodo actual = frente;
        int indice = 0;

        while (actual != null) {
            if (java.util.Objects.equals(actual.dato, elem)) {
                return indice;
            }

            actual = actual.siguiente;
            indice++;
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        Nodo actual = frente;

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

        Nodo actual = frente;

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
        frente = null;
        fin = null;
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