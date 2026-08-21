package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListaDobleTest extends TestCase {

    public ListaDobleTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ListaDobleTest.class);
    }

    // =========================================================
    // LISTA NUEVA
    // =========================================================

    public void testListaNuevaEstaVacia() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregar() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarEnPrimeraPosicion() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.agregar(0, 5);

        assertEquals(3, lista.tamaño());
        assertEquals(5, lista.obtener(0).intValue());
        assertEquals(10, lista.obtener(1).intValue());
        assertEquals(20, lista.obtener(2).intValue());
    }

    public void testAgregarEnMedio() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarAlFinalPorIndice() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.agregar(2, 30);

        assertEquals(3, lista.tamaño());
        assertEquals(30, lista.obtener(2).intValue());
    }

    // =========================================================
    // OBTENER
    // =========================================================

    public void testObtener() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    // =========================================================
    // REMOVER
    // =========================================================

    public void testRemoverPrimero() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.remover(0).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(20, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverMedio() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(20, lista.remover(1).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverUltimo() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(30, lista.remover(2).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
    }

    public void testRemoverPorElemento() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertTrue(lista.remover(Integer.valueOf(20)));

        assertEquals(2, lista.tamaño());
        assertFalse(lista.contiene(20));
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverElementoInexistente() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);

        assertFalse(lista.remover(Integer.valueOf(50)));
        assertEquals(2, lista.tamaño());
    }

    // =========================================================
    // BUSQUEDA
    // =========================================================

    public void testIndiceDe() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(0, lista.indiceDe(10));
        assertEquals(1, lista.indiceDe(20));
        assertEquals(2, lista.indiceDe(30));
        assertEquals(-1, lista.indiceDe(40));
    }

    public void testContiene() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertTrue(lista.contiene(20));
        assertFalse(lista.contiene(30));
    }

    public void testBuscar() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(20, lista.buscar(x -> x == 20).intValue());
    }

    // =========================================================
    // CASOS BORDE
    // =========================================================

    public void testUnSoloElemento() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);

        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(10, lista.remover(0).intValue());

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testVaciar() {
        ListaDoble<Integer> lista = new ListaDoble<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }
}