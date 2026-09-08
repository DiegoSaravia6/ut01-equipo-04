package ucu.edu.aed.tda;

import java.util.function.Consumer;

/** Árbol binario de búsqueda no balanceado. */
public class ArbolBinarioBusqueda<T> implements TDAArbolBinario<T> {
    private ElementoABB<T> raiz;

    @Override
    public T buscar(Comparable<T> criterioBusqueda) {
        if (criterioBusqueda == null) throw new IllegalArgumentException("El criterio no puede ser null");
        ElementoABB<T> encontrado = raiz == null ? null : raiz.buscar(criterioBusqueda);
        return encontrado == null ? null : encontrado.getDato();
    }

    @Override public ElementoABB<T> obtenerRaiz() { return raiz; }

    @Override
    public boolean insertar(Comparable<T> dato) {
        if (dato == null) throw new IllegalArgumentException("El dato no puede ser null");
        if (raiz == null) {
            @SuppressWarnings("unchecked") T valor = (T) dato;
            raiz = new ElementoABB<>(valor);
            return true;
        }
        return raiz.insertar(dato);
    }

    @Override
    public boolean eliminar(Comparable<T> criterioBusqueda) {
        if (raiz == null) return false;
        if (criterioBusqueda == null) throw new IllegalArgumentException("El criterio no puede ser null");
        ResultadoEliminacion<T> resultado = eliminarRec(raiz, criterioBusqueda);
        raiz = resultado.raiz;
        return resultado.eliminado;
    }

    private ResultadoEliminacion<T> eliminarRec(ElementoABB<T> nodo, Comparable<T> criterio) {
        if (nodo == null) return new ResultadoEliminacion<>(null, false);
        int cmp = criterio.compareTo(nodo.getDato());
        if (cmp < 0) {
            ResultadoEliminacion<T> r = eliminarRec(nodo.getHijoIzquierdo(), criterio);
            nodo.setHijoIzquierdo(r.raiz);
            return new ResultadoEliminacion<>(nodo, r.eliminado);
        }
        if (cmp > 0) {
            ResultadoEliminacion<T> r = eliminarRec(nodo.getHijoDerecho(), criterio);
            nodo.setHijoDerecho(r.raiz);
            return new ResultadoEliminacion<>(nodo, r.eliminado);
        }

        if (nodo.getHijoIzquierdo() == null) return new ResultadoEliminacion<>(nodo.getHijoDerecho(), true);
        if (nodo.getHijoDerecho() == null) return new ResultadoEliminacion<>(nodo.getHijoIzquierdo(), true);

        ElementoABB<T> sucesor = minimo(nodo.getHijoDerecho());
        nodo.setDato(sucesor.getDato());
        Comparable<T> criterioSucesor = comparableDe(sucesor.getDato());
        ResultadoEliminacion<T> r = eliminarRec(nodo.getHijoDerecho(), criterioSucesor);
        nodo.setHijoDerecho(r.raiz);
        return new ResultadoEliminacion<>(nodo, true);
    }

    @SuppressWarnings("unchecked")
    private Comparable<T> comparableDe(T dato) {
        if (!(dato instanceof Comparable<?>)) {
            throw new IllegalStateException("Los datos insertados deben implementar Comparable");
        }
        return (Comparable<T>) dato;
    }

    private ElementoABB<T> minimo(ElementoABB<T> n) {
        while (n.getHijoIzquierdo() != null) n = n.getHijoIzquierdo();
        return n;
    }

    @Override public void inOrder(Consumer<T> c) {
        if (raiz != null) raiz.inOrder(n -> c.accept(n.getDato()));
    }
    @Override public void preOrder(Consumer<T> c) {
        if (raiz != null) raiz.preOrder(n -> c.accept(n.getDato()));
    }
    @Override public void postOrder(Consumer<T> c) {
        if (raiz != null) raiz.postOrder(n -> c.accept(n.getDato()));
    }

    public void porNiveles(Consumer<T> c) {
        if (raiz == null) return;
        Cola<ElementoABB<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            ElementoABB<T> n = cola.quitaDeCola();
            c.accept(n.getDato());
            if (n.getHijoIzquierdo() != null) cola.poneEnCola(n.getHijoIzquierdo());
            if (n.getHijoDerecho() != null) cola.poneEnCola(n.getHijoDerecho());
        }
    }

    @Override public boolean esVacio() { return raiz == null; }
    @Override public int cantidadNodos() { return raiz == null ? 0 : raiz.cantidadNodos(); }
    @Override public int cantidadHojas() { return raiz == null ? 0 : raiz.cantidadHojas(); }
    @Override public int cantidadNodosInternos() { return raiz == null ? 0 : raiz.cantidadNodosInternos(); }
    public int altura() { return raiz == null ? -1 : raiz.altura(); }
    public int obtenerNivel(Comparable<T> criterio) { return raiz == null ? -1 : raiz.obtenerNivel(criterio); }

    private static class ResultadoEliminacion<E> {
        private final ElementoABB<E> raiz;
        private final boolean eliminado;
        private ResultadoEliminacion(ElementoABB<E> raiz, boolean eliminado) {
            this.raiz = raiz;
            this.eliminado = eliminado;
        }
    }
}
