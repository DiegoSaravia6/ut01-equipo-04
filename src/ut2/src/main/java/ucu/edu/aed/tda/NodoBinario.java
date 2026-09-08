package ucu.edu.aed.tda;

/** Nodo utilizado por ArbolBinario. */
public class NodoBinario<T> {
    private T dato;
    private NodoBinario<T> hijoIzquierdo;
    private NodoBinario<T> hijoDerecho;

    public NodoBinario(T dato) {
        this.dato = dato;
    }

    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    public NodoBinario<T> getHijoIzquierdo() { return hijoIzquierdo; }
    public NodoBinario<T> getHijoDerecho() { return hijoDerecho; }
    public void setHijoIzquierdo(NodoBinario<T> hijoIzquierdo) { this.hijoIzquierdo = hijoIzquierdo; }
    public void setHijoDerecho(NodoBinario<T> hijoDerecho) { this.hijoDerecho = hijoDerecho; }
    public boolean esHoja() { return hijoIzquierdo == null && hijoDerecho == null; }
}
