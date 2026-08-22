package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

public class ListaArreglo<T> implements TDALista<T> {

    private T[] elementos;
    private int tamaño;

    private static final int CAPACIDAD_INICIAL = 10;

    @SuppressWarnings("unchecked")
    public ListaArreglo() {
        elementos = (T[]) new Object[CAPACIDAD_INICIAL];
        tamaño = 0;
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    @Override
    public void agregar(T elem) {
        asegurarCapacidad();

        elementos[tamaño] = elem;
        tamaño++;
    }

    @Override
    public void agregar(int index, T elem) {
        if (index < 0 || index > tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }

        asegurarCapacidad();

        // Desplazamos los elementos hacia la derecha.
        for (int i = tamaño; i > index; i--) {
            elementos[i] = elementos[i - 1];
        }

        elementos[index] = elem;
        tamaño++;
    }

    // =========================================================
    // OBTENER
    // =========================================================

    @Override
    public T obtener(int index) {
        validarIndice(index);

        return elementos[index];
    }

    // =========================================================
    // REMOVER
    // =========================================================

    @Override
    public T remover(int index) {
        validarIndice(index);

        T eliminado = elementos[index];

        // Desplazamos los elementos hacia la izquierda.
        for (int i = index; i < tamaño - 1; i++) {
            elementos[i] = elementos[i + 1];
        }

        // Evitamos mantener una referencia innecesaria.
        elementos[tamaño - 1] = null;

        tamaño--;

        return eliminado;
    }

    @Override
    public boolean remover(T elem) {
        int index = indiceDe(elem);

        if (index == -1) {
            return false;
        }

        remover(index);
        return true;
    }

    // =========================================================
    // BÚSQUEDA
    // =========================================================

    @Override
    public boolean contiene(T elem) {
        return indiceDe(elem) != -1;
    }

    @Override
    public int indiceDe(T elem) {
        for (int i = 0; i < tamaño; i++) {
            if (Objects.equals(elementos[i], elem)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        for (int i = 0; i < tamaño; i++) {
            if (criterio.test(elementos[i])) {
                return elementos[i];
            }
        }

        return null;
    }

    // =========================================================
    // ORDENAR
    // =========================================================

   @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        ListaArreglo<T> resultado = new ListaArreglo<>();

        for (int i = 0; i < tamaño; i++) {
            resultado.agregar(elementos[i]);
        }

        resultado.elementos = mergeSort(
                resultado.elementos,
                resultado.tamaño,
                comparator
        );

        return resultado;
    }

    // =========================================================
    // ESTADO
    // =========================================================

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
        for (int i = 0; i < tamaño; i++) {
            elementos[i] = null;
        }

        tamaño = 0;
    }

    // =========================================================
    // AUXILIARES
    // =========================================================

    private void validarIndice(int index) {
        if (index < 0 || index >= tamaño) {
            throw new IndexOutOfBoundsException(
                    "Índice fuera de rango: " + index
            );
        }
    }

    private void asegurarCapacidad() {
        if (tamaño < elementos.length) {
            return;
        }

        crecer();
    }

    @SuppressWarnings("unchecked")
    private void crecer() {
        int nuevaCapacidad = elementos.length * 2;

        T[] nuevo = (T[]) new Object[nuevaCapacidad];

        for (int i = 0; i < tamaño; i++) {
            nuevo[i] = elementos[i];
        }

        elementos = nuevo;
    }

   private T[] mergeSort(
            T[] arreglo,
            int cantidad,
            Comparator<T> comparator) {

        if (cantidad <= 1) {
            return arreglo;
        }

        int mitad = cantidad / 2;

        @SuppressWarnings("unchecked")
        T[] izquierda = (T[]) new Object[mitad];

        @SuppressWarnings("unchecked")
        T[] derecha = (T[]) new Object[cantidad - mitad];

        for (int i = 0; i < mitad; i++) {
            izquierda[i] = arreglo[i];
        }

        for (int i = mitad; i < cantidad; i++) {
            derecha[i - mitad] = arreglo[i];
        }

        izquierda = mergeSort(izquierda, izquierda.length, comparator);
        derecha = mergeSort(derecha, derecha.length, comparator);

        return mezclar(
                izquierda,
                derecha,
                comparator
        );
    } 

    private T[] mezclar(
            T[] izquierda,
            T[] derecha,
            Comparator<T> comparator) {

        @SuppressWarnings("unchecked")
        T[] resultado =
                (T[]) new Object[izquierda.length + derecha.length];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < izquierda.length && j < derecha.length) {

            if (comparator.compare(
                    izquierda[i],
                    derecha[j]
            ) <= 0) {

                resultado[k] = izquierda[i];
                i++;

            } else {

                resultado[k] = derecha[j];
                j++;
            }

            k++;
        }

        while (i < izquierda.length) {
            resultado[k] = izquierda[i];
            i++;
            k++;
        }

        while (j < derecha.length) {
            resultado[k] = derecha[j];
            j++;
            k++;
        }

        return resultado;
    }
}