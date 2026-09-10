package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Montículo binario mínimo. El elemento que el Comparator considera menor
 * tiene mayor prioridad y queda en la raíz.
 */
public class MonticuloBinario<T> {
    private Object[] elementos;
    private int tamaño;
    private final Comparator<T> comparator;
    private static final int CAPACIDAD_INICIAL = 10;

    public MonticuloBinario(Comparator<T> comparator) {
        if (comparator == null) throw new IllegalArgumentException("El comparator no puede ser null");
        this.comparator = comparator;
        this.elementos = new Object[CAPACIDAD_INICIAL];
    }

    public void insertar(T dato) {
        if (dato == null) throw new IllegalArgumentException("El dato no puede ser null");
        asegurarCapacidad();
        elementos[tamaño] = dato;
        actualizarIndice(dato, tamaño);
        subir(tamaño);
        tamaño++;
    }

    public T primero() {
        if (esVacio()) throw new NoSuchElementException("El montículo está vacío");
        return elemento(0);
    }

    public T extraerPrimero() {
        if (esVacio()) throw new NoSuchElementException("El montículo está vacío");
        T resultado = elemento(0);
        tamaño--;
        elementos[0] = elementos[tamaño];
        elementos[tamaño] = null;
        actualizarIndice(resultado, -1);
        if (tamaño > 0) {
            actualizarIndice(elemento(0), 0);
            bajar(0);
        }
        return resultado;
    }

    /** Remover por valor cuesta O(n) para localizarlo y O(log n) para restaurar el heap. */
    public boolean remover(T dato) {
        int i = indiceDe(dato);
        if (i < 0) return false;
        tamaño--;
        if (i == tamaño) {
            elementos[i] = null;
            actualizarIndice(dato, -1);
            return true;
        }
        elementos[i] = elementos[tamaño];
        elementos[tamaño] = null;
        actualizarIndice(dato, -1);
        actualizarIndice(elemento(i), i);
        reordenarIndice(i);
        return true;
    }

    /**
     * Reordena un elemento cuyo criterio de prioridad cambió.
     * La localización es O(n), la reparación del heap O(log n).
     */
    public boolean reordenar(T dato) {
        int i = indiceDe(dato);
        if (i < 0) return false;
        reordenarIndice(i);
        return true;
    }

    private void reordenarIndice(int i) {
        int padre = padre(i);
        if (i > 0 && comparar(i, padre) < 0) subir(i);
        else bajar(i);
    }

    public boolean contiene(T dato) { return indiceDe(dato) >= 0; }
    private int indiceDe(T dato) {
        if (dato instanceof IndexableMonticulo) {
            int i = ((IndexableMonticulo) dato).getIndiceMonticulo();
            if (i >= 0 && i < tamaño && elemento(i) == dato) return i;
        }
        for (int i = 0; i < tamaño; i++) {
            if (Objects.equals(elemento(i), dato)) return i;
        }
        return -1;
    }

    private void subir(int indice) {
        int i = indice;
        while (i > 0) {
            int p = padre(i);
            if (comparar(i, p) >= 0) break;
            intercambiar(i, p);
            i = p;
        }
    }

    private void bajar(int indice) {
        int i = indice;
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int mejor = i;
            if (izq < tamaño && comparar(izq, mejor) < 0) mejor = izq;
            if (der < tamaño && comparar(der, mejor) < 0) mejor = der;
            if (mejor == i) return;
            intercambiar(i, mejor);
            i = mejor;
        }
    }

    private int padre(int i) { return (i - 1) / 2; }
    private int comparar(int i, int j) { return comparator.compare(elemento(i), elemento(j)); }
    private void intercambiar(int i, int j) {
        Object tmp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = tmp;
        actualizarIndice(elemento(i), i);
        actualizarIndice(elemento(j), j);
    }

    private void actualizarIndice(T dato, int indice) {
        if (dato instanceof IndexableMonticulo) {
            ((IndexableMonticulo) dato).setIndiceMonticulo(indice);
        }
    }

    @SuppressWarnings("unchecked")
    private T elemento(int i) { return (T) elementos[i]; }

    private void asegurarCapacidad() {
        if (tamaño < elementos.length) return;
        Object[] nuevo = new Object[elementos.length * 2];
        for (int i = 0; i < tamaño; i++) nuevo[i] = elementos[i];
        elementos = nuevo;
    }

    public int tamaño() { return tamaño; }
    public boolean esVacio() { return tamaño == 0; }
    public void vaciar() {
        for (int i = 0; i < tamaño; i++) {
            actualizarIndice(elemento(i), -1);
            elementos[i] = null;
        }
        tamaño = 0;
    }
}
