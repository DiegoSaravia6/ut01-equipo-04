package ucu.edu.aed.tda;

/** Nodo de un árbol general n-ario. */
public class ElementoArbolGeneral<T> {
    private T dato;
    private ElementoArbolGeneral<T> padre;
    private final ListaArreglo<ElementoArbolGeneral<T>> hijos;

    public ElementoArbolGeneral(T dato) {
        this.dato = dato;
        this.hijos = new ListaArreglo<>();
    }

    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    public ElementoArbolGeneral<T> getPadre() { return padre; }
    void setPadre(ElementoArbolGeneral<T> padre) { this.padre = padre; }
    public int cantidadHijos() { return hijos.tamaño(); }
    public ElementoArbolGeneral<T> obtenerHijo(int indice) { return hijos.obtener(indice); }
    public boolean esHoja() { return hijos.esVacio(); }

    void agregarHijo(ElementoArbolGeneral<T> hijo) {
        hijo.padre = this;
        hijos.agregar(hijo);
    }

    boolean removerHijo(ElementoArbolGeneral<T> hijo) {
        boolean removido = hijos.remover(hijo);
        if (removido) hijo.padre = null;
        return removido;
    }
}
