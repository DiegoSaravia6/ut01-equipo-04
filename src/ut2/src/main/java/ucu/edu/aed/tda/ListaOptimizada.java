package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaOptimizada<T> implements TDALista<T> {

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

    public ListaOptimizada() {
        primero = null;
        ultimo = null;
        tamaño = 0;
    }

    @Override
    public void agregar(T elem) {
        Nodo nuevo = new Nodo(elem);

        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }

        tamaño++;
    }

    @Override
    public void agregar(int index, T elem) {

        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango"
            );
        }

        // Agregar al principio.
        if (index == 0) {

            Nodo nuevo = new Nodo(elem);
            nuevo.siguiente = primero;

            primero = nuevo;

            if (tamaño == 0) {
                ultimo = nuevo;
            }

            tamaño++;
            return;
        }

        // Agregar al final.
        if (index == tamaño) {
            agregar(elem);
            return;
        }

        Nodo anterior = primero;

        for (int i = 0; i < index - 1; i++) {
            anterior = anterior.siguiente;
        }

        Nodo nuevo = new Nodo(elem);

        nuevo.siguiente = anterior.siguiente;
        anterior.siguiente = nuevo;

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

        // Remover el primer elemento.
        if (index == 0) {

            eliminado = primero;
            primero = primero.siguiente;

            tamaño--;

            if (tamaño == 0) {
                ultimo = null;
            }

            return eliminado.dato;
        }

        Nodo anterior = primero;

        for (int i = 0; i < index - 1; i++) {
            anterior = anterior.siguiente;
        }

        eliminado = anterior.siguiente;

        anterior.siguiente = eliminado.siguiente;

        if (eliminado == ultimo) {
            ultimo = anterior;
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

                    if (actual == ultimo) {
                        ultimo = null;
                    }

                } else {

                    anterior.siguiente = actual.siguiente;

                    if (actual == ultimo) {
                        ultimo = anterior;
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

        ListaOptimizada<T> resultado =
                new ListaOptimizada<>();

        Nodo actual = primero;

        while (actual != null) {
            resultado.agregar(actual.dato);
            actual = actual.siguiente;
        }

        resultado.primero = mergeSort(
                resultado.primero,
                resultado.tamaño,
                comparator
        );

        // Después del Merge Sort, recalculamos el último nodo.
        resultado.actualizarUltimo();

        return resultado;
    }

    private Nodo mergeSort(
            Nodo cabeza,
            int cantidad,
            Comparator<T> comparator) {

        if (cabeza == null || cabeza.siguiente == null) {
            return cabeza;
        }

        int mitad = cantidad / 2;

        Nodo derecha = cabeza;

        for (int i = 0; i < mitad; i++) {
            derecha = derecha.siguiente;
        }

        Nodo anterior = cabeza;

        for (int i = 1; i < mitad; i++) {
            anterior = anterior.siguiente;
        }

        anterior.siguiente = null;

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

        return mezclarNodos(
                izquierdaOrdenada,
                derechaOrdenada,
                comparator
        );
    }

    private Nodo mezclarNodos(
            Nodo izquierda,
            Nodo derecha,
            Comparator<T> comparator) {

        Nodo cabeza = null;
        Nodo ultimoResultado = null;

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
                ultimoResultado = elegido;
            } else {
                ultimoResultado.siguiente = elegido;
                ultimoResultado = elegido;
            }
        }

        if (izquierda != null) {
            ultimoResultado.siguiente = izquierda;
        } else {
            ultimoResultado.siguiente = derecha;
        }

        return cabeza;
    }

    private void actualizarUltimo() {

        if (primero == null) {
            ultimo = null;
            return;
        }

        Nodo actual = primero;

        while (actual.siguiente != null) {
            actual = actual.siguiente;
        }

        ultimo = actual;
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
        ultimo = null;
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