package ucu.edu.aed.tda;

import java.util.Comparator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConjuntoTest extends TestCase {

    public ConjuntoTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ConjuntoTest.class);
    }

    private Conjunto<Integer> crearConjunto(Integer... elementos) {
        Conjunto<Integer> conjunto =
                new Conjunto<Integer>(Comparator.naturalOrder());

        for (Integer elemento : elementos) {
            conjunto.agregar(elemento);
        }

        return conjunto;
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregarElementos() {
        Conjunto<Integer> conjunto = crearConjunto();

        conjunto.agregar(5);
        conjunto.agregar(2);
        conjunto.agregar(8);

        assertEquals(3, conjunto.tamaño());
        assertTrue(conjunto.contiene(5));
        assertTrue(conjunto.contiene(2));
        assertTrue(conjunto.contiene(8));
    }

    public void testNoPermiteDuplicados() {
        Conjunto<Integer> conjunto = crearConjunto();

        conjunto.agregar(5);
        conjunto.agregar(5);
        conjunto.agregar(5);

        assertEquals(1, conjunto.tamaño());
        assertTrue(conjunto.contiene(5));
    }

    // =========================================================
    // CONTIENE
    // =========================================================

    public void testContieneElementoExistente() {
        Conjunto<Integer> conjunto = crearConjunto(1, 3, 5);

        assertTrue(conjunto.contiene(3));
    }

    public void testNoContieneElementoInexistente() {
        Conjunto<Integer> conjunto = crearConjunto(1, 3, 5);

        assertFalse(conjunto.contiene(7));
    }

    // =========================================================
    // REMOVER
    // =========================================================

    public void testRemoverElementoExistente() {
        Conjunto<Integer> conjunto = crearConjunto(1, 3, 5);

        assertTrue(conjunto.remover(Integer.valueOf(3)));

        assertFalse(conjunto.contiene(3));
        assertEquals(2, conjunto.tamaño());
    }

    public void testRemoverElementoInexistente() {
        Conjunto<Integer> conjunto = crearConjunto(1, 3, 5);

        assertFalse(conjunto.remover(Integer.valueOf(7)));

        assertEquals(3, conjunto.tamaño());
    }

    // =========================================================
    // UNION
    // =========================================================

    public void testUnion() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5, 7);
        Conjunto<Integer> b = crearConjunto(2, 3, 6, 7);

        TDAConjunto<Integer> resultado = a.union(b);

        assertEquals(6, resultado.tamaño());

        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(2));
        assertTrue(resultado.contiene(3));
        assertTrue(resultado.contiene(5));
        assertTrue(resultado.contiene(6));
        assertTrue(resultado.contiene(7));
    }

    public void testUnionConjuntoVacio() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto();

        TDAConjunto<Integer> resultado = a.union(b);

        assertEquals(3, resultado.tamaño());

        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(3));
        assertTrue(resultado.contiene(5));
    }

    public void testUnionNoModificaOriginales() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto(3, 7);

        a.union(b);

        assertEquals(3, a.tamaño());
        assertEquals(2, b.tamaño());
    }

    // =========================================================
    // INTERSECCIÓN
    // =========================================================

    public void testInterseccion() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5, 7);
        Conjunto<Integer> b = crearConjunto(2, 3, 6, 7);

        TDAConjunto<Integer> resultado = a.interseccion(b);

        assertEquals(2, resultado.tamaño());

        assertTrue(resultado.contiene(3));
        assertTrue(resultado.contiene(7));
    }

    public void testInterseccionSinElementosComunes() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto(2, 4, 6);

        TDAConjunto<Integer> resultado = a.interseccion(b);

        assertTrue(resultado.esVacio());
    }

    public void testInterseccionConVacio() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto();

        TDAConjunto<Integer> resultado = a.interseccion(b);

        assertTrue(resultado.esVacio());
    }

    // =========================================================
    // DIFERENCIA
    // =========================================================

    public void testDiferencia() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5, 7);
        Conjunto<Integer> b = crearConjunto(3, 7);

        TDAConjunto<Integer> resultado = a.diferencia(b);

        assertEquals(2, resultado.tamaño());

        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(5));
    }

    public void testDiferenciaConVacio() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto();

        TDAConjunto<Integer> resultado = a.diferencia(b);

        assertEquals(3, resultado.tamaño());
    }

    public void testDiferenciaDeConjuntoIgual() {
        Conjunto<Integer> a = crearConjunto(1, 3, 5);
        Conjunto<Integer> b = crearConjunto(1, 3, 5);

        TDAConjunto<Integer> resultado = a.diferencia(b);

        assertTrue(resultado.esVacio());
    }

    // =========================================================
    // SUBCONJUNTO
    // =========================================================

    public void testEsSubconjunto() {
        Conjunto<Integer> a = crearConjunto(3, 5);
        Conjunto<Integer> b = crearConjunto(1, 3, 5, 7);

        assertTrue(a.esSubconjuntoDe(b));
    }

    public void testNoEsSubconjunto() {
        Conjunto<Integer> a = crearConjunto(3, 8);
        Conjunto<Integer> b = crearConjunto(1, 3, 5, 7);

        assertFalse(a.esSubconjuntoDe(b));
    }

    public void testVacioEsSubconjunto() {
        Conjunto<Integer> a = crearConjunto();
        Conjunto<Integer> b = crearConjunto(1, 2, 3);

        assertTrue(a.esSubconjuntoDe(b));
    }

    // =========================================================
    // CASOS BORDE
    // =========================================================

    public void testConjuntoNuevoEstaVacio() {
        Conjunto<Integer> conjunto =
                new Conjunto<Integer>(Comparator.naturalOrder());

        assertTrue(conjunto.esVacio());
        assertEquals(0, conjunto.tamaño());
    }

    public void testVaciar() {
        Conjunto<Integer> conjunto = crearConjunto(1, 2, 3);

        conjunto.vaciar();

        assertTrue(conjunto.esVacio());
        assertEquals(0, conjunto.tamaño());
    }
}