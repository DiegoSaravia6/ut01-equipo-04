package ucu.edu.aed.tda;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Árbol binario general. No impone orden entre sus elementos.
 * La inserción automática se realiza por niveles para ocupar el primer hueco disponible.
 */
public class ArbolBinario<T> {
    private NodoBinario<T> raiz;
    private int tamaño;

    public boolean insertar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser null");
        }
        if (contiene(dato)) {
            return false;
        }
        NodoBinario<T> nuevo = new NodoBinario<>(dato);
        if (raiz == null) {
            raiz = nuevo;
            tamaño = 1;
            return true;
        }
        Cola<NodoBinario<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            NodoBinario<T> actual = cola.quitaDeCola();
            if (actual.getHijoIzquierdo() == null) {
                actual.setHijoIzquierdo(nuevo);
                tamaño++;
                return true;
            }
            cola.poneEnCola(actual.getHijoIzquierdo());
            if (actual.getHijoDerecho() == null) {
                actual.setHijoDerecho(nuevo);
                tamaño++;
                return true;
            }
            cola.poneEnCola(actual.getHijoDerecho());
        }
        return false;
    }

    /** Elimina por reemplazo con el nodo más profundo y más a la derecha. */
    public boolean eliminar(T dato) {
        if (raiz == null) return false;
        if (tamaño == 1) {
            if (Objects.equals(raiz.getDato(), dato)) {
                raiz = null;
                tamaño = 0;
                return true;
            }
            return false;
        }

        Cola<NodoConPadre<T>> cola = new Cola<>();
        cola.poneEnCola(new NodoConPadre<>(raiz, null));
        NodoBinario<T> objetivo = null;
        NodoBinario<T> masProfundo = null;
        NodoBinario<T> padreMasProfundo = null;

        while (!cola.esVacio()) {
            NodoConPadre<T> par = cola.quitaDeCola();
            NodoBinario<T> actual = par.nodo;
            masProfundo = actual;
            padreMasProfundo = par.padre;
            if (objetivo == null && Objects.equals(actual.getDato(), dato)) {
                objetivo = actual;
            }
            if (actual.getHijoIzquierdo() != null) {
                cola.poneEnCola(new NodoConPadre<>(actual.getHijoIzquierdo(), actual));
            }
            if (actual.getHijoDerecho() != null) {
                cola.poneEnCola(new NodoConPadre<>(actual.getHijoDerecho(), actual));
            }
        }
        if (objetivo == null) return false;

        objetivo.setDato(masProfundo.getDato());
        if (padreMasProfundo.getHijoDerecho() == masProfundo) {
            padreMasProfundo.setHijoDerecho(null);
        } else {
            padreMasProfundo.setHijoIzquierdo(null);
        }
        tamaño--;
        return true;
    }

    public T buscar(Predicate<T> criterio) {
        if (criterio == null) throw new IllegalArgumentException("El criterio no puede ser null");
        return buscarRec(raiz, criterio);
    }

    private T buscarRec(NodoBinario<T> nodo, Predicate<T> criterio) {
        if (nodo == null) return null;
        if (criterio.test(nodo.getDato())) return nodo.getDato();
        T encontrado = buscarRec(nodo.getHijoIzquierdo(), criterio);
        return encontrado != null ? encontrado : buscarRec(nodo.getHijoDerecho(), criterio);
    }

    public boolean contiene(T dato) {
        return buscar(x -> Objects.equals(x, dato)) != null;
    }

    public void preOrder(Consumer<T> consumidor) { preOrder(raiz, consumidor); }
    private void preOrder(NodoBinario<T> n, Consumer<T> c) {
        if (n == null) return;
        c.accept(n.getDato());
        preOrder(n.getHijoIzquierdo(), c);
        preOrder(n.getHijoDerecho(), c);
    }

    public void inOrder(Consumer<T> consumidor) { inOrder(raiz, consumidor); }
    private void inOrder(NodoBinario<T> n, Consumer<T> c) {
        if (n == null) return;
        inOrder(n.getHijoIzquierdo(), c);
        c.accept(n.getDato());
        inOrder(n.getHijoDerecho(), c);
    }

    public void postOrder(Consumer<T> consumidor) { postOrder(raiz, consumidor); }
    private void postOrder(NodoBinario<T> n, Consumer<T> c) {
        if (n == null) return;
        postOrder(n.getHijoIzquierdo(), c);
        postOrder(n.getHijoDerecho(), c);
        c.accept(n.getDato());
    }

    /** Recorrido por niveles apoyado en la Cola del primer hito. */
    public void porNiveles(Consumer<T> consumidor) {
        if (raiz == null) return;
        Cola<NodoBinario<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            NodoBinario<T> n = cola.quitaDeCola();
            consumidor.accept(n.getDato());
            if (n.getHijoIzquierdo() != null) cola.poneEnCola(n.getHijoIzquierdo());
            if (n.getHijoDerecho() != null) cola.poneEnCola(n.getHijoDerecho());
        }
    }

    public NodoBinario<T> obtenerRaiz() { return raiz; }
    public int cantidadNodos() { return tamaño; }
    public boolean esVacio() { return tamaño == 0; }
    public int altura() { return altura(raiz); }
    private int altura(NodoBinario<T> n) {
        if (n == null) return -1;
        return 1 + Math.max(altura(n.getHijoIzquierdo()), altura(n.getHijoDerecho()));
    }
    public int cantidadHojas() { return cantidadHojas(raiz); }
    private int cantidadHojas(NodoBinario<T> n) {
        if (n == null) return 0;
        if (n.esHoja()) return 1;
        return cantidadHojas(n.getHijoIzquierdo()) + cantidadHojas(n.getHijoDerecho());
    }
    public int cantidadNodosInternos() { return tamaño - cantidadHojas(); }

    private static class NodoConPadre<E> {
        private final NodoBinario<E> nodo;
        private final NodoBinario<E> padre;
        private NodoConPadre(NodoBinario<E> nodo, NodoBinario<E> padre) {
            this.nodo = nodo;
            this.padre = padre;
        }
    }
}
