package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.NoSuchElementException;

public class ColaTest extends TestCase {

    public ColaTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ColaTest.class);
    }

    // =========================================================
    // COLA NUEVA
    // =========================================================

    public void testColaNuevaEstaVacia() {
        Cola<Integer> cola = new Cola<Integer>();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    // =========================================================
    // PONER EN COLA
    // =========================================================

    public void testPoneEnCola() {
        Cola<Integer> cola = new Cola<Integer>();

        assertTrue(cola.poneEnCola(10));

        assertEquals(1, cola.tamaño());
        assertTrue(cola.contiene(10));
        assertEquals(10, cola.frente().intValue());
    }

    public void testPoneVariosElementos() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(3, cola.tamaño());
        assertEquals(10, cola.frente().intValue());
    }

    // =========================================================
    // FRENTE
    // =========================================================

    public void testFrenteNoRemueveElemento() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(10, cola.frente().intValue());
        assertEquals(2, cola.tamaño());
        assertTrue(cola.contiene(10));
    }

    public void testFrenteColaVacia() {
        Cola<Integer> cola = new Cola<Integer>();

        try {
            cola.frente();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // QUITAR DE COLA
    // =========================================================

    public void testQuitaDeCola() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertEquals(10, cola.quitaDeCola().intValue());
        assertEquals(1, cola.tamaño());
        assertEquals(20, cola.frente().intValue());
    }

    public void testQuitaDeColaRespetaFIFO() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(10, cola.quitaDeCola().intValue());
        assertEquals(20, cola.quitaDeCola().intValue());
        assertEquals(30, cola.quitaDeCola().intValue());

        assertTrue(cola.esVacio());
    }

    public void testQuitaDeColaColaVacia() {
        Cola<Integer> cola = new Cola<Integer>();

        try {
            cola.quitaDeCola();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    public void testQuitarUltimoElementoDejaColaVacia() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);

        assertEquals(10, cola.quitaDeCola().intValue());

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    // =========================================================
    // OPERACIONES HEREDADAS DE LISTA
    // =========================================================

    public void testObtener() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(10, cola.obtener(0).intValue());
        assertEquals(20, cola.obtener(1).intValue());
        assertEquals(30, cola.obtener(2).intValue());
    }

    public void testIndiceDe() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(0, cola.indiceDe(10));
        assertEquals(1, cola.indiceDe(20));
        assertEquals(2, cola.indiceDe(30));
        assertEquals(-1, cola.indiceDe(40));
    }

    public void testContiene() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);

        assertTrue(cola.contiene(10));
        assertTrue(cola.contiene(20));
        assertFalse(cola.contiene(30));
    }

    public void testRemoverPorElemento() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertTrue(cola.remover(Integer.valueOf(20)));

        assertEquals(2, cola.tamaño());
        assertFalse(cola.contiene(20));
        assertEquals(10, cola.frente().intValue());
    }

    public void testRemoverPorIndice() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(20, cola.remover(1).intValue());

        assertEquals(2, cola.tamaño());
        assertEquals(10, cola.obtener(0).intValue());
        assertEquals(30, cola.obtener(1).intValue());
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        cola.vaciar();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    // =========================================================
    // BUSCAR
    // =========================================================

    public void testBuscar() {
        Cola<Integer> cola = new Cola<Integer>();

        cola.poneEnCola(10);
        cola.poneEnCola(20);
        cola.poneEnCola(30);

        assertEquals(20, cola.buscar(x -> x == 20).intValue());
    }
}