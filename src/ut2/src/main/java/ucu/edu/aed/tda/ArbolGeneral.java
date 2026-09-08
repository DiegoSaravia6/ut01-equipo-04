package ucu.edu.aed.tda;

import java.util.function.Consumer;
import java.util.function.Predicate;

/** Árbol general n-ario con cantidad de niveles no fijada de antemano. */
public class ArbolGeneral<T> {
    private ElementoArbolGeneral<T> raiz;
    private int tamaño;

    public ArbolGeneral() { }
    public ArbolGeneral(T datoRaiz) { insertarRaiz(datoRaiz); }

    public void insertarRaiz(T dato) {
        if (dato == null) throw new IllegalArgumentException("El dato no puede ser null");
        if (raiz != null) throw new IllegalStateException("El árbol ya tiene raíz");
        raiz = new ElementoArbolGeneral<>(dato);
        tamaño = 1;
    }

    public ElementoArbolGeneral<T> agregarHijo(ElementoArbolGeneral<T> padre, T dato) {
        if (padre == null || dato == null) throw new IllegalArgumentException("Padre y dato son obligatorios");
        if (!pertenece(padre)) throw new IllegalArgumentException("El nodo padre no pertenece al árbol");
        ElementoArbolGeneral<T> hijo = new ElementoArbolGeneral<>(dato);
        padre.agregarHijo(hijo);
        tamaño++;
        return hijo;
    }

    public ElementoArbolGeneral<T> agregarHijo(Predicate<T> criterioPadre, T dato) {
        ElementoArbolGeneral<T> padre = buscarNodo(criterioPadre);
        if (padre == null) throw new IllegalArgumentException("No se encontró el nodo padre");
        return agregarHijo(padre, dato);
    }

    public T buscar(Predicate<T> criterio) {
        ElementoArbolGeneral<T> n = buscarNodo(criterio);
        return n == null ? null : n.getDato();
    }

    public ElementoArbolGeneral<T> buscarNodo(Predicate<T> criterio) {
        if (criterio == null) throw new IllegalArgumentException("El criterio no puede ser null");
        return buscarNodo(raiz, criterio);
    }

    private ElementoArbolGeneral<T> buscarNodo(ElementoArbolGeneral<T> n, Predicate<T> criterio) {
        if (n == null) return null;
        if (criterio.test(n.getDato())) return n;
        for (int i = 0; i < n.cantidadHijos(); i++) {
            ElementoArbolGeneral<T> encontrado = buscarNodo(n.obtenerHijo(i), criterio);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    /** Elimina el nodo encontrado y todo su subárbol. */
    public boolean eliminarSubarbol(Predicate<T> criterio) {
        ElementoArbolGeneral<T> n = buscarNodo(criterio);
        if (n == null) return false;
        int eliminados = contar(n);
        if (n == raiz) {
            raiz = null;
            tamaño = 0;
            return true;
        }
        n.getPadre().removerHijo(n);
        tamaño -= eliminados;
        return true;
    }

    public void preOrder(Consumer<T> c) { preOrder(raiz, c); }
    private void preOrder(ElementoArbolGeneral<T> n, Consumer<T> c) {
        if (n == null) return;
        c.accept(n.getDato());
        for (int i = 0; i < n.cantidadHijos(); i++) preOrder(n.obtenerHijo(i), c);
    }

    public void postOrder(Consumer<T> c) { postOrder(raiz, c); }
    private void postOrder(ElementoArbolGeneral<T> n, Consumer<T> c) {
        if (n == null) return;
        for (int i = 0; i < n.cantidadHijos(); i++) postOrder(n.obtenerHijo(i), c);
        c.accept(n.getDato());
    }

    /** Recorrido por niveles usando la Cola implementada en el primer hito. */
    public void porNiveles(Consumer<T> c) {
        if (raiz == null) return;
        Cola<ElementoArbolGeneral<T>> cola = new Cola<>();
        cola.poneEnCola(raiz);
        while (!cola.esVacio()) {
            ElementoArbolGeneral<T> n = cola.quitaDeCola();
            c.accept(n.getDato());
            for (int i = 0; i < n.cantidadHijos(); i++) cola.poneEnCola(n.obtenerHijo(i));
        }
    }

    public ListaArreglo<T> ancestros(ElementoArbolGeneral<T> nodo) {
        if (nodo == null || !pertenece(nodo)) throw new IllegalArgumentException("El nodo no pertenece al árbol");
        ListaArreglo<T> resultado = new ListaArreglo<>();
        ElementoArbolGeneral<T> actual = nodo.getPadre();
        while (actual != null) {
            resultado.agregar(actual.getDato());
            actual = actual.getPadre();
        }
        return resultado;
    }

    public void recorrerSubarbol(ElementoArbolGeneral<T> nodo, Consumer<T> c) {
        if (nodo == null || !pertenece(nodo)) throw new IllegalArgumentException("El nodo no pertenece al árbol");
        preOrder(nodo, c);
    }

    public void recorrerSubarbolPostOrder(ElementoArbolGeneral<T> nodo, Consumer<T> c) {
        if (nodo == null || !pertenece(nodo)) throw new IllegalArgumentException("El nodo no pertenece al árbol");
        postOrder(nodo, c);
    }

    public boolean pertenece(ElementoArbolGeneral<T> nodo) {
        if (nodo == null) return false;
        ElementoArbolGeneral<T> actual = nodo;
        while (actual.getPadre() != null) actual = actual.getPadre();
        return actual == raiz;
    }

    public ElementoArbolGeneral<T> obtenerRaiz() { return raiz; }
    public int cantidadNodos() { return tamaño; }
    public boolean esVacio() { return raiz == null; }
    public int altura() { return altura(raiz); }
    private int altura(ElementoArbolGeneral<T> n) {
        if (n == null) return -1;
        int mayor = -1;
        for (int i = 0; i < n.cantidadHijos(); i++) mayor = Math.max(mayor, altura(n.obtenerHijo(i)));
        return mayor + 1;
    }
    private int contar(ElementoArbolGeneral<T> n) {
        int total = 1;
        for (int i = 0; i < n.cantidadHijos(); i++) total += contar(n.obtenerHijo(i));
        return total;
    }
}
