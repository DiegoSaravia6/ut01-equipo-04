package ucu.edu.aed.tda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ListaCircularTest extends TestCase {

    public ListaCircularTest(String nombre) {
        super(nombre);
    }

    public static Test suite() {
        return new TestSuite(ListaCircularTest.class);
    }

    // =========================================================
    // LISTA NUEVA
    // =========================================================

    public void testListaNuevaEstaVacia() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    // =========================================================
    // AGREGAR
    // =========================================================

    public void testAgregar() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarAlPrincipio() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);

        lista.agregar(0, 5);

        assertEquals(3, lista.tamaño());
        assertEquals(5, lista.obtener(0).intValue());
        assertEquals(10, lista.obtener(1).intValue());
        assertEquals(20, lista.obtener(2).intValue());
    }

    public void testAgregarEnMedio() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(30);

        lista.agregar(1, 20);

        assertEquals(3, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
        assertEquals(30, lista.obtener(2).intValue());
    }

    public void testAgregarAlFinalPorIndice() {
        ListaCircular<Integer> lista = new ListaCircular<>();

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
        ListaCircular<Integer> lista = new ListaCircular<>();

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
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.remover(0).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(20, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverMedio() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(20, lista.remover(1).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(30, lista.obtener(1).intValue());
    }

    public void testRemoverUltimo() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(30, lista.remover(2).intValue());

        assertEquals(2, lista.tamaño());
        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(20, lista.obtener(1).intValue());
    }

    public void testRemoverPorElemento() {
        ListaCircular<Integer> lista = new ListaCircular<>();

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
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);

        assertFalse(lista.remover(Integer.valueOf(50)));
        assertEquals(2, lista.tamaño());
    }

    // =========================================================
    // BUSQUEDA
    // =========================================================

    public void testIndiceDe() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(0, lista.indiceDe(10));
        assertEquals(1, lista.indiceDe(20));
        assertEquals(2, lista.indiceDe(30));
        assertEquals(-1, lista.indiceDe(40));
    }

    public void testContiene() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);

        assertTrue(lista.contiene(10));
        assertTrue(lista.contiene(20));
        assertFalse(lista.contiene(30));
    }

    public void testBuscar() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(20, lista.buscar(x -> x == 20).intValue());
    }

    // =========================================================
    // CASOS BORDE
    // =========================================================

    public void testUnSoloElemento() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);

        assertEquals(10, lista.obtener(0).intValue());
        assertEquals(10, lista.remover(0).intValue());

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }

    public void testVaciar() {
        ListaCircular<Integer> lista = new ListaCircular<>();

        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        lista.vaciar();

        assertTrue(lista.esVacio());
        assertEquals(0, lista.tamaño());
    }
}