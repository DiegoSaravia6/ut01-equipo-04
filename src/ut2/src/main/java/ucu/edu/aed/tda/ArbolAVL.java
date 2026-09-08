package ucu.edu.aed.tda;

import java.util.function.Consumer;

/**
 * Árbol binario de búsqueda AVL. Mantiene |factor de balance| <= 1 en todos los nodos.
 */
public class ArbolAVL<T extends Comparable<T>> {
    private NodoAVL<T> raiz;
    private int tamaño;

    private static class NodoAVL<E> {
        E dato;
        NodoAVL<E> izquierdo;
        NodoAVL<E> derecho;
        int altura;
        NodoAVL(E dato) { this.dato = dato; this.altura = 0; }
    }

    public boolean insertar(T dato) {
        if (dato == null) throw new IllegalArgumentException("El dato no puede ser null");
        boolean[] insertado = {false};
        raiz = insertar(raiz, dato, insertado);
        if (insertado[0]) tamaño++;
        return insertado[0];
    }

    private NodoAVL<T> insertar(NodoAVL<T> nodo, T dato, boolean[] insertado) {
        if (nodo == null) {
            insertado[0] = true;
            return new NodoAVL<>(dato);
        }
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) nodo.izquierdo = insertar(nodo.izquierdo, dato, insertado);
        else if (cmp > 0) nodo.derecho = insertar(nodo.derecho, dato, insertado);
        else return nodo;
        actualizarAltura(nodo);
        return balancear(nodo);
    }

    /** Búsqueda por una clave externa compatible con el orden del árbol. */
    public T buscar(Comparable<T> criterio) {
        if (criterio == null) return null;
        NodoAVL<T> n = raiz;
        while (n != null) {
            int cmp = criterio.compareTo(n.dato);
            if (cmp == 0) return n.dato;
            n = cmp < 0 ? n.izquierdo : n.derecho;
        }
        return null;
    }

    public boolean eliminar(T dato) {
        if (dato == null) return false;
        boolean[] eliminado = {false};
        raiz = eliminar(raiz, dato, eliminado);
        if (eliminado[0]) tamaño--;
        return eliminado[0];
    }

    private NodoAVL<T> eliminar(NodoAVL<T> nodo, T dato, boolean[] eliminado) {
        if (nodo == null) return null;
        int cmp = dato.compareTo(nodo.dato);
        if (cmp < 0) nodo.izquierdo = eliminar(nodo.izquierdo, dato, eliminado);
        else if (cmp > 0) nodo.derecho = eliminar(nodo.derecho, dato, eliminado);
        else {
            eliminado[0] = true;
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            NodoAVL<T> sucesor = minimo(nodo.derecho);
            nodo.dato = sucesor.dato;
            boolean[] ignorar = {false};
            nodo.derecho = eliminar(nodo.derecho, sucesor.dato, ignorar);
        }
        actualizarAltura(nodo);
        return balancear(nodo);
    }

    private NodoAVL<T> minimo(NodoAVL<T> n) {
        while (n.izquierdo != null) n = n.izquierdo;
        return n;
    }

    private int altura(NodoAVL<T> n) { return n == null ? -1 : n.altura; }
    private void actualizarAltura(NodoAVL<T> n) {
        n.altura = 1 + Math.max(altura(n.izquierdo), altura(n.derecho));
    }
    private int balance(NodoAVL<T> n) { return n == null ? 0 : altura(n.izquierdo) - altura(n.derecho); }

    private NodoAVL<T> balancear(NodoAVL<T> n) {
        int b = balance(n);
        if (b > 1) {
            if (balance(n.izquierdo) < 0) n.izquierdo = rotarIzquierda(n.izquierdo);
            return rotarDerecha(n);
        }
        if (b < -1) {
            if (balance(n.derecho) > 0) n.derecho = rotarDerecha(n.derecho);
            return rotarIzquierda(n);
        }
        return n;
    }

    private NodoAVL<T> rotarDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = y.izquierdo;
        NodoAVL<T> t2 = x.derecho;
        x.derecho = y;
        y.izquierdo = t2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    private NodoAVL<T> rotarIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = x.derecho;
        NodoAVL<T> t2 = y.izquierdo;
        y.izquierdo = x;
        x.derecho = t2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    public void inOrder(Consumer<T> c) { inOrder(raiz, c); }
    private void inOrder(NodoAVL<T> n, Consumer<T> c) {
        if (n == null) return;
        inOrder(n.izquierdo, c);
        c.accept(n.dato);
        inOrder(n.derecho, c);
    }
    public void preOrder(Consumer<T> c) { preOrder(raiz, c); }
    private void preOrder(NodoAVL<T> n, Consumer<T> c) {
        if (n == null) return;
        c.accept(n.dato);
        preOrder(n.izquierdo, c);
        preOrder(n.derecho, c);
    }
    public void postOrder(Consumer<T> c) { postOrder(raiz, c); }
    private void postOrder(NodoAVL<T> n, Consumer<T> c) {
        if (n == null) return;
        postOrder(n.izquierdo, c);
        postOrder(n.derecho, c);
        c.accept(n.dato);
    }
    public void porNiveles(Consumer<T> c) {
        if (raiz == null) return;
        Cola<NodoAVL<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            NodoAVL<T> n = cola.quitaDeCola();
            c.accept(n.dato);
            if (n.izquierdo != null) cola.poneEnCola(n.izquierdo);
            if (n.derecho != null) cola.poneEnCola(n.derecho);
        }
    }

    /** Devuelve los elementos entre desde y hasta, inclusive, en orden. */
    public ListaArreglo<T> listarRango(T desde, T hasta) {
        if (desde == null || hasta == null) throw new IllegalArgumentException("Los límites no pueden ser null");
        if (desde.compareTo(hasta) > 0) throw new IllegalArgumentException("El límite inferior es mayor al superior");
        ListaArreglo<T> resultado = new ListaArreglo<>();
        listarRango(raiz, desde, hasta, resultado);
        return resultado;
    }

    private void listarRango(NodoAVL<T> n, T desde, T hasta, ListaArreglo<T> r) {
        if (n == null) return;
        int cmpDesde = n.dato.compareTo(desde);
        int cmpHasta = n.dato.compareTo(hasta);
        if (cmpDesde > 0) listarRango(n.izquierdo, desde, hasta, r);
        if (cmpDesde >= 0 && cmpHasta <= 0) r.agregar(n.dato);
        if (cmpHasta < 0) listarRango(n.derecho, desde, hasta, r);
    }

    public boolean estaBalanceado() { return verificarBalance(raiz) != Integer.MIN_VALUE; }
    private int verificarBalance(NodoAVL<T> n) {
        if (n == null) return -1;
        int izq = verificarBalance(n.izquierdo);
        if (izq == Integer.MIN_VALUE) return izq;
        int der = verificarBalance(n.derecho);
        if (der == Integer.MIN_VALUE) return der;
        if (Math.abs(izq - der) > 1) return Integer.MIN_VALUE;
        return 1 + Math.max(izq, der);
    }

    public boolean esVacio() { return tamaño == 0; }
    public int cantidadNodos() { return tamaño; }
    public int altura() { return altura(raiz); }
    public T obtenerRaizDato() { return raiz == null ? null : raiz.dato; }
}
