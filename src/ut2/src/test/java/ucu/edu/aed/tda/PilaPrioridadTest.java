package ucu.edu.aed.tda;

import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PilaPrioridadTest extends TestCase {

    public PilaPrioridadTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(PilaPrioridadTest.class);
    }

    // =========================================================
    // PILA NUEVA
    // =========================================================

    public void testPilaNuevaEstaVacia() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    // =========================================================
    // UN ELEMENTO
    // =========================================================

    public void testAgregarUnElemento() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("A", 1);

        assertFalse(pila.esVacio());
        assertEquals(1, pila.tamaño());
        assertEquals("A", pila.tope());
    }

    // =========================================================
    // PRIORIDADES
    // =========================================================

    public void testRespetaPrioridad() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("Normal", 3);
        pila.mete("Urgente", 1);
        pila.mete("Importante", 2);

        assertEquals("Urgente", pila.saca());
        assertEquals("Importante", pila.saca());
        assertEquals("Normal", pila.saca());
    }

    // =========================================================
    // MISMA PRIORIDAD -> LIFO
    // =========================================================

    public void testMismaPrioridadRespetaLIFO() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("A", 1);
        pila.mete("B", 1);
        pila.mete("C", 1);

        assertEquals("C", pila.saca());
        assertEquals("B", pila.saca());
        assertEquals("A", pila.saca());
    }

    // =========================================================
    // TOPE NO REMUEVE
    // =========================================================

    public void testTopeNoRemueve() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("A", 1);
        pila.mete("B", 2);

        assertEquals("A", pila.tope());
        assertEquals(2, pila.tamaño());
        assertEquals("A", pila.tope());
    }

    // =========================================================
    // SACAR HASTA VACIAR
    // =========================================================

    public void testSacarDejaPilaVacia() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("A", 1);

        assertEquals("A", pila.saca());

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }

    // =========================================================
    // PILA VACIA
    // =========================================================

    public void testTopePilaVacia() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        try {
            pila.tope();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    public void testSacaPilaVacia() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        try {
            pila.saca();
            fail("Se esperaba NoSuchElementException");
        } catch (NoSuchElementException e) {
            // comportamiento esperado
        }
    }

    // =========================================================
    // VACIAR
    // =========================================================

    public void testVaciar() {
        PilaPrioridad<String> pila = new PilaPrioridad<>();

        pila.mete("A", 1);
        pila.mete("B", 2);

        pila.vaciar();

        assertTrue(pila.esVacio());
        assertEquals(0, pila.tamaño());
    }
}