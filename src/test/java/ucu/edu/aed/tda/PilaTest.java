package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.NoSuchElementException;

public class PilaTest extends TestCase {

    public PilaTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(PilaTest.class);
    }

    // =========================================================
    // PILA NUEVA
    // =========================================================

    public void testPilaNuevaEstaVacia() {
        Pila<Integer> pila = new Pila<Integer>();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    // =========================================================
    // METER ELEMENTOS
    // =========================================================

    public void testMete() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);

        assertEquals(1, pila.tamaño());
        assertTrue(pila.contiene(10));
        assertEquals(10, pila.tope().intValue());
    }

    public void testMeteVariosElementos() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(3, pila.tamaño());
        assertEquals(30, pila.tope().intValue());
    }

    // =========================================================
    // TOPE
    // =========================================================

    public void testTopeNoRemueveElemento() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);

        assertEquals(20, pila.tope().intValue());
        assertEquals(2, pila.tamaño());
        assertTrue(pila.contiene(20));
    }

    public void testTopePilaVacia() {
        Pila<Integer> pila = new Pila<Integer>();

        try {
            pila.tope();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // SACAR
    // =========================================================

    public void testSaca() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);

        assertEquals(20, pila.saca().intValue());
        assertEquals(1, pila.tamaño());
        assertEquals(10, pila.tope().intValue());
    }

    public void testSacaRespetaLIFO() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(30, pila.saca().intValue());
        assertEquals(20, pila.saca().intValue());
        assertEquals(10, pila.saca().intValue());

        assertTrue(pila.esVacio());
    }

    public void testSacaPilaVacia() {
        Pila<Integer> pila = new Pila<Integer>();

        try {
            pila.saca();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    public void testSacarUltimoElementoDejaPilaVacia() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);

        assertEquals(10, pila.saca().intValue());

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    // =========================================================
    // OPERACIONES HEREDADAS DE LISTA
    // =========================================================

    public void testAgregar() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.agregar(10);
        pila.agregar(20);

        assertEquals(2, pila.tamaño());
        assertEquals(20, pila.tope().intValue());
    }

    public void testAgregarPorIndice() {
    Pila<Integer> pila = new Pila<Integer>();

    pila.mete(10);
    pila.mete(30);

    pila.agregar(1, 20);

    assertEquals(3, pila.tamaño());
    assertEquals(30, pila.obtener(0).intValue());
    assertEquals(20, pila.obtener(1).intValue());
    assertEquals(10, pila.obtener(2).intValue());
}

    public void testObtener() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(30, pila.obtener(0).intValue());
        assertEquals(20, pila.obtener(1).intValue());
        assertEquals(10, pila.obtener(2).intValue());
    }

    public void testIndiceDe() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(2, pila.indiceDe(10));
        assertEquals(1, pila.indiceDe(20));
        assertEquals(0, pila.indiceDe(30));
        assertEquals(-1, pila.indiceDe(40));
    }

    public void testContiene() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);

        assertTrue(pila.contiene(10));
        assertTrue(pila.contiene(20));
        assertFalse(pila.contiene(30));
    }

    public void testRemoverPorElemento() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertTrue(pila.remover(Integer.valueOf(20)));

        assertEquals(2, pila.tamaño());
        assertFalse(pila.contiene(20));
        assertEquals(30, pila.tope().intValue());
    }

    public void testRemoverPorIndice() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(20, pila.remover(1).intValue());

        assertEquals(2, pila.tamaño());
        assertEquals(30, pila.obtener(0).intValue());
        assertEquals(10, pila.obtener(1).intValue());
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        pila.vaciar();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    // =========================================================
    // BUSCAR
    // =========================================================

    public void testBuscar() {
        Pila<Integer> pila = new Pila<Integer>();

        pila.mete(10);
        pila.mete(20);
        pila.mete(30);

        assertEquals(20, pila.buscar(x -> x == 20).intValue());
    }
}