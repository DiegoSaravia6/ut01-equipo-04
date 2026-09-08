package ucu.edu.aed.tda;

import java.util.function.Consumer;

/** Nodo recursivo para el árbol binario de búsqueda. */
public class ElementoABB<T> implements TDAElemento<T> {
    private T dato;
    private ElementoABB<T> hijoIzquierdo;
    private ElementoABB<T> hijoDerecho;

    public ElementoABB(T dato) {
        this.dato = dato;
    }

    @Override
    public void setHijoIzquierdo(TDAElemento<T> hijoIzquierdo) {
        this.hijoIzquierdo = (ElementoABB<T>) hijoIzquierdo;
    }

    @Override
    public void setHijoDerecho(TDAElemento<T> hijoDerecho) {
        this.hijoDerecho = (ElementoABB<T>) hijoDerecho;
    }

    @Override public ElementoABB<T> getHijoIzquierdo() { return hijoIzquierdo; }
    @Override public ElementoABB<T> getHijoDerecho() { return hijoDerecho; }
    @Override public void setDato(T dato) { this.dato = dato; }
    @Override public T getDato() { return dato; }

    @Override
    public ElementoABB<T> buscar(Comparable<T> criterioBusqueda) {
        int comparacion = criterioBusqueda.compareTo(dato);
        if (comparacion == 0) return this;
        if (comparacion < 0) {
            return hijoIzquierdo == null ? null : hijoIzquierdo.buscar(criterioBusqueda);
        }
        return hijoDerecho == null ? null : hijoDerecho.buscar(criterioBusqueda);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean insertar(Comparable<T> nuevoDato) {
        T valor = (T) nuevoDato;
        int comparacion = nuevoDato.compareTo(dato);
        if (comparacion == 0) return false;
        if (comparacion < 0) {
            if (hijoIzquierdo == null) {
                hijoIzquierdo = new ElementoABB<>(valor);
                return true;
            }
            return hijoIzquierdo.insertar(nuevoDato);
        }
        if (hijoDerecho == null) {
            hijoDerecho = new ElementoABB<>(valor);
            return true;
        }
        return hijoDerecho.insertar(nuevoDato);
    }

    /**
     * Elimina dentro del subárbol y retorna el nodo que contenía el dato eliminado.
     * Para eliminar la raíz completa se utiliza ArbolBinarioBusqueda.eliminar.
     */
    @Override
    public ElementoABB<T> eliminar(Comparable<T> criterioBusqueda) {
        int cmp = criterioBusqueda.compareTo(dato);
        if (cmp < 0) return eliminarDesdeHijo(true, criterioBusqueda);
        if (cmp > 0) return eliminarDesdeHijo(false, criterioBusqueda);

        // Si se invoca directamente sobre este nodo, preservamos la referencia
        // copiando el sucesor/hijo cuando es posible.
        ElementoABB<T> eliminado = new ElementoABB<>(dato);
        if (hijoIzquierdo == null && hijoDerecho == null) return eliminado;
        if (hijoIzquierdo == null) {
            copiarDesde(hijoDerecho);
            return eliminado;
        }
        if (hijoDerecho == null) {
            copiarDesde(hijoIzquierdo);
            return eliminado;
        }
        ElementoABB<T> sucesorPadre = this;
        ElementoABB<T> sucesor = hijoDerecho;
        while (sucesor.hijoIzquierdo != null) {
            sucesorPadre = sucesor;
            sucesor = sucesor.hijoIzquierdo;
        }
        dato = sucesor.dato;
        if (sucesorPadre == this) sucesorPadre.hijoDerecho = sucesor.hijoDerecho;
        else sucesorPadre.hijoIzquierdo = sucesor.hijoDerecho;
        return eliminado;
    }

    private ElementoABB<T> eliminarDesdeHijo(boolean izquierda, Comparable<T> criterio) {
        ElementoABB<T> hijo = izquierda ? hijoIzquierdo : hijoDerecho;
        if (hijo == null) return null;
        int cmp = criterio.compareTo(hijo.dato);
        if (cmp != 0) return hijo.eliminar(criterio);

        ElementoABB<T> eliminado = new ElementoABB<>(hijo.dato);
        ElementoABB<T> reemplazo = reemplazoDe(hijo);
        if (izquierda) hijoIzquierdo = reemplazo;
        else hijoDerecho = reemplazo;
        return eliminado;
    }

    private ElementoABB<T> reemplazoDe(ElementoABB<T> nodo) {
        if (nodo.hijoIzquierdo == null) return nodo.hijoDerecho;
        if (nodo.hijoDerecho == null) return nodo.hijoIzquierdo;
        ElementoABB<T> padreSucesor = nodo;
        ElementoABB<T> sucesor = nodo.hijoDerecho;
        while (sucesor.hijoIzquierdo != null) {
            padreSucesor = sucesor;
            sucesor = sucesor.hijoIzquierdo;
        }
        nodo.dato = sucesor.dato;
        if (padreSucesor == nodo) padreSucesor.hijoDerecho = sucesor.hijoDerecho;
        else padreSucesor.hijoIzquierdo = sucesor.hijoDerecho;
        return nodo;
    }

    private void copiarDesde(ElementoABB<T> otro) {
        dato = otro.dato;
        hijoIzquierdo = otro.hijoIzquierdo;
        hijoDerecho = otro.hijoDerecho;
    }

    @Override public void inOrder(Consumer<TDAElemento<T>> c) {
        if (hijoIzquierdo != null) hijoIzquierdo.inOrder(c);
        c.accept(this);
        if (hijoDerecho != null) hijoDerecho.inOrder(c);
    }
    @Override public void preOrder(Consumer<TDAElemento<T>> c) {
        c.accept(this);
        if (hijoIzquierdo != null) hijoIzquierdo.preOrder(c);
        if (hijoDerecho != null) hijoDerecho.preOrder(c);
    }
    @Override public void postOrder(Consumer<TDAElemento<T>> c) {
        if (hijoIzquierdo != null) hijoIzquierdo.postOrder(c);
        if (hijoDerecho != null) hijoDerecho.postOrder(c);
        c.accept(this);
    }
    @Override public boolean esHoja() { return hijoIzquierdo == null && hijoDerecho == null; }
    @Override public int cantidadHojas() {
        if (esHoja()) return 1;
        return (hijoIzquierdo == null ? 0 : hijoIzquierdo.cantidadHojas())
                + (hijoDerecho == null ? 0 : hijoDerecho.cantidadHojas());
    }
    @Override public int cantidadNodosInternos() {
        if (esHoja()) return 0;
        return 1 + (hijoIzquierdo == null ? 0 : hijoIzquierdo.cantidadNodosInternos())
                + (hijoDerecho == null ? 0 : hijoDerecho.cantidadNodosInternos());
    }
    @Override public int cantidadNodos() {
        return 1 + (hijoIzquierdo == null ? 0 : hijoIzquierdo.cantidadNodos())
                + (hijoDerecho == null ? 0 : hijoDerecho.cantidadNodos());
    }
    @Override public int altura() {
        return 1 + Math.max(hijoIzquierdo == null ? -1 : hijoIzquierdo.altura(),
                hijoDerecho == null ? -1 : hijoDerecho.altura());
    }
    @Override public int obtenerNivel(Comparable<T> criterioBusqueda) {
        int cmp = criterioBusqueda.compareTo(dato);
        if (cmp == 0) return 0;
        ElementoABB<T> hijo = cmp < 0 ? hijoIzquierdo : hijoDerecho;
        if (hijo == null) return -1;
        int nivel = hijo.obtenerNivel(criterioBusqueda);
        return nivel == -1 ? -1 : nivel + 1;
    }
}
