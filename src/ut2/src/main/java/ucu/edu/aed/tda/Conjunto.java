package ucu.edu.aed.tda;

import java.util.Comparator;
import java.util.function.Predicate;

public class Conjunto<T> implements TDAConjunto<T> {

    private Lista<T> elementos;
    private Comparator<T> comparator;

    public Conjunto(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException(
                    "El comparador no puede ser null"
            );
        }

        this.elementos = new Lista<>();
        this.comparator = comparator;
    }

    // =========================================================
    // OPERACIONES BÁSICAS
    // =========================================================

    @Override
    public void agregar(T elem) {
        if (contiene(elem)) {
            return;
        }

        elementos.agregar(elem);
        elementos = (Lista<T>) elementos.ordenar(comparator);
    }

    @Override
    public boolean remover(T elem) {
        return elementos.remover(elem);
    }

    @Override
    public boolean contiene(T elem) {
        return elementos.contiene(elem);
    }

    // =========================================================
    // OPERACIONES HEREDADAS DE TDALista
    // =========================================================

    @Override
    public void agregar(int index, T elem) {

        if (contiene(elem)) {
            return;
        }

        elementos.agregar(index, elem);
        elementos = (Lista<T>) elementos.ordenar(comparator);
    }

    @Override
    public T obtener(int index) {
        return elementos.obtener(index);
    }

    @Override
    public T remover(int index) {
        return elementos.remover(index);
    }

    @Override
    public int indiceDe(T elem) {
        return elementos.indiceDe(elem);
    }

    @Override
    public T buscar(Predicate<T> criterio) {
        return elementos.buscar(criterio);
    }

    @Override
    public TDALista<T> ordenar(Comparator<T> comparator) {
        return elementos.ordenar(comparator);
    }

    @Override
    public int tamaño() {
        return elementos.tamaño();
    }

    @Override
    public boolean esVacio() {
        return elementos.esVacio();
    }

    @Override
    public void vaciar() {
        elementos.vaciar();
    }

    // =========================================================
    // UNION
    // =========================================================

    @Override
    public TDAConjunto<T> union(TDAConjunto<T> otro) {

        if (otro == null) {
            throw new IllegalArgumentException(
                    "El conjunto no puede ser null"
            );
        }

        Conjunto<T> resultado = new Conjunto<>(comparator);

        int i = 0;
        int j = 0;

        while (i < this.tamaño() && j < otro.tamaño()) {

            T elementoA = this.obtener(i);
            T elementoB = otro.obtener(j);

            int comparacion = comparator.compare(
                    elementoA,
                    elementoB
            );

            if (comparacion < 0) {

                resultado.elementos.agregar(elementoA);
                i++;

            } else if (comparacion > 0) {

                resultado.elementos.agregar(elementoB);
                j++;

            } else {

                resultado.elementos.agregar(elementoA);
                i++;
                j++;
            }
        }

        // Elementos restantes de A
        while (i < this.tamaño()) {
            resultado.elementos.agregar(this.obtener(i));
            i++;
        }

        // Elementos restantes de B
        while (j < otro.tamaño()) {
            resultado.elementos.agregar(otro.obtener(j));
            j++;
        }

        return resultado;
    }

    // =========================================================
    // INTERSECCIÓN
    // =========================================================

    @Override
    public TDAConjunto<T> interseccion(TDAConjunto<T> otro) {

        if (otro == null) {
            throw new IllegalArgumentException(
                    "El conjunto no puede ser null"
            );
        }

        Conjunto<T> resultado = new Conjunto<>(comparator);

        int i = 0;
        int j = 0;

        while (i < this.tamaño() && j < otro.tamaño()) {

            T elementoA = this.obtener(i);
            T elementoB = otro.obtener(j);

            int comparacion = comparator.compare(
                    elementoA,
                    elementoB
            );

            if (comparacion < 0) {

                i++;

            } else if (comparacion > 0) {

                j++;

            } else {

                resultado.elementos.agregar(elementoA);

                i++;
                j++;
            }
        }

        return resultado;
    }

    // =========================================================
    // DIFERENCIA
    // =========================================================

    @Override
    public TDAConjunto<T> diferencia(TDAConjunto<T> otro) {

        if (otro == null) {
            throw new IllegalArgumentException(
                    "El conjunto no puede ser null"
            );
        }

        Conjunto<T> resultado = new Conjunto<>(comparator);

        int i = 0;
        int j = 0;

        while (i < this.tamaño() && j < otro.tamaño()) {

            T elementoA = this.obtener(i);
            T elementoB = otro.obtener(j);

            int comparacion = comparator.compare(
                    elementoA,
                    elementoB
            );

            if (comparacion < 0) {

                resultado.elementos.agregar(elementoA);
                i++;

            } else if (comparacion > 0) {

                j++;

            } else {

                i++;
                j++;
            }
        }

        // Lo que queda en A no aparece en B
        while (i < this.tamaño()) {

            resultado.elementos.agregar(
                    this.obtener(i)
            );

            i++;
        }

        return resultado;
    }

    // =========================================================
    // SUBCONJUNTO
    // =========================================================

    @Override
    public boolean esSubconjuntoDe(TDAConjunto<T> otro) {

        if (otro == null) {
            return false;
        }

        int i = 0;
        int j = 0;

        while (i < this.tamaño() && j < otro.tamaño()) {

            T elementoA = this.obtener(i);
            T elementoB = otro.obtener(j);

            int comparacion = comparator.compare(
                    elementoA,
                    elementoB
            );

            if (comparacion < 0) {

                // A tiene un elemento que B no tiene.
                return false;

            } else if (comparacion > 0) {

                j++;

            } else {

                i++;
                j++;
            }
        }

        return i == this.tamaño();
    }
}