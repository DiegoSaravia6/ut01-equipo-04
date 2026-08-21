package ucu.edu.aed.tda;

import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ColaPrioridadTest extends TestCase {

    public ColaPrioridadTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ColaPrioridadTest.class);
    }

    // =========================================================
    // COLA NUEVA
    // =========================================================

    public void testColaNuevaEstaVacia() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    // =========================================================
    // UN ELEMENTO
    // =========================================================

    public void testAgregarUnElemento() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("A", 1);

        assertFalse(cola.esVacio());
        assertEquals(1, cola.tamaño());
        assertEquals("A", cola.frente());
    }

    // =========================================================
    // PRIORIDADES
    // =========================================================

    public void testRespetaPrioridad() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("Normal", 3);
        cola.poneEnCola("Urgente", 1);
        cola.poneEnCola("Importante", 2);

        assertEquals("Urgente", cola.quitaDeCola());
        assertEquals("Importante", cola.quitaDeCola());
        assertEquals("Normal", cola.quitaDeCola());
    }

    // =========================================================
    // MISMA PRIORIDAD -> FIFO
    // =========================================================

    public void testMismaPrioridadRespetaFIFO() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("A", 1);
        cola.poneEnCola("B", 1);
        cola.poneEnCola("C", 1);

        assertEquals("A", cola.quitaDeCola());
        assertEquals("B", cola.quitaDeCola());
        assertEquals("C", cola.quitaDeCola());
    }

    // =========================================================
    // FRENTE NO REMUEVE
    // =========================================================

    public void testFrenteNoRemueve() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("A", 1);
        cola.poneEnCola("B", 2);

        assertEquals("A", cola.frente());
        assertEquals(2, cola.tamaño());
        assertEquals("A", cola.frente());
    }

    // =========================================================
    // QUITAR HASTA VACIAR
    // =========================================================

    public void testQuitarDejaColaVacia() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("A", 1);

        assertEquals("A", cola.quitaDeCola());

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }

    // =========================================================
    // COLA VACIA
    // =========================================================

    public void testFrenteColaVacia() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        try {
            cola.frente();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    public void testQuitaColaVacia() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        try {
            cola.quitaDeCola();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        ColaPrioridad<String> cola = new ColaPrioridad<>();

        cola.poneEnCola("A", 1);
        cola.poneEnCola("B", 2);

        cola.vaciar();

        assertTrue(cola.esVacio());
        assertEquals(0, cola.tamaño());
    }
}